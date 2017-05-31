/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.swe.v20;

import java.util.List;
import net.opengis.HasCopy;


/**
 * <p>
 * Base interface for all SWE Common data components.
 * It provides an API auto-generated from XML schemas as well as a generic
 * API that useful to navigate a component tree in a more generic manner.
 * </p>
 *
 * @author Alex Robin
 * @since Nov 10, 2014
 */
public interface DataComponent extends AbstractSWEIdentifiable, HasCopy
{
        
    /**
     * Gets the updatable property
     * @return true if component can be updated dynamically
     */
    public boolean getUpdatable();
    
    
    /**
     * Checks if updatable is set
     * @return true if the updatable attribute is set
     */
    public boolean isSetUpdatable();
    
    
    /**
     * Sets the updatable property
     * @param updatable
     */
    public void setUpdatable(boolean updatable);
    
    
    /**
     * Unsets the updatable property
     */
    public void unSetUpdatable();
    
    
    /**
     * Gets the optional property
     * @return true if component value is optional in the stream
     */
    public boolean getOptional();
    
    
    /**
     * Checks if optional is set
     * @return true if the optional attribute is set
     */
    public boolean isSetOptional();
    
    
    /**
     * Sets the optional property
     * @param optional 
     */
    public void setOptional(boolean optional);
    
    
    /**
     * Unsets the optional property
     */
    public void unSetOptional();
    
    
    /**
     * Gets the definition property
     * @return definition URI
     */
    public String getDefinition();
    
    
    /**
     * Checks if definition is set
     * @return true if the definition attribute is set
     */
    public boolean isSetDefinition();
    
    
    /**
     * Sets the definition property
     * @param definition definition URI
     */
    public void setDefinition(String definition);
    
    
    /*---------------------*/
    /* Generic API methods */
    /*---------------------*/
    
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
     * Remove the sub-component at the specified index
     * @param index
     * @return the component that was just removed
     */
    public DataComponent removeComponent(int index);
    
    
    /**
     * Remove sub-component with the specified name
     * @param name
     * @return the component that was just removed
     */
    public DataComponent removeComponent(String name);
    
    
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
     * @return true if a datablock is associated to this component
     */
    public boolean hasData();
    
    
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
     * Validates datablock against constraints if any.
     * No exceptions are thrown, rather they are appended to the provided list.
     * @param errorList list to which validation exceptions will be appended
     */
    public void validateData(List<ValidationException> errorList);
    
    
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
    @Override
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
     * Renew the datablock of this component.
     * This method is faster than recreating a datablock from scratch with createDataBlock().
     */
    public void renewDataBlock();
    
    
    
    public void accept(DataComponentVisitor visitor);
}
