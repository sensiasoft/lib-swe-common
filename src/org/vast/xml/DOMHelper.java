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

import org.apache.xml.serialize.Method;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.vast.util.ExceptionSystem;
import org.vast.util.URIResolver;
import org.w3c.dom.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


/**
 * <p><b>Title:</b><br/>
 * DOM Helper
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO DOMHelper description
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 3, 2005
 * @version 1.0
 */
public class DOMHelper
{
    static protected final String PATH_SEPARATOR = "/";
    
    /** User prefix to domain map **/
    protected Hashtable<String, String> userPrefixTable = new Hashtable<String, String>();

    /** Linked documents (URI object -> XMLDocument Object) */
    protected Hashtable<String, XMLDocument> loadedDocuments = new Hashtable<String, XMLDocument>();

    /** Main Document object */
    protected XMLFragment mainFragment;

    /** List of nodes returned */
    protected XMLNodeList matchingNodes = new XMLNodeList();

    /** Current path inside the xml file being parsed.<br>
        Paths have to be given as element names separated by /<br>
        Namespaces are separated from the node name by ':'<br>
        Example:   "gml:feature/gml:polyline/epsg"   */
    protected ArrayList<String> currentPath = new ArrayList<String>();
    protected String[] wantedPath;
    protected QName eltQName = new QName();
    protected boolean validation = false;
    

    public DOMHelper()
    {
        createDocument(null);
    }
    

    /**
     * Loads an existing XML document from the given URL
     * @param url
     * @param validation
     * @throws DOMHelperException
     */
    public DOMHelper(String url, boolean validation) throws DOMHelperException
    {
        this.validation = validation;
        mainFragment = parseURI(url, true);
    }


    /**
     * Loads an existing XML document from the given InputStream
     * @param inputStream
     * @param validation
     * @throws DOMHelperException
     */
    public DOMHelper(InputStream inputStream, boolean validation) throws DOMHelperException
    {
        this.validation = validation;
        mainFragment = parseStream(inputStream, true);
    }


    /**
     * Loads an existing document from the given DOM Document object
     * @param domDocument
     */
    public DOMHelper(Document domDocument)
    {
        setDocument(domDocument);
    }
    
    
    /**
     * Creates a DOMHelper on a new document with the given QName
     * @param docQName
     */
    public DOMHelper(String docQName)
    {
        createDocument(docQName);
    }
    
    
    /**
     * Creates a new Document with the given qname
     * The new document will replace the document loaded in this DOMHelper
     * @param qname
     * @return
     */
    public void createDocument(String qname)
    {
        XMLDocument xmlDoc;
        
        if (qname == null || qname.equals(""))
            xmlDoc = new XMLDocument();
        else
        {
            QName qnameObj = getQName(null, qname);
            xmlDoc = new XMLDocument(qnameObj);
        }
        
        mainFragment = new XMLFragment(xmlDoc, xmlDoc.getDocumentElement());        
        loadedDocuments.put("NEW", xmlDoc);
    }


    /**
     * Returns the root element of the document
     * @return Root Element Node or null if root element is not found
     */
    public Element getRootElement()
    {
        return mainFragment.getXmlDocument().getDocumentElement();
    }
    
    
    /**
     * Returns the base element of the fragment attached to this reader
     * @return W3C Element
     */
    public Element getBaseElement()
    {
        return mainFragment.getBaseElement();
    }
    
    
    /**
     * Sets the element to use as the base when reading/writing
     * with default methods
     * @param elt
     */
    public void setBaseElement(Element elt)
    {
        if (elt.getOwnerDocument() != getDocument())
        {
            setDocument(elt.getOwnerDocument());
            setBaseElement(elt);
        }
        else
        {
            mainFragment.setBaseElement(elt);
        }
    }
    
    
    /**
     * Gets the DOM Document attached to this DOMHelper
     * @return
     */
    public Document getDocument()
    {
        return mainFragment.getXmlDocument().getDocument();
    }
    
    
    /**
     * Sets the Document to wrap with this DOMHelper
     * @param domDocument
     */
    public void setDocument(Document domDocument)
    {
        XMLDocument xmlDocument = new XMLDocument(domDocument);
        mainFragment = new XMLFragment(xmlDocument, xmlDocument.getDocumentElement());
        xmlDocument.readIdentifiers(domDocument.getDocumentElement());
        xmlDocument.readNamespaces(domDocument.getDocumentElement());
        loadedDocuments.put("NEW", xmlDocument);
    }
    
    
    /**
     * Gets the XMLDocument attached to this DOMHelper
     * @return XMLDocument
     */
    public XMLDocument getXmlDocument()
    {
        return mainFragment.getXmlDocument();
    }


