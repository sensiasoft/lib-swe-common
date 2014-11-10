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

package org.vast.sweCommon;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import net.opengis.swe.v20.DataComponent;
import org.custommonkey.xmlunit.Validator;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.vast.sweCommon.SWECommonUtils;
import org.vast.xml.DOMHelper;
import org.xml.sax.InputSource;


public class TestSweDomBindingsV20 extends XMLTestCase
{
    
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
    
    
    protected void readWriteCompareSweCommonXml(String path) throws Exception
    {
        SWECommonUtils utils = new SWECommonUtils();
        utils.setOutputVersion("2.0");
        
        InputStream is = getClass().getResourceAsStream(path);
        DOMHelper dom1 = new DOMHelper(is, false);
        DataComponent data = utils.readComponent(dom1, dom1.getBaseElement());
        is.close();
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.out.println();
        utils.writeComponent(System.out, data, true);
        utils.writeComponent(os, data, true);
        os.close();
        
        DOMHelper dom2 = new DOMHelper(new ByteArrayInputStream(os.toByteArray()), false);
        assertXMLEqual(dom1.getDocument(), dom2.getDocument());
    }
    
    
    public void testReadWriteScalars() throws Exception
    {
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/simple_components.xml");
    }
    
    
    public void testReadWriteRecord() throws Exception
    {
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/record_weather.xml");
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/record_coefs.xml");
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/range_components.xml");
        readWriteCompareSweCommonXml("/swe/examples_v20/sps/TaskingParameter_DataRecord.xml");
    }
    
    
    public void testReadWriteRecordWithConstraints() throws Exception
    {
        readWriteCompareSweCommonXml("/swe/examples_v20/sps/TaskingParameter_DataRecord_constraints.xml");
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/constraints.xml");
    }
    
    
    public void testReadWriteRecordWithOptionals() throws Exception
    {
        readWriteCompareSweCommonXml("/swe/examples_v20/sps/TaskingParameter_DataRecord_optional.xml");
    }
    
    
    public void testReadWriteRecordWithNilValues() throws Exception
    {
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/nilValues.xml");
    }
    
    
    public void testReadWriteRecordWithQuality() throws Exception
    {
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/quality.xml");
    }
    
    
    public void testReadWriteRecordWithXlinks() throws Exception
    {
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/record_weather_xlinks.xml");
    }
    
    
    public void testReadWriteArrayNoData() throws Exception
    {        
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/array_trajectory.xml");
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/array_image_band_interleaved.xml");
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/array_image_pixel_interleaved.xml");
    }
    
    
    public void testReadWriteArrayWithTextData() throws Exception
    {        
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/array_weather.xml");
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/enc_text_curve.xml");
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/enc_text_profile_series.xml");
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/enc_text_stress_matrix.xml");
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/matrix_rotation.xml");
    }
    
    
    public void testReadWriteArrayWithRangeData() throws Exception
    {        
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/array_with_ranges.xml");
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
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/choice_stream.xml");
    }
    
    
    public void testReadWriteDataStream() throws Exception
    {
        readWriteCompareSweCommonXml("/swe/examples_v20/weather_data.xml");
        readWriteCompareSweCommonXml("/swe/examples_v20/nav_data.xml");
        readWriteCompareSweCommonXml("/swe/examples_v20/image_data.xml");
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/datastream_with_quality.xml");
    }
    
    
    public void testReadWriteDataStreamWithChoice() throws Exception
    {
        readWriteCompareSweCommonXml("/swe/examples_v20/spec/enc_text_choice_stream.xml");
    }
    
}
