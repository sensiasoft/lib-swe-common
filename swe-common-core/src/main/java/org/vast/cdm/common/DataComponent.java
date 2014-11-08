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

package org.vast.cdm.common;

import java.util.List;
import net.opengis.HasCopy;


/**
 * <p>
 * Implementation of this class should store information relative
 * to the component structure of the data.
 * It should give access to information such as component names, units,
 * definition, etc.. in a hierarchical manner.
 * </p>
 *
 * <p>Copyright (c) 2014</p>
 * @author Alexandre Robin
 * @since Nov 30, 2005
 * @version 1.0
 */
public interface DataComponent extends HasCopy, Cloneable
{
    
    /**
     * Returns number of sub-components in this component
     * @return number of direct sub-components
     */
    public int getComponentCount();


    /**
     * Get the sub-component located at the specified index
     * @param index index of component to lookup
     * @return child component or null if none exists at the specified index
     */
    public DataComponent getComponent(int index);


	/**
     * Get the sub-component registered with the specified name
     * @param name name of component to lookup
     * @return child component or null if none exists with the specified name
     */
    public DataComponent getComponent(String name);
    
    
    /**
     * Get the index of the sub-component registered with this name
     * @param name name of component to lookup
     * @return index of child component or -1 if none exists with the specified name
     */
    public int getComponentIndex(String name);
	
	
    /**
     * Add a sub-component and registers a name for it
     * @param name name of component to use
     * @param component new sub-component to append to this component
     */
    public void addComponent(String name, DataComponent component);
	
	
	/**
     * @return parent component of this component
     */
    public DataComponent getParent();
	
	
    /**
     * @return name of sub-component
     */
    public String getName();
    
    
    /**
     * Sets the name of this component
     * @param name
     */
    public void setName(String name);
	
	
	/**
     * Get the datablock associated to this component 
     * @return datablock object or null if none has been generated yet
     */
    public DataBlock getData();
	
	
	/**
     * Assign a new datablock to this component.
     * This will recursively assign the right datablocks to sub-components recursively.
     * @param dataBlock
     */
    public void setData(DataBlock dataBlock);
    
    
	/**
     * Clear the datablock used by this component.
     * This will also clear data from all the sub-components recursively.
     */
    public void clearData();
    
    
    /**
     * Validates dataBlock against constraints if any.
     * No exceptions are thrown, rather they are appended to the provided list.
     * @param errorList list to which validation exceptions will be appended
     */
    public void validateData(List<CDMException> errorList);
    
    
    /**
     * Recursively checks if constraints are specified in this component or 
     * any of its sub-components
     * @return true if at least one constraint is found, false otherwise
     */
    public boolean hasConstraints();
    
    
    /**
     * Return a structural copy of this component
     * The copy is done recursively, but only the structure is copied (not the data)
     * @return copy of this component, including sub-components
     */
    public DataComponent copy();
    
    
    /**
     * Get a full recursive copy of this component. Both structure and data are copied
     * @return clone of this component, including sub-components
     */
    public DataComponent clone();
    
    
    /**
     * Create a new datablock for holding data of this component
     * @return new datablock object
     */
    public DataBlock createDataBlock();
    
    
    /**
     * Create and assign a new datablock structure to this component.
     * This will also assign the right datablocks to sub-components recursively.
     */
    public void assignNewDataBlock();
    
    
    /**
     * Renew the datablock of this component by cloning it.
     * This method is faster than recreating a datablock from scratch with createDataBlock().
     */
    public void renewDataBlock();
}