    /**
     * Gets the XMLFragment attached to this DOMHelper
     * @return XMLFragment
     */
    public XMLFragment getXmlFragment()
    {
        return mainFragment;
    }
    
    
    /**
     * Add an entry to the table of user prefixes
     * This table is used to map user prefixes to a real namespace uri
     * @param prefix
     * @param nsDomain
     */
    public void addUserPrefix(String prefix, String nsDomain)
    {
        userPrefixTable.put(prefix, nsDomain);
    }
    
    
    /**
     * Find all nodes matching the given path - ex: Sensor/identification
     * @param startNode DOM Node where to start the search
     * @param nodePath Path of the node relative to startNode (case independent, separation char = /)
     * @param nodeTypeFilter type of node to look for (0 for any type)
     * @param maxCount maximum number of nodes to look for
     * @return the NodeList of Node objects found or an Empty NodeList if no matching nodes are found
     */
    public NodeList getNodes(Node startNode, String nodePath, int nodeTypeFilter, int maxCount)
    {
        if (startNode == null)
            throw new IllegalArgumentException("startNode cannot be null");

        this.currentPath.clear();
        this.matchingNodes.clear();
        
        if (nodePath == null || nodePath.equals(""))
            wantedPath = new String[0];
        else
            wantedPath = nodePath.split(PATH_SEPARATOR);
        
        readNode(getParentDocument(startNode), startNode, nodeTypeFilter, maxCount);

        return new XMLNodeList(this.matchingNodes);
    }


    /**
     * Find elements matching the given path - ex: Sensor.locatedUsing
     * @param startElement DOM Node where to start the search
     * @param nodePath Path of the element relative to startNode (case independent, separation char = /)
     * @return the NodeList of Element objects found or an Empty NodeList if no matching elements are found
     */
    public NodeList getElements(Element startElement, String nodePath)
    {
        return getNodes(startElement, nodePath, Node.ELEMENT_NODE, -1);
    }


    /**
     * Default version of the previous function with startNode = base node of
     * document (not always the root !!)
     * @param nodePath Path of the element in the XML tree (case independent, separation char = /)
     * @return the NodeList of Element objects found or an Empty NodeList if no matching elements are found
     */
    public NodeList getElements(String nodePath)
    {
        return getElements(mainFragment.getBaseElement(), nodePath);
    }


    /**
     * Find the first element matching the given path
     * @param startElement DOM Node where to start the search
     * @param nodePath Path of the element relative to startNode (case independent, separation char = /)
     * @return the first Element object found or null if no matching element is found
     */
    public Element getElement(Element startElement, String nodePath)
    {
        NodeList nodeList = getNodes(startElement, nodePath, Node.ELEMENT_NODE, 1);

        if (nodeList.getLength() == 0)
            return null;

        return (Element) nodeList.item(0);
    }


    /**
     * Default version of the previous function with startNode = base node of document (not always the root !!)
     * @param nodePath Path of the element in the XML tree (case independent, separation char = "/")
     * @return the first Element object found or null if no matching nodes are found
     */
    public Element getElement(String nodePath)
    {
        return getElement(mainFragment.getBaseElement(), nodePath);
    }


    /**
     * Get the value of an element from its path
     * @param startElement DOM Node where to start the search
     * @param nodePath Path of the element in the XML tree (case independent, separation char = "/")
     * @return Node value as a String or null if no matching element is found
     */
    public String getElementValue(Element startElement, String nodePath)
    {
        Node node = (nodePath == null) ? startElement : getElement(startElement, nodePath);

        if (node == null)
            return null;

        node = node.getFirstChild();

        if ((node == null) || (node.getNodeType() != Node.TEXT_NODE))
            return null;

        String value = node.getNodeValue();

        if (value == null)
            return null;

        // remove tabs and returns, replace by a space ??
        value = value.replaceAll("[ \\t\\n\\f\\r]+", " ");

        return value.trim();
    }


    /**
     * Default version of the previous function that gets the value of the first element found
     * @param nodePath Path of the element in the XML tree (case independent, separation char = "/")
     * @return Node value as a String or null if no matching element is found
     */
    public String getElementValue(String nodePath)
    {
        return getElementValue(mainFragment.getBaseElement(), nodePath);
    }
    
    
    /**
     * Default version of the getElementValue(Element, String) function.
     * The nodePath is here set to null and thus this method simply retrieves
     * the text content of the given Element.
     * @param element
     * @return
     */
    public String getElementValue(Element element)
    {
        return getElementValue(element, null);
    }


