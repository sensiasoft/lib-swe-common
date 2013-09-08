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

package org.vast.data;

import org.vast.cdm.common.DataType;


/**
 * <p><b>Title:</b><br/>
 * Boolean Data Block
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Carries an array of boolean primitives.
 * All data is casted to other types when requested.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Nov 23, 2005
 * @version 1.0
 */
public class DataBlockBoolean extends AbstractDataBlock
{
	private static final long serialVersionUID = 6984929839422362525L;
    public static byte trueVal = 1;
	public static byte falseVal = 0;
	protected boolean[] primitiveArray;
	
	
	public DataBlockBoolean()
	{
	}
	
	
	public DataBlockBoolean(int size)
	{
		resize(size);
	}
	
	
	public DataBlockBoolean copy()
	{
		DataBlockBoolean newBlock = new DataBlockBoolean();
		newBlock.primitiveArray = this.primitiveArray;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    public DataBlockBoolean renew()
    {
        DataBlockBoolean newBlock = new DataBlockBoolean();
        newBlock.primitiveArray = new boolean[this.atomCount];
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public DataBlockBoolean clone()
    {
        DataBlockBoolean newBlock = new DataBlockBoolean();
        //newBlock.primitiveArray = this.primitiveArray.clone();
        newBlock.primitiveArray = new boolean[this.atomCount];
        System.arraycopy(this.primitiveArray, 0, newBlock.primitiveArray, 0, this.atomCount);
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public boolean[] getUnderlyingObject()
    {
        return primitiveArray;
    }
    
    
    public void setUnderlyingObject(boolean[] primitiveArray)
    {
        this.primitiveArray = primitiveArray;
    }
    
    
    public void setUnderlyingObject(Object obj)
    {
    	this.primitiveArray = (boolean[])obj;
    }
	
	
	public DataType getDataType()
	{
		return DataType.BOOLEAN;
	}


	public DataType getDataType(int index)
	{
		return DataType.BOOLEAN;
	}
	
	
	public void resize(int size)
	{
		primitiveArray = new boolean[size];
		this.atomCount = size;
	}


	public boolean getBooleanValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


	public byte getByteValue(int index)
	{
		return primitiveArray[startIndex + index] ? trueVal : falseVal;
	}


	public short getShortValue(int index)
	{
		return primitiveArray[startIndex + index] ? trueVal : falseVal;
	}


	public int getIntValue(int index)
	{
		return primitiveArray[startIndex + index] ? trueVal : falseVal;
	}


	public long getLongValue(int index)
	{
		return primitiveArray[startIndex + index] ? trueVal : falseVal;
	}


	public float getFloatValue(int index)
	{
		return primitiveArray[startIndex + index] ? trueVal : falseVal;
	}


	public double getDoubleValue(int index)
	{
		return primitiveArray[startIndex + index] ? trueVal : falseVal;
	}


	public String getStringValue(int index)
	{
		return Boolean.toString(primitiveArray[startIndex + index]);
	}


	public boolean getBooleanValue()
	{
		return primitiveArray[startIndex];
	}


	public byte getByteValue()
	{
		return primitiveArray[startIndex] ? trueVal : falseVal;
	}


	public short getShortValue()
	{
		return primitiveArray[startIndex] ? trueVal : falseVal;
	}


	public int getIntValue()
	{
		return primitiveArray[startIndex] ? trueVal : falseVal;
	}


	public long getLongValue()
	{
		return primitiveArray[startIndex] ? trueVal : falseVal;
	}


	public float getFloatValue()
	{
		return primitiveArray[startIndex] ? trueVal : falseVal;
	}


	public double getDoubleValue()
	{
		return primitiveArray[startIndex] ? trueVal : falseVal;
	}


	public String getStringValue()
	{
		return Boolean.toString(primitiveArray[startIndex]);
	}


	public void setBooleanValue(int index, boolean value)
	{
		primitiveArray[startIndex + index] = value;
	}


	public void setByteValue(int index, byte value)
	{
		primitiveArray[startIndex + index] = (value == 0) ? false : true;
	}


	public void setShortValue(int index, short value)
	{
		primitiveArray[startIndex + index] = (value == 0) ? false : true;
	}


	public void setIntValue(int index, int value)
	{
		primitiveArray[startIndex + index] = (value == 0) ? false : true;
	}


	public void setLongValue(int index, long value)
	{
		primitiveArray[startIndex + index] = (value == 0) ? false : true;
	}


	public void setFloatValue(int index, float value)
	{
		primitiveArray[startIndex + index] = (Float.isNaN(value) || value == 0) ? false : true;
	}


	public void setDoubleValue(int index, double value)
	{
		primitiveArray[startIndex + index] = (Double.isNaN(value) || value == 0) ? false : true;
	}


	public void setStringValue(int index, String value)
	{
		primitiveArray[startIndex + index] = Boolean.parseBoolean(value);
	}


	public void setBooleanValue(boolean value)
	{
		primitiveArray[startIndex] = value;
	}


	public void setByteValue(byte value)
	{
		primitiveArray[startIndex] = (value == 0) ? false : true;
	}


	public void setShortValue(short value)
	{
		primitiveArray[startIndex] = (value == 0) ? false : true;
	}


	public void setIntValue(int value)
	{
		primitiveArray[startIndex] = (value == 0) ? false : true;
	}


	public void setLongValue(long value)
	{
		primitiveArray[startIndex] = (value == 0) ? false : true;
	}


	public void setFloatValue(float value)
	{
		primitiveArray[startIndex] = (Float.isNaN(value) || value == 0) ? false : true;
	}


	public void setDoubleValue(double value)
	{
		primitiveArray[startIndex] = (Double.isNaN(value) || value == 0) ? false : true;
	}


	public void setStringValue(String value)
	{
		primitiveArray[startIndex] = Boolean.parseBoolean(value);
	}
}
