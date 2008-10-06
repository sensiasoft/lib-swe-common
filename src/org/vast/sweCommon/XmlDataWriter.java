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
		}
		catch (XMLStreamException e)
		{
			throw new CDMException(e);
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
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	protected void processAtom(DataValue scalarInfo) throws CDMException
	{
		try
        {
            String val = scalarInfo.getData().getStringValue();
            String eltName = scalarInfo.getName();
            
            if (prefix != null && namespace != null)
            	writer.writeStartElement(prefix, eltName, namespace);           	
            else
            	writer.writeStartElement(eltName);            	
            
            writer.writeCharacters(val);
            writer.writeEndElement();
        }
        catch (XMLStreamException e)
        {
            throw new CDMException("Error while writing XML stream", e);
        }
	}
	
	
	@Override
	protected boolean processBlock(DataComponent blockInfo) throws CDMException
	{
		return true;	
	}
	
	
	public void flush() throws CDMException
	{
		try
		{
			writer.flush();
		}
		catch (XMLStreamException e)
		{
			throw new CDMException(e);
		}
	}

}