/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamReader;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;


/**
 * <p>
 * Stream reader implementation for unmarshalling SWE metadata from JSON.<br/>
 * This transforms JSON parsing events into XML parsing events so it can be 
 * used with existing StAX parsers.
 * </p>
 *
 * @author Alex Robin
 * @since Jan 26, 2017
 */
public class JsonStreamReader implements XMLStreamReader, JsonConstants
{
    private final static String DEFAULT_XML_VERSION = "1.0";
    private final static String DEFAULT_XML_ENCODING = "UTF-8";
        
    protected JsonReader reader;
    protected String inputEncoding;
    protected JsonContext currentContext = new JsonContext();    
    protected int eventType;
    protected String nextName = "root";
    protected ArrayList<String> attNames = new ArrayList<String>();
    protected ArrayList<String> attValues = new ArrayList<String>();
    
    
    protected static class JsonContext
    {
        public JsonContext parent;
        public String eltName;
        public String text;
        public boolean isArray;
        public boolean isObjectType;
        public boolean isTextOnly;
        public boolean consumeText;
        public boolean insertObject;
    }
    
    
    public JsonStreamReader(InputStream is, String encoding) throws JsonStreamException
    {
        try
        {
            this.inputEncoding = encoding;
            this.reader = new JsonReader(new InputStreamReader(is, encoding));
        }
        catch (UnsupportedEncodingException e)
        {
            throw new JsonStreamException("Cannot instantiate JSON parser", e);
        }
    }
    
    
    protected boolean isXmlAttribute(String name)
    {
        return (name.charAt(0) == ATT_PREFIX);
    }
    
    
    protected boolean isInlineValue(String name)
    {
        return false;
    }
    
    
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
        nextName = null;
    }
    
    
    protected void clearContext()
    {
        eventType = 0;
        attNames.clear();
        attValues.clear();
    }
    
    
    @Override
    public int next() throws JsonStreamException
    {
        if (currentContext != null && currentContext.consumeText)
        {
            currentContext.consumeText = false;
            eventType = CHARACTERS;
            return eventType;
        }
        
        return nextTag();
    }


    @Override
    public int nextTag() throws JsonStreamException
    {
        // if it was a text only element, generate close event
        if (currentContext.isTextOnly)
        {
            popContext();
            eventType = END_ELEMENT;
            return eventType;
        }
        
        clearContext();
        
        try
        {
            boolean isAtt = false;
            JsonToken nextToken;
            if (nextName == null)
                nextName = currentContext.eltName;
                        
            while ((nextToken = reader.peek()) != JsonToken.END_DOCUMENT)
            {
                switch (nextToken)
                {
                    case BEGIN_ARRAY:
                        reader.beginArray();
                        pushContext(nextName);
                        currentContext.isArray = true;
                        break;
                        
                    case END_ARRAY:
                        reader.endArray();
                        popContext();
                        break;
                    
                    case BEGIN_OBJECT:
                        reader.beginObject();
                        pushContext(nextName);
                        eventType = START_ELEMENT;
                        break;
                    
                    case END_OBJECT:
                        // don't consume close if we're ending the 'virtual' object element
                        if (!currentContext.isObjectType)
                            reader.endObject();
                        popContext();
                        eventType = END_ELEMENT;
                        return eventType;
                        
                    case NAME:
                        nextName = reader.nextName();
                        isAtt = false;
                        
                        // accumulate attributes
                        if (reader.peek() != JsonToken.BEGIN_OBJECT &&
                            reader.peek() != JsonToken.BEGIN_ARRAY &&
                            isXmlAttribute(nextName))
                        {
                            if (nextName.charAt(0) == ATT_PREFIX)
                                nextName = nextName.substring(1);
                            attNames.add(nextName);
                            isAtt = true;                            
                        }
                        
                        else if (nextName.equals(OBJECT_TYPE_PROPERTY))
                        {
                            currentContext.insertObject = true;
                            return eventType;
                        }
                        
                        // if no more attributes send element start event
                        else if (eventType == START_ELEMENT)
                        {
                            return eventType;
                        }
                        
                        break;
                                                
                    case STRING:
                    case NUMBER:
                    case BOOLEAN:                    
                        String val = reader.nextString();
                        
                        // case of attribute
                        if (isAtt)
                        {
                            attValues.add(val);
                            // finish start element tag if there end of object
                            if (reader.peek() == JsonToken.END_OBJECT)
                                return eventType;
                        }
                        
                        // if we have to insert an object element
                        else if (currentContext.insertObject)
                        {
                            currentContext.insertObject = false;
                            pushContext(val);
                            currentContext.isObjectType = true;
                            eventType = START_ELEMENT;
                            if (reader.peek() == JsonToken.END_OBJECT)
                                return eventType;
                        }
                        
                        // case of inline value
                        else if (isInlineValue(currentContext.eltName))
                        {
                            currentContext.text = val;
                            currentContext.consumeText = true;
                            return eventType;
                        }
                        
                        // case of text only element
                        else
                        {
                            pushContext(nextName);
                            currentContext.text = val;
                            currentContext.isTextOnly = true;
                            currentContext.consumeText = true;
                            eventType = START_ELEMENT;
                            return eventType;
                        }
                        break;
                        
                    case NULL:
                        reader.nextNull();
                        if (isAtt)
                            break;
                        pushContext(nextName);
                        currentContext.isTextOnly = true;
                        eventType = START_ELEMENT;
                        return eventType;
                        
                    case END_DOCUMENT:
                        return END_DOCUMENT;
                }
            }
            
            return 0;
        }
        catch (IOException e)
        {
            throw new JsonStreamException("Cannot parse next JSON element", e);
        }
    }
    
    
    @Override
    public int getAttributeCount()
    {
        return attNames.size();
    }


    @Override
    public String getAttributeLocalName(int index)
    {
        return attNames.get(index);
    }


    @Override
    public String getAttributeValue(int index)
    {
        return attValues.get(index);
    }


    @Override
    public QName getAttributeName(int index)
    {
        return new QName(getAttributeLocalName(index));
    }


    @Override
    public String getAttributeNamespace(int index)
    {
        return null;
    }


    @Override
    public String getAttributePrefix(int index)
    {
        return null;
    }


    @Override
    public String getAttributeType(int index)
    {
        return null;
    }


    @Override
    public String getAttributeValue(String namespaceURI, String localName)
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public String getElementText() throws JsonStreamException
    {
        if (currentContext.text == null)
            throw new IllegalStateException();
        return currentContext.text;
    }


    @Override
    public int getEventType()
    {
        return eventType;
    }


    @Override
    public String getLocalName()
    {
        if (currentContext.eltName == null)
            throw new IllegalStateException();
        return currentContext.eltName;
    }


    @Override
    public QName getName()
    {
        return new QName(getLocalName());
    }


    @Override
    public NamespaceContext getNamespaceContext()
    {
        return new NamespaceContext() {

            @Override
            public String getNamespaceURI(String prefix)
            {
                return XMLConstants.NULL_NS_URI;
            }

            @Override
            public String getPrefix(String namespaceURI)
            {
                return null;
            }

            @Override
            public Iterator<String> getPrefixes(String namespaceURI)
            {
                return Collections.EMPTY_LIST.iterator();
            }            
        };
    }


    @Override
    public int getNamespaceCount()
    {
        return 0;
    }


    @Override
    public String getNamespacePrefix(int index)
    {
        if (currentContext.eltName == null)
            throw new IllegalStateException();
        return null;
    }


    @Override
    public String getNamespaceURI()
    {
        return null;
    }


    @Override
    public String getNamespaceURI(String prefix)
    {
        return null;
    }


    @Override
    public String getNamespaceURI(int index)
    {
        return null;
    }


    @Override
    public String getPIData()
    {
        return null;
    }


    @Override
    public String getPITarget()
    {
        return null;
    }


    @Override
    public String getPrefix()
    {
        return null;
    }


    @Override
    public Object getProperty(String name) throws IllegalArgumentException
    {
        return null;
    }


    @Override
    public String getText()
    {
        if (currentContext.text == null)
            throw new IllegalStateException();
        return currentContext.text;
    }


    @Override
    public char[] getTextCharacters()
    {
        return getText().toCharArray();
    }


    @Override
    public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws JsonStreamException
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public int getTextLength()
    {
        return getText().length();
    }


    @Override
    public int getTextStart()
    {
        return 0;
    }


    @Override
    public boolean hasName()
    {
        return (currentContext.eltName != null);
    }


    @Override
    public boolean hasNext() throws JsonStreamException
    {
        try
        {
            return (reader.peek() != JsonToken.END_DOCUMENT);
        }
        catch (IOException e)
        {
            throw new JsonStreamException(e);
        }
    }


    @Override
    public boolean hasText()
    {
        return (currentContext.text != null);
    }


    @Override
    public boolean isAttributeSpecified(int index)
    {
        return (attNames.size() > index);
    }


    @Override
    public boolean isCharacters()
    {
        return (currentContext.text != null);
    }


    @Override
    public boolean isEndElement()
    {
        return (eventType == END_ELEMENT);
    }


    @Override
    public boolean isStartElement()
    {
        return (eventType == START_ELEMENT);
    }


    @Override
    public boolean isWhiteSpace()
    {
        return false;
    }
    

    @Override
    public void require(int type, String namespaceURI, String localName) throws JsonStreamException
    {
        if (type != eventType || (currentContext != null && !localName.equals(currentContext.eltName)))
            throw new JsonStreamException("Unexpected JSON data");
    }


    @Override
    public String getVersion()
    {
        return DEFAULT_XML_VERSION;
    }


    @Override
    public String getCharacterEncodingScheme()
    {
        return DEFAULT_XML_ENCODING;
    }


    @Override
    public boolean isStandalone()
    {
        return false;
    }
    
    
    @Override
    public boolean standaloneSet()
    {
        return false;
    }


    @Override
    public Location getLocation()
    {
        return null;
    }


    @Override
    public String getEncoding()
    {
        return inputEncoding;
    }
    
    
    @Override
    public void close() throws JsonStreamException
    {
        try
        {
            reader.close();
        }
        catch (IOException e)
        {
            throw new JsonStreamException(e);
        }
    }

}
