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
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.Vector;
import org.junit.Test;
import org.vast.data.TextEncodingImpl;
import org.vast.swe.SWEUtils;
import org.vast.swe.SWEConstants;
import org.vast.swe.SWEHelper;
import org.vast.swe.helper.GeoPosHelper;


public class TestGeoPosHelper
{
    GeoPosHelper fac = new GeoPosHelper();
    SWEUtils utils = new SWEUtils(SWEUtils.V2_0);
    
    
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
    public void testWrapWithTimeStamp() throws Exception
    {
        Vector vec = fac.newLocationVectorLLA(null);
        vec.setName("location");
        assertEquals(3, vec.getComponentCount());
        
        DataArray img = fac.newRgbImage(800, 600, DataType.FLOAT);
        img.setName("img");
        assertEquals(600, img.getComponentCount());
        
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
}
