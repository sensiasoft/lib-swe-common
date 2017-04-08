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
	public final static String OGC_DEF_URI = "http://www.opengis.net/def/";
	
    // nil values
	public final static String OGC_NIL_URI = OGC_DEF_URI + "nil/OGC/0/";
    public final static String NIL_ABOVE_MAX = OGC_NIL_URI + "AboveDetectionRange";
    public final static String NIL_BELOW_MIN = OGC_NIL_URI + "BelowDetectionRange";
    public final static String NIL_INAPPLICABLE = OGC_NIL_URI + "inapplicable";
    public final static String NIL_MISSING = OGC_NIL_URI + "missing";
    public final static String NIL_TEMPLATE = OGC_NIL_URI + "template";
    public final static String NIL_UNKNOWN = OGC_NIL_URI + "unknown";
    public final static String NIL_WITHHELD = OGC_NIL_URI + "withheld";
    
    // temporal reference systems
    public static final String TIME_REF_UTC = OGC_DEF_URI + "trs/BIPM/0/UTC";
    public static final String TIME_REF_GPS = OGC_DEF_URI + "trs/USNO/0/GPS";
    
    // other spatial reference frames
    public final static String OGC_CRS_URI = OGC_DEF_URI + "crs/";
    public static final String EPSG_URI_PREFIX = OGC_CRS_URI + "EPSG/0/";
    public static final String REF_FRAME_4979 = EPSG_URI_PREFIX + 4979;
    public static final String REF_FRAME_4326 = EPSG_URI_PREFIX + 4326;
    public static final String REF_FRAME_ECEF = EPSG_URI_PREFIX + 4978;
    public static final String REF_FRAME_ECI_GCRF = OGC_CRS_URI + "IERS/0/ECI_GCRF";
    public static final String REF_FRAME_ECI_J2000 = OGC_CRS_URI + "IERS/0/ECI_J2000";
    public static final String REF_FRAME_ECI_M50 = OGC_CRS_URI + "IERS/0/ECI_M50";
    public static final String REF_FRAME_ENU = OGC_CRS_URI + "OGC/0/ENU";
    public static final String REF_FRAME_NED = OGC_CRS_URI + "OGC/0/NED";
    
    // vertical datums
    public static final String VERT_DATUM_EGM96_MSL = EPSG_URI_PREFIX + 5773;
    
    // OGC definition URIs
    public final static String OGC_PROP_URI = OGC_DEF_URI + "property/OGC/0/";
    public final static String DEF_SAMPLING_TIME = OGC_PROP_URI + "SamplingTime";
    public final static String DEF_SAMPLING_LOC = OGC_PROP_URI + "SamplingLocation";
    public final static String DEF_SENSOR_LOC = OGC_PROP_URI + "SensorLocation";
    public final static String DEF_SENSOR_ORIENT = OGC_PROP_URI + "SensorOrientation";
    public final static String DEF_PLATFORM_LOC = OGC_PROP_URI + "PlatformLocation";
    public final static String DEF_PLATFORM_ORIENT = OGC_PROP_URI + "PlatformOrientation";
    public final static String DEF_FOI_ID = OGC_PROP_URI + "FeatureOfInterestID";
    
    // SWE definition URIs
    public static final String SWE_PROP_URI_PREFIX = "http://sensorml.com/ont/swe/property/";
    public static final String DEF_SYSTEM_ID = SWE_PROP_URI_PREFIX + "SystemID";
    public static final String DEF_COORD = SWE_PROP_URI_PREFIX + "Coordinate";    
    public static final String DEF_IMAGE = SWE_PROP_URI_PREFIX + "Image";
    public static final String DEF_MATRIX = SWE_PROP_URI_PREFIX + "Matrix";
    
}
