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

package org.vast.ogc.gml;

import java.util.UUID;
import org.vast.util.Bbox;
import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b>
 * GML Envelope Writer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Used to serialize Bbox objects to GML.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 25, 2006
 * @version 1.0
 */
public class GMLEnvelopeWriter
{
    private String version = null;
    
    
    public GMLEnvelopeWriter()
    {    	
    }
    
    
    public GMLEnvelopeWriter(String version)
    {    	
    	this.version = version;
    }
    
        
    public Element writeEnvelope(DOMHelper dom, Bbox bbox)
    {
        dom.addUserPrefix("gml", OGCRegistry.getNamespaceURI(OGCRegistry.GML, version));
        
        Element envelopeElt = dom.createElement("gml:Envelope");
    	
        if (bbox.getCrs() != null)
        	dom.setAttributeValue(envelopeElt, "@srsName", bbox.getCrs());
        
		String lowerCorner = bbox.getMinX() + " " + bbox.getMinY();
		dom.setElementValue(envelopeElt, "gml:lowerCorner", lowerCorner);
		String upperCorner = bbox.getMaxX() + " " + bbox.getMaxY();
		dom.setElementValue(envelopeElt, "gml:upperCorner", upperCorner);
        
		// assign random ID
        String randomId = "T" + Integer.toString(UUID.randomUUID().hashCode());
        envelopeElt.setAttribute("gml:id", randomId);
        
        return envelopeElt;
    }
    
    
    public Element writeEnvelopeWithPos(DOMHelper dom, Bbox bbox)
    {
        dom.addUserPrefix("gml", OGCRegistry.getNamespaceURI(OGCRegistry.GML));
        
        Element envelopeElt = dom.createElement("gml:Envelope");
    	
        if (bbox.getCrs() != null)
        	dom.setAttributeValue(envelopeElt, "@srsName", bbox.getCrs());
        
		String lowerCorner = bbox.getMinX() + " " + bbox.getMinY();
		dom.setElementValue(envelopeElt, "gml:pos", lowerCorner);
		String upperCorner = bbox.getMaxX() + " " + bbox.getMaxY();
		dom.setElementValue(envelopeElt, "+gml:pos", upperCorner);
        
        return envelopeElt;
    }
    
    
    public Element writeGridEnvelope(DOMHelper dom, Bbox bbox)
    {
        dom.addUserPrefix("gml", OGCRegistry.getNamespaceURI(OGCRegistry.GML));
        
        Element envelopeElt = dom.createElement("gml:GridEnvelope");
    	
        if (bbox.getCrs() != null)
        	dom.setAttributeValue(envelopeElt, "@srsName", bbox.getCrs());
        
		String lowerCorner = bbox.getMinX() + " " + bbox.getMinY();
		dom.setElementValue(envelopeElt, "gml:low", lowerCorner);
		String upperCorner = bbox.getMaxX() + " " + bbox.getMaxY();
		dom.setElementValue(envelopeElt, "gml:high", upperCorner);
        
		return envelopeElt;
    }
}
