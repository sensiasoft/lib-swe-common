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

import org.vast.util.Bbox;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 *  * <p>
 * Reads GML envelope (coords + CRS)
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @since Oct 25, 2006
 * @version 1.0
 */
public class GMLEnvelopeReader
{
    protected final static String invalidCoordinates = "Invalid Coordinates: ";
    
    
    public GMLEnvelopeReader()
    {
    }
    
        
    public Bbox readEnvelope(DOMHelper dom, Element envelopeElt) throws XMLReaderException
    {
    	Bbox bbox = new Bbox();
        String coordsText = "";
        String[] coords;
        
        // read SRS
        String srs = dom.getAttributeValue(envelopeElt, "@srsName");
        bbox.setCrs(srs);
        
        try
        {
	        Element lowerCornerElt = dom.getElement(envelopeElt, "lowerCorner");
	        Element upperCornerElt = dom.getElement(envelopeElt, "upperCorner");
	        
        	// case of gml:pos (deprecated but used by WCS 1.0)
	        if (dom.existElement(envelopeElt, "pos"))
	        {
	        	NodeList posElts = dom.getElements(envelopeElt, "pos");
	        	lowerCornerElt = (Element)posElts.item(0);
		        upperCornerElt = (Element)posElts.item(1);
	        }
	        	        
	        // read lower corner
            coordsText = dom.getElementValue(lowerCornerElt);
            coords = coordsText.split(" ");
            bbox.setMinX(Double.parseDouble(coords[0]));
            bbox.setMinY(Double.parseDouble(coords[1]));
            if (coords.length == 3)
            	bbox.setMinZ(Double.parseDouble(coords[2]));                
            
            // read upper corner
            coordsText = dom.getElementValue(upperCornerElt);
            coords = coordsText.split(" ");
            bbox.setMaxX(Double.parseDouble(coords[0]));
            bbox.setMaxY(Double.parseDouble(coords[1]));
            if (coords.length == 3)
            	bbox.setMaxZ(Double.parseDouble(coords[2]));
        }
        catch (Exception e)
        {
            throw new XMLReaderException(invalidCoordinates + coordsText, envelopeElt, e);
        }
        
        return bbox;
    }
}
