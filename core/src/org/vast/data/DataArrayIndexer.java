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


/**
 * <p><b>Title:</b><br/>
 * Data Array Indexer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO DataArrayIndexer type description
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Aug 15, 2005
 * @version 1.0
 */
public class DataArrayIndexer extends DataIndexer
{
    protected int currentIndex;
    protected int arraySize;
    protected int childScalarCount; // count of child
    protected int indexOffset = 0;
    protected boolean interleavedBlock;
    protected boolean tupleBlock;
    protected boolean hasChildArray;
    protected DataIndexer varSizeIndexer;
    protected DataVisitor varSizeVisitor; // for implicit array size
    protected DataBlockInt arraySizeBlock = new DataBlockInt(1);
    
    
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
        newIndexer.childScalarCount = this.childScalarCount;
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
                indexer.updateStartIndex(startIndex + currentIndex*childScalarCount + indexer.componentIndex);
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
        else // case of list of DataBlockMixed
        {
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                indexer.updateStartIndex(0);
            }
        }
    }
    
    
    @Override
    public void updateScalarCount()
    {
        if (hasChildArray)
            childScalarCount = indexerList[0].scalarCount;
        
        scalarCount = childScalarCount * arraySize;
        
        if (parentIndexer != null)
            parentIndexer.updateScalarCount();
    }
    
    
    @Override
    public void setData(AbstractDataBlock data)
    {
        AbstractDataBlock childBlock;
        this.data = data;
        
        // datablock mixed not allowed
        if (data instanceof DataBlockMixed)
        {
            throw new IllegalStateException(DataArray.errorBlockMixed);
        }
        else if (data instanceof DataBlockParallel)
        {
            this.interleavedBlock = false;
            this.tupleBlock = true;
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                indexer.setData(data.copy());
            }
        }
        else if (data instanceof DataBlockList)
        {
            this.interleavedBlock = false;
            this.tupleBlock = false;
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                indexer.setData(data.copy());
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
        
        // if implicit variable size, update size
        if (varSizeVisitor != null)
        {
        	arraySize = data.atomCount / childScalarCount;
        	updateScalarCount();
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
                int nextIndex = data.startIndex + currentIndex * childScalarCount + indexer.componentIndex;
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
        else // case of list of DataBlockMixed
        {
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                int nextIndex = data.startIndex + currentIndex;
                indexer.updateStartIndex(0);
                indexer.setData((AbstractDataBlock)((DataBlockList)data).get(nextIndex));
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
    
    
    // TODO DataArrayIndexer now only has one indexer anyway so no need to loop !
    @Override
    public void next()
    {       
        boolean childDone = true;
        
        // apply visitors here
        if (currentIndex == 0)
        	this.applyVisitors();
        
        // try to get next element from each child indexer
        if (interleavedBlock)
        {
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                if (indexer.hasNext)
                {
                    int nextIndex = data.startIndex + currentIndex * childScalarCount + indexer.componentIndex;
                    indexer.updateStartIndex(nextIndex);
                    indexer.next();
                    if (indexer.hasNext)
                        childDone = false;
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
                        childDone = false;
                }
            }
        }
        else // case of list of DataBlockMixed
        {
            for (int i = 0; i < indexerList.length; i++)
            {
                DataIndexer indexer = indexerList[i];
                if (indexer.hasNext)
                {
                    int nextIndex = data.startIndex + currentIndex;
                    indexer.updateStartIndex(0);
                    indexer.setData((AbstractDataBlock)((DataBlockList)data).get(nextIndex));
                    indexer.next();
                    if (indexer.hasNext)
                        childDone = false;
                }
            }
        }
        
        // when all child indexers are done, increment index
        if (childDone)
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
    
    
    @Override
	protected void applyVisitors()
	{
		super.applyVisitors();
		if (varSizeVisitor != null)
		{
			arraySizeBlock.setIntValue(arraySize);
        	varSizeVisitor.mapData(arraySizeBlock);
		}
	}


    public int getIndexOffset()
    {
        return indexOffset;
    }


    public void setIndexOffset(int indexOffset)
    {
        this.indexOffset = indexOffset;
    }


    public int getChildScalarCount()
    {
        return childScalarCount;
    }


    public void setChildScalarCount(int atomCount)
    {
        this.childScalarCount = atomCount;
    }


    public int getArraySize()
    {
        return arraySize;
    }


    public void setArraySize(int arraySize)
    {
        this.arraySize = arraySize;
    }


    public void setHasChildArray(boolean hasChildArray)
    {
        this.hasChildArray = hasChildArray;
    }


    public DataIndexer getVarSizeIndexer()
    {
        return varSizeIndexer;
    }


    public void setVarSizeIndexer(DataIndexer varSizeIndexer)
    {
        this.varSizeIndexer = varSizeIndexer;
    }


	public DataVisitor getVarSizeVisitor()
	{
		return varSizeVisitor;
	}


	public void setVarSizeVisitor(DataVisitor varSizeVisitor)
	{
		this.varSizeVisitor = varSizeVisitor;
	}
}