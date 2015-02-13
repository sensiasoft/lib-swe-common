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
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


/**
 * <p>
 * Writer for GML geometries
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Nov 26, 2012
 * */
public class GMLGeometryWriter
{
    private static String DEFAULT_CRS_URI_PREFIX = "http://www.opengis.net/def/crs/EPSG/0/";
    private String gmlNsUri;
    protected int currentId;
    protected NumberFormat idFormatter;
    protected StringBuilder buf = new StringBuilder();
    
    
    public GMLGeometryWriter()
    {
        currentId = 1;
        idFormatter = NumberFormat.getNumberInstance();
        idFormatter.setMinimumIntegerDigits(3);
        idFormatter.setGroupingUsed(false);
    }
    
    
    public void setGmlVersion(String gmlVersion)
    {       
        gmlNsUri = OGCRegistry.getNamespaceURI(GMLUtils.GML, gmlVersion);
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
    
    
    public Element writePoint(DOMHelper dom, Point pos) throws XMLWriterException
    {
        dom.addUserPrefix("gml", gmlNsUri);
        Element pointElt = dom.createElement("gml:Point");
        
        if (pos.getSRID() != 0)
            dom.setAttributeValue(pointElt, "srsName", DEFAULT_CRS_URI_PREFIX + pos.getSRID());
        
        Coordinate coords = pos.getCoordinate();
        String text = coords.x + " " + coords.y;
        if (!Double.isNaN(coords.z))
            text += " " + coords.z;
        dom.setElementValue(pointElt, "gml:coordinates", text);
        
        // assign ID
        String nextId = "P" + idFormatter.format(currentId++);
        pointElt.setAttribute("gml:id", nextId);
        
        return pointElt;
    }
    
    
    public Element writePolygon(DOMHelper dom, Polygon poly) throws XMLWriterException
    {
        dom.addUserPrefix("gml", gmlNsUri);
        Element polyElt = dom.createElement("gml:Polygon");
        
        // exterior
        fillBufferWithCoordinates(poly.getExteriorRing().getCoordinates());
        dom.setElementValue(polyElt, "gml:exterior/gml:LinearRing/gml:posList", buf.toString());
                
        // interiors
        for (int i=0; i<poly.getNumInteriorRing(); i++)
        {
            fillBufferWithCoordinates(poly.getInteriorRingN(i).getCoordinates());
            dom.setElementValue(polyElt, "+gml:interior/gml:LinearRing/gml:posList", buf.toString());
        }
        
        // assign ID
        String nextId = "P" + idFormatter.format(currentId++);
        polyElt.setAttribute("gml:id", nextId);
        
        return polyElt;
    }
    
    
    protected void fillBufferWithCoordinates(Coordinate[] coordinates)
    {
        buf.setLength(0);
                
        for (Coordinate coord: coordinates)
        {
            buf.append(coord.x);
            buf.append(' ');
            buf.append(coord.y);
            buf.append(' ');
            if (!Double.isNaN(coord.z))
            {
                buf.append(coord.x);
                buf.append(' ');                
            }
        }
        
        // remove last space
        buf.setLength(buf.length()-1);
    }
}
