/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.fast;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.vast.swe.SWEDataTypeUtils;
import org.vast.util.DateTimeFormat;
import org.vast.util.WriterException;
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.CategoryRange;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.CountRange;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.QuantityRange;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.TimeRange;
import net.opengis.swe.v20.Vector;


/**
 * <p>
 * New implementation of JSON data writer with better efficiency since the 
 * write tree is pre-computed during init instead of being re-evaluated
 * while iterating through the component tree.
 * </p>
 *
 * @author Alex Robin
 * @since Dec 7, 2016
 */
public class JsonDataWriter extends AbstractDataWriter
{
    static final String JSON_ERROR = "Error writing JSON stream for ";
    static final String INDENT = "  ";
    
    protected Writer writer;
    protected NullWriter nullWriter = new NullWriter();
    protected int depth;
    boolean multipleRecords;
    boolean firstBlock = true;
    Map<String, IntegerWriter> countWriters = new HashMap<>();
    
    
    protected interface JsonWriter
    {
        String getEltName();
    }
    
    
    protected abstract class ValueWriter extends BaseProcessor implements JsonWriter
    {
        String eltName;
        
        public abstract void writeValue(DataBlock data, int index) throws IOException;
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            try
            {
                writeValue(data, index);
                return ++index;
            }
            catch (IOException e)
            {
                throw new WriterException(JSON_ERROR + eltName + " value", e);
            }
        }
        