    /**
     * Read the attribute value
     * @param startElement DOM Node where to start the search
     * @param nodePath Path of the element relative to startNode (case independent, separation char = "/")
     * @return the attribute value as a string or null if no matching attribute is found
     */
    public String getAttributeValue(Element startElement, String nodePath)
    {
        NodeList resultList = getNodes(startElement, nodePath, Node.ATTRIBUTE_NODE, 1);

        if (resultList.getLength() == 0)
            return null;

        String value = resultList.item(0).getNodeValue();

        if (value == null)
            return null;

        return value;//.trim();
    }


    /**
     * Default version of the previous function with startNode = base node of document (not always the root !!)
     * @param nodePath Path of the element in the XML tree (case independent, separation char = "/")
     * @return the attribute value as a String or null if no matching attribute is found
     */
    public String getAttributeValue(String nodePath)
    {
        return getAttributeValue(mainFragment.getBaseElement(), nodePath);
    }


    /**
     * Determine if the element corresponding to the given path exists
     * @param startElement DOM Node where to start the search
     * @param nodePath Path of the element in the DOM tree (case independent, separation char = "/")
     * @return true if the element is present
     */
    public boolean existElement(Element startElement, String nodePath)
    {
        getNodes(startElement, nodePath, Node.ELEMENT_NODE, 1);
        return (this.matchingNodes.getLength() != 0);
    }


    /**
     * Default version of the previous function with startNode = base node of document (not always the root !!)
     * @param nodePath Path of the element in the XML tree (case independent, separation char = /)
     * @return true if the element is present
     */
    public boolean existElement(String nodePath)
    {
        return existElement(mainFragment.getBaseElement(), nodePath);
    }


    /**
     * Determine if the attribute corresponding to the given path exists
     * @param startElement Node where to start the search
     * @param nodePath Path of the attribute in the DOM tree (case independent, separation char = /)
     * @return true if the attribute is present
     */
    public boolean existAttribute(Element startElement, String nodePath)
    {
        getNodes(startElement, nodePath, Node.ATTRIBUTE_NODE, 1);
        return (this.matchingNodes.getLength() != 0);
    }


    /**
     * Default version of the previous function with startNode = base node of document (not always the root !!)
     * @param nodePath Path of the attribute in the DOM tree (case independent, separation char = /)
     * @return true if the attribute is present
     */
    public boolean existAttribute(String nodePath)
    {
        return existAttribute(mainFragment.baseElement, nodePath);
    }
    
    
    /**
     * Checks if the given node has the given name
     * This does the comparison using user prefixes
     * @param node
     * @param qname
     * @return
     */
    public boolean hasQName(Node node, String qname)
    {
        eltQName.setFullName(qname);
        String nsUri = userPrefixTable.get(eltQName.prefix);
        String localName = eltQName.localName;
        
        if (node.getNamespaceURI() != null && nsUri != null)
        {
            if (node.getNamespaceURI().equals(nsUri) && node.getLocalName().equals(localName))
                return true;
            else
                return false;
        }
        else
        {
            if (node.getLocalName().equals(localName))
                return true;
            else
                return false;
        }
    }


    /**
     * Look for all the elements below the startNode with the given tag name
     * If the XML document defines namespaces, this method requires a fully
     * qualified element name (prefix + local name), and the prefix needs
     * to be defined by calling addUserPrefix(prefix, ns-uri)
     * @param startElement Node where to start the search
     * @param name Name of the nodes to look for
     * @return a NodeList containing all the matching elements or an Empty NodeList if no matching elements are found
     */
    public NodeList getAllElements(Element startElement, String tagName)
    {
        NodeList list = null;
        
        if ((startElement == null) || (tagName == null))
            throw new IllegalArgumentException("startNode and name can't be null");
        
        // separate prefix from local name
        int separatorIndex = tagName.indexOf(":");
        if (separatorIndex != -1)
        {
            String localName = tagName.substring(separatorIndex + 1);
            String userPrefix = tagName.substring(0, separatorIndex);
            
            // lookup namespace URI
            String nsUri = userPrefixTable.get(userPrefix);            
            list = ((Element) startElement).getElementsByTagNameNS(nsUri, localName);
        }
        else
        {
            list = ((Element) startElement).getElementsByTagName(tagName);
        }

        return list;
    }


