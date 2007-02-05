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

package org.vast.cdm.common;

import java.util.Stack;
import org.vast.data.*;


public abstract class DataIterator
{
	protected DataHandler dataHandler;
	protected RawDataHandler rawHandler;
	protected ErrorHandler errorHandler;
	protected AbstractDataComponent dataComponents;
	protected DataEncoding dataEncoding;
	protected Stack<Record> componentStack;
	protected Record currentComponent;
		
	
	public DataIterator()
	{
		this.componentStack = new Stack<Record>();
	}
	
	
	protected abstract void processAtom(DataValue scalarInfo) throws CDMException;
	
	
	/**
	 * TODO nextInfo method description
	 * @return
	 */
	protected void processNextElement() throws CDMException
	{
		// send begining of block events
    	if (currentComponent.index == 0)
    	{
    		if (componentStack.isEmpty())
				dataHandler.startData(dataComponents);
			else
				dataHandler.startDataBlock(currentComponent.parent);
    	}
        
    	// now get next child
    	AbstractDataComponent next = (AbstractDataComponent)currentComponent.parent.getComponent(currentComponent.index);
        currentComponent.index++;
        
        // if child is not a DataValue, go in !!
        if (!(next instanceof DataValue))
        {
        	// update size of variable size arrays
        	if (next instanceof DataArray)
        		((DataArray)next).updateSize();
        	
        	componentStack.push(currentComponent);
        	currentComponent = new Record(next);
            processNextElement();
        }
        
        // otherwise parse element
        else
        {
            dataHandler.beginDataAtom(next);
            processAtom((DataValue)next);
        	dataHandler.endDataAtom(next, next.getData());
        }
        
        // if at the end of previous record
        while(currentComponent.index >= currentComponent.count)
    	{
        	if (!componentStack.isEmpty())
        	{
        		dataHandler.endDataBlock(currentComponent.parent, currentComponent.parent.getData());
        		currentComponent = componentStack.pop();
        	}
        	else
        	{
        		dataHandler.endData(dataComponents, dataComponents.getData());
				this.reset();
				break;
        	}
    	}
	}
		
	
	/**
	 * Reset the parser before parsing a new tuple
	 */
	public void reset()
	{
		componentStack.clear();		
		currentComponent = new Record(dataComponents);
        dataComponents.renewDataBlock();
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
		this.dataComponents = (AbstractDataComponent)dataInfo;
	}


	public void setDataEncoding(DataEncoding dataEncoding)
	{
		this.dataEncoding = dataEncoding;
	}
}


class Record
{
    public AbstractDataComponent parent;
    public int index;
    public int count;

    public Record(AbstractDataComponent parent)
    {
        this.parent = parent;
        this.count = parent.getComponentCount();
        this.index = 0;
    }
}