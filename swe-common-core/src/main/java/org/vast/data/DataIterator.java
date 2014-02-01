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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import org.vast.cdm.common.DataComponent;
import org.vast.data.DataArray;
import org.vast.data.DataValue;


/**
 * <p>
 * This iterator will go through all components recursively.
 * This does not iterate through each array value.
 * </p>
 *
 * <p>Copyright (c) 2013</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Sep 19, 2013
 */
public class DataIterator implements Iterator<DataComponent>
{
	protected DataComponent rootComponent;
	protected Stack<Record> componentStack;
	
    
	protected class Record
    {
        public DataComponent component;
        public int index;
        public int count;

        public Record(DataComponent component)
        {
            this.component = component;
            this.count = component.getComponentCount();
            this.index = -1;
            
            if (component instanceof DataArray)
                this.count = 1;
        }
    }
    
    
	public DataIterator(DataComponent rootComponent)
	{
		this.componentStack = new Stack<Record>();
		this.componentStack.push(new Record(rootComponent));
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public DataComponent next()
	{   	
        if (!hasNext())
            throw new NoSuchElementException();
	    
	    DataComponent next = null;
        
	    // retrieve current component
        Record currentRecord = componentStack.peek();
    	DataComponent current = currentRecord.component;
    	    	
        // if child is not a DataValue, go in !!
        if (!(current instanceof DataValue))
        {
            // return the aggregate itself for 1st pass   
            if (currentRecord.index < 0)
            {
                currentRecord.index = 0;
                return current;
            }
            
            // select next child of aggregate
            DataComponent child;
            if (current instanceof DataArray)
                child = ((DataArray)current).getArrayComponent();
            else
                child = current.getComponent(currentRecord.index);
            currentRecord.index++;
            
            // create new record for next child and push it to the stack
            componentStack.push(new Record(child));
            next = next();
        }
        else
        {
            currentRecord.index++;
            next = current;
        }

        // pop components until we find one that still has components to process
        while (!componentStack.isEmpty() && componentStack.peek().index >= componentStack.peek().count)
            componentStack.pop();
        
        return next;
	}
	
	
	public boolean hasNext()
	{
	    return !componentStack.isEmpty();
	}
	
	
	public void skipChildren()
	{
	    // skip to parent component in stack
	    componentStack.pop();
	    
	    // keep poping if ancestors are done as well
	    while (!componentStack.isEmpty() && componentStack.peek().index >= componentStack.peek().count)
            componentStack.pop();
	}


    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}