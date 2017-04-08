/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
 Alexandre Robin <robin@nsstc.uah.edu>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * <p>
 * This class allows to keep track of what classes to use
 * to read/write different versions of service requests as well
 * as other (mostly xml) messages and documents. This class
 * obtains the default mappings from the OGCRegistry.xml file. 
 * </p>
 *
 * @author Alex Robin
 * @since Jan 16, 2007
 * */
public class OGCRegistry
{
    public static final String XLINK = "XLINK";
    protected static final String DEFAULT_OWS_VERSION = "1.0";
    protected static final Pattern VERSION_NORMALIZE_PATTERN = Pattern.compile("(\\.0)+$");
    protected static Map<String, String> readerClasses;
    protected static Map<String, String> writerClasses;
    protected static Map<String, String> namespaces;
    protected static Map<String, String> owsVersions;
    protected static final Logger log = LoggerFactory.getLogger(OGCRegistry.class);
    

    static
    {
        readerClasses = new HashMap<String, String>();
        writerClasses = new HashMap<String, String>();
        namespaces = new HashMap<String, String>();
        owsVersions = new HashMap<String, String>();
        String mapFileUrl = OGCRegistry.class.getResource("OGCRegistry.xml").toString();
        loadMaps(mapFileUrl, false);
    }


    /**
     * Computes namespace URI for the specified OGC spec and version
     * @param spec
     * @param version
     * @return complete namespace URI
     */
    public static String getNamespaceURI(String spec, String version)
    {
        String nsUri = null;
        
    	if (version != null)
            nsUri = namespaces.get(spec + "_" + normalizeVersionString(version));
        else
        	nsUri = namespaces.get(spec);
                	
        if (nsUri == null)
        	throw new IllegalStateException("Unsupported Specification: " + spec + " v" + version);
    	
    	return nsUri;
    }


    /**
     * Computes namespace URI for the specified OGC spec
     * @param spec
     * @return complete namespace URI
     */
    public static String getNamespaceURI(String spec)
    {
        return getNamespaceURI(spec, null);
    }
    
    
    /**
     * Retrieves OWS version for given OGC spec
     * @param spec
     * @param version
     * @return version string
     */
    public static String getOWSVersion(String spec, String version)
    {
    	String owsVersion = owsVersions.get(spec + "_" + normalizeVersionString(version));
    	
    	if (owsVersion == null)
    	    return DEFAULT_OWS_VERSION;
    	
    	return owsVersion;
    }


    /**
     * Instantiates a reader object for the specified content type, subtype and version
     * @param type
     * @param subType
     * @param version
     * @return reader instance
     * @throws IllegalStateException
     */
    public static Object createReader(String type, String subType, String version) throws IllegalStateException
    {
        return createObject(readerClasses, type, subType, version);
    }


    /**
     * Instantiates a reader object for the specified content type and version
     * @param type
     * @param version
     * @return reader instance
     * @throws IllegalStateException
     */
    public static Object createReader(String type, String version) throws IllegalStateException
    {
        return createObject(readerClasses, type, null, version);
    }


    /**
     * Instantiates a writer object for the specified content type, subtype and version
     * @param type
     * @param subType
     * @param version
     * @return writer instance
     * @throws IllegalStateException
     */
    public static Object createWriter(String type, String subType, String version) throws IllegalStateException
    {
        return createObject(writerClasses, type, subType, version);
    }


    /**
     * Instantiates a writer object for the specified content type and version
     * @param type
     * @param version
     * @return writer instance
     * @throws IllegalStateException
     */
    public static Object createWriter(String type, String version) throws IllegalStateException
    {
        return createObject(writerClasses, type, null, version);
    }


    /**
     * Registers a reader class for given content type and version
     * @param type
     * @param subType
     * @param version
     * @param className
     * @throws IllegalStateException
     */
    public static void addReaderClass(String type, String subType, String version, String className) throws IllegalStateException
    {
        addClass(readerClasses, type, subType, version, className);
    }


    /**
     * Registers a writer class for given content type and version
     * @param type
     * @param subType
     * @param version
     * @param className
     * @throws IllegalStateException
     */
    public static void addWriterClass(String type, String subType, String version, String className) throws IllegalStateException
    {
        addClass(writerClasses, type, subType, version, className);
    }


    /**
     * Handles registration of reader/writer classes into the tables
     * @param table
     * @param type
     * @param subType
     * @param version
     * @param className
     */
    private static void addClass(Map<String, String> table, String type, String subType, String version, String className)
    {
        type = normalizeTypeString(type);
        subType = normalizeSubtypeString(subType);
        version = normalizeVersionString(version);
        StringBuffer key = new StringBuffer();

        if (type != null)
            key.append(type);

        if (subType != null)
        {
            key.append('_');
            key.append(subType);
        }

        if (version != null)
        {
            key.append('_');
            key.append(version);
        }

        // store class name in table
        table.put(key.toString(), className);
    }


