/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2016 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.vast.util.NumberUtils;
import com.google.gson.stream.JsonWriter;


/**
 * <p>
 * Stream writer implementation for serializing SWE metadata as JSON
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Jun 4, 2016
 */
public class JsonStreamWriter implements XMLStreamWriter, JsonConstants
{
    final static String INDENT1 = "  ";

    protected JsonWriter writer;
    //protected Writer writer;
    protected JsonContext currentContext = new JsonContext();
    protected boolean indent;
    protected int indentSize = 0;
    protected boolean markAttributes = false;

    protected static class JsonContext
    {
        public JsonContext parent;
        public String eltName;
        public boolean firstChild = true;
        public boolean skipParent = false;
        public boolean isArray = false;
        public boolean emptyElement = true;
    }


    public JsonStreamWriter(OutputStream os, String encoding) throws XMLStreamException
    {
        try
        {
            //this.writer = new OutputStreamWriter(os, encoding);
            this.writer = new JsonWriter(new OutputStreamWriter(os, encoding));
            this.writer.setLenient(true);
            this.writer.setSerializeNulls(true);
            this.writer.setIndent("  ");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new XMLStreamException("Cannot instantiate JSON writer", e);
        }
    }


    protected boolean isArray(String namespaceURI, String localName)
    {
        return false;
    }


    protected boolean isObjectElement(String namespaceURI, String localName)
    {
        return false;
    }


    protected boolean isNumericValue(String value)
    {
        return NumberUtils.isNumericExp(value);
    }


    /*protected void indent() throws IOException
    {
        for (int i = 0; i < indentSize; i++)
            writer.write(INDENT1);
    }*/


    protected void pushContext(String eltName)
    {
        JsonContext prevContext = currentContext;
        currentContext = new JsonContext();
        currentContext.parent = prevContext;
        currentContext.eltName = eltName;
    }


    protected void popContext()
    {
        currentContext = currentContext.parent;
    }


    @Override
    public void writeStartDocument() throws XMLStreamException
    {
        /*try
        {
            // just open bracket
            //writer.write('[');
            writer.beginArray();
        }
        catch (IOException e)
        {
            throw new XMLStreamException("Error when starting root object", e);
        }*/
    }


    @Override
    public void writeStartDocument(String version) throws XMLStreamException
    {
        writeStartDocument();
    }


    @Override
    public void writeStartDocument(String encoding, String version) throws XMLStreamException
    {
        writeStartDocument();
    }


    @Override
    public void writeEndDocument() throws XMLStreamException
    {
        try
        {
            // just close bracket
            //writer.write(']');
            //writer.endArray();
            writer.endObject();
        }
        catch (IOException e)
        {
            throw new XMLStreamException("Error when closing root object", e);
        }
    }


    protected void prepareAppendToObject() throws IOException
    {
        if (currentContext.firstChild)
        {
            /*writer.write("{\n");
            indentSize++;*/
            writer.beginObject();
            currentContext.firstChild = false;
        }
        /*else
        {
            writer.write(",\n");
        }*/
    }


    /*protected void writeFieldName(String localName) throws IOException
    {
        writer.write('"');
        writer.write(localName);
        writer.write("\": ");
    }*/


    protected void closeArray() throws IOException
    {
        /*writer.write('\n');
        indentSize--;
        indent();
        writer.write(']');*/
        writer.endArray();
        popContext();
    }


    @Override
    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException
    {
        try
        {
            // need to close current array if element name changes
            if (currentContext.isArray)
            {
                if (!localName.equals(currentContext.eltName))
                    closeArray();
            }

            prepareAppendToObject();
            //indent();
            currentContext.emptyElement = false;

            // special case for object elements: use 'type' in JSON
            if (isObjectElement(namespaceURI, localName))
            {
                /*writeFieldName(OBJECT_TYPE_PROPERTY);
                writer.write('"');
                writer.write(localName);
                writer.write('"');*/
                writer.name(OBJECT_TYPE_PROPERTY);
                writer.value(localName);
                pushContext(localName);
                currentContext.firstChild = false;
                currentContext.skipParent = true;
            }
            else
            {
                if (!currentContext.isArray)
                {
                    //writeFieldName(localName);
                    writer.name(localName);

                    // start JSON array if element has multiplicity > 1
                    if (isArray(namespaceURI, localName))
                    {
                        /*writer.write("[\n");
                        indentSize++;
                        indent();*/
                        writer.beginArray();
                        pushContext(localName);
                        currentContext.firstChild = false;
                        currentContext.isArray = true;
                    }
                }

                pushContext(localName);
            }
        }
        catch (IOException e)
        {
            throw new XMLStreamException("Error starting JSON object " + localName, e);
        }
    }


    @Override
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException
    {
        writeStartElement(null, localName, namespaceURI);
    }


    @Override
    public void writeStartElement(String localName) throws XMLStreamException
    {
        writeStartElement(null, localName, null);
    }


