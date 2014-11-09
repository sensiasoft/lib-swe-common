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

package org.vast.sweCommon;

import java.util.Stack;
import net.opengis.swe.v20.AbstractEncoding;
import net.opengis.swe.v20.BlockComponent;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataHandler;
import org.vast.cdm.common.ErrorHandler;
import org.vast.cdm.common.RawDataHandler;
import org.vast.data.AbstractArrayImpl;
import org.vast.data.DataArrayImpl;
import org.vast.data.DataChoiceImpl;
import org.vast.data.DataValue;
import org.vast.data.CountImpl;


public abstract class DataTreeVisitor
{
	protected DataHandler dataHandler;
	protected RawDataHandler rawHandler;
	protected ErrorHandler errorHandler;
    protected BlockComponent parentArray;
    protected int parentArrayIndex;
	protected DataComponent dataComponents;
	protected AbstractEncoding dataEncoding;
	protected Stack<Record> componentStack;
	protected Record currentRecord;
    protected boolean newBlock = true;
	protected boolean endOfArray = false;
	protected boolean parsing = true;
	protected CountImpl sizeValue; // for holding implicit array size
	
    
	protected class Record
    {
        public DataComponent component;
        public int index;
        public int count;

        public Record(DataComponent component)
        {
            this.component = component;
            this.count = component.getComponentCount();
            this.index = 0;
        }
    }
    
    
	public DataTreeVisitor(boolean parsing)
	{
		this.parsing = parsing;
		this.componentStack = new Stack<Record>();
		this.sizeValue = new CountImpl();
		this.sizeValue.assignNewDataBlock();
	}
	
	
	protected abstract void processAtom(DataValue scalarComponent) throws Exception;
	
	
	/**
	 * Process an aggregate component
	 * @param scalarComponent
	 * @return true if children should be processed, false otherwise
	 * @throws CDMException
	 */
	protected abstract boolean processBlock(DataComponent blockInfoComponent) throws Exception;
	
	
	/**
	 * Process the next scalar element in the stream
	 * @throws Exception 
	 */
	public void processNextElement() throws Exception
	{
        // reset iterator if new block is starting
		if (newBlock)
        {
            this.reset();
            newBlock = false;
        }
        
        // send beginning of data event
    	if (dataHandler != null && componentStack.isEmpty() && currentRecord.index == 0)
    		dataHandler.startData(dataComponents);
    	
        // retrieve current component
    	DataComponent next = currentRecord.component;
    	
        // if child is not a DataValue, go in !!
        if (!(next instanceof DataValue))
        {
        	// send start block event
        	if (dataHandler != null)
        		dataHandler.startDataBlock(currentRecord.component);
        	
        	// some reader/writer may process the whole block at once
        	// and will then return false here to skip children
        	boolean processChildren = processBlock(next);
        	
        	if (processChildren)
        	{
        		// case of variable array size
	    		if ((next instanceof DataArrayImpl) && ((DataArrayImpl)next).isVariableSize())
	    		{
	    			if (((DataArrayImpl)next).isImplicitSize())
	    			{
	    				// read implicit array size (when parsing)
	    				if (parsing)
	        			{
	    					processAtom(sizeValue);
	            			int newSize = sizeValue.getData().getIntValue();
	            			
	            			// resize array according to size read!
	            			((DataArrayImpl)next).updateSize(newSize);
	            			currentRecord.count = newSize;
	        			}
	        		
	        			// write array size
	        			else
	        			{
	        				sizeValue.getData().setIntValue(((AbstractArrayImpl)next).getComponentCount());
	        				processAtom(sizeValue);
	        			}
	    			}        		
	    			else if (parsing)
	    				((DataArrayImpl)next).updateSize();
	    		}
	    	
	    		// do only if we actually have children
	    		if (currentRecord.count > 0)
	    		{
	    			// push this aggregate record to the stack
		    		componentStack.push(currentRecord);
		    		
		    		// select first child of aggregate
	    			if (next instanceof DataChoiceImpl)
	    			{
	    				DataComponent selectedComponent = ((DataChoiceImpl)next).getSelectedComponent();
	    				if (selectedComponent != null)
	    					next = selectedComponent;
	    				else
	    					next = next.getComponent(0);
	    			}
	    			else
	    				next = next.getComponent(0);
	        	
	    			// create new record for first child an process it right away
		    		currentRecord = new Record(next);
		    		processNextElement();
		    		return;
	    		}
        	}
        }
        
        // otherwise parse one atom element
        else
        {
            if (dataHandler != null)
                dataHandler.beginDataAtom(next);
            
            processAtom((DataValue)next);
            
            if (dataHandler != null)
                dataHandler.endDataAtom(next, next.getData());
        }
        
        // take care of parent record(s) in stack
        Record parentRecord = null;        
        do
        {
            // catch end of data
            if (componentStack.isEmpty())
            {
                // send end of data event
                if (dataHandler != null)
                    dataHandler.endData(dataComponents, dataComponents.getData());
                
                // signal that a new block is starting
                newBlock = true;
                currentRecord = null;
                
                if (parentArray != null && parentArrayIndex == ((DataComponent)parentArray).getComponentCount())
                    endOfArray = true;
                
                return;
            }
            
            // pop next parent from stack
            parentRecord = componentStack.pop();
            
            // increment index of parent component
            if (parentRecord.component instanceof DataChoiceImpl)
                parentRecord.index = parentRecord.count;
            else
                parentRecord.index++;
            
            // send end of block event if there are no more children
            if (parentRecord.index >= parentRecord.count)
            {
                if (dataHandler != null)
                    dataHandler.endDataBlock(parentRecord.component, parentRecord.component.getData());
            }
            
            // keep closing parents recursively until the parent has more children to process
        }
        while (parentRecord.index >= parentRecord.count);
        
        // create next component record
        componentStack.push(parentRecord);
        next = parentRecord.component.getComponent(parentRecord.index);
        currentRecord = new Record(next);
	}
	
	
	public boolean isEndOfDataBlock()
	{
	    return newBlock;
	}
		
	
	/**
	 * Reset the parser before parsing a new tuple
	 */
	public void reset()
	{
		// prepare next array element
		if (parentArray != null)
        {
            dataComponents = ((DataComponent)parentArray).getComponent(parentArrayIndex);
            parentArrayIndex++;
        }
        
		// reset component stack
        componentStack.clear();
        currentRecord = new Record(dataComponents);
	}

	
	/////////////////////
	// Get/Set Methods //
	/////////////////////
	public DataHandler getDataHandler()
	{
		return this.dataHandler;
	}
	
	
	public RawDataHandler getRawDataHandler()
	{
		return this.rawHandler;
	}


	public ErrorHandler getErrorHandler()
	{
		return this.errorHandler;
	}
	
	
	public DataComponent getDataComponents()
	{
		return this.dataComponents;
	}


	public AbstractEncoding getDataEncoding()
	{
		return this.dataEncoding;
	}


	public void setDataHandler(DataHandler handler)
	{
		this.dataHandler = handler;
	}
	
	
	public void setRawDataHandler(RawDataHandler handler)
	{
		this.rawHandler = handler;		
	}


	public void setErrorHandler(ErrorHandler handler)
	{
		this.errorHandler = handler;
	}
	
	
	public void setDataComponents(DataComponent dataInfo)
	{
		this.dataComponents = dataInfo;
	}


	public void setDataEncoding(AbstractEncoding dataEncoding)
	{
		this.dataEncoding = dataEncoding;
	}
    
    
    public void setParentArray(BlockComponent parentArray)
    {
        this.parentArray = parentArray;
        ((DataComponent)parentArray).renewDataBlock();
        parentArrayIndex = 0;
    }
}