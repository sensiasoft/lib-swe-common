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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.vast.swe.SWEDataTypeUtils;
import org.vast.util.DateTimeFormat;
import org.vast.util.WriterException;
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.TextEncoding;
import net.opengis.swe.v20.Time;


/**
 * <p>
 * New implementation of text data writer with better efficiency since the 
 * write tree is pre-computed during init instead of being re-evaluated
 * while iterating through the component tree.
 * </p>
 *
 * @author Alex Robin
 * @since Dec 7, 2016
 */
public class TextDataWriter extends AbstractDataWriter
{
    Writer writer;
    String tokenSep = ",";
    String blockSep = "\n";
    boolean collapseWhiteSpaces = true;
    boolean firstToken;

    
    protected class BooleanWriter extends BaseProcessor
    {
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            boolean val = data.getBooleanValue(index);
            writeSeparator();
            writer.write(java.lang.Boolean.toString(val));
            return ++index;
        }
    }
    
    
    protected class IntegerWriter extends BaseProcessor
    {
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            int val = data.getIntValue(index);
            writeSeparator();
            writer.write(Integer.toString(val));
            return ++index;
        }
    }
    
    
    protected class DoubleWriter extends BaseProcessor
    {
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            double val = data.getDoubleValue(index);
            writeSeparator();
            writer.write(SWEDataTypeUtils.getDoubleOrInfAsString(val));            
            return ++index;
        }
    }
    
    
    protected class FloatWriter extends BaseProcessor
    {
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            float val = data.getFloatValue(index);
            writeSeparator();
            
            if (Float.isNaN(val))
                writer.write("NaN");
            else if (val == Float.POSITIVE_INFINITY)
                writer.write("+INF");
            else if (val == Float.NEGATIVE_INFINITY)
                writer.write("-INF");
            else
                writer.write(Float.toString(val));
            
            return ++index;
        }
    }
    
    
    protected class RoundingDecimalWriter extends BaseProcessor
    {
        int scale;
        
        public RoundingDecimalWriter(int numDecimalPlaces)
        {
            this.scale = numDecimalPlaces;
        }        
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            double val = data.getDoubleValue(index);
            writeSeparator();
            
            if (Double.isNaN(val))
                writer.write("NaN");
            else if (val == Double.POSITIVE_INFINITY)
                writer.write("+INF");
            else if (val == Double.NEGATIVE_INFINITY)
                writer.write("-INF");
            else
            {
                BigDecimal bd = BigDecimal.valueOf(val);
                bd = bd.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();
                writer.write(bd.toPlainString());
            }
            
            return ++index;
        }
    }
    
    
    protected class IsoDateTimeWriter extends BaseProcessor
    {
        DateTimeFormat timeFormat = new DateTimeFormat();
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            double val = data.getDoubleValue(index);
            writeSeparator();
            writer.write(timeFormat.formatIso(val, 0));
            return ++index;
        }
    }
    
    
    protected class StringWriter extends BaseProcessor
    {
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            String val = data.getStringValue(index);
            if (collapseWhiteSpaces)
                val = val.trim();
            writeSeparator();
            writer.write(val);
            return ++index;
        }
    }    
    
    
    protected class ChoiceTokenWriter extends ChoiceProcessor
    {
        ArrayList<String> choiceTokens;
        
        public ChoiceTokenWriter(DataChoice choice)
        {
            choiceTokens = new ArrayList<String>(choice.getNumItems());
            for (DataComponent item: choice.getItemList())
                choiceTokens.add(item.getName());
        }
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            int selectedIndex = data.getIntValue(index);
            if (selectedIndex < 0 || selectedIndex >= choiceTokens.size())
                throw new WriterException(AbstractDataParser.INVALID_CHOICE_MSG + selectedIndex);
            
            writeSeparator();
            writer.write(choiceTokens.get(selectedIndex));
            return super.process(data, ++index, selectedIndex);
        }
    }
    
    
    protected class ArraySizeWriter extends BaseProcessor
    {
        ArrayProcessor arrayProcessor;
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            int arraySize = data.getIntValue(index);
            arrayProcessor.arraySize = arraySize;
            writer.write(Integer.toString(arraySize));
            return ++index;
        }
    }
    
    
    protected void writeSeparator() throws IOException
    {
        if (!firstToken)
            writer.write(tokenSep);
        else
            firstToken = false;
    }
    
    
    @Override
    protected void init()
    {
        this.tokenSep = ((TextEncoding)dataEncoding).getTokenSeparator();
        this.blockSep = ((TextEncoding)dataEncoding).getBlockSeparator();
        //this.decimalSep = ((TextEncoding)dataEncoding).getDecimalSeparator().charAt(0);
        this.collapseWhiteSpaces = ((TextEncoding)dataEncoding).getCollapseWhiteSpaces();
    }
    
    
    @Override
    public void setOutput(OutputStream os)
    {
        this.writer = new OutputStreamWriter(os, StandardCharsets.UTF_8);
    }
    
    
    @Override
    public void write(DataBlock data) throws IOException
    {
        firstToken = true;
        super.write(data);
        writer.write(blockSep);
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
    public void visit(Boolean comp)
    {
        addToProcessorTree(new BooleanWriter());
    }
    
    
    @Override
    public void visit(Count comp)
    {
        addToProcessorTree(new IntegerWriter());
    }
    
    
    @Override
    public void visit(Quantity comp)
    {
        if (comp.getConstraint() != null && comp.getConstraint().isSetSignificantFigures())
        {
            int sigFigures = comp.getConstraint().getSignificantFigures(); 
            addToProcessorTree(new RoundingDecimalWriter(sigFigures));
        }
        else if (comp.getDataType() == DataType.FLOAT)
            addToProcessorTree(new FloatWriter());
        else
            addToProcessorTree(new DoubleWriter());
    }
    
    
    @Override
    public void visit(Time comp)
    {
        if (!comp.isIsoTime())
        {
            if (comp.getConstraint() != null && comp.getConstraint().isSetSignificantFigures())
            {
                int sigFigures = comp.getConstraint().getSignificantFigures(); 
                addToProcessorTree(new RoundingDecimalWriter(sigFigures));
            }
            else
                addToProcessorTree(new DoubleWriter());
        }
        else
            addToProcessorTree(new IsoDateTimeWriter());
    }
    
    
    @Override
    public void visit(Category comp)
    {
        addToProcessorTree(new StringWriter());
    }
    
    
    @Override
    public void visit(Text comp)
    {
        addToProcessorTree(new StringWriter());
    }
    
    
    @Override
    public void visit(DataArray array)
    {
        if (array.isImplicitSize())
        {
            ArrayProcessor arrayProcessor = new ArrayProcessor();
            ArraySizeWriter sizeWriter = new ArraySizeWriter();
            sizeWriter.arrayProcessor = arrayProcessor;
            addToProcessorTree(sizeWriter);
            addToProcessorTree(arrayProcessor);
            array.getElementType().accept(this);        
            processorStack.pop();
        }
        else
            super.visit(array);
    }
    
    
    @Override
    public void visit(DataChoice choice)
    {
        addToProcessorTree(new ChoiceTokenWriter(choice));
        for (DataComponent field: choice.getItemList())
            field.accept(this);        
        processorStack.pop();
    }
}
