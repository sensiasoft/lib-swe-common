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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.ScalarComponent;
import net.opengis.swe.v20.TextEncoding;
import org.vast.data.DataChoiceImpl;
import org.vast.util.WriterException;


/**
 * <p>
 * TODO AsciiDataWriter type description
 * </p>
 *
 * @author Alex Robin
 * @since Feb 10, 2006
 * */
public class AsciiDataWriter extends AbstractDataWriter
{
    protected String nextToken;
    protected int tupleSize;
    protected char[] tokenSep, blockSep;
    protected boolean firstToken;
    protected Writer outputWriter;
    
	
	public AsciiDataWriter()
	{
        firstToken = true;
	}
	
	
	@Override
    public void setOutput(OutputStream outputStream) throws IOException
    {
	    outputWriter = new OutputStreamWriter(outputStream);
        tokenSep = ((TextEncoding)dataEncoding).getTokenSeparator().toCharArray();
        blockSep = ((TextEncoding)dataEncoding).getBlockSeparator().toCharArray();
    }
	
	
	@Override
    public void reset()
    {
        super.reset();
        firstToken = true;
    }
	
	
	@Override
    public void close() throws IOException
    {
	    outputWriter.flush();
        outputWriter.close();
    }
	
	
	@Override
    public void flush() throws IOException
    {
	    outputWriter.flush();      
    }
	
	
    @Override
	protected void processAtom(ScalarComponent component) throws IOException
	{
        try
        {
            String val = dataTypeUtils.getStringValue(component);
            writeToken(val);
        }
        catch (IOException e)
        {
            throw new WriterException("Error writing value for scalar component " + component.getName(), e);
        }
	}
    
    
    protected void writeToken(String token) throws IOException
    {
        if (firstToken)
            firstToken = false;
        else
            outputWriter.write(tokenSep);
        
        if (token != null)
            outputWriter.write(token);
    }


    @Override
	protected boolean processBlock(DataComponent component) throws IOException
	{
		if (component instanceof DataChoiceImpl)
		{
			String token = null;
			
			// write implicit choice token
			try
			{
				token = ((DataChoiceImpl)component).getSelectedItem().getName();
				writeToken(token);
			}
			catch (IllegalStateException e)
			{
				throw new WriterException(CHOICE_ERROR, e);
			}
		}
		
		return true;
	}
    
    
    @Override
    protected void endDataBlock() throws IOException
    {
        if (!endOfArray)
            outputWriter.write(blockSep);
    }
}