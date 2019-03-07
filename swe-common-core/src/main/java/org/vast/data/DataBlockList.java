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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataType;


/**
 * <p>
 * Uses the composite pattern to hold a list of child DataBlocks.
 * This class also implements the DataBlock interface. 
 * </p>
 *
 * @author Alex Robin
 * */
public class DataBlockList extends AbstractDataBlock
{
    private static final long serialVersionUID = -413032909256132305L;
    protected List<DataBlock> blockList; // either ArrayList or LinkedList so it's serializable
	protected int blockAtomCount = -1;
	protected int blockIndex;
	protected int localIndex;
	protected boolean equalBlockSize;
    
    
    public DataBlockList()
    {
    	this(1, false);
    }
    
    
    public DataBlockList(int listSize)
    {
        this(listSize, true);
    }
    
    
    public DataBlockList(int listSize, boolean useArrayList)
    {
    	if (useArrayList)
    		this.blockList = new ArrayList<>(listSize);
    	else
    		this.blockList = new LinkedList<>();
    	
    	this.equalBlockSize = true;
    }
    
    
    @Override
    public DataBlockList copy()
    {
        DataBlockList newBlock = new DataBlockList();
        newBlock.startIndex = this.startIndex;
        newBlock.blockAtomCount = this.blockAtomCount;
        newBlock.equalBlockSize = this.equalBlockSize;
        newBlock.blockList = this.blockList;       
        return newBlock;
    }
    
    
    @Override
    public DataBlockList renew()
    {
        DataBlockList newBlock = new DataBlockList();
        newBlock.startIndex = this.startIndex;
        newBlock.blockAtomCount = this.blockAtomCount;
        newBlock.equalBlockSize = this.equalBlockSize;
        newBlock.blockList = new LinkedList<>();
        
        // renew all blocks in the list
        Iterator<DataBlock> it = this.blockList.iterator();
        while (it.hasNext())
            newBlock.add(it.next().renew());
        
        return newBlock;
    }
    
    
    @Override
    public DataBlockList clone()
    {
        DataBlockList newBlock = new DataBlockList();
        newBlock.startIndex = this.startIndex;
        newBlock.blockAtomCount = this.blockAtomCount;
        newBlock.equalBlockSize = this.equalBlockSize;
        newBlock.blockList = new LinkedList<>();
        
        // fully copy (clone) all blocks in the list
        Iterator<DataBlock> it = this.blockList.iterator();
        while (it.hasNext())
            newBlock.add(it.next().clone());
            
        return newBlock;
    }
    
    
    @Override
    public List<DataBlock> getUnderlyingObject()
    {
        return blockList;
    }
    
    
    @Override
    public void setUnderlyingObject(Object obj)
    {
    	this.blockList = (List<DataBlock>)(Serializable)obj;
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
		return blockList.get(blockIndex).getDataType();
	}
	
	
	@Override
    public void resize(int size)
	{
		if (blockList instanceof ArrayList)
		    ((ArrayList<DataBlock>)blockList).ensureCapacity(size);
	    
	    if (!blockList.isEmpty())
        {
		    DataBlock childBlock = get(0);
		    atomCount = childBlock.getAtomCount() * size;
    		blockList.clear();        
        
            for (int i=0; i<size; i++)
                blockList.add(childBlock.clone());
        }        
	}

    
	protected final void selectBlock(int index)
	{
		int desiredIndex = index + startIndex;
		
		if (equalBlockSize)
		{
			blockIndex = desiredIndex / blockAtomCount;
	        localIndex = desiredIndex % blockAtomCount;
		}
		else
		{
	    	int size;
			int cumul = 0;
			int i = 0;
	
			do
			{
				size = blockList.get(i).getAtomCount();
				cumul += size;
				i++;
			}
			while (desiredIndex >= cumul);
	
			blockIndex = i - 1;
			localIndex = desiredIndex - (cumul - size);
		}
	}
    
    
    public ListIterator<DataBlock> blockIterator()
    {
        return this.blockList.listIterator();
    }
    
    
    public int getListSize()
    {
    	return this.blockList.size();
    }
    
    
    public void add(DataBlock block)
    {
    	if (blockAtomCount < 0)
    		blockAtomCount = block.getAtomCount();
    	
    	else if (block.getAtomCount() != blockAtomCount)
    		equalBlockSize = false;

        blockList.add(block);
    	atomCount += block.getAtomCount();
    }
    
    
    public void add(int blockIndex, AbstractDataBlock block)
    {
    	blockList.add(blockIndex, block);
    }
    
    
    public void set(int blockIndex, DataBlock block)
    {
    	DataBlock oldBlock = blockList.set(blockIndex, block);
    	atomCount -= oldBlock.getAtomCount();
    	atomCount += block.getAtomCount();
    }
    
    
    public DataBlock get(int blockIndex)
    {
        return blockList.get(blockIndex);
    }
    
    
    public void remove(AbstractDataBlock block)
    {
    	blockList.remove(block);
    	atomCount -= block.atomCount;
    }
    
    
    public void remove(int blockIndex)
    {
    	DataBlock oldBlock = blockList.remove(blockIndex);
    	atomCount -= oldBlock.getAtomCount();
    }
	
	
    @Override
    public String toString()
    {
    	StringBuilder buffer = new StringBuilder();
		buffer.append("LIST " + super.toString());
		buffer.append('\n');
    	int imax = blockList.size();
    	
    	for (int i=0; i<imax; i++)
    	{
    		buffer.append(blockList.get(i).toString());
        	buffer.append('\n');        		
    	}
    	
    	return buffer.toString();
    }
	
	
	@Override
    public boolean getBooleanValue(int index)
	{
		selectBlock(index);
		return blockList.get(blockIndex).getBooleanValue(localIndex);
	}


