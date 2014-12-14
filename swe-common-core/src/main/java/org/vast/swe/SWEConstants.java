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
 * 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @since 5 mars 08
 * @version 1.0
 */
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
	
	/* property values defined in SWE Common standards */
	public static final String DEF_SAMPLING_TIME = "http://www.opengis.net/def/property/OGC/0/SamplingTime";
	public static final String DEF_SAMPLING_LOC = "http://www.opengis.net/def/property/OGC/0/SamplingLocation";
    
}
