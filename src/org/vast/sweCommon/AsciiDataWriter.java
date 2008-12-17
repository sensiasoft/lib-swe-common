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
import org.vast.cdm.common.*;
import org.vast.data.*;


/**
 * <p><b>Title:</b><br/>
 * Ascii Data Writer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO AsciiDataWriter type description
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Feb 10, 2006
 * @version 1.0
 */
public class AsciiDataWriter extends AbstractDataWriter
{
	String nextToken;
	int tupleSize;
	char[] tokenSep, blockSep;
    boolean firstToken, appendBlockSeparator;
    Writer outputWriter;
    
	
	public AsciiDataWriter()
	{
	}
	
	
	/**
	 * Start writing data to the given output stream
	 */
	public void write(OutputStream outputStream) throws CDMException
	{
		try
		{
            outputWriter = new OutputStreamWriter(outputStream);
			tokenSep = ((AsciiEncoding)dataEncoding).tokenSeparator.toCharArray();
			blockSep = ((AsciiEncoding)dataEncoding).blockSeparator.toCharArray();
			firstToken = true;
            appendBlockSeparator = false;
            
			do processNextElement();
			while(!stopWriting);
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
    
    
    public void reset() throws CDMException
    {
        super.reset();
        appendBlockSeparator = true;
    }
	
	
    @Override
	protected void processAtom(DataValue scalarInfo) throws CDMException
	{
    	String val = getStringValue(scalarInfo);
        //System.out.println(scalarInfo.getName() + ": " + val);
    	
        writeToken(val);
	}
    
    
    protected void writeToken(String token) throws CDMException
    {
    	try
		{
			if (!firstToken)
			{
			    // write a block separator if this is a new block
				if (appendBlockSeparator)
			    {
			        outputWriter.write(blockSep);
			        appendBlockSeparator = false;
			    }
			    else
			        outputWriter.write(tokenSep);
			}
			else
				firstToken = appendBlockSeparator = false;
			
			outputWriter.write(token);
		}
		catch (IOException e)
		{
			throw new CDMException(STREAM_ERROR, e);
		}
    }


	public void flush() throws CDMException
	{
		try
		{
			outputWriter.flush();
		}
		catch (IOException e)
		{
			throw new CDMException(STREAM_ERROR, e);
		}		
	}


	@Override
	protected boolean processBlock(DataComponent blockInfo) throws CDMException
	{
		if (blockInfo instanceof DataChoice)
		{
			String token = null;
			
			// write implicit choice token
			try
			{
				token = ((DataChoice)blockInfo).getSelectedComponent().getName();
				writeToken(token);
			}
			catch (IllegalStateException e)
			{
				throw new CDMException(CHOICE_ERROR);
			}
		}
		
		return true;
	}
}