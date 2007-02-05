/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sweCommon;

import java.io.*;

import org.vast.cdm.common.*;
import org.vast.data.*;


public class AsciiDataParser extends AbstractDataParser
{
	String nextToken;
	int tupleSize;
	char[] tokenSep, tupleSep;
	
	
	public AsciiDataParser()
	{
	}
	
	
	/**
	 * Start parsing data coming from the given stream
	 */
	public void parse(InputStream inputStream) throws CDMException
	{
        stopParsing = false;
        
        try
		{
			Reader reader = new InputStreamReader(inputStream);
			tokenSep = ((AsciiEncoding)dataEncoding).tokenSeparator.toCharArray();
			tupleSep = ((AsciiEncoding)dataEncoding).tupleSeparator.toCharArray();
			
			do
			{
				// get next token
				this.nextToken = this.nextToken(reader);
				
				// stop if end of stream
				if (this.nextToken == null)
					break;
				
				processNextElement();
			}
			while(!stopParsing);
		}
		catch (IOException e)
		{
			throw new CDMException("Error while reading ASCII tuple stream", e);
		}
		finally
		{
			try
			{
				inputStream.close();
                dataComponents.clearData();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Extract next token from stream
	 * Discard any character with ASCII code less than 32 (space)
	 * @param inputStream
	 * @return next token as a String
	 * @throws IOException
	 */
	private String nextToken(Reader inputReader) throws IOException
	{
		StringBuffer tokenBuf = new StringBuffer();
		int tokenSepIndex = 0;
		int tupleSepIndex = 0;
		boolean endToken = false;
		boolean endTuple = false;
		int nextChar;
		
		
		// skip all invalid characters and go to beginning of token
		do
		{
			nextChar = inputReader.read();
			
            // to support single char separators below ASCII code 32 
            if (nextChar == (int)tokenSep[0] || nextChar == (int)tupleSep[0])
                break;
            
			if (nextChar == -1)
				return null;
		}
		while (nextChar < 32);

		
		// add characters until we find separator or end of token
		while (nextChar != -1)
		{
			tokenBuf.append((char)nextChar);
						
			// check for token separator
			tokenSepIndex = 1;
			while ((tokenSepIndex <= tokenSep.length) &&
				   (tokenSep[tokenSep.length - tokenSepIndex] == tokenBuf.charAt(tokenBuf.length() - tokenSepIndex)))
				tokenSepIndex++;
			
			if (tokenSepIndex > tokenSep.length)
			{
				endToken = true;
				break;
			}
			
			// check for tuple separator
			tupleSepIndex = 1;
			while ((tupleSepIndex <= tupleSep.length) &&
					(tupleSep[tupleSep.length - tupleSepIndex] == tokenBuf.charAt(tokenBuf.length() - tupleSepIndex)))
				tupleSepIndex++;
			
			if (tupleSepIndex > tupleSep.length)
			{
				endTuple = true;
				break;
			}
			
			nextChar = inputReader.read();
		}
		
		// remove separator characters from buffer
		if (endToken)
			tokenBuf.setLength(tokenBuf.length() - tokenSepIndex + 1);
		else if (endTuple)
			tokenBuf.setLength(tokenBuf.length() - tupleSepIndex + 1);
		
		return tokenBuf.toString();
	}
	
	
    @Override
	protected void processAtom(DataValue scalarInfo) throws CDMException
	{
		char decimalSep = ((AsciiEncoding)dataEncoding).decimalSeparator;
		SweComponentReaderV0.parseToken(scalarInfo, this.nextToken, decimalSep);
	}
}