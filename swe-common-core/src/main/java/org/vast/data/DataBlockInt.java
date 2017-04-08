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
 * All data is casted to other types when requested.
 * </p>
 *
 * @author Alex Robin
 * @since Nov 23, 2005
 * */
public class DataBlockInt extends AbstractDataBlock
{
	private static final long serialVersionUID = -6396843894262396349L;
    protected int[] primitiveArray;
	
	
	public DataBlockInt()
	{
	}
	
	
	public DataBlockInt(int size)
	{
		resize(size);
	}
	
	
	public DataBlockInt copy()
	{
		DataBlockInt newBlock = new DataBlockInt();
		newBlock.primitiveArray = this.primitiveArray;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    public DataBlockInt renew()
    {
        DataBlockInt newBlock = new DataBlockInt();
        newBlock.primitiveArray = new int[this.atomCount];
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public DataBlockInt clone()
    {
        DataBlockInt newBlock = new DataBlockInt();
        //newBlock.primitiveArray = this.primitiveArray.clone();
        newBlock.primitiveArray = new int[this.atomCount];
        System.arraycopy(this.primitiveArray, this.startIndex, newBlock.primitiveArray, 0, this.atomCount);
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
        this.atomCount = primitiveArray.length;
    }
    
    
    public void setUnderlyingObject(Object obj)
    {
    	this.primitiveArray = (int[])obj;
    }
	
	
	public DataType getDataType()
	{
		return DataType.INT;
	}


	public DataType getDataType(int index)
	{
		return DataType.INT;
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
		return Integer.toString(primitiveArray[startIndex + index]);
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
		return Integer.toString(primitiveArray[startIndex]);
	}


	public void setBooleanValue(int index, boolean value)
	{
		primitiveArray[startIndex + index] = value ? DataBlockBoolean.TRUE_VAL : DataBlockBoolean.FALSE_VAL;
	}


	public void setByteValue(int index, byte value)
	{
		primitiveArray[startIndex + index] = (int)value;
	}


	public void setShortValue(int index, short value)
	{
		primitiveArray[startIndex + index] = (int)value;
	}


	public void setIntValue(int index, int value)
	{
		primitiveArray[startIndex + index] = value;
	}


	public void setLongValue(int index, long value)
	{
		primitiveArray[startIndex + index] = (int)value;
	}


	public void setFloatValue(int index, float value)
	{
		primitiveArray[startIndex + index] = (int)Math.round(value);
	}


	public void setDoubleValue(int index, double value)
	{
		primitiveArray[startIndex + index] = (int)Math.round(value);
	}


	public void setStringValue(int index, String value)
	{
		primitiveArray[startIndex + index] = Integer.parseInt(value);
	}


	public void setBooleanValue(boolean value)
	{
		primitiveArray[startIndex] = value ? DataBlockBoolean.TRUE_VAL : DataBlockBoolean.FALSE_VAL;
	}


	public void setByteValue(byte value)
	{
		primitiveArray[startIndex] = (int)value;
	}


	public void setShortValue(short value)
	{
		primitiveArray[startIndex] = (int)value;
	}


	public void setIntValue(int value)
	{
		primitiveArray[startIndex] = value;
	}


	public void setLongValue(long value)
	{
		primitiveArray[startIndex] = (int)value;
	}


	public void setFloatValue(float value)
	{
		primitiveArray[startIndex] = (int)Math.round(value);
	}


	public void setDoubleValue(double value)
	{
		primitiveArray[startIndex] = (int)Math.round(value);
	}


	public void setStringValue(String value)
	{
		primitiveArray[startIndex] = Integer.parseInt(value);
	}
}
