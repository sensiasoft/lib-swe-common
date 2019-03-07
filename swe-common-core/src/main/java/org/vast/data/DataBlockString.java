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
 * Carries an array of String objects.
 * All data is casted to other types when requested.
 * </p>
 *
 * @author Alex Robin
 * @since Nov 23, 2005
 * */
public class DataBlockString extends AbstractDataBlock
{
	private static final long serialVersionUID = -7268873688598943399L;
    protected String[] primitiveArray;
	
	
	public DataBlockString()
	{
	}
	
	
	public DataBlockString(int size)
	{
		resize(size);
	}
	
	
	@Override
    public DataBlockString copy()
	{
		DataBlockString newBlock = new DataBlockString();
		newBlock.primitiveArray = this.primitiveArray;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    @Override
    public DataBlockString renew()
    {
        DataBlockString newBlock = new DataBlockString();
        newBlock.primitiveArray = new String[this.atomCount];
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public DataBlockString clone()
    {
        // TODO make sure new Strings are created
        DataBlockString newBlock = new DataBlockString();
        //newBlock.primitiveArray = this.primitiveArray.clone();
        newBlock.primitiveArray = new String[this.atomCount];
        System.arraycopy(this.primitiveArray, this.startIndex, newBlock.primitiveArray, 0, this.atomCount);
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public String[] getUnderlyingObject()
    {
        return primitiveArray;
    }
    
    
    protected void setUnderlyingObject(String[] primitiveArray)
    {
        this.primitiveArray = primitiveArray;
        this.atomCount = primitiveArray.length;
    }
    
    
    @Override
    public void setUnderlyingObject(Object obj)
    {
        setUnderlyingObject((String[])obj);
    }
	
	
	@Override
    public DataType getDataType()
	{
		return DataType.UTF_STRING;
	}


	@Override
    public DataType getDataType(int index)
	{
		return DataType.UTF_STRING;
	}
	
	
	@Override
    public void resize(int size)
	{
		primitiveArray = new String[size];
		this.atomCount = size;
	}


	@Override
    public boolean getBooleanValue(int index)
	{
		return Boolean.parseBoolean(primitiveArray[startIndex + index]);
	}


	@Override
    public byte getByteValue(int index)
	{
		return Byte.parseByte(primitiveArray[startIndex + index]);
	}


	@Override
    public short getShortValue(int index)
	{
		return Short.parseShort(primitiveArray[startIndex + index]);
	}


	@Override
    public int getIntValue(int index)
	{
		return Integer.parseInt(primitiveArray[startIndex + index]);
	}


	@Override
    public long getLongValue(int index)
	{
		return Long.parseLong(primitiveArray[startIndex + index]);
	}


	@Override
    public float getFloatValue(int index)
	{
		return Float.parseFloat(primitiveArray[startIndex + index]);
	}


	@Override
    public double getDoubleValue(int index)
	{
		return Double.parseDouble(primitiveArray[startIndex + index]);
	}


	@Override
    public String getStringValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


	@Override
    public boolean getBooleanValue()
	{
		return Boolean.parseBoolean(primitiveArray[startIndex]);
	}


	@Override
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


	@Override
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


	@Override
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


	@Override
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


	@Override
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


	@Override
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


	@Override
    public String getStringValue()
	{
		return primitiveArray[startIndex];
	}


	@Override
    public void setBooleanValue(int index, boolean value)
	{
		primitiveArray[startIndex + index] = Boolean.toString(value);
	}


	@Override
    public void setByteValue(int index, byte value)
	{
		primitiveArray[startIndex + index] = Byte.toString(value);
	}


	@Override
    public void setShortValue(int index, short value)
	{
		primitiveArray[startIndex + index] = Short.toString(value);
	}


	@Override
    public void setIntValue(int index, int value)
	{
		primitiveArray[startIndex + index] = Integer.toString(value);
	}


	@Override
    public void setLongValue(int index, long value)
	{
		primitiveArray[startIndex + index] = Long.toString(value);
	}


	@Override
    public void setFloatValue(int index, float value)
	{
		primitiveArray[startIndex + index] = Float.toString(value);
	}


	@Override
    public void setDoubleValue(int index, double value)
	{
		primitiveArray[startIndex + index] = Double.toString(value);
	}


	@Override
    public void setStringValue(int index, String value)
	{
		primitiveArray[startIndex + index] = value;
	}


	@Override
    public void setBooleanValue(boolean value)
	{
		primitiveArray[startIndex] = Boolean.toString(value);;
	}


	@Override
    public void setByteValue(byte value)
	{
		primitiveArray[startIndex] = Byte.toString(value);;
	}


	@Override
    public void setShortValue(short value)
	{
		primitiveArray[startIndex] = Short.toString(value);
	}


	@Override
    public void setIntValue(int value)
	{
		primitiveArray[startIndex] = Integer.toString(value);
	}


	@Override
    public void setLongValue(long value)
	{
		primitiveArray[startIndex] = Long.toString(value);
	}


	@Override
    public void setFloatValue(float value)
	{
		primitiveArray[startIndex] = Float.toString(value);
	}


	@Override
    public void setDoubleValue(double value)
	{
		primitiveArray[startIndex] = Double.toString(value);
	}


	@Override
    public void setStringValue(String value)
	{
		primitiveArray[startIndex] = value;
	}
}
