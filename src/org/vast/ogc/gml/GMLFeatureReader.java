/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Initial Developer of the Original Code is SENSIA SOFTWARE LLC.
 Portions created by the Initial Developer are Copyright (C) 2012
 the Initial Developer. All Rights Reserved.

 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> for more
 information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.text.ParseException;
import javax.xml.namespace.QName;
import org.vast.math.Vector3d;
import org.vast.util.DateTimeFormat;
import org.vast.xml.DOMHelper;
import org.vast.xml.IXMLReaderDOM;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p><b>Title:</b>
 * GMLFeatureReader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Reader for generic GML features
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @date Nov 18, 2012
 * @version 1.0
 */
public class GMLFeatureReader implements IXMLReaderDOM<IFeature>
{
    GMLGeometryReader geometryReader = new GMLGeometryReader();
    
    
    public GMLFeatureReader()
    {        
    }
    
    
    @Override
    public IFeature read(DOMHelper dom, Element featureElt) throws XMLReaderException
    {
        QName qname = new QName(featureElt.getNamespaceURI(), featureElt.getLocalName());
        FeatureImpl feature = new FeatureImpl(qname);
        
        // id
        feature.setLocalId(dom.getAttributeValue(featureElt, "@id"));
        
        // scan all child elements
        NodeList allElts = dom.getAllChildElements(featureElt);
        for (int i = 0; i < allElts.getLength(); i++)
        {
            Element elt = (Element)allElts.item(i);
            
            if (elt.getNamespaceURI().startsWith("http://www.opengis.net/gml"))
            {
                // description
                if (elt.getLocalName().equals("description"))
                    feature.setDescription(dom.getElementValue(elt));
                    
                // identifier
                else if (elt.getLocalName().equals("identifier"))
                    feature.setIdentifier(dom.getElementValue(elt));
                
                // names
                else if (elt.getLocalName().equals("name"))
                {
                    String value = dom.getElementValue(elt);
                    String codeSpace = dom.getAttributeValue(elt, "codeSpace");
                    QName name = new QName(codeSpace, value);
                    feature.addName(name);
                }
                
                else if (elt.getLocalName().equals("location"))
                {
                    Vector3d location = geometryReader.readVector(dom, dom.getFirstChildElement(elt));
                    feature.setLocation(location);
                }
            }
            
            // non gml properties
            else
            {
                // TODO implement schema aware parsing ??
                QName propName = new QName(elt.getNamespaceURI(), elt.getLocalName());                
                Object value = null;
                
                // simple values
                if (dom.getFirstChildElement(elt) == null)
                {
                    String propVal = dom.getElementValue(elt);
                                        
                    if (propVal != null)
                    {
                        try
                        {
                            value = Integer.parseInt(propVal);
                        }
                        catch (NumberFormatException e)
                        {
                            try
                            {
                                value = Double.parseDouble(propVal);
                            }
                            catch (NumberFormatException e1)
                            {
                                try
                                {
                                    if (propVal.equalsIgnoreCase("true") || propVal.equalsIgnoreCase("false"))
                                        value = Boolean.parseBoolean(propVal);
                                    else
                                        DateTimeFormat.parseIso(propVal);
                                }
                                catch (ParseException e2)
                                {
                                    value = propVal;
                                }
                            }
                        }
                    }
                }
                
                // complex content -> save raw XML
                else
                    value = dom.getFirstChildElement(elt).cloneNode(true);
                                
                feature.setProperty(propName, value);
            }
        }
        
        return feature;
    }
}
