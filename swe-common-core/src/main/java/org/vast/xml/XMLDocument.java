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
import java.util.Map.Entry;
import org.w3c.dom.*;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSParserFilter;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.traversal.NodeFilter;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/**
 * <p>
 * Wrapper class for a DOM XML document
 * </p>
 *
 * @author Alex Robin
 * @since Nov 3, 2005
 * */
public class XMLDocument
{
    protected static DOMImplementation domImpl;
    
    /** document URI */
	protected URI uri = null;

	/** w3c.Document object */
	protected Document domDocument;

	/** map linking each (local) ID (String) to an Element (w3c.Element) */
	protected Map<String, Element> identifiers = new LinkedHashMap<String, Element>();

	/** map linking NS URI (String) to a NS prefix (String) */
	protected Map<String, String> nsUriToPrefix = new LinkedHashMap<String, String>();
    
    /** map linking NS Prefix (String) to a NS URI (String) */
    protected Map<String, String> nsPrefixToUri = new LinkedHashMap<String, String>();
    
    
	public XMLDocument()
	{
        // HACK because createDocument(null, null, null) throws an exception with some DOM impl
		domDocument = XMLImplFinder.getDOMImplementation().createDocument(null, "root", null);
		domDocument.removeChild(domDocument.getDocumentElement());
	}
    
    
    public XMLDocument(QName qname)
    {    
        domDocument = XMLImplFinder.getDOMImplementation().createDocument(qname.nsUri, qname.getFullName(), null);
        addNS(qname.prefix, qname.nsUri);
    }


