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

import java.util.Hashtable;

import org.ogc.cdm.common.DataComponent;


/**
 * <p><b>Title:</b><br/>
 * Indexer Tree Builder
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Helper class to build an indexer tree modeled on a DataComponent
 * structure. This also allows to add visitors at any level in the tree.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Apr 6, 2006
 * @version 1.0
 */
public class IndexerTreeBuilder
{
    protected Hashtable<DataComponent, DataIndexer> indexerMap;
    protected DataComponent rootComponent;
    protected DataIndexer rootIndexer;
    
    
    public IndexerTreeBuilder(DataComponent rootComponent)
    {
        this.rootComponent = rootComponent;
        this.indexerMap = new Hashtable<DataComponent, DataIndexer>();
    }
    
    
    /**
     * Adds a visitor at the right level in the indexer tree
     * This will create all necessary indexers when needed.
     * @param componentPath
     * @param visitor
     */
    public void addVisitor(String componentPath, DataVisitor visitor)// throws DataException
    {
        String [] componentNames = componentPath.split("/");
        this.addVisitor(componentNames, 0, null, rootComponent, 0, visitor);
    }
    
    
    /**
     * Called recursively to handle logic for addVisitor() method above.
     * This will create all the necessary indexers in the tree and attach the visitor at the right place
     */
    protected void addVisitor(String[] componentNames, int pathPosition, DataIndexer parentIndexer,
                              DataComponent component, int currentIndex, DataVisitor visitor)
    {       
        DataIndexer indexer = null;
        
        // case of DataGroup
        if (component instanceof DataGroup)
        {
            // skip DataGroup if parentIndexer is not null
            if (parentIndexer == null)
            {
                // retrieve already created indexer or create a new one
                indexer = indexerMap.get(component);
                if (indexer == null)
                {
                    indexer = new DataGroupIndexer((DataGroup)component, currentIndex);
                    indexerMap.put(component, indexer);
                }
            }
            else
                indexer = parentIndexer;
            
            // if end of path, just add the visitor here
            if (pathPosition == componentNames.length-1)
            {
                indexer.addVisitor(visitor);
                return;
            }
        }
        
        // case of DataArray
        else if (component instanceof DataArray)
        {
            // retrieve already created indexer or create a new one
            indexer = indexerMap.get(component);            
            if (indexer == null)
            {
                indexer = new DataArrayIndexer((DataArray)component, currentIndex);
                indexerMap.put(component, indexer);                
                
                ((DataArrayIndexer)indexer).atomCount = countDescendants(component, 0);
                
                // add VarSizeMapper if array has variable size
                if (((DataArray)component).isVariableSize())
                    this.addVarSizeMapper((DataArray)component, (DataArrayIndexer)indexer);
                else // or set array size
                    ((DataArrayIndexer)indexer).arraySize = component.getComponentCount();
                
                // also compute scalar count
                indexer.updateScalarCount();
                
                // add to parentIndexer if not null
                if (parentIndexer != null)
                {
                    parentIndexer.addChildIndexer(indexer);
                    
                    if (parentIndexer instanceof DataArrayIndexer)
                    {
                        ((DataArrayIndexer)parentIndexer).hasChildArray = true;
                        indexer.parentIndexer = parentIndexer;
                        indexer.updateScalarCount();
                    }
                }
            }
            
            // reset currentIndex in the case of an array
            currentIndex = 0;
            
            // if end of path, just add the visitor here
            if (pathPosition == componentNames.length-1)
            {
                indexer.addVisitor(visitor);
                return;
            }            
        }
        
        // case of DataValue
        else if (component instanceof DataValue)
        {
            // retrieve already created indexer or create a new one
            indexer = indexerMap.get(component);            
            if (indexer == null)
            {
                indexer = new DataValueIndexer((DataValue)component, currentIndex);
                indexerMap.put(component, indexer);
                
                // add to parentIndexer if not null
                if (parentIndexer != null)
                    parentIndexer.addChildIndexer(indexer);
            }
            
            // if end of path, just add the visitor here
            if (pathPosition == componentNames.length-1)
            {
                indexer.addVisitor(visitor);
                return;
            }
        }
        
        // if a child component has the desired name, apply recursively
        int newPathPos = pathPosition+1;
        DataComponent childComponent = component.getComponent(componentNames[newPathPos]);
        if (childComponent != null)
        {            
            int newIndex = component.getComponentIndex(componentNames[newPathPos]) + currentIndex;            
            this.addVisitor(componentNames, newPathPos, indexer, childComponent, newIndex, visitor);
        }
        else
        {
            StringBuffer buf = new StringBuffer();
            for (int i=0; i<componentNames.length; i++)
            {
                if (i != 0)
                    buf.append('/');
                buf.append(componentNames[i]);                
            }
            throw new IllegalStateException("Component " + buf.toString() + " not found");
        }
        
        // if first level, set rootIndexer
        if (this.rootIndexer == null && pathPosition == 0)
            this.rootIndexer = indexer;
    }
    
    
    /**
     * This methods counts all scalar descendants and arrays
     * This should correspond to the number of atoms in each array element
     * @param component
     * @return
     */
    private int countDescendants(DataComponent component, int count)
    {
        if (component instanceof DataValue)
        {
            return 1;
        }
        else if (component instanceof DataArray)
        {
            if (count == 0)
                return countDescendants(component.getComponent(0), 0);
            else
                return 1;
        }
        else if (component instanceof DataGroup)
        {
            for (int i = 0; i < component.getComponentCount(); i++)
                count += countDescendants(component.getComponent(i), count);
            
            return count;
        }
        
        return 0;
    }
    
    
    /**
     * Finds the indexer dealing with the parent of the var size component
     * @param dataArray
     * @return
     */
    private void addVarSizeMapper(DataArray dataArray, DataArrayIndexer arrayIndexer)
    {
        DataValue sizeData = dataArray.getSizeData();
        DataComponent parentComponent = sizeData.parent;
        int index = parentComponent.getComponentIndex(sizeData.getName());
        
        DataIndexer parentIndexer = indexerMap.get(parentComponent);
        DataIndexer sizeIndexer = indexerMap.get(sizeData);            
        if (sizeIndexer == null)
        {
            sizeIndexer = new DataValueIndexer(sizeData, index);
            parentIndexer.insertChildIndexer(sizeIndexer);
            indexerMap.put(sizeData, sizeIndexer);
        }        
        
        sizeIndexer.addVisitor(new VarSizeMapper(arrayIndexer));
    }


    public DataIndexer getRootIndexer()
    {
        return rootIndexer;
    }
}