	@Override
    public byte getByteValue(int index)
	{
		selectBlock(index);
		return blockList.get(blockIndex).getByteValue(localIndex);
	}


	@Override
    public short getShortValue(int index)
	{
		selectBlock(index);
		return blockList.get(blockIndex).getShortValue(localIndex);
	}


	@Override
	public int getIntValue(int index)
	{
		selectBlock(index);
		return blockList.get(blockIndex).getIntValue(localIndex);
	}


	@Override
    public long getLongValue(int index)
	{
		selectBlock(index);
		return blockList.get(blockIndex).getLongValue(localIndex);
	}


	@Override
    public float getFloatValue(int index)
	{
		selectBlock(index);
		return blockList.get(blockIndex).getFloatValue(localIndex);
	}


	@Override
    public double getDoubleValue(int index)
	{
		selectBlock(index);
		return blockList.get(blockIndex).getDoubleValue(localIndex);
	}


	@Override
    public String getStringValue(int index)
	{
		selectBlock(index);
		return blockList.get(blockIndex).getStringValue(localIndex);
	}


	@Override
    public boolean getBooleanValue()
	{
		selectBlock(0);
		return blockList.get(blockIndex).getBooleanValue(localIndex);
	}


	@Override
    public byte getByteValue()
	{
		selectBlock(0);
		return blockList.get(blockIndex).getByteValue(localIndex);
	}


	@Override
    public short getShortValue()
	{
		selectBlock(0);
		return blockList.get(blockIndex).getShortValue(localIndex);
	}


	@Override
    public int getIntValue()
	{
		selectBlock(0);
		return blockList.get(blockIndex).getIntValue(localIndex);
	}


	@Override
    public long getLongValue()
	{
		selectBlock(0);
		return blockList.get(blockIndex).getLongValue(localIndex);
	}


	@Override
    public float getFloatValue()
	{
		selectBlock(0);
		return blockList.get(blockIndex).getFloatValue(localIndex);
	}


	@Override
    public double getDoubleValue()
	{
		selectBlock(0);
		return blockList.get(blockIndex).getDoubleValue(localIndex);
	}


	@Override
    public String getStringValue()
	{
		selectBlock(0);
		return blockList.get(blockIndex).getStringValue(localIndex);
	}


	@Override
    public void setBooleanValue(int index, boolean value)
	{
		selectBlock(index);
		blockList.get(blockIndex).setBooleanValue(localIndex, value);
	}


	@Override
    public void setByteValue(int index, byte value)
	{
		selectBlock(index);
		blockList.get(blockIndex).setByteValue(localIndex, value);
	}


	@Override
    public void setShortValue(int index, short value)
	{
		selectBlock(index);
		blockList.get(blockIndex).setShortValue(localIndex, value);
	}


	@Override
    public void setIntValue(int index, int value)
	{
		selectBlock(index);
		blockList.get(blockIndex).setIntValue(localIndex, value);
	}


	@Override
    public void setLongValue(int index, long value)
	{
		selectBlock(index);
		blockList.get(blockIndex).setLongValue(localIndex, value);
	}


	@Override
    public void setFloatValue(int index, float value)
	{
		selectBlock(index);
		blockList.get(blockIndex).setFloatValue(localIndex, value);
	}


	@Override
    public void setDoubleValue(int index, double value)
	{
		selectBlock(index);
		blockList.get(blockIndex).setDoubleValue(localIndex, value);
	}


	@Override
    public void setStringValue(int index, String value)
	{
		selectBlock(index);
		blockList.get(blockIndex).setStringValue(localIndex, value);
	}


	@Override
    public void setBooleanValue(boolean value)
	{
		selectBlock(0);
		blockList.get(blockIndex).setBooleanValue(localIndex, value);
	}


	@Override
    public void setByteValue(byte value)
	{
		selectBlock(0);
		blockList.get(blockIndex).setByteValue(localIndex, value);
	}


	@Override
    public void setShortValue(short value)
	{
		selectBlock(0);
		blockList.get(blockIndex).setShortValue(localIndex, value);
	}


	@Override
    public void setIntValue(int value)
	{
		selectBlock(0);
		blockList.get(blockIndex).setIntValue(localIndex, value);
	}


	@Override
    public void setLongValue(long value)
	{
		selectBlock(0);
		blockList.get(blockIndex).setLongValue(localIndex, value);
	}


	@Override
    public void setFloatValue(float value)
	{
		selectBlock(0);
		blockList.get(blockIndex).setFloatValue(localIndex, value);
	}


	@Override
    public void setDoubleValue(double value)
	{
		selectBlock(0);
		blockList.get(blockIndex).setDoubleValue(localIndex, value);
	}


	@Override
    public void setStringValue(String value)
	{
		selectBlock(0);
		blockList.get(blockIndex).setStringValue(localIndex, value);
	}
}