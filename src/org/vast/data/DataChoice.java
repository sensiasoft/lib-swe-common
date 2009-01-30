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


/**
 * <p><b>Title:</b><br/>
 * DataChoice Component
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Exclusive list of DataComponents (Choice)
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @version 1.0
 */
public class DataChoice extends AbstractDataComponent
{
	protected static int UNSELECTED = -1;
	protected static String UNSELECTED_ERROR = "No item was selected in DataChoice ";
	protected List<AbstractDataComponent> itemList;
	protected int selected = -1;
    

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
    	newChoice.selected = this.selected;
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
        // TODO don't think there is anything to do here
    }
    
    
    @Override
    protected void updateAtomCount(int childAtomCountDiff)
    {
        if (dataBlock != null)
            dataBlock.atomCount += childAtomCountDiff;
        
        if (parent != null)
            parent.updateAtomCount(childAtomCountDiff);
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
        // HACK makes sure scalar count was properly computed
        if (scalarCount == 0)
            this.assignNewDataBlock();
        
        // must always be a datablock mixed!
    	DataBlockMixed mixedBlock = (DataBlockMixed)dataBlock;
    	this.dataBlock = mixedBlock;

		// first value = index of selected component
    	int index = mixedBlock.blockArray[0].getIntValue();    	
    	if (index == UNSELECTED)
    		return;
    	
    	checkIndex(index);
    	this.selected = index;
    	
    	// also assign block to selected child
    	itemList.get(index).setData(mixedBlock.blockArray[1]);
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
    public void validateData(List<CDMException> errorList)
    {
    	itemList.get(selected).validateData(errorList);
    }
    
    
    @Override
    protected AbstractDataBlock createDataBlock()
    {
    	DataBlockMixed newBlock = new DataBlockMixed(2);
    	newBlock.blockArray[0] = new DataBlockInt(1);

    	// create data blocks for all children
    	int childNumber = itemList.size();
        for (int i=0; i<childNumber; i++)
        {
        	AbstractDataComponent nextComponent = itemList.get(i);
            nextComponent.assignNewDataBlock();
        }
        
        // if one item is selected, set data
    	if (selected >= 0)
    	{
    		newBlock.blockArray[0].setIntValue(selected);
    		newBlock.blockArray[1] = (AbstractDataBlock)itemList.get(selected).getData();
    		newBlock.atomCount = newBlock.blockArray[1].atomCount + 1;
    	}
    	else
    	{
    		newBlock.blockArray[0].setIntValue(UNSELECTED);
    		newBlock.atomCount = 1;
    	}
    	
    	this.scalarCount = newBlock.atomCount;
        return newBlock;
    }
    
    
    /**
     * Check that the integer index given is in range: 0 to item list size
     * @param index int
     * @throws DataException
     */
    protected void checkIndex(int index)
    {
        // error if index is out of range
        if ((index >= itemList.size()) || (index < 0))
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds");
    }


    @Override
    public int getComponentCount()
    {
        return itemList.size();
    }
    
    
    public int getSelected()
	{
		return selected;
	}
    
    
    public DataComponent getSelectedComponent()
    {
    	if (selected < 0)
    		return null;
    	
    	return getComponent(selected);
    }
    

	public void setSelected(int index)
	{
		checkIndex(index);
		this.selected = index;
		
		if (this.dataBlock != null)
		{
			int prevAtomCount = dataBlock.atomCount;
				
			((DataBlockMixed)dataBlock).blockArray[0].setIntValue(index);
			AbstractDataBlock childData = (AbstractDataBlock)itemList.get(selected).getData();
			((DataBlockMixed)dataBlock).blockArray[1] = childData;
			dataBlock.atomCount = childData.atomCount + 1;
			
			if (parent != null)
				parent.updateAtomCount(dataBlock.atomCount - prevAtomCount);
		}
	}
	
	
	public void unselect()
	{
		this.selected = UNSELECTED;
		if (this.dataBlock != null)
		{
			((DataBlockMixed)dataBlock).blockArray[0].setIntValue(UNSELECTED);
			((DataBlockMixed)dataBlock).blockArray[1] = null;
			dataBlock.atomCount = 1;
		}
	}
	
	
	public void setSelectedComponent(String name)
	{
		int index = getComponentIndex(name);
		if (index < 0)
			throw new IllegalStateException("Invalid component: " + name);
		
		setSelected(index);
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
    
    
	@Override
	public boolean hasConstraints()
	{
		return getSelectedComponent().hasConstraints();
	}
}
