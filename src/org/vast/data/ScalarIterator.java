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
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Aug 15, 2005
 * @version 1.0
 */
public class ScalarIterator
{
    protected Stack<Record> componentStack = new Stack<Record>();
    protected Record currentRecord;
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
            
            if (parent instanceof DataArray)
                this.count = 1;
            else
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
    	currentRecord = new Record(baseComponent);
    	//componentStack.push(currentComponent);
    }


    public boolean hasNext()
    {
        // if at the end of previous record
        while(currentRecord.index >= currentRecord.count)
        {
            if (!componentStack.isEmpty())
                currentRecord = componentStack.pop();
            else
                break;
        }
        
        if (componentStack.isEmpty() && (currentRecord.index >= currentRecord.count))
            return false;
        else
            return true;
    }


    public DataValue next()
    {
        nextComponent = getNextScalar();
        return (DataValue)nextComponent;
    }
    
    
    public DataComponent[] nextPath()
    {
        nextComponent = getNextScalar();
        
        int numComponents = componentStack.size() + 2;
        DataComponent[] componentPath = new DataComponent[numComponents];
        
        for (int i=0; i<numComponents-2; i++)
            componentPath[i] = componentStack.get(i).parent;
        
        componentPath[numComponents-2] = currentRecord.parent;
        componentPath[numComponents-1] = nextComponent;
        
        return componentPath;
    }



    /**
     * Finds next container given current record
     * Uses a stack instead of recursion
     * @return DataContainer
     */
    private DataValue getNextScalar()
    {
    	// now get next child
    	DataComponent next = currentRecord.parent.getComponent(currentRecord.index);
        currentRecord.index++;
        
        // if child is not a DataValue, go in !!
        if (!(next instanceof DataValue))
        {
        	componentStack.push(currentRecord);
        	currentRecord = new Record(next);
        	return getNextScalar();
        }
        
        return (DataValue)next;
    }
}