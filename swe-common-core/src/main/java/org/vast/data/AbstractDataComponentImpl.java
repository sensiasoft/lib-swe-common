/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import java.io.Serializable;
import java.util.*;
import net.opengis.swe.v20.BinaryMember;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataBlock;
import org.vast.cdm.common.DataComponent;


/**
 * <p>
 * Abstract DataComponent base
 * </p>
 *
 * <p>Copyright (c) 2014</p>
 * @author Alexandre Robin
 * @version 1.0
 */
@SuppressWarnings("serial")
public abstract class AbstractDataComponentImpl extends AbstractSWEIdentifiableImpl implements DataComponent, Serializable
{
    public static int MAX_ARRAY_ERRORS = 10;
    protected final static String INDENT = "  ";
    
    protected Boolean updatable;
    protected Boolean optional;
    protected String definition;
    
    protected AbstractDataComponentImpl parent = null;
    protected AbstractDataBlock dataBlock;
	protected int scalarCount = -1;
	protected BinaryMember encodingInfo;
	
	private String name; // can we get rid of this? it is also in property object but it's convenient to have it here    

	
	public AbstractDataComponentImpl()
	{	    
	}
	
	
	@Override
    public abstract AbstractDataComponentImpl copy();
    
    
	protected void copyTo(AbstractDataComponentImpl other)
	{
	    super.copyTo(other);
	    other.updatable = updatable;
        other.optional = optional;
        other.definition = definition;
	    other.name = name;
	    other.parent = null;
	    other.scalarCount = scalarCount;
	}
	
	
	@Override
    public AbstractDataComponentImpl clone()
    {
        AbstractDataComponentImpl newComponent = this.copy();
        if (this.dataBlock != null)
            newComponent.dataBlock = this.dataBlock.clone();
        return newComponent;
    }

    
    @Override
    public final DataComponent getParent()
    {
        return parent;
    }


    protected final void setParent(AbstractDataComponentImpl parent)
    {
        this.parent = parent;
    }


    @Override
    public abstract void addComponent(String name, DataComponent component);


    @Override
    public abstract DataComponent getComponent(int index);


	@Override
    public abstract int getComponentIndex(String name);
    
    
	@Override
    public abstract DataComponent getComponent(String name);
	
	
    @Override
    public abstract int getComponentCount();


	@Override
    public abstract AbstractDataBlock createDataBlock();
    
    
	@Override
    public void assignNewDataBlock()
	{
		dataBlock = createDataBlock();
		setData(dataBlock);
	}
    
    
	@Override
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


    @Override
    public DataBlock getData()
	{
		return dataBlock;
	}


	@Override
    public abstract void setData(DataBlock dataBlock);
    
    
    @Override
	public abstract void clearData();
    
    
    @Override
    public abstract void validateData(List<CDMException> errorList);
	
    
    @Override
    public abstract boolean hasConstraints();
	
	
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
	 * Returns String representation of this DataContainer
	 * @param indent int
	 * @return String
	 */
	public abstract String toString(String indent);


	public String toString()
	{
		return this.toString("");
	}
	
	
	/**
     * Gets the updatable property
     */
    @Override
    public boolean getUpdatable()
    {
        return updatable;
    }
    
    
    /**
     * Checks if updatable is set
     */
    @Override
    public boolean isSetUpdatable()
    {
        return (updatable != null);
    }
    
    
    /**
     * Sets the updatable property
     */
    @Override
    public void setUpdatable(boolean updatable)
    {
        this.updatable = updatable;
    }
    
    
    /**
     * Unsets the updatable property
     */
    @Override
    public void unSetUpdatable()
    {
        this.updatable = null;
    }
    
    
    /**
     * Gets the optional property
     */
    @Override
    public boolean getOptional()
    {
        return optional;
    }
    
    
    /**
     * Checks if optional is set
     */
    @Override
    public boolean isSetOptional()
    {
        return (optional != null);
    }
    
    
    /**
     * Sets the optional property
     */
    @Override
    public void setOptional(boolean optional)
    {
        this.optional = optional;
    }
    
    
    /**
     * Unsets the optional property
     */
    @Override
    public void unSetOptional()
    {
        this.optional = null;
    }
    
    
    /**
     * Gets the definition property
     */
    @Override
    public String getDefinition()
    {
        return definition;
    }
    
    
    /**
     * Checks if definition is set
     */
    @Override
    public boolean isSetDefinition()
    {
        return (definition != null);
    }
    
    
    /**
     * Sets the definition property
     */
    @Override
    public void setDefinition(String definition)
    {
        this.definition = definition;
    }
    
    
	@Override
    public String getName()
    {
        return name;
    }


	@Override
    public void setName(String name)
    {
        this.name = name;
    }


	public BinaryMember getEncodingInfo()
    {
        return encodingInfo;
    }


    public void setEncodingInfo(BinaryMember encodingInfo)
    {
        this.encodingInfo = encodingInfo;
    }
}
