/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.

 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe;

import java.io.IOException;
import java.io.OutputStream;
import net.opengis.swe.v20.BinaryBlock;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.BinaryMember;
import net.opengis.swe.v20.ByteEncoding;
import net.opengis.swe.v20.ByteOrder;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.ScalarComponent;
import org.vast.util.WriterException;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataOutputExt;
import org.vast.data.AbstractDataComponentImpl;
import org.vast.data.BinaryComponentImpl;
import org.vast.data.DataBlockCompressed;
import org.vast.data.DataChoiceImpl;


/**
 * <p>
 * Writes CDM binary data stream using the given data components
 * structure and binary encoding information. This supports raw
 * binary and base64 for now.
 * </p>
 *
 * @author Alex Robin
 * @since Feb 10, 2006
 * */
public class BinaryDataWriter extends AbstractDataWriter
{
    protected DataOutputExt dataOutput;
	protected boolean componentEncodingResolved = false;
	
	
	public BinaryDataWriter()
	{
	}

	
	@Override
	public void setOutput(OutputStream outputStream) throws IOException
	{
	    ByteEncoding byteEnc = ((BinaryEncoding)dataEncoding).getByteEncoding();
	    
	    switch (byteEnc)
        {
            case BASE_64:
                // use streaming Base64 converter
                outputStream = new Base64Encoder(outputStream);
                break;
                
            case RAW:
                break;
                
            default:
                throw new WriterException("Unsupported byte encoding: " + byteEnc);
        }
        
        // create right data output stream
        if (((BinaryEncoding)dataEncoding).getByteOrder() == ByteOrder.LITTLE_ENDIAN)
            dataOutput = new DataOutputStreamLI(outputStream);
        else
            dataOutput = new DataOutputStreamBI(outputStream);
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
	
	
	@Override
	public void close() throws IOException
	{
	    dataOutput.flush();
        dataOutput.close();
	}
	
	
	@Override
	public void flush() throws IOException
    {
	    dataOutput.flush();
    }	
	

	protected void resolveComponentEncodings() throws CDMException
	{
		SWEHelper.assignBinaryEncoding(dataComponents, (BinaryEncoding)dataEncoding);
		componentEncodingResolved = true;
	}
	
	
	@Override
	protected void processAtom(ScalarComponent component) throws IOException
	{
		// get encoding info for component
		BinaryMember binaryInfo = ((AbstractDataComponentImpl)component).getEncodingInfo();
		writeBinaryAtom(component, binaryInfo);
	}
	
	
	/**
	 * Parse binary component using info and encoding options
	 * Decoded value is assigned to each DataValue
	 * @param component
	 * @param binaryInfo
	 * @throws WriterException
	 */
	private void writeBinaryAtom(ScalarComponent component, BinaryMember binaryInfo) throws WriterException
	{
	    DataType dataType = ((BinaryComponentImpl)binaryInfo).getCdmDataType();
        DataBlock data = component.getData();
        
        try
        {
            switch (dataType)
            {
                case BOOLEAN:
                    boolean boolValue = data.getBooleanValue();
                    dataOutput.writeBoolean(boolValue);                                     
                    break;
                
                case BYTE:
                    byte byteValue = data.getByteValue();
                    dataOutput.writeByte(byteValue);
                    break;
                    
                case UBYTE:
                    short ubyteValue = data.getShortValue();
                    dataOutput.writeUnsignedByte(ubyteValue);
                    break;
                    
                case SHORT:
                    short shortValue = data.getShortValue();
                    dataOutput.writeShort(shortValue);
                    break;
                    
                case USHORT:
                    int ushortValue = data.getIntValue();
                    dataOutput.writeUnsignedShort(ushortValue);
                    break;
                    
                case INT:
                    int intValue = data.getIntValue();
                    dataOutput.writeInt(intValue);
                    break;
                    
                case UINT:
                    long uintValue = data.getLongValue();
                    dataOutput.writeUnsignedInt(uintValue);
                    break;
                    
                case LONG:
                    long longValue = data.getLongValue();
                    dataOutput.writeLong(longValue);
                    break;
                    
                case ULONG:
                    long ulongValue = data.getLongValue();
                    dataOutput.writeLong(ulongValue);
                    break;
                    
                case FLOAT:
                    float floatValue = data.getFloatValue();
                    dataOutput.writeFloat(floatValue);
                    break;
                    
                case DOUBLE:
                    double doubleValue = data.getDoubleValue();
                    dataOutput.writeDouble(doubleValue);
                    break;
                    
                case UTF_STRING:
                    String utfValue = data.getStringValue();
                    dataOutput.writeUTF(utfValue);
                    break;
                    
                case ASCII_STRING:
                    String asciiValue = data.getStringValue();
                    dataOutput.writeASCII(asciiValue);
                    break;
                    
                default:
                    throw new IllegalStateException("Unsupported datatype " + dataType);
            }
        }
        catch (Exception e)
        {
            throw new WriterException("Error while writing scalar component " + component.getName(), e);
        }
	}
	
	
	@Override
	protected boolean processBlock(DataComponent blockComponent) throws IOException
	{		
		if (blockComponent instanceof DataChoiceImpl)
		{
		    int selected = ((DataChoiceImpl)blockComponent).getSelected();
            dataOutput.writeByte(selected);
		}
		else
		{
			// get next encoding block
		    BinaryBlock binaryInfo = (BinaryBlock)((AbstractDataComponentImpl)blockComponent).getEncodingInfo();
			
			// write whole block at once
			if (binaryInfo != null)
			{
				writeBinaryBlock(blockComponent, binaryInfo);
				return false;
			}
		}
		
		return true;		
	}

	
	private void writeBinaryBlock(DataComponent blockComponent, BinaryBlock binaryInfo)  throws IOException
	{
	    try
        {
	        DataBlock data = blockComponent.getData();
	        
	        // in case of data block compressed
	        if (data instanceof DataBlockCompressed)
	        {
	            DataBlockCompressed compressedBlock = (DataBlockCompressed)data;
	            byte[] bytes = (byte[])data.getUnderlyingObject();
	                    
	            // if same codec as requested in encoding options, write as-is 
	            if (binaryInfo.getCompression() != null) // && binaryInfo.getCodec().equals(compressedBlock.getCompressionType()))
	            {
	                dataOutput.writeInt(bytes.length);
	                dataOutput.write(bytes);
	            }
	            
	            // otherwise decompress or transcode to desired codec
	            else
	            {
	                compressedBlock.ensureUncompressed();
	                // TODO write uncompressed data
	            }
	        }	            
	        	        
	        // TODO implement writeBinaryBlock: call special compressed writer
        }
        catch (Exception e)
        {
            throw new WriterException("Error while writing block component " + blockComponent.getName() + " as " + binaryInfo.getCompression(), e);
        }
	}
}