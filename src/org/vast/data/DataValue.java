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
 * <p><b>Title:</b><br/>
 * Atomic Data Value
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Atomic (no children) DataContainer usually containing a scalar value 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @version 1.0
 */
public class DataValue extends AbstractDataComponent
{
    private static final long serialVersionUID = -7411819306997320628L;
    protected DataType dataType;
    protected ConstraintList constraints;
    
    
    public DataValue()
    {        
    }
    
    
    public DataValue(String name)
    {
    	this.scalarCount = 1;
    	this.dataType = DataType.OTHER;
        this.setName(name);
    }
    
    
    public DataValue(DataType type)
    {
        this.scalarCount = 1;
        this.dataType = type;
        //this.assignNewDataBlock();
    }
    
    public DataValue(String name, DataType type)
    {
        this(type);
        this.setName(name);
    }
    
    
    @Override
    public DataValue copy()
    {
    	DataValue newVal = new DataValue(name, dataType);
    	newVal.properties = this.properties;
    	newVal.constraints = this.constraints;
    	return newVal;
    }
    
    
    @Override
    protected void updateStartIndex(int startIndex)
    {
        dataBlock.startIndex = startIndex;
    }
        
    
    @Override
    public void setData(DataBlock dataBlock)
    {
    	if (dataBlock instanceof DataBlockTuple)
    	{
    		int blockIndex = ((DataBlockTuple)dataBlock).startIndex;
    		this.dataBlock = ((DataBlockTuple)dataBlock).blockArray[blockIndex];
    	}
    	else
    		this.dataBlock = (AbstractDataBlock)dataBlock;
    }
    
    
    @Override
    public void clearData()
    {
        this.dataBlock = null;
    }
    
    
    @Override
    public void validateData(List<CDMException> errorList)
    {
    	if (constraints != null)
    	{
    		if (!constraints.validate(this.dataBlock))
    		{
    			errorList.add(new CDMException(name, "Value '" + dataBlock.getStringValue() +
									   		   "' is not valid for component '" +
									           name + "': " + constraints.getAssertionMessage()));
    		}
    	}
    }
    
    
    @Override
    public AbstractDataBlock createDataBlock()
    {
    	switch (dataType)
        {
        	case BOOLEAN:
        		return new DataBlockBoolean(1);
            
        	case BYTE:
        		return new DataBlockByte(1);
                
            case UBYTE:
                return new DataBlockUByte(1);
                
            case SHORT:
            	return new DataBlockShort(1);
                
            case USHORT:
                return new DataBlockUShort(1);
                
            case INT:
            	return new DataBlockInt(1);
                
            case UINT:
                return new DataBlockUInt(1);
                
            case LONG:
            case ULONG:
            	return new DataBlockLong(1);
                                
            case FLOAT:
            	return new DataBlockFloat(1);
                
            case DOUBLE:
            	return new DataBlockDouble(1);
                
            case UTF_STRING:
            case ASCII_STRING:
            	return new DataBlockString(1);
                
            default:
            	throw new RuntimeException("Data Type not allowed for a DataValue: " + dataType);
        }
    }
    
    
    @Override
    public int getComponentCount()
    {
        return 0; //was 1 but i think it's wrong
    }


    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append("DataValue: ");                
        text.append(this.getName() + " (" + this.dataType + ")");
        //if (dataBlock != null)
        //    text.append(" = " + dataBlock.getStringValue());
        text.append("\n");
        return text.toString();
    }
    
    
    //////////////////////////////////
    // Invalid methods in DataValue //
    //////////////////////////////////
    public void addComponent(DataComponent component)
    {
    	throw new UnsupportedOperationException();
    }
    
    public AbstractDataComponent getComponent(int index)
    {
        throw new UnsupportedOperationException();
    }
    
    public void removeComponent(int index)
    {
        throw new UnsupportedOperationException();
    }    

    public void removeAllComponents()
    {
        throw new UnsupportedOperationException();
    }


    public DataType getDataType()
    {
        return dataType;
    }


    public void setDataType(DataType type)
    {
        this.dataType = type;
    }
    
    
    @Override
    protected void updateAtomCount(int childOffsetCount)
    {
        // DO NOTHING
    }


	public ConstraintList getConstraints()
    {
        return constraints;
    }


    public void setConstraints(ConstraintList constraints)
    {
        this.constraints = constraints;
    }


    @Override
	public boolean hasConstraints()
	{
		return (constraints != null);
	}
}
