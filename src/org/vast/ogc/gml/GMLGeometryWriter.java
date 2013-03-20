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

import java.text.NumberFormat;
import org.vast.math.Vector3d;
import org.vast.ogc.OGCRegistry;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLWriterException;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b>
 * GMLGeometryWriter
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Writer for GML geometries
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @date Nov 26, 2012
 * @version 1.0
 */
public class GMLGeometryWriter
{
    private String gmlNsUri;
    protected int currentId;
    protected NumberFormat idFormatter;
    
    
    public GMLGeometryWriter()
    {
        currentId = 1;
        idFormatter = NumberFormat.getNumberInstance();
        idFormatter.setMinimumIntegerDigits(3);
        idFormatter.setGroupingUsed(false);
    }
    
    
    public void setGmlVersion(String gmlVersion)
    {       
        gmlNsUri = OGCRegistry.getNamespaceURI(OGCRegistry.GML, gmlVersion);
    }
    
    
    public Element writePoint(DOMHelper dom, Vector3d pos) throws XMLWriterException
    {
        dom.addUserPrefix("gml", gmlNsUri);
        Element pointElt = dom.createElement("gml:Point");
        
        if (pos.getCrs() != null)
            dom.setAttributeValue(pointElt, "srsName", pos.getCrs());
        
        String text = pos.x + " " + pos.y;
        if (!Double.isNaN(pos.z))
            text += " " + pos.z;
        dom.setElementValue(pointElt, "gml:coordinates", text);
        
        // assign ID
        String nextId = "P" + idFormatter.format(currentId++);
        pointElt.setAttribute("gml:id", nextId);
        
        return pointElt;
    }
}
