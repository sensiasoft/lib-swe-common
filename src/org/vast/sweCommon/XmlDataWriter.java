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
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.vast.data.*;
import org.vast.cdm.common.*;


/**
 * <p><b>Title:</b><br/>
 * XML Data Writer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Writes CDM XML data stream using the given data components
 * structure and encoding information.
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin
 * @date Feb 29, 2008
 * @version 1.0
 */
public class XmlDataWriter extends AbstractDataWriter
{
	protected XMLStreamWriter writer;
	protected String namespace;
	protected String prefix;
	protected int prevStackSize = 0;
	

	public void write(OutputStream outputStream) throws CDMException
	{
		try
		{
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			writer = factory.createXMLStreamWriter(outputStream);
			
			namespace = ((XmlEncoding)dataEncoding).namespace;
			prefix = ((XmlEncoding)dataEncoding).prefix;
			if (prefix == null)
				prefix = "data";
			
			do processNextElement();
			while(!stopWriting);
			
			closeElements();
		}
		catch (XMLStreamException e)
		{
			throw new CDMException(STREAM_ERROR, e);
		}
		finally
		{
			try
			{
				outputStream.close();
                dataComponents.clearData();
			}
			catch (IOException e)
			{
				throw new CDMException(STREAM_ERROR, e);
			}
		}
	}
	
	
	protected void closeElements() throws XMLStreamException
	{
		if (prevStackSize > componentStack.size())
		{
			while (prevStackSize > componentStack.size())
			{
				writer.writeEndElement();
				prevStackSize--;
			}
		}
		else
			prevStackSize = componentStack.size();
	}
	
	
	@Override
	protected boolean processBlock(DataComponent blockInfo) throws CDMException
	{
		try
		{
			closeElements();
			
			String eltName = blockInfo.getName();
            
            if (namespace != null)
            	writer.writeStartElement(prefix, eltName, namespace);
            else
            	writer.writeStartElement(eltName); 
			
			return true;
		}
		catch (XMLStreamException e)
		{
			throw new CDMException(STREAM_ERROR, e);
		}	
	}
	
	
	@Override
	protected void processAtom(DataValue scalarInfo) throws CDMException
	{
		try
        {
			closeElements();
			String localName = scalarInfo.getName();
			
			if (localName.equals(SweConstants.ELT_COUNT_NAME))
			{
				writer.writeAttribute(localName, scalarInfo.getData().getStringValue());
			}
			else
			{
				String eltName = scalarInfo.getName();
	            
	            if (namespace != null)
	            	writer.writeStartElement(prefix, eltName, namespace);           	
	            else
	            	writer.writeStartElement(eltName);
	            
	            writer.writeCharacters(getStringValue(scalarInfo));
	            writer.writeEndElement();
	        }
        }
        catch (XMLStreamException e)
        {
            throw new CDMException(STREAM_ERROR, e);
        }
	}
	
	
	public void flush() throws CDMException
	{
		try
		{
			writer.flush();
		}
		catch (XMLStreamException e)
		{
			throw new CDMException(STREAM_ERROR, e);
		}
	}

}