    /**
     * Default version of the previous function
     * <br>with startNode = base node of document (not always the root !!)
     * @param name String Name of the nodes to look for
     * @return NodeList containing all the matching elements or an Empty NodeList if no matching elements are found
     */
    public NodeList getAllElements(String name)
    {
        return getAllElements(mainFragment.getBaseElement(), name);
    }


    /**
     * Get all child elements of the given node (don't return any text or comment nodes !)
     * This method will follow any xlink:href if specified.
     * @param parentElement Node to retrieve the children from
     * @return NodeList of child elements or an Empty NodeList if no children elements are found
     */
    public NodeList getAllChildElements(Element parentElement)
    {
        return getElements(parentElement, "*");
    }


    /**
     * Get the first child of the parentNode.
     * This method will follow any xlink:href if specified.
     * @param parentElement Node to retrieve the children from
     * @return NodeList of child elements or null if no children elements are found
     */
    public Element getFirstChildElement(Element parentElement)
    {
        NodeList children = getNodes(parentElement, "*", Node.ELEMENT_NODE, 1);
        if (children.getLength() == 0)
            return null;
        return (Element) children.item(0);
    }
    
    
    /**
     * Get the child elements (only elements !!) of the parentNode
     * This method is basic... It won't follow xlink:href
     * @param parentNode Node to retrieve the children from
     * @return NodeList of child elements or an Empty NodeList if no children elements are found
     */
    public NodeList getChildElements(Node parentNode)
    {
        if (parentNode == null)
            throw new IllegalArgumentException("parentNode cannot be null");

        XMLNodeList elementList = new XMLNodeList();

        NodeList children = parentNode.getChildNodes();

        for (int i = 0; i < children.getLength(); i++)
        {
            Node child = children.item(i);
            
            if (child.getNodeType() == Node.ELEMENT_NODE)
                elementList.addNode(child);

            if (child.getNodeType() == Node.ENTITY_REFERENCE_NODE)
            {
                NodeList entityChildList = getChildElements(child);
                elementList.addNode(entityChildList.item(0));
            }
        }

        return elementList;
    }
    
    
    /**
     * Adds any type of node to the parent node
     * @param parentNode
     * @param nodePath
     * @param nodeType
     * @return the newly added Node
     */
    public Node addNode(Node parentNode, String nodePath, int nodeType)
    {
        if ((parentNode == null) || (nodePath == null))
            throw new IllegalArgumentException("startNode and nodePath cannot be null");

        this.currentPath.clear();
        this.matchingNodes.clear();
        wantedPath = nodePath.split(PATH_SEPARATOR);
        writeNode(getParentDocument(parentNode), parentNode, nodeType);

        return matchingNodes.item(0);
    }
    
    
    /**
     * Adds an element to the document at the given location relative
     * to the parent element. This will also add all the elements given
     * in the path if they are not yet present or if marked with a '+' prefix.
     * @param parentElement
     * @param nodePath
     * @return
     */
    public Element addElement(Node parentNode, String nodePath)
    {
        if (parentNode == null)
            parentNode = getDocument();
        
        Node newNode = addNode(parentNode, nodePath, Node.ELEMENT_NODE);
        return (Element)newNode;
    }
    
    
    /**
     * Default version of the previous method using the base element
     * as the parent element.
     * @param nodePath
     * @return
     */
    public Element addElement(String nodePath)
    {
        return addElement(mainFragment.getBaseElement(), nodePath);
    }
    
    
    /**
     * Sets the text content of a node (element or atribute)
     * @param parentNode
     * @param val
     */
    public void setNodeValue(Node parentNode, String val)
    {
        Text text = getParentDocument(parentNode).getDocument().createTextNode(val);
        parentNode.appendChild(text);
    }


    /**
     * Remove all text node children from the parent
     * @param parent
     */
    protected void removeAllText(Node parent)
    {
    	NodeList childNodes = parent.getChildNodes();
        for (int i=0; i<childNodes.getLength(); i++)
        {
        	Node child = childNodes.item(i);
        	if (child.getNodeType() == Node.TEXT_NODE)
        		parent.removeChild(child);
        }
    }
    
    
    /**
     * Adds all necessary elements in the path and sets the text value
     * of the leaf element to the given String.
     * @param parentElement
     * @param nodePath
     * @param val
     */
    public void setElementValue(Element parentElement, String nodePath, String val)
    {
        Element elt = addElement(parentElement, nodePath);
        Text text = getParentDocument(parentElement).getDocument().createTextNode(val);
        removeAllText(elt);
        elt.appendChild(text);
    }
    
    
    /**
     * Default version of previous method to add value to an element directly
     * @param elt
     * @param val
     */
    public void setElementValue(Element elt, String val)
    {
        Text text = getParentDocument(elt).getDocument().createTextNode(val);
        removeAllText(elt);
        elt.appendChild(text);
    }


