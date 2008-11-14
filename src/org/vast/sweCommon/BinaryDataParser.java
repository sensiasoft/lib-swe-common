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
import java.util.Hashtable;
import org.vast.data.*;
import org.vast.cdm.common.*;


/**
 * <p><b>Title:</b><br/>
 * Binary Data Parser
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Parses CDM binary data stream using the data components structure
 * and the binary encoding information.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin & Gregoire Berthiau
 * @date Nov 22, 2005
 * @version 1.0
 */
public class BinaryDataParser extends AbstractDataParser
{
	protected Hashtable<DataComponent, BinaryOptions> componentEncodings;
	protected DataInputExt dataInput;
	
	
	public BinaryDataParser()
	{
	}
	
	
	public void setInput(InputStream inputStream) throws CDMException
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
				throw new CDMException("Unsupported byte encoding");
		}
		
		// create data input stream
		if (((BinaryEncoding)dataEncoding).byteOrder == BinaryEncoding.ByteOrder.BIG_ENDIAN)
		    dataInput = new DataInputStreamBI(new BufferedInputStream(dataIn));
        else if (((BinaryEncoding)dataEncoding).byteOrder == BinaryEncoding.ByteOrder.LITTLE_ENDIAN)
            dataInput = new DataInputStreamLI(new BufferedInputStream(dataIn));
	}

	
	/**
	 * 
	 */
	public void parse(InputStream inputStream) throws CDMException
	{
		stopParsing = false;
		
		try
		{
			setInput(inputStream);				
			
			// if a dataHandler is registered, parse each individual element
			if (dataHandler != null)
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
			
			// if a raw handler is registered, just extract byte arrays
			// of the whole block size and send it directly.
			else if (rawHandler != null)
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
		catch (IOException e)
		{
			throw new CDMException("Error while reading binary stream", e);
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
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public void reset() throws CDMException
	{
		if (componentEncodings == null)
			resolveComponentEncodings();
		
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
		componentEncodings = new Hashtable<DataComponent, BinaryOptions>();
		BinaryOptions[] encodingList = ((BinaryEncoding)dataEncoding).componentEncodings;
				
		for (int i=0; i<encodingList.length; i++)
		{
			String [] dataPath = encodingList[i].componentName.split("/");
			DataComponent data = null;
			
            for (int j=0; j<dataPath.length; j++)
            {
                if (j==0)
                {
                    if (dataPath[0].equals(this.dataComponents.getName()))
                        data = this.dataComponents;
                }
                else
                    data = data.getComponent(dataPath[j]);
                
                if (data == null)
                {
                    throw new CDMException("Unknown component " + encodingList[i].componentName);
                }
            }
			
			// add this mapping to the Hashtable
            if(encodingList[i] instanceof BinaryComponent){
            	componentEncodings.put((DataValue)data, encodingList[i]);
            	((DataValue)data).setDataType(((BinaryComponent)encodingList[i]).type);
            }
            else if(encodingList[i] instanceof BinaryBlock){
            	componentEncodings.put(data, encodingList[i]);
            	((BinaryBlock)encodingList[i]).buildReader(data);
            }
		}
	}
	
	
	@Override
	protected void processAtom(DataValue scalarInfo) throws CDMException
	{
		// get next encoding block
		BinaryComponent binaryComponent = (BinaryComponent)componentEncodings.get(scalarInfo);
		
		// parse token = dataAtom					
		parseBinaryAtom(scalarInfo, binaryComponent);
	}
	
	
	@Override
	protected boolean processBlock(DataComponent blockInfo) throws CDMException
	{
		// get block details
		BinaryBlock binaryBlock = (BinaryBlock)componentEncodings.get(blockInfo);
		
		// parse whole block at once
		if (binaryBlock != null)
		{
			parseBinaryBlock(blockInfo, binaryBlock);
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Checks if more data is available from the stream
	 * @return true if more data needs to be parsed, false otherwise
	 */
	protected boolean moreData()
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
			e.printStackTrace();
			return false;
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
		(binaryInfo.reader).readCompressedStream(dataInput, blockInfo);
		System.gc();
					
	}

}