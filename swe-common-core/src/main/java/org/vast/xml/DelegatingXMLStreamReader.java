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
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


/**
 * <p>
 * Abstract base for delegating XML stream readers.<br/>
 * By default, all methods are delegated to the underlying reader
 * </p>
 *
 * @author Alex Robin
 * @since May 18, 2017
 */
public abstract class DelegatingXMLStreamReader implements XMLStreamReader
{
    final XMLStreamReader reader;


    protected DelegatingXMLStreamReader(XMLStreamReader reader)
    {
        this.reader = reader;
    }


    public void close() throws XMLStreamException
    {
        reader.close();
    }


    public int getAttributeCount()
    {
        return reader.getAttributeCount();
    }


    public String getAttributeLocalName(int index)
    {
        return reader.getAttributeLocalName(index);
    }


    public QName getAttributeName(int index)
    {
        return reader.getAttributeName(index);
    }


    public String getAttributeNamespace(int index)
    {
        return reader.getAttributeNamespace(index);
    }


    public String getAttributePrefix(int index)
    {
        return reader.getAttributePrefix(index);
    }


    public String getAttributeType(int index)
    {
        return reader.getAttributeType(index);
    }


    public String getAttributeValue(int index)
    {
        return reader.getAttributeValue(index);
    }


    public String getAttributeValue(String namespaceURI, String localName)
    {
        return reader.getAttributeValue(namespaceURI, localName);
    }


    public String getCharacterEncodingScheme()
    {
        return reader.getCharacterEncodingScheme();
    }


    public String getElementText() throws XMLStreamException
    {
        return reader.getElementText();
    }


    public String getEncoding()
    {
        return reader.getEncoding();
    }


    public int getEventType()
    {
        return reader.getEventType();
    }


    public String getLocalName()
    {
        return reader.getLocalName();
    }


    public Location getLocation()
    {
        return reader.getLocation();
    }


    public QName getName()
    {
        return reader.getName();
    }


    public NamespaceContext getNamespaceContext()
    {
        return reader.getNamespaceContext();
    }


    public int getNamespaceCount()
    {
        return reader.getNamespaceCount();
    }


    public String getNamespacePrefix(int index)
    {
        return reader.getNamespacePrefix(index);
    }


    public String getNamespaceURI()
    {
        return reader.getNamespaceURI();
    }


    public String getNamespaceURI(int index)
    {
        return reader.getNamespaceURI(index);
    }


    public String getNamespaceURI(String prefix)
    {
        return reader.getNamespaceURI(prefix);
    }


    public String getPIData()
    {
        return reader.getPIData();
    }


    public String getPITarget()
    {
        return reader.getPITarget();
    }


    public String getPrefix()
    {
        return reader.getPrefix();
    }


    public Object getProperty(String name) throws IllegalArgumentException
    {
        return reader.getProperty(name);
    }


    public String getText()
    {
        return reader.getText();
    }


    public char[] getTextCharacters()
    {
        return reader.getTextCharacters();
    }


    public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException
    {
        return reader.getTextCharacters(sourceStart, target, targetStart, length);
    }


    public int getTextLength()
    {
        return reader.getTextLength();
    }


    public int getTextStart()
    {
        return reader.getTextStart();
    }


    public String getVersion()
    {
        return reader.getVersion();
    }


    public boolean hasName()
    {
        return reader.hasName();
    }


    public boolean hasNext() throws XMLStreamException
    {
        return reader.hasNext();
    }


    public boolean hasText()
    {
        return reader.hasText();
    }


    public boolean isAttributeSpecified(int index)
    {
        return reader.isAttributeSpecified(index);
    }


    public boolean isCharacters()
    {
        return reader.isCharacters();
    }


    public boolean isEndElement()
    {
        return reader.isEndElement();
    }


    public boolean isStandalone()
    {
        return reader.isStandalone();
    }


    public boolean isStartElement()
    {
        return reader.isStartElement();
    }


    public boolean isWhiteSpace()
    {
        return reader.isWhiteSpace();
    }


    public int next() throws XMLStreamException
    {
        return reader.next();
    }


    public int nextTag() throws XMLStreamException
    {
        return reader.nextTag();
    }


    public void require(int type, String namespaceURI, String localName) throws XMLStreamException
    {
        reader.require(type, namespaceURI, localName);
    }


    public boolean standaloneSet()
    {
        return reader.standaloneSet();
    }
}
