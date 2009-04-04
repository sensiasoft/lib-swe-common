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

import java.util.List;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataBlock;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataType;


/**
 * <p><b>Title:</b><br/> DataArray</p>
 *
 * <p><b>Description:</b><br/>
 * Array of identical DataContainers. Can be of variable size.
 * In the case of a variable size array, size is actually given
 * by another component: sizeData which should be a DataValue
 * carrying an Integer value.
 * There are two cases of variable size component:
 *  - The component is explicitely listed in the component tree
 *    (in this case, the count component has a parent) 
 *  - The component is implicitely given before the array data
 *    (in this case, the count component has no parent)
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @version 1.0
 */
public class DataArray extends AbstractDataComponent
{
    public final static String ARRAY_SIZE_FIELD = "ArraySize";
	protected final static String errorBlockMixed = "Error: DataArrays should never contain a DataBlockMixed";
    protected AbstractDataComponent component = null;
    protected DataValue sizeComponent = null;
    protected String sizeDataName = null;
    protected int stepsToSizeData = -1;
    protected int currentSize;
    protected boolean variableSize;
    protected boolean implicitSize;
    
    
    public DataArray()
    {
    }  
    
    
    public DataArray(int arraySize)
    {
        this.sizeComponent = new DataValue(ARRAY_SIZE_FIELD, DataType.INT);
        updateSizeComponent(arraySize);
        this.variableSize = false;
    }
    
    
    public DataArray(DataValue sizeComponent, boolean variableSize)
    {
        this.sizeComponent = sizeComponent;
        this.variableSize = variableSize;
        
        // if sizeComponent has not parent -> implicit size
        if (sizeComponent.getParent() == null)
        	implicitSize = true;
        
        // initialize size to 1 if variable size
        if (variableSize)
        	updateSizeComponent(1);
        else
            currentSize = sizeComponent.getData().getIntValue();
    }


    @Override
    public DataArray copy()
    {
    	DataArray newArray = new DataArray();
    	newArray.name = this.name;
    	newArray.properties = this.properties;
    	newArray.variableSize = this.variableSize;
    	newArray.implicitSize = this.implicitSize;
    	newArray.currentSize = this.currentSize;
        newArray.addComponent(this.component.copy());
        
        // case of size data as a separate component
        if (variableSize && !implicitSize)
        {
       		// find out where size component is (name + steps to parent)
        	// cannot just copy cause we don't know what the new component
        	// will be until we reconstruct the whole component tree!
            int step = 0;
            AbstractDataComponent dataComponent = this;
            while (dataComponent.getComponent(sizeComponent.name) != sizeComponent)
            {
                dataComponent = dataComponent.parent;
                step++;
            }
            
            newArray.sizeDataName = sizeComponent.name;
            newArray.stepsToSizeData = step;
        }
        
        // case of fixed size or implicit variable size
        // just clone the size component
        else
        	newArray.setSizeComponent((DataValue)sizeComponent.clone());
        
    	return newArray;
    }
    
    
    @Override
    protected void updateStartIndex(int startIndex)
    {
        dataBlock.startIndex = startIndex;
        
        if (dataBlock instanceof DataBlockMixed)
        {
            throw new IllegalStateException(errorBlockMixed);
        }
        else if (dataBlock instanceof DataBlockParallel)
        {
            component.updateStartIndex(startIndex);
        }
        else // case of primitive array
        {
            component.updateStartIndex(startIndex);
        }
    }
    
    
    @Override
    protected void updateAtomCount(int childAtomCountDiff)
    {
    	int arraySize = getComponentCount();
    	int atomCountDiff = 0;
        
        if (dataBlock != null)
        {
            // if using DataBlockList, each array element is
        	// independant and can be of variable length
        	if (dataBlock instanceof DataBlockList)
            {
            	atomCountDiff = childAtomCountDiff;
            	dataBlock.atomCount += atomCountDiff;
            }
            else
            {        	
            	atomCountDiff = childAtomCountDiff * arraySize;
            	AbstractDataBlock childBlock = component.dataBlock;
	            childBlock.resize(childBlock.atomCount * arraySize);
	            dataBlock.setUnderlyingObject(childBlock.getUnderlyingObject());
	            dataBlock.atomCount = childBlock.atomCount;
	            setData(dataBlock);
            }
        }
        
        if (parent != null)
            parent.updateAtomCount(atomCountDiff);
    }
    
    
    @Override
    public void addComponent(DataComponent component)
    {
        String componentName = component.getName();
        if (componentName != null)
            this.names.put(componentName, 0);
        
    	this.component = (AbstractDataComponent)component;
        ((AbstractDataComponent)component).parent = this;
    }


