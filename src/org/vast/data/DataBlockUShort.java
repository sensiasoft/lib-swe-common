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
 * Unsigned Short Data Block
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Carries an array of short primitives.
 * All data is casted to other types when requested and in a way
 * that returns unsigned values even for values > 2^15
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Nov 23, 2005
 * @version 1.0
 */
public class DataBlockUShort extends AbstractDataBlock
{
	protected short[] primitiveArray;
	
	
	public DataBlockUShort()
	{
	}
	
	
	public DataBlockUShort(int size)
	{
		resize(size);
	}
	
	
	public DataBlockUShort copy()
	{
		DataBlockUShort newBlock = new DataBlockUShort();
		newBlock.primitiveArray = this.primitiveArray;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    public DataBlockUShort renew()
    {
        DataBlockUShort newBlock = new DataBlockUShort();
        newBlock.primitiveArray = new short[this.primitiveArray.length];
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public DataBlockUShort clone()
    {
        DataBlockUShort newBlock = new DataBlockUShort();
        newBlock.primitiveArray = this.primitiveArray.clone();
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public short[] getUnderlyingObject()
    {
        return primitiveArray;
    }
    
    
    public void setUnderlyingObject(short[] primitiveArray)
    {
        this.primitiveArray = primitiveArray;
        this.atomCount = primitiveArray.length;
    }
    
    
    public void setUnderlyingObject(Object obj)
    {
    	this.primitiveArray = (short[])obj;
    }
	
	
	public DataType getDataType()
	{
		return DataType.USHORT;
	}


	public DataType getDataType(int index)
	{
		return DataType.USHORT;
	}
	
	
	public void resize(int size)
	{
		primitiveArray = new short[size];
		this.atomCount = size;
	}


	public boolean getBooleanValue(int index)
	{
		return (primitiveArray[startIndex + index] == 0) ? false : true;
	}


	public byte getByteValue(int index)
	{
		return (byte)primitiveArray[startIndex + index];
	}


	public short getShortValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


	public int getIntValue(int index)
	{
		return (int)(primitiveArray[startIndex + index] & 0xffff);
	}


	public long getLongValue(int index)
	{
		return (long)(primitiveArray[startIndex + index] & 0xffff);
	}


	public float getFloatValue(int index)
	{
		return (float)(primitiveArray[startIndex + index] & 0xffff);
	}


	public double getDoubleValue(int index)
	{
		return (double)(primitiveArray[startIndex + index] & 0xffff);
	}


	public String getStringValue(int index)
	{
		return Integer.toString((primitiveArray[startIndex + index] & 0xffff));
	}


	public boolean getBooleanValue()
	{
		return (primitiveArray[startIndex] == 0) ? false : true;
	}


	public byte getByteValue()
	{
		return (byte)primitiveArray[startIndex];
	}


	public short getShortValue()
	{
		return primitiveArray[startIndex];
	}


	public int getIntValue()
	{
		return (int)(primitiveArray[startIndex] & 0xffff);
	}


	public long getLongValue()
	{
		return (long)(primitiveArray[startIndex] & 0xffff);
	}


	public float getFloatValue()
	{
		return (float)(primitiveArray[startIndex] & 0xffff);
	}


	public double getDoubleValue()
	{
		return (double)(primitiveArray[startIndex] & 0xffff);
	}


	public String getStringValue()
	{
		return Integer.toString((primitiveArray[startIndex] & 0xffff));
	}


	public void setBooleanValue(int index, boolean value)
	{
		primitiveArray[startIndex + index] = (short)(value ? DataBlockBoolean.trueVal : DataBlockBoolean.falseVal);
	}


	public void setByteValue(int index, byte value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (short)value;
	}


	public void setShortValue(int index, short value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (short)value;
	}


	public void setIntValue(int index, int value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (short)value;
	}


	public void setLongValue(int index, long value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (short)value;
	}


	public void setFloatValue(int index, float value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (short)Math.round(value);
	}


	public void setDoubleValue(int index, double value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (short)Math.round(value);
	}


	public void setStringValue(int index, String value)
	{
		short val = Short.parseShort(value);
		primitiveArray[startIndex + index] = val < 0 ? 0 : val;
	}


	public void setBooleanValue(boolean value)
	{
		primitiveArray[startIndex] = (short)(value ? DataBlockBoolean.trueVal : DataBlockBoolean.falseVal);
	}


	public void setByteValue(byte value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (short)value;
	}


	public void setShortValue(short value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (short)value;
	}


	public void setIntValue(int value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (short)value;
	}


	public void setLongValue(long value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (short)value;
	}


	public void setFloatValue(float value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (short)Math.round(value);
	}


	public void setDoubleValue(double value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (short)Math.round(value);
	}


	public void setStringValue(String value)
	{
		short val = Short.parseShort(value);
		primitiveArray[startIndex] = val < 0 ? 0 : val;
	}
}
