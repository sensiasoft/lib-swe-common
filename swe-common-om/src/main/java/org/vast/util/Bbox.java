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
 Tony Cook <tcook@nsstc.uah.edu>
 Alex Robin <robin@nsstc.uah.edu>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.util;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.operation.buffer.BufferParameters;


/**
 * <p>Title:
 * Bbox
 * </p>
 *
 * <p>Description:
 * Simple structure for OGC-style bbox info.
 * </p>
 *
 * @author Tony Cook, Alex Robin
 * @since Aug 16, 2005
 * */
public class Bbox extends SpatialExtent
{
   
	public Bbox()
	{		
	}
	
	
	public Bbox(double minX, double minY, double maxX, double maxY)
    {
    	setMinX(minX);
        setMinY(minY);
        setMaxX(maxX);
        setMaxY(maxY);
    }
	
	
	public Bbox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
    {
    	setMinX(minX);
        setMinY(minY);
        setMinZ(minZ);
        setMaxX(maxX);
        setMaxY(maxY);
        setMaxZ(maxZ);
    }
	
	
	public Bbox(double minX, double minY, double maxX, double maxY, String crs)
    {
        this(minX, minY, maxX, maxY);
        this.crs = crs;
    }
    
    
    public Bbox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, String crs)
    {
        this(minX, minY, minZ, maxX, maxY, maxZ);
        this.crs = crs;
    }
	
	
	@Override
    public Bbox copy()
    {
        Bbox bbox = new Bbox();
        bbox.setMinX(this.minX);
        bbox.setMinY(this.minY);
        bbox.setMinZ(this.minZ);
        bbox.setMaxX(this.maxX);
        bbox.setMaxY(this.maxY);
        bbox.setMaxZ(this.maxZ);
        return bbox;
    }
	
	
	public com.vividsolutions.jts.geom.Envelope toJtsEnvelope()
	{
	    return new com.vividsolutions.jts.geom.Envelope(minX, maxX, minY, maxY);
	}
	
	
	public com.vividsolutions.jts.geom.Polygon toJtsPolygon()
    {
	    Geometry geom = new GeometryFactory().toGeometry(toJtsEnvelope());
	    
        if (geom instanceof Point)
            geom = geom.buffer(1e-6, 1, BufferParameters.CAP_SQUARE);
        else if (geom instanceof LineString)
            geom = geom.buffer(1e-6, 1, BufferParameters.CAP_FLAT);
        
	    return (com.vividsolutions.jts.geom.Polygon)geom;
    }
	
	
	public net.opengis.gml.v32.Envelope toGmlEnvelope()
	{
	    if (Double.isNaN(minZ))
	        return new net.opengis.gml.v32.impl.EnvelopeImpl(crs, minX, maxX, minY, maxY);
	    else
	        return new net.opengis.gml.v32.impl.EnvelopeImpl(crs, minX, maxX, minY, maxY, minZ, maxZ);
	}


    public void checkValid()
    {
        if (minX > maxX)
            throw new IllegalArgumentException("MinX > MaxX");
        
        if (minY > maxY)
            throw new IllegalArgumentException("MinY > MaxY");
        
        if (minZ > maxZ)
            throw new IllegalArgumentException("MinZ > MaxZ");
    }
}
