/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import net.opengis.swe.v20.DataType;


/**
 * <p>
 * Carries an array of int primitives.
 * All data is casted to other types when requested in a way
 * that returns unsigned values even for values > 2^31
 * </p>
 *
 * @author Alex Robin
 * @since Nov 23, 2005
 * */
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
	
	
	@Override
    public DataBlockUInt copy()
	{
		DataBlockUInt newBlock = new DataBlockUInt();
		newBlock.primitiveArray = this.primitiveArray;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    @Override
    public DataBlockUInt renew()
    {
        DataBlockUInt newBlock = new DataBlockUInt();
        newBlock.primitiveArray = new int[this.atomCount];
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public DataBlockUInt clone()
    {
        DataBlockUInt newBlock = new DataBlockUInt();
        //newBlock.primitiveArray = this.primitiveArray.clone();
        newBlock.primitiveArray = new int[this.atomCount];
        System.arraycopy(this.primitiveArray, this.startIndex, newBlock.primitiveArray, 0, this.atomCount);
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public int[] getUnderlyingObject()
    {
        return primitiveArray;
    }
    
    
    protected void setUnderlyingObject(int[] primitiveArray)
    {
        this.primitiveArray = primitiveArray;
        this.atomCount = primitiveArray.length;
    }
    
    
    @Override
    public void setUnderlyingObject(Object obj)
    {
        setUnderlyingObject((int[])obj);
    }
	
	
	@Override
    public DataType getDataType()
	{
		return DataType.UINT;
	}


	@Override
    public DataType getDataType(int index)
	{
		return DataType.UINT;
	}
	
	
	@Override
    public void resize(int size)
	{
		primitiveArray = new int[size];
		this.atomCount = size;
	}


	@Override
    public boolean getBooleanValue(int index)
	{
		return (primitiveArray[startIndex + index] == 0) ? false : true;
	}


	@Override
    public byte getByteValue(int index)
	{
		return (byte)primitiveArray[startIndex + index];
	}


	@Override
    public short getShortValue(int index)
	{
		return (short)primitiveArray[startIndex + index];
	}


	@Override
    public int getIntValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


	@Override
    public long getLongValue(int index)
	{
		return ((long)primitiveArray[startIndex + index] << 32) >>> 32;
	}


	@Override
    public float getFloatValue(int index)
	{
		return ((long)primitiveArray[startIndex + index] << 32) >>> 32;
	}


	@Override
    public double getDoubleValue(int index)
	{
		return ((long)primitiveArray[startIndex + index] << 32) >>> 32;
	}


	@Override
    public String getStringValue(int index)
	{
		return Long.toString(((long)primitiveArray[startIndex + index] << 32) >>> 32);
	}


	@Override
    public boolean getBooleanValue()
	{
		return (primitiveArray[startIndex] == 0) ? false : true;
	}


	@Override
    public byte getByteValue()
	{
		return (byte)primitiveArray[startIndex];
	}


	@Override
    public short getShortValue()
	{
		return (short)primitiveArray[startIndex];
	}


	@Override
    public int getIntValue()
	{
		return primitiveArray[startIndex];
	}


	@Override
    public long getLongValue()
	{
		return ((long)primitiveArray[startIndex] << 32) >>> 32;
	}


	@Override
    public float getFloatValue()
	{
		return ((long)primitiveArray[startIndex] << 32) >>> 32;
	}


	@Override
    public double getDoubleValue()
	{
		return ((long)primitiveArray[startIndex] << 32) >>> 32;
	}


	@Override
    public String getStringValue()
	{
		return Long.toString(((long)primitiveArray[startIndex] << 32) >>> 32);
	}


	@Override
    public void setBooleanValue(int index, boolean value)
	{
		primitiveArray[startIndex + index] = value ? DataBlockBoolean.TRUE_VAL : DataBlockBoolean.FALSE_VAL;
	}


	@Override
    public void setByteValue(int index, byte value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (int)value;
	}


	@Override
    public void setShortValue(int index, short value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (int)value;
	}


	@Override
    public void setIntValue(int index, int value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (int)value;
	}


	@Override
    public void setLongValue(int index, long value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (int)value;
	}


	@Override
    public void setFloatValue(int index, float value)
	{
	    // force conversion to long first so that it is not truncated to 2.XXe9
        primitiveArray[startIndex + index] = value < 0 ? 0 : (int)Math.round((double)value);
	}


	@Override
    public void setDoubleValue(int index, double value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (int)Math.round(value);
	}


	@Override
    public void setStringValue(int index, String value)
	{
		int val = Integer.parseInt(value);
		primitiveArray[startIndex + index] = val < 0 ? 0 : val;
	}


	@Override
    public void setBooleanValue(boolean value)
	{
		primitiveArray[startIndex] = value ? DataBlockBoolean.TRUE_VAL : DataBlockBoolean.FALSE_VAL;
	}


	@Override
    public void setByteValue(byte value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (int)value;
	}


	@Override
    public void setShortValue(short value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (int)value;
	}


	@Override
    public void setIntValue(int value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (int)value;
	}


	@Override
    public void setLongValue(long value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (int)value;
	}


	@Override
    public void setFloatValue(float value)
	{
	    // force conversion to long first so that it is not truncated to 2.XXe9
        primitiveArray[startIndex] = value < 0 ? 0 : (int)Math.round((double)value);
	}


	@Override
    public void setDoubleValue(double value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (int)Math.round(value);
	}


	@Override
    public void setStringValue(String value)
	{
		int val = Integer.parseInt(value);
		primitiveArray[startIndex] = val < 0 ? 0 : val;
	}
}
