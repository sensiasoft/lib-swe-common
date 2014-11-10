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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.util.List;
import net.opengis.swe.v20.BinaryBlock;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.BinaryMember;
import net.opengis.swe.v20.ByteEncoding;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.ScalarComponent;
import org.vast.util.WriterException;
import org.vast.cdm.common.DataOutputExt;
import org.vast.data.AbstractDataComponentImpl;
import org.vast.data.BinaryComponentImpl;
import org.vast.data.DataChoiceImpl;


/**
 * <p>
 * Writes CDM binary data stream using the given data components
 * structure and binary encoding information. This supports raw
 * binary and base64 for now.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin & Gregoire Berthiau
 * @since Feb 10, 2006
 * @version 1.0
 */
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
        if (((BinaryEncoding)dataEncoding).getByteOrder() == ByteOrder.BIG_ENDIAN)
            dataOutput = new DataOutputStreamBI(new BufferedOutputStream(outputStream));
        else if (((BinaryEncoding)dataEncoding).getByteOrder() == ByteOrder.LITTLE_ENDIAN)
            dataOutput = new DataOutputStreamLI(new BufferedOutputStream(outputStream));
	}
	
		
	@Override
	public void reset()
	{
		if (!componentEncodingResolved)
			resolveComponentEncodings();
		
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
	
	
	/**
	 * Maps a given scalar component to the corresponding BinaryValue
	 * object containing binary encoding information. This also
	 * forces the DataValues primitive type to be the same as the ones
	 * specified in the binary encoding section.
	 * @throws CDMException
	 */
	protected void resolveComponentEncodings()
	{
		List<BinaryMember> encodingList = ((BinaryEncoding)dataEncoding).getMemberList();
		
	    for (BinaryMember binaryOpts: encodingList)
		{
			String [] dataPath = binaryOpts.getRef().split("/");
			DataComponent dataComponent = dataComponents;
			
			// find component in tree
            for (int j=0; j<dataPath.length; j++)
            {
                dataComponent = dataComponent.getComponent(dataPath[j]);                
                if (dataComponent == null)
                {
                    System.err.println("Unknown component " + dataPath[j]);
                    continue;
                }
            }
			
			((AbstractDataComponentImpl)dataComponent).setEncodingInfo(binaryOpts);	
		}
		
		componentEncodingResolved = true;
	}
	
	
	@Override
	protected void processAtom(ScalarComponent component) throws IOException
	{
		// get encoding info for component
		BinaryMember binaryInfo = ((AbstractDataComponentImpl)component).getEncodingInfo();
		
		try
        {
            writeBinaryAtom(component, binaryInfo);
        }
        catch (Exception e)
        {
            throw new WriterException("Error while writing scalar component " + component.getName(), e);
        }
	}
	
	
	/**
	 * Parse binary component using info and encoding options
	 * Decoded value is assigned to each DataValue
	 * @param scalarInfo
	 * @param binaryInfo
	 * @throws CDMException
	 */
	private void writeBinaryAtom(ScalarComponent component, BinaryMember binaryInfo) throws Exception
	{
	    DataType dataType = ((BinaryComponentImpl)binaryInfo).getCdmDataType();
        DataBlock data = component.getData();
        
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
                dataOutput.writeByte(shortValue);
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
                throw new RuntimeException("Unsupported datatype " + dataType);
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
	        // TODO implement writeBinaryBlock: call special compressed writer
        }
        catch (Exception e)
        {
            throw new WriterException("Error while writing block component " + blockComponent.getName() + " as " + binaryInfo.getCompression(), e);
        }
	}
}