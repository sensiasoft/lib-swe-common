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
 * TODO DataGroupIndexer type description
 * </p>
 *
 * @author Alex Robin
 * @since Aug 15, 2005
 * */
public class DataGroupIndexer extends DataIndexer
{
    protected boolean interleavedBlock;
    protected boolean tupleBlock;
        
    
    private DataGroupIndexer()
    {
        
    }
    
    
    public DataGroupIndexer(int componentIndex)
    {
        this.componentIndex = componentIndex;
        this.indexerList = new DataIndexer[0];
    }
    
    
    @Override
    public DataGroupIndexer copy()
    {
        DataGroupIndexer newIndexer = new DataGroupIndexer();
        newIndexer.componentIndex = this.componentIndex;
        newIndexer.scalarCount = this.scalarCount;
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
        data.startIndex = startIndex;
        
        if (data instanceof DataBlockMixed)
        {
            for (int i = 0; i < indexerList.length; i++)
            {
                indexerList[i].updateStartIndex(startIndex);
            }
        }
        else
        {
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                indexer.updateStartIndex(startIndex + indexer.componentIndex);
            }
        }
    }
    
    
    @Override
    public void updateScalarCount()
    {
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
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                childBlock = ((DataBlockParallel)data).blockArray[indexer.componentIndex];
                indexer.setData(childBlock);
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
        else
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
        
        // try to get requested element from each child indexer
        if (interleavedBlock)
        {
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                int nextIndex = data.startIndex + indexer.componentIndex;
                indexer.updateStartIndex(nextIndex);
                indexer.getData(indexList);
            }
        }
        else if (tupleBlock)
        {
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                indexer.updateStartIndex(data.startIndex);
                indexer.getData(indexList);
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
        this.hasNext = false;
        this.applyVisitors();
        
        // try to get next element from each child indexer
        if (interleavedBlock)
        {
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];                
                if (indexer.hasNext)
                {
                    int nextIndex = data.startIndex + indexer.componentIndex;
                    indexer.updateStartIndex(nextIndex);
                    indexer.next();
                    if (indexer.hasNext)
                        this.hasNext = true;
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
                    indexer.updateStartIndex(data.startIndex);
                    indexer.next();
                    if (indexer.hasNext)
                        this.hasNext = true;
                }
            }
        }
        else
        {
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                if (indexer.hasNext)
                {
                    //indexer.updateStartIndex(data.startIndex);
                    indexer.next();
                    if (indexer.hasNext)
                        this.hasNext = true;
                }
            }
        }
    }
    
    
    @Override
    public int skip(int num)
    {
        //TODO skip method implementation
        return 0;
    }
}