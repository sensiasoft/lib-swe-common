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
 * Data Block Parallel
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Uses the composite pattern to carry a fixed size array
 * of parallel array DataBlocks.  Children datablocks will
 * thus be read in parallel.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @version 1.0
 */
public class DataBlockParallel extends AbstractDataBlock
{
	private static final long serialVersionUID = 8610635426607515415L;
    protected AbstractDataBlock[] blockArray;
	protected int blockIndex;
	protected int localIndex;


	public DataBlockParallel()
	{
	}


	public DataBlockParallel(int numBlocks)
	{
		blockArray = new AbstractDataBlock[numBlocks];
	}
	
	
	public void setChildBlock(int blockIndex, AbstractDataBlock dataBlock)
	{
	    if (blockArray[0] != null)
	        if (blockArray[0].atomCount != dataBlock.atomCount)
	            throw new IllegalArgumentException("All child data blocks of a parallel data block must have the same size");
	    
	    if (blockArray[blockIndex] == null)
	        this.atomCount += dataBlock.atomCount;
	    
	    blockArray[blockIndex] = dataBlock;
	}
	
	
	public DataBlockParallel copy()
	{
		DataBlockParallel newBlock = new DataBlockParallel();
		newBlock.startIndex = this.startIndex;
		newBlock.blockArray = new AbstractDataBlock[blockArray.length];
		
        // shallow copy all blocks in the array
		for (int i=0; i<blockArray.length; i++)
			newBlock.blockArray[i] = this.blockArray[i].copy();
		
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    public DataBlockParallel renew()
    {
        DataBlockParallel newBlock = new DataBlockParallel();
        newBlock.startIndex = this.startIndex;
        newBlock.blockArray = new AbstractDataBlock[blockArray.length];
        
        // renew all blocks in the array
        for (int i=0; i<blockArray.length; i++)
            newBlock.blockArray[i] = this.blockArray[i].renew();
        
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public DataBlockParallel clone()
    {
        DataBlockParallel newBlock = new DataBlockParallel();
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
    
    
    public void setUnderlyingObject(Object obj)
    {
    	this.blockArray = (AbstractDataBlock[])obj;
    }
	
	
	public DataType getDataType()
	{
		return DataType.MIXED;
	}


	public DataType getDataType(int index)
	{
		selectBlock(index);
		return blockArray[blockIndex].getDataType();
	}


	public void resize(int size)
	{
		// resize all sub blocks
		for (int i=0; i<blockArray.length; i++)
			blockArray[i].resize(size/blockArray.length);
		
		this.atomCount = size;
	}


	protected void selectBlock(int index)
	{
		blockIndex = index % blockArray.length;
        localIndex = startIndex + index / blockArray.length;
        localIndex -= blockArray[blockIndex].startIndex;
	}


	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("PARALLEL: ");
		buffer.append('[');

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

		buffer.append(']');
		return buffer.toString();
	}


	public boolean getBooleanValue(int index)
	{
		selectBlock(index);
		return blockArray[blockIndex].getBooleanValue(localIndex);
	}


	public byte getByteValue(int index)
	{
		selectBlock(index);
		return blockArray[blockIndex].getByteValue(localIndex);
	}


	public short getShortValue(int index)
	{
		selectBlock(index);
		return blockArray[blockIndex].getShortValue(localIndex);
	}


	public int getIntValue(int index)
	{
		selectBlock(index);
		return blockArray[blockIndex].getIntValue(localIndex);
	}


	public long getLongValue(int index)
	{
		selectBlock(index);
		return blockArray[blockIndex].getLongValue(localIndex);
	}


	public float getFloatValue(int index)
	{
		selectBlock(index);
		return blockArray[blockIndex].getFloatValue(localIndex);
	}


	public double getDoubleValue(int index)
	{
		selectBlock(index);
        //System.out.println(blockIndex + " " + localIndex);
		return blockArray[blockIndex].getDoubleValue(localIndex);
	}


	public String getStringValue(int index)
	{
		selectBlock(index);
		return blockArray[blockIndex].getStringValue(localIndex);
	}


	public boolean getBooleanValue()
	{
		selectBlock(0);
		return blockArray[blockIndex].getBooleanValue(localIndex);
	}


	public byte getByteValue()
	{
		selectBlock(0);
		return blockArray[blockIndex].getByteValue(localIndex);
	}


	public short getShortValue()
	{
		selectBlock(0);
		return blockArray[blockIndex].getShortValue(localIndex);
	}


	public int getIntValue()
	{
		selectBlock(0);
		return blockArray[blockIndex].getIntValue(localIndex);
	}


	public long getLongValue()
	{
		selectBlock(0);
		return blockArray[blockIndex].getLongValue(localIndex);
	}


	public float getFloatValue()
	{
		selectBlock(0);
		return blockArray[blockIndex].getFloatValue(localIndex);
	}


	public double getDoubleValue()
	{
		selectBlock(0);
		return blockArray[blockIndex].getDoubleValue(localIndex);
	}


	public String getStringValue()
	{
		selectBlock(0);
		return blockArray[blockIndex].getStringValue(localIndex);
	}


	public void setBooleanValue(int index, boolean value)
	{
		selectBlock(index);
		blockArray[blockIndex].setBooleanValue(localIndex, value);
	}


	public void setByteValue(int index, byte value)
	{
		selectBlock(index);
		blockArray[blockIndex].setByteValue(localIndex, value);
	}


	public void setShortValue(int index, short value)
	{
		selectBlock(index);
		blockArray[blockIndex].setShortValue(localIndex, value);
	}


	public void setIntValue(int index, int value)
	{
		selectBlock(index);
		blockArray[blockIndex].setIntValue(localIndex, value);
	}


	public void setLongValue(int index, long value)
	{
		selectBlock(index);
		blockArray[blockIndex].setLongValue(localIndex, value);
	}


	public void setFloatValue(int index, float value)
	{
		selectBlock(index);
		blockArray[blockIndex].setFloatValue(localIndex, value);
	}


	public void setDoubleValue(int index, double value)
	{
		selectBlock(index);
		blockArray[blockIndex].setDoubleValue(localIndex, value);
	}


	public void setStringValue(int index, String value)
	{
		selectBlock(index);
		blockArray[blockIndex].setStringValue(localIndex, value);
	}


	public void setBooleanValue(boolean value)
	{
		selectBlock(0);
		blockArray[blockIndex].setBooleanValue(localIndex, value);
	}


	public void setByteValue(byte value)
	{
		selectBlock(0);
		blockArray[blockIndex].setByteValue(localIndex, value);
	}


	public void setShortValue(short value)
	{
		selectBlock(0);
		blockArray[blockIndex].setShortValue(localIndex, value);
	}


	public void setIntValue(int value)
	{
		selectBlock(0);
		blockArray[blockIndex].setIntValue(localIndex, value);
	}


	public void setLongValue(long value)
	{
		selectBlock(0);
		blockArray[blockIndex].setLongValue(localIndex, value);
	}


	public void setFloatValue(float value)
	{
		selectBlock(0);
		blockArray[blockIndex].setFloatValue(localIndex, value);
	}


	public void setDoubleValue(double value)
	{
		selectBlock(0);
		blockArray[blockIndex].setDoubleValue(localIndex, value);
	}


	public void setStringValue(String value)
	{
		selectBlock(0);
		blockArray[blockIndex].setStringValue(localIndex, value);
	}
}
