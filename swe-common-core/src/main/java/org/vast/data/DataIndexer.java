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


/**
 * <p>
 * Abstract base class for all Data Indexers
 * </p>
 *
 * @author Alex Robin
 * @since Apr 4, 2006
 * */
public abstract class DataIndexer
{
    protected AbstractDataBlock data;
    protected int componentIndex;
    protected int scalarCount;
    protected boolean hasNext;
    public boolean doVisitors = false;
    protected DataVisitor[] visitorList;
    protected DataIndexer[] indexerList;    
    protected DataIndexer parentIndexer;
    
    
    public abstract void getData(int[] indexList);
    public abstract void setData(AbstractDataBlock data);
    public abstract void clearData();
    public abstract void next();
    public abstract int skip(int num);
    public abstract void updateStartIndex(int startIndex);
    public abstract void updateScalarCount();
    public abstract DataIndexer copy();
    
    
    public DataIndexer()
    {
    }
    
    
    public boolean hasNext()
    {
        return hasNext;
    }
    
    
    /**
     * Adds a child indexer to the list
     * @param newIndexer
     */
    public void addChildIndexer(DataIndexer newIndexer)
    {
        DataIndexer[] oldMappers = indexerList;
        indexerList = new DataIndexer[oldMappers.length + 1];
        System.arraycopy(oldMappers, 0, indexerList, 0, oldMappers.length);
        indexerList[oldMappers.length] = newIndexer;
        newIndexer.parentIndexer = this;
    }
    
    
    /**
     * Insert a child indexer at the begining of the list
     * @param newIndexer
     */
    public void insertChildIndexer(DataIndexer newIndexer)
    {
        DataIndexer[] oldMappers = indexerList;
        indexerList = new DataIndexer[oldMappers.length + 1];
        System.arraycopy(oldMappers, 0, indexerList, 1, oldMappers.length);
        indexerList[0] = newIndexer;
        newIndexer.parentIndexer = this;
    }
     
    
    /**
     * Adds a visitor to the list
     * @param newVisitor
     */
    public void addVisitor(DataVisitor newVisitor)
    {
        if (visitorList != null)
        {
            DataVisitor[] oldVisitors = visitorList;
            visitorList = new DataVisitor[oldVisitors.length + 1];
            System.arraycopy(oldVisitors, 0, visitorList, 0, oldVisitors.length);
            visitorList[oldVisitors.length] = newVisitor;
        }
        else
        {
            visitorList = new DataVisitor[1];
            visitorList[0] = newVisitor;
        }
    }
    
    
    /**
     * Resets the indexer by resetting all necessary variables.
     * This also resets all child indexers.
     */
    public void reset()
    {
        this.hasNext = true;
        
        if (visitorList != null)
            this.doVisitors = true;
        
        for (int i = 0; i < indexerList.length; i++)
            indexerList[i].reset();
    }
    
    
    /**
     * Loop through all registered visitors and call their mapData method
     */
    protected void applyVisitors()
    {
        if (this.doVisitors)
        {
            for (int i = 0; i < visitorList.length; i++)
                visitorList[i].mapData(this.data);
            
            this.doVisitors = false;
        }
    }
}
