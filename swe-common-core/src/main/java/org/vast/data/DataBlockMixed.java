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

import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataType;


/**
 * <p>
 * Uses the composite pattern to carry a fixed size array
 * of mixed types DataBlocks. If dynamic size is needed, use DataBlockList.
 * Children datablocks will be read sequentially.
 * </p>
 *
 * @author Alex Robin
 * */
public class DataBlockMixed extends AbstractDataBlock
{
	private static final long serialVersionUID = 4082289189930783352L;
    protected AbstractDataBlock[] blockArray;
	protected int blockIndex;
	protected int localIndex;
    //protected int lastBlockIndex, lastIndex, lastCumulIndex;


	public DataBlockMixed()
	{
	}


	public DataBlockMixed(int numBlocks)
	{
		blockArray = new AbstractDataBlock[numBlocks];
	}
	
	
	public DataBlockMixed(int numBlocks, int atomCount)
	{
		blockArray = new AbstractDataBlock[numBlocks];
		this.atomCount = atomCount;
	}
	
	
	public DataBlockMixed(AbstractDataBlock... childBlocks)
    {
        blockArray = childBlocks;
        for (DataBlock dataBlock: childBlocks)
            this.atomCount += dataBlock.getAtomCount();
    }
	
	
	@Override
    public DataBlockMixed copy()
	{
		DataBlockMixed newBlock = new DataBlockMixed();
		newBlock.startIndex = this.startIndex;
		newBlock.blockArray = this.blockArray;	
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    @Override
    public DataBlockMixed renew()
    {
        DataBlockMixed newBlock = new DataBlockMixed();
        newBlock.startIndex = this.startIndex;
        newBlock.blockArray = new AbstractDataBlock[blockArray.length];
        
        // renew all blocks in the array
        for (int i=0; i<blockArray.length; i++)
        {
            if (this.blockArray[i] != null)
                newBlock.blockArray[i] = this.blockArray[i].renew();
        }
        
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public DataBlockMixed clone()
    {
        DataBlockMixed newBlock = new DataBlockMixed();
        newBlock.startIndex = this.startIndex;
        newBlock.blockArray = new AbstractDataBlock[blockArray.length];
        
        // fully copy (clone) all blocks in the array
        for (int i=0; i<blockArray.length; i++)
        {
            if (this.blockArray[i] != null)
                newBlock.blockArray[i] = this.blockArray[i].clone();
        }
        
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
        
        // init atom count to the whole size
        this.atomCount = 0;
        for (AbstractDataBlock block: blockArray)
            this.atomCount += block.atomCount;
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
		selectBlock(index);
		return blockArray[blockIndex].getDataType();
	}


	@Override
    public void resize(int size)
	{
		// don't know what to do here !!
	}
	
	
	protected void selectBlock(int index)
	{
        int i = 0;
        int cumul = 0;
        int size;
        int desiredIndex = startIndex + index;
        
        do
        {
            size = blockArray[i].atomCount;
            cumul += size;
            i++;
        }
        while (desiredIndex >= cumul);

        blockIndex = i - 1;
        localIndex = desiredIndex - (cumul - size);
        
//		int i, size, cumul;
//		int desiredIndex = startIndex + index;
//		
//        if (index > lastIndex)
//        {
//            i = lastBlockIndex;
//            cumul = lastCumulIndex;
//        }
//        else
//        {
//            i = 0;
//            cumul = 0;
//        }
//            
//		do
//		{
//			size = blockArray[i].atomCount;
//			cumul += size;
//			i++;
//		}
//		while (desiredIndex >= cumul);
//
//		blockIndex = i - 1;
//        lastCumulIndex = cumul - size;
//		localIndex = desiredIndex - lastCumulIndex;
//        
//        lastIndex = index;
//        lastBlockIndex = blockIndex;
//        lastCumulIndex = cumul - size;
	}


	@Override
    public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.toString());
		buffer.append(": ");
		buffer.append('[');

		if (atomCount > 0)
		{
			selectBlock(0);
			int start = blockIndex;
			selectBlock(getAtomCount() - 1);
			int stop = blockIndex + 1;
			
			for (int i = start; i < stop; i++)
			{
				buffer.append(blockArray[i].toString());
				if (i < stop - 1)
					buffer.append(',');
			}
		}
		
		buffer.append(']');
		return buffer.toString();
	}


	@Override
    public boolean getBooleanValue(int index)
	{
		selectBlock(index);
		return blockArray[blockIndex].getBooleanValue(localIndex);
	}


	@Override
    public byte getByteValue(int index)
	{
		selectBlock(index);
		return blockArray[blockIndex].getByteValue(localIndex);
	}


	@Override
    public short getShortValue(int index)
	{
		selectBlock(index);
		return blockArray[blockIndex].getShortValue(localIndex);
	}


