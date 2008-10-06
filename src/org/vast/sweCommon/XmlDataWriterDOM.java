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

package org.vast.sweCommon;

import java.io.*;
import org.vast.data.*;
import org.vast.xml.DOMHelper;
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
public class XmlDataWriterDOM extends AbstractDataWriter
{
	protected DOMHelper dom;
	protected Element currentParent;
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
			
			NodeList children = dom.getChildElements(dom.getRootElement());
			for (int i=0; i<children.getLength(); i++)
				dom.serialize(children.item(i), outputStream, true);
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
		
		currentParent = parentElt;
		
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
		if (prevStackSize > componentStack.size())
		{
			while (prevStackSize > componentStack.size())
			{
				currentParent = (Element)currentParent.getParentNode();
				prevStackSize--;
			}
		}
		else
			prevStackSize = componentStack.size();
	}
	
	
	@Override
	protected boolean processBlock(DataComponent blockInfo) throws CDMException
	{
		setCurrentParent();
		
		String eltName = getElementName(blockInfo);
		currentParent = dom.addElement(currentParent, eltName);		
		
		return true;
	}
	
	
	@Override
	protected void processAtom(DataValue scalarInfo) throws CDMException
	{
		setCurrentParent();
		
		if (!scalarInfo.getName().equals(SweConstants.SELECTED_ITEM_NAME))
		{
			String eltName = getElementName(scalarInfo);
			dom.setElementValue(currentParent, eltName, getStringValue(scalarInfo));
		}
	}
	
	
	public void flush() throws CDMException
	{
	}
}