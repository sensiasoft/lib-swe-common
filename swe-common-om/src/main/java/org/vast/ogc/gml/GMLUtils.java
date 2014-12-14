/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.

 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or 
 Mike Botts <mike.botts@botts-inc.net for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.io.InputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import net.opengis.gml.v32.AbstractGeometry;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.om.OMUtils;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.vast.xml.XMLReaderException;
import org.vast.xml.XMLWriterException;
import org.w3c.dom.Element;


/**
 * <p>
 * Utility methods to read/write GML documents.
 * This class is not thread-safe.
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Feb 8, 2014
 */
public class GMLUtils
{
    public final static String GML;
    GMLStaxBindings gmlBindings = new GMLStaxBindings();
    
    
    static 
    {
        GML = "GML";
        loadRegistry();
    }
    
    
    public static void loadRegistry()
    {
        String mapFileUrl = OMUtils.class.getResource("OMRegistry.xml").toString();
        OGCRegistry.loadMaps(mapFileUrl, false);
    }
    
    
    public static IFeature readFeature(InputStream inputStream) throws XMLReaderException
    {
        try
        {
            DOMHelper dom = new DOMHelper(inputStream, false);
            GMLFeatureReader reader = new GMLFeatureReader();
            return reader.read(dom, dom.getBaseElement());
        }
        catch (DOMHelperException e)
        {
            throw new XMLReaderException("Error while reading GML from input stream", e);
        }
    }
    
    
    public AbstractGeometry readGeometry(DOMHelper dom, Element geomElt) throws XMLReaderException
    {
        try
        {
            DOMSource source = new DOMSource(geomElt);
            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(source);
            reader.nextTag();
            return gmlBindings.readAbstractGeometry(reader);
        }
        catch (Exception e)
        {
            throw new XMLReaderException("Error while reading GML geometry", e);
        }
    }
    
    
    public Element writeGeometry(DOMHelper dom, AbstractGeometry geom) throws XMLWriterException
    {
        try
        {
            DOMResult result = new DOMResult(dom.createElement("fragment"));
            XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(result);
            gmlBindings.setNamespacePrefixes(writer);
            gmlBindings.declareNamespacesOnRootElement();
            gmlBindings.writeAbstractGeometry(writer, geom);
            return (Element)result.getNode().getFirstChild();
        }
        catch (Exception e)
        {
            throw new XMLWriterException("Error while writing GML geometry", e);
        }
    }
}
