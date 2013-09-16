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
import org.vast.data.*;
import org.vast.util.ReaderException;
import org.vast.cdm.common.*;


/**
 * <p>
 * Parses CDM binary data stream using the data components structure
 * and the binary encoding information.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin & Gregoire Berthiau
 * @since Nov 22, 2005
 * @version 1.0
 */
public class BinaryDataParser extends AbstractDataParser
{
	protected static String BLOCK_READER = "BLOCK_READER";
    protected DataInputExt dataInput;
    protected InputStream input;
	protected boolean componentEncodingResolved = false;
	
	
	public BinaryDataParser()
	{
	}
	
	
	public void setInput(InputStream inputStream) throws IOException
	{
		InputStream dataIn = null;
		
		// use Base64 converter, and TODO swap bytes if necessary
		switch (((BinaryEncoding)dataEncoding).byteEncoding)
		{
			case BASE64:
				dataIn = new Base64Decoder(inputStream);
				break;
				
			case RAW:
				dataIn = inputStream;
				break;
				
			default:
				throw new ReaderException("Unsupported byte encoding");
		}
		
		// create data input stream
		if (((BinaryEncoding)dataEncoding).byteOrder == BinaryEncoding.ByteOrder.BIG_ENDIAN)
		    input = new DataInputStreamBI(new BufferedInputStream(dataIn));
        else if (((BinaryEncoding)dataEncoding).byteOrder == BinaryEncoding.ByteOrder.LITTLE_ENDIAN)
            input = new DataInputStreamLI(new BufferedInputStream(dataIn));
		
		dataInput = (DataInputExt)input;
	}

	
	public void parse(InputStream inputStream) throws IOException
	{
		stopParsing = false;
		
		try
		{
			setInput(inputStream);				
			
			// if no rawHandler is registered, parse each individual element
			if (rawHandler == null)
			{
				do
				{
					// stop if end of stream
					if (!moreData())
						break;
					
					processNextElement();
				}
				while(!stopParsing && !endOfArray);
			}
			
			// else just extract byte array
			// of the whole block size and send it directly.
			else
			{
				int size = (int)((BinaryEncoding)dataEncoding).byteLength;			
				int byteLeft = size;
				int byteRead = 0;
				byte [] buffer = new byte[size];
				
				do
				{
					byteRead = ((InputStream)dataInput).read(buffer, size-byteLeft, byteLeft);
					byteLeft -= byteRead;
					
					if (byteRead == -1)
						break;
					
					// send endDataAtom event				
					if (byteLeft == 0)
					{
						rawHandler.endData(dataComponents, buffer);
						buffer = new byte[size];
						byteLeft = size;
					}
				}
				while(!stopParsing);
			}
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
                //dataComponents.clearData();
			}
			catch (IOException e)
			{
			}
		}
	}
	
	
	//@Override
	public DataBlock parse() throws IOException
	{
	    try
        {
            do processNextElement();
            while(!isEndOfDataBlock());
        }
        catch (Exception e)
        {
            throw new ReaderException(STREAM_ERROR, e);
        }
	    
	    return dataComponents.getData();
	}
	
	
	@Override
	public void reset()
	{
		try
        {
            if (!componentEncodingResolved)
                resolveComponentEncodings();
        }
        catch (CDMException e)
        {
            throw new IllegalStateException("Invalid binary encoding mapping", e);
        }
		
		super.reset();
	}
	
	
	/**
	 * Maps a given scalar component to the corresponding BinaryValue
	 * object containing binary encoding information. This also
	 * forces the DataValues primitive type to be the same as the ones
	 * specified in the binary encoding section.
	 * @throws CDMException
	 */
	protected void resolveComponentEncodings() throws CDMException
	{
		BinaryOptions[] encodingList = ((BinaryEncoding)dataEncoding).componentEncodings;
				
		for (BinaryOptions binaryOpts: encodingList)
		{
			String [] dataPath = binaryOpts.componentName.split("/");
			DataComponent dataComponent = null;
			
			// find component in tree
            for (int j=0; j<dataPath.length; j++)
            {
                if (j==0)
                {
                    if (dataPath[0].equals(this.dataComponents.getName()))
                        dataComponent = this.dataComponents;
                }
                else
                    dataComponent = dataComponent.getComponent(dataPath[j]);
                
                if (dataComponent == null)
                {
                    throw new CDMException("Unknown component " + binaryOpts.componentName);
                }
            }
			
			// add binary info to component
            if (binaryOpts instanceof BinaryComponent)
            {
            	((DataValue)dataComponent).setDataType(((BinaryComponent)binaryOpts).type);
            	dataComponent.setEncodingInfo(binaryOpts);
            }
            else if(binaryOpts instanceof BinaryBlock)
            {
                dataComponent.setEncodingInfo(binaryOpts);
                initBlockReader(dataComponent, (BinaryBlock)binaryOpts);
            }
		}
		
		componentEncodingResolved = true;
	}
	
	
	protected void initBlockReader(DataComponent blockComponent, BinaryBlock binaryOpts) throws CDMException
    {
	    CompressedStreamParser reader = CodecLookup.getInstance().createDecoder(binaryOpts.compression);
        reader.init(blockComponent, binaryOpts);
        blockComponent.setProperty(BLOCK_READER, reader);
    }
	
	
	@Override
	protected void processAtom(DataValue scalarInfo) throws CDMException
	{
		// get next encoding block
		BinaryComponent binaryComponent = (BinaryComponent)scalarInfo.getEncodingInfo();
		
		// parse token = dataAtom					
		parseBinaryAtom(scalarInfo, binaryComponent);
	}
	
	
	@Override
	protected boolean processBlock(DataComponent blockInfo) throws CDMException
	{
		if (blockInfo instanceof DataChoice)
		{
			int choiceIndex = -1;
			
			// read implicit choice index
			try
			{
				choiceIndex = dataInput.readByte();
				((DataChoice)blockInfo).setSelected(choiceIndex);
			}
			catch (IllegalStateException e)
			{
				throw new CDMException(CHOICE_ERROR + choiceIndex);
			}
			catch (IOException e)
			{
				throw new CDMException(STREAM_ERROR);
			}
		}
		else
		{		
			// get block details
			BinaryBlock binaryBlock = (BinaryBlock)blockInfo.getEncodingInfo();
			
			// parse whole block at once if compression found
			if (binaryBlock != null)
			{
				parseBinaryBlock(blockInfo, binaryBlock);
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * Checks if more data is available from the stream
	 * @return true if more data needs to be parsed, false otherwise
	 */
	public boolean moreData() throws IOException
	{
		try
		{
			dataInput.mark(1);
			int result = dataInput.read();
			
			if (result == -1)
			{
				return false;
			}
			else
			{
				dataInput.reset();
				return true;
			}				
		}
		catch (IOException e)
		{
			throw new ReaderException(STREAM_ERROR, e);
		}
	}
	
	
	/**
	 * Parse binary block using DataInfo and DataEncoding structures
	 * Decoded value is assigned to each DataValue
	 * @param scalarInfo
	 * @param binaryInfo
	 * @throws CDMException
	 */
	private void parseBinaryAtom(DataValue scalarInfo, BinaryComponent binaryInfo) throws CDMException
	{
		try
		{
			switch (binaryInfo.type)
			{
				case BOOLEAN:
					boolean boolValue = dataInput.readBoolean();
					scalarInfo.getData().setBooleanValue(boolValue);					
					break;
				
				case BYTE:
					byte byteValue = dataInput.readByte();
					scalarInfo.getData().setByteValue(byteValue);
					break;
					
				case UBYTE:
					int ubyteValue = dataInput.readUnsignedByte();
					scalarInfo.getData().setIntValue(ubyteValue);
					break;
					
				case SHORT:
					short shortValue = dataInput.readShort();
					scalarInfo.getData().setShortValue(shortValue);	
					break;
					
				case USHORT:
					int ushortValue = dataInput.readUnsignedShort();
					scalarInfo.getData().setIntValue(ushortValue);
					break;
					
				case INT:
					int intValue = dataInput.readInt();
					scalarInfo.getData().setIntValue(intValue);
					break;
					
				case UINT:
					long uintValue = dataInput.readUnsignedInt();
					scalarInfo.getData().setLongValue(uintValue);
					break;
					
				case LONG:
					long longValue = dataInput.readLong();
					scalarInfo.getData().setLongValue(longValue);
					break;
					
				case ULONG:
					long ulongValue = dataInput.readUnsignedLong();
					scalarInfo.getData().setLongValue(ulongValue);
					break;
					
				case FLOAT:
					float floatValue = dataInput.readFloat();
					scalarInfo.getData().setFloatValue(floatValue);
					break;
					
				case DOUBLE:
					double doubleValue = dataInput.readDouble();
					scalarInfo.getData().setDoubleValue(doubleValue);
					break;
					
				case UTF_STRING:
					String utfValue = dataInput.readUTF();
					scalarInfo.getData().setStringValue(utfValue);
					break;
					
				case ASCII_STRING:
					String asciiValue = dataInput.readASCII();
					scalarInfo.getData().setStringValue(asciiValue);
					break;
			}
		}
		catch (RuntimeException e)
		{
            throw new CDMException("Error while parsing component " + scalarInfo.getName(), e);
		}
		catch (IOException e)
		{
			throw new CDMException("Error while reading binary stream", e);
		}
	}

	
	/**
	 * Parse binary block using DataInfo and DataEncoding structures
	 * Decoded value is assigned to each DataValue
	 * @param scalarInfo
	 * @param binaryInfo
	 * @throws CDMException
	 */
	private void parseBinaryBlock(DataComponent blockInfo, BinaryBlock binaryInfo) throws CDMException
	{
		// TODO: PADDING IS TAKEN CARE OF HERE... 
	    CompressedStreamParser reader = (CompressedStreamParser)blockInfo.getProperty(BLOCK_READER);
		reader.decode(dataInput, blockInfo);					
	}
	
	
    @Override
    public void close() throws IOException
    {
        input.close();
    }
}