        @Override
        public String getEltName()
        {
            return eltName;
        }
    }
    
    
    protected class BooleanWriter extends ValueWriter
    {
        public BooleanWriter(String eltName)
        {
            this.eltName = eltName;
        }
        
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            boolean val = data.getBooleanValue(index);
            writer.write(java.lang.Boolean.toString(val));
        }
    }
    
    
    protected class IntegerWriter extends ValueWriter
    {
        int val;
        
        public IntegerWriter(String eltName)
        {
            this.eltName = eltName;
        }
        
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            val = data.getIntValue(index);
            writer.write(Integer.toString(val));
        }
    }
    
    
    protected class DecimalWriter extends ValueWriter
    {
        public DecimalWriter(String eltName)
        {
            this.eltName = eltName;
        }
        
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            double val = data.getDoubleValue(index);
            String stringVal = SWEDataTypeUtils.getDoubleOrInfAsString(val);
            
            // need to add quote on special values because they are not valid literal values in JSON
            if (Double.isNaN(val) || Double.isInfinite(val))
            {
                writer.write('"');
                writer.write(stringVal);
                writer.write('"');
            }
            else
                writer.write(stringVal);
        }
    }
    
    
    protected class RoundingDecimalWriter extends DecimalWriter
    {
        public RoundingDecimalWriter(String eltName, int sigDigits)
        {
            super(eltName);
        }
    }
    
    
    protected class IsoDateTimeWriter extends ValueWriter
    {
        DateTimeFormat timeFormat = new DateTimeFormat();
        
        public IsoDateTimeWriter(String eltName)
        {
            this.eltName = eltName;
        }
        
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            double val = data.getDoubleValue(index);
            writer.write('"');
            if (Double.isNaN(val) || Double.isInfinite(val))
                writer.write(SWEDataTypeUtils.getDoubleOrInfAsString(val));
            else
                writer.write(timeFormat.formatIso(val, 0));
            writer.write('"');
        }
    }
    
    
    protected class StringWriter extends ValueWriter
    {
        public StringWriter(String eltName)
        {
            this.eltName = eltName;
        }
        
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            String val = data.getStringValue(index);
            
            if (val != null)
            {
                writer.write('"');
                writer.write(val);
                writer.write('"');
            }
            else
                writer.write("null");
        }
    }
    
    
    protected class RangeWriter extends RecordProcessor implements JsonWriter
    {
        String eltName;
        
        public RangeWriter(String eltName)
        {
            this.eltName = eltName;
        }
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            try
            {
                writer.write('[');
                fieldProcessors.get(0).process(data, index);
                writer.write(", ");
                fieldProcessors.get(1).process(data, index);
                writer.write(']');                
                return index;
            }
            catch (IOException e)
            {
                throw new WriterException(JSON_ERROR + eltName + " range", e);
            }
        }
        
        @Override
        public String getEltName()
        {
            return eltName;
        }
    }
    
    
    protected class RecordWriter extends RecordProcessor implements JsonWriter
    {
        String eltName;
        boolean onlyScalars = true;
        
        public RecordWriter(String eltName)
        {
            this.eltName = eltName;
        }
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            try
            {
                writer.write('{');
                
                depth++;
                int i = 0;                
                for (AtomProcessor p: fieldProcessors)
                {
                    // switch to null writer if this field should be skipped
                    Writer oldWriter = writer;
                    if (!p.isEnabled())
                        writer = nullWriter;
                    
                    // insert separator
                    if (i > 0)
                        writer.write(',');
                    
                    // indent only if child is complex
                    if (!onlyScalars)
                    {
                        writer.write('\n');
                        indent();
                    }
                    else if (i > 0)
                        writer.write(' ');
                    
                    writer.append('"').append(((JsonWriter)p).getEltName()).append('"');
                    writer.write(": ");
                    index = p.process(data, index);
                    i++;
                    
                    writer = oldWriter; // restore old writer
                }
                
                depth--;
                if (!onlyScalars)
                {
                    writer.write('\n');
                    indent();
                }
                writer.write('}');
                
                return index;
            }
            catch (IOException e)
            {
                throw new WriterException(JSON_ERROR + eltName + " record", e);
            }
        }
        
        @Override
        public void add(AtomProcessor processor)
        {
            fieldProcessors.add(processor);
            if (!(processor instanceof ValueWriter))
                onlyScalars = false;
        }
        
        @Override
        public String getEltName()
        {
            return eltName;
        }
    }
    
    
    protected class ArrayWriter extends ArrayProcessor implements JsonWriter
    {
        String eltName;
        IntegerWriter sizeWriter;
        boolean onlyScalars = true;
        
        public ArrayWriter(String eltName)
        {
            this.eltName = eltName;
        }
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            try
            {
                // retrieve variable array size
                if (sizeWriter != null)
                    arraySize = sizeWriter.val;
                
                writer.write('[');
                
                depth++;                
                for (int i = 0; i < arraySize; i++)
                {
                    // insert separator
                    if (i > 0)
                        writer.write(',');
                    
                    // indent only if child is complex
                    if (!onlyScalars)
                    {
                        writer.write('\n');
                        indent();
                    }
                    else if (i > 0)
                        writer.write(' ');
                    
                    index = eltProcessor.process(data, index);
                }
                
                depth--;
                if (!onlyScalars)
                {
                    writer.write('\n');
                    indent();
                }
                writer.write(']');
                
                return index;
            }
            catch (IOException e)
            {
                throw new WriterException(JSON_ERROR + eltName + " array", e);
            }
        }
        
        @Override
        public void add(AtomProcessor processor)
        {
            super.add(processor);
            if (!(processor instanceof ValueWriter))
                onlyScalars = false;
        }
        
        @Override
        public String getEltName()
        {
            return eltName;
        }
    }
    
    
    protected class ChoiceWriter extends ChoiceProcessor implements JsonWriter
    {
        String eltName;
        boolean onlyScalars = false;
        ArrayList<String> choiceTokens;
        
        public ChoiceWriter(DataChoice choice)
        {
            this.eltName = choice.getName();
            choiceTokens = new ArrayList<>(choice.getNumItems());
            for (DataComponent item: choice.getItemList())
                choiceTokens.add(item.getName());
        }
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            int selectedIndex = data.getIntValue(index);
            if (selectedIndex < 0 || selectedIndex >= choiceTokens.size())
                throw new WriterException(AbstractDataParser.INVALID_CHOICE_MSG + selectedIndex);
            
            try
            {
                writer.write('{');
                
                depth++;
                if (!onlyScalars)
                {
                    writer.write('\n');
                    indent();
                }
                
                writer.append('"').append(choiceTokens.get(selectedIndex)).append('"');
                writer.write(": ");
                index = super.process(data, ++index, selectedIndex);
                                
                depth--;
                if (!onlyScalars)
                {
                    writer.write('\n');
                    indent();
                }
                writer.write('}');
                
                return index;
            }
            catch (IOException e)
            {
                throw new WriterException(JSON_ERROR + eltName + " choice", e);
            }            
        }
        
        @Override
        public String getEltName()
        {
            return eltName;
        }
    }
    
    
    protected class ArraySizeScanner extends BaseProcessor
    {
        ArrayProcessor arrayProcessor;
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            int arraySize = data.getIntValue(index);
            arrayProcessor.arraySize = arraySize;
            return ++index;
        }
    }
    
    
    protected void indent() throws IOException
    {
        for (int i = 0; i < depth; i++)
            writer.write(INDENT);
    }


    @Override
    protected void init()
    {        
    }
    
    
    @Override
    public void setOutput(OutputStream os)
    {
        this.writer = new OutputStreamWriter(os, StandardCharsets.UTF_8);
    }
    
    
    @Override
    public void write(DataBlock data) throws IOException
    {
        if (!firstBlock)
            writer.write(",\n");
        
        indent();
        super.write(data);
        
        if (multipleRecords)
            firstBlock = false;
    }
    
    
    @Override
    public void startStream(boolean multipleRecords) throws IOException
    {
        this.multipleRecords = multipleRecords;
        
        // wrap records with array if we're writing multiple ones together
        if (multipleRecords)
        {
            writer.write("[\n");
            depth++;
        }
    }
    
    
    @Override
    public void endStream() throws IOException
    {        
        if (multipleRecords)
        {
            writer.write("\n]");
            flush();
        }
    }
    

    @Override
    public void flush() throws IOException
    {
        if (writer != null)
            writer.flush();
    }
    

    @Override
    public void close() throws IOException
    {
        if (writer != null)
            writer.close();
    }
    
    
    @Override
    public void reset()
    {
        super.reset();
        firstBlock = true;
    }
    
    
    @Override
    public void visit(Boolean comp)
    {
        addToProcessorTree(new BooleanWriter(comp.getName()));
    }
    
    
    @Override
    public void visit(Count comp)
    {
        IntegerWriter writer = new IntegerWriter(comp.getName());
        if (comp.isSetId())
            countWriters.put(comp.getId(), writer);
        addToProcessorTree(writer);
    }
    
    
    @Override
    public void visit(Quantity comp)
    {
        if (comp.getConstraint() != null && comp.getConstraint().isSetSignificantFigures())
        {
            int sigFigures = comp.getConstraint().getSignificantFigures(); 
            addToProcessorTree(new RoundingDecimalWriter(comp.getName(), sigFigures));
        }
        else
            addToProcessorTree(new DecimalWriter(comp.getName()));
    }
    
    
    @Override
    public void visit(Time comp)
    {
        if (!comp.isIsoTime())
        {
            if (comp.getConstraint() != null && comp.getConstraint().isSetSignificantFigures())
            {
                int sigFigures = comp.getConstraint().getSignificantFigures(); 
                addToProcessorTree(new RoundingDecimalWriter(comp.getName(), sigFigures));
            }
            else
                addToProcessorTree(new DecimalWriter(comp.getName()));
        }
        else
            addToProcessorTree(new IsoDateTimeWriter(comp.getName()));
    }
    
    
    @Override
    public void visit(Category comp)
    {
        addToProcessorTree(new StringWriter(comp.getName()));
    }
    
    
    @Override
    public void visit(Text comp)
    {
        addToProcessorTree(new StringWriter(comp.getName()));
    }
    
    
    @Override
    public void visit(CountRange range)
    {
        addToProcessorTree(new RangeWriter(range.getName()));
        range.getComponent(0).accept(this);
        range.getComponent(1).accept(this);
        processorStack.pop();
    }
    
    
    @Override
    public void visit(QuantityRange range)
    {
        addToProcessorTree(new RangeWriter(range.getName()));
        range.getComponent(0).accept(this);
        range.getComponent(1).accept(this);
        processorStack.pop();
    }
    
    
    @Override
    public void visit(TimeRange range)
    {
        addToProcessorTree(new RangeWriter(range.getName()));
        range.getComponent(0).accept(this);
        range.getComponent(1).accept(this);
        processorStack.pop();
    }
    
    
    @Override
    public void visit(CategoryRange range)
    {
        addToProcessorTree(new RangeWriter(range.getName()));
        range.getComponent(0).accept(this);
        range.getComponent(1).accept(this);
        processorStack.pop();
    }
    
    
    @Override
    public void visit(DataRecord rec)
    {
        addToProcessorTree(new RecordWriter(rec.getName()));
        for (DataComponent field: rec.getFieldList())
        {
            boolean saveEnabled = enableSubTree;
            checkEnabled(field);
            field.accept(this);
            enableSubTree = saveEnabled; // reset flag
        }
        processorStack.pop();
    }
    
    
    @Override
    public void visit(Vector rec)
    {
        addToProcessorTree(new RecordWriter(rec.getName()));
        for (DataComponent field: rec.getCoordinateList())
            field.accept(this);
        processorStack.pop();
    }
    
    
    @Override
    public void visit(DataChoice choice)
    {
        addToProcessorTree(new ChoiceWriter(choice));
        for (DataComponent item: choice.getItemList())
            item.accept(this);
        processorStack.pop();
    }
    
    
    @Override
    public void visit(DataArray array)
    {
        ArrayWriter arrayWriter = new ArrayWriter(array.getName());
        
        if (array.isImplicitSize())
        {
            ArraySizeScanner sizeWriter = new ArraySizeScanner();
            sizeWriter.arrayProcessor = arrayWriter;
            addToProcessorTree(sizeWriter);
        }
        else if (array.isVariableSize())
        {
            // look for size writer
            String refId = array.getArraySizeComponent().getId();
            arrayWriter.sizeWriter = countWriters.get(refId);
        }
        else
            arrayWriter.setArraySize(array.getComponentCount());
        
        addToProcessorTree(arrayWriter);
        array.getElementType().accept(this);
        processorStack.pop();
    }
}
