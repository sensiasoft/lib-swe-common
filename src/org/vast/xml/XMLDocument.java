/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.xml;

import java.util.*;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSParserFilter;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.traversal.NodeFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;


/**
 * <p><b>Title:</b><br/>
 * XML Document
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO XMLDocument type description
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 3, 2005
 * @version 1.0
 */
public class XMLDocument
{
    protected static DOMImplementation domImpl;
    
    /** document uri */
	protected URI uri = null;

	/** w3c.Document object */
	protected Document domDocument;

	/** Hashtable linking each (local) ID (String) to an Element (w3c.Element) */
	protected Hashtable<String, Element> identifiers = new Hashtable<String, Element>();

	/** Hashtable linking NS URI (String) to a NS prefix (String) */
	protected Hashtable<String, String> nsUriToPrefix = new Hashtable<String, String>();
    
    /** Hashtable linking NS Prefix (String) to a NS URI (String) */
    protected Hashtable<String, String> nsPrefixToUri = new Hashtable<String, String>();
    

    public static DOMImplementation getDOMImplementation()
    {
        if (domImpl == null)
        {
            try
            {
                DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
                domImpl = registry.getDOMImplementation("LS");
            }
            catch (Exception e)
            {
                throw new IllegalStateException("Impossible to initialize DOM 3.0 LS implementation");
            }
        }
        
        return domImpl;
    }
    
    
	public XMLDocument()
	{
        // HACK because createDocument(null, null, null) throws an exception with some DOM impl
		domDocument = getDOMImplementation().createDocument(null, "root", null);
		domDocument.removeChild(domDocument.getDocumentElement());
	}
    
    
    public XMLDocument(QName qname)
    {    
        domDocument = getDOMImplementation().createDocument(qname.nsUri, qname.getFullName(), null);
        addNS(qname.prefix, qname.nsUri);
    }


	public XMLDocument(Document doc)
	{
		domDocument = doc;
	}
    
    
    public XMLDocument(InputStream inputStream, boolean validate) throws DOMHelperException
    {
        domDocument = parseDOM(inputStream, validate);
    }


	/**
	 * Adds a pair identifier/node in the list
	 * @param id XML Identifier
	 * @param elt XML Node
	 */
	public void addId(String id, Element elt)
	{
		this.identifiers.put(id, elt);
	}
    
    
    /**
     * Adds mapping from uri to prefix and prefix to uri
     * for further lookups
     * @param prefix
     * @param uri
     */
    public void addNS(String prefix, String uri)
    {
        if (uri != null)
        {
            this.nsUriToPrefix.put(uri, prefix);
            this.nsPrefixToUri.put(prefix, uri);
        }
    }


	/**
	 * Retrieve a node from the unique ID
	 * @param id Node identifier
	 * @return the corresponding Node object
	 */
	public Element getElementByID(String id) throws DOMHelperException
	{
		Element elt = this.identifiers.get(id);

		if (elt == null)
			throw new DOMHelperException("Id '" + id + "' not found in document: " + uri);

		return elt;
	}


	/**
	 * Retrieve a namespace prefix for this document
     * @param nsUri namespace domain uri
	 * @return the prefix string or null if not found
	 */
    public String getNSPrefix(String nsUri)
    {
        return nsUriToPrefix.get(nsUri);
    }
    
    
    /**
     * Retrieves the namespace uri matching the given prefix
     * @param nsPrefix namespace prefix string
     * @return the uri or null if not found
     */
    public String getNSUri(String nsPrefix)
    {
        return nsPrefixToUri.get(nsPrefix);
    }
    
    
    /**
     * Retrieves the namespace table
     * @return
     */
    public Hashtable<String, String> getNSTable()
    {
    	return nsPrefixToUri;
    }


	/**
	 * Get the dom document object
	 * @return Document object
	 */
	public Document getDocument()
	{
		return this.domDocument;
	}


	/**
	 * Get this dom document root element
	 * @return Element
	 */
	public Element getDocumentElement()
	{
		return this.domDocument.getDocumentElement();
	}


	/**
	 * @return Returns the base uri of the document.
	 */
	public URI getUri()
	{
		return uri;
	}


