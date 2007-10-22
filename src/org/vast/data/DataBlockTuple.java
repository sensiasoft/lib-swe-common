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
 * Data Block Tuple
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Uses the composite pattern to carry a fixed size array
 * of mixed types DataBlocks. If dynamic size is needed, use DataBlockList.
 * Children datablocks will be read in parallel. Designed specifically for
 * DataGroups (slightly more performant than DataBlockTuple for this case)
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @version 1.0
 */
public class DataBlockTuple extends AbstractDataBlock
{
	protected AbstractDataBlock[] blockArray;


	public DataBlockTuple()
	{
	}


	public DataBlockTuple(int numBlocks)
	{
		blockArray = new AbstractDataBlock[numBlocks];
	}
	
	
	public DataBlockTuple copy()
	{
		DataBlockTuple newBlock = new DataBlockTuple();
		newBlock.startIndex = this.startIndex;
		newBlock.blockArray = this.blockArray;	
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    public DataBlockTuple renew()
    {
        DataBlockTuple newBlock = new DataBlockTuple();
        newBlock.startIndex = this.startIndex;
        newBlock.blockArray = new AbstractDataBlock[blockArray.length];
        
        // fully copy (clone) all blocks in the array
        for (int i=0; i<blockArray.length; i++)
            newBlock.blockArray[i] = this.blockArray[i].renew();
        
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public DataBlockTuple clone()
    {
        DataBlockTuple newBlock = new DataBlockTuple();
        newBlock.startIndex = this.startIndex;
        newBlock.blockArray = new AbstractDataBlock[blockArray.length];
        
        // fully copy (clone) all blocks in the array
        for (int i=0; i<blockArray.length; i++)
            newBlock.blockArray[i] = this.blockArray[i].clone();
        
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public AbstractDataBlock[] getUnderlyingObject()
    {
        return blockArray;
    }
    
    
    public void setUnderlyingObject(AbstractDataBlock[] blockArray)
    {
        this.blockArray = blockArray;
    }
	
	
	public DataType getDataType()
	{
		return DataType.MIXED;
	}


	public DataType getDataType(int index)
	{
		return blockArray[startIndex + index].getDataType();
	}


	public void resize(int size)
	{
		
	}


	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("TUPLE[" + atomCount + "]: ");

		int start = startIndex;
		int stop = startIndex + atomCount;
		
		for (int i = start; i < stop; i++)
		{
			buffer.append(blockArray[i].toString());
			if (i < stop - 1)
				buffer.append(',');
		}

		buffer.append(']');
		return buffer.toString();
	}


	public boolean getBooleanValue(int index)
	{
		return blockArray[startIndex + index].getBooleanValue();
	}


	public byte getByteValue(int index)
	{
		return blockArray[startIndex + index].getByteValue();
	}


	public short getShortValue(int index)
	{
		return blockArray[startIndex + index].getShortValue();
	}


	public int getIntValue(int index)
	{
		return blockArray[startIndex + index].getIntValue();
	}


	public long getLongValue(int index)
	{
		return blockArray[startIndex + index].getLongValue();
	}


	public float getFloatValue(int index)
	{
		return blockArray[startIndex + index].getFloatValue();
	}


	public double getDoubleValue(int index)
	{
		return blockArray[startIndex + index].getDoubleValue();
	}


	public String getStringValue(int index)
	{
		
		return blockArray[startIndex + index].getStringValue();
	}


	public boolean getBooleanValue()
	{
		return blockArray[startIndex].getBooleanValue();
	}


	public byte getByteValue()
	{
		return blockArray[startIndex].getByteValue();
	}


	public short getShortValue()
	{
		return blockArray[startIndex].getShortValue();
	}


	public int getIntValue()
	{
		return blockArray[startIndex].getIntValue();
	}


	public long getLongValue()
	{
		return blockArray[startIndex].getLongValue();
	}


	public float getFloatValue()
	{
		return blockArray[startIndex].getFloatValue();
	}


	public double getDoubleValue()
	{
		return blockArray[startIndex].getDoubleValue();
	}


	public String getStringValue()
	{
		return blockArray[startIndex].getStringValue();
	}


	public void setBooleanValue(int index, boolean value)
	{
		
		blockArray[startIndex + index].setBooleanValue(value);
	}


	public void setByteValue(int index, byte value)
	{
		
		blockArray[startIndex + index].setByteValue(value);
	}


	public void setShortValue(int index, short value)
	{
		
		blockArray[startIndex + index].setShortValue(value);
	}


	public void setIntValue(int index, int value)
	{
		
		blockArray[startIndex + index].setIntValue(value);
	}


	public void setLongValue(int index, long value)
	{
		
		blockArray[startIndex + index].setLongValue(value);
	}


	public void setFloatValue(int index, float value)
	{
		
		blockArray[startIndex + index].setFloatValue(value);
	}


	public void setDoubleValue(int index, double value)
	{
		blockArray[startIndex + index].setDoubleValue(value);
	}


	public void setStringValue(int index, String value)
	{
		blockArray[startIndex + index].setStringValue(value);
	}


	public void setBooleanValue(boolean value)
	{
		blockArray[startIndex].setBooleanValue(value);
	}


	public void setByteValue(byte value)
	{
		blockArray[startIndex].setByteValue(value);
	}


	public void setShortValue(short value)
	{
		blockArray[startIndex].setShortValue(value);
	}


	public void setIntValue(int value)
	{
		blockArray[startIndex].setIntValue(value);
	}


	public void setLongValue(long value)
	{
		blockArray[startIndex].setLongValue(value);
	}


	public void setFloatValue(float value)
	{
		blockArray[startIndex].setFloatValue(value);
	}


	public void setDoubleValue(double value)
	{
		blockArray[startIndex].setDoubleValue(value);
	}


	public void setStringValue(String value)
	{
		blockArray[startIndex].setStringValue(value);
	}
}
