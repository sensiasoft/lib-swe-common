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
 * Uses the composite pattern to carry a fixed size array
 * of mixed types DataBlocks. If dynamic size is needed, use DataBlockList.
 * Children datablocks will be read in parallel. Designed specifically for
 * DataGroups. (slightly more performant than DataBlockParallel for the
 * case of a group of scalars of different data types)
 * </p>
 *
 * @author Alex Robin
 * */
public class DataBlockTuple extends AbstractDataBlock
{
	private static final long serialVersionUID = -1435102674147112451L;
    protected AbstractDataBlock[] blockArray;


	public DataBlockTuple()
	{
	}


	public DataBlockTuple(int numBlocks)
	{
		blockArray = new AbstractDataBlock[numBlocks];
	}
	
	
	@Override
    public DataBlockTuple copy()
	{
		DataBlockTuple newBlock = new DataBlockTuple();
		newBlock.startIndex = this.startIndex;
		newBlock.blockArray = this.blockArray;	
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    @Override
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
    
    
    @Override
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
    
    
    @Override
    public AbstractDataBlock[] getUnderlyingObject()
    {
        return blockArray;
    }
    
    
    protected void setUnderlyingObject(AbstractDataBlock[] blockArray)
    {
        this.blockArray = blockArray;
    }
    
    
    @Override
    public void setUnderlyingObject(Object obj)
    {
        setUnderlyingObject((AbstractDataBlock[])obj);
    }
	
	
	@Override
    public DataType getDataType()
	{
		return DataType.MIXED;
	}


	@Override
    public DataType getDataType(int index)
	{
		return blockArray[startIndex + index].getDataType();
	}


	@Override
    public void resize(int size)
	{
		
	}


	@Override
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


	@Override
    public boolean getBooleanValue(int index)
	{
		return blockArray[startIndex + index].getBooleanValue();
	}


	@Override
    public byte getByteValue(int index)
	{
		return blockArray[startIndex + index].getByteValue();
	}


	@Override
    public short getShortValue(int index)
	{
		return blockArray[startIndex + index].getShortValue();
	}


	@Override
    public int getIntValue(int index)
	{
		return blockArray[startIndex + index].getIntValue();
	}


	@Override
    public long getLongValue(int index)
	{
		return blockArray[startIndex + index].getLongValue();
	}


	@Override
    public float getFloatValue(int index)
	{
		return blockArray[startIndex + index].getFloatValue();
	}


	@Override
    public double getDoubleValue(int index)
	{
		return blockArray[startIndex + index].getDoubleValue();
	}


	@Override
    public String getStringValue(int index)
	{
		
		return blockArray[startIndex + index].getStringValue();
	}


	@Override
    public boolean getBooleanValue()
	{
		return blockArray[startIndex].getBooleanValue();
	}


	@Override
    public byte getByteValue()
	{
		return blockArray[startIndex].getByteValue();
	}


	@Override
    public short getShortValue()
	{
		return blockArray[startIndex].getShortValue();
	}


	@Override
    public int getIntValue()
	{
		return blockArray[startIndex].getIntValue();
	}


	@Override
    public long getLongValue()
	{
		return blockArray[startIndex].getLongValue();
	}


	@Override
    public float getFloatValue()
	{
		return blockArray[startIndex].getFloatValue();
	}


	@Override
    public double getDoubleValue()
	{
		return blockArray[startIndex].getDoubleValue();
	}


	@Override
    public String getStringValue()
	{
		return blockArray[startIndex].getStringValue();
	}


	@Override
    public void setBooleanValue(int index, boolean value)
	{
		
		blockArray[startIndex + index].setBooleanValue(value);
	}


	@Override
    public void setByteValue(int index, byte value)
	{
		
		blockArray[startIndex + index].setByteValue(value);
	}


	@Override
    public void setShortValue(int index, short value)
	{
		
		blockArray[startIndex + index].setShortValue(value);
	}


	@Override
    public void setIntValue(int index, int value)
	{
		
		blockArray[startIndex + index].setIntValue(value);
	}


	@Override
    public void setLongValue(int index, long value)
	{
		
		blockArray[startIndex + index].setLongValue(value);
	}


	@Override
    public void setFloatValue(int index, float value)
	{
		
		blockArray[startIndex + index].setFloatValue(value);
	}


	@Override
    public void setDoubleValue(int index, double value)
	{
		blockArray[startIndex + index].setDoubleValue(value);
	}


	@Override
    public void setStringValue(int index, String value)
	{
		blockArray[startIndex + index].setStringValue(value);
	}


	@Override
    public void setBooleanValue(boolean value)
	{
		blockArray[startIndex].setBooleanValue(value);
	}


	@Override
    public void setByteValue(byte value)
	{
		blockArray[startIndex].setByteValue(value);
	}


	@Override
    public void setShortValue(short value)
	{
		blockArray[startIndex].setShortValue(value);
	}


	@Override
    public void setIntValue(int value)
	{
		blockArray[startIndex].setIntValue(value);
	}


	@Override
    public void setLongValue(long value)
	{
		blockArray[startIndex].setLongValue(value);
	}


	@Override
    public void setFloatValue(float value)
	{
		blockArray[startIndex].setFloatValue(value);
	}


	@Override
    public void setDoubleValue(double value)
	{
		blockArray[startIndex].setDoubleValue(value);
	}


	@Override
    public void setStringValue(String value)
	{
		blockArray[startIndex].setStringValue(value);
	}
}