    /**
     * Handles the retrieval of class objects from tables
     * @param table
     * @param type
     * @param subType
     * @param version
     * @return
     * @throws IllegalStateException
     */
    private static String getClassName(Map<String, String> table, String type, String subType, String version) throws IllegalStateException
    {
        String ioClass;
        StringBuffer key;
        type = normalizeTypeString(type);
        subType = normalizeSubtypeString(subType);
        version = normalizeVersionString(version);

        // first try to retrieve exactly what's asked
        key = new StringBuffer(type);
        if (subType != null)
        {
            key.append('_');
            key.append(subType);
        }
        if (version != null)
        {
            key.append('_');
            key.append(version);
        }
        ioClass = table.get(key.toString());
        if (ioClass != null)
            return ioClass;

        // if not found, try to find one without subType
        key = new StringBuffer(type);
        if (version != null)
        {
            key.append('_');
            key.append(version);
        }
        ioClass = table.get(key.toString());
        if (ioClass != null)
            return ioClass;

        // if not found, try to find one without version
        key = new StringBuffer(type);
        if (subType != null)
        {
            key.append('_');
            key.append(subType);
        }
        ioClass = table.get(key.toString());
        if (ioClass != null)
            return ioClass;

        // if not found, try to find one without type
        key = new StringBuffer();
        if (subType != null)
        {
            key.append('_');
            key.append(subType);
        }
        if (version != null)
        {
            key.append('_');
            key.append(version);
        }
        ioClass = table.get(key.toString());
        if (ioClass != null)
            return ioClass;

        // if not found, try to find one without version nor subType
        ioClass = table.get(type);
        if (ioClass != null)
            return ioClass;

        // if not found, try to find one without version nor type
        ioClass = table.get("_" + subType);
        if (ioClass != null)
            return ioClass;

        throw new IllegalStateException("No reader/writer found for " + type + "/" + subType + " v" + version);
    }


    /**
     * Handles the instantiation of reader/writer classes
     * @param table
     * @param type
     * @param subType
     * @param version
     * @return
     */
    private static Object createObject(Map<String, String> table, String type, String subType, String version) throws IllegalStateException
    {
        String className = getClassName(table, type, subType, version);
        Class<?> objClass;
        
        // load class
        try
        {
            objClass = Class.forName(className);
        }
        catch (ClassNotFoundException e)
        {
            throw new IllegalStateException("Error while registering reader/writer Class " + className, e);
        }
        
        // instantiate using reflection
        try
        {
            Object obj = objClass.newInstance();
            return obj;
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Error while instantiating new reader/writer", e);
        }
    }


    /**
     * Loads an xml file containing mappings from types of readers/writers to class
     * @param xmlFileUrl
     * @param replace
     */
    public static void loadMaps(String xmlFileUrl, boolean replace)
    {
        try
        {
            // open mappings file
            DOMHelper dom = new DOMHelper(xmlFileUrl, false);

            // clear hashtables if requested
            if (replace)
            {
                readerClasses.clear();
                writerClasses.clear();
            }

            // add namespace hashtable entries
            NodeList namespaceElts = dom.getElements("Namespace");
            for (int i = 0; i < namespaceElts.getLength(); i++)
            {
                Element nsElt = (Element) namespaceElts.item(i);
                String type = dom.getAttributeValue(nsElt, "type");
                String version = dom.getAttributeValue(nsElt, "version");
                version = normalizeVersionString(version);
                String uri = dom.getAttributeValue(nsElt, "uri");
                String owsVersion = dom.getAttributeValue(nsElt, "ows");
                
                String spec;
                if (version != null)
                	spec = type + "_" + version;
                else
                	spec = type;
                
                namespaces.put(spec, uri);
                
                if (owsVersion != null)
                	owsVersions.put(spec, owsVersion);
            }            

            // add reader hashtable entries
            NodeList readerElts = dom.getElements("Reader");
            for (int i = 0; i < readerElts.getLength(); i++)
            {
                Element readerElt = (Element) readerElts.item(i);
                String type = dom.getAttributeValue(readerElt, "type");
                String subType = dom.getAttributeValue(readerElt, "subType");
                String version = dom.getAttributeValue(readerElt, "version");
                String className = dom.getAttributeValue(readerElt, "class");
                addClass(readerClasses, type, subType, version, className);
            }

            // add writer hashtable entries
            NodeList writerElts = dom.getElements("Writer");
            for (int i = 0; i < writerElts.getLength(); i++)
            {
                Element writerElt = (Element) writerElts.item(i);
                String type = dom.getAttributeValue(writerElt, "type");
                String subType = dom.getAttributeValue(writerElt, "subType");
                String version = dom.getAttributeValue(writerElt, "version");
                String className = dom.getAttributeValue(writerElt, "class");
                addClass(writerClasses, type, subType, version, className);
            }            
        }
        catch (DOMHelperException e)
        {
            throw new IllegalStateException("Invalid OWSRegistry mapping file", e);
        }
    }


    private static String normalizeTypeString(String type)
    {
        if (type != null && type.length() > 0 && !type.equalsIgnoreCase("*"))
            return type.toUpperCase();
        else
            return null;
    }


    private static String normalizeSubtypeString(String subType)
    {
        if (subType != null && subType.length() > 0 && !subType.equalsIgnoreCase("*"))
            return subType.toUpperCase();
        else
            return null;
    }


    public static String normalizeVersionString(String version)
    {
        if (version != null && version.length() > 0 && !version.equalsIgnoreCase("*"))
        {
            version = VERSION_NORMALIZE_PATTERN.matcher(version).replaceAll("");
            return version;
        }
        else
            return null;
    }


    /**
     * Provides direct access to the namespaceBuilders hashtable
     * @return map of spec to namespace URIs
     */
    public static Map<String, String> getNamespaces()
    {
        return namespaces;
    }
}
