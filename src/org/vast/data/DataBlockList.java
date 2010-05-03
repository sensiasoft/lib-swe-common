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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.vast.cdm.common.DataType;


/**
 * <p><b>Title:</b><br/>
 * Data Block List
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Uses the composite pattern to hold a list of child DataBlocks.
 * This class also implements the DataBlock interface. 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @version 1.0
 */
public class DataBlockList extends AbstractDataBlock
{
    private static final long serialVersionUID = -9032080133600839734L;
    protected List<AbstractDataBlock> blockList;
	protected int blockAtomCount = -1;
	protected int blockIndex;
	protected int localIndex;
	protected boolean equalBlockSize;
    
    
    public DataBlockList()
    {
    	this(false);
    }
    
    
    public DataBlockList(boolean useArrayList)
    {
    	if (useArrayList)
    		this.blockList = new ArrayList<AbstractDataBlock>();
    	else
    		this.blockList = new LinkedList<AbstractDataBlock>();
    	
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
        newBlock.blockList = new LinkedList<AbstractDataBlock>();
        
        // renew all blocks in the list
        Iterator<AbstractDataBlock> it = this.blockList.iterator();
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
        newBlock.blockList = new LinkedList<AbstractDataBlock>();
        
        // fully copy (clone) all blocks in the list
        Iterator<AbstractDataBlock> it = this.blockList.iterator();
        while (it.hasNext())
            newBlock.add(it.next().clone());
            
        return newBlock;
    }
    
    
    public List<AbstractDataBlock> getUnderlyingObject()
    {
        return blockList;
    }
    
    
    public void setUnderlyingObject(List<AbstractDataBlock> blockList)
    {
        this.blockList = blockList;
    }
    
    
    public void setUnderlyingObject(Object obj)
    {
    	this.blockList = (LinkedList<AbstractDataBlock>)obj;
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
		AbstractDataBlock childBlock = get(0);
        atomCount = childBlock.atomCount * size;
		blockList.clear();
        
        if (childBlock != null)
        {
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
				size = blockList.get(i).atomCount;
				cumul += size;
				i++;
			}
			while (desiredIndex >= cumul);
	
			blockIndex = i - 1;
			localIndex = desiredIndex - (cumul - size);
		}
	}
    
    
    public ListIterator<AbstractDataBlock> blockIterator()
    {
        return this.blockList.listIterator();
    }
    
    
    public int getListSize()
    {
    	return this.blockList.size();
    }
    
    
    public void add(AbstractDataBlock block)
    {
    	if (blockAtomCount < 0)
    		blockAtomCount = block.atomCount;
    	
    	else if (block.atomCount != blockAtomCount)
    		equalBlockSize = false;

        blockList.add(block);
    	atomCount += block.atomCount;
    }
    
    
    public void add(int blockIndex, AbstractDataBlock block)
    {
    	blockList.add(blockIndex, block);
    }
    
    
    public void set(int blockIndex, AbstractDataBlock block)
    {
    	AbstractDataBlock oldBlock = blockList.set(blockIndex, block);
    	atomCount -= oldBlock.atomCount;
    	atomCount += block.atomCount;
    }
    
    
    public AbstractDataBlock get(int blockIndex)
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
    	AbstractDataBlock oldBlock = blockList.remove(blockIndex);
    	atomCount -= oldBlock.atomCount;
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