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
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Mar 5, 2008
 * */
public class SWEConstants
{
	// nil values
    public final static String NIL_ABOVE_MAX = "http://www.opengis.net/def/nil/OGC/0/AboveDetectionRange";
    public final static String NIL_BELOW_MIN = "http://www.opengis.net/def/nil/OGC/0/BelowDetectionRange";
    public final static String NIL_INAPPLICABLE = "http://www.opengis.net/def/nil/OGC/0/inapplicable";
    public final static String NIL_MISSING = "http://www.opengis.net/def/nil/OGC/0/missing";
    public final static String NIL_TEMPLATE = "http://www.opengis.net/def/nil/OGC/0/template";
    public final static String NIL_UNKNOWN = "http://www.opengis.net/def/nil/OGC/0/unknown";
    public final static String NIL_WITHHELD = "http://www.opengis.net/def/nil/OGC/0/withheld";
    
    // temporal reference systems
    public static final String TIME_REF_UTC = "http://www.opengis.net/def/trs/BIPM/0/UTC";
    public static final String TIME_REF_GPS = "http://www.opengis.net/def/trs/USNO/0/GPS";
    
    // EPSG crs URI prefix
    public static final String EPSG_URI_PREFIX = "http://www.opengis.net/def/crs/EPSG/0/";
        
    // other spatial reference frames
    public static final String REF_FRAME_4979 = EPSG_URI_PREFIX + 4979;
    public static final String REF_FRAME_4326 = EPSG_URI_PREFIX + 4326;
    public static final String REF_FRAME_ECEF = EPSG_URI_PREFIX + 4978;
    public static final String REF_FRAME_ECI_GCRF = "http://www.opengis.net/def/crs/IERS/0/ECI_GCRF";
    public static final String REF_FRAME_ECI_J2000 = "http://www.opengis.net/def/crs/IERS/0/ECI_J2000";
    public static final String REF_FRAME_ECI_M50 = "http://www.opengis.net/def/crs/IERS/0/ECI_M50";
    public static final String REF_FRAME_ENU = "http://www.opengis.net/def/crs/OGC/0/ENU";
    public static final String REF_FRAME_NED = "http://www.opengis.net/def/crs/OGC/0/NED";
    
    // vertical datums
    public static final String VERT_DATUM_EGM96_MSL = EPSG_URI_PREFIX + 5773;
    
    // OGC definition URIs
    public final static String DEF_SAMPLING_TIME = "http://www.opengis.net/def/property/OGC/0/SamplingTime";
    public final static String DEF_SAMPLING_LOC = "http://www.opengis.net/def/property/OGC/0/SamplingLocation";
    public final static String DEF_SENSOR_LOC = "http://www.opengis.net/def/property/OGC/0/SensorLocation";
    public final static String DEF_SENSOR_ORIENT = "http://www.opengis.net/def/property/OGC/0/SensorOrientation";
    public final static String DEF_PLATFORM_LOC = "http://www.opengis.net/def/property/OGC/0/PlatformLocation";
    public final static String DEF_PLATFORM_ORIENT = "http://www.opengis.net/def/property/OGC/0/PlatformOrientation";
    
    // SWE definition URIs
    public static final String SWE_PROP_URI_PREFIX = "http://sensorml.com/ont/swe/property/";
    public static final String DEF_SYSTEM_ID = SWE_PROP_URI_PREFIX + "SystemID";
    public static final String DEF_COORD = SWE_PROP_URI_PREFIX + "Coordinate";    
    public static final String DEF_IMAGE = SWE_PROP_URI_PREFIX + "Image";
    public static final String DEF_MATRIX = SWE_PROP_URI_PREFIX + "Matrix";
    
}
