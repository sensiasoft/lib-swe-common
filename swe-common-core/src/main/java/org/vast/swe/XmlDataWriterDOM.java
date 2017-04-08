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
import java.io.OutputStream;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.ScalarComponent;
import org.vast.data.AbstractArrayImpl;
import org.vast.data.XMLEncodingImpl;
import org.vast.util.WriterException;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p>
 * Writes CDM XML data stream using the given data components
 * structure directly inside a DOM.
 * </p>
 *
 * @author Alex Robin
 * @since Feb 29, 2008
 * */
public class XmlDataWriterDOM extends AbstractDataWriter
{
	protected DOMHelper dom;
	protected Element currentParentElt;
	protected String namespace;
	protected String prefix;
	protected int prevStackSize = 0;
	protected OutputStream outputStream;
	
	
	public XmlDataWriterDOM()
	{
	}
	
	
    @Override
    public void setOutput(OutputStream outputStream) throws IOException
    {
	    // in this writer the output stream is not always used
	    // since we're usually writing to the DOM tree!
	    this.outputStream = outputStream;
	    
	    if (dom == null)
	    {
	        dom = new DOMHelper("SweData");
	        init(dom, dom.getRootElement()); 
	    }
    }
    
    
    @Override
	public void write(OutputStream outputStream) throws IOException
	{
		DOMHelper dom = new DOMHelper("SweData");
		write(dom, dom.getRootElement());
	}
	
	
	@Override
    public void write(DataBlock data) throws IOException
    {
	    try
	    {
    	    do processNextElement();
            while(!isEndOfDataBlock());
            
            dataComponents.clearData();
        }
        catch (Exception e)
        {
            throw new WriterException(STREAM_ERROR, e);
        }
    }
	
	
	public void write(DOMHelper dom, Element parentElt) throws IOException
	{
	    init(dom, parentElt);
		
	    try
        {
            do processNextElement();
            while(!isEndOfDataBlock());
            
            dataComponents.clearData();
        }
        catch (Exception e)
        {
            throw new WriterException(STREAM_ERROR, e);
        }
	}
	
	
	@Override
    public void flush() throws IOException
    {
        dom.serialize(dom.getRootElement(), outputStream, true);
    }
    
    
    @Override
    public void close() throws IOException
    {
        this.flush();
        outputStream.close();
    }
    
    
    protected void init(DOMHelper dom, Element parentElt) throws IOException
    {
        this.dom = dom;
        this.currentParentElt = parentElt;
        
        namespace = ((XMLEncodingImpl)dataEncoding).getNamespace();
        prefix = ((XMLEncodingImpl)dataEncoding).getPrefix();
        if (prefix == null)
            prefix = "data";
        
        if (namespace != null)
            dom.addUserPrefix(prefix, namespace);
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
	protected boolean processBlock(DataComponent blockInfo) throws IOException
	{
		setCurrentParent();
		
		String eltName = getElementName(blockInfo);
		currentParentElt = dom.addElement(currentParentElt, eltName);	
		
		return true;
	}
	
	
	@Override
	protected void processAtom(ScalarComponent scalarInfo) throws IOException
	{
		setCurrentParent();
		String localName = scalarInfo.getName();
		String eltName = getElementName(scalarInfo);		
		String val = dataTypeUtils.getStringValue(scalarInfo);
		
		if (localName.equals(AbstractArrayImpl.ELT_COUNT_NAME))
			dom.setAttributeValue(currentParentElt, localName, val);
		else
			dom.setElementValue(currentParentElt, eltName, val);
	}
}