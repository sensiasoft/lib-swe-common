/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/
package org.vast.swe;

/**
 * <p>
 * Constants defined by SWE Common standard and other commonly used URIs
 * </p>
 *
 * @author Alex Robin
 * @since Mar 5, 2008
 * */
public class SWEConstants
{
    public static final String URN_PREFIX = "urn:";
    public static final String HTTP_PREFIX = "http://";
    public static final String OGC_DEF_URI = "http://www.opengis.net/def/";
	
    // nil values
	public static final String OGC_NIL_URI = OGC_DEF_URI + "nil/OGC/0/";
    public static final String NIL_ABOVE_MAX = OGC_NIL_URI + "AboveDetectionRange";
    public static final String NIL_BELOW_MIN = OGC_NIL_URI + "BelowDetectionRange";
    public static final String NIL_INAPPLICABLE = OGC_NIL_URI + "inapplicable";
    public static final String NIL_MISSING = OGC_NIL_URI + "missing";
    public static final String NIL_TEMPLATE = OGC_NIL_URI + "template";
    public static final String NIL_UNKNOWN = OGC_NIL_URI + "unknown";
    public static final String NIL_WITHHELD = OGC_NIL_URI + "withheld";
    
    // common temporal reference systems
    public static final String TIME_REF_UTC = OGC_DEF_URI + "trs/BIPM/0/UTC";
    public static final String TIME_REF_GPS = OGC_DEF_URI + "trs/USNO/0/GPS";
    public static final String TIME_REF_TAI = OGC_DEF_URI + "trs/BIPM/0/TAI";
    
    // common spatial reference frames
    public static final String OGC_CRS_URI = OGC_DEF_URI + "crs/";
    public static final String EPSG_URI_PREFIX = OGC_CRS_URI + "EPSG/0/";
    public static final String REF_FRAME_4979 = EPSG_URI_PREFIX + 4979;
    public static final String REF_FRAME_4326 = EPSG_URI_PREFIX + 4326;
    public static final String REF_FRAME_ECEF = EPSG_URI_PREFIX + 4978;
    public static final String REF_FRAME_ECI_GCRF = OGC_CRS_URI + "IERS/0/ECI_GCRF";
    public static final String REF_FRAME_ECI_J2000 = OGC_CRS_URI + "IERS/0/ECI_J2000";
    public static final String REF_FRAME_ECI_M50 = OGC_CRS_URI + "IERS/0/ECI_M50";
    public static final String REF_FRAME_ENU = OGC_CRS_URI + "OGC/0/ENU";
    public static final String REF_FRAME_NED = OGC_CRS_URI + "OGC/0/NED";
    
    // common vertical datums
    public static final String VERT_DATUM_EGM96_MSL = EPSG_URI_PREFIX + 5773;
    
    // OGC definition URIs
    public static final String OGC_PROP_URI = OGC_DEF_URI + "property/OGC/0/";
    public static final String DEF_ARRAY_SIZE = OGC_PROP_URI + "ArraySize";
    public static final String DEF_NUM_POINTS = OGC_PROP_URI + "NumberOfPoints";
    public static final String DEF_NUM_ROWS = OGC_PROP_URI + "NumberOfRows";
    public static final String DEF_NUM_SAMPLES = OGC_PROP_URI + "NumberOfSamples";
    public static final String DEF_PHENOMENON_TIME = OGC_PROP_URI + "PhenomenonTime";
    public static final String DEF_SAMPLING_TIME = OGC_PROP_URI + "SamplingTime";
    public static final String DEF_FORECAST_TIME = OGC_PROP_URI + "ForecastTime";
    public static final String DEF_RUN_TIME = OGC_PROP_URI + "RunTime";
    public static final String DEF_MISSION_START_TIME = OGC_PROP_URI + "MissionStartTime";
    public static final String DEF_SCAN_START_TIME = OGC_PROP_URI + "ScanStartTime";
    public static final String DEF_SAMPLING_LOC = OGC_PROP_URI + "SamplingLocation";
    public static final String DEF_SENSOR_LOC = OGC_PROP_URI + "SensorLocation";
    public static final String DEF_SENSOR_ORIENT = OGC_PROP_URI + "SensorOrientation";
    public static final String DEF_PLATFORM_LOC = OGC_PROP_URI + "PlatformLocation";
    public static final String DEF_PLATFORM_ORIENT = OGC_PROP_URI + "PlatformOrientation";
    public static final String DEF_SENSOR_TYPE = OGC_PROP_URI + "SensorType";
    public static final String DEF_SENSOR_STATUS = OGC_PROP_URI + "SensorStatus";
    public static final String DEF_PLATFORM_TYPE = OGC_PROP_URI + "PlatformType";
    public static final String DEF_FOI_ID = OGC_PROP_URI + "FeatureOfInterestID";
    
    // SWE definition URIs
    public static final String SWE_PROP_URI_PREFIX = "http://sensorml.com/ont/swe/property/";
    public static final String DEF_SYSTEM_ID = SWE_PROP_URI_PREFIX + "SystemID";
    public static final String DEF_COORD = SWE_PROP_URI_PREFIX + "Coordinate";    
    public static final String DEF_IMAGE = SWE_PROP_URI_PREFIX + "Image";
    public static final String DEF_MATRIX = SWE_PROP_URI_PREFIX + "Matrix";
    public static final String DEF_DN = SWE_PROP_URI_PREFIX + "DN";
    public static final String DEF_FLAG = SWE_PROP_URI_PREFIX + "Flag";
    
    // SWE special units
    public static final String SWE_UOM_URI_PREFIX = "http://sensorml.com/ont/swe/uom/";
    public static final String UOM_ANY = SWE_UOM_URI_PREFIX + "Any";
    public static final String UOM_ANY_CODE = "any";
    public static final String UOM_UNITLESS = "1";
    
    
    private SWEConstants()
    {        
    }
}