    /**
     * Default version of the previous method using the base element
     * as the parent element.
     * @param nodePath
     * @param text
     */
    public void setElementValue(String nodePath, String text)
    {
        setElementValue(mainFragment.getBaseElement(), nodePath, text);
    }


    /**
     * Adds all necessary elements and the final attribute in the path and
     * sets the text value of the attribute to the given String.
     * @param parentElement
     * @param nodePath
     * @param val
     */
    public void setAttributeValue(Element startElement, String nodePath, String val)
    {
        int attIndex = nodePath.lastIndexOf(PATH_SEPARATOR);
        Element elt = startElement;
        String attPath = null;
        
        if (attIndex != -1)
            attPath = nodePath.substring(0, attIndex);
            
        if (nodePath.charAt(attIndex+1) == '@')
            attIndex++;
            
        String attName = nodePath.substring(attIndex+1);
        
        if (attPath != null)
            elt = addElement(startElement, attPath);
        
        QName qnameObj = getQName(getParentDocument(elt), attName);
        if (qnameObj.getNsUri() == null)
            elt.setAttribute(attName, val);
        else
            elt.setAttributeNS(qnameObj.getNsUri(), qnameObj.getFullName(), val);
    }


    /**
     * Default version of the previous method using the base element
     * as the parent element.
     * @param nodePath
     * @param text
     */
    public void setAttributeValue(String nodePath, String text)
    {
        setAttributeValue(mainFragment.getBaseElement(), nodePath, text);
    }
    
    
    /**
     * Special method to set an id attribute value on selected element
     * @param nodePath
     * @param text
     */
    public void setIdAttribute(String nodePath, String text)
    {
        String eltPath = nodePath.substring(0, nodePath.lastIndexOf(PATH_SEPARATOR));
        Element elt = getElement(eltPath);
        setAttributeValue(nodePath, text);
        getXmlDocument().addId(text, elt);
    }
    
    
    /**
     * Creates a new element using the underlying DOM Document.
     * @param qname qname of the new element
     * @return
     */
    public Element createElement(XMLDocument parentDoc, String qname)
    {
        Element elt;
        QName qnameObj = getQName(parentDoc, qname);
        
        if (qnameObj.getNsUri() == null)
            elt = parentDoc.getDocument().createElement(qnameObj.getLocalName());
        else
            elt = parentDoc.getDocument().createElementNS(qnameObj.getNsUri(), qnameObj.getFullName());
        
        return elt;
    }
    
    
    /**
     * Creates a new element using the main document
     * @param qname
     * @return
     */
    public Element createElement(String qname)
    {
        return createElement(mainFragment.getXmlDocument(), qname);
    }


    /**
     * Follow the path if nodes already exist or create necessary elements
     * @param currentDocument
     * @param node
     * @param nodeType
     * @return
     */
    protected boolean writeNode(XMLDocument currentDocument, Node node, int nodeType)
    {
        NodeList children = getChildElements(node);
        int remainingLevels = comparePaths(currentDocument, currentPath, wantedPath);
        int nextIndex = wantedPath.length - remainingLevels;
        
        if (remainingLevels == 0)
        {
            matchingNodes.addNode(node);
            return true;
        }
            
        // we keep going down if path already exists
        if (remainingLevels != -1)
        {
            String qname = wantedPath[nextIndex];
            
            if (!qname.startsWith("+"))
            {
                for (int i = 0; i < children.getLength(); i++)
                {
                    currentPath.add(children.item(i).getNodeName());
                    boolean done = writeNode(currentDocument, children.item(i), nodeType);
                    if (done) return done;                    
                }
            }
            else
            {
                qname = qname.substring(1); // remove '+'
                wantedPath[nextIndex] = qname;
            }
            
            // if no child was matching or '+' was present, create it
            Element elt = createElement(currentDocument, qname);
            node.appendChild(elt);
            
            // call ourselves recursively with the new node
            currentPath.add(elt.getNodeName());
            return writeNode(currentDocument, elt, nodeType);
        }
        else
        {
            currentPath.remove(currentPath.size() - 1);
            return false;
        }
    }