    @Override
    public AbstractDataComponent getComponent(int index)
    {
        checkIndex(index);
        
        if (dataBlock != null)
        {
            int startIndex = dataBlock.startIndex;
            
            if (dataBlock instanceof DataBlockMixed)
            {
                throw new IllegalStateException(errorBlockMixed);
            }            
            else if (dataBlock instanceof DataBlockParallel)
            {
                if (component instanceof DataArray)
                    startIndex += index * ((DataArray)component).getComponentCount();
                else
                    startIndex += index;
               // System.err.println("DataArr: startIndx = " + startIndex);
            }
            else if (dataBlock instanceof DataBlockList)
            {
                startIndex = 0;
                component.setData(((DataBlockList)dataBlock).get(index));
            }
            else // primitive block
            {
                startIndex += index * component.scalarCount;
            }
            
            // update child start index
            component.updateStartIndex(startIndex);
        }
        
        return component;
    }
    
    
    public DataComponent getArrayComponent()
    {
    	return component;
    }
    
    
    @Override
    public void setData(DataBlock dataBlock)
    {
    	// HACK makes sure scalar count was properly computed
        if (scalarCount == 0)
            this.assignNewDataBlock();
        
        this.dataBlock = (AbstractDataBlock)dataBlock;
    	
    	// update size component if variable size
        if (variableSize && implicitSize)
        {
        	int newSize = 0;
        	
        	if (dataBlock instanceof DataBlockList)
        	{
        		newSize = ((DataBlockList)dataBlock).getListSize();
        	}
        	else
        	{
	        	// TODO should infer array size differently (keep size int in data block??)
	        	// TODO potential bug here with nested variable size arrays
	        	newSize = dataBlock.getAtomCount() / component.scalarCount;	        	
        	}
        	
        	updateSizeComponent(newSize);
        }
        
		// also assign dataBlock to child
    	AbstractDataBlock childBlock = ((AbstractDataBlock)dataBlock).copy();
		childBlock.atomCount = component.scalarCount;
		component.setData(childBlock);
    }
    
    
    @Override
    public void clearData()
    {
        this.dataBlock = null;
        component.clearData();
        if (variableSize)
        	updateSizeComponent(1);
    }
    
    
    @Override
    public void validateData(List<CDMException> errorList)
    {
    	// do only if constraints are specified on descendants
    	if (hasConstraints())
    	{
    		int numErrors = errorList.size();
    		
    		for (int i = 0; i < getComponentCount(); i++)
    		{
    			getComponent(i).validateData(errorList);
    			
    			// max N errors generated!
    			if (errorList.size() > numErrors + MAX_ARRAY_ERRORS)
    				return;
    		}
    	}
    }

    
    /**
     * Create the right data block to carry this array data
     * It can be either a scalar array (DataBlockDouble, etc...)
     * or a group of mixed types parallel arrays (DataBlockMixed)
     */
    @Override
    protected AbstractDataBlock createDataBlock()
    {
    	AbstractDataBlock childBlock = component.createDataBlock();
    	AbstractDataBlock newBlock = null;
    	int arraySize = getComponentCount();
        int newSize = 0;
        
    	if (arraySize > 0)
    	{
	    	// if child is parallel block, create bigger parallel block
	        if (childBlock instanceof DataBlockParallel)
	        {
	        	newBlock = childBlock.copy();
                newSize = childBlock.atomCount * arraySize;
	        }
            
            // if child is tuple block, create parallel block
            else if (childBlock instanceof DataBlockTuple)
            {
                DataBlockParallel parallelBlock = new DataBlockParallel();
                parallelBlock.blockArray = ((DataBlockTuple)childBlock).blockArray;
                newSize = childBlock.atomCount * arraySize;
                newBlock = parallelBlock;
            }
            
            // if child is mixed block, create list block
            else if (childBlock instanceof DataBlockMixed)
            {
                DataBlockList blockList = new DataBlockList();
                blockList.add(childBlock.copy());
                newBlock = blockList;
                newSize = arraySize;
            }
	        
	        // if child is already a primitive block, create bigger primitive block
	        else
	        {
	        	newBlock = childBlock.copy();
                newSize = childBlock.atomCount * arraySize;
	        }
	        
	        newBlock.resize(newSize);
	        scalarCount = newBlock.atomCount;
    	}
        
        return newBlock;
    }


