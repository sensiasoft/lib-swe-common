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
public class AsciiDataWriter extends DataWriter
{
	String nextToken;
	int tupleSize;
	char[] tokenSep, tupleSep;
    boolean firstToken, newBlock;
    Writer outputWriter;
    
	
	public AsciiDataWriter()
	{
	}
	
	
	/**
	 * Start writing data coming to the given stream
	 */
	public void write(OutputStream outputStream) throws CDMException
	{
		try
		{
            outputWriter = new OutputStreamWriter(outputStream);
			tokenSep = ((AsciiEncoding)dataEncoding).tokenSeparator.toCharArray();
			firstToken = true;
            newBlock = false;
            
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
				e.printStackTrace();
			}
		}
	}
    
    
    public void reset()
    {
        super.reset();
        newBlock = true;
    }
	
	
    @Override
	protected void processAtom(DataValue scalarInfo) throws CDMException
	{
		try
        {
            if (!firstToken)
            {
                if (newBlock)
                {
                    outputWriter.write(tupleSep);
                    newBlock = false;
                }
                else
                    outputWriter.write(tokenSep);
            }
            else
                firstToken = false;
            
            String val = scalarInfo.getData().getStringValue();
            outputWriter.write(val);
        }
        catch (IOException e)
        {
            throw new CDMException("Error while writing ASCII tuple stream", e);
        }
	}
}