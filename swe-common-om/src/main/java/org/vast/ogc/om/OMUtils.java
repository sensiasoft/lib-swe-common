/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.om;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;
import org.vast.ogc.OGCRegistry;
import org.vast.xml.DOMHelper;
import org.vast.xml.IXMLReaderDOM;
import org.vast.xml.IXMLWriterDOM;
import org.vast.xml.XMLReaderException;
import org.vast.xml.XMLWriterException;
import org.w3c.dom.Element;


/**
 * <p>
 * Provides Helper methods to read and write O&M observations
 * in a version agnostic manner.
 * </p>
 *
 * @author Alex Robin
 * @since Feb 22, 2007
 * */
public class OMUtils
{
    public final static String OM;
    public final static String V2_0 = "2.0";
    public final static String OBSERVATION = "Observation";
    protected final static Pattern versionRegex = Pattern.compile("^\\d+(\\.\\d+)?(\\.\\d+)?$");
    protected final static String defaultVersion = "1.0";

    
    static 
    {
        OM = "OM";
        loadRegistry();
    }
    
    
    public static void loadRegistry()
    {
        String mapFileUrl = OMUtils.class.getResource("OMRegistry.xml").toString();
        OGCRegistry.loadMaps(mapFileUrl, false);
    }
    
    
    public OMUtils(String version)
    {
    }
    
    
    /**
     * Read an O&M observation object from a DOM element 
     * @param dom DOMHelper to use to read the element content
     * @param obsElt DOM element corresponding to the Observation
     * @return Concrete instance of IObservation containing information parsed from the DOM tree
     * @throws XMLReaderException
     */
    public IObservation readObservation(DOMHelper dom, Element obsElt) throws XMLReaderException
    {
        String version = getVersion(dom, obsElt);
        IXMLReaderDOM<IObservation> reader = (IXMLReaderDOM<IObservation>)OGCRegistry.createReader(OM, OBSERVATION, version);
        return reader.read(dom, obsElt);
    }
    
    
    /**
     * Reads an O&M observation object from an InputStream
     * @param inputStream input stream to read from
     * @return Concrete instance of IObservation containing information parsed from XML
     * @throws XMLReaderException
     */
    public IObservation readObservation(InputStream inputStream) throws XMLReaderException
    {
        try
        {
            DOMHelper dom = new DOMHelper(inputStream, false);
            return readObservation(dom, dom.getBaseElement());
        }
        catch (XMLReaderException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new XMLReaderException("Error while reading inputstream", e);
        }
    }
    
    
    /**
     * Builds a DOM Element from the content of the IObservation object
     * @param dom DOMHelper used to generate the element
     * @param obs Observation instance whose content will be serialized
     * @param version Version of O&M schema to use
     * @return New DOM Element
     * @throws XMLWriterException
     */
    public Element writeObservation(DOMHelper dom, IObservation obs, String version) throws XMLWriterException
    {
        IXMLWriterDOM<IObservation> writer = (IXMLWriterDOM<IObservation>)OGCRegistry.createWriter(OM, OBSERVATION, version);
        return writer.write(dom, obs);
    }
    
    
    /**
     * Writes XML for an Observation of selected version in the specified OutputStream
     * @param outputStream Outputstream to write the XML into
     * @param obs Observation instance whose content will be serialized
     * @param version Version of O&M schema to use
     * @throws XMLWriterException
     * @throws IOException 
     */
    public void writeObservation(OutputStream outputStream, IObservation obs, String version) throws XMLWriterException, IOException
    {
        DOMHelper dom = new DOMHelper("obs");
        Element obsElt = writeObservation(dom, obs, version);
        dom.serialize(obsElt, outputStream, true);
    }

    
    /**
     * Logic to guess O&M version from namespace
     * @param dom
     * @param omElt 
     * @return Version string
     */
    public String getVersion(DOMHelper dom, Element omElt)
    {
        // get version from the last part of namespace URI
        String omUri = omElt.getNamespaceURI();
        String version = omUri.substring(omUri.lastIndexOf('/') + 1);
        
        // check if version is a valid version number otherwise use default
        if (!versionRegex.matcher(version).matches())
            version = defaultVersion;
        
        return version;
    }
}
