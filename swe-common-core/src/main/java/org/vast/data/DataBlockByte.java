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
 * Carries an array of byte primitives.
 * All data is casted to other types when requested.
 * </p>
 *
 * @author Alex Robin
 * @since Nov 23, 2005
 * */
public class DataBlockByte extends AbstractDataBlock
{
	private static final long serialVersionUID = 9112814840259970633L;
    protected byte[] primitiveArray;
	
	
	public DataBlockByte()
	{
	}
	
	
	public DataBlockByte(int size)
	{
		resize(size);
	}
	
	
	@Override
    public DataBlockByte copy()
	{
		DataBlockByte newBlock = new DataBlockByte();
		newBlock.primitiveArray = this.primitiveArray;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    @Override
    public DataBlockByte renew()
    {
        DataBlockByte newBlock = new DataBlockByte();
        newBlock.primitiveArray = new byte[this.atomCount];
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
	
    
    @Override
    public DataBlockByte clone()
    {
        DataBlockByte newBlock = new DataBlockByte();
        //newBlock.primitiveArray = this.primitiveArray.clone();
        newBlock.primitiveArray = new byte[this.atomCount];
        System.arraycopy(this.primitiveArray, this.startIndex, newBlock.primitiveArray, 0, this.atomCount);
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public byte[] getUnderlyingObject()
    {
        return primitiveArray;
    }
    
    
    protected void setUnderlyingObject(byte[] primitiveArray)
    {
        this.primitiveArray = primitiveArray;
        this.atomCount = primitiveArray.length;
    }
    
    
    @Override
    public void setUnderlyingObject(Object obj)
    {
        setUnderlyingObject((byte[])obj);
    }
    
	
	@Override
    public DataType getDataType()
	{
		return DataType.BYTE;
	}


	@Override
    public DataType getDataType(int index)
	{
		return DataType.BYTE;
	}


	@Override
    public void resize(int size)
	{
		primitiveArray = new byte[size];
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
		return primitiveArray[startIndex + index];
	}


	@Override
    public short getShortValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


	@Override
    public int getIntValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


	@Override
    public long getLongValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


	@Override
    public float getFloatValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


	@Override
    public double getDoubleValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


	@Override
    public String getStringValue(int index)
	{
		return Byte.toString(primitiveArray[startIndex + index]);
	}


	@Override
    public boolean getBooleanValue()
	{
		return (primitiveArray[startIndex] == 0) ? false : true;
	}


	@Override
    public byte getByteValue()
	{
		return primitiveArray[startIndex];
	}


	@Override
    public short getShortValue()
	{
		return primitiveArray[startIndex];
	}


	@Override
    public int getIntValue()
	{
		return primitiveArray[startIndex];
	}


	@Override
    public long getLongValue()
	{
		return primitiveArray[startIndex];
	}


	@Override
    public float getFloatValue()
	{
		return primitiveArray[startIndex];
	}


	@Override
    public double getDoubleValue()
	{
		return primitiveArray[startIndex];
	}


	@Override
    public String getStringValue()
	{
		return Byte.toString(primitiveArray[startIndex]);
	}


	@Override
    public void setBooleanValue(int index, boolean value)
	{
		primitiveArray[startIndex + index] = value ? DataBlockBoolean.TRUE_VAL : DataBlockBoolean.FALSE_VAL;
	}


	@Override
    public void setByteValue(int index, byte value)
	{
		primitiveArray[startIndex + index] = value;
	}


	@Override
    public void setShortValue(int index, short value)
	{
		primitiveArray[startIndex + index] = (byte)value;
	}


	@Override
    public void setIntValue(int index, int value)
	{
		primitiveArray[startIndex + index] = (byte)value;
	}


	@Override
    public void setLongValue(int index, long value)
	{
		primitiveArray[startIndex + index] = (byte)value;
	}


	@Override
    public void setFloatValue(int index, float value)
	{
		primitiveArray[startIndex + index] = (byte)Math.round(value);
	}


	@Override
    public void setDoubleValue(int index, double value)
	{
		primitiveArray[startIndex + index] = (byte)Math.round(value);
	}


	@Override
    public void setStringValue(int index, String value)
	{
		primitiveArray[startIndex + index] = Byte.parseByte(value);
	}


	@Override
    public void setBooleanValue(boolean value)
	{
		primitiveArray[startIndex] = value ? DataBlockBoolean.TRUE_VAL : DataBlockBoolean.FALSE_VAL;
	}


	@Override
    public void setByteValue(byte value)
	{
		primitiveArray[startIndex] = value;
	}


	@Override
    public void setShortValue(short value)
	{
		primitiveArray[startIndex] = (byte)value;
	}


	@Override
    public void setIntValue(int value)
	{
		primitiveArray[startIndex] = (byte)value;
	}


	@Override
    public void setLongValue(long value)
	{
		primitiveArray[startIndex] = (byte)value;
	}


	@Override
    public void setFloatValue(float value)
	{
		primitiveArray[startIndex] = (byte)Math.round(value);
	}


	@Override
    public void setDoubleValue(double value)
	{
		primitiveArray[startIndex] = (byte)Math.round(value);
	}


	@Override
    public void setStringValue(String value)
	{
		primitiveArray[startIndex] = Byte.parseByte(value);
	}
}
