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

import java.util.*;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataBlock;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataType;


/**
 * <p><b>Title:</b><br/>
 * DataChoice Component
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Exclusive list of DataComponents (DataChoice)
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @version 1.0
 */
public class DataChoice extends AbstractDataComponent
{
	protected List<AbstractDataComponent> itemList;
	protected int selectedItem = -1;
    

    public DataChoice()
    {
    	this.itemList = new ArrayList<AbstractDataComponent>();
    }
    
    
    public DataChoice(int size)
    {
        this.itemList = new ArrayList<AbstractDataComponent>(size);
    }
    
    
    public DataChoice(int size, String name)
    {
        this(size);
        this.setName(name);
    }
    
    
    @Override
    public DataChoice copy()
    {
    	int groupSize = itemList.size();    	
    	DataChoice newChoice = new DataChoice(groupSize);
    	newChoice.name = this.name;
    	newChoice.properties = this.properties;    	
    	
    	for (int i=0; i<groupSize; i++)
    	{
    		AbstractDataComponent child = itemList.get(i);
    		newChoice.addComponent(child.copy());
    	}
    	
    	return newChoice;
    }
    
    
    @Override
    protected void updateStartIndex(int startIndex)
    {
        dataBlock.startIndex = startIndex;
        
        if (dataBlock instanceof DataBlockMixed)
        {
            // TODO updateStartIndex for group with DataBlockMixed
        }
        else if (dataBlock instanceof DataBlockParallel)
        {
            for (int i = 0; i < itemList.size(); i++)
            {
                AbstractDataComponent childComponent = itemList.get(i);
                childComponent.updateStartIndex(startIndex);
            }
        }
        else // case of primitive array
        {
            int scalarTotal = 0;
            for (int i = 0; i < itemList.size(); i++)
            {
                AbstractDataComponent childComponent = itemList.get(i);
                childComponent.updateStartIndex(startIndex + scalarTotal);
                scalarTotal += childComponent.scalarCount;
            }
        }
    }
    
    
    @Override
    protected void updateAtomCount(int childOffsetCount)
    {
        if (dataBlock != null)
            dataBlock.atomCount += childOffsetCount;
        
        if (parent != null)
            parent.updateAtomCount(childOffsetCount);
    }
    
    
    /**
     * Inserts the component at the specified index
     * @param index
     * @param component
     */
    public void insertComponent(int index, DataComponent component)
    {
        itemList.add(index, (AbstractDataComponent)component);
        ((AbstractDataComponent)component).parent = this;
        
        // refresh index table
        this.names.clear();
        for (int i=0; i<itemList.size(); i++)
        {
            AbstractDataComponent next = itemList.get(i);
            this.names.put(next.getName(), i);
        }
    }
    
    
    public void insertComponent(String previousComponentName, DataComponent component)
    {
        int index = this.getComponentIndex(previousComponentName);
        
        if (index == -1)
            throw new IllegalArgumentException("No component with name " + previousComponentName + " found");
        
        insertComponent(index, component);
    }
    
    
    @Override
    public void addComponent(DataComponent component)
    {
        String componentName = component.getName();
        if (componentName != null)
            this.names.put(componentName, this.getComponentCount());
        
        itemList.add((AbstractDataComponent)component);
        ((AbstractDataComponent)component).parent = this;
    }


    @Override
    public AbstractDataComponent getComponent(int index)
    {
        AbstractDataComponent component = (AbstractDataComponent)itemList.get(index);
        return component;
    }


    @Override
    public void removeComponent(int index)
    {
        itemList.remove(index);
    }


