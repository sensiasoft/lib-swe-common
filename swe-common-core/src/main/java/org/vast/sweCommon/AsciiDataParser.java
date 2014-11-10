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
import java.text.ParseException;
import net.opengis.swe.v20.TextEncoding;
import org.vast.cdm.common.DataBlock;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataType;
import org.vast.data.*;
import org.vast.util.DateTimeFormat;
import org.vast.util.ReaderException;


public class AsciiDataParser extends AbstractDataParser
{
	protected int tupleSize;
	protected char[] tokenSep, blockSep;
	protected char decimalSep;
	protected boolean collapseWhiteSpaces = true;
	protected StringBuffer tokenBuf = new StringBuffer();
	protected String lastToken;
	//protected Reader reader;
	protected InputStream reader;
    boolean consecutiveTokenSep = false;
    
    
	public AsciiDataParser()
	{
	}
	
	
	public void setInput(InputStream inputStream) throws IOException
	{
		//reader = new BufferedReader(new InputStreamReader(inputStream));
	    reader = new BufferedInputStream(inputStream);
		tokenSep = ((TextEncoding)dataEncoding).getTokenSeparator().toCharArray();
		blockSep = ((TextEncoding)dataEncoding).getBlockSeparator().toCharArray();
		decimalSep = ((TextEncoding)dataEncoding).getDecimalSeparator().charAt(0);
		collapseWhiteSpaces = ((TextEncoding)dataEncoding).getCollapseWhiteSpaces();
	}
	
	
	/**
	 * Start parsing data coming from the given stream
	 */
	public void parse(InputStream inputStream) throws IOException
	{
        stopParsing = false;
        
        try
		{
        	setInput(inputStream);
			
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
		finally
		{
			try
			{
				inputStream.close();
				// dataComponents.clearData();
			}
			catch (IOException e)
			{
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
	private String readToken() throws IOException
	{
		int tokenSepIndex = 0;
		int blockSepIndex = 0;
		boolean endToken = false;
		boolean endBlock = false;
		int nextChar;		
		
		try
		{
			// if a token has already been parsed in moreData() just return it
		    if (lastToken != null)
            {
		        String token = lastToken;
		        lastToken = null;
		        return token;
            }
		    
			tokenBuf.setLength(0);
			
			// collapse white space characters
			do
			{
				nextChar = reader.read();
				
			    // to support single char separators below ASCII code 32 
			    //if (nextChar == (int)tokenSep[0] || nextChar == (int)blockSep[0])
			    //    break;

				if (nextChar == -1)
					return null;
			}
			while (nextChar <= 32 && collapseWhiteSpaces);

			
			// add characters until we find token or block separator
			while (nextChar != -1)
			{
				tokenBuf.append((char)nextChar);
				
				// for 2 sets of tokenSeparator without data
				if(tokenBuf.length()==1 && tokenBuf.charAt(0)==tokenSep[0]){
					int i = 0;
					while(tokenBuf.length()==(i+1) && tokenBuf.charAt(i)==tokenSep[i]){
						if(i==tokenSep.length-1)
						{
							consecutiveTokenSep = true;
							break;
						}
						nextChar = reader.read();
						tokenBuf.append((char)nextChar);
						i++;
					}
				}
			
				if (consecutiveTokenSep)
				{
					endToken = true;
					break;
				}
			
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
			
				// check for block separator
				blockSepIndex = 1;
				while ((blockSepIndex <= blockSep.length) &&
						(blockSep[blockSep.length - blockSepIndex] == tokenBuf.charAt(tokenBuf.length() - blockSepIndex)))
					blockSepIndex++;
			
				if (blockSepIndex > blockSep.length)
				{
					endBlock = true;
					break;
				}
				
				nextChar = reader.read();
			}		
			
			// remove separator characters from buffer
			if (endToken)
			{
				tokenBuf.setLength(tokenBuf.length() - tokenSepIndex + 1);
				if(consecutiveTokenSep)
				{
					tokenBuf.setLength(0);
					consecutiveTokenSep = false;
				}
				
			}
			else if (endBlock)
				tokenBuf.setLength(tokenBuf.length() - blockSepIndex + 1);
			
			return tokenBuf.toString();
		}
		catch (IOException e)
		{
			throw new ReaderException(STREAM_ERROR, e);
		}
	}
	
	
    @Override
	protected void processAtom(DataValue component) throws IOException
	{
        String token = null;
        
        try
        {
            token = readToken();
            parseToken(component, token, decimalSep);
        }
        catch (Exception e)
        {
            if ((e instanceof NumberFormatException) || (e instanceof ParseException))
                throw new ReaderException("Invalid value '" + token + "' for scalar component '" + component.getName() + "'", e);
            else
                throw new ReaderException(STREAM_ERROR, e);
        }
	}
    
    
    /**
	 * Checks if more data is available from the stream
	 * @return true if more data needs to be parsed, false otherwise
	 */
	protected boolean moreData() throws IOException
	{
	    lastToken = readToken();
        return !(lastToken == null);
	}
    
        
    /**
     * Parse a token from a tuple depending on the corresponding Data Component Definition 
     * @param scalarInfo
     * @param token
     * @param decimalSep character to be used as the decimal separator. (don't change anything and assume '.' if 0)
     * @param dataBlock the DataBlock to contain the read data
     * @throws CDMException
     * @throws NumberFormatException
     */
    protected void parseToken(AbstractSimpleComponentImpl component, String token, char decimalSep) throws Exception
    {
        // get component data type
        DataBlock data = component.getData();
        DataType dataType = data.getDataType();
        //System.out.println(scalarInfo.getName() + ": " + token);
        
        // always replace decimal separator by '.'
        if (decimalSep != 0)
            token.replace(decimalSep, '.');
               
        switch (dataType)
        {
            case BOOLEAN:
                try
                {
                    int intValue = Integer.parseInt(token);
                    if ((intValue != 0) && (intValue != 1))
                        throw new ParseException("", 0);
                    
                    data.setBooleanValue(intValue != 0);
                }
                catch (NumberFormatException e)
                {
                    boolean boolValue = Boolean.parseBoolean(token);
                    data.setBooleanValue(boolValue);
                } 
                break;
                
            case BYTE:
                byte byteValue = Byte.parseByte(token);
                data.setByteValue(byteValue);
                break;
                
            case SHORT:
            case UBYTE:
                short shortValue = Short.parseShort(token);
                data.setShortValue(shortValue);
                break;
                
            case INT:
            case USHORT:
                int intValue = Integer.parseInt(token);
                data.setIntValue(intValue);
                break;
                
            case LONG:
            case UINT:
            case ULONG:
                long longValue = Long.parseLong(token);
                data.setLongValue(longValue);
                break;
                
            case FLOAT:
                float floatValue = Float.parseFloat(token);
                data.setFloatValue(floatValue);
                break;
                
            case DOUBLE:
                double doubleValue = parseDoubleOrInfOrIsoTime(token);
                data.setDoubleValue(doubleValue);                     
                break;
                
            case UTF_STRING:
            case ASCII_STRING:
                data.setStringValue(token);
                break;
                
            default:
                throw new IllegalArgumentException("Unsupported datatype " + dataType);
        }
    }


	@Override
	protected boolean processBlock(DataComponent component) throws IOException
	{
		if (component instanceof DataChoiceImpl)
		{
			String token = null;
			
			// read implicit choice token
			try
			{
				token = readToken();
				((DataChoiceImpl)component).setSelectedComponent(token);
			}
			catch (IllegalStateException e)
			{
				throw new ReaderException(CHOICE_ERROR + token);
			}
		}
		
		return true;
	}		
   
    
    @Override
    public void close() throws IOException
    {
        reader.close();
    }
    
    
    /**
     * Improves on Java Double.parseDouble() method to include +INF/-INF
     * This is needed because Java Infinity is not allowed by XML schema
     * @param text string to decode value from
     * @return decoded value
     * @throws NumberFormatException if argument doesn't contain a valid double string
     */
    public static double parseDoubleOrInf(String text) throws NumberFormatException
    {
        double val;
        
        try
        {
            val = Double.parseDouble(text);
        }
        catch (NumberFormatException e)
        {
            if (text.equalsIgnoreCase("-INF"))
                val = Double.NEGATIVE_INFINITY;
            else if (text.equalsIgnoreCase("INF") || text.equalsIgnoreCase("+INF"))
                val = Double.POSITIVE_INFINITY;
            else
                throw e; 
        }
        
        return val;
    }
    
    
    /**
     * Allows parsing a double or ISO encoded date/time value
     * @param text string to decode value from
     * @return decoded value
     * @throws NumberFormatException 
     */
    public static double parseDoubleOrInfOrIsoTime(String text) throws NumberFormatException
    {
        double val;
        
        try
        {
            val = parseDoubleOrInf(text);
        }
        catch (NumberFormatException e)
        {
            try
            {
                val = DateTimeFormat.parseIso(text);
            }
            catch (ParseException e1)
            {
                if (text.equals("NO_DATA"))
                    return Double.NaN;
                else
                    throw e;
            } 
        }
        
        return val;
    }
}