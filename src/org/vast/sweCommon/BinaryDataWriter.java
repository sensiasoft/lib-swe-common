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
 * Binary Data Writer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Writes CDM binary data stream using the given data components
 * structure and binary encoding information. This supports raw
 * binary and base64 for now.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Feb 10, 2006
 * @version 1.0
 */
public class BinaryDataWriter extends DataWriter
{
	SWEOutputStream dataOutput;
	Hashtable<DataValue, BinaryOptions> componentEncodings;
	
	
	public BinaryDataWriter()
	{
	}

	
	/**
	 * 
	 */
	public void write(OutputStream outputStream) throws CDMException
	{
		OutputStream dataOut = null;
		stopWriting = false;
		
		try
		{
			// use Base64 converter, and TODO swap bytes if necessary
			switch (((BinaryEncoding)dataEncoding).byteEncoding)
			{
				case BASE64:
					dataOut = new Base64Encoder(outputStream);
					break;
					
				case RAW:
					dataOut = outputStream;
					break;
					
				default:
					throw new CDMException("Unsupported byte encoding");
			}
			
			// if a dataHandler is registered, parse each individual element
			if (dataHandler != null)
			{
				dataOutput = new SWEOutputStream(new BufferedOutputStream(dataOut));
				
				do processNextElement();
				while(!stopWriting);
			}
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
	protected void resolveComponentEncodings()
	{
		componentEncodings = new Hashtable<DataValue, BinaryOptions>();
		BinaryOptions[] encodingList = ((BinaryEncoding)dataEncoding).componentEncodings;
				
		for (int i=0; i<encodingList.length; i++)
		{
			String [] dataPath = encodingList[i].componentName.split("/");
			DataComponent data = null;
			
            for (int j=0; j<dataPath.length; j++)
            {
                if (j==0)
                {
                    if (dataPath[0].equals(dataComponents.getName()))
                        data = dataComponents;
                }
                else
                    data = data.getComponent(dataPath[j]);
                
                if (data == null)
                {
                    System.err.println("Unknown component " + dataPath[j]);
                    continue;
                }
            }
			
			// add this mapping to the Hashtable
			componentEncodings.put((DataValue)data, encodingList[i]);
			
			// modify DataValue dataType
			switch (encodingList[i].type)
			{
				case UBYTE:
					((DataValue)data).setDataType(DataType.SHORT);
					break;
			
				case USHORT:
					((DataValue)data).setDataType(DataType.INT);
					break;
					
				case UINT:
					((DataValue)data).setDataType(DataType.LONG);
					break;
					
				case ULONG:
					((DataValue)data).setDataType(DataType.LONG);
					break;
					
				default:
					((DataValue)data).setDataType(encodingList[i].type);
			}		
		}
	}
	
	
	@Override
	protected void processAtom(DataValue scalarInfo) throws CDMException
	{
		// get next encoding block
		BinaryOptions binaryBlock = componentEncodings.get(scalarInfo);
		
		// parse token = dataAtom					
		writeBinaryAtom(scalarInfo, binaryBlock);
	}
	
	
	/**
	 * Parse binary block using DataInfo and DataEncoding structures
	 * Decoded value is assigned to each DataValue
	 * @param scalarInfo
	 * @param binaryInfo
	 * @throws CDMException
	 */
	private void writeBinaryAtom(DataValue scalarInfo, BinaryOptions binaryInfo) throws CDMException
	{
		try
		{
			switch (scalarInfo.getDataType())
			{
				case BOOLEAN:
					boolean boolValue = scalarInfo.getData().getBooleanValue();
                    dataOutput.writeBoolean(boolValue);										
					break;
				
				case BYTE:
					byte byteValue = scalarInfo.getData().getByteValue();
                    dataOutput.writeByte(byteValue);
					break;
					
				case UBYTE:
					short ubyteValue = scalarInfo.getData().getShortValue();
					dataOutput.writeUnsignedByte(ubyteValue);
					break;
					
				case SHORT:
					short shortValue = scalarInfo.getData().getShortValue();
                    dataOutput.writeByte(shortValue);
					break;
					
				case USHORT:
                    int ushortValue = scalarInfo.getData().getIntValue();
					dataOutput.writeUnsignedShort(ushortValue);
					break;
					
				case INT:
					int intValue = scalarInfo.getData().getIntValue();
                    dataOutput.writeInt(intValue);
					break;
					
				case UINT:
					long uintValue = scalarInfo.getData().getLongValue();
					dataOutput.writeUnsignedInt(uintValue);
					break;
					
				case LONG:
					long longValue = scalarInfo.getData().getLongValue();
                    dataOutput.writeLong(longValue);
					break;
					
				case ULONG:
                    long ulongValue = scalarInfo.getData().getLongValue();
                    dataOutput.writeLong(ulongValue);
					break;
					
				case FLOAT:
					float floatValue = scalarInfo.getData().getFloatValue();
                    dataOutput.writeFloat(floatValue);
					break;
					
				case DOUBLE:
                    double doubleValue = scalarInfo.getData().getDoubleValue();
                    dataOutput.writeDouble(doubleValue);
					break;
					
				case UTF_STRING:
					String utfValue = scalarInfo.getData().getStringValue();
                    dataOutput.writeUTF(utfValue);
					break;
					
				case ASCII_STRING:
                    String asciiValue = scalarInfo.getData().getStringValue();
                    dataOutput.writeASCII(asciiValue);
					break;
			}
		}
		catch (RuntimeException e)
		{
			throw new CDMException("Error while writing component " + scalarInfo.getName(), e);
		}
		catch (IOException e)
		{
			throw new CDMException("Error while writing binary stream", e);
		}
	}
}