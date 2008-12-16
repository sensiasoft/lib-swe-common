/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SWE Common Data Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sweCommon;

import java.io.*;
import org.vast.data.*;
import org.vast.xml.DOMHelper;
import org.vast.cdm.common.*;
import org.w3c.dom.Element;


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
public class XmlDataWriterDOM extends AbstractDataWriter
{
	protected DOMHelper dom;
	protected Element currentParentElt;
	protected String namespace;
	protected String prefix;
	protected int prevStackSize = 0;
	
	
	public XmlDataWriterDOM()
	{
	}
	
	
	public void write(OutputStream outputStream) throws CDMException
	{
		try
		{
			DOMHelper dom = new DOMHelper("SweData");
			write(dom, dom.getRootElement());
			dom.serialize(dom.getRootElement().getFirstChild(), outputStream, true);
		}
		catch (IOException e)
		{
			throw new CDMException(e);
		}
	}
	
	
	public void write(DOMHelper dom, Element parentElt) throws CDMException
	{
		this.dom = dom;
		
		namespace = ((XmlEncoding)dataEncoding).namespace;
		prefix = ((XmlEncoding)dataEncoding).prefix;
		if (prefix == null)
			prefix = "data";
		
		if (namespace != null)
			dom.addUserPrefix(prefix, namespace);
		
		currentParentElt = parentElt;
		
		do processNextElement();
		while(!stopWriting);
		
		dataComponents.clearData();
	}
	
	
	protected String getElementName(DataComponent component)
	{
		String eltName = "+";
		
		if (namespace != null)
			eltName += prefix + ":" + component.getName();
		else
			eltName += component.getName();
			
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
	
	
	@Override
	protected boolean processBlock(DataComponent blockInfo) throws CDMException
	{
		setCurrentParent();
		
		String eltName = getElementName(blockInfo);
		currentParentElt = dom.addElement(currentParentElt, eltName);	
		
		return true;
	}
	
	
	@Override
	protected void processAtom(DataValue scalarInfo) throws CDMException
	{
		setCurrentParent();
		String localName = scalarInfo.getName();
		String eltName = getElementName(scalarInfo);		
		String val = getStringValue(scalarInfo);
		
		if (localName.equals(SweConstants.ELT_COUNT_NAME))
			dom.setAttributeValue(currentParentElt, localName, val);
		else if (!localName.equals(SweConstants.SELECTED_ITEM_NAME))
			dom.setElementValue(currentParentElt, eltName, getStringValue(scalarInfo));
	}
	
	
	public void flush() throws CDMException
	{
	}
}