    /**
     * This function is called recursively and look for the wanted nodes
     * <br> The matching nodes are added to the class variable 'matchingNodes'
     * @param currentDocument XML document where the search is performed
     * @param node The node from which we start the search
     * @param nodeTypeFilter node type to be retrieved
     * @param maxCount maximum number of matching nodes to look for
     * @return boolean true if the search is done, false otherwise
     */
    protected boolean readNode(XMLDocument currentDocument, Node node, int nodeTypeFilter, int maxCount)
    {
        int remainingLevels;
        boolean found = false;
        String href = null;

        NodeList children = getChildElements(node);
        NamedNodeMap attribs = node.getAttributes();
        short nodeType = node.getNodeType();

        // if found we record the match
        if ((remainingLevels = comparePaths(currentDocument, currentPath, wantedPath)) == 0)
            if (((nodeType == nodeTypeFilter) || (nodeTypeFilter == 0)))
            {
                matchingNodes.addNode(node);
                found = true;
            }

        // we look further only if the path starts good (remainingLevels = -1 otherwise)
        // and we didn't found the element yet
        if (!found && (remainingLevels != -1))
        {
            // read all the attributes
            if ((nodeType == Node.ELEMENT_NODE) && (attribs != null))
            {
                // check for xlink:href attribute only if the node doesn't have child elements
                if (children.getLength() == 0)
                {
                    for (int i = 0; i < attribs.getLength(); i++)
                    {
                        String attName = attribs.item(i).getLocalName();
                        if (attName == "href")
                            href = attribs.item(i).getNodeValue();
                    }
                }
                
                // only case where we look for attributes is we are at the last level in path!
                if (remainingLevels == 1 && nodeTypeFilter == Node.ATTRIBUTE_NODE)
                {
                    for (int i = 0; i < attribs.getLength(); i++)
                    {
                        // check if it's a match
                        if (((nodeTypeFilter == Node.ATTRIBUTE_NODE) || (nodeTypeFilter == 0)))
                        {
                            currentPath.add(attribs.item(i).getNodeName());

                            if ((remainingLevels = comparePaths(currentDocument, currentPath, wantedPath)) == 0)
                            {
                                // if found attribute add to the list and get out of the for loop
                                matchingNodes.addNode(attribs.item(i));
                                found = true;
                                break;
                            }

                            // if we're here attribute is not the good one so remove its name
                            currentPath.remove(currentPath.size() - 1);
                        }
                    }
                }
            }

            if (!found && (remainingLevels != -1))
            {
                // if it's a href attribute, we have to read from the linked file + id
                if (href != null)
                {
                    XMLFragment fragment = null;

                    try
                    {
                        fragment = getLinkedFragment(currentDocument, href);
                    }
                    catch (DOMHelperException e)
                    {
                        ExceptionSystem.display(new DOMHelperException("Linked object at " + href + " not found"));
                    }

                    if (fragment != null)
                    {
                        Node newNode = fragment.getBaseElement();
                        XMLDocument newDocument = fragment.getXmlDocument();
                        currentPath.add(newNode.getNodeName());
                        readNode(newDocument, newNode, nodeTypeFilter, maxCount);
                        currentPath.remove(currentPath.size() - 1);
                    }
                }

                // otherwise scan child nodes
                else
                {
                    for (int i = 0; i < children.getLength(); i++)
                    {
                        Node childNode = children.item(i);
                        currentPath.add(childNode.getNodeName());
                        boolean done = readNode(currentDocument, childNode, nodeTypeFilter, maxCount);
                        if (done == true) return true;
                        currentPath.remove(currentPath.size() - 1);
                    }
                }
            }
        }

        if (maxCount == matchingNodes.getLength())
            return true;
        else
            return false;
    }
    
    
    /**
     * Helper method to serialize this DOM node to a stream using the provided serializer
     * @param node
     * @param out
     * @param format
     * @param serializer
     * @throws IOException
     */
    public void serialize(Node node, OutputStream out, OutputFormat format, XMLSerializer serializer) throws IOException
    {
        // select root element to serialize
        Element elt = null;
        if (node.getNodeType() == Node.ELEMENT_NODE)
            elt = (Element)node;
        else if (node.getNodeType() == Node.DOCUMENT_NODE)
            elt = ((Document)node).getDocumentElement();
        else
            return;
        
        // use default format if no format specified
        if (format == null)
        {
            format = new OutputFormat();
            format.setMethod(Method.XML);
            format.setIndenting(true);
            format.setIndent(3);
            format.setLineWidth(0);
        }
        
        // add namespaces xmlns attributes
        XMLDocument parentDoc = getParentDocument(elt);
        Enumeration nsEnum = parentDoc.nsUriToPrefix.keys();
        while (nsEnum.hasMoreElements())
        {
            String uri = (String)nsEnum.nextElement();
            String prefix = parentDoc.getNSPrefix(uri);
            
            // add namespace attributes only if missing
            if (prefix.equals(QName.DEFAULT_PREFIX))
            {
                if (!elt.hasAttribute("xmlns"))
                    elt.setAttribute("xmlns", uri);
            }
            else
            {
                if (!elt.hasAttribute("xmlns:" + prefix))
                    elt.setAttribute("xmlns:" + prefix, uri);
            }
        }
        
        // setup serializer and launch serialization
        serializer.setOutputByteStream(out);
        serializer.setOutputFormat(format);
        serializer.setNamespaces(false);
        serializer.serialize(elt);
    }
    
    
    /**
     * Helper method to serialize this DOM to a stream
     * @param node
     * @param out
     * @param format use specified format or default format if null
     * @throws IOException
     */
    public void serialize(Node node, OutputStream out, OutputFormat format) throws IOException
    {
        XMLSerializer serializer = new XMLSerializer();
        serialize(node, out, format, serializer);
    }
    
    
    /**
     * Helper method to serialize xml in pretty or compact formats
     * @param node
     * @param out
     * @param pretty if true, adds CR/LF and indents ech child element
     * @throws IOException
     */
    public void serialize(Node node, OutputStream out, boolean pretty) throws IOException
    {
        OutputFormat myFormat = new OutputFormat();
        
        if (pretty)
        {
            myFormat.setMethod(Method.XML);
            myFormat.setIndenting(true);
            myFormat.setIndent(3);
            myFormat.setLineWidth(0);
        }
        else
        {
            myFormat.setMethod(Method.TEXT);
            myFormat.setIndenting(false);
            myFormat.setIndent(1);
            myFormat.setLineSeparator("");
            myFormat.setLineWidth(0);
        }
        
        serialize(node, out, myFormat);
    }


