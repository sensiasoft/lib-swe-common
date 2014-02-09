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

import java.util.Date;
import java.util.Map.Entry;
import javax.xml.namespace.QName;
import org.vast.math.Vector3d;
import org.vast.ogc.OGCRegistry;
import org.vast.util.DateTimeFormat;
import org.vast.xml.DOMHelper;
import org.vast.xml.IXMLWriterDOM;
import org.vast.xml.XMLWriterException;
import org.w3c.dom.Element;


/**
 * <p>
 * Writer for generic feature.
 * This class can serialize simple properties of type Boolean, Number, String or Date.
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @since Nov 18, 2012
 * @version 1.0
 */
public class GMLFeatureWriter implements IXMLWriterDOM<IFeature>
{
    private GMLGeometryWriter geometryWriter = new GMLGeometryWriter();
    private String gmlNsUri;
    
    
    public GMLFeatureWriter()
    {        
    }
    
    
    public void setGmlVersion(String gmlVersion)
    {       
        gmlNsUri = OGCRegistry.getNamespaceURI(GMLUtils.GML, gmlVersion);
        geometryWriter.setGmlVersion(gmlVersion);
    }
    
    
    @Override
    public Element write(DOMHelper dom, IFeature feature) throws XMLWriterException
    {
        dom.addUserPrefix("gml", gmlNsUri);
        
        QName qname = feature.getQName();
        Element featureElt = dom.getDocument().createElementNS(qname.getNamespaceURI(), qname.getLocalPart());
        
        if (feature.getLocalId() != null)
            dom.setAttributeValue(featureElt, "gml:id", feature.getLocalId());
        
        if (feature.getDescription() != null)
            dom.setElementValue(featureElt, "gml:description", feature.getDescription());
        
        if (feature.getIdentifier() != null)
            dom.setElementValue(featureElt, "gml:identifier", feature.getIdentifier());
        
        for (QName name: feature.getNames())
        {
            Element nameElt = dom.setElementValue(featureElt, "+gml:name", name.getLocalPart());
            if (name.getNamespaceURI() != null)
                nameElt.setAttribute("codeSpace", name.getNamespaceURI());
        }
        
        for (Entry<QName, Object> property: feature.getProperties().entrySet())
        {
            Object val = property.getValue();
            QName propQName = property.getKey();
            Element propElt = dom.getDocument().createElementNS(propQName.getNamespaceURI(), propQName.getLocalPart());
            featureElt.appendChild(propElt);
            
            if (val instanceof Boolean || val instanceof Number || val instanceof String)
            {
                dom.setElementValue(propElt, val.toString());
            }
            else if (val instanceof Date)
            {
                double julian = ((Date)val).getTime() / 1000.0;
                String propVal = DateTimeFormat.formatIso(julian, 0);
                dom.setElementValue(propElt, propVal);
            }
            else if (val instanceof Vector3d)
            {
                Element pointElt = geometryWriter.writePoint(dom, (Vector3d)val);
                propElt.appendChild(pointElt);
            }
            
            // TODO serialization of complex properties using delegated writers
        }
        
        return featureElt;
    }

}
