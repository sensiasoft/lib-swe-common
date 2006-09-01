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


/**
 * <p><b>Title:</b><br/>
 * Data Array Indexer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO DataArrayIndexer type description
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Aug 15, 2005
 * @version 1.0
 */
public class DataArrayIndexer extends DataIndexer
{
    protected int currentIndex;
    protected int arraySize;
    protected int atomCount;
    protected int indexOffset = 0;
    protected boolean interleavedBlock;
    protected boolean tupleBlock;
    protected boolean hasChildArray;
        
    
    private DataArrayIndexer()
    {        
    }
    
    
    public DataArrayIndexer(int componentIndex)
    {
        this.componentIndex = componentIndex;
        this.indexerList = new DataIndexer[0];
    }
    
    
    public DataArrayIndexer copy()
    {
        DataArrayIndexer newIndexer = new DataArrayIndexer();
        newIndexer.componentIndex = this.componentIndex;
        newIndexer.scalarCount = this.scalarCount;
        newIndexer.arraySize = this.arraySize;
        newIndexer.interleavedBlock = this.interleavedBlock;
        newIndexer.tupleBlock = this.tupleBlock;
        newIndexer.visitorList = this.visitorList;
        
        // copy all child indexers
        newIndexer.indexerList = new DataIndexer[indexerList.length];
        for (int i = 0; i < indexerList.length; i++)
            newIndexer.indexerList[i] = indexerList[i].copy();
        
        return newIndexer;
    }
    
    
    @Override
    public void updateStartIndex(int startIndex)
    {
        this.data.startIndex = startIndex;
        
        if (interleavedBlock)
        {
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                indexer.updateStartIndex(startIndex + currentIndex*atomCount + indexer.componentIndex);
            }
        }
        else if (tupleBlock)
        {
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                indexer.updateStartIndex(startIndex + currentIndex);
            }
        }
        else
        {
            // TODO case of DataBlockMixed in an array
        }
    }
    
    
    @Override
    public void updateScalarCount()
    {
        if (hasChildArray)
            atomCount = indexerList[0].scalarCount;
        
        scalarCount = atomCount * arraySize;        
        
        if (parentIndexer != null)
            parentIndexer.updateScalarCount();
    }
    
    
    @Override
    public void setData(AbstractDataBlock data)
    {
        AbstractDataBlock childBlock;
        this.data = data;
        
        if (data instanceof DataBlockParallel)
        {
            this.interleavedBlock = false;
            this.tupleBlock = true;
            
            if (hasChildArray)
            {
                indexerList[0].setData(data.copy());
            }
            else
            {
                for (int i = 0; i < indexerList.length; i++)
                {
                    DataIndexer indexer = indexerList[i];
                    childBlock = ((DataBlockParallel)data).blockArray[indexer.componentIndex];
                    indexer.setData(childBlock);
                }
            }
        }
        else if (data instanceof DataBlockMixed)
        {
            this.interleavedBlock = false;
            this.tupleBlock = false;
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                childBlock = ((DataBlockMixed)data).blockArray[indexer.componentIndex];
                indexer.setData(childBlock);
            }
        }
        else // case of primitive array blocks
        {
            this.interleavedBlock = true;
            this.tupleBlock = false;
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                childBlock = this.data.copy();
                indexer.setData(childBlock);
            }
        }
    }


    @Override
    public void getData(int[] indexList)
    {
        this.applyVisitors();
        currentIndex = indexList[indexOffset];
        
        // try to get requested element from each child indexer
        if (interleavedBlock)
        {
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                int nextIndex = data.startIndex + currentIndex * atomCount + indexer.componentIndex;
                indexer.updateStartIndex(nextIndex);
                indexer.getData(indexList);
            }
        }
        else if (tupleBlock)
        {
            if (hasChildArray)
            {
                int childSize = ((DataArrayIndexer)indexerList[0]).arraySize;
                int nextIndex = data.startIndex + currentIndex * childSize;
                indexerList[0].updateStartIndex(nextIndex);
                indexerList[0].getData(indexList);
            }
            else
            {
                for (int i = 0; i < indexerList.length; i++)
                {
                    DataIndexer indexer = indexerList[i];
                    int nextIndex = data.startIndex + currentIndex;
                    indexer.updateStartIndex(nextIndex);
                    indexer.getData(indexList);
                }
            }
        }
        else
        {
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                // indexer.updateStartIndex(data.startIndex);
                indexer.getData(indexList);
            }
        }
    }
    
    
    @Override
    public void clearData()
    {
        this.data = null;
        for (int i = 0; i < indexerList.length; i++)
        {
            DataIndexer indexer = indexerList[i];
            indexer.clearData();
        }
    }
    

    @Override
    public void next()
    {       
        boolean next = true;
        
        // apply visitors here
        this.applyVisitors();
        
        // try to get next element from each child indexer
        if (interleavedBlock)
        {
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                if (indexer.hasNext)
                {
                    int nextIndex = data.startIndex + currentIndex * atomCount + indexer.componentIndex;
                    indexer.updateStartIndex(nextIndex);
                    indexer.next();
                    if (indexer.hasNext)
                        next = false;
                }
            }
        }
        else if (tupleBlock)
        {
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                if (indexer.hasNext)
                {
                    int nextIndex = data.startIndex + currentIndex;
                    indexer.updateStartIndex(nextIndex);
                    indexer.next();
                    if (indexer.hasNext)
                        next = false;
                }
            }
        }
        else
        {
            // TODO case of DataBlockMixed in an array
        }
        
        // when all child indexers are done, increment index
        if (next)
        {
            currentIndex++;
            
            if (currentIndex >= arraySize)
            {
                this.hasNext = false;
                return;
            }
            
            for (int i = 0; i < indexerList.length; i++)
                indexerList[i].reset();
        }
    }
    
    
    @Override
    public int skip(int num)
    {
        //TODO skip method implementation
        return 0;
    }
    
    
    @Override
    public void reset()
    {
        super.reset();
        this.currentIndex = 0;
    }


    public int getIndexOffset()
    {
        return indexOffset;
    }


    public void setIndexOffset(int indexOffset)
    {
        this.indexOffset = indexOffset;
    }
}