    /**
     * This function parse the XML file if necessary
     * @param inputStream Stream containing XML file we want to parse
     * @return The XMLDocument object created
     * @throws DOMHelperException
     */
    protected XMLFragment parseStream(InputStream inputStream, boolean addToTable) throws DOMHelperException
    {
        XMLDocument newDocument;

        if (inputStream == null)
            throw new IllegalArgumentException("inputStream can't be null");

        // parse xml stream
        newDocument = new XMLDocument(inputStream, validation);
        
        // add entry to loadedDocuments
        if (addToTable)
            loadedDocuments.put(inputStream.toString(), newDocument);

        return new XMLFragment(newDocument, newDocument.getDocumentElement());
    }


    /**
     * This function parse the XML file if necessary or reuse one already loaded if available
     * @param href link (URL#id) to the document
     * @param forceReload if true, force the document to reload
     * @return The XMLDocument object created
     * @throws DOMHelperException
     */
    protected XMLFragment parseURI(String href, boolean forceReload) throws DOMHelperException
    {
        URI uri;
        URIResolver uriResolver;
        XMLFragment xmlFragment = new XMLFragment();

        if (href == null)
            throw new IllegalArgumentException("Uri can't be null");

        // construct a new URL object from the String argument
        try
        {
            // make sure we replace spaces by UTF-8 encoded char
            href = href.replace(" ", "%20");
            uri = new URI(href);
            uriResolver = new URIResolver(uri);
            uri = uriResolver.getResolvedUri();
        }
        catch (URISyntaxException e)
        {
            throw new DOMHelperException("URI " + href + " is malformed");
        }
        
        // get docUri string w/o the fragment
        String docUri = uri.toString();
        int fragIndex = docUri.indexOf('#');
        if (fragIndex != -1)
            docUri = docUri.substring(0, fragIndex);

        // check if the same document is already opened
        // forces reload if uri contains a query string
        if (uri.getQuery() == null)
            xmlFragment.xmlDocument = (XMLDocument)loadedDocuments.get(docUri);

        // parse doc only if not already present in the table or forceReload is true
        if ((xmlFragment.xmlDocument == null) || (forceReload == true))
        {
            try
            {
                // parses the xml stream !!
                xmlFragment = parseStream(uriResolver.openStream(), false);
            }
            catch (IOException e)
            {
                throw new DOMHelperException("Error while opening XML stream", e);
            }

            // add an entry to the linkedDocuments table
            // don't do it if url includes a query string (usually means dynamic document!)
            if (uri.getQuery() == null)
                loadedDocuments.put(docUri, xmlFragment.xmlDocument);

            // also set the uri in the document
            xmlFragment.xmlDocument.setUri(uri);
        }
        else
        {
            xmlFragment.baseElement = xmlFragment.xmlDocument.getDocumentElement();
        }

        // change base element if there is an ID
        String id = uri.getFragment();
        if (id != null)
        {
            xmlFragment.baseElement = xmlFragment.xmlDocument.getElementByID(id);
        }

        return xmlFragment;
    }
    
    
    /**
     * Returns an xml element given its ID
     * @param currentDocument XMLDocument where to look for local IDs
     * @param idRef String containing the unique id (local or global with a url)
     * @return Element
     * @throws DOMHelperException
     */
    public XMLFragment getLinkedFragment(XMLDocument currentDocument, String idRef) throws DOMHelperException
    {
        XMLFragment xmlFragment;

        if (idRef.startsWith("#"))
        {
            xmlFragment = new XMLFragment();
            xmlFragment.xmlDocument = currentDocument;
            xmlFragment.baseElement = currentDocument.getElementByID(idRef.substring(1));
        }
        else
        {
            try
            {
                URI linkUri = new URI(idRef);                
                if (!linkUri.isAbsolute())
                {
                    URI baseUri = currentDocument.getUri();
                    linkUri = baseUri.resolve(linkUri);
                }                
                xmlFragment = parseURI(linkUri.toString(), false);
            }
            catch (URISyntaxException e)
            {
                throw new DOMHelperException("URI " + idRef + " is malformed");
            }
        }

        return xmlFragment;
    }
    
    
    /**
     * Retrieve XMLDocument this element is in
     * @param node Node
     * @return XMLDocument
     */
    public XMLDocument getParentDocument(Node node)
    {
        XMLDocument nextDoc = null;
        Enumeration docs = loadedDocuments.elements();

        // find corresponding document
        while (docs.hasMoreElements())
        {
            nextDoc = (XMLDocument) docs.nextElement();
            if (nextDoc.getDocument() == node.getOwnerDocument())
                break;
        }

        return nextDoc;
    }


