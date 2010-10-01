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
import java.util.ListIterator;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataBlock;
import org.vast.cdm.common.DataComponent;


/**
 * <p><b>Title:</b><br/>
 * Data List
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Growable List of identical DataContainers.
 * Each new cluster of data must comply to the DataList component
 * structure. Data clusters are stored using a DataBlockList so
 * that data can be added and removed in real time.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @version 1.0
 */
public class DataList extends AbstractDataComponent
{
    private static final long serialVersionUID = 3525149069812989611L;
    protected ListIterator<AbstractDataBlock> blockIterator;
    protected AbstractDataComponent component = null;
    protected int size = 0;
        

    public DataList()
    {
        this.dataBlock = new DataBlockList();
    }
    
    
    public DataList(String name)
    {
        this();
        this.setName(name);
    }
    
    
    @Override
    public DataList copy()
    {
    	// TODO Implement DataList copy()
    	return null;
    }
    
    
    @Override
    protected void updateStartIndex(int startIndex)
    {        
    }
    
    
    @Override
    protected void updateAtomCount(int childOffsetCount)
    {
    }
    
    
    public void resetIterator()
    {
        blockIterator = ((DataBlockList)dataBlock).blockIterator();
    }


    @Override
    public void addComponent(DataComponent component)
    {
        if (this.component == null)
        {
            String componentName = component.getName();
            if (componentName != null)
                this.names.put(componentName, 0);
            
            AbstractDataComponent container = (AbstractDataComponent)component;
    		this.component = container;
        	this.component.parent = this;
        }
    }
    
    
    @Override
    public AbstractDataComponent getComponent(int index)
    {
        checkIndex(index);
        component.setData(((DataBlockList)dataBlock).blockList.get(index));        
        return component;
    }
    
    
    public DataComponent getListComponent()
    {
    	return component;
    }
    
    
    public AbstractDataComponent nextComponent()
    {
        component.setData(blockIterator.next());        
        return component;
    }
    
    
    public boolean hasNext()
    {
        return blockIterator.hasNext();
    }
    
    
    @Override
    public void setData(DataBlock dataBlock)
    {
    	this.dataBlock = (DataBlockList)dataBlock;
    }
    
    
    @Override
    public void clearData()
    {
    	this.dataBlock = new DataBlockList();
        component.clearData();
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
    
    
    public void addData(DataBlock dataBlock)
    {
    	((DataBlockList)this.dataBlock).add((AbstractDataBlock)dataBlock);
    	this.size++;
    }
    
    
    @Override
    public AbstractDataBlock createDataBlock()
    {
    	this.dataBlock = new DataBlockList();
    	return this.dataBlock;
    }


    /**
     * Check that the integer index given is in range: 0 to size of array - 1 
     * @param index int
     * @throws DataException
     */
    protected void checkIndex(int index)
    {
        // error if index is out of range
        if ((index >= size) || (index < 0))
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds");
    }
    
    
    @Override
    public int getComponentCount()
    {
    	return this.size;
    }


    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();

        text.append("DataList[" + size + "] of:\n");
        text.append(indent + "  ");
        text.append(getComponent(0).toString(indent + "  "));

        return text.toString();
    }
    
    
    @Override
    public void removeAllComponents()
    {
        this.clearData();
    	this.dataBlock = new DataBlockList();
    	this.component.parent = null;
        this.component = null;
        this.size = 0;
    }
    
    
    @Override
    public void removeComponent(int index)
    {
    	throw new UnsupportedOperationException();
    }
    
    
	@Override
	public boolean hasConstraints()
	{
		return component.hasConstraints();
	}
}