    @Override
    public void writeEmptyElement(String localName) throws XMLStreamException
    {
        try
        {
            /*indent();
            writeFieldName(localName);
            writer.write("null\n");*/
            writer.name(localName);
            writer.nullValue();
        }
        catch (IOException e)
        {
            throw new XMLStreamException("Error writing empty JSON object " + localName, e);
        }
    }


    @Override
    public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException
    {
        // no namespace support -> use only local name
        writeEmptyElement(localName);
    }


    @Override
    public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException
    {
        // no namespace support -> use only local name
        writeEmptyElement(localName);
    }


    @Override
    public void writeEndElement() throws XMLStreamException
    {
        try
        {
            // close current array if end of surrounding object
            if (currentContext.isArray)
                closeArray();
            
            if (!currentContext.skipParent)
            {
                if (!currentContext.firstChild)
                {
                    /*writer.write('\n');
                    indentSize--;
                    indent();
                    writer.write("}");*/
                    writer.endObject();
                }
                
                else if (currentContext.emptyElement)
                {
                    //writer.write("null");
                    writer.nullValue();
                }
            }
            
            writer.flush();
            popContext();
        }
        catch (IOException e)
        {
            throw new XMLStreamException("Error closing JSON object", e);
        }
    }


    protected void writeValue(String value) throws IOException
    {
        boolean isNumber = isNumericValue(value);

        /*if (!isNumber)
            writer.write('"');
        writer.write(value);
        if (!isNumber)
            writer.write('"');*/
        if (isNumber)
            writer.jsonValue(value);
        else
            writer.value(value);
    }


    @Override
    public void writeAttribute(String localName, String value) throws XMLStreamException
    {
        try
        {
            if (markAttributes)
                localName = ATT_PREFIX + localName;
            
            prepareAppendToObject();
            /*indent();            
            writeFieldName(localName);*/
            writer.name(localName);
            writeValue(value);
            
            currentContext.emptyElement = false;
        }
        catch (IOException e)
        {
            throw new XMLStreamException("Error writing attribute " + localName, e);
        }
    }


    @Override
    public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException
    {
        // no namespace support -> use only local name
        writeAttribute(localName, value);
    }


    @Override
    public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException
    {
        // no namespace support -> use only local name
        writeAttribute(localName, value);
    }


    @Override
    public void writeCharacters(String text) throws XMLStreamException
    {
        try
        {
            if (currentContext.firstChild)
                writeValue(text);
            else
            {
                prepareAppendToObject();
                /*indent();
                writeFieldName("value");
                writeValue(text);*/
                writer.name("value");
                writeValue(text);
            }
            
            currentContext.emptyElement = false;
        }
        catch (IOException e)
        {
            throw new XMLStreamException("Error writing JSON value", e);
        }
    }


    @Override
    public void writeCharacters(char[] text, int start, int len) throws XMLStreamException
    {
        writeCharacters(new String(text, start, len));
    }


    @Override
    public NamespaceContext getNamespaceContext()
    {
        // no namespace support
        return null;
    }


    @Override
    public String getPrefix(String uri) throws XMLStreamException
    {
        // no namespace support
        return null;
    }


    @Override
    public void setDefaultNamespace(String uri) throws XMLStreamException
    {
        // no namespace support
    }


    @Override
    public void setNamespaceContext(NamespaceContext context) throws XMLStreamException
    {
        // no namespace support
    }


    @Override
    public void setPrefix(String prefix, String uri) throws XMLStreamException
    {
        // no namespace support
    }


    @Override
    public void writeCData(String data) throws XMLStreamException
    {
        // CData is not supported in JSON
        // ignore silently
    }


    @Override
    public void writeComment(String data) throws XMLStreamException
    {
        // comments are not supported in JSON
        // ignore silently
    }


    @Override
    public void writeDTD(String dtd) throws XMLStreamException
    {
        // no DTD support
        // ignore silently
    }


    @Override
    public void writeEntityRef(String name) throws XMLStreamException
    {
        // entities not supported in JSON
        // ignore silently
    }


    @Override
    public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException
    {
        // no namespace support
        // ignore silently
    }


    @Override
    public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException
    {
        // no namespace support
        // ignore silently
    }


    @Override
    public void writeProcessingInstruction(String target) throws XMLStreamException
    {
        // processing instructions not supported in JSON
        // ignore silently
    }


    @Override
    public void writeProcessingInstruction(String target, String data) throws XMLStreamException
    {
        // processing instructions not supported in JSON
        // ignore silently
    }


    @Override
    public Object getProperty(String name) throws IllegalArgumentException
    {
        return null;
    }


    @Override
    public void flush() throws XMLStreamException
    {
        try
        {
            writer.flush();
        }
        catch (IOException e)
        {
            throw new XMLStreamException("Error when flushing JSON writer", e);
        }
    }


    @Override
    public void close() throws XMLStreamException
    {
        try
        {
            writer.close();
        }
        catch (IOException e)
        {
            throw new XMLStreamException("Error when closing JSON writer", e);
        }
    }
}
