/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import org.ogc.cdm.common.*;
import org.vast.unit.Unit;


/**
 * <p><b>Title:</b><br/>
 * Atomic Data Value
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Atomic (no children) DataContainer usually containing a scalar value 
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @version 1.0
 */
public class DataValue extends AbstractDataComponent
{
    protected DataType dataType;
    protected Unit uom;
    
    
    public DataValue()
    {
    	this.scalarCount = 1;
    	this.dataType = DataType.OTHER;
    }
    
    
    public DataValue(DataType type)
    {
    	this.scalarCount = 1;
    	this.dataType = type;
    	this.assignNewDataBlock();
    }
    
    
    @Override
    public DataValue copy()
    {
    	DataValue newVal = new DataValue(dataType);
    	newVal.name = this.name;
    	newVal.properties = this.properties;
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
    	this.dataBlock = (AbstractDataBlock)dataBlock;
    }
    
    
    @Override
    protected AbstractDataBlock createDataBlock()
    {
    	switch (dataType)
        {
        	case BOOLEAN:
        		return new DataBlockBoolean(1);
            
        	case BYTE:
        		return new DataBlockByte(1);
                
            case SHORT:
            	return new DataBlockShort(1);
            
            case INT:
            	return new DataBlockInt(1);
                
            case LONG:
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
        //    text.append(" = " + dataBlock.getStringValue() + "\n");
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


    public Unit getUom()
    {
        return uom;
    }


    public void setUom(Unit uom)
    {
        this.uom = uom;
    }


    public DataType getDataType()
    {
        return dataType;
    }


    public void setDataType(DataType type)
    {
        this.dataType = type;
    }
}