	public XMLDocument(Document doc)
	{
	    this.domDocument = doc;
	}
    
    
	public XMLDocument(InputStream inputStream, boolean validate) throws DOMHelperException
    {
        this(inputStream, validate, null);
    }
	
	
    public XMLDocument(InputStream inputStream, boolean validate, Map<String, String> schemaLocations) throws DOMHelperException
    {
        this.domDocument = parseDOM(inputStream, validate, schemaLocations);
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
	 * @throws DOMHelperException if no node with the given ID belongs to the document
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
     * @return map of namespace prefix to URI
     */
    public Map<String, String> getNSTable()
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
	 * Parse DOM from given input stream
	 * @param inputSource Stream containing XML file we want to parse
     * @param validation boolean if true force the validation process with the schema
     * @return The XMLDocument object created
     * @throws DOMHelperException if a problem occur during parsing
	 */
	protected Document parseDOM(InputStream inputStream, boolean validate, Map<String, String> schemaLocations) throws DOMHelperException
    {
	    try
        {
            // use LS implementation when available
            DOMImplementation impl = XMLImplFinder.getDOMImplementation();            
            if (impl instanceof DOMImplementationLS)
                return parseDOM_LS(inputStream, validate, schemaLocations);
            else
                return parseDOM_JAXP(inputStream, validate, schemaLocations);
        }
        catch (Exception e)
        {
            throw new DOMHelperException("Error while reading XML document", e);
        }
    }


    protected Document parseDOM_LS(InputStream inputStream, boolean validate, Map<String, String> schemaLocations) throws DOMHelperException, IOException
    {
        DOMImplementationLS impl = ((DOMImplementationLS)XMLImplFinder.getDOMImplementation());
        
        // wrap input stream
        LSInput input = impl.createLSInput();
        input.setByteStream(inputStream);
        
        // prepare filter to extract IDs and namespaces
        LSParserFilter filter = new LSParserFilter()
        {
            @Override
            public int getWhatToShow()
            {
                return NodeFilter.SHOW_ELEMENT;
            }
            
            @Override
            public short acceptNode(Node node)
            {
                if (!(node instanceof Element))
                    return LSParserFilter.FILTER_ACCEPT;
                
                Element elt = (Element)node;
                readIdentifiers(elt, false);
                readNamespaces(elt, false);
                
                return LSParserFilter.FILTER_ACCEPT;
            }

            @Override
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
        
        final StringBuilder errors = new StringBuilder();
        if (validate)
        {
            config.setParameter("schema-type", "http://www.w3.org/2001/XMLSchema");
            config.setParameter("validate", true);
            
            // set override schema locations if specified
            if (schemaLocations != null && !schemaLocations.isEmpty())
            {
                StringBuilder schemaLocText = new StringBuilder();
                for (Entry<String,String> schemaLoc: schemaLocations.entrySet())
                {
                    if (!schemaLoc.getKey().equals(DOMHelper.DEFAULT_PREFIX))
                    {
                        schemaLocText.append(schemaLoc.getKey());
                        schemaLocText.append(' ');
                    }
                    schemaLocText.append(schemaLoc.getValue());
                    schemaLocText.append(' ');
                }
                
                config.setParameter("schema-location", schemaLocText.toString());
            }
            
            config.setParameter("error-handler", new DOMErrorHandler() {
                @Override
                public boolean handleError(DOMError e)
                {
                    errors.append("Line " + e.getLocation().getLineNumber() + ": " + e.getMessage());
                    errors.append('\n');
                    return false;
                }                
            });
        }
        
        // don't use defered nodes
        //domParser.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
        
        // start parsing
        Document document = parser.parse(input);        
        if (errors.length() > 0)
            throw new DOMHelperException("Validation errors detected while parsing XML document:\n" + errors.toString());
        inputStream.close();
        
        // also read IDs and namespaces on root element
        readIdentifiers(document.getDocumentElement(), false);
        readNamespaces(document.getDocumentElement(), false);
                
        return document;
    }
    
    
    protected Document parseDOM_JAXP(InputStream inputStream, boolean validate, Map<String, String> schemaLocations) throws ParserConfigurationException, IOException, SAXException
    {
        // create document builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        // launch parser
        Document document = builder.parse(inputStream);
        
        // scan all IDs and namespaces in doc
        readIdentifiers(document.getDocumentElement(), true);
        readNamespaces(document.getDocumentElement(), true);
                
        return document;
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
        Element elt = null;
        
        try
        {
            // select root element to serialize
            if (node.getNodeType() == Node.ELEMENT_NODE)
                elt = (Element)node;
            else if (node.getNodeType() == Node.DOCUMENT_NODE)
                elt = ((Document)node).getDocumentElement();
            else
                return;
            
            // add namespaces xmlns attributes
            for (Entry<String, String> nsEntry: nsUriToPrefix.entrySet())
            {
                String uri = nsEntry.getKey();
                String prefix = nsEntry.getValue();
                
                // add namespace attributes to root element
                String attName = "xmlns";
                if (!prefix.equals(DOMHelper.DEFAULT_PREFIX))
                    attName += ":" + prefix;
                elt.setAttributeNS("http://www.w3.org/2000/xmlns/", attName, uri);
            }
        }
        catch (Exception e)
        {
            throw new IOException("Error while writing namespaces", e);
        }  
            
        try
        {    
            // use LS implementation when available
            DOMImplementation impl = XMLImplFinder.getDOMImplementation();            
            if (impl instanceof DOMImplementationLS)
                serializeDOM_LS(elt, out, pretty);
            else
                serializeDOM_JAXP(elt, out, pretty);
        }
        catch (Exception e)
        {
            throw new IOException("Error while serializing XML document", e);
        }
    }
    
    
    protected void serializeDOM_LS(Element elt, OutputStream out, boolean pretty) throws LSException
    {
        DOMImplementationLS impl = (DOMImplementationLS)XMLImplFinder.getDOMImplementation(); 
        
        // init and configure serializer
        LSSerializer serializer = impl.createLSSerializer();
        DOMConfiguration config = serializer.getDomConfig();
        config.setParameter("format-pretty-print", pretty);
        
        // wrap output stream
        LSOutput output = impl.createLSOutput();
        output.setByteStream(out);
        
        // launch serialization
        serializer.write(elt, output);
    }
    
    
    protected void serializeDOM_JAXP(Element elt, OutputStream out, boolean pretty) throws TransformerException
    {
        // create transformer
        Transformer serializer = TransformerFactory.newInstance().newTransformer();
        serializer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
        
        // set inputs/outputs
        Source input = new DOMSource(elt);
        Result output = new StreamResult(out);
        
        // launch transformer
        serializer.transform(input, output);
    }
    
    
    /**
     * This function looks and stores all identifiers attributes found in the document<br>
     * <br>This adds every id it founds to the corresponding hashtable in the given XMLDocument<br>
     * <br>This is called recursively for each child node
     * @param elt the DOM element where to start the search
     * @param recursive if true, all children elements are also processed recursively
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
        if (recursive)
        {
            NodeList children = elt.getChildNodes();
            if (children != null)
            {
                for (int i = 0; i < children.getLength(); i++)
                {
                    Node nextNode = children.item(i);
                    if (nextNode instanceof Element)
                        readIdentifiers((Element) children.item(i), recursive);
                }
            }
        }
    }


    /**
     * Reads all the namespace prefix:domain pairs and store them in the hashtable of the XMLDocument
     * @param elt the DOM element where to start the search
     * @param recursive if true, all children elements are also processed recursively
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
                addNS(DOMHelper.DEFAULT_PREFIX, val);
        }
        
        // call the function for all the child nodes
        if (recursive)
        {
            NodeList children = elt.getChildNodes();
            if (children != null)
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
}
