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

import java.util.*;

import org.vast.cdm.common.DataComponent;


/**
 * <p><b>Title:</b><br/>
 * ScalarIterator
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO ScalarIterator type description
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Aug 15, 2005
 * @version 1.0
 */
public class ScalarIterator
{
    protected Stack<Record> componentStack = new Stack<Record>();
    protected Record currentComponent;
    protected DataComponent nextComponent;
    protected DataComponent baseComponent;

    protected class Record
    {
        public DataComponent parent;
        public int index;
        public int count;

        public Record(DataComponent parent)
        {
            this.parent = parent;
            this.count = parent.getComponentCount();
            this.index = 0;
        }
    }
    
    
    public ScalarIterator(DataComponent baseContainer)
    {
    	this.baseComponent = baseContainer;
    	this.reset();
    }
    
    
    public void reset()
    {
    	componentStack.clear();
    	currentComponent = new Record(baseComponent);
    	componentStack.push(currentComponent);
    }


    public boolean hasNext()
    {
        // if at the end of previous record
        while(currentComponent.index >= currentComponent.count)
        {
            if (!componentStack.isEmpty())
                currentComponent = componentStack.pop();
            else
                break;
        }
        
        if (componentStack.isEmpty() && (currentComponent.index >= currentComponent.count))
            return false;
        else
            return true;
    }


    public DataComponent next()
    {
        nextComponent = getNextScalar();
        return nextComponent;
    }
    
    
    public DataComponent[] nextPath()
    {
        nextComponent = getNextScalar();
        
        int numComponents = componentStack.size();
        DataComponent[] componentPath = new DataComponent[numComponents+1];
        
        for (int i=0; i<numComponents; i++)
            componentPath[i] = componentStack.get(i).parent;
        
        //componentPath[numComponents-1] = oldParent;
        componentPath[numComponents] = nextComponent;
        
        return componentPath;
    }



    /**
     * Finds next container given current record
     * Uses a stack instead of recursion
     * @return DataContainer
     */
    private DataComponent getNextScalar()
    {
    	// now get next child
    	DataComponent next = currentComponent.parent.getComponent(currentComponent.index);
        currentComponent.index++;
        
        // if child is not a DataValue, go in !!
        if (!(next instanceof DataValue))
        {
        	componentStack.push(currentComponent);
        	currentComponent = new Record(next);
        	
        	if (next instanceof DataArray)
        		currentComponent.count = 1;
        	
        	return getNextScalar();
        }
        
        return next;
    }
}