    @Override
    public void removeAllComponents()
    {
        itemList.clear();
        this.dataBlock = null;
    }
    
    
    @Override
    public void setData(DataBlock dataBlock)
    {
    	this.dataBlock = (AbstractDataBlock)dataBlock;

		// also assign dataBlock to children
    	if (dataBlock instanceof DataBlockParallel)
    	{
    		for (int i = 0; i < itemList.size(); i++)
    		{
    			AbstractDataBlock childBlock = ((DataBlockParallel)dataBlock).blockArray[i];
    			itemList.get(i).setData(childBlock);
    		}
    	}
        else if (dataBlock instanceof DataBlockMixed)
        {
            for (int i = 0; i < itemList.size(); i++)
            {
                AbstractDataBlock childBlock = ((DataBlockMixed)dataBlock).blockArray[i];
                itemList.get(i).setData(childBlock);
            }
        }
        else if (dataBlock instanceof DataBlockTuple)
        {
            int currentIndex = 0;
            for (int i = 0; i < itemList.size(); i++)
            {
                AbstractDataComponent nextComponent = itemList.get(i);
                AbstractDataBlock childBlock = ((AbstractDataBlock)dataBlock).copy();
                childBlock.atomCount = nextComponent.scalarCount;
                childBlock.startIndex += currentIndex;
                currentIndex += childBlock.atomCount;
                nextComponent.setData(childBlock);
            }
        }
    	else // case of big primitive array
    	{
    		int currentIndex = 0;
    		for (int i = 0; i < itemList.size(); i++)
    		{
    			AbstractDataComponent nextComponent = itemList.get(i);
    			AbstractDataBlock childBlock = ((AbstractDataBlock)dataBlock).copy();
    			childBlock.atomCount = nextComponent.scalarCount;
    			childBlock.startIndex += currentIndex;
    			currentIndex += childBlock.atomCount;
    			nextComponent.setData(childBlock);
    		}
    	}
    }
    
    
    @Override
    public void clearData()
    {
        this.dataBlock = null;
        for (int i = 0; i < itemList.size(); i++)
        {
            AbstractDataComponent nextComponent = itemList.get(i);
            nextComponent.clearData();
        }
    }
    
    
    @Override
    public void validateData() throws CDMException
    {
    	itemList.get(selectedItem).validateData();
    }
    
    
    /**
     * Create object adapted to carry data for this container
     * TODO could save the calculated structure and do a shallow copy for the nexts...
     * Saved copy would be discarded every time the structure changes
     */
    @Override
    protected AbstractDataBlock createDataBlock() 
    {
    	DataType currentType = DataType.OTHER;
        DataType previousType = DataType.OTHER;
        AbstractDataBlock newBlock = null;
        AbstractDataBlock nextBlock = null;
        DataBlockMixed mixedBlock;
        AbstractDataComponent nextComponent;
        boolean sameType = true;
        boolean allScalars = true;
        int totalSize = 0;        
        
    	// create a mixed block with all children block
    	int childNumber = itemList.size();
    	mixedBlock = new DataBlockMixed(childNumber);        
        
        for (int i=0; i<childNumber; i++)
        {
        	nextComponent = itemList.get(i);
            nextBlock = nextComponent.createDataBlock();
        	currentType = nextBlock.getDataType();
        	totalSize += nextBlock.atomCount;       	
        	mixedBlock.blockArray[i] = nextBlock;
        	
        	/*if ((currentType == DataType.MIXED) || (i != 0 && currentType != previousType))
        		sameType = false;
        	
        	else if ((nextComponent instanceof DataArray) && ((DataArray)nextComponent).variableSize)
        		sameType = false;*/
            
            if (nextComponent instanceof DataArray || nextBlock instanceof DataBlockMixed)
            {
                allScalars = false;
            }
            else
            {
                if (i != 0 && (currentType != previousType))
                    sameType = false;
            }
        	
        	previousType = currentType;
        }
        
        // if everything was of same type, create a big shared primitive block
        if (allScalars && sameType)
        {
        	newBlock = nextBlock.copy();
        	newBlock.resize(totalSize);
        }
        
        // otherwise if only scalars, use DataBlockTuple or PrimitiveBlock
        else if (allScalars)
        {
            DataBlockTuple tupleBlock = new DataBlockTuple(totalSize);
            
            int currentIndex = 0;
            for (int i=0; i<childNumber; i++)
            {
                AbstractDataBlock childBlock = mixedBlock.blockArray[i];
                
                if (childBlock instanceof DataBlockTuple)
                {
                    for (int j=0; j<childBlock.atomCount; j++)
                    {
                        tupleBlock.blockArray[currentIndex] = ((DataBlockTuple)childBlock).blockArray[j];
                        currentIndex++;
                    }
                }
                else
                {
                    for (int j=0; j<childBlock.atomCount; j++)
                    {
                        AbstractDataBlock block = childBlock.copy();
                        block.resize(1);
                        tupleBlock.blockArray[currentIndex] = block;
                        currentIndex++;
                    }
                }
            }
            
            tupleBlock.atomCount = totalSize;
            newBlock = tupleBlock;
        }
        
        // otherwise use the mixed block
        else
        {
        	newBlock = mixedBlock;
        	newBlock.atomCount = totalSize;
        }
        
        newBlock.startIndex = 0;
        scalarCount = totalSize;
        return newBlock;
    }
    
    
    public void combineDataBlocks()
    {
        int groupSize = itemList.size();
        DataBlockMixed newBlock = new DataBlockMixed(groupSize);
        
        for (int i=0; i<groupSize; i++)
        {
            AbstractDataComponent childComponent = itemList.get(i);
            
            if (childComponent instanceof DataChoice && childComponent.dataBlock == null)
                ((DataChoice)childComponent).combineDataBlocks();

            newBlock.blockArray[i] = childComponent.dataBlock;
            newBlock.atomCount += childComponent.dataBlock.atomCount;
        }
        
        this.dataBlock = newBlock;
    }


    @Override
    public int getComponentCount()
    {
        return itemList.size();
    }


    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append("DataChoice: " + name + "\n");

        for (int i=0; i<itemList.size(); i++)
        {
        	text.append(indent + "  ");
        	text.append(getComponent(i).toString(indent + "  "));
        }

        return text.toString();
    }
}
