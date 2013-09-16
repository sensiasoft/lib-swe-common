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
package org.vast.sweCommon;

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
public class SweConstants
{
	public final static String ELT_COUNT_NAME = "elementCount";
	public final static String MIN_VALUE_NAME = "min";
	public final static String MAX_VALUE_NAME = "max";
	
	/* property names used to hold soft-typed information in DataComponents */ 
	public final static String COMP_QNAME = "obj_qname"; // Component Elt QName
	public final static String ID = "id"; // String
	public final static String META = "meta"; // Object
	public final static String DESC = "desc"; // String
	public final static String NAME = "name"; // String
	public final static String REF_FRAME = "ref_frame"; // String
	public final static String REF_TIME = "ref_time"; // Double
	public final static String LOCAL_FRAME = "loc_frame"; // String
	public final static String CRS = "crs"; // String
	public final static String AXIS_CODE = "axis_code"; // String
	public final static String DEF_URI = "def_uri"; // String
	public final static String DEF_OBJ = "def_obj";
	public final static String UOM_CODE = "uom_code"; // String
	public final static String UOM_URI = "uom_uri"; // String
	public final static String UOM_OBJ = "uom_obj"; // Unit
	public final static String DIC_URI = "dic_uri"; // String
	public final static String DIC_OBJ = "dic_obj";
	public final static String QUALITY = "quality"; // DataComponent
	public final static String UPDATABLE = "updatable"; // Boolean
	public final static String OPTIONAL = "optional"; // Boolean
	public final static String NIL_VALUES = "nil_values"; // NilValues object
	public static final String ENCODING_TYPE = "encoding"; // DataEncoding object for DataArray
	
	/* property values defined in SWE Common standards */
	public static final String ISO_TIME_DEF = "http://www.opengis.net/def/uom/ISO-8601/0/Gregorian";
}
