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
    Alexandre Robin <alexandre.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sweCommon;

import java.io.*;
import org.vast.data.*;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.vast.cdm.common.*;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p><b>Title:</b><br/>
 * XML Data Writer DOM
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Writes CDM XML data stream using the given data components
 * structure directly inside a DOM.
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin
 * @date Feb 29, 2008
 * @version 1.0
 */
public class XmlDataParserDOM extends AbstractDataParser
{
	protected DOMHelper dom;
	protected Element currentParentElt;
	protected String namespace;
	protected String prefix;
	protected int prevStackSize = 0;
	protected AsciiDataParser tokenParser;
	protected int recordCounter;
	protected int numRecord;
	
	
	public XmlDataParserDOM()
	{
		tokenParser = new AsciiDataParser();
	}
	
	
	public void setInput(InputStream inputStream) throws CDMException
	{
		try
		{
			this.dom = new DOMHelper(inputStream, false);
		}
		catch (DOMHelperException e)
		{
			throw new CDMException("Error while parsing XML stream", e);
		}
	}
	
	
	public void parse(InputStream inputStream) throws CDMException
	{
		setInput(inputStream);
		read(dom, dom.getRootElement());
	}
	
	
	public void read(DOMHelper dom, Element parentElt) throws CDMException
	{
		this.dom = dom;
		
		/*namespace = ((XmlEncoding)dataEncoding).namespace;
		prefix = "data";
		
		if (namespace != null)
			dom.addUserPrefix(prefix, namespace);*/
		
		currentParentElt = parentElt;
		numRecord = dom.getChildElements(parentElt).getLength(); 
			
		do
		{
			// stop if end of stream
			if (!moreData())
				break;
			
			processNextElement();
		}
		while(!stopParsing && !endOfArray);
		
		//dataComponents.clearData();
	}
	
	
	/**
	 * Checks if more data is available from the stream
	 * @return true if more data needs to be parsed, false otherwise
	 */
	protected boolean moreData()
	{
		if (newBlock && recordCounter >= numRecord)
			return false;
		else
			return true;
	}
	
	
	@Override
	public void reset() throws CDMException
	{
		super.reset();
	}


	protected String getElementName(DataComponent component)
	{
		String eltName;
		
		if (namespace != null)
			eltName = prefix + ":" + component.getName();
		else
			eltName = component.getName();
			
		return eltName;
	}
	
	
	protected void setCurrentParent()
	{
		// if stack size has decreased, we need to go up the tree
		if (prevStackSize > componentStack.size())
		{
			// go back to parent of next item
			while (prevStackSize > componentStack.size())
			{
				currentParentElt = (Element)currentParentElt.getParentNode();
				prevStackSize--;
			}
		}
		
		// else we just save the stack size
		else
			prevStackSize = componentStack.size();
	}
	
	
	protected Element getCurrentElement(DataComponent componentInfo)
	{
		setCurrentParent();
		String eltName = getElementName(componentInfo);
		
		// case of element at root of block
		if (componentStack.isEmpty())
		{
			NodeList elts = dom.getElements(currentParentElt, eltName);
			Element nextRootElt = (Element)elts.item(recordCounter);
			recordCounter++;
			return nextRootElt;
		}
		
		Record parentRecord = componentStack.peek();
		
		// case of multiple children in DataArray
		if (parentRecord.component instanceof DataArray)
		{
			NodeList elts = dom.getElements(currentParentElt, eltName);
			return (Element)elts.item(parentRecord.index);
		}
		
		// any other case
		else
			return dom.getElement(currentParentElt, eltName);
	}
	
	
	@Override
	protected boolean processBlock(DataComponent blockInfo) throws CDMException
	{
		currentParentElt = getCurrentElement(blockInfo);
		
		/* deal with optional values here
		if (currentParentElt == null)
		{
			// skip children if the block is optional
			if (blockInfo.getProperty(SweConstants.OPTIONAL) != null)
				return false;
			else
				throw new CDMException("Component " + blockInfo.getName() + " is missing");
		}
		*/
		
		//System.out.println(blockInfo.getName());
		
		return true;
	}
	
	
	@Override
	protected void processAtom(DataValue scalarInfo) throws CDMException
	{
		setCurrentParent();
		String localName = scalarInfo.getName();
		String val;
		
		// figure out choice selection
		if (localName.equals(SweConstants.SELECTED_ITEM_NAME))
		{
			Element firstChild = dom.getFirstChildElement(currentParentElt);
			DataComponent choice = componentStack.peek().component;
			int selectedIndex = choice.getComponentIndex(firstChild.getLocalName());
			scalarInfo.getData().setIntValue(selectedIndex);
		}
		else
		{		
			// special case of array size -> read from elementCount attribute
			if (localName.equals(SweConstants.ELT_COUNT_NAME))
			{
				val = dom.getAttributeValue(currentParentElt, localName);
			}
			
			// else read from element content
			else
			{
				Element currentElt = getCurrentElement(scalarInfo);
				val = dom.getElementValue(currentElt);
			}
			
			//System.out.println(scalarInfo.getName());
			tokenParser.parseToken(scalarInfo, val, (char)0);
		}
	}
}