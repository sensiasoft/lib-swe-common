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
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.DataType;
import org.junit.Test;
import org.vast.data.BinaryEncodingImpl;
import org.vast.swe.SWEUtils;
import org.vast.swe.SWEBuilders;
import org.vast.swe.SWEHelper;


public class TestSweBuilders
{
    SWEUtils utils = new SWEUtils(SWEUtils.V2_0);
    
    
    @Test
    public void testCreateQuantities() throws Exception
    {
        utils.writeComponent(System.out, SWEBuilders.newQuantity()
            .withDefinition(SWEHelper.getPropertyUri("AirTemperature"))
            .withLabel("Air Temperature")
            .withDescription("Temperature of air in the garden")
            .withUomCode("Cel")
            .build(), false, true);
        System.out.println();
        System.out.println();
        
        utils.writeComponent(System.out, SWEBuilders.newQuantity()
            .withDefinition(SWEHelper.getPropertyUri("LinearAcceleration"))
            .withLabel("Acceleration")
            .withUomCode("m/s2")
            .build(), false, true);
        System.out.println();
        System.out.println();
    }
    
    
    /*@Test
    public void testCreateRasters() throws Exception
    {
        // RGB
        utils.writeComponent(System.out, fac.newRgbImage(640, 480, DataType.BYTE), false, true);
    }*/
    
    
    @Test
    public void testCreateWeatherRecord() throws Exception
    {
        utils.writeComponent(System.out, SWEBuilders.newRecord()
            .withLabel("Weather Record")
            .withIsoTimeStampUTC("time")
            .withQuantityField("temp")
                .withDefinition(SWEHelper.getPropertyUri("AirTemperature"))
                .withLabel("Air Temperature")
                .withUomCode("Cel")
                .done()
            .withQuantityField("press")
                .withDefinition(SWEHelper.getPropertyUri("AtmosphericPressure"))
                .withLabel("Air Pressure")
                .withUomCode("hPa")
                .done()
            .withQuantityField("windSpeed")
                .withDefinition(SWEHelper.getPropertyUri("WindSpeed"))
                .withLabel("Wind Speed")
                .withUomCode("km/h")
                .done()
            .build(), false, true);
    }
    
    
    /*@Test
    public void testWrapWithTimeStamp() throws Exception
    {
        DataRecord rec = createWeatherRecord();
        rec.setName("weather");
        DataArray img = fac.newRgbImage(800, 600, DataType.FLOAT);
        img.setName("img");
        DataRecord all = fac.wrapWithTimeStamp(fac.newTimeStampIsoUTC(), rec, img);
        utils.writeComponent(System.out, all, false, true);
    }
    
    
    @Test
    public void testCreateDefaultEncodingForImage() throws Exception
    {
        DataArray array = fac.newRgbImage(640, 480, DataType.BYTE);
        DataEncoding encoding = SWEHelper.getDefaultEncoding(array);
        assertEquals(BinaryEncodingImpl.class, encoding.getClass());
        utils.writeEncoding(System.out, encoding, true);
    }
    
    
    @Test
    public void testGetComponentByPath() throws Exception
    {
        DataRecord rec = createWeatherRecord();
        DataComponent c;
        
        c = SWEHelper.findComponentByPath(rec, "temp");
        assertEquals(c.getName(), "temp");
        
        c = SWEHelper.findComponentByPath(rec, "press");
        assertEquals(c.getName(), "press");
    }*/
}
