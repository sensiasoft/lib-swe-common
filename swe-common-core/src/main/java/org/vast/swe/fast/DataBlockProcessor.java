/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2016 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.fast;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import org.vast.swe.IComponentFilter;
import net.opengis.swe.v20.CategoryRange;
import net.opengis.swe.v20.CountRange;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataComponentVisitor;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.QuantityRange;
import net.opengis.swe.v20.TimeRange;
import net.opengis.swe.v20.Vector;


/**
 * <p>
 * Base class for all data block level processors.<br/>
 * The processor tree is pre-configured so that the processing can occur very
 * fast on many data blocks matching the same components structure.
 * </p>
 *
 * @author Alex Robin
 * @since Dec 7, 2016
 */
public abstract class DataBlockProcessor implements DataComponentVisitor
{
    DataComponent dataComponents;
    IComponentFilter filter;
    ArrayDeque<AtomProcessor> processorStack = new ArrayDeque<>();
    AtomProcessor rootProcessor;
    boolean enableSubTree = true;
    boolean processorTreeReady;
    
    
    public interface AtomProcessor
    {
        public void setEnabled(boolean enabled);
        public boolean isEnabled();
        public int process(DataBlock data, int index) throws IOException;
    }
    
    
    public interface CompositeProcessor extends AtomProcessor
    {
        public void add(AtomProcessor parser);
    }
    
    
    public abstract static class BaseProcessor implements AtomProcessor
    {
        boolean enabled = true;
        
        @Override
        public void setEnabled(boolean enabled)
        {
            this.enabled = enabled;
        }
        
        @Override
        public boolean isEnabled()
        {
            return this.enabled;
        }
    }
    
    
    public static class RecordProcessor extends BaseProcessor implements CompositeProcessor
    {
        List<AtomProcessor> fieldProcessors = new ArrayList<>();
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            for (AtomProcessor p: fieldProcessors)
                index = p.process(data, index);            
            return index;
        }
        
        @Override
        public void add(AtomProcessor processor)
        {
            fieldProcessors.add(processor);
        }
    }
    
    
    public static class ArrayProcessor extends BaseProcessor implements CompositeProcessor
    {
        AtomProcessor eltProcessor;
        int arraySize;
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            for (int i = 0; i < arraySize; i++)
                index = eltProcessor.process(data, index);            
            return index;
        }
        
        public void setArraySize(int arraySize)
        {
            this.arraySize = arraySize;
        }
        
        @Override
        public void add(AtomProcessor processor)
        {
            this.eltProcessor = processor;
        }
    }
    
    
    public abstract static class ChoiceProcessor extends BaseProcessor implements CompositeProcessor
    {
        List<AtomProcessor> itemProcessors = new ArrayList<>();
                
        public int process(DataBlock data, int index, int selectedIndex) throws IOException
        {
            return itemProcessors.get(selectedIndex).process(data, index);
        }
        
        @Override
        public void add(AtomProcessor processor)
        {
            itemProcessors.add(processor);
        }
    }
    
    
    protected abstract void init();
    
    
    protected void addToProcessorTree(AtomProcessor processor)
    {
        // add to parent processor or root
        if (!processorStack.isEmpty())
        {
            CompositeProcessor parent = (CompositeProcessor)processorStack.peek();
            parent.add(processor);
        }
        else
            rootProcessor = processor;
        
        // enable/disable processor and parents as needed
        setEnabled(processor);
        
        if (processor instanceof CompositeProcessor)
            processorStack.push(processor);
    }
    
    
    protected void setEnabled(AtomProcessor processor)
    {
        processor.setEnabled(enableSubTree);
        
        // if enabled, also enable all parents
        if (enableSubTree)
        {
            for (AtomProcessor parent: processorStack)
                parent.setEnabled(true);                    
        }
    }
    
    
    protected void checkEnabled(DataComponent comp)
    {
        // do nothing if we're already enabled
        if (enableSubTree)
            return;
        
        if (filter == null || filter.accept(comp))
            enableSubTree = true;
    }
    
    
    @Override
    public void visit(CountRange component)
    {
        component.getComponent(0).accept(this);
        component.getComponent(1).accept(this);
    }
    
    
    @Override
    public void visit(QuantityRange component)
    {
        component.getComponent(0).accept(this);
        component.getComponent(1).accept(this);
    }
    
    
    @Override
    public void visit(TimeRange component)
    {
        component.getComponent(0).accept(this);
        component.getComponent(1).accept(this);
    }
    
    
    @Override
    public void visit(CategoryRange component)
    {
        component.getComponent(0).accept(this);
        component.getComponent(1).accept(this);
    }
    
    
    @Override
    public void visit(DataRecord record)
    {
        addToProcessorTree(new RecordProcessor());        
        for (DataComponent field: record.getFieldList())
        {
            boolean saveEnabled = enableSubTree;
            checkEnabled(field);
            field.accept(this);
            enableSubTree = saveEnabled; // reset flag
        }
        processorStack.pop();
    }


    @Override
    public void visit(Vector vect)
    {
        addToProcessorTree(new RecordProcessor());        
        for (DataComponent coord: vect.getCoordinateList())
            coord.accept(this);        
        processorStack.pop();
    }
    
    
    @Override
    public void visit(DataArray array)
    {
        ArrayProcessor arrayProcessor = new ArrayProcessor();
        arrayProcessor.setArraySize(array.getComponentCount());
        addToProcessorTree(arrayProcessor);        
        array.getElementType().accept(this);        
        processorStack.pop();
    }
    
    
    public void setDataComponents(DataComponent components)
    {
        this.dataComponents = components;
        this.processorTreeReady = false;
    }


    public DataComponent getDataComponents()
    {
        return this.dataComponents;
    }
    
    
    public void setDataComponentFilter(IComponentFilter filter)
    {
        this.filter = filter;
        this.enableSubTree = false;
    }
}
