/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.cdm.common;

import java.util.List;


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
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Nov 30, 2005
 * @version 1.0
 */
public interface DataComponent extends Cloneable
{
    
	public int getComponentCount();


	public DataComponent getComponent(int index);


	public DataComponent getComponent(String id);
    
    
    public int getComponentIndex(String id);
	
	
	public void addComponent(DataComponent dataInfo);


	public void addComponent(String id, DataComponent dataInfo);
	
	
	public String getName();
	
	
	public DataComponent getParent();
	
	
	public void setName(String name);


	public Object getProperty(String propName);


	public void setProperty(String propName, Object propValue);
	
	
	public EncodingInfo getEncodingInfo();
	
	
	public void setEncodingInfo(EncodingInfo encInfo);
	
	
	public DataBlock getData();
	
	
	public void setData(DataBlock dataBlock);
    
    
    public void clearData();
    
    
    public void validateData(List<CDMException> errorList);
    
    
    public boolean hasConstraints();
    
    
    public DataComponent copy();
    
    
    public DataComponent clone();
    
    
    public DataBlock createDataBlock();
    
    
    public void assignNewDataBlock();
    
    
    public void renewDataBlock();
}
