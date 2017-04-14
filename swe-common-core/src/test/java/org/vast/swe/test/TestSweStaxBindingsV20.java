/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.test;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import net.opengis.swe.v20.AllowedTokens;
import net.opengis.swe.v20.AllowedValues;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.BlockComponent;
import net.opengis.swe.v20.ByteEncoding;
import net.opengis.swe.v20.ByteOrder;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.AbstractSWE;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.DataStream;
import net.opengis.swe.v20.NilValue;
import net.opengis.swe.v20.NilValues;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.UnitReference;
import org.custommonkey.xmlunit.Validator;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.vast.cdm.common.DataStreamParser;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.data.*;
import org.vast.swe.SWEHelper;
import org.vast.swe.SWEStaxBindings;
import org.vast.xml.IndentingXMLStreamWriter;
import org.xml.sax.InputSource;


public class TestSweStaxBindingsV20 extends XMLTestCase
{
    static final boolean enableDecoding = true;
    
    
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
    
    
    protected void readWriteCompareSweCommonXml(String path) throws Exception
    {
        readWriteCompareSweCommonXml(path, false);
    }
    
    
    protected AbstractSWE readSweCommonXml(String path, boolean isDataStream) throws Exception
    {
        SWEStaxBindings sweHelper = new SWEStaxBindings();
        
        // read from file
        InputStream is = getClass().getResourceAsStream(path);
        XMLInputFactory input = new com.ctc.wstx.stax.WstxInputFactory();
        XMLStreamReader reader = input.createXMLStreamReader(is);
        reader.nextTag();
        
        AbstractSWE sweObj;
        if (isDataStream)
            sweObj = sweHelper.readDataStream(reader);
        else
            sweObj = sweHelper.readDataComponent(reader);
        is.close();
        
        return sweObj;
    }
    
    
    protected void writeSweCommonXmlToStream(AbstractSWE sweObj, OutputStream os, boolean indent) throws Exception
    {
        SWEStaxBindings sweHelper = new SWEStaxBindings();
        
        XMLOutputFactory output = new com.ctc.wstx.stax.WstxOutputFactory();
        if (os == System.out)
            System.err.println("Using " + output.getClass().getSimpleName());        
        
        XMLStreamWriter writer = output.createXMLStreamWriter(os);
        if (indent)
            writer = new IndentingXMLStreamWriter(writer);
        
        sweHelper.setNamespacePrefixes(writer);
        writer.writeStartDocument();
        sweHelper.declareNamespacesOnRootElement();
        if (sweObj instanceof DataStream)
            sweHelper.writeDataStream(writer, (DataStream)sweObj);
        else
            sweHelper.writeDataComponent(writer, (DataComponent)sweObj, true);
        writer.writeEndDocument();
        os.flush();
    }
    
    
    protected void readWriteCompareSweCommonXml(String path, boolean isDataStream) throws Exception
    {
        try
        {
            AbstractSWE sweObj = readSweCommonXml(path, isDataStream);
            
            // write back to stdout and buffer
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeSweCommonXmlToStream(sweObj, os, false);
            writeSweCommonXmlToStream(sweObj, System.out, true);
            System.out.println('\n');
            
            // compare with original
            InputSource src1 = new InputSource(getClass().getResourceAsStream(path));
            InputSource src2 = new InputSource(new ByteArrayInputStream(os.toByteArray()));
            assertXMLEqual(src1, src2);
        }
        catch (Throwable e)
        {
            throw new Exception("Failed test " + path, e);
        }
    }
    
    
    protected void readWriteCompareSweEncodedData(String testName, DataComponent dataStruct, DataEncoding encoding) throws Exception
    {
        try
        {
            // write encoded data
            DataStreamWriter dataWriter = SWEHelper.createDataWriter(encoding);
            dataWriter.setDataComponents(dataStruct);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            dataWriter.setOutput(os);
                        
            // write to buffer
            for (int i=0; i<5; i++)
            {
                // select item randomly
                if (dataStruct instanceof DataChoice)
                {
                    int randomItem = (int)(Math.random() * dataStruct.getComponentCount());
                    ((DataChoice) dataStruct).setSelectedItem(randomItem);
                }
                
                DataBlock data = dataStruct.createDataBlock();
                
                // change values randomly (start at 1 so choice item is not changed)
                for (int b=1; b<data.getAtomCount(); b++)
                    data.setDoubleValue(b, Math.random()*255.);
                
                dataWriter.write(data);
            }
            dataWriter.close();
            System.out.println("Data written to buffer. Size=" + os.size());
            //System.out.println(os);
                
            // read and write back to other buffer
            DataStreamParser dataParser = SWEHelper.createDataParser(encoding);
            dataParser.setDataComponents(dataStruct);
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            dataParser.setInput(is);            
            dataWriter = SWEHelper.createDataWriter(encoding);
            dataWriter.setDataComponents(dataStruct);
            ByteArrayOutputStream os2 = new ByteArrayOutputStream();
            dataWriter.setOutput(os2);            
            DataBlock data;
            while ((data = dataParser.parseNextBlock()) != null)
                dataWriter.write(data);
            dataParser.close();
            dataWriter.close();
            
            // compare with original
            assertArrayEquals(os.toByteArray(), os2.toByteArray());
        }
        catch (Throwable e)
        {
            throw new Exception("Failed test " + testName, e);
        }
    }
    
    
    public void testReadWriteScalars() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/spec/simple_components.xml");
    }
    
    
    public void testReadWriteRecord() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/spec/record_weather.xml");
        readWriteCompareSweCommonXml("examples_v20/spec/record_coefs.xml");
        readWriteCompareSweCommonXml("examples_v20/spec/range_components.xml");
        readWriteCompareSweCommonXml("examples_v20/sps/TaskingParameter_DataRecord.xml");
    }
    
    
    public void testReadWriteRecordWithConstraints() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/sps/TaskingParameter_DataRecord_constraints.xml");
        readWriteCompareSweCommonXml("examples_v20/spec/constraints.xml");
    }
    
    
    public void testReadWriteRecordWithOptionals() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/sps/TaskingParameter_DataRecord_optional.xml");
    }
    
    
    public void testReadWriteRecordWithNilValues() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/spec/nilValues.xml");
    }
    
    
    public void testReadWriteRecordWithQuality() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/spec/quality.xml");
    }
    
    
    public void testReadWriteRecordWithXlinks() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/spec/record_weather_xlinks.xml");
    }
    
    
    public void testReadWriteVector() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/spec/vector_location.xml");
        readWriteCompareSweCommonXml("examples_v20/spec/vector_quaternion.xml");
        readWriteCompareSweCommonXml("examples_v20/spec/vector_velocity.xml");
    }
    
    
    public void testReadWriteArrayNoData() throws Exception
    {        
        readWriteCompareSweCommonXml("examples_v20/spec/array_trajectory.xml");
        readWriteCompareSweCommonXml("examples_v20/spec/array_image_band_interleaved.xml");
        readWriteCompareSweCommonXml("examples_v20/spec/array_image_pixel_interleaved.xml");
    }
    
    
    public void testReadWriteArrayWithTextData() throws Exception
    {        
        readWriteCompareSweCommonXml("examples_v20/spec/array_weather.xml");
        readWriteCompareSweCommonXml("examples_v20/spec/enc_text_curve.xml");
        readWriteCompareSweCommonXml("examples_v20/spec/enc_text_profile_series.xml");
        readWriteCompareSweCommonXml("examples_v20/spec/enc_text_stress_matrix.xml");
        readWriteCompareSweCommonXml("examples_v20/spec/matrix_rotation.xml");
    }
    
    
    /*public void testReadWriteArrayWithXmlData() throws Exception
    {        
        readWriteCompareSweCommonXml("examples_v20/spec/enc_xml_curve.xml");
        readWriteCompareSweCommonXml("examples_v20/spec/enc_xml_profile_series.xml");
    }*/
    
    
    public void testReadWriteDataChoice() throws Exception
    {
        readWriteCompareSweCommonXml("examples_v20/spec/choice_stream.xml");
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
    
    
    protected void testReadWriteBinaryData(String path, boolean isDataStream, boolean encodeArrayElt) throws Exception
    {        
        String testName;
        DataComponent dataStruct = (DataComponent)readSweCommonXml(path, isDataStream);
        if (encodeArrayElt)
            dataStruct = ((BlockComponent)dataStruct).getElementType();
        
        // try to use binary encoding defined in XML
        BinaryEncoding encoding = null;
        if (dataStruct instanceof BlockComponent && ((BlockComponent)dataStruct).isSetEncoding())
        {
            DataEncoding encodingInXml = ((BlockComponent)dataStruct).getEncoding();
            if (encodingInXml instanceof BinaryEncoding)
                encoding = (BinaryEncoding)encodingInXml;
        }
        
        // otherwise use default binary encoding
        if (encoding == null)
            encoding = SWEHelper.getDefaultBinaryEncoding(dataStruct);
                
        // test 4 combinations of raw/base64 and littleEndian/bigEndian
        encoding.setByteEncoding(ByteEncoding.RAW);
        encoding.setByteOrder(ByteOrder.BIG_ENDIAN);
        testName = path + ", Binary " + encoding.getByteEncoding() + ", " + encoding.getByteOrder();
        readWriteCompareSweEncodedData(testName, dataStruct, encoding);
        
        encoding.setByteEncoding(ByteEncoding.RAW);
        encoding.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        testName = path + ", Binary " + encoding.getByteEncoding() + ", " + encoding.getByteOrder();
        readWriteCompareSweEncodedData(testName, dataStruct, encoding);
        
        encoding.setByteEncoding(ByteEncoding.BASE_64);
        encoding.setByteOrder(ByteOrder.BIG_ENDIAN);
        testName = path + ", Binary " + encoding.getByteEncoding() + ", " + encoding.getByteOrder();
        readWriteCompareSweEncodedData(testName, dataStruct, encoding);
        
        encoding.setByteEncoding(ByteEncoding.BASE_64);
        encoding.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        testName = path + ", Binary " + encoding.getByteEncoding() + ", " + encoding.getByteOrder();
        readWriteCompareSweEncodedData(testName, dataStruct, encoding);
    }
    
    
    public void testReadWriteBinaryArrayData() throws Exception
    { 
        testReadWriteBinaryData("examples_v20/spec/enc_binary_image.xml", false, false);
        testReadWriteBinaryData("examples_v20/spec/enc_text_stress_matrix.xml", false, false);
        testReadWriteBinaryData("examples_v20/spec/matrix_rotation.xml", false, false);
    }
    
    
    public void testReadWriteBinaryArrayDataImplicitVarSize() throws Exception
    { 
        testReadWriteBinaryData("examples_v20/spec/enc_text_profile_series.xml", false, true);
    }
    
    
    public void testReadWriteBinaryRecordData() throws Exception
    { 
        testReadWriteBinaryData("examples_v20/spec/array_weather.xml", false, true);
        testReadWriteBinaryData("examples_v20/spec/enc_text_nav_options.xml", true, true);
        testReadWriteBinaryData("examples_v20/spec/vector_location.xml", false, false);
        testReadWriteBinaryData("examples_v20/spec/vector_quaternion.xml", false, false);
    }
    
    
    public void testReadWriteBinaryChoiceData() throws Exception
    { 
        testReadWriteBinaryData("examples_v20/spec/enc_text_choice_stream.xml", true, true);
    }
    
    
    public void testSkipExtension() throws Exception
    {
        DataRecord rec = (DataRecord)readSweCommonXml("examples_v20/spec/extensions.xml", false);
        writeSweCommonXmlToStream(rec, System.out, true);
        assertEquals(4, rec.getFieldList().size());
    }
    
    
    public void testGenerateInstance() throws Exception
    {
        SWEStaxBindings sweXmlBindings = new SWEStaxBindings();
        
        //XMLOutputFactory output = XMLOutputFactory.newInstance();
        //XMLInputFactory input = XMLInputFactory.newInstance();
        XMLOutputFactory output = new com.ctc.wstx.stax.WstxOutputFactory();
        XMLInputFactory input = new com.ctc.wstx.stax.WstxInputFactory();
        System.err.println("Using " + output.getClass().getSimpleName());
        
        UnitReference uom;
        DataRecord rec = new DataRecordImpl();
        rec.setLabel("Weather Data Record");
        rec.setDescription("Record of synchronous weather measurements");
        
        Time t = new TimeImpl();
        t.setDefinition("http://www.opengis.net/def/property/OGC/0/SamplingTime");
        t.setReferenceFrame("http://www.opengis.net/def/trs/OGC/0/GPS");
        t.setLabel("Sampling Time");
        uom = new UnitReferenceImpl();
        uom.setHref("http://www.opengis.net/def/uom/ISO-8601/0/Gregorian");
        t.setUom(uom);
        rec.addField("time", t);
        
        Quantity q1 = new QuantityImpl();
        q1.setDefinition("http://mmisw.org/ont/cf/parameter/air_temperature");
        q1.setLabel("Air Temperature");
        uom = new UnitReferenceImpl();
        uom.setCode("Cel");
        q1.setUom(uom);
        AllowedValues constraint = new AllowedValuesImpl();
        constraint.addInterval(new double[] {-25.0, +70.0});
        constraint.addValue(150.0);
        q1.setConstraint(constraint);
        Quantity acc = new QuantityImpl();
        acc.setDefinition("http://mmisw.org/ont/cf/parameter/accuracy");
        uom = new UnitReferenceImpl();
        uom.setCode("%");
        acc.setUom(uom);
        q1.addQuality(acc);
        rec.addField("temp", q1);
        
        Category c1 = new CategoryImpl();        
        c1.setDefinition("http://mmisw.org/ont/cf/parameter/air_quality");
        c1.setLabel("Air Quality");
        c1.setCodeSpace("http://mmisw.org/ont/cf/parameter/air_quality_code");
        AllowedTokens tokens = new AllowedTokensImpl();
        tokens.addValue("bad");
        tokens.addValue("average");
        tokens.addValue("good");
        tokens.addValue("excellent");
        c1.setConstraint(tokens);
        c1.setValue("good");
        rec.addField("air_qual", c1);
                
        Quantity q3 = new QuantityImpl();
        q3.setDefinition("http://mmisw.org/ont/cf/parameter/wind_speed");
        q3.setLabel("Wind Speed");
        uom = new UnitReferenceImpl();
        uom.setCode("km/h");
        q3.setUom(uom);
        NilValues nilValues = new NilValuesImpl();
        NilValue nil = new NilValueImpl();
        nil.setReason("aboveThreshold");
        nil.setValue("NaN");
        nilValues.addNilValue(nil);
        q3.setNilValues(nilValues);
        rec.addField("wind_speed", q3);
        //rec.getFieldList().getProperty(rec.getFieldList().size()-1).setHref("http://link/to/my/website.xml");
        
        Quantity q4 = new QuantityImpl();
        q4.setDefinition("http://mmisw.org/ont/cf/parameter/wind_to_direction");
        q4.setLabel("Wind Direction");
        uom = new UnitReferenceImpl();
        uom.setCode("deg");
        q4.setUom(uom);
        rec.addField("wind_dir", q4);
        
        rec.getFieldList().add("rad", "http://mmisw.org/ont/cf/parameter/irradiance", null);
        
        DataArray ar1 = new DataArrayImpl();
        Count arSize = new CountImpl();
        arSize.setValue(1024);
        ar1.setElementCount(arSize);
        DataRecord rec1 = new DataRecordImpl();
        Quantity b1 = new QuantityImpl();
        b1.setDefinition("http://mmisw.org/ont/cf/parameter/radiance");
        uom = new UnitReferenceImpl();
        uom.setCode("W.sr-1.m2-um");
        b1.setUom(uom);
        rec1.addField("band1", b1);
        rec1.getFieldList().add("band2", "http://mmisw.org/ont/cf/parameter/blue", null);
        ar1.setElementType("pixel", rec1);
        ar1.setEncoding(new TextEncodingImpl());
        ar1.setValues(null);
        rec.addField("scanLine", ar1);
        
        // write to byte array
        ByteArrayOutputStream os = new ByteArrayOutputStream(10000);
        XMLStreamWriter writer = new IndentingXMLStreamWriter(output.createXMLStreamWriter(os));
        sweXmlBindings.setNamespacePrefixes(writer);
        writer.writeStartDocument();            
        sweXmlBindings.declareNamespacesOnRootElement();
        sweXmlBindings.writeDataRecord(writer, rec, true);
        writer.writeEndDocument();
        
        // write to sysout
        byte[] xmlData = os.toByteArray();
        //xmlData[292] = '0';
        for (byte b: xmlData)
            System.out.print((char)b);
        System.out.println();
        System.out.println();
        
        // read back
        ByteArrayInputStream is = new ByteArrayInputStream(xmlData);
        XMLStreamReader reader = input.createXMLStreamReader(is);
        reader.nextTag();
        rec = sweXmlBindings.readDataRecord(reader);
        
        // write back to sysout
        writer = new IndentingXMLStreamWriter(output.createXMLStreamWriter(System.out));
        sweXmlBindings.setNamespacePrefixes(writer);
        writer.writeStartDocument();
        sweXmlBindings.declareNamespacesOnRootElement();
        sweXmlBindings.writeDataRecord(writer, rec, true);
        writer.writeEndDocument();
        System.out.println();
                
        // performance tests
        int numLoops = 1000;
        int skipCount = 2;
        float read_avg = 0.0f;
        float write_avg = 0.0f;
        for (int i=0; i<numLoops; i++)
        {
            os.reset();
            writer = output.createXMLStreamWriter(os);
            sweXmlBindings.setNamespacePrefixes(writer);        
            
            long t0 = System.nanoTime();
            writer.writeStartDocument();
            sweXmlBindings.declareNamespacesOnRootElement();
            sweXmlBindings.writeDataRecord(writer, rec, true);
            writer.writeEndDocument();
            long t1 = System.nanoTime();
            
            int dt = (int)((t1-t0)/1000);
            //System.out.println("DataRecord written in " + dt + "us");            
            if (i >= skipCount)
                write_avg += dt;
            
            // read back
            is = new ByteArrayInputStream(os.toByteArray());
            reader = input.createXMLStreamReader(is);
            t0 = System.nanoTime();
            reader.nextTag();
            rec = sweXmlBindings.readDataRecord(reader);
            t1 = System.nanoTime();
            
            dt = (int)((t1-t0)/1000);
            //System.out.println("DataRecord read in " + dt + "us");
            if (i >= skipCount)
                read_avg += dt;
        }
        
        read_avg = read_avg / (numLoops - skipCount);
        write_avg = write_avg / (numLoops - skipCount);
        
        System.out.println("Average reading time = " + read_avg + "us");
        System.out.println("Average writing time = " + write_avg + "us");
    }
}
