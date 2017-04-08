/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SWE Common Data Framework".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


/**
 * <p>
 * Wrapper providing indenting support for any stream writer
 * </p>
 *
 * @author Alex Robin
 * @since Nov 10, 2014
 */
public class IndentingXMLStreamWriter extends DelegatingXMLStreamWriter
{
    private final static int SEEN_NOTHING = 0;
    private final static int SEEN_ELEMENT = 1;
    private final static int SEEN_DATA = 2;
    private final static String NEW_LINE = "\n";

    private int state = SEEN_NOTHING;
    private String indentStep = "  ";
    private int depth = 0;


    public IndentingXMLStreamWriter(XMLStreamWriter writer)
    {
        super(writer);
    }


    public void setIndentStep(String s)
    {
        this.indentStep = s;
    }


    private void onStartElement() throws XMLStreamException
    {
        state = SEEN_NOTHING;
        
        if (depth > 0)
        {
            writer.writeCharacters(NEW_LINE);
            doIndent();
        }
        
        depth++;
    }


    private void onEndElement() throws XMLStreamException
    {
        depth--;
        
        if (state == SEEN_ELEMENT)
        {
            writer.writeCharacters(NEW_LINE);
            doIndent();
        }
        
        state = SEEN_ELEMENT;
    }


    private void onEmptyElement() throws XMLStreamException
    {
        state = SEEN_ELEMENT;
        
        if (depth > 0)
        {
            writer.writeCharacters(NEW_LINE);
            doIndent();
        }
    }


    private void doIndent() throws XMLStreamException
    {
        if (depth > 0)
        {
            for (int i = 0; i < depth; i++)
                writer.writeCharacters(indentStep);
        }
    }


    @Override
    public void writeStartDocument() throws XMLStreamException
    {
        writer.writeStartDocument();
        writer.writeCharacters(NEW_LINE);
    }


    @Override
    public void writeStartDocument(String version) throws XMLStreamException
    {
        writer.writeStartDocument(version);
        writer.writeCharacters(NEW_LINE);
    }


    @Override
    public void writeStartDocument(String encoding, String version) throws XMLStreamException
    {
        writer.writeStartDocument(encoding, version);
        writer.writeCharacters(NEW_LINE);
    }


    @Override
    public void writeStartElement(String localName) throws XMLStreamException
    {
        onStartElement();
        writer.writeStartElement(localName);
    }


    @Override
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException
    {
        onStartElement();
        writer.writeStartElement(namespaceURI, localName);
    }


    @Override
    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException
    {
        onStartElement();
        writer.writeStartElement(prefix, localName, namespaceURI);
    }


    @Override
    public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException
    {
        onEmptyElement();
        writer.writeEmptyElement(namespaceURI, localName);
    }


    @Override
    public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException
    {
        onEmptyElement();
        writer.writeEmptyElement(prefix, localName, namespaceURI);
    }


    @Override
    public void writeEmptyElement(String localName) throws XMLStreamException
    {
        onEmptyElement();
        writer.writeEmptyElement(localName);
    }


    @Override
    public void writeEndElement() throws XMLStreamException
    {
        onEndElement();
        writer.writeEndElement();
    }
    
    
    @Override
    public void writeEndDocument() throws XMLStreamException
    {
        writer.writeCharacters(NEW_LINE);
        writer.writeEndDocument();
    }


    @Override
    public void writeCharacters(String text) throws XMLStreamException
    {
        state = SEEN_DATA;
        writer.writeCharacters(text);
    }


    @Override
    public void writeCharacters(char[] text, int start, int len) throws XMLStreamException
    {
        state = SEEN_DATA;
        writer.writeCharacters(text, start, len);
    }


    @Override
    public void writeCData(String data) throws XMLStreamException
    {
        state = SEEN_DATA;
        writer.writeCData(data);
    }    
}
