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

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


/**
 * <p>
 * Abstract base for delegating XML stream writers.<br/>
 * By default, all methods are delegated to the underlying writer.
 * </p>
 *
 * @author Alex Robin
 * @since Nov 10, 2014
 */
public abstract class DelegatingXMLStreamWriter implements XMLStreamWriter
{
    final XMLStreamWriter writer;


    protected DelegatingXMLStreamWriter(XMLStreamWriter writer)
    {
        this.writer = writer;
    }
    
    
    @Override
    public void close() throws XMLStreamException
    {
        writer.close();
    }


    @Override
    public void flush() throws XMLStreamException
    {
        writer.flush();
    }


    @Override
    public NamespaceContext getNamespaceContext()
    {
        return writer.getNamespaceContext();
    }


    @Override
    public String getPrefix(String arg0) throws XMLStreamException
    {
        return writer.getPrefix(arg0);
    }


    @Override
    public Object getProperty(String arg0) throws IllegalArgumentException
    {
        return writer.getProperty(arg0);
    }


    @Override
    public void setDefaultNamespace(String arg0) throws XMLStreamException
    {
        writer.setDefaultNamespace(arg0);
    }


    @Override
    public void setNamespaceContext(NamespaceContext arg0) throws XMLStreamException
    {
        writer.setNamespaceContext(arg0);
    }


    @Override
    public void setPrefix(String arg0, String arg1) throws XMLStreamException
    {
        writer.setPrefix(arg0, arg1);
    }


    @Override
    public void writeAttribute(String arg0, String arg1, String arg2, String arg3) throws XMLStreamException
    {
        writer.writeAttribute(arg0, arg1, arg2, arg3);
    }


    @Override
    public void writeAttribute(String arg0, String arg1, String arg2) throws XMLStreamException
    {
        writer.writeAttribute(arg0, arg1, arg2);
    }


    @Override
    public void writeAttribute(String arg0, String arg1) throws XMLStreamException
    {
        writer.writeAttribute(arg0, arg1);
    }


    @Override
    public void writeCData(String arg0) throws XMLStreamException
    {
        writer.writeCData(arg0);
    }


    @Override
    public void writeCharacters(char[] arg0, int arg1, int arg2) throws XMLStreamException
    {
        writer.writeCharacters(arg0, arg1, arg2);
    }


    @Override
    public void writeCharacters(String arg0) throws XMLStreamException
    {
        writer.writeCharacters(arg0);
    }


    @Override
    public void writeComment(String arg0) throws XMLStreamException
    {
        writer.writeComment(arg0);
    }


    @Override
    public void writeDTD(String arg0) throws XMLStreamException
    {
        writer.writeDTD(arg0);
    }


    @Override
    public void writeDefaultNamespace(String arg0) throws XMLStreamException
    {
        writer.writeDefaultNamespace(arg0);
    }


    @Override
    public void writeEmptyElement(String arg0, String arg1, String arg2) throws XMLStreamException
    {
        writer.writeEmptyElement(arg0, arg1, arg2);
    }


    @Override
    public void writeEmptyElement(String arg0, String arg1) throws XMLStreamException
    {
        writer.writeEmptyElement(arg0, arg1);
    }


    @Override
    public void writeEmptyElement(String arg0) throws XMLStreamException
    {
        writer.writeEmptyElement(arg0);
    }


    @Override
    public void writeEndDocument() throws XMLStreamException
    {
        writer.writeEndDocument();
    }


    @Override
    public void writeEndElement() throws XMLStreamException
    {
        writer.writeEndElement();
    }


    @Override
    public void writeEntityRef(String arg0) throws XMLStreamException
    {
        writer.writeEntityRef(arg0);
    }


    @Override
    public void writeNamespace(String arg0, String arg1) throws XMLStreamException
    {
        writer.writeNamespace(arg0, arg1);
    }


    @Override
    public void writeProcessingInstruction(String arg0, String arg1) throws XMLStreamException
    {
        writer.writeProcessingInstruction(arg0, arg1);
    }


    @Override
    public void writeProcessingInstruction(String arg0) throws XMLStreamException
    {
        writer.writeProcessingInstruction(arg0);
    }


    @Override
    public void writeStartDocument() throws XMLStreamException
    {
        writer.writeStartDocument();
    }


    @Override
    public void writeStartDocument(String arg0, String arg1) throws XMLStreamException
    {
        writer.writeStartDocument(arg0, arg1);
    }


    @Override
    public void writeStartDocument(String arg0) throws XMLStreamException
    {
        writer.writeStartDocument(arg0);
    }


    @Override
    public void writeStartElement(String arg0, String arg1, String arg2) throws XMLStreamException
    {
        writer.writeStartElement(arg0, arg1, arg2);
    }


    @Override
    public void writeStartElement(String arg0, String arg1) throws XMLStreamException
    {
        writer.writeStartElement(arg0, arg1);
    }


    @Override
    public void writeStartElement(String arg0) throws XMLStreamException
    {
        writer.writeStartElement(arg0);
    }
}
