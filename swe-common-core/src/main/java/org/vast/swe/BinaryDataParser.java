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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.BinaryMember;
import net.opengis.swe.v20.ByteOrder;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.ScalarComponent;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.CompressedStreamParser;
import org.vast.cdm.common.DataInputExt;
import org.vast.data.AbstractDataComponentImpl;
import org.vast.data.BinaryBlockImpl;
import org.vast.data.BinaryComponentImpl;
import org.vast.data.DataBlockCompressed;
import org.vast.data.DataChoiceImpl;
import org.vast.util.ReaderException;


/**
 * <p>
 * Parses CDM binary data stream using the data components structure
 * and the binary encoding information.
 * </p>
 *
 * @author Alex Robin
 * @since Nov 22, 2005
 * */
public class BinaryDataParser extends AbstractDataParser
{
	protected DataInputExt dataInput;
    protected InputStream input;
	protected boolean componentEncodingResolved = false;
	
	
	public BinaryDataParser()
	{
	}
	
	
	@Override
    public void setInput(InputStream inputStream) throws IOException
	{
		InputStream dataIn = null;
		
		// use Base64 converter
		switch (((BinaryEncoding)dataEncoding).getByteEncoding())
		{
			case BASE_64:
				dataIn = new BufferedInputStream(new Base64Decoder(inputStream), 1024);
				break;
				
			case RAW:
				dataIn = inputStream;
				break;
				
			default:
				throw new ReaderException("Unsupported byte encoding");
		}
		
		// create data input stream
		if (((BinaryEncoding)dataEncoding).getByteOrder() == ByteOrder.LITTLE_ENDIAN)
            input = new DataInputStreamLI(dataIn);
		else
		    input = new DataInputStreamBI(dataIn);
		
		dataInput = (DataInputExt)input;
	}

	
	@Override
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
				int size = (int)((BinaryEncoding)dataEncoding).getByteLength();		
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
		    inputStream.close();
            //dataComponents.clearData();
		}
	}
	
	
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
	    // TODO need to also call initBlockReader(dataComponent, (BinaryBlockImpl)binaryOpts);
	    SWEHelper.assignBinaryEncoding(dataComponents, (BinaryEncoding)dataEncoding);
        componentEncodingResolved = true;
	}
	
	
	protected void initBlockReader(DataComponent blockComponent, BinaryBlockImpl binaryOpts) throws CDMException
    {
	    CompressedStreamParser reader = CodecLookup.getInstance().createDecoder(binaryOpts.getCompression());
	    if (reader != null)
	    {
	        binaryOpts.setBlockReader(reader);
	        reader.init(blockComponent, binaryOpts);
	    }
    }
    
    
    /**
     * Checks if more data is available from the stream
     * @return true if more data needs to be parsed, false otherwise
     * @throws IOException
     */
    @Override
    public boolean moreData() throws IOException
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
	
	
	@Override
	protected void processAtom(ScalarComponent scalarComponent) throws IOException
	{
		// get next encoding block
	    BinaryMember binaryInfo = ((AbstractDataComponentImpl)scalarComponent).getEncodingInfo();
		
		// parse token = dataAtom					
		parseBinaryAtom(scalarComponent, binaryInfo);
	}
	
	
	@Override
	protected boolean processBlock(DataComponent blockComponent) throws IOException
	{
		if (blockComponent instanceof DataChoiceImpl)
		{
			int choiceIndex = -1;
			
			// read implicit choice index
			try
			{
				choiceIndex = dataInput.readByte();
				((DataChoiceImpl)blockComponent).setSelectedItem(choiceIndex);
			}
			catch (Exception e)
			{
				throw new ReaderException(CHOICE_ERROR + choiceIndex, e);
			}
		}
		else
		{		
			// get block encoding details
			BinaryMember binaryInfo = ((AbstractDataComponentImpl)blockComponent).getEncodingInfo();
			
			// parse whole block at once if compression found
			if (binaryInfo != null)
			{
				parseBinaryBlock(blockComponent, (BinaryBlockImpl)binaryInfo);
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * Parse binary block using DataInfo and DataEncoding structures
	 * Decoded value is assigned to each DataValue
	 * @param scalarInfo
	 * @param binaryInfo
	 * @throws IOException
	 */
	private void parseBinaryAtom(ScalarComponent scalarInfo, BinaryMember binaryInfo) throws IOException
	{
		DataType dataType = ((BinaryComponentImpl)binaryInfo).getCdmDataType();
		
	    try
		{
			switch (dataType)
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
					
				default:
                    throw new ReaderException("Unsupported datatype " + dataType);
			}
		}
		catch (RuntimeException e)
		{
            throw new ReaderException("Error while parsing component " + scalarInfo.getName(), e);
		}
	}

	
	/**
	 * Parse binary block using DataInfo and DataEncoding structures
	 * Decoded value is assigned to each DataValue
	 * @param blockComponent
	 * @param binaryInfo
	 * @throws CDMException
	 */
	private void parseBinaryBlock(DataComponent blockComponent, BinaryBlockImpl binaryInfo) throws IOException
	{
		try
        {
            // TODO: PADDING IS TAKEN CARE OF HERE... 
            
            DataBlock dataBlock = blockComponent.getData();
            
            if (dataBlock instanceof DataBlockCompressed)
            {
                // read block size
                int blockSize = dataInput.readInt();
                byte[] bytes = new byte[blockSize];
                dataInput.readFully(bytes);
                dataBlock.setUnderlyingObject(bytes);
            }
            
            // otherwise need to uncompress on-the-fly
            else
            {
                // if decoder is specified, uncompress data now
                CompressedStreamParser reader = binaryInfo.getBlockReader();
                if (reader != null)
                {
                    reader.decode(dataInput, blockComponent);
                }
            }
        }
        catch (IOException | CDMException e)
        {
            throw new ReaderException("Cannot parse binary block", e);
        }
	}
	
	
    @Override
    public void close() throws IOException
    {
        input.close();
    }
}