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
import net.opengis.swe.v20.Vector;
import org.junit.Test;
import org.vast.data.BinaryEncodingImpl;
import org.vast.data.TextEncodingImpl;
import org.vast.swe.SWEUtils;
import org.vast.swe.SWEConstants;
import org.vast.swe.SWEHelper;


public class TestSweHelper
{
    SWEHelper fac = new SWEHelper();
    SWEUtils utils = new SWEUtils(SWEUtils.V2_0);
    
    
    @Test
    public void testCreateQuantities() throws Exception
    {
        utils.writeComponent(System.out, fac.newQuantity(SWEHelper.SWE_PROP_URI_PREFIX + "AirTemperature", "Air Temperature", "Temperature of air in the garden", "Cel"), false, true);
        utils.writeComponent(System.out, fac.newQuantity(SWEHelper.SWE_PROP_URI_PREFIX + "Acceleration", "Acceleration", null, "m/s2"), false, true);
    }
    
    
    @Test
    public void testCreateLocationVectors() throws Exception
    {
        utils.writeComponent(System.out, fac.newLocationVectorLLA(SWEConstants.DEF_SENSOR_LOC), false, true);
        utils.writeComponent(System.out, fac.newLocationVectorECEF(SWEConstants.DEF_SENSOR_LOC), false, true);
    }

    
    @Test
    public void testCreateOrientationVectors() throws Exception
    {
        // orientation vectors
        utils.writeComponent(System.out, fac.newEulerOrientationENU(SWEConstants.DEF_SENSOR_ORIENT), false, true);
        utils.writeComponent(System.out, fac.newEulerOrientationNED(SWEConstants.DEF_SENSOR_ORIENT), false, true);
        utils.writeComponent(System.out, fac.newEulerOrientationECEF(SWEConstants.DEF_PLATFORM_ORIENT), false, true);
        
        // quaternions
        utils.writeComponent(System.out, fac.newQuatOrientationENU(SWEConstants.DEF_SENSOR_ORIENT), false, true);
        utils.writeComponent(System.out, fac.newQuatOrientationNED(SWEConstants.DEF_SENSOR_ORIENT), false, true);
        utils.writeComponent(System.out, fac.newQuatOrientationECEF(SWEConstants.DEF_PLATFORM_ORIENT), false, true);
    }
    
    
    @Test
    public void testCreateRasters() throws Exception
    {
        // RGB
        utils.writeComponent(System.out, fac.newRgbImage(640, 480, DataType.BYTE), false, true);
    }
    
    
    protected DataRecord createWeatherRecord() throws Exception
    {
        DataRecord rec = fac.newDataRecord(3);
        rec.addField("time", fac.newTimeStampIsoUTC());
        rec.addField("temp", fac.newQuantity(SWEHelper.getPropertyUri("AirTemperature"), "Air Temperature", null, "Cel"));
        rec.addField("press", fac.newQuantity(SWEHelper.getPropertyUri("AtmosphericPressure"), "Air Pressure", null, "hPa"));
        rec.addField("windSpeed", fac.newQuantity(SWEHelper.getPropertyUri("WindSpeed"), "Wind Speed", null, "km/h"));
        return rec;
    }
    
    
    protected DataRecord createWeatherRecordWithLocation() throws Exception
    {
        DataRecord rec = createWeatherRecord();
        rec.addField("location", fac.newLocationVectorLLA(null));
        return rec;
    }
    
    
    @Test
    public void testCreateWeatherRecord() throws Exception
    {
        utils.writeComponent(System.out, createWeatherRecord(), false, true);
        utils.writeComponent(System.out, createWeatherRecordWithLocation(), false, true);
    }
    
    
    @Test
    public void testWrapWithTimeStamp() throws Exception
    {
        Vector vec = fac.newLocationVectorLLA(null);
        vec.setName("location");
        DataArray img = fac.newRgbImage(800, 600, DataType.FLOAT);
        img.setName("img");
        DataRecord rec = fac.wrapWithTimeStamp(fac.newTimeStampIsoUTC(), vec, img);
        utils.writeComponent(System.out, rec, false, true);
    }
    
    
    @Test
    public void testCreateDefaultEncodingForVector() throws Exception
    {
        Vector vec = fac.newLocationVectorLLA(null);
        DataEncoding encoding = SWEHelper.getDefaultEncoding(vec);
        assertEquals(TextEncodingImpl.class, encoding.getClass());
        utils.writeEncoding(System.out, encoding, true);
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
        DataRecord rec = createWeatherRecordWithLocation();
        DataComponent c;
        
        c = SWEHelper.findComponentByPath(rec, "temp");
        assertEquals(c.getName(), "temp");
        
        c = SWEHelper.findComponentByPath(rec, "location/lat");
        assertEquals(c.getName(), "lat");
    }
}