	/**
	 * @param uri The uri to set as base uri for this document.
	 */
	public void setUri(URI uri)
	{
		this.uri = uri;
	}
        
    
    /**
     * Launch the W3C Xerces DOM Parser
     * @param inputSource Stream containing XML file we want to parse
     * @param validation boolean if true force the validation process with the schema
     * @return The XMLDocument object created
     * @throws DOMHelperException if a problem occur during parsing
     */
    protected Document parseDOM(InputStream inputStream, boolean validate) throws DOMHelperException
    {
        DOMImplementationLS impl = ((DOMImplementationLS)getDOMImplementation());
        
        // wrap input stream
        LSInput input = impl.createLSInput();
        input.setByteStream(inputStream);
        
        // prepare filter to extract IDs and namespaces
        LSParserFilter filter = new LSParserFilter()
        {
            public int getWhatToShow()
            {
                return NodeFilter.SHOW_ELEMENT;
            }
            
            public short acceptNode(Node node)
            {
                if (!(node instanceof Element))
                    return LSParserFilter.FILTER_ACCEPT;
                
                Element elt = (Element)node;
                readIdentifiers(elt, false);
                readNamespaces(elt, false);
                
                return LSParserFilter.FILTER_ACCEPT;
            }

            public short startElement(Element arg0)
            {
                return LSParserFilter.FILTER_ACCEPT;
            }            
        };
        
        // init parser
        LSParser parser = impl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
        parser.setFilter(filter);
        
        // configure parser
        DOMConfiguration config = parser.getDomConfig();
        config.setParameter("namespace-declarations", true);
        
        if (validate)
        {
            config.setParameter("schema-type", "http://www.w3.org/2001/XMLSchema");
            config.setParameter("validate-if-schema", true);
        }
        
        // don't use defered nodes
        //domParser.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
        
        // start parsing
        try
        {
            Document document = parser.parse(input);
            readIdentifiers(document.getDocumentElement(), false);
            readNamespaces(document.getDocumentElement(), false);
            inputStream.close();
            return document;
        }
        catch (Exception e)
        {
            throw new DOMHelperException("Error while reading XML document", e);
        }
    }
    
    
    /**
     * Helper method to serialize this DOM node to a stream using the provided serializer
     * @param node
     * @param out
     * @param serializer
     * @throws IOException
     */
    public void serialize(Node node, OutputStream out, LSSerializer serializer) throws IOException
    {
        // select root element to serialize
        Element elt = null;
        if (node.getNodeType() == Node.ELEMENT_NODE)
            elt = (Element)node;
        else if (node.getNodeType() == Node.DOCUMENT_NODE)
            elt = ((Document)node).getDocumentElement();
        else
            return;

        // wrap output stream
        DOMImplementationLS impl = (DOMImplementationLS)getDOMImplementation();
        LSOutput output = impl.createLSOutput();
        output.setByteStream(out);
        
        // add namespaces xmlns attributes
        Enumeration<String> nsEnum = nsUriToPrefix.keys();
        while (nsEnum.hasMoreElements())
        {
            String uri = (String)nsEnum.nextElement();
            String prefix = getNSPrefix(uri);
            
            // add namespace attributes to root element
            String attName = "xmlns";
            if (!prefix.equals(QName.DEFAULT_PREFIX))
                attName += ":" + prefix;
            elt.setAttributeNS("http://www.w3.org/2000/xmlns/", attName, uri);
        }
        
        // launch serialization
        serializer.write(elt, output);
    }
    
    
    /**
     * Helper method to serialize this DOM to a stream
     * @param node
     * @param out
     * @param pretty set to true to indent output
     * @throws IOException
     */
    public void serialize(Node node, OutputStream out, boolean pretty) throws IOException
    {
        try
        {
            DOMImplementationLS impl = (DOMImplementationLS)XMLDocument.getDOMImplementation();
            
            // init and configure serializer
            LSSerializer serializer = impl.createLSSerializer();
            DOMConfiguration config = serializer.getDomConfig();
            config.setParameter("format-pretty-print", pretty);
            
            serialize(node, out, serializer);
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Impossible to initilize DOM implementation for serialization");
        }
    }
    
    
    /**
     * This function looks and stores all identifiers attributes found in the document<br>
     * <br>This adds every id it found to the corresponding hashtable in the given XMLDocument<br>
     * <br>This is called recursively for each child node
     * @param xmlDocument The document where the search is done
     * @param node The node from which we start the search (if null, we use the root element)
     */
    public void readIdentifiers(Element elt, boolean recursive)
    {
        if (elt == null)
            elt = getDocument().getDocumentElement();
        
        NamedNodeMap attribs = elt.getAttributes();
        if (attribs != null)
        {
            for (int i = 0; i < attribs.getLength(); i++)
            {
                Attr attrib = (Attr)attribs.item(i);
                String localName = attrib.getLocalName();
                String val = attrib.getValue();
                if (localName.equalsIgnoreCase("id"))
                    addId(val, elt);
            }
        }

        // call the function for all the child nodes
        NodeList children = elt.getChildNodes();
        if (children != null && recursive)
        {
            for (int i = 0; i < children.getLength(); i++)
            {
                Node nextNode = children.item(i);
                if (nextNode instanceof Element)
                    readIdentifiers((Element) children.item(i), recursive);
            }
        }
    }


    /**
     * Reads all the namespace prefix:domain pairs and store them in the hashtable of the XMLDocument
     * @param xmlDocument document to read the namespaces from
     */
    public void readNamespaces(Element elt, boolean recursive)
    {
        if (elt == null)
            elt = getDocument().getDocumentElement();

        NamedNodeMap attribs = elt.getAttributes();
        for (int i = 0; i < attribs.getLength(); i++)
        {
            Attr att = (Attr)attribs.item(i);
            String prefix = att.getPrefix();
            String localName = att.getLocalName().trim();
            String val = att.getValue().trim();
                
            if (prefix != null && prefix.equals("xmlns"))
                addNS(localName, val);
            else if(localName.equals("xmlns"))
                addNS(QName.DEFAULT_PREFIX, val);
        }
        
        // call the function for all the child nodes
        NodeList children = elt.getChildNodes();
        if (children != null && recursive)
        {
            for (int i = 0; i < children.getLength(); i++)
            {
                Node nextNode = children.item(i);
                if (nextNode instanceof Element)
                    readNamespaces((Element) children.item(i), recursive);
            }
        }
    }
}
