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
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.vast.data.AbstractArrayImpl;
import org.vast.data.XMLEncodingImpl;
import org.vast.swe.SWEDataTypeUtils;
import org.vast.util.DateTimeFormat;
import org.vast.util.WriterException;
import org.vast.xml.IndentingXMLStreamWriter;
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.Vector;


/**
 * <p>
 * New implementation of XML data writer with better efficiency since the 
 * write tree is pre-computed during init instead of being re-evaluated
 * while iterating through the component tree.
 * </p>
 *
 * @author Alex Robin
 * @since Dec 7, 2016
 */
public class XmlDataWriter extends AbstractDataWriter
{
    static final String XML_ERROR = "Error writing XML stream for ";
    protected XMLStreamWriter xmlWriter;
    protected String namespace;
    protected String prefix;

    
    protected abstract class ValueWriter extends BaseProcessor
    {
        String eltName;
        
        public abstract void writeValue(DataBlock data, int index) throws XMLStreamException;
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            try
            {
                writeStartElement(eltName);
                writeValue(data, index);
                xmlWriter.writeEndElement();
                return ++index;
            }
            catch (XMLStreamException e)
            {
                throw new WriterException(XML_ERROR + eltName + " value", e);
            }
        }
    }
    
    
    protected class BooleanWriter extends ValueWriter
    {
        public BooleanWriter(String eltName)
        {
            this.eltName = eltName;
        }
        
        @Override
        public void writeValue(DataBlock data, int index) throws XMLStreamException
        {
            boolean val = data.getBooleanValue(index);
            xmlWriter.writeCharacters(java.lang.Boolean.toString(val));
        }
    }
    
    
    protected class IntegerWriter extends ValueWriter
    {
        public IntegerWriter(String eltName)
        {
            this.eltName = eltName;
        }
        
        @Override
        public void writeValue(DataBlock data, int index) throws XMLStreamException
        {
            int val = data.getIntValue(index);
            xmlWriter.writeCharacters(Integer.toString(val));
        }
    }
    
    
    protected class DecimalWriter extends ValueWriter
    {
        public DecimalWriter(String eltName)
        {
            this.eltName = eltName;
        }
        
        @Override
        public void writeValue(DataBlock data, int index) throws XMLStreamException
        {
            double val = data.getDoubleValue(index);
            xmlWriter.writeCharacters(SWEDataTypeUtils.getDoubleOrInfAsString(val));
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
        public void writeValue(DataBlock data, int index) throws XMLStreamException
        {
            double val = data.getDoubleValue(index);
            xmlWriter.writeCharacters(timeFormat.formatIso(val, 0));
        }
    }
    
    
    protected class StringWriter extends ValueWriter
    {
        public StringWriter(String eltName)
        {
            this.eltName = eltName;
        }
        
        @Override
        public void writeValue(DataBlock data, int index) throws XMLStreamException
        {
            String val = data.getStringValue(index);
            xmlWriter.writeCharacters(val);
        }
    }
    
    
    protected class RecordWriter extends RecordProcessor
    {
        String eltName;
        
        public RecordWriter(String eltName)
        {
            this.eltName = eltName;
        }
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            try
            {
                writeStartElement(eltName);
                int newIndex = super.process(data, index);
                xmlWriter.writeEndElement();
                return newIndex;
            }
            catch (XMLStreamException e)
            {
                throw new WriterException(XML_ERROR + eltName + " record", e);
            }            
        }
    }
    
    
    protected class ChoiceWriter extends ChoiceProcessor
    {
        String eltName;
        
        public ChoiceWriter(String eltName)
        {
            this.eltName = eltName;
        }
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            int selectedIndex = data.getIntValue(index);
            
            try
            {
                writeStartElement(eltName);
                int newIndex = super.process(data, ++index, selectedIndex);
                xmlWriter.writeEndElement();
                return newIndex;
            }
            catch (XMLStreamException e)
            {
                throw new WriterException(XML_ERROR + eltName + " choice", e);
            }            
        }
    }
    
    
    protected class ArrayWriter extends ArrayProcessor
    {
        String eltName;
        
        public ArrayWriter(String eltName)
        {
            this.eltName = eltName;
        }
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            try
            {
                writeStartElement(eltName);
                xmlWriter.writeAttribute(AbstractArrayImpl.ELT_COUNT_NAME, Integer.toString(arraySize));
                int newIndex = super.process(data, index);
                xmlWriter.writeEndElement();
                return newIndex;
            }
            catch (XMLStreamException e)
            {
                throw new WriterException(XML_ERROR + eltName + " array", e);
            }
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
    
    
    protected void writeStartElement(String eltName) throws XMLStreamException
    {
        if (namespace != null)
        {
            if (prefix != null)
                xmlWriter.writeStartElement(prefix, eltName, namespace);
            else
                xmlWriter.writeStartElement(namespace, eltName);
        }
        else
            xmlWriter.writeStartElement(eltName);
    }
    
    
    @Override
    protected void init()
    {
        namespace = ((XMLEncodingImpl)dataEncoding).getNamespace();
        prefix = ((XMLEncodingImpl)dataEncoding).getPrefix();
    }
    
    
    @Override
    public void setOutput(OutputStream os) throws IOException
    {
        try
        {
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            xmlWriter = factory.createXMLStreamWriter(os);
            xmlWriter = new IndentingXMLStreamWriter(xmlWriter);
            xmlWriter.writeStartElement("root");
        }
        catch (XMLStreamException e)
        {
            throw new WriterException("Error while creating XML stream writer", e);
        }
    }
    

    @Override
    public void flush() throws IOException
    {
        try
        {
            if (xmlWriter != null)
                xmlWriter.flush();
        }
        catch (XMLStreamException e)
        {
            throw new WriterException("Error while flushing XML stream writer", e);
        }
    }
    

    @Override
    public void close() throws IOException
    {
        try
        {
            if (xmlWriter != null)
                xmlWriter.close();
        }
        catch (XMLStreamException e)
        {
            throw new WriterException("Error while closing XML stream writer", e);
        }
    }
    
    
    @Override
    public void visit(Boolean comp)
    {
        addToProcessorTree(new BooleanWriter(comp.getName()));
    }
    
    
    @Override
    public void visit(Count comp)
    {
        addToProcessorTree(new IntegerWriter(comp.getName()));
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
    public void visit(DataRecord rec)
    {
        addToProcessorTree(new RecordWriter(rec.getName()));
        for (DataComponent field: rec.getFieldList())
        {
            field.accept(this);
            checkEnabled(field);
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
        addToProcessorTree(new ChoiceWriter(choice.getName()));
        for (DataComponent field: choice.getItemList())
            field.accept(this);        
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
        else
            arrayWriter.setArraySize(array.getComponentCount());
        
        addToProcessorTree(arrayWriter);        
        array.getElementType().accept(this);        
        processorStack.pop();
    }
}
