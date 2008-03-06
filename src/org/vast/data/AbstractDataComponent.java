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
 * AbstractDataComponent
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Abstract DataComponent base
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @version 1.0
 */
public abstract class AbstractDataComponent implements DataComponent
{
    protected Hashtable<String, Object> properties = null;
	protected Hashtable<String, Integer> names = new Hashtable<String, Integer>(0, 1.0f);
	protected AbstractDataBlock dataBlock;
	protected AbstractDataComponent parent = null;
	protected int scalarCount = 0;
	protected String name;

	
	/**
	 * Return a structural copy of this container
     * Only the structure is copied (not the data)
	 * @return
	 */
	public abstract AbstractDataComponent copy();
    
    
    /**
     * Return a full copy of this container
     * Both structure and data are copied
     * @return
     */
    public AbstractDataComponent clone()
    {
        AbstractDataComponent newComponent = this.copy();
        if (newComponent.dataBlock != null)
            newComponent.dataBlock = this.dataBlock.clone();
        return newComponent;
    }
    
    
    /**
     * Update datablock start index (for array support)
     * Needed when data is selected by DataArray getComponent(int)
     * and the DataArray is using a parallel or primitive DataBlock
     * @param startIndex
     */
    protected abstract void updateStartIndex(int startIndex);
    
    
    /**
     * Update datablock atomCount (for resizable array support)
     * Needed to update atom count info in all parents of a
     * DataArray that has been resized!
     * @param startIndex
     */
    protected abstract void updateAtomCount(int childOffsetCount);

    
    /**
     * Return parent component
     * @return
     */
    public AbstractDataComponent getParent()
    {
        return parent;
    }

	
	/**
	 * Adds data to this container
	 * @param component DataContainer
	 * @throws DataException
	 */
    public abstract void addComponent(DataComponent component);	


	/**
	 * Adds data and register a name for it
	 * @param componentName String
	 * @param component DataContainer
	 * @throws DataException
	 */
	public void addComponent(String componentName, DataComponent component)
	{
		component.setName(componentName);        
        addComponent(component);
	}


	/**
	 * Returns data located at the specified index
	 * @param index int
	 * @throws DataException
	 * @return DataContainer
	 */
	public abstract AbstractDataComponent getComponent(int index);


	/**
	 * Returns data registered with the specified name
	 * @param componentName String
	 * @throws DataException
	 * @return DataContainer
	 */
	public AbstractDataComponent getComponent(String componentName)
	{
		int index = getComponentIndex(componentName);
		
		if (index == -1)
			return null;
		
		return getComponent(index);
	}


	/**
	 * Check if component with this name exists
	 * @param componentName
	 * @return
	 */
	public boolean existComponent(String componentName)
	{
		if (getComponentIndex(componentName) != -1)
			return true;
		else
			return false;
	}


	/**
	 * Removes data at the specified index
	 * @param index int
	 * @throws DataException
	 */
	public abstract void removeComponent(int index);


	/** @todo Make sure hashtable is cleaned when removing !! */

	/**
	 * Removes all data from this container
	 * @throws DataException
	 */
	public abstract void removeAllComponents();


	/**
	 * Create a new container's datablock
	 * @param value Object
	 */
	protected abstract AbstractDataBlock createDataBlock();
    
    
	/**
	 * Create and assign a new data block structure to this container
	 */
	public void assignNewDataBlock()
	{
		dataBlock = createDataBlock();
		setData(dataBlock);
	}
    
    
    /**
     * Renew the data block of this container by cloning it
     */
    public void renewDataBlock()
    {
        if (dataBlock != null)
        {
            dataBlock = dataBlock.renew();
            setData(dataBlock);
        }
        else
            assignNewDataBlock();
    }


	/**
	 * Get data block for this container 
	 * @return
	 */
	public DataBlock getData()
	{
		return dataBlock;
	}


	/**
	 * Assigns dataBlock to this container
	 * @param dataBlock
	 * @param startIndex
	 * @throws DataException
	 */
	public abstract void setData(DataBlock dataBlock);
    
    
    /**
     * Clears dataBlock from this container
     */
    public abstract void clearData();
    
    
    /**
     * Validates dataBlock against constraints if any
     */
    public abstract void validateData() throws CDMException;
	
	
	/**
	 * Returns number of elements in this container
	 * @return int
	 */
	public abstract int getComponentCount();


	/**
	 * Returns String representation of this DataContainer
	 * @param indent int
	 * @return String
	 */
	public abstract String toString(String indent);


	public String toString()
	{
		return this.toString("");
	}
	
	
	public String getName()
	{
		return this.name;
	}
	
	
	public void setName(String name)
	{
		this.name = name;
	}


	public Object getProperty(String propName)
	{
		if (properties == null)
			return null;
		else
			return properties.get(propName);
	}


	public void setProperty(String propName, Object propValue)
	{
		if (properties == null)
			properties = new Hashtable<String, Object>(5, 1.0f);
		
		properties.put(propName, propValue);
	}


	/**
	 * Gives the index of the component registered with this name
	 * @param componentName String
	 * @throws DataException
	 * @return int
	 */
	public int getComponentIndex(String componentName)
	{
		Integer componentIndex = names.get(componentName);

		if (componentIndex == null)
			return -1;

		return componentIndex.intValue();
	}
}
