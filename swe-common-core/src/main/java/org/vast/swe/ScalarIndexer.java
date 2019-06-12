/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe;

import java.util.ArrayList;
import java.util.List;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.RangeComponent;
import net.opengis.swe.v20.ScalarComponent;
import net.opengis.swe.v20.Vector;


public class ScalarIndexer
{
    IndexerPart rootIndexer;
    
    
    interface IndexerPart
    {
        public int getOffset(int startOffset, DataBlock dataBlk);
        public void addOffset(int offset);
        public void addIndexer(IndexerPart indexer);
    }
    
    
    // the most basic sub-indexer holding a fixed offset
    static class FixedIndexer implements IndexerPart
    {
        int offset;
        
        @Override
        public int getOffset(int startOffset, DataBlock dataBlk)
        {
            return startOffset + offset;
        }
        
        @Override
        public void addOffset(int offset)
        {
            this.offset += offset;
        }
        
        @Override
        public void addIndexer(IndexerPart indexer)
        {
            // never include sub-indexers
        }
    }
    
    
    // sub-indexer for record and vector components
    // this maintains a list of sub-indexers that are collapsed in simple cases
    static class ListIndexer implements IndexerPart
    {
        List<IndexerPart> subIndexers = new ArrayList<>();
        
        @Override
        public int getOffset(int startOffset, DataBlock dataBlk)
        {
            for (IndexerPart part: subIndexers)
                startOffset = part.getOffset(startOffset, dataBlk);      
            return startOffset;
        }
        
        @Override
        public void addOffset(int offset)
        {
            IndexerPart lastIndexer;
            
            if (subIndexers.isEmpty())
            {
                lastIndexer = new FixedIndexer();
                subIndexers.add(lastIndexer);
            }
            else
            {
                lastIndexer = subIndexers.get(subIndexers.size()-1);
                if (!(lastIndexer instanceof FixedIndexer))
                {
                    lastIndexer = new FixedIndexer();
                    subIndexers.add(lastIndexer);
                }
            }
            
            lastIndexer.addOffset(offset);
        }
        
        @Override
        public void addIndexer(IndexerPart indexer)
        {
            subIndexers.add(indexer);
        }
        
        public IndexerPart simplify()
        {
            if (subIndexers.isEmpty())
                return new FixedIndexer();
            
            if (subIndexers.size() == 1)
                return subIndexers.get(0);
            
            return this;
        }
    }
    
    // sub-indexer for choice components
    // it uses a different indexer depending on the item selected
    static class ChoiceIndexer implements IndexerPart
    {
        List<IndexerPart> itemIndexers = new ArrayList<>();
        
        @Override
        public int getOffset(int startOffset, DataBlock dataBlk)
        {
            int choiceIndex = dataBlk.getIntValue(startOffset);
            
            if (choiceIndex < 0 || choiceIndex > itemIndexers.size()-1)
                return Integer.MIN_VALUE;
            
            IndexerPart itemIndexer = itemIndexers.get(choiceIndex);            
            if (itemIndexer == null)
                return Integer.MIN_VALUE;
            
            return itemIndexer.getOffset(startOffset + 1, dataBlk);
        }
        
        @Override
        public void addOffset(int offset)
        {
            IndexerPart fixedIndexer = new FixedIndexer();
            fixedIndexer.addOffset(offset);
            itemIndexers.add(fixedIndexer);
        }
        
        @Override
        public void addIndexer(IndexerPart indexer)
        {
            itemIndexers.add(indexer);
        }
    }
    
    
    public ScalarIndexer(DataComponent rootComponent, ScalarComponent target)
    {
        ListIndexer initIndexer = new ListIndexer();
        buildIndexer(rootComponent, target, initIndexer);
        
        // use sub-indexer directly if there is only one!
        rootIndexer = initIndexer.simplify();
    }
    
    
    /**
     * Keep building indexer tree using the given component
     * @param comp
     * @param target
     * @param currentPart
     * @return true if current component is the one we're trying to index
     */
    private boolean buildIndexer(DataComponent comp, DataComponent target, IndexerPart currentIndexer)
    {
        if (comp == target)
            return true;
        
        boolean foundTarget = false;        
        
        if (comp instanceof DataRecord || comp instanceof Vector)
        {
            ListIndexer recordIndexer = new ListIndexer();
            for (int i=0; i<comp.getComponentCount(); i++)
            {
                DataComponent field = comp.getComponent(i);
                foundTarget = buildIndexer(field, target, recordIndexer);
                if (foundTarget)
                    break;
            }
            currentIndexer.addIndexer(recordIndexer.simplify());
        }
        
        else if (comp instanceof DataArray)
        {
            if (((DataArray) comp).isVariableSize())
            {
                throw new UnsupportedOperationException("Variable size arrays are not supported in indexer");
            }
            else
            {
                // TODO add support for DataArray
            }
        }
        
        else if (comp instanceof DataChoice)
        {
            ChoiceIndexer choiceIndexer = new ChoiceIndexer();
            for (DataComponent item: ((DataChoice) comp).getItemList())
            {
                int prevSize = choiceIndexer.itemIndexers.size();
                foundTarget = buildIndexer(item, target, choiceIndexer);
                
                // if nothing was added, add a 0 fixed offset
                int newSize = choiceIndexer.itemIndexers.size();
                if (newSize == prevSize)
                    choiceIndexer.addIndexer(new FixedIndexer());
                
                if (foundTarget)
                    break;
            }
            
            // if target was found within the choice, we keep only the last item indexer,
            // so that other choices immediately lead to negative index during lookup
            if (foundTarget)
            {
                for (int i=0; i<choiceIndexer.itemIndexers.size()-1; i++)
                    choiceIndexer.itemIndexers.set(i, null);
            }
                
            currentIndexer.addIndexer(choiceIndexer);
        }
        
        // simple components
        else if (comp instanceof RangeComponent)
        {
            currentIndexer.addOffset(2);
        }
        
        else if (comp instanceof ScalarComponent)
        {
            currentIndexer.addOffset(1);
        }
        
        return foundTarget;
    }
    
    
    public final int getDataIndex(DataBlock dataBlk)
    {
        return rootIndexer.getOffset(0, dataBlk);
    }
    
    
    public final boolean getBooleanValue(DataBlock dataBlk)
    {
        return dataBlk.getBooleanValue(getDataIndex(dataBlk));
    }
    
    
    public final byte getByteValue(DataBlock dataBlk)
    {
        return dataBlk.getByteValue(getDataIndex(dataBlk));
    }


    public final short getShortValue(DataBlock dataBlk)
    {
        return dataBlk.getShortValue(getDataIndex(dataBlk));
    }


    public final int getIntValue(DataBlock dataBlk)
    {
        return dataBlk.getIntValue(getDataIndex(dataBlk));
    }


    public final long getLongValue(DataBlock dataBlk)
    {
        return dataBlk.getLongValue(getDataIndex(dataBlk));
    }


    public final float getFloatValue(DataBlock dataBlk)
    {
        return dataBlk.getFloatValue(getDataIndex(dataBlk));
    }


    public final double getDoubleValue(DataBlock dataBlk)
    {
        return dataBlk.getDoubleValue(getDataIndex(dataBlk));
    }
    
    
    public final String getStringValue(DataBlock dataBlk)
    {
        return dataBlk.getStringValue(getDataIndex(dataBlk));
    }
}