	@Override
    public int getIntValue(int index)
	{
		selectBlock(index);
		return blockArray[blockIndex].getIntValue(localIndex);
	}


	@Override
    public long getLongValue(int index)
	{
		selectBlock(index);
		return blockArray[blockIndex].getLongValue(localIndex);
	}


	@Override
    public float getFloatValue(int index)
	{
		selectBlock(index);
		return blockArray[blockIndex].getFloatValue(localIndex);
	}


	@Override
    public double getDoubleValue(int index)
	{
		selectBlock(index);
        //System.out.println(blockIndex + " " + localIndex);
		return blockArray[blockIndex].getDoubleValue(localIndex);
	}


	@Override
    public String getStringValue(int index)
	{
		selectBlock(index);
		return blockArray[blockIndex].getStringValue(localIndex);
	}


	@Override
    public boolean getBooleanValue()
	{
		selectBlock(0);
		return blockArray[blockIndex].getBooleanValue(localIndex);
	}


	@Override
    public byte getByteValue()
	{
		selectBlock(0);
		return blockArray[blockIndex].getByteValue(localIndex);
	}


	@Override
    public short getShortValue()
	{
		selectBlock(0);
		return blockArray[blockIndex].getShortValue(localIndex);
	}


	@Override
    public int getIntValue()
	{
		selectBlock(0);
		return blockArray[blockIndex].getIntValue(localIndex);
	}


	@Override
    public long getLongValue()
	{
		selectBlock(0);
		return blockArray[blockIndex].getLongValue(localIndex);
	}


	@Override
    public float getFloatValue()
	{
		selectBlock(0);
		return blockArray[blockIndex].getFloatValue(localIndex);
	}


	@Override
    public double getDoubleValue()
	{
		selectBlock(0);
		return blockArray[blockIndex].getDoubleValue(localIndex);
	}


	@Override
    public String getStringValue()
	{
		selectBlock(0);
		return blockArray[blockIndex].getStringValue(localIndex);
	}
	

	public void setBlock(int blockIndex, AbstractDataBlock dataBlock)
	{
	    // update atom count
        AbstractDataBlock oldBlock = blockArray[blockIndex];
        if (oldBlock != null)
            this.atomCount -= oldBlock.atomCount;
        this.atomCount += dataBlock.atomCount;
        
        // set actual child block
        blockArray[blockIndex] = dataBlock;
	}
	
	
	@Override
    public void setBooleanValue(int index, boolean value)
	{
		selectBlock(index);
		blockArray[blockIndex].setBooleanValue(localIndex, value);
	}


	@Override
    public void setByteValue(int index, byte value)
	{
		selectBlock(index);
		blockArray[blockIndex].setByteValue(localIndex, value);
	}


	@Override
    public void setShortValue(int index, short value)
	{
		selectBlock(index);
		blockArray[blockIndex].setShortValue(localIndex, value);
	}


	@Override
    public void setIntValue(int index, int value)
	{
		selectBlock(index);
		blockArray[blockIndex].setIntValue(localIndex, value);
	}


	@Override
    public void setLongValue(int index, long value)
	{
		selectBlock(index);
		blockArray[blockIndex].setLongValue(localIndex, value);
	}


	@Override
    public void setFloatValue(int index, float value)
	{
		selectBlock(index);
		blockArray[blockIndex].setFloatValue(localIndex, value);
	}


	@Override
    public void setDoubleValue(int index, double value)
	{
		selectBlock(index);
		blockArray[blockIndex].setDoubleValue(localIndex, value);
	}


	@Override
    public void setStringValue(int index, String value)
	{
		selectBlock(index);
		blockArray[blockIndex].setStringValue(localIndex, value);
	}


	@Override
    public void setBooleanValue(boolean value)
	{
		selectBlock(0);
		blockArray[blockIndex].setBooleanValue(localIndex, value);
	}


	@Override
    public void setByteValue(byte value)
	{
		selectBlock(0);
		blockArray[blockIndex].setByteValue(localIndex, value);
	}


	@Override
    public void setShortValue(short value)
	{
		selectBlock(0);
		blockArray[blockIndex].setShortValue(localIndex, value);
	}


	@Override
    public void setIntValue(int value)
	{
		selectBlock(0);
		blockArray[blockIndex].setIntValue(localIndex, value);
	}


	@Override
    public void setLongValue(long value)
	{
		selectBlock(0);
		blockArray[blockIndex].setLongValue(localIndex, value);
	}


	@Override
    public void setFloatValue(float value)
	{
		selectBlock(0);
		blockArray[blockIndex].setFloatValue(localIndex, value);
	}


	@Override
    public void setDoubleValue(double value)
	{
		selectBlock(0);
		blockArray[blockIndex].setDoubleValue(localIndex, value);
	}


	@Override
    public void setStringValue(String value)
	{
		selectBlock(0);
		blockArray[blockIndex].setStringValue(localIndex, value);
	}
}
