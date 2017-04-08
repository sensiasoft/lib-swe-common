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
 * Carries an array of double primitives.
 * All data is casted to other types when requested.
 * </p>
 *
 * @author Alex Robin
 * @since Nov 23, 2005
 * */
public class DataBlockDouble extends AbstractDataBlock
{
	protected double[] primitiveArray;
	
	
	public DataBlockDouble()
	{
	}
	
	
	public DataBlockDouble(int size)
	{
		resize(size);
	}
	
	
	public DataBlockDouble copy()
	{
		DataBlockDouble newBlock = new DataBlockDouble();
		newBlock.primitiveArray = this.primitiveArray;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    public DataBlockDouble renew()
    {
        DataBlockDouble newBlock = new DataBlockDouble();
        newBlock.primitiveArray = new double[this.atomCount];
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public DataBlockDouble clone()
    {
        DataBlockDouble newBlock = new DataBlockDouble();
        //newBlock.primitiveArray = this.primitiveArray.clone();
        newBlock.primitiveArray = new double[this.atomCount];
        System.arraycopy(this.primitiveArray, this.startIndex, newBlock.primitiveArray, 0, this.atomCount);
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public double[] getUnderlyingObject()
    {
        return primitiveArray;
    }
    
    
    public void setUnderlyingObject(double[] primitiveArray)
    {
        this.primitiveArray = primitiveArray;
        this.atomCount = primitiveArray.length;
    }
    
    
    public void setUnderlyingObject(Object obj)
    {
    	this.primitiveArray = (double[])obj;
    }
	
	
	public DataType getDataType()
	{
		return DataType.DOUBLE;
	}


	public DataType getDataType(int index)
	{
		return DataType.DOUBLE;
	}
	
	
	public void resize(int size)
	{
		primitiveArray = new double[size];
		this.atomCount = size;
	}


	public boolean getBooleanValue(int index)
	{
		double val = primitiveArray[startIndex + index];		        
	    return (Math.abs(val) < Math.ulp(0.0)) ? false : true;
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
		return primitiveArray[startIndex + index];
	}


	public String getStringValue(int index)
	{
		return Double.toString(primitiveArray[startIndex + index]);
	}


	public boolean getBooleanValue()
	{
		return getBooleanValue(0);
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
		return primitiveArray[startIndex];
	}


	public String getStringValue()
	{
		return Double.toString(primitiveArray[startIndex]);
	}


	public void setBooleanValue(int index, boolean value)
	{
		primitiveArray[startIndex + index] = value ? DataBlockBoolean.TRUE_VAL : DataBlockBoolean.FALSE_VAL;
	}


	public void setByteValue(int index, byte value)
	{
		primitiveArray[startIndex + index] = (double)value;
	}


	public void setShortValue(int index, short value)
	{
		primitiveArray[startIndex + index] = (double)value;
	}


	public void setIntValue(int index, int value)
	{
		primitiveArray[startIndex + index] = (double)value;
	}


	public void setLongValue(int index, long value)
	{
		primitiveArray[startIndex + index] = (double)value;
	}


	public void setFloatValue(int index, float value)
	{
		primitiveArray[startIndex + index] = (double)value;
	}


	public void setDoubleValue(int index, double value)
	{
		primitiveArray[startIndex + index] = value;
	}


	public void setStringValue(int index, String value)
	{
		primitiveArray[startIndex + index] = Double.parseDouble(value);
	}


	public void setBooleanValue(boolean value)
	{
		primitiveArray[startIndex] = value ? DataBlockBoolean.TRUE_VAL : DataBlockBoolean.FALSE_VAL;
	}


	public void setByteValue(byte value)
	{
		primitiveArray[startIndex] = (double)value;
	}


	public void setShortValue(short value)
	{
		primitiveArray[startIndex] = (double)value;
	}


	public void setIntValue(int value)
	{
		primitiveArray[startIndex] = (double)value;
	}


	public void setLongValue(long value)
	{
		primitiveArray[startIndex] = (double)value;
	}


	public void setFloatValue(float value)
	{
		primitiveArray[startIndex] = (double)value;
	}


	public void setDoubleValue(double value)
	{
		primitiveArray[startIndex] = value;
	}


	public void setStringValue(String value)
	{
		primitiveArray[startIndex] = Double.parseDouble(value);
	}
}
