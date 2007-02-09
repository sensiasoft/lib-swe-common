/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.cdm.common;


/**
 * <p><b>Title:</b><br/>
 * Data Component
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Implementation of this class should store information relative
 * to the component structure of the data. (see swe:dataComponents).
 * It should give access to information such as component names, units,
 * definition, etc.. in a hierarchical manner.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 30, 2005
 * @version 1.0
 */
public interface DataComponent extends Cloneable
{
    public final static String META = "meta";
    public final static String DESC = "desc";
    public final static String NAMES = "names";
    public final static String REF = "refFrame"; 
    public final static String LOC = "locFrame";
    public final static String UOM_CODE = "uom_code";
    public final static String UOM_URI = "uom_uri";
    public final static String UOM_OBJ = "uom_obj";
    public final static String DEF_URI = "def_uri";
    public final static String DEF_OBJ = "def_obj";
    public final static String AXIS = "axis";
    public final static String SCALE = "scale";
    public final static String QUALITY = "quality";
    
    
    public int getComponentCount();


	public DataComponent getComponent(int index);


	public DataComponent getComponent(String id);
    
    
    public int getComponentIndex(String id);
	
	
	public void addComponent(DataComponent dataInfo);


	public void addComponent(String id, DataComponent dataInfo);
	
	
	public String getName();
	
	
	public void setName(String name);


	public Object getProperty(String propName);


	public void setProperty(String propName, Object propValue);
	
	
	public DataBlock getData();
	
	
	public void setData(DataBlock dataBlock);
    
    
    public void clearData();
    
    
    public DataComponent copy();
    
    
    public DataComponent clone();
    
    
    public void assignNewDataBlock();
    
    
    public void renewDataBlock();
}
