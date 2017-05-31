/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.xml;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import net.opengis.AbstractXMLStreamBindings;
import net.opengis.NamespaceRegister;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public abstract class XMLBindingsUtils
{
    private final static String TEMP_FRAGMENT_NAME = "F";
    
    protected String encoding = "UTF-8";
    protected AbstractXMLStreamBindings staxBindings;
    
    
    protected abstract Object readFromXmlStream(XMLStreamReader reader, Enum<?>  eltType) throws XMLStreamException;
    
    protected abstract void writeToXmlStream(XMLStreamWriter writer, Object sweObj, Enum<?> eltType) throws XMLStreamException;
    
    
    protected Object readFromDom(DOMHelper dom, Element componentElt, Enum<?> eltType) throws XMLReaderException
    {
        try
        {
            DOMSource source = new DOMSource(componentElt);
            XMLStreamReader reader = XMLImplFinder.getStaxInputFactory().createXMLStreamReader(source);
            reader = new XMLStreamReaderWithLocation(reader, dom.getXmlDocument().getUri());
            return readFromXmlStream(reader, eltType);
        }
        catch (Exception e)
        {
            throw new XMLReaderException("Error while reading " + eltType + " from DOM tree", e);
        }
    }
    
    
    protected Object readFromStream(InputStream is, Enum<?> eltType) throws XMLReaderException
    {
        return readFromStream(is, null, eltType);
    }
    
    
    public Object readFromStream(InputStream is, URI baseURI, Enum<?> eltType) throws XMLReaderException
    {
        try
        {
            XMLStreamReader reader = XMLImplFinder.getStaxInputFactory().createXMLStreamReader(is, encoding);
            if (baseURI != null)
                reader = new XMLStreamReaderWithLocation(reader, baseURI);
            return readFromXmlStream(reader, eltType);
        }
        catch (Exception e)
        {
            throw new XMLReaderException("Error while reading " + eltType + " from input stream", e);
        }
    }
    
    
    protected Element writeToDom(DOMHelper dom, Object sweObj, Enum<?> eltType) throws XMLWriterException
    {
        try
        {
            DOMResult result = new DOMResult(dom.createElement(TEMP_FRAGMENT_NAME));
            XMLOutputFactory factory = XMLImplFinder.getStaxOutputFactory();
            XMLStreamWriter writer = factory.createXMLStreamWriter(result);
            staxBindings.setNamespacePrefixes(writer);
            writeToXmlStream(writer, sweObj, eltType);
            addOnlyUsedNamespaceMappingsToDOM(dom, dom.getXmlDocument(), staxBindings.getNamespaceContext(), result.getNode());
            writer.close();
            return (Element)result.getNode().getFirstChild();
        }
        catch (Exception e)
        {
            throw new XMLWriterException("Error while writing " + eltType + " to DOM tree", e);
        }
    }
    
    
    protected void writeToStream(OutputStream os, Object sweObj, Enum<?> eltType, boolean indent) throws XMLWriterException
    {
        try
        {
            XMLOutputFactory factory = XMLImplFinder.getStaxOutputFactory();
            XMLStreamWriter writer = factory.createXMLStreamWriter(os, encoding);
            if (indent)
                writer = new IndentingXMLStreamWriter(writer);
            staxBindings.setNamespacePrefixes(writer);
            staxBindings.declareNamespacesOnRootElement();
            writeToXmlStream(writer, sweObj, eltType);
            writer.close();
        }
        catch (XMLStreamException e)
        {
            throw new XMLWriterException("Error while writing " + eltType + " to output stream", e);
        }
    }
    
    
    /**
     * This recursively scans the created DOM tree to get all used namespaces
     * @param dom
     * @param xmlDoc
     * @param elt
     */
    protected void addOnlyUsedNamespaceMappingsToDOM(DOMHelper dom, XMLDocument xmlDoc, NamespaceRegister staxNsMap, Node elt)
    {
        NodeList children = elt.getChildNodes();
        for (int i = 0; i < children.getLength(); i++)
        {
            Node child = children.item(i);
            if (child.getNodeType() != Node.ELEMENT_NODE)
                continue;
            
            String nsUri = child.getNamespaceURI();
            if (nsUri != null && xmlDoc.getNSPrefix(nsUri) == null)
                xmlDoc.addNS(child.getPrefix(), nsUri);
            
            // scan attributes
            NamedNodeMap attribs = child.getAttributes();
            for (int a = 0; a < attribs.getLength(); a++)
            {
                Node att = attribs.item(a);
                nsUri = att.getNamespaceURI();
                if (DOMHelper.XML_NS_URI.equals(nsUri))
                    continue;
                if (nsUri != null && xmlDoc.getNSPrefix(nsUri) == null)
                    xmlDoc.addNS(staxNsMap.getPrefix(nsUri), nsUri);
            }
            
            // call recursively
            addOnlyUsedNamespaceMappingsToDOM(dom, xmlDoc, staxNsMap, child);
        }
    }
    
    
    /**
     * To set output encoding (defaults to UTF-8)
     * @param encoding
     */
    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }
}
