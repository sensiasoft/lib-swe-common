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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import net.opengis.swe.v20.AbstractSWE;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataStream;
import org.vast.swe.SWEStaxBindings;
import org.vast.swe.json.SWEJsonStreamReader;
import org.vast.swe.json.SWEJsonStreamWriter;
import org.vast.xml.IndentingXMLStreamWriter;
import junit.framework.TestCase;
import static javax.xml.stream.XMLStreamReader.*;


public class TestSweJsonBindingsV20 extends TestCase
{


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
    
    
    protected void writeSweCommonJsonToStream(AbstractSWE sweObj, OutputStream os, boolean indent) throws Exception
    {
        SWEStaxBindings sweHelper = new SWEStaxBindings();
        SWEJsonStreamWriter writer = new SWEJsonStreamWriter(os, "UTF-8");
                
        sweHelper.setNamespacePrefixes(writer);
        writer.writeStartDocument();
        sweHelper.declareNamespacesOnRootElement();
        if (sweObj instanceof DataStream)
            sweHelper.writeDataStream(writer, (DataStream)sweObj);
        else
            sweHelper.writeDataComponent(writer, (DataComponent)sweObj, true);
        writer.writeEndDocument();
        writer.flush();
    }
    
    
    protected void readXmlWriteJson(String path) throws Exception
    {
        readXmlWriteJson(path, false);
    }
    
    
    protected void readXmlWriteJson(String path, boolean isDataStream) throws Exception
    {
        try
        {
            AbstractSWE sweObj = readSweCommonXml(path, isDataStream);
            
            // write JSON to stdout and buffer
            writeSweCommonJsonToStream(sweObj, System.out, true);
            System.out.println('\n');
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeSweCommonJsonToStream(sweObj, os, false);
            
            // read back to check JSON is well formed
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            SWEJsonStreamReader jsonReader = new SWEJsonStreamReader(is, "UTF-8");            
            XMLOutputFactory output = new com.ctc.wstx.stax.WstxOutputFactory();
            XMLStreamWriter xmlWriter = output.createXMLStreamWriter(System.out);
            xmlWriter = new IndentingXMLStreamWriter(xmlWriter);
            
            while (jsonReader.hasNext())
            {
                switch (jsonReader.next())
                {
                    case START_ELEMENT:
                        String name = jsonReader.getLocalName();
                        xmlWriter.writeStartElement(name);
                        for(int i=0; i<jsonReader.getAttributeCount(); i++)
                        {
                            name = jsonReader.getAttributeLocalName(i);
                            String val = jsonReader.getAttributeValue(i);
                            xmlWriter.writeAttribute(name, val);
                        }
                        break;
                        
                    case END_ELEMENT:
                        xmlWriter.writeEndElement();
                        break;
                        
                    case CHARACTERS:
                        xmlWriter.writeCharacters(jsonReader.getText());
                        break;
                }
                
                xmlWriter.flush();
            }
            
            System.out.println("\n\n");
            // read back to check JSON is well formed
            //InputSource src1 = new InputSource(getClass().getResourceAsStream(path));
            //InputSource src2 = new InputSource(new ByteArrayInputStream(os.toByteArray()));
            //assertXMLEqual(src1, src2);
        }
        catch (Throwable e)
        {
            throw new Exception("Failed test " + path, e);
        }
    }
    
    
    public void testReadWriteScalars() throws Exception
    {
        readXmlWriteJson("examples_v20/spec/simple_components.xml");
    }
    
    
    public void testReadWriteRecord() throws Exception
    {
        readXmlWriteJson("examples_v20/spec/record_weather.xml");
        readXmlWriteJson("examples_v20/spec/record_coefs.xml");
        readXmlWriteJson("examples_v20/spec/range_components.xml");
        readXmlWriteJson("examples_v20/sps/TaskingParameter_DataRecord.xml");
    }
    
    
    public void testReadWriteRecordWithConstraints() throws Exception
    {
        readXmlWriteJson("examples_v20/sps/TaskingParameter_DataRecord_constraints.xml");
        readXmlWriteJson("examples_v20/spec/constraints.xml");
    }
    
    
    public void testReadWriteRecordWithOptionals() throws Exception
    {
        readXmlWriteJson("examples_v20/sps/TaskingParameter_DataRecord_optional.xml");
    }
    
    
    public void testReadWriteRecordWithNilValues() throws Exception
    {
        readXmlWriteJson("examples_v20/spec/nilValues.xml");
    }
    
    
    public void testReadWriteRecordWithQuality() throws Exception
    {
        readXmlWriteJson("examples_v20/spec/quality.xml");
    }
    
    
    public void testReadWriteRecordWithXlinks() throws Exception
    {
        readXmlWriteJson("examples_v20/spec/record_weather_xlinks.xml");
    }
    
    
    public void testReadWriteVector() throws Exception
    {
        readXmlWriteJson("examples_v20/spec/vector_location.xml");
        readXmlWriteJson("examples_v20/spec/vector_quaternion.xml");
        readXmlWriteJson("examples_v20/spec/vector_velocity.xml");
    }
    
    
    public void testReadWriteArrayNoData() throws Exception
    {        
        readXmlWriteJson("examples_v20/spec/array_trajectory.xml");
        readXmlWriteJson("examples_v20/spec/array_image_band_interleaved.xml");
        readXmlWriteJson("examples_v20/spec/array_image_pixel_interleaved.xml");
    }
    
    
    public void testReadWriteArrayWithTextData() throws Exception
    {        
        readXmlWriteJson("examples_v20/spec/array_weather.xml");
        readXmlWriteJson("examples_v20/spec/enc_text_curve.xml");
        readXmlWriteJson("examples_v20/spec/enc_text_profile_series.xml");
        readXmlWriteJson("examples_v20/spec/enc_text_stress_matrix.xml");
        readXmlWriteJson("examples_v20/spec/matrix_rotation.xml");
    }
    
    
    /*public void testReadWriteArrayWithXmlData() throws Exception
    {        
        readWriteCompareSweCommonXml("examples_v20/spec/enc_xml_curve.xml");
        readWriteCompareSweCommonXml("examples_v20/spec/enc_xml_profile_series.xml");
    }*/
    
    
    public void testReadWriteDataChoice() throws Exception
    {
        readXmlWriteJson("examples_v20/spec/choice_stream.xml");
    }
    
    
    public void testReadWriteDataStream() throws Exception
    {
        readXmlWriteJson("examples_v20/weather_data.xml", true);
        readXmlWriteJson("examples_v20/nav_data.xml", true);
        readXmlWriteJson("examples_v20/image_data.xml", true);
        readXmlWriteJson("examples_v20/spec/datastream_with_quality.xml", true);
    }
    
    
    public void testReadWriteDataStreamWithChoice() throws Exception
    {
        readXmlWriteJson("examples_v20/spec/enc_text_choice_stream.xml", true);
    }
}
