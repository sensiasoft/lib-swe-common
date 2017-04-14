/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataStream;
import org.custommonkey.xmlunit.Validator;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.vast.swe.SWEUtils;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;


public class TestSweDomBindingsV20 extends XMLTestCase
{
    
    @Override
    public void setUp() throws Exception
    {
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setNormalizeWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
    }
    
    
    protected void validate(InputStream is, String schemaUrl) throws Exception
    {
        InputSource saxIs = new InputSource(is);
        Validator v = new Validator(saxIs);
        v.useXMLSchema(true);
        v.setJAXP12SchemaSource(schemaUrl);
        assertTrue(v.isValid());
    }
    
    
    protected void readWriteCompareSweCommonXml(String path, boolean isDataStream) throws Exception
    {
        SWEUtils utils = new SWEUtils(SWEUtils.V2_0);
        
        InputStream is = getClass().getResourceAsStream(path);
        DOMHelper dom1 = new DOMHelper(is, false);
        DataComponent data;
        if (isDataStream)
            data = utils.readDataStream(dom1, dom1.getBaseElement());
        else
            data = utils.readComponent(dom1, dom1.getBaseElement());
        is.close();
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.out.println();
        DOMHelper dom = new DOMHelper();
        Element domElt;
        if (isDataStream)
            domElt = utils.writeDataStream(dom, (DataStream)data);
        else
            domElt = utils.writeComponent(dom, data, true);
        dom.serialize(domElt, System.out, true);
        dom.serialize(domElt, os, true);
        os.close();
        
        DOMHelper dom2 = new DOMHelper(new ByteArrayInputStream(os.toByteArray()), false);
        assertXMLEqual(dom1.getDocument(), dom2.getDocument());
    }
    
    
    public void testReadWriteScalars() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/spec/simple_components.xml", false);
    }
    
    
    public void testReadWriteRecord() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/spec/record_weather.xml", false);
        readWriteCompareSweCommonXml("examples_v20/spec/record_coefs.xml", false);
        readWriteCompareSweCommonXml("examples_v20/spec/range_components.xml", false);
        readWriteCompareSweCommonXml("examples_v20/sps/TaskingParameter_DataRecord.xml", false);
    }
    
    
    public void testReadWriteRecordWithConstraints() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/sps/TaskingParameter_DataRecord_constraints.xml", false);
        readWriteCompareSweCommonXml("examples_v20/spec/constraints.xml", false);
    }
    
    
    public void testReadWriteRecordWithOptionals() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/sps/TaskingParameter_DataRecord_optional.xml", false);
    }
    
    
    public void testReadWriteRecordWithNilValues() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/spec/nilValues.xml", false);
    }
    
    
    public void testReadWriteRecordWithQuality() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/spec/quality.xml", false);
    }
    
    
    public void testReadWriteRecordWithXlinks() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/spec/record_weather_xlinks.xml", false);
    }
    
    
    public void testReadWriteArrayNoData() throws Exception
    {        
        readWriteCompareSweCommonXml("examples_v20/spec/array_trajectory.xml", false);
        readWriteCompareSweCommonXml("examples_v20/spec/array_image_band_interleaved.xml", false);
        readWriteCompareSweCommonXml("examples_v20/spec/array_image_pixel_interleaved.xml", false);
    }
    
    
    public void testReadWriteArrayWithTextData() throws Exception
    {        
        readWriteCompareSweCommonXml("examples_v20/spec/array_weather.xml", false);
        readWriteCompareSweCommonXml("examples_v20/spec/enc_text_curve.xml", false);
        readWriteCompareSweCommonXml("examples_v20/spec/enc_text_profile_series.xml", false);
        readWriteCompareSweCommonXml("examples_v20/spec/enc_text_stress_matrix.xml", false);
        readWriteCompareSweCommonXml("examples_v20/spec/matrix_rotation.xml", false);
    }
    
    
    public void testReadWriteArrayWithRangeData() throws Exception
    {        
        readWriteCompareSweCommonXml("examples_v20/spec/array_with_ranges.xml", false);
    }
    
    
    /*public void testReadWriteArrayWithXmlData() throws Exception
    {        
        readWriteCompareSweCommonXml("examples_v20/spec/enc_xml_curve.xml");
        readWriteCompareSweCommonXml("examples_v20/spec/enc_xml_profile_series.xml");
    }*/
    
    
    public void testReadWriteArrayBinary() throws Exception
    {        
        
    }
    
    
    public void testReadWriteDataChoice() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/spec/choice_stream.xml", false);
    }
    
    
    public void testReadWriteDataStream() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/weather_data.xml", true);
        readWriteCompareSweCommonXml("examples_v20/nav_data.xml", true);
        readWriteCompareSweCommonXml("examples_v20/image_data.xml", true);
        readWriteCompareSweCommonXml("examples_v20/spec/datastream_with_quality.xml", true);
    }
    
    
    public void testReadWriteDataStreamWithChoice() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/spec/enc_text_choice_stream.xml", true);
    }
    
}