    /**
     * Check that the integer index given is in range: 0 to size of array - 1
     * @param index int
     * @throws DataException
     */
    protected void checkIndex(int index)
    {
        // error if index is out of range
        if ((index >= getComponentCount()) || (index < 0))
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds");
    }
    
    
    /**
     * Do everyting that is necessary to properly resize data block 
     * @param oldArraySize
     * @param newArraySize
     */
    protected void resizeDataBlock(int oldArraySize, int newArraySize)
    {
    	if (dataBlock != null)
        {
            if (dataBlock instanceof DataBlockList)
            {
            	dataBlock.resize(newArraySize);
            }
            else
            {
            	this.scalarCount = component.scalarCount * newArraySize;
            			
            	dataBlock.resize(scalarCount);
            	
            	// reassign a copy of dataBlock to child
            	AbstractDataBlock childBlock = ((AbstractDataBlock)dataBlock).copy();
        		childBlock.atomCount = component.scalarCount;
        		component.setData(childBlock);
            }
        }
    	
    	this.currentSize = newArraySize;
    	
    	// update parent atom count
        if (parent != null)
            parent.updateAtomCount(component.scalarCount * (newArraySize - oldArraySize));
    }
    
    
    /**
     * Dynamically update size of a VARIABLE SIZE array
     * Note that sizeData must carry the right value at this time
     */
    public void updateSize()
    {
    	if (variableSize)
    	{
    		int newSize = 0;
    		int oldSize = this.currentSize;
    		
    		// stop here if parent also has variable size
            // in this case updateSize() will be called from parent anyway!!
            if (parent instanceof DataArray)
            {
                if (((DataArray)parent).isVariableSize())
                    return;
            }
            
            //  
	    	if (this.getSizeComponent() != null)
	    	{
	    		DataBlock data = sizeComponent.getData();
	    		if (data != null)
                {
	    			// continue only if sized has changed
	    			newSize = data.getIntValue();
                    if (newSize == oldSize)
                        return;
                }
	    	}
            
            // take care of variable size child array
	    	// before we resize everything
            if (component instanceof DataArray)
            {
                if (((DataArray)component).isVariableSize())
                    ((DataArray)component).updateSize();
            }
            
            // resize datablock
            resizeDataBlock(oldSize, newSize);
    	}
    }
    
    
    /**
     * Set the size of this VARIABLE SIZE array to a new value
     * Automatically updates the sizeData component value.
     * @param newSize
     */
    public void updateSize(int newSize)
    {
    	if (newSize == this.currentSize)
            return;
    	
    	if (newSize > 0)
        {
    		int oldSize = this.currentSize; // don't use getComponentCount() because sizeData may have changed already
    		updateSizeComponent(newSize);
    		
    		// resize underlying datablock
    		resizeDataBlock(oldSize, newSize);
        }
    }
    
    
    /**
     * Set the size of this array to a new FIXED value
     * @param newSize
     */
    public void setSize(int newSize)
    {
        if (newSize > 0)
        {
        	int oldSize = getComponentCount();
    		
    		// reset size data to fixed value
        	this.sizeComponent = new DataValue("ArraySize", DataType.INT);
        	updateSizeComponent(newSize);
        	
        	this.variableSize = false;
        	
        	// stop here if size is same as before!
        	if (newSize == oldSize)
                return;
        	
        	// resize underlying datablock
        	resizeDataBlock(oldSize, newSize);         
        }
    }
    
    
    /**
     * Simply update value in size data component w/o resizing datablock
     * @param newSize
     */
    public void updateSizeComponent(int newSize)
    {
    	// update value of size data
    	DataBlock data = sizeComponent.getData();
        if (data == null)
        {
        	sizeComponent.renewDataBlock();
        	data = sizeComponent.getData();
        }        
        data.setIntValue(newSize);
        
        // save size so that we can detect if size changes later
        // this avoids resizing arrays for nothing if size does not change!
        this.currentSize = newSize;
    }
    
    
    public boolean isVariableSize()
    {
        return this.variableSize;
    }
    

    @Override
    public int getComponentCount()
    {
    	DataBlock data = getSizeComponent().getData();
    	    	
    	if (data != null)
    	{
    		int arraySize = data.getIntValue();
    		if (arraySize > 0)
    			return arraySize;
    	}
    	
    	return 1;
    }
    

    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append("DataArray[");
        if (variableSize)
        	text.append("?=");
        text.append(getComponentCount());
        text.append("]: " + name + "\n");
        text.append(indent + "  ");
        text.append(component.toString(indent + "  "));

        return text.toString();
    }
    
    
    @Override
    public void removeAllComponents()
    {
        this.component = null;
        this.dataBlock = null;
    }
    
    
    @Override
    public void removeComponent(int index)
    {
        removeAllComponents();
    }


    public DataValue getSizeComponent()
    {
        if (sizeComponent != null)
            return sizeComponent;
        
        // if DataArray was copied, try to find sizeData from parent hierarchy
        AbstractDataComponent dataComponent = this;
        for (int i=0; i<stepsToSizeData; i++)
            dataComponent = dataComponent.parent;
        sizeComponent = (DataValue)dataComponent.getComponent(sizeDataName);
        
        // initialize size to 1 if variable size
        if (variableSize)
        	updateSizeComponent(1);
        
        return sizeComponent;
    }


    public void setSizeComponent(DataValue sizeComponent)
    {
        this.sizeComponent = sizeComponent;
    }
    
    
	@Override
	public boolean hasConstraints()
	{
		return component.hasConstraints();
	}
}
