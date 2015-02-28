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

package org.vast.codec;

import java.util.Hashtable;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p>
 * This class allows to keep track of what classes to use
 * to decompress streams, e.g. JPEG, JPEG2K... 
 * </p>
 *
 * @author Gregoire Berthiau
 * @since Sep 15, 2008
 * */
public class DecompressionRegistry
{
    protected static Hashtable<String, Class<?>> readerClasses;

    
    static
    {
        readerClasses = new Hashtable<String, Class<?>>();
        String mapFileUrl = DecompressionRegistry.class.getResource("CodecRegistry.xml").toString();
        loadMaps(mapFileUrl, false);
    }

    /**
     * Instantiates a reader object for the specified compression type
     * @param compressionType
     * @return new reader instance
     * @throws IllegalStateException
     */
    public static Object createReader(String compressionType) throws IllegalStateException
    {
        return createObject(readerClasses, compressionType);
    }


    /**
     * Registers a reader class for given compression type
     * @param compressionType
     * @param className
     * @throws IllegalStateException
     */
    public static void addReaderClass(String compressionType, String className) throws IllegalStateException
    {
        addClass(readerClasses, compressionType, className);
    }


    /**
     * Retrieves a registered reader class
     * @param compressionType
     * @return reader class or null if none found
     */
    public static Class<?> getReaderClass(String compressionType)
    {
        return getClass(readerClasses, compressionType);
    }


    /*
     * Handles registration of reader/writer classes into the tables
     */
    private static void addClass(Hashtable<String, Class<?>> table, String compressionType, String className) throws IllegalStateException
    {
    	compressionType = normalizeCompressionTypeString(compressionType);
        StringBuffer key = new StringBuffer();

        if (compressionType != null)
            key.append(compressionType);


        // try to instantiate corresponding class
        try
        {
            Class<?> ioClass = Class.forName(className);
            table.put(key.toString(), ioClass);
        }
        catch (ClassNotFoundException e)
        {
            // problem if class is not found in class path
            throw new IllegalStateException("Error while registering reader/writer Class " + className, e);
        }
    }


    /*
     * Handles the retrieval of class objects from tables
     */
    private static Class<?> getClass(Hashtable<String, Class<?>> table, String compressionType) throws IllegalStateException
    {
        Class<?> ioClass;
        StringBuffer key;
        compressionType = normalizeCompressionTypeString(compressionType);

        // first try to retrieve exactly what's asked
        key = new StringBuffer(compressionType);

        ioClass = table.get(key.toString());
        if (ioClass != null)
            return ioClass;

        throw new IllegalStateException("No reader found for " + compressionType);
    }


    /*
     * Handles the instantiation of reader/writer classes
     */
    private static Object createObject(Hashtable<String, Class<?>> table, String compressionType) throws IllegalStateException
    {
        Class<?> objClass = getClass(table, compressionType);

        // instantiate the reader class using reflection
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
            }

            // add reader hashtable entries
            NodeList readerElts = dom.getElements("Reader");
            for (int i = 0; i < readerElts.getLength(); i++)
            {
                Element readerElt = (Element) readerElts.item(i);
                String compressionType = dom.getAttributeValue(readerElt, "compressionType");
                String className = dom.getAttributeValue(readerElt, "class");

                try
                {
                    addClass(readerClasses, compressionType, className);
                }
                catch (IllegalStateException e)
                {
                    //ExceptionSystem.display(e);
                }
            }

        }
        catch (DOMHelperException e)
        {
            throw new IllegalStateException("Invalid OWSRegistry mapping file");
        }
    }


    private static String normalizeCompressionTypeString(String type)
    {
        if (type != null && type.length() > 0 && !type.equalsIgnoreCase("*"))
            return type.toUpperCase();
        else
            return null;
    }

}
