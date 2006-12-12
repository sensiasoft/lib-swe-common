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
 * Data Block
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Implementations of this class should be able to carry data
 * for any part of the data cluster defined in the DataDefinition
 * part of the Common Data Model. It should provide access to any
 * data atom through primitive specific methods. Data should be
 * casted from one primitive to another when needed and possible.
 * In order to improve performance, no check needs to be enforced on
 * the index argument (i.e 0 < index < blockSize - 1), thus all
 * getXXXValue(int index) and setXXXValue(int index, ...) do not
 * need to guarantee their bahavior when an invalid index is used.
 * IndexOutOfBoundException can be thrown in some cases.  
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Aug 14, 2005
 * @version 1.0
 */
public interface DataBlock extends Cloneable
{
	public DataType getDataType();
	
	
	public DataType getDataType(int index);
	
	
	public int getAtomCount();
	
	
	public boolean getBooleanValue(int index);
	
	
	public byte getByteValue(int index);


	public short getShortValue(int index);


	public int getIntValue(int index);


	public long getLongValue(int index);


	public float getFloatValue(int index);


	public double getDoubleValue(int index);
	
	
	public String getStringValue(int index);


	public boolean getBooleanValue();
	
	
	public byte getByteValue();


	public short getShortValue();


	public int getIntValue();


	public long getLongValue();


	public float getFloatValue();


	public double getDoubleValue();
	
	
	public String getStringValue();


	public void setBooleanValue(int index, boolean value);
			
			
	public void setByteValue(int index, byte value);
	
	
	public void setShortValue(int index, short value);


	public void setIntValue(int index, int value);


	public void setLongValue(int index, long value);


	public void setFloatValue(int index, float value);


	public void setDoubleValue(int index, double value);
	
	
	public void setStringValue(int index, String value);


	public void setBooleanValue(boolean value);
	
	
	public void setByteValue(byte value);


	public void setShortValue(short value);


	public void setIntValue(int value);


	public void setLongValue(long value);


	public void setFloatValue(float value);


	public void setDoubleValue(double value);
	
	
	public void setStringValue(String value);
	
	
	public void resize(int size);
    
    
    public DataBlock copy();
    
    
    public DataBlock clone();
}
