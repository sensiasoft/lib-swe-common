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
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * */
public class DataBlockList extends AbstractDataBlock
{
    private static final long serialVersionUID = -9032080133600839734L;
    protected List<DataBlock> blockList;
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
    		this.blockList = new ArrayList<DataBlock>(listSize);
    	else
    		this.blockList = new LinkedList<DataBlock>();
    	
    	this.equalBlockSize = true;
    }
    
    
    public DataBlockList copy()
    {
        DataBlockList newBlock = new DataBlockList();
        newBlock.startIndex = this.startIndex;
        newBlock.blockAtomCount = this.blockAtomCount;
        newBlock.equalBlockSize = this.equalBlockSize;
        newBlock.blockList = this.blockList;       
        return newBlock;
    }
    
    
    public DataBlockList renew()
    {
        DataBlockList newBlock = new DataBlockList();
        newBlock.startIndex = this.startIndex;
        newBlock.blockAtomCount = this.blockAtomCount;
        newBlock.equalBlockSize = this.equalBlockSize;
        newBlock.blockList = new LinkedList<DataBlock>();
        
        // renew all blocks in the list
        Iterator<DataBlock> it = this.blockList.iterator();
        while (it.hasNext())
            newBlock.add(it.next().renew());
        
        return newBlock;
    }
    
    
    public DataBlockList clone()
    {
        DataBlockList newBlock = new DataBlockList();
        newBlock.startIndex = this.startIndex;
        newBlock.blockAtomCount = this.blockAtomCount;
        newBlock.equalBlockSize = this.equalBlockSize;
        newBlock.blockList = new LinkedList<DataBlock>();
        
        // fully copy (clone) all blocks in the list
        Iterator<DataBlock> it = this.blockList.iterator();
        while (it.hasNext())
            newBlock.add(it.next().clone());
            
        return newBlock;
    }
    
    
    public List<DataBlock> getUnderlyingObject()
    {
        return blockList;
    }
    
    
    public void setUnderlyingObject(List<DataBlock> blockList)
    {
        this.blockList = blockList;
    }
    
    
    public void setUnderlyingObject(Object obj)
    {
    	this.blockList = (LinkedList<DataBlock>)obj;
    }
    
    
    public DataType getDataType()
	{
		return DataType.MIXED;
	}


	public DataType getDataType(int index)
	{
		selectBlock(index);
		return blockList.get(blockIndex).getDataType();
	}
	
	
	public void resize(int size)
	{
		if (blockList instanceof ArrayList)
		    ((ArrayList<DataBlock>)blockList).ensureCapacity(size);
	    
	    if (blockList.size() > 0)
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
	
	
	public String toString()
    {
    	StringBuffer buffer = new StringBuffer();
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
	
	
	public boolean getBooleanValue(int index)
	{
		selectBlock(index);
		return blockList.get(blockIndex).getBooleanValue(localIndex);
	}


	public byte getByteValue(int index)
	{
		selectBlock(index);
		return blockList.get(blockIndex).getByteValue(localIndex);
	}


	public short getShortValue(int index)
	{
		selectBlock(index);
		return blockList.get(blockIndex).getShortValue(localIndex);
	}


	public int getIntValue(int index)
	{
		selectBlock(index);
		return blockList.get(blockIndex).getIntValue(localIndex);
	}


	public long getLongValue(int index)
	{
		selectBlock(index);
		return blockList.get(blockIndex).getLongValue(localIndex);
	}


	public float getFloatValue(int index)
	{
		selectBlock(index);
		return blockList.get(blockIndex).getFloatValue(localIndex);
	}


	public double getDoubleValue(int index)
	{
		selectBlock(index);
		return blockList.get(blockIndex).getDoubleValue(localIndex);
	}


	public String getStringValue(int index)
	{
		selectBlock(index);
		return blockList.get(blockIndex).getStringValue(localIndex);
	}


	public boolean getBooleanValue()
	{
		selectBlock(0);
		return blockList.get(blockIndex).getBooleanValue(localIndex);
	}


	public byte getByteValue()
	{
		selectBlock(0);
		return blockList.get(blockIndex).getByteValue(localIndex);
	}


	public short getShortValue()
	{
		selectBlock(0);
		return blockList.get(blockIndex).getShortValue(localIndex);
	}


	public int getIntValue()
	{
		selectBlock(0);
		return blockList.get(blockIndex).getIntValue(localIndex);
	}


	public long getLongValue()
	{
		selectBlock(0);
		return blockList.get(blockIndex).getLongValue(localIndex);
	}


	public float getFloatValue()
	{
		selectBlock(0);
		return blockList.get(blockIndex).getFloatValue(localIndex);
	}


	public double getDoubleValue()
	{
		selectBlock(0);
		return blockList.get(blockIndex).getDoubleValue(localIndex);
	}


	public String getStringValue()
	{
		selectBlock(0);
		return blockList.get(blockIndex).getStringValue(localIndex);
	}


	public void setBooleanValue(int index, boolean value)
	{
		selectBlock(index);
		blockList.get(blockIndex).setBooleanValue(localIndex, value);
	}


	public void setByteValue(int index, byte value)
	{
		selectBlock(index);
		blockList.get(blockIndex).setByteValue(localIndex, value);
	}


	public void setShortValue(int index, short value)
	{
		selectBlock(index);
		blockList.get(blockIndex).setShortValue(localIndex, value);
	}


	public void setIntValue(int index, int value)
	{
		selectBlock(index);
		blockList.get(blockIndex).setIntValue(localIndex, value);
	}


	public void setLongValue(int index, long value)
	{
		selectBlock(index);
		blockList.get(blockIndex).setLongValue(localIndex, value);
	}


	public void setFloatValue(int index, float value)
	{
		selectBlock(index);
		blockList.get(blockIndex).setFloatValue(localIndex, value);
	}


	public void setDoubleValue(int index, double value)
	{
		selectBlock(index);
		blockList.get(blockIndex).setDoubleValue(localIndex, value);
	}


	public void setStringValue(int index, String value)
	{
		selectBlock(index);
		blockList.get(blockIndex).setStringValue(localIndex, value);
	}


	public void setBooleanValue(boolean value)
	{
		selectBlock(0);
		blockList.get(blockIndex).setBooleanValue(localIndex, value);
	}


	public void setByteValue(byte value)
	{
		selectBlock(0);
		blockList.get(blockIndex).setByteValue(localIndex, value);
	}


	public void setShortValue(short value)
	{
		selectBlock(0);
		blockList.get(blockIndex).setShortValue(localIndex, value);
	}


	public void setIntValue(int value)
	{
		selectBlock(0);
		blockList.get(blockIndex).setIntValue(localIndex, value);
	}


	public void setLongValue(long value)
	{
		selectBlock(0);
		blockList.get(blockIndex).setLongValue(localIndex, value);
	}


	public void setFloatValue(float value)
	{
		selectBlock(0);
		blockList.get(blockIndex).setFloatValue(localIndex, value);
	}


	public void setDoubleValue(double value)
	{
		selectBlock(0);
		blockList.get(blockIndex).setDoubleValue(localIndex, value);
	}


	public void setStringValue(String value)
	{
		selectBlock(0);
		blockList.get(blockIndex).setStringValue(localIndex, value);
	}
}