    /**
     * Check if the paths are matching<br>
     * <br>Returns -1 if the beginning of wantedPath doesn't match actualPath<br>
     * <br>Otherwise returns the number levels still needed to reach the wantedPath
     * @param currentDocument XMLDocument
     * @param actualPath Actual path in the xml tree
     * @param wantedPath The path we are looking for
     * @return -1 or the number of levels still needed to reach the wanted node (positive integer)
     */
    protected int comparePaths(XMLDocument currentDocument, ArrayList<String> actualPath, String[] wantedPath)
    {
        if (actualPath.size() == 0)
            return wantedPath.length;
        
        String wantedName = wantedPath[actualPath.size() - 1];
        String actualName = actualPath.get(actualPath.size()-1);
        
        // remove '@' if present (XPath attribute style)
        if (wantedName.startsWith("@"))
        	wantedName = wantedName.substring(1);
        
        // check if last names in the path match

        // check if a namespace prefix is present (i.e. if ':' is present)
        if (wantedName.indexOf(":") != -1)
        {
            String qname = getQName(currentDocument, wantedName).getFullName();
            if (!qname.equals(actualName))
                return -1;
        }
        else // no namespace prefix specified - just use local name
        {
            // if wildcard any name is ok
            if (!wantedName.equals("*"))
            {
                int separatorIndex = actualName.indexOf(":");
                String nodeLocalName = actualName.substring(separatorIndex + 1);

                if (!wantedName.equals(nodeLocalName))
                    return -1;
            }
        }

        return wantedPath.length - actualPath.size();
    }
    
    
    /**
     * Creates a QName object from the given "prefix:name" String.
     * 
     * @param qname
     * @return
     */
    protected QName getQName(XMLDocument parentDoc, String qname)
    {
        eltQName.setFullName(qname);
        String nsUri = userPrefixTable.get(eltQName.prefix);
        
        if (nsUri == null)
        {
            if (eltQName.prefix != QName.DEFAULT_PREFIX)
                throw new IllegalStateException("No namespace URI defined for user prefix " + eltQName.prefix);
            
            return eltQName;
        }
        
        eltQName.nsUri = nsUri;
        
        // if there is already an existing prefix for this NS use it
        if (parentDoc != null)
        {
            String newPrefix = parentDoc.getNSPrefix(nsUri);
            if (newPrefix != null)
                eltQName.prefix = newPrefix;
            else
                parentDoc.addNS(eltQName.prefix, eltQName.nsUri);
        }
        
        return eltQName;
    }
}
