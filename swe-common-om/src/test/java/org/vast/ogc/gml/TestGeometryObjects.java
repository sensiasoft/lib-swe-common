/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are copyright (C) 2018, Sensia Software LLC
 All Rights Reserved. This software is the property of Sensia Software LLC.
 It cannot be duplicated, used, or distributed without the express written
 consent of Sensia Software LLC.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import static org.junit.Assert.*;
import org.junit.Test;
import net.opengis.gml.v32.LineString;
import net.opengis.gml.v32.Point;
import net.opengis.gml.v32.Polygon;
import net.opengis.gml.v32.impl.GMLFactory;


public class TestGeometryObjects
{
    
    @Test
    public void testBuildPoint2D()
    {
        GMLFactory gml = new GMLFactory(false);
        Point point;
        
        point = gml.pointBuilder()
            .withDimension(2)
            .withCoordinates(33.65888,45.12499)
            .build();
        System.out.println(point);
    }
    
    
    @Test
    public void testBuildPoint3D()
    {
        GMLFactory gml = new GMLFactory(false);
        Point point;
        
        point = gml.pointBuilder()
            .withCoordinates(-76.817978500001,13.122848800001,356)
            .withDimension(3)
            .build();
        System.out.println(point);
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void testBuildPointWrongDimension()
    {
        GMLFactory gml = new GMLFactory(false);
        Point point;
        
        point = gml.pointBuilder()
            .withCoordinates(0,0, 1,1, 2,3, 2,0, 0.5,0)
            .withDimension(2)
            .build();
        System.out.println(point);
    }
    
    
    @Test
    public void testBuildLineString2D()
    {
        GMLFactory gml = new GMLFactory(false);
        LineString line;
        
        line = gml.lineStringBuilder()
            .withDimension(2)
            .withCoordinates(0,0, 1,1, 2,3, 2,0, 0.5,0)
            .build();
        System.out.println(line);
    }
    
    
    @Test
    public void testBuildLineString3D()
    {
        GMLFactory gml = new GMLFactory(false);
        LineString line;
        
        line = gml.lineStringBuilder()
            .withDimension(3)
            .withCoordinates(0,0,0, 1,1,1, 2,2,2, 3,3,3)
            .build();
        System.out.println(line);
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void testBuildLineStringWrongDimension()
    {
        GMLFactory gml = new GMLFactory(false);
        LineString line;
        
        line = gml.lineStringBuilder()
            .withDimension(3)
            .withCoordinates(0, 0, 1, 1, 2, 1, 2, 0, 0.5, 0)
            .build();
        System.out.println(line);
    }
    
    
    @Test
    public void testBuildPolygon2D()
    {
        GMLFactory gml = new GMLFactory(false);
        Polygon poly;
        
        // no holes
        poly = gml.polygonBuilder()
            .withExterior(0, 0, 1, 0, 1, 1, 0, 1, 0, 0)
            .build();
        System.out.println(poly);
        
        // 1 hole
        poly = gml.polygonBuilder()
            .withExterior(0, 0, 1.5, 0, 1.5, 2.5, 0, 1.3, 0, 0)
            .withInterior(0.4, 0.4, 0.7, 0.4, 0.7, 0.7, 0.4, 0.7, 0.4, 0.4)
            .build();
        System.out.println(poly);
        
        // several holes
        poly = gml.polygonBuilder()
            .withExterior(0, 0, 1.5, 0, 1.5, 2.5, 0, 1.3, 0, 0)
            .withInterior(0.4, 0.4, 0.7, 0.4, 0.7, 0.7, 0.4, 0.7, 0.4, 0.4)
            .withInterior(0.4, 0.4, 0.7, 0.4, 0.7, 0.7, 0.4, 0.7, 0.4, 0.4)
            .withInterior(0.4, 0.4, 0.7, 0.4, 0.7, 0.7, 0.4, 0.7, 0.4, 0.4)
            .build();
        System.out.println(poly);        
    }

}
