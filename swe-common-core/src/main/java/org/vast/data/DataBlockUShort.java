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
 * Carries an array of short primitives.
 * All data is casted to other types when requested and in a way
 * that returns unsigned values even for values > 2^15
 * </p>
 *
 * @author Alex Robin
 * @since Nov 23, 2005
 * */
public class DataBlockUShort extends AbstractDataBlock
{
	private static final long serialVersionUID = 3688413125911940425L;
    protected short[] primitiveArray;
	
	
	public DataBlockUShort()
	{
	}
	
	
	public DataBlockUShort(int size)
	{
		resize(size);
	}
	
	
	@Override
    public DataBlockUShort copy()
	{
		DataBlockUShort newBlock = new DataBlockUShort();
		newBlock.primitiveArray = this.primitiveArray;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    @Override
    public DataBlockUShort renew()
    {
        DataBlockUShort newBlock = new DataBlockUShort();
        newBlock.primitiveArray = new short[this.atomCount];
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public DataBlockUShort clone()
    {
        DataBlockUShort newBlock = new DataBlockUShort();
        //newBlock.primitiveArray = this.primitiveArray.clone();
        newBlock.primitiveArray = new short[this.atomCount];
        System.arraycopy(this.primitiveArray, this.startIndex, newBlock.primitiveArray, 0, this.atomCount);
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public short[] getUnderlyingObject()
    {
        return primitiveArray;
    }
    
    
    protected void setUnderlyingObject(short[] primitiveArray)
    {
        this.primitiveArray = primitiveArray;
        this.atomCount = primitiveArray.length;
    }
    
    
    @Override
    public void setUnderlyingObject(Object obj)
    {
        setUnderlyingObject((short[])obj);
    }
	
	
	@Override
    public DataType getDataType()
	{
		return DataType.USHORT;
	}


	@Override
    public DataType getDataType(int index)
	{
		return DataType.USHORT;
	}
	
	
	@Override
    public void resize(int size)
	{
		primitiveArray = new short[size];
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
		return primitiveArray[startIndex + index];
	}


	@Override
    public int getIntValue(int index)
	{
		return primitiveArray[startIndex + index] & 0xffff;
	}


	@Override
    public long getLongValue(int index)
	{
		return primitiveArray[startIndex + index] & 0xffff;
	}


	@Override
    public float getFloatValue(int index)
	{
		return primitiveArray[startIndex + index] & 0xffff;
	}


	@Override
    public double getDoubleValue(int index)
	{
		return primitiveArray[startIndex + index] & 0xffff;
	}


	@Override
    public String getStringValue(int index)
	{
		return Integer.toString((primitiveArray[startIndex + index] & 0xffff));
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
		return primitiveArray[startIndex];
	}


	@Override
    public int getIntValue()
	{
		return primitiveArray[startIndex] & 0xffff;
	}


	@Override
    public long getLongValue()
	{
		return primitiveArray[startIndex] & 0xffff;
	}


	@Override
    public float getFloatValue()
	{
		return primitiveArray[startIndex] & 0xffff;
	}


	@Override
    public double getDoubleValue()
	{
		return primitiveArray[startIndex] & 0xffff;
	}


	@Override
    public String getStringValue()
	{
		return Integer.toString((primitiveArray[startIndex] & 0xffff));
	}


	@Override
    public void setBooleanValue(int index, boolean value)
	{
		primitiveArray[startIndex + index] = value ? DataBlockBoolean.TRUE_VAL : DataBlockBoolean.FALSE_VAL;
	}


	@Override
    public void setByteValue(int index, byte value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (short)value;
	}


	@Override
    public void setShortValue(int index, short value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (short)value;
	}


	@Override
    public void setIntValue(int index, int value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (short)value;
	}


	@Override
    public void setLongValue(int index, long value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (short)value;
	}


	@Override
    public void setFloatValue(int index, float value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (short)Math.round(value);
	}


	@Override
    public void setDoubleValue(int index, double value)
	{
		primitiveArray[startIndex + index] = value < 0 ? 0 : (short)Math.round(value);
	}


	@Override
    public void setStringValue(int index, String value)
	{
		short val = Short.parseShort(value);
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
		primitiveArray[startIndex] = value < 0 ? 0 : (short)value;
	}


	@Override
    public void setShortValue(short value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (short)value;
	}


	@Override
    public void setIntValue(int value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (short)value;
	}


	@Override
    public void setLongValue(long value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (short)value;
	}


	@Override
    public void setFloatValue(float value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (short)Math.round(value);
	}


	@Override
    public void setDoubleValue(double value)
	{
		primitiveArray[startIndex] = value < 0 ? 0 : (short)Math.round(value);
	}


	@Override
    public void setStringValue(String value)
	{
		short val = Short.parseShort(value);
		primitiveArray[startIndex] = val < 0 ? 0 : val;
	}
}
