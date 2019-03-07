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
 * Carries an array of boolean primitives.
 * All data is casted to other types when requested.
 * </p>
 *
 * @author Alex Robin
 * @since Nov 23, 2005
 * */
public class DataBlockBoolean extends AbstractDataBlock
{
	private static final long serialVersionUID = -6524352354000350972L;
    public static final byte TRUE_VAL = 1;
	public static final byte FALSE_VAL = 0;
	protected boolean[] primitiveArray;
	
	
	public DataBlockBoolean()
	{
	}
	
	
	public DataBlockBoolean(int size)
	{
		resize(size);
	}
	
	
	@Override
    public DataBlockBoolean copy()
	{
		DataBlockBoolean newBlock = new DataBlockBoolean();
		newBlock.primitiveArray = this.primitiveArray;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    @Override
    public DataBlockBoolean renew()
    {
        DataBlockBoolean newBlock = new DataBlockBoolean();
        newBlock.primitiveArray = new boolean[this.atomCount];
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public DataBlockBoolean clone()
    {
        DataBlockBoolean newBlock = new DataBlockBoolean();
        //newBlock.primitiveArray = this.primitiveArray.clone();
        newBlock.primitiveArray = new boolean[this.atomCount];
        System.arraycopy(this.primitiveArray, this.startIndex, newBlock.primitiveArray, 0, this.atomCount);
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public boolean[] getUnderlyingObject()
    {
        return primitiveArray;
    }
    
    
    protected void setUnderlyingObject(boolean[] primitiveArray)
    {
        this.primitiveArray = primitiveArray;
        this.atomCount = primitiveArray.length;
    }
    
    
    @Override
    public void setUnderlyingObject(Object obj)
    {
        setUnderlyingObject((boolean[])obj);
    }
	
	
	@Override
    public DataType getDataType()
	{
		return DataType.BOOLEAN;
	}


	@Override
    public DataType getDataType(int index)
	{
		return DataType.BOOLEAN;
	}
	
	
	@Override
    public void resize(int size)
	{
		primitiveArray = new boolean[size];
		this.atomCount = size;
	}


	@Override
    public boolean getBooleanValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


	@Override
    public byte getByteValue(int index)
	{
		return primitiveArray[startIndex + index] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public short getShortValue(int index)
	{
		return primitiveArray[startIndex + index] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public int getIntValue(int index)
	{
		return primitiveArray[startIndex + index] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public long getLongValue(int index)
	{
		return primitiveArray[startIndex + index] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public float getFloatValue(int index)
	{
		return primitiveArray[startIndex + index] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public double getDoubleValue(int index)
	{
		return primitiveArray[startIndex + index] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public String getStringValue(int index)
	{
		return Boolean.toString(primitiveArray[startIndex + index]);
	}


	@Override
    public boolean getBooleanValue()
	{
		return primitiveArray[startIndex];
	}


	@Override
    public byte getByteValue()
	{
		return primitiveArray[startIndex] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public short getShortValue()
	{
		return primitiveArray[startIndex] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public int getIntValue()
	{
		return primitiveArray[startIndex] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public long getLongValue()
	{
		return primitiveArray[startIndex] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public float getFloatValue()
	{
		return primitiveArray[startIndex] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public double getDoubleValue()
	{
		return primitiveArray[startIndex] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public String getStringValue()
	{
		return Boolean.toString(primitiveArray[startIndex]);
	}


	@Override
    public void setBooleanValue(int index, boolean value)
	{
		primitiveArray[startIndex + index] = value;
	}


	@Override
    public void setByteValue(int index, byte value)
	{
		primitiveArray[startIndex + index] = (value == 0) ? false : true;
	}


	@Override
    public void setShortValue(int index, short value)
	{
		primitiveArray[startIndex + index] = (value == 0) ? false : true;
	}


	@Override
    public void setIntValue(int index, int value)
	{
		primitiveArray[startIndex + index] = (value == 0) ? false : true;
	}


	@Override
    public void setLongValue(int index, long value)
	{
		primitiveArray[startIndex + index] = (value == 0) ? false : true;
	}


	@Override
    public void setFloatValue(int index, float value)
	{
		primitiveArray[startIndex + index] = (Float.isNaN(value) || Math.abs(value) < Math.ulp(0.0)) ? false : true;
	}


	@Override
    public void setDoubleValue(int index, double value)
	{
		primitiveArray[startIndex + index] = (Double.isNaN(value) || Math.abs(value) < Math.ulp(0.0)) ? false : true;
	}


	@Override
    public void setStringValue(int index, String value)
	{
		primitiveArray[startIndex + index] = Boolean.parseBoolean(value);
	}


	@Override
    public void setBooleanValue(boolean value)
	{
		primitiveArray[startIndex] = value;
	}


	@Override
    public void setByteValue(byte value)
	{
		primitiveArray[startIndex] = (value == 0) ? false : true;
	}


	@Override
    public void setShortValue(short value)
	{
		primitiveArray[startIndex] = (value == 0) ? false : true;
	}


	@Override
    public void setIntValue(int value)
	{
		primitiveArray[startIndex] = (value == 0) ? false : true;
	}


	@Override
    public void setLongValue(long value)
	{
		primitiveArray[startIndex] = (value == 0) ? false : true;
	}


	@Override
    public void setFloatValue(float value)
	{
		primitiveArray[startIndex] = (Float.isNaN(value) || Math.abs(value) < Math.ulp(0.0)) ? false : true;
	}


	@Override
    public void setDoubleValue(double value)
	{
		primitiveArray[startIndex] = (Double.isNaN(value) || Math.abs(value) < Math.ulp(0.0)) ? false : true;
	}


	@Override
    public void setStringValue(String value)
	{
		primitiveArray[startIndex] = Boolean.parseBoolean(value);
	}
}
