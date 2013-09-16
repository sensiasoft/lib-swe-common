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
 * <p>
 * Carries an array of short primitives.
 * All data is casted to other types when requested.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @since Nov 23, 2005
 * @version 1.0
 */
public class DataBlockShort extends AbstractDataBlock
{
	private static final long serialVersionUID = 5819941204196722413L;
    protected short[] primitiveArray;
	
	
	public DataBlockShort()
	{
	}
	
	
	public DataBlockShort(int size)
	{
		resize(size);
	}
	
	
	public DataBlockShort copy()
	{
		DataBlockShort newBlock = new DataBlockShort();
		newBlock.primitiveArray = this.primitiveArray;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    public DataBlockShort renew()
    {
        DataBlockShort newBlock = new DataBlockShort();
        newBlock.primitiveArray = new short[this.atomCount];
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public DataBlockShort clone()
    {
        DataBlockShort newBlock = new DataBlockShort();
        //newBlock.primitiveArray = this.primitiveArray.clone();
        newBlock.primitiveArray = new short[this.atomCount];
        System.arraycopy(this.primitiveArray, 0, newBlock.primitiveArray, 0, this.atomCount);
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
    }
    
    
    public void setUnderlyingObject(Object obj)
    {
    	this.primitiveArray = (short[])obj;
    }
	
	
	public DataType getDataType()
	{
		return DataType.SHORT;
	}


	public DataType getDataType(int index)
	{
		return DataType.SHORT;
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
		return Short.toString(primitiveArray[startIndex + index]);
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
		return Short.toString(primitiveArray[startIndex]);
	}


	public void setBooleanValue(int index, boolean value)
	{
		primitiveArray[startIndex + index] = (short)(value ? DataBlockBoolean.trueVal : DataBlockBoolean.falseVal);
	}


	public void setByteValue(int index, byte value)
	{
		primitiveArray[startIndex + index] = (short)value;
	}


	public void setShortValue(int index, short value)
	{
		primitiveArray[startIndex + index] = value;
	}


	public void setIntValue(int index, int value)
	{
		primitiveArray[startIndex + index] = (short)value;
	}


	public void setLongValue(int index, long value)
	{
		primitiveArray[startIndex + index] = (short)value;
	}


	public void setFloatValue(int index, float value)
	{
		primitiveArray[startIndex + index] = (short)Math.round(value);
	}


	public void setDoubleValue(int index, double value)
	{
		primitiveArray[startIndex + index] = (short)Math.round(value);
	}


	public void setStringValue(int index, String value)
	{
		primitiveArray[startIndex + index] = Short.parseShort(value);
	}


	public void setBooleanValue(boolean value)
	{
		primitiveArray[startIndex] = (short)(value ? DataBlockBoolean.trueVal : DataBlockBoolean.falseVal);
	}


	public void setByteValue(byte value)
	{
		primitiveArray[startIndex] = (short)value;
	}


	public void setShortValue(short value)
	{
		primitiveArray[startIndex] = value;
	}


	public void setIntValue(int value)
	{
		primitiveArray[startIndex] = (short)value;
	}


	public void setLongValue(long value)
	{
		primitiveArray[startIndex] = (short)value;
	}


	public void setFloatValue(float value)
	{
		primitiveArray[startIndex] = (short)Math.round(value);
	}


	public void setDoubleValue(double value)
	{
		primitiveArray[startIndex] = (short)Math.round(value);
	}


	public void setStringValue(String value)
	{
		primitiveArray[startIndex] = Short.parseShort(value);
	}
}
