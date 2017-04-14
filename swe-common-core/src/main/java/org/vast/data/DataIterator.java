/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import net.opengis.swe.v20.DataComponent;


/**
 * <p>
 * This iterator will go through all components recursively.
 * This does not iterate through each array value.
 * </p>
 *
 * @author Alex Robin
 * @since Sep 19, 2013
 */
public class DataIterator implements Iterator<DataComponent>, Iterable<DataComponent>
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
            
            if (component instanceof DataArrayImpl)
                this.count = 1;
        }
    }
    
    
	public DataIterator(DataComponent rootComponent)
	{
		this.rootComponent = rootComponent;
		reset();
	}
	
	
	public void reset()
	{
	    this.componentStack = new Stack<Record>();
        this.componentStack.push(new Record(rootComponent));
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
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
            if (current instanceof DataArrayImpl)
                child = ((DataArrayImpl)current).getElementType();
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
	
	
	@Override
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


    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public Iterator<DataComponent> iterator()
    {
        reset();
        return this;
    }
}