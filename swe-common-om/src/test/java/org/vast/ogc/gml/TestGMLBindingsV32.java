/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SWE Common Data Framework".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.io.InputStream;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.AbstractTimeGeometricPrimitive;
import net.opengis.gml.v32.Envelope;
import org.custommonkey.xmlunit.Validator;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.vast.ogc.gml.GMLUtils;
import org.vast.util.TimeExtent;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;


public class TestGMLBindingsV32 extends XMLTestCase
{
    
    
    @Override
    public void setUp() throws Exception
    {
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setNormalizeWhitespace(true);
    }
    
    
    protected void validate(InputStream is, String schemaUrl) throws Exception
    {
        InputSource saxIs = new InputSource(is);
        Validator v = new Validator(saxIs);
        v.useXMLSchema(true);
        v.setJAXP12SchemaSource(schemaUrl);
        assertTrue(v.isValid());
    }
    
    
    protected void readWriteCompareTimePrimitiveXml(String path) throws Exception
    {
        GMLUtils gmlUtils = new GMLUtils(GMLUtils.V3_2);
                
        // read XML example
        InputStream is = getClass().getResourceAsStream(path);
        DOMHelper dom1 = new DOMHelper(is, false);
        AbstractTimeGeometricPrimitive gmlTime = gmlUtils.readTimePrimitive(dom1, dom1.getBaseElement());
        is.close();
        
        // write GML object
        DOMHelper dom2 = new DOMHelper();
        Element gmlElt = gmlUtils.writeTimePrimitive(dom2, gmlTime);
        dom2.getDocument().appendChild(gmlElt);
        System.out.println();
        dom2.serialize(gmlElt, System.out, true);
                
        assertXMLEqual(dom1.getDocument(), dom2.getDocument());
    }
    
    
    protected void readWriteCompareTimeExtentAsGml(String path) throws Exception
    {
        GMLUtils gmlUtils = new GMLUtils(GMLUtils.V3_2);
                
        // read XML example
        InputStream is = getClass().getResourceAsStream(path);
        DOMHelper dom1 = new DOMHelper(is, false);
        AbstractTimeGeometricPrimitive gmlTime = gmlUtils.readTimePrimitive(dom1, dom1.getBaseElement());
        is.close();
        
        // write after conversion to TimeExtent
        TimeExtent timeExtent = gmlUtils.timePrimitiveToTimeExtent(gmlTime);
        DOMHelper dom2 = new DOMHelper();
        Element gmlElt = gmlUtils.writeTimeExtentAsTimePrimitive(dom2, timeExtent);
        dom2.getDocument().appendChild(gmlElt);
        System.out.println();
        dom2.serialize(gmlElt, System.out, true);
                
        assertXMLEqual(dom1.getDocument(), dom2.getDocument());
    }
    
    
    protected void readWriteCompareGeometryXml(String path) throws Exception
    {
        GMLUtils gmlUtils = new GMLUtils(GMLUtils.V3_2);
                
        // read XML example
        InputStream is = getClass().getResourceAsStream(path);
        DOMHelper dom1 = new DOMHelper(is, false);
        AbstractGeometry geom = gmlUtils.readGeometry(dom1, dom1.getBaseElement());
        is.close();
        
        // write GML object
        DOMHelper dom2 = new DOMHelper();
        Element gmlElt = gmlUtils.writeGeometry(dom2, geom);
        dom2.getDocument().appendChild(gmlElt);
        System.out.println();
        dom2.serialize(gmlElt, System.out, true);
                
        assertXMLEqual(dom1.getDocument(), dom2.getDocument());
    }
    
    
    protected Envelope readGeometryAndGetEnvelope(String path) throws Exception
    {
        GMLUtils gmlUtils = new GMLUtils(GMLUtils.V3_2);
                
        // read XML example
        InputStream is = getClass().getResourceAsStream(path);
        DOMHelper dom1 = new DOMHelper(is, false);
        AbstractGeometry geom = gmlUtils.readGeometry(dom1, dom1.getBaseElement());
        is.close();
        
        return geom.getGeomEnvelope();
    }
    
    
    public void testReadWriteTimeInstant() throws Exception
    {
        readWriteCompareTimePrimitiveXml("examples_v32/TimeInstant_datetime.xml");
        readWriteCompareTimePrimitiveXml("examples_v32/TimeInstant_now.xml");
        readWriteCompareTimePrimitiveXml("examples_v32/TimeInstant_unknown.xml");
    }
    
    
    public void testReadWriteTimePeriod() throws Exception
    {
        readWriteCompareTimePrimitiveXml("examples_v32/TimePeriod_datetime.xml");
        readWriteCompareTimePrimitiveXml("examples_v32/TimePeriod_beginNow.xml");
        readWriteCompareTimePrimitiveXml("examples_v32/TimePeriod_endNow.xml");
        readWriteCompareTimePrimitiveXml("examples_v32/TimePeriod_withTimeStep.xml");
        readWriteCompareTimePrimitiveXml("examples_v32/TimePeriod_beginUnknown.xml");
        readWriteCompareTimePrimitiveXml("examples_v32/TimePeriod_endUnknown.xml");
    }
    

