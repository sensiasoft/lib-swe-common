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
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.ScalarComponent;
import org.vast.data.AbstractArrayImpl;
import org.vast.data.XMLEncodingImpl;
import org.vast.util.WriterException;


/**
 * <p>
 * Writes CDM XML data stream using the given data components
 * structure and encoding information.
 * </p>
 *
 * @author Alex Robin
 * @since Feb 29, 2008
 * */
public class XmlDataWriter extends AbstractDataWriter
{
	protected XMLStreamWriter xmlWriter;
	protected String namespace;
	protected String prefix;
	protected int prevStackSize = 0;
	

	public XmlDataWriter()
	{	    
	}
	
	
	@Override
    public void setOutput(OutputStream outputStream) throws IOException
    {
	    try
        {
    	    XMLOutputFactory factory = XMLOutputFactory.newInstance();
            xmlWriter = factory.createXMLStreamWriter(outputStream);
            
            namespace = ((XMLEncodingImpl)dataEncoding).getNamespace();
            prefix = ((XMLEncodingImpl)dataEncoding).getPrefix();
        }
	    catch (XMLStreamException e)
        {
            throw new WriterException(STREAM_ERROR, e);
        }
    }
	
	
	@Override
    public void close() throws IOException
    {
        try
        {
            xmlWriter.flush();
            xmlWriter.close();
        }
        catch (XMLStreamException e)
        {
            throw new WriterException(STREAM_ERROR, e);
        }
    }
    
    
    @Override
    public void flush() throws IOException
    {
        try
        {
            xmlWriter.flush();
        }
        catch (XMLStreamException e)
        {
            throw new WriterException(STREAM_ERROR, e);
        }
    }
	
	
    @Override
	public void write(OutputStream outputStream) throws IOException
	{
		try
        {
		    super.write(outputStream);
		    closeElements();
        }
        catch (Exception e)
        {
            throw new WriterException(STREAM_ERROR, e);
        }
	}
	
	
	protected void closeElements() throws XMLStreamException
	{
		if (prevStackSize > componentStack.size())
		{
			while (prevStackSize > componentStack.size())
			{
				xmlWriter.writeEndElement();
				prevStackSize--;
			}
		}
		else
			prevStackSize = componentStack.size();
		
		if (componentStack.size() == 0)
		    xmlWriter.writeCharacters("\n");
	}
	
	
	@Override
	protected boolean processBlock(DataComponent component) throws IOException
	{
		try
		{
			closeElements();
			
			String eltName = component.getName();
            
			if (namespace != null)
            {
                if (prefix != null)
                    xmlWriter.writeStartElement(prefix, eltName, namespace);
                else
                    xmlWriter.writeStartElement("", eltName, namespace);
            }
            else
                xmlWriter.writeStartElement(eltName);
			
			return true;
		}
		catch (XMLStreamException e)
		{
			throw new WriterException("Error writing data for block component " + component.getName(), e);
		}	
	}
	
	
	@Override
	protected void processAtom(ScalarComponent component) throws IOException
	{
		try
        {
			closeElements();
			String localName = component.getName();
			
			if (localName.equals(AbstractArrayImpl.ELT_COUNT_NAME))
			{
				xmlWriter.writeAttribute(localName, component.getData().getStringValue());
			}
			else
			{
				String eltName = component.getName();
	            
	            if (namespace != null)
	            {
	            	if (prefix != null)
	            	    xmlWriter.writeStartElement(prefix, eltName, namespace);
	            	else
	            	    xmlWriter.writeStartElement(namespace, eltName);
	            }
	            else
	            	xmlWriter.writeStartElement(eltName);
	            
	            xmlWriter.writeCharacters(dataTypeUtils.getStringValue(component));
	            xmlWriter.writeEndElement();
	        }
        }
        catch (XMLStreamException e)
        {
            throw new WriterException("Error writing data for scalar component " + component.getName(), e);
        }
	}
}