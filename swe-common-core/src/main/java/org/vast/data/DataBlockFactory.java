/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataType;


/**
 * <p>
 * Helper to create DataBlocks using existing arrays of data.
 * </p>
 *
 * @author Alex Robin
 * @since Jan 27, 2006
 * */
public class DataBlockFactory
{

	public static DataBlock createBlock(DataType dataType)
	{
		switch (dataType)
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
	
	
	public static DataBlockMixed createMixedBlock(AbstractDataBlock... dataBlocks)
	{
		DataBlockMixed block = new DataBlockMixed(dataBlocks.length);
		block.startIndex = 0;
		
		for (int b=0; b<dataBlocks.length; b++)
		{
			block.blockArray[b] = dataBlocks[b];
			block.atomCount += dataBlocks[b].atomCount;
		}	
		
		return block;
	}
	
	
	public static DataBlockParallel createParallelBlock(AbstractDataBlock... dataBlocks)
    {
        DataBlockParallel block = new DataBlockParallel(dataBlocks.length);
        block.startIndex = 0;
        
        for (int b=0; b<dataBlocks.length; b++)
        {
            block.blockArray[b] = dataBlocks[b];
            block.atomCount += dataBlocks[b].atomCount;
        }   
        
        return block;
    }
}