    public void testReadWriteTimeExtentAsTimeInstant() throws Exception
    {
        readWriteCompareTimeExtentAsGml("examples_v32/TimeInstant_datetime.xml");
        readWriteCompareTimeExtentAsGml("examples_v32/TimeInstant_now.xml");
        readWriteCompareTimeExtentAsGml("examples_v32/TimeInstant_unknown.xml");
    }
    
    
    public void testReadWriteTimeExtentAsTimePeriod() throws Exception
    {
        readWriteCompareTimePrimitiveXml("examples_v32/TimePeriod_datetime.xml");
        readWriteCompareTimePrimitiveXml("examples_v32/TimePeriod_beginNow.xml");
        readWriteCompareTimePrimitiveXml("examples_v32/TimePeriod_endNow.xml");
        readWriteCompareTimePrimitiveXml("examples_v32/TimePeriod_withTimeStep.xml");
        readWriteCompareTimePrimitiveXml("examples_v32/TimePeriod_beginUnknown.xml");
        readWriteCompareTimePrimitiveXml("examples_v32/TimePeriod_endUnknown.xml");
    }
    
    
    public void testReadWritePoint() throws Exception
    {
        readWriteCompareGeometryXml("examples_v32/Point2D.xml");
        readWriteCompareGeometryXml("examples_v32/Point3D.xml");
    }
    
    
    public void testReadWriteLineString() throws Exception
    {
        readWriteCompareGeometryXml("examples_v32/Line2D.xml");
        readWriteCompareGeometryXml("examples_v32/Line3D.xml");
    }
    
    
    public void testReadWritePolygon() throws Exception
    {
        readWriteCompareGeometryXml("examples_v32/Polygon_noInterior.xml");
        readWriteCompareGeometryXml("examples_v32/Polygon_withInteriors.xml");
    }
    
    
    public void testComputeGeomEnvelopes() throws Exception
    {
        Envelope env;
        
        env = readGeometryAndGetEnvelope("examples_v32/Point2D.xml");        
        testEnvelope(env, 2, new double[] {1,2}, new double[] {1,2});
        
        env = readGeometryAndGetEnvelope("examples_v32/Point3D.xml");        
        testEnvelope(env, 3, new double[] {1,2,3}, new double[] {1,2,3});
        
        env = readGeometryAndGetEnvelope("examples_v32/Line2D.xml");        
        testEnvelope(env, 2, new double[] {1,2}, new double[] {5,6});
        
        env = readGeometryAndGetEnvelope("examples_v32/Line3D.xml");        
        testEnvelope(env, 3, new double[] {1,2,3}, new double[] {7,8,9});
        
        env = readGeometryAndGetEnvelope("examples_v32/Polygon_noInterior.xml");        
        testEnvelope(env, 2, new double[] {0,0}, new double[] {1,1});
        
        env = readGeometryAndGetEnvelope("examples_v32/Polygon_withInteriors.xml");        
        testEnvelope(env, 2, new double[] {0,0}, new double[] {1,1});
    }
    
    
    protected void testEnvelope(Envelope env, int numDims, double[] lower, double[] upper)
    {
        assertEquals(numDims, env.getSrsDimension());
        for (int i=0; i<numDims; i++)
            assertEquals(lower[i], env.getLowerCorner()[i]);
        for (int i=0; i<numDims; i++)
            assertEquals(upper[i], env.getUpperCorner()[i]);
    }
}
