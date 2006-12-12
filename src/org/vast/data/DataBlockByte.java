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

package org.vast.data;

import org.vast.cdm.common.DataType;


/**
 * <p><b>Title:</b><br/>
 * Byte Data Block
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Carries an array of byte primitives.
 * All data is casted to other types when requested.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 23, 2005
 * @version 1.0
 */
public class DataBlockByte extends AbstractDataBlock
{
	protected byte[] primitiveArray;
	
	
	public DataBlockByte()
	{
	}
	
	
	public DataBlockByte(int size)
	{
		resize(size);
	}
	
	
	public DataBlockByte copy()
	{
		DataBlockByte newBlock = new DataBlockByte();
		newBlock.primitiveArray = this.primitiveArray;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    public DataBlockByte renew()
    {
        DataBlockByte newBlock = new DataBlockByte();
        newBlock.primitiveArray = new byte[this.primitiveArray.length];
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
	
    
    public DataBlockByte clone()
    {
        DataBlockByte newBlock = new DataBlockByte();
        newBlock.primitiveArray = this.primitiveArray.clone();
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public byte[] getUnderlyingObject()
    {
        return primitiveArray;
    }
    
    
    public void setUnderlyingObject(byte[] primitiveArray)
    {
        this.primitiveArray = primitiveArray;
    }
    
	
	public DataType getDataType()
	{
		return DataType.BYTE;
	}


	public DataType getDataType(int index)
	{
		return DataType.BYTE;
	}


	public void resize(int size)
	{
		primitiveArray = new byte[size];
		this.atomCount = size;
	}
	
	
	public boolean getBooleanValue(int index)
	{
		return (primitiveArray[startIndex + index] == 0) ? false : true;
	}


	public byte getByteValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


	public short getShortValue(int index)
	{
		return (short)primitiveArray[startIndex + index];
	}


	public int getIntValue(int index)
	{
		return (int)primitiveArray[startIndex + index];
	}


	public long getLongValue(int index)
	{
		return (long)primitiveArray[startIndex + index];
	}


	public float getFloatValue(int index)
	{
		return (float)primitiveArray[startIndex + index];
	}


	public double getDoubleValue(int index)
	{
		return (double)primitiveArray[startIndex + index];
	}


	public String getStringValue(int index)
	{
		return Byte.toString(primitiveArray[startIndex + index]);
	}


	public boolean getBooleanValue()
	{
		return (primitiveArray[startIndex] == 0) ? false : true;
	}


	public byte getByteValue()
	{
		return primitiveArray[startIndex];
	}


	public short getShortValue()
	{
		return (short)primitiveArray[startIndex];
	}


	public int getIntValue()
	{
		return (int)primitiveArray[startIndex];
	}


	public long getLongValue()
	{
		return (long)primitiveArray[startIndex];
	}


	public float getFloatValue()
	{
		return (float)primitiveArray[startIndex];
	}


	public double getDoubleValue()
	{
		return (double)primitiveArray[startIndex];
	}


	public String getStringValue()
	{
		return Byte.toString(primitiveArray[startIndex]);
	}


	public void setBooleanValue(int index, boolean value)
	{
		primitiveArray[startIndex + index] = value ? DataBlockBoolean.trueVal : DataBlockBoolean.falseVal;
	}


	public void setByteValue(int index, byte value)
	{
		primitiveArray[startIndex + index] = value;
	}


	public void setShortValue(int index, short value)
	{
		primitiveArray[startIndex + index] = (byte)value;
	}


	public void setIntValue(int index, int value)
	{
		primitiveArray[startIndex + index] = (byte)value;
	}


	public void setLongValue(int index, long value)
	{
		primitiveArray[startIndex + index] = (byte)value;
	}


	public void setFloatValue(int index, float value)
	{
		primitiveArray[startIndex + index] = (byte)value;
	}


	public void setDoubleValue(int index, double value)
	{
		primitiveArray[startIndex + index] = (byte)value;
	}


	public void setStringValue(int index, String value)
	{
		primitiveArray[startIndex + index] = Byte.parseByte(value);
	}


	public void setBooleanValue(boolean value)
	{
		primitiveArray[startIndex] = value ? DataBlockBoolean.trueVal : DataBlockBoolean.falseVal;
	}


	public void setByteValue(byte value)
	{
		primitiveArray[startIndex] = value;
	}


	public void setShortValue(short value)
	{
		primitiveArray[startIndex] = (byte)value;
	}


	public void setIntValue(int value)
	{
		primitiveArray[startIndex] = (byte)value;
	}


	public void setLongValue(long value)
	{
		primitiveArray[startIndex] = (byte)value;
	}


	public void setFloatValue(float value)
	{
		primitiveArray[startIndex] = (byte)value;
	}


	public void setDoubleValue(double value)
	{
		primitiveArray[startIndex] = (byte)value;
	}


	public void setStringValue(String value)
	{
		primitiveArray[startIndex] = Byte.parseByte(value);
	}
}
