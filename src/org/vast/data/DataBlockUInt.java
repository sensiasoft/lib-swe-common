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
 * Unsigned Integer Data Block
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Carries an array of int primitives.
 * All data is casted to other types when requested in a way
 * that returns unsigned values even for values > 2^31
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Nov 23, 2005
 * @version 1.0
 */
public class DataBlockUInt extends AbstractDataBlock
{
	private static final long serialVersionUID = -962702071289349674L;
    protected int[] primitiveArray;
	
	
	public DataBlockUInt()
	{
	}
	
	
	public DataBlockUInt(int size)
	{
		resize(size);
	}
	
	
	public DataBlockUInt copy()
	{
		DataBlockUInt newBlock = new DataBlockUInt();
		newBlock.primitiveArray = this.primitiveArray;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    public DataBlockUInt renew()
    {
        DataBlockUInt newBlock = new DataBlockUInt();
        newBlock.primitiveArray = new int[this.atomCount];
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public DataBlockUInt clone()
    {
        DataBlockUInt newBlock = new DataBlockUInt();
        //newBlock.primitiveArray = this.primitiveArray.clone();
        newBlock.primitiveArray = new int[this.atomCount];
        System.arraycopy(this.primitiveArray, 0, newBlock.primitiveArray, 0, this.atomCount);
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public int[] getUnderlyingObject()
    {
        return primitiveArray;
    }
    
    
    public void setUnderlyingObject(int[] primitiveArray)
    {
        this.primitiveArray = primitiveArray;
    }
    
    
    public void setUnderlyingObject(Object obj)
    {
    	this.primitiveArray = (int[])obj;
    }
	
	
	public DataType getDataType()
	{
		return DataType.UINT;
	}


	public DataType getDataType(int index)
	{
		return DataType.UINT;
	}
	
	
	public void resize(int size)
	{
		primitiveArray = new int[size];
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
		return (short)primitiveArray[startIndex + index];
	}


	public int getIntValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


	public long getLongValue(int index)
	{
		return ((long)primitiveArray[startIndex + index] << 32) >>> 32;
	}


	public float getFloatValue(int index)
	{
		return (float) (((long)primitiveArray[startIndex + index] << 32) >>> 32);
	}


	public double getDoubleValue(int index)
	{
		return (double) (((long)primitiveArray[startIndex + index] << 32) >>> 32);
	}


	public String getStringValue(int index)
	{
		return Long.toString(((long)primitiveArray[startIndex + index] << 32) >>> 32);
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
		return (short)primitiveArray[startIndex];
	}


	public int getIntValue()
	{
		return primitiveArray[startIndex];
	}


	public long getLongValue()
	{
		return ((long)primitiveArray[startIndex] << 32) >>> 32;
	}


	public float getFloatValue()
	{
		return (float) (((long)primitiveArray[startIndex] << 32) >>> 32);
	}


	public double getDoubleValue()
	{
		return (double) (((long)primitiveArray[startIndex] << 32) >>> 32);
	}


	public String getStringValue()
	{
		return Long.toString(((long)primitiveArray[startIndex] << 32) >>> 32);
	}


	public void setBooleanValue(int index, boolean value)
	{
		primitiveArray[startIndex + index] = value ? DataBlockBoolean.trueVal : DataBlockBoolean.falseVal;
	}


	public void setByteValue(int index, byte value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (int)value;
	}


	public void setShortValue(int index, short value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (int)value;
	}


	public void setIntValue(int index, int value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (int)value;
	}


	public void setLongValue(int index, long value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (int)value;
	}


	public void setFloatValue(int index, float value)
	{
	    // force conversion to long first so that it is not truncated to 2.XXe9
        primitiveArray[startIndex + index] = value < 0 ? 0 : (int)Math.round((double)value);
	}


	public void setDoubleValue(int index, double value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (int)Math.round(value);
	}


	public void setStringValue(int index, String value)
	{
		int val = Integer.parseInt(value);
		primitiveArray[startIndex + index] = val < 0 ? 0 : val;
	}


	public void setBooleanValue(boolean value)
	{
		primitiveArray[startIndex] = value ? DataBlockBoolean.trueVal : DataBlockBoolean.falseVal;
	}


	public void setByteValue(byte value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (int)value;
	}


	public void setShortValue(short value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (int)value;
	}


	public void setIntValue(int value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (int)value;
	}


	public void setLongValue(long value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (int)value;
	}


	public void setFloatValue(float value)
	{
	    // force conversion to long first so that it is not truncated to 2.XXe9
        primitiveArray[startIndex] = value < 0 ? 0 : (int)Math.round((double)value);
	}


	public void setDoubleValue(double value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (int)Math.round(value);
	}


	public void setStringValue(String value)
	{
		int val = Integer.parseInt(value);
		primitiveArray[startIndex] = val < 0 ? 0 : val;
	}
}
