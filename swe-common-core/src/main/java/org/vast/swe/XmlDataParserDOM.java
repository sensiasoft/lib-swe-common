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

package org.vast.swe;

import java.io.IOException;
import java.io.InputStream;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.ScalarComponent;
import org.vast.data.AbstractArrayImpl;
import org.vast.data.DataChoiceImpl;
import org.vast.util.ReaderException;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p>
 * Writes CDM XML data stream using the given data components
 * structure directly inside a DOM.
 * </p>
 *
 * @author Alex Robin
 * @since Feb 29, 2008
 * */
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
	
	
	@Override
    public void setInput(InputStream inputStream) throws IOException
	{
		try
		{
			this.dom = new DOMHelper(inputStream, false);
		}
		catch (DOMHelperException e)
		{
			throw new XMLReaderException("Error while parsing XML stream", e);
		}
	}
	
	
	@Override
    public void parse(InputStream inputStream) throws IOException
	{
		setInput(inputStream);
		read(dom, dom.getRootElement());
	}
	
	
	public void read(DOMHelper dom, Element parentElt) throws IOException
	{
		this.dom = dom;
		
		/*namespace = ((XmlEncoding)dataEncoding).namespace;
		prefix = "data";
		
		if (namespace != null)
			dom.addUserPrefix(prefix, namespace);*/
		
		currentParentElt = parentElt;
		numRecord = dom.getChildElements(parentElt).getLength(); 
			
		try
        {
            do
            {
            	// stop if end of stream
            	if (!moreData())
            		break;
            	
            	processNextElement();
            }
            while(!stopParsing && !endOfArray);
        }
        catch (Exception e)
        {
            throw new ReaderException(STREAM_ERROR, e);
        }
		
		//dataComponents.clearData();
	}
	
	
	/**
	 * Checks if more data is available from the stream
	 * @return true if more data needs to be parsed, false otherwise
	 */
	@Override
    protected boolean moreData()
	{
		if (newBlock && recordCounter >= numRecord)
			return false;
		else
			return true;
	}
	
	
	@Override
	public void reset()
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
	
	
	protected Element getCurrentElement(DataComponent componentInfo) throws IOException
	{
		setCurrentParent();
		String eltName = getElementName(componentInfo);
		
		if (!dom.existElement(currentParentElt, eltName))
			throw new XMLReaderException("Missing XML element: " + eltName);
		
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
	protected boolean processBlock(DataComponent blockComponent) throws IOException
	{
		currentParentElt = getCurrentElement(blockComponent);
		
		if (blockComponent instanceof DataChoiceImpl)
		{
			// figure out choice selection
			Element firstChild = dom.getFirstChildElement(currentParentElt);
			((DataChoiceImpl)blockComponent).setSelectedItem(firstChild.getLocalName());
		}
		
		/* deal with optional block here
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
	protected void processAtom(ScalarComponent component) throws IOException
	{
		setCurrentParent();
		String localName = component.getName();
		String val;
		
		// special case of array size -> read from elementCount attribute
		if (localName.equals(AbstractArrayImpl.ELT_COUNT_NAME))
		{
			val = dom.getAttributeValue(currentParentElt, localName);
		}
		
		// else read from element content
		else
		{
			Element currentElt = getCurrentElement(component);
			val = dom.getElementValue(currentElt);
		}
		
		//System.out.println(scalarInfo.getName());
		try
        {
            tokenParser.parseToken(component, val, (char)0);
        }
        catch (Exception e)
        {
            throw new ReaderException("Invalid value '" + val + "' for scalar component '" + localName + "'", e);
        }
	}
	
	
    @Override
    public void close() throws IOException
    {        
    }
}