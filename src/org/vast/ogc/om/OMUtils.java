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

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vast.ogc.OGCRegistry;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b>
 * OM Utils
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides Helper methods to read and write O&M observations
 * in a version agnostic manner.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Feb 22, 2007
 * @version 1.0
 */
public class OMUtils
{
    public final static String OM = "OM";
    public final static String OBSERVATION = "Observation";
    protected final static int NS_CHARS = 500;
    protected final static String versionRegex = "^\\d+(\\.\\d+)?(\\.\\d+)?$";
    protected final static String namespaceRegex = "=\"http://www\\.opengis\\.net/om/[\\d\\.]*\\d?";
    protected final static String defaultVersion = "0.0.1";
    
    
    /**
     * Read an O&M observation object from a DOM element 
     * @param dom
     * @param obsElt
     * @return
     * @throws OMException
     */
    public AbstractObservation readObservation(DOMHelper dom, Element obsElt) throws OMException
    {
        String version = getVersion(dom, obsElt);
        ObservationReader reader = (ObservationReader)OGCRegistry.createReader(OM, OBSERVATION, version);
        return reader.readObservation(dom, obsElt);
    }
    
    
    /**
     * Reads an O&M observation object from an InputStream
     * @param is
     * @param version
     * @return
     * @throws OMException
     */
    public AbstractObservation readObservation(InputStream inputStream) throws OMException
    {
        try
        {
            String version = defaultVersion;           
            
            // buffer the stream with a buffer size of NS_CHARS+2 and mark beginning
            BufferedInputStream bufferedInput = new BufferedInputStream(inputStream, NS_CHARS+2);
            bufferedInput.mark(NS_CHARS+1);
                        
            // read first characters
            StringBuilder buf = new StringBuilder(NS_CHARS);
            for (int i=0; i<NS_CHARS; i++)
                buf.append((char)bufferedInput.read());
            
            // look for O&M namespace
            String text = buf.toString();
            Pattern pattern = Pattern.compile(namespaceRegex);
            Matcher matcher = pattern.matcher(text);
            
            // extract version from namespace
            if (matcher.find())
            {
                String match = matcher.group();
                version = match.substring(match.lastIndexOf('/') + 1);
                
                // check if version is a valid version number otherwise defaults to 0
                if (!version.matches(versionRegex))
                    version = defaultVersion;
            }
            
            // reset to mark for further processing
            bufferedInput.reset();
            
            // parse observation using appropariate reader for this version
            ObservationReader reader = (ObservationReader)OGCRegistry.createReader(OM, OBSERVATION, version);
            return reader.readObservation(bufferedInput);
            
            //DOMHelper dom = new DOMHelper (inputStream, false);
            //return readObservation(dom, dom.getBaseElement());
        }
        catch (Exception e)
        {
            throw new OMException("Error while accessing inputstream", e);
        }
    }
    
    
    /**
     * Builds a DOM Element for the Observation and selected version
     * @param dom
     * @param observation
     * @param version
     * @return
     * @throws OMException
     */
    public Element writeObservation(DOMHelper dom, AbstractObservation observation, String version) throws OMException
    {
        ObservationWriter writer = (ObservationWriter)OGCRegistry.createWriter(OM, OBSERVATION, version);
        return writer.writeObservation(dom, observation);
    }
    
    
    /**
     * Writes XML for an Observation of selected version in the OutputStream
     * @param outputStream
     * @param observation
     * @param version
     * @throws OMException
     */
    public void writeObservation(OutputStream outputStream, AbstractObservation observation, String version) throws OMException
    {
        ObservationWriter writer = (ObservationWriter)OGCRegistry.createWriter(OM, OBSERVATION, version);
        writer.writeObservation(outputStream, observation);
    }

    
    /**
     * Logic to guess SWE version from namespace
     * @param dom
     * @return
     */
    public String getVersion(DOMHelper dom, Element omElt)
    {
        // get version from the last part of namespace URI
        //String sweUri = dom.getXmlDocument().getNSUri("swe");
        String sweUri = omElt.getNamespaceURI();
        String version = sweUri.substring(sweUri.lastIndexOf('/') + 1);
        
        // check if version is a valid version number otherwise defaults to 0
        if (!version.matches(versionRegex))
            version = defaultVersion;
        
        return version;
    }
}
