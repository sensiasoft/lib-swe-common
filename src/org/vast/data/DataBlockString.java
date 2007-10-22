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
 * String Data Block
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Carries an array of String objects.
 * All data is casted to other types when requested.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Nov 23, 2005
 * @version 1.0
 */
public class DataBlockString extends AbstractDataBlock
{
	protected String[] primitiveArray;
	
	
	public DataBlockString()
	{
	}
	
	
	public DataBlockString(int size)
	{
		resize(size);
	}
	
	
	public DataBlockString copy()
	{
		DataBlockString newBlock = new DataBlockString();
		newBlock.primitiveArray = this.primitiveArray;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    public DataBlockString renew()
    {
        DataBlockString newBlock = new DataBlockString();
        newBlock.primitiveArray = new String[this.primitiveArray.length];
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public DataBlockString clone()
    {
        // TODO make sure new Strings are created
        DataBlockString newBlock = new DataBlockString();
        newBlock.primitiveArray = this.primitiveArray.clone();
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public String[] getUnderlyingObject()
    {
        return primitiveArray;
    }
    
    
    public void setUnderlyingObject(String[] primitiveArray)
    {
        this.primitiveArray = primitiveArray;
    }
	
	
	public DataType getDataType()
	{
		return DataType.UTF_STRING;
	}


	public DataType getDataType(int index)
	{
		return DataType.UTF_STRING;
	}
	
	
	public void resize(int size)
	{
		primitiveArray = new String[size];
		this.atomCount = size;
	}


	public boolean getBooleanValue(int index)
	{
		return Boolean.parseBoolean(primitiveArray[startIndex + index]);
	}


	public byte getByteValue(int index)
	{
		return Byte.parseByte(primitiveArray[startIndex + index]);
	}


	public short getShortValue(int index)
	{
		return Short.parseShort(primitiveArray[startIndex + index]);
	}


	public int getIntValue(int index)
	{
		return Integer.parseInt(primitiveArray[startIndex + index]);
	}


	public long getLongValue(int index)
	{
		return Long.parseLong(primitiveArray[startIndex + index]);
	}


	public float getFloatValue(int index)
	{
		return Float.parseFloat(primitiveArray[startIndex + index]);
	}


	public double getDoubleValue(int index)
	{
		return Double.parseDouble(primitiveArray[startIndex + index]);
	}


	public String getStringValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


	public boolean getBooleanValue()
	{
		return Boolean.parseBoolean(primitiveArray[startIndex]);
	}


	public byte getByteValue()
	{
	    byte val;
        
        try
        {
            val = Byte.parseByte(primitiveArray[startIndex]);
        }
        catch (NumberFormatException e)
        {
            val = -1;
        }
        
        return val;
	}


	public short getShortValue()
	{
	    short val;
        
        try
        {
            val = Short.parseShort(primitiveArray[startIndex]);
        }
        catch (NumberFormatException e)
        {
            val = -1;
        }
        
        return val;
	}


	public int getIntValue()
	{
	    int val;
        
        try
        {
            val = Integer.parseInt(primitiveArray[startIndex]);
        }
        catch (NumberFormatException e)
        {
            val = -1;
        }
        
        return val;
	}


	public long getLongValue()
	{
	    long val;
        
        try
        {
            val = Long.parseLong(primitiveArray[startIndex]);
        }
        catch (NumberFormatException e)
        {
            val = -1;
        }
        
        return val;
	}


	public float getFloatValue()
	{
	    float val;
        
        try
        {
            val = Float.parseFloat(primitiveArray[startIndex]);
        }
        catch (NumberFormatException e)
        {
            val = Float.NaN;
        }
        
        return val;
	}


	public double getDoubleValue()
	{
        double val;
        
        try
        {
            val = Double.parseDouble(primitiveArray[startIndex]);
        }
        catch (NumberFormatException e)
        {
            val = Double.NaN;
        }
        
        return val;
	}


	public String getStringValue()
	{
		return primitiveArray[startIndex];
	}


	public void setBooleanValue(int index, boolean value)
	{
		primitiveArray[startIndex + index] = Boolean.toString(value);
	}


	public void setByteValue(int index, byte value)
	{
		primitiveArray[startIndex + index] = Byte.toString(value);
	}


	public void setShortValue(int index, short value)
	{
		primitiveArray[startIndex + index] = Short.toString(value);
	}


	public void setIntValue(int index, int value)
	{
		primitiveArray[startIndex + index] = Integer.toString(value);
	}


	public void setLongValue(int index, long value)
	{
		primitiveArray[startIndex + index] = Long.toString(value);
	}


	public void setFloatValue(int index, float value)
	{
		primitiveArray[startIndex + index] = Float.toString(value);
	}


	public void setDoubleValue(int index, double value)
	{
		primitiveArray[startIndex + index] = Double.toString(value);
	}


	public void setStringValue(int index, String value)
	{
		primitiveArray[startIndex + index] = value;
	}


	public void setBooleanValue(boolean value)
	{
		primitiveArray[startIndex] = Boolean.toString(value);;
	}


	public void setByteValue(byte value)
	{
		primitiveArray[startIndex] = Byte.toString(value);;
	}


	public void setShortValue(short value)
	{
		primitiveArray[startIndex] = Short.toString(value);
	}


	public void setIntValue(int value)
	{
		primitiveArray[startIndex] = Integer.toString(value);
	}


	public void setLongValue(long value)
	{
		primitiveArray[startIndex] = Long.toString(value);
	}


	public void setFloatValue(float value)
	{
		primitiveArray[startIndex] = Float.toString(value);
	}


	public void setDoubleValue(double value)
	{
		primitiveArray[startIndex] = Double.toString(value);
	}


	public void setStringValue(String value)
	{
		primitiveArray[startIndex] = value;
	}
}
