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

import org.vast.xml.DOMHelper;
import org.vast.math.Vector3d;
import org.w3c.dom.Element;
import com.vividsolutions.jts.geom.Geometry;


/**
 *  * <p><b>Title:</b>
 * GML Geometry Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Reads different GML geometry objects (coords + CRS)
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 25, 2006
 * @version 1.0
 */
public class GMLGeometryReader
{
    protected final static String invalidCoordinates = "Invalid Coordinates: ";
    
    
    public GMLGeometryReader()
    {
    }
    
    
    public Geometry readGeometry(DOMHelper dom, Element geomElt) throws GMLException
    {
        return null;
    }
    
        
    public Vector3d readVector(DOMHelper dom, Element pointElt) throws GMLException
    {
        Vector3d point = new Vector3d();
        String coordsText = "";
        String[] coords;
        
        // read SRS
        String srs = dom.getAttributeValue(pointElt, "@srsName");
        point.setCrs(srs);
        
        try
        {
            // read lower corner
            coordsText = dom.getElementValue(pointElt, "coordinates");
            coords = coordsText.split(" ");
            
            if (srs.contains("EPSG") && srs.contains("432"))
            {
                point.y = Double.parseDouble(coords[0]);
                point.x = Double.parseDouble(coords[1]);
            }
            else if (srs.contains("CRS84"))
            {
                point.x = Double.parseDouble(coords[0]);
                point.y = Double.parseDouble(coords[1]);
            }
            
            if (coords.length > 2)
                point.z = Double.parseDouble(coords[2]);
        }
        catch (Exception e)
        {
            throw new GMLException(invalidCoordinates + coordsText, e);
        }
        
        return point;
    }
}
