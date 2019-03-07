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
 * Carries data in compressed format.
 * Data is uncompressed on the fly when one of the get methods is called, and
 * set Methods are not implemented.<br/>
 * All data is casted to the correct types when requested.
 * </p>
 *
 * @author Alex Robin
 * @since Jan 10, 2015
 * */
public class DataBlockCompressed extends AbstractDataBlock
{
	private static final long serialVersionUID = 2808433335202338950L;
    protected byte[] compressedData;
    protected DataBlock uncompressedData;
    protected int compressionType;
	
	
	public DataBlockCompressed()
	{
	}
	
	
	public DataBlockCompressed(byte[] compressedData, int atomCount)
	{
		this.compressedData = compressedData;
		this.atomCount = atomCount;
	}
	
	
	@Override
    public DataBlockCompressed copy()
	{
		DataBlockCompressed newBlock = new DataBlockCompressed();
		newBlock.compressedData = this.compressedData;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    @Override
    public DataBlockCompressed renew()
    {
        DataBlockCompressed newBlock = new DataBlockCompressed();
        newBlock.compressedData = null;
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public DataBlockCompressed clone()
    {
        DataBlockCompressed newBlock = new DataBlockCompressed();
        if (compressedData != null)
            newBlock.compressedData = this.compressedData.clone();
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public int getCompressionType()
    {
        return compressionType;
    }


    public void setCompressionType(int compressionType)
    {
        this.compressionType = compressionType;
    }


    @Override
    public byte[] getUnderlyingObject()
    {
        return compressedData;
    }
    
    
    protected void setUnderlyingObject(byte[] compressedData)
    {
        this.compressedData = compressedData;
    }
    
    
    @Override
    public void setUnderlyingObject(Object obj)
    {
        setUnderlyingObject((byte[])obj);
    }
    
    
    @Override
    public final int getAtomCount()
    {
        return this.atomCount;
    }


    @Override
    public void resize(int size)
    {
        if (uncompressedData != null)
            uncompressedData.resize(size);
        this.atomCount = size;
    }
	
	
	@Override
    public DataType getDataType()
	{
	    if (uncompressedData != null)
	        return uncompressedData.getDataType();
	    return DataType.MIXED;
	}


	@Override
    public DataType getDataType(int index)
	{
	    if (uncompressedData != null)
            return uncompressedData.getDataType(index);
        return DataType.MIXED;
	}
	
	
	@Override
    public final boolean getBooleanValue(int index)
    {
        ensureUncompressed();
        return uncompressedData.getBooleanValue(index);
    }


    @Override
    public final byte getByteValue(int index)
    {
        ensureUncompressed();
        return uncompressedData.getByteValue(index);
    }


    @Override
    public final short getShortValue(int index)
    {
        ensureUncompressed();
        return uncompressedData.getShortValue(index);
    }


    @Override
    public final int getIntValue(int index)
    {
        ensureUncompressed();
        return uncompressedData.getIntValue(index);
    }


    @Override
    public final long getLongValue(int index)
    {
        ensureUncompressed();
        return uncompressedData.getLongValue(index);
    }


    @Override
    public final float getFloatValue(int index)
    {
        ensureUncompressed();
        return uncompressedData.getFloatValue(index);
    }


    @Override
    public final double getDoubleValue(int index)
    {
        ensureUncompressed();
        return uncompressedData.getDoubleValue(index);
    }


    @Override
    public final String getStringValue(int index)
    {
        ensureUncompressed();
        return uncompressedData.getStringValue(index);
    }


    @Override
    public final boolean getBooleanValue()
    {
        ensureUncompressed();
        return uncompressedData.getBooleanValue();
    }


    @Override
    public final byte getByteValue()
    {
        ensureUncompressed();
        return uncompressedData.getByteValue();
    }


    @Override
    public final short getShortValue()
    {
        ensureUncompressed();
        return uncompressedData.getShortValue();
    }


    @Override
    public final int getIntValue()
    {
        ensureUncompressed();
        return uncompressedData.getIntValue();
    }


    @Override
    public final long getLongValue()
    {
        ensureUncompressed();
        return uncompressedData.getLongValue();
    }


    @Override
    public final float getFloatValue()
    {
        ensureUncompressed();
        return uncompressedData.getFloatValue();
    }


    @Override
    public final double getDoubleValue()
    {
        ensureUncompressed();
        return uncompressedData.getDoubleValue();
    }


    @Override
    public final String getStringValue()
    {
        ensureUncompressed();
        return uncompressedData.getStringValue();
    }


    public final void ensureUncompressed()
	{
	    if (uncompressedData == null) 
	    {
	        // TODO call correct codec
	    }
	}


	@Override
    public void setBooleanValue(int index, boolean value)
	{
	    throwUnsupportedException();
	}


	@Override
    public void setByteValue(int index, byte value)
	{
	    throwUnsupportedException();
	}


	@Override
    public void setShortValue(int index, short value)
	{
	    throwUnsupportedException();
	}


	@Override
    public void setIntValue(int index, int value)
	{
	    throwUnsupportedException();
	}


	@Override
    public void setLongValue(int index, long value)
	{
	    throwUnsupportedException();
	}


	@Override
    public void setFloatValue(int index, float value)
	{
	    throwUnsupportedException();
	}


	@Override
    public void setDoubleValue(int index, double value)
	{
	    throwUnsupportedException();
	}


	@Override
    public void setStringValue(int index, String value)
	{
	    throwUnsupportedException();
	}


	@Override
    public void setBooleanValue(boolean value)
	{
	    throwUnsupportedException();
	}


	@Override
    public void setByteValue(byte value)
	{
	    throwUnsupportedException();
	}


	@Override
    public void setShortValue(short value)
	{
	    throwUnsupportedException();
	}


	@Override
    public void setIntValue(int value)
	{
	    throwUnsupportedException();
	}


	@Override
    public void setLongValue(long value)
	{
	    throwUnsupportedException();
	}


	@Override
    public void setFloatValue(float value)
	{
	    throwUnsupportedException();
	}


	@Override
    public void setDoubleValue(double value)
	{
	    throwUnsupportedException();
	}


	@Override
    public void setStringValue(String value)
	{
	    throwUnsupportedException();
	}
	
	
	private void throwUnsupportedException()
	{
	    throw new UnsupportedOperationException("Individual values of a compressed data block cannot be set directly");
	}
}
