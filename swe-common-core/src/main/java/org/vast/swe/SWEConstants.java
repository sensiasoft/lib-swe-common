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
 * Constants defined by SWE Common standard
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since 5 mars 08
 * */
public class SWEConstants
{
	/* component tag names */
    public final static String BOOL_COMPONENT_TAG = "Boolean";
    public final static String COUNT_COMPONENT_TAG = "Count";
    public final static String QUANTITY_COMPONENT_TAG = "Quantity";
    public final static String TIME_COMPONENT_TAG = "Time";
    public final static String TEXT_COMPONENT_TAG = "Text";
    public final static String CATEGORY_COMPONENT_TAG = "Category";
    public final static String DATARECORD_COMPONENT_TAG = "DataRecord";
    public final static String VECTOR_COMPONENT_TAG = "Vector";
    public final static String DATAARRAY_COMPONENT_TAG = "DataArray";
    public final static String MATRIX_COMPONENT_TAG = "Matrix";
    public final static String DATASTREAM_COMPONENT_TAG = "DataStream";
    public final static String DATACHOICE_COMPONENT_TAG = "DataChoice"; 	
	
	/* definition URIs */
	public final static String DEF_SAMPLING_TIME = "http://www.opengis.net/def/property/OGC/0/SamplingTime";
	public final static String DEF_SAMPLING_LOC = "http://www.opengis.net/def/property/OGC/0/SamplingLocation";
	public final static String DEF_SENSOR_LOC = "http://www.opengis.net/def/property/OGC/0/SensorLocation";
	public final static String DEF_SENSOR_ORIENT = "http://www.opengis.net/def/property/OGC/0/SensorOrientation";
	public final static String DEF_PLATFORM_LOC = "http://www.opengis.net/def/property/OGC/0/PlatformLocation";
    public final static String DEF_PLATFORM_ORIENT = "http://www.opengis.net/def/property/OGC/0/PlatformOrientation";
    
    /* nil values */
    public final static String NIL_ABOVE_MAX = "http://www.opengis.net/def/nil/OGC/0/AboveDetectionRange";
    public final static String NIL_BELOW_MIN = "http://www.opengis.net/def/nil/OGC/0/BelowDetectionRange";
    public final static String NIL_INAPPLICABLE = "http://www.opengis.net/def/nil/OGC/0/inapplicable";
    public final static String NIL_MISSING = "http://www.opengis.net/def/nil/OGC/0/missing";
    public final static String NIL_TEMPLATE = "http://www.opengis.net/def/nil/OGC/0/template";
    public final static String NIL_UNKNOWN = "http://www.opengis.net/def/nil/OGC/0/unknown";
    public final static String NIL_WITHHELD = "http://www.opengis.net/def/nil/OGC/0/withheld";
    
}
