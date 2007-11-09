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

package org.vast.data;

import org.vast.cdm.common.DataBlock;
import org.vast.cdm.common.DataType;


/**
 * <p><b>Title:</b><br/>
 * DataBlockFactory
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Helper to create DataBlocks using existing arrays of data.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Jan 27, 2006
 * @version 1.0
 */
public class DataBlockFactory
{
    
	public static DataBlock createBlock(DataType dataType)
	{
		switch(dataType)
		{
			case BOOLEAN:
				return new DataBlockBoolean();
				
			case BYTE:
				return new DataBlockByte();
				
			case UBYTE:
				return new DataBlockUByte();
				
			case SHORT:
				return new DataBlockShort();
				
			case USHORT:
				return new DataBlockUShort();
				
			case INT:
				return new DataBlockInt();
				
			case UINT:
				return new DataBlockUInt();
				
			case LONG:
			case ULONG:
				return new DataBlockLong();
				
			case FLOAT:
				return new DataBlockFloat();
				
			case DOUBLE:
				return new DataBlockDouble();
				
			case UTF_STRING:
			case ASCII_STRING:
				return new DataBlockString();
				
			default:
				throw new IllegalArgumentException("Unsupported Data Type: " + dataType);
		}
	}
	
	public static DataBlockBoolean createBlock(boolean[] data)
    {
		DataBlockBoolean block = new DataBlockBoolean();        
        block.primitiveArray = data;
        block.atomCount = data.length;
        block.startIndex = 0;
        return block;
    }
	
	public static DataBlockByte createBlock(byte[] data)
    {
        DataBlockByte block = new DataBlockByte();        
        block.primitiveArray = data;
        block.atomCount = data.length;
        block.startIndex = 0;
        return block;
    }
    
    public static DataBlockUByte createUnsignedBlock(byte[] data)
    {
        DataBlockUByte block = new DataBlockUByte();        
        block.primitiveArray = data;
        block.atomCount = data.length;
        block.startIndex = 0;
        return block;
    }
    
    public static DataBlockShort createBlock(short[] data)
    {
    	DataBlockShort block = new DataBlockShort();        
        block.primitiveArray = data;
        block.atomCount = data.length;
        block.startIndex = 0;
        return block;
    }
    
    public static DataBlockUShort createUnsignedBlock(short[] data)
    {
    	DataBlockUShort block = new DataBlockUShort();        
        block.primitiveArray = data;
        block.atomCount = data.length;
        block.startIndex = 0;
        return block;
    }
    
    public static DataBlockInt createBlock(int[] data)
    {
    	DataBlockInt block = new DataBlockInt();        
        block.primitiveArray = data;
        block.atomCount = data.length;
        block.startIndex = 0;
        return block;
    }
    
    public static DataBlockUInt createUnsignedBlock(int[] data)
    {
    	DataBlockUInt block = new DataBlockUInt();        
        block.primitiveArray = data;
        block.atomCount = data.length;
        block.startIndex = 0;
        return block;
    }
    
    public static DataBlockLong createBlock(long[] data)
    {
    	DataBlockLong block = new DataBlockLong();        
        block.primitiveArray = data;
        block.atomCount = data.length;
        block.startIndex = 0;
        return block;
    }
    
    public static DataBlockFloat createBlock(float[] data)
    {
    	DataBlockFloat block = new DataBlockFloat();        
        block.primitiveArray = data;
        block.atomCount = data.length;
        block.startIndex = 0;
        return block;
    }
    
    public static DataBlockDouble createBlock(double[] data)
    {
        DataBlockDouble block = new DataBlockDouble();        
        block.primitiveArray = data;
        block.atomCount = data.length;
        block.startIndex = 0;
        return block;
    }
}
