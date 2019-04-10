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

package org.vast.swe;

import java.io.IOException;
import java.util.Stack;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.BlockComponent;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.ScalarComponent;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataHandler;
import org.vast.cdm.common.ErrorHandler;
import org.vast.cdm.common.RawDataHandler;
import org.vast.data.AbstractArrayImpl;
import org.vast.data.DataArrayImpl;
import org.vast.data.DataChoiceImpl;


public abstract class DataTreeVisitor
{
	protected DataHandler dataHandler;
	protected RawDataHandler rawHandler;
	protected ErrorHandler errorHandler;
    protected BlockComponent parentArray;
    protected int parentArrayIndex;
	protected DataComponent dataComponents;
	protected DataEncoding dataEncoding;
	protected Stack<Record> componentStack;
	protected Record currentRecord;
    protected boolean newBlock = true;
	protected boolean endOfArray = false;
	protected boolean parsing = true;
	
    
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
		this.componentStack = new Stack<>();
	}
	
	
	/**
	 * Processes a scalar component
	 * @param component
	 * @throws CDMException
	 */
	protected abstract void processAtom(ScalarComponent component) throws CDMException, IOException;
	
	
	/**
	 * Processes an aggregate component
	 * @param component
	 * @return true if children should be processed, false otherwise
	 * @throws CDMException
	 */
	protected abstract boolean processBlock(DataComponent component) throws CDMException, IOException;
	
	
	/**
	 * Process the next scalar element in the stream
	 * @throws Exception 
	 */
	public void processNextElement() throws CDMException, IOException
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
        if (!(next instanceof ScalarComponent))
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
	    				Count sizeValue = ((DataArrayImpl)next).getArraySizeComponent();
	    				        
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
	    				DataComponent selectedComponent = ((DataChoiceImpl)next).getSelectedItem();
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
            
            processAtom((ScalarComponent)next);
            
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
                if (parentArray != null && parentArrayIndex == parentArray.getComponentCount())
                    endOfArray = true;
                
                endDataBlock();
                
                // send end of data event
                if (dataHandler != null)
                    dataHandler.endData(dataComponents, dataComponents.getData());
                
                // signal that a new block is starting
                newBlock = true;
                currentRecord = null;
                
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
	
	
	protected void endDataBlock() throws CDMException, IOException
    {        
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
            dataComponents = parentArray.getComponent(parentArrayIndex);
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


	public DataEncoding getDataEncoding()
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


	public void setDataEncoding(DataEncoding dataEncoding)
	{
		this.dataEncoding = dataEncoding;
	}
    
    
    public void setParentArray(BlockComponent parentArray)
    {
        this.parentArray = parentArray;
        this.dataComponents = parentArray.getElementType();
        if (parsing)
            parentArray.renewDataBlock();
        parentArrayIndex = 0;
    }
}