/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.xml;

import java.util.*;

import org.apache.xerces.dom.DOMImplementationImpl;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.io.InputStream;
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
    /** document uri */
	protected URI uri = null;

	/** w3c.Document object */
	protected Document domDocument;

	/** Hashtable linking each (local) ID (String) to an Element (w3c.Element) */
	protected Hashtable<String, Element> identifiers;

	/** Hashtable linking NS URI (String) to a NS prefix (String) */
	protected Hashtable<String, String> nsUriToPrefix;
    
    /** Hashtable linking NS Prefix (String) to a NS URI (String) */
    protected Hashtable<String, String> nsPrefixToUri;
    

	public XMLDocument()
	{
		identifiers = new Hashtable<String, Element>();
        nsUriToPrefix = new Hashtable<String, String>();
        nsPrefixToUri = new Hashtable<String, String>();
	}
    
    
    public XMLDocument(String ns, String qname)
    {
        this();        
        domDocument = DOMImplementationImpl.getDOMImplementation().createDocument(ns, qname, null);
    }


	public XMLDocument(Document doc)
	{
		this();		
		domDocument = doc;
	}
    
    
    public XMLDocument(InputStream inputStream, boolean validate) throws DOMHelperException
    {
        this();
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
        this.nsUriToPrefix.put(uri, prefix);
        this.nsPrefixToUri.put(prefix, uri);
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
        // create a custom DOMParser for catching namespace and ids declarations
        final XMLDocument xmlDoc = this;
        DOMParser domParser = new DOMParser()
        {
            @Override
            public void startElement(org.apache.xerces.xni.QName qname, XMLAttributes attribs, Augmentations aug) throws XNIException
            {
                super.startElement(qname, attribs, aug);
                
                // pick up all id and xmlns attributes
                int attrCount = attribs.getLength ();
                for (int i = 0; i < attrCount; i++)
                {
                    String prefix = attribs.getPrefix(i);
                    String localName = attribs.getLocalName(i);
                    
                    if (prefix.equals("xmlns"))
                    {
                        xmlDoc.addNS(localName, attribs.getValue(i));
                        fCurrentNode.getAttributes().removeNamedItem(attribs.getQName(i));
                    }
                    else if(localName.equalsIgnoreCase("id"))
                        xmlDoc.addId(attribs.getValue(i), (Element)fCurrentNode);
                }
            }          
        };

        //domParser.setErrorHandler(new HandlerBase());

        try
        {
            // validation ?
            domParser.setFeature("http://xml.org/sax/features/validation", validate);
            domParser.setFeature("http://apache.org/xml/features/validation/dynamic", validate);
            domParser.setFeature("http://apache.org/xml/features/validation/schema", validate);
            domParser.setFeature("http://apache.org/xml/features/validation/schema-full-checking", validate);
            
            // don't use defered nodes
            domParser.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
            
            // get real namespace domains ?
            //domParser.setFeature("http://xml.org/sax/features/namespaces", false);

            // get elements default value ?
            //domParser.setFeature("http://apache.org/xml/features/validation/schema/element-default", true);

            // force use of real URI when needed ?
            //domParser.setFeature("http://apache.org/xml/features/standard-uri-conformant", false);

            domParser.parse(new InputSource(inputStream));
            inputStream.close();
        }
        catch (SAXParseException e)
        {
            String xmlDebugInfo = "Line " + e.getLineNumber() + ", Column " + e.getColumnNumber();
            throw new DOMHelperException("Error while parsing XML document at " + xmlDebugInfo, e);
        }
        catch (SAXException e)
        {
            throw new DOMHelperException("SAX Error while parsing XML document", e);
        }
        catch (java.io.IOException e)
        {
            throw new DOMHelperException("Error while reading XML document", e);
        }

        return domParser.getDocument();
    }
    
    
      /**
     * This function looks and stores all identifiers attributes found in the document<br>
     * <br>This adds every id it found to the corresponding hashtable in the given XMLDocument<br>
     * <br>This is called recursively for each child node
     * @param xmlDocument The document where the search is done
     * @param node The node from which we start the search (if null, we use the root element)
     */
    public void readIdentifiers(Element elt)
    {
        if (elt == null)
            elt = getDocument().getDocumentElement();

        NodeList children = elt.getChildNodes();
        NamedNodeMap attribs = elt.getAttributes();
        short nodeType = elt.getNodeType();

        if ((nodeType == Node.ELEMENT_NODE) && (attribs != null))
        {
            for (int i = 0; i < attribs.getLength(); i++)
            {
                if (attribs.item(i).getLocalName().equalsIgnoreCase("id"))
                    addId(attribs.item(i).getNodeValue(), elt);
            }
        }

        // call the function for all the child nodes
        if (children != null)
        {
            for (int i = 0; i < children.getLength(); i++)
            {
                Node nextNode = children.item(i);
                if (nextNode instanceof Element)
                    readIdentifiers((Element) children.item(i));
            }
        }
    }


    /**
     * Reads all the namespace prefix:domain pairs and store them in the hashtable of the XMLDocument
     * @param xmlDocument document to read the namespaces from
     */
    public void readNamespaces(Element elt)
    {
        String uri, prefix;

        if (elt == null)
            elt = getDocument().getDocumentElement();

        NamedNodeMap attribs = elt.getAttributes();

        for (int i = 0; i < attribs.getLength(); i++)
        {
            String attribName = attribs.item(i).getNodeName();
            if (attribName.startsWith("xmlns"))
            {
                uri = attribs.item(i).getNodeValue().trim();

                // get the prefix from the attrib name
                int index = attribName.indexOf(":");
                if (index == -1)
                    prefix = QName.DEFAULT_PREFIX;
                else
                    prefix = attribName.substring(index + 1);

                addNS(prefix, uri);
            }
        }
    }
}
