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

package org.vast.swe;

import java.io.*;
import java.util.Stack;


/**
 * <p>
 * Simple filter to be able to parse the DataDefinition
 * using DOM before the end of the XML document is reached
 * and start processing inline data as it comes.<br/>
 * 
 * The filter starts by passing data through while recording
 * the element hierarchy down to the element containing the data stream.
 * When this element is reached its content is not read and
 * the filter simulates a valid end of the xml tree by writing
 * closing tags for all elements previously recorded, then EOF. 
 * </p>
 *
 * @author Alex Robin
 * @since Aug 19, 2005
 * */
public class SWEFilter extends InputStream
{
	InputStream source;
	boolean startTag = false;
	boolean readData = false;
	boolean finishXML = false;
	boolean comment = false;
	StringReader endOfXML;
	StringBuffer buf;
	String dataEltName;
	Stack<String> elementTree;
	
	
	public SWEFilter(InputStream source)
	{
		this.source = source;
		this.elementTree = new Stack<String>();
		this.buf = new StringBuffer(200);
	}
	
	
	@Override
    public int read() throws IOException
	{
		if (dataEltName == null)
			throw new IOException("Cannot start filtering if data element name is not set");
		
		if (readData)
		{
			int val = source.read();
				
			// simulate eof if closing xml tag is reached
			if ((char)val == '<' || val == -1)
			{
				readData = false;
				return -1;
			}

			return val;				
		}
		else if (finishXML)
		{
			int val = endOfXML.read();
			return val;
		}
		else
		{
			int val = source.read();
			
			if (((char)val) == '>')
			{
				// stop comment when we get -->
				if (comment)
				{
					if ( (buf.charAt(buf.length() - 1) == '-') &&
						 (buf.charAt(buf.length() - 2) == '-') )
						comment = false;
				}
				
				// skip comments, xml header and simple elements
				else if ((buf.charAt(0) != '!') && (buf.charAt(0) != '?') && (buf.charAt(buf.length() - 1) != '/'))
				{
					// end element tag
					if (buf.charAt(0) == '/')
					{
						elementTree.pop();
					}
					// start element tag
					else
					{
						int endName = buf.indexOf(" ");
						endName = (endName == -1) ? buf.length() : endName;
						String qName = buf.substring(0, endName);
							
						// data tag
						if ( qName.endsWith(dataEltName) )
						{
							finishXML = true;
							
							if (buf.charAt(buf.length() - 1) != '/')
								elementTree.push(qName);
							
							// construct valid end of xml
							StringBuffer endXML = new StringBuffer(60);
							
							while(!elementTree.isEmpty())
								endXML.append("</" + elementTree.pop() + ">");
							
							this.endOfXML = new StringReader(endXML.toString());
						}
						else
						{
							elementTree.push(qName);
						}
					}
				}
			}
			else if (startTag)
			{
				// start comment when we get <!
				if (((char)val) == '!')
					comment = true;
				
				buf.append((char)val);
			}
			
			if (((char)val) == '<')
			{
				startTag = true;
				buf.setLength(0);
			}
				
			return val;
		}
	}
	
	
	@Override
    public void close() throws IOException
	{
		// don't close the stream unless we are parsing the data
		if (readData)
			source.close();
	}
	
	
	public void startReadingData()
	{
		readData = true;
	}
	
	
	/**
	 * Sets element name that contains the data stream values (ascii, base64, etc.)
	 * TODO setDataElementName method description
	 * @param eltName
	 */
	public void setDataElementName(String eltName)
	{
		this.dataEltName = eltName;
	}
}
