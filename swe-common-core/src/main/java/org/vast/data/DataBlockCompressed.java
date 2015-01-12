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
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
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
 * <p>Copyright (c) 2015</p>
 * @author Alexandre Robin
 * @since Jan 10, 2015
 * @version 1.0
 */
public class DataBlockCompressed extends AbstractDataBlock
{
	private static final long serialVersionUID = -6396843894262396349L;
    protected byte[] compressedData;
    protected DataBlock uncompressedData;
    protected int compressionType;
	
	
	public DataBlockCompressed()
	{
	}
	
	
	public DataBlockCompressed(int size)
	{
		resize(size);
	}
	
	
	public DataBlockCompressed copy()
	{
		DataBlockCompressed newBlock = new DataBlockCompressed();
		newBlock.compressedData = this.compressedData;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    public DataBlockCompressed renew()
    {
        DataBlockCompressed newBlock = new DataBlockCompressed();
        newBlock.compressedData = null;
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    public DataBlockCompressed clone()
    {
        DataBlockCompressed newBlock = new DataBlockCompressed();
        newBlock.compressedData = new byte[this.compressedData.length];
        System.arraycopy(this.compressedData, 0, newBlock.compressedData, 0, this.compressedData.length);
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


    public byte[] getUnderlyingObject()
    {
        return compressedData;
    }
    
    
    public void setUnderlyingObject(byte[] compressedData)
    {
        this.compressedData = compressedData;
    }
    
    
    public void setUnderlyingObject(Object obj)
    {
    	this.compressedData = (byte[])obj;
    }
    
    
    public final int getAtomCount()
    {
        return this.atomCount;
    }


    public void resize(int size)
    {
        throwUnsupportedException();
    }
	
	
	public DataType getDataType()
	{
	    ensureUncompressed();
        return uncompressedData.getDataType();
	}


	public DataType getDataType(int index)
	{
	    ensureUncompressed();
        return uncompressedData.getDataType(index);
	}
	
	
	public final boolean getBooleanValue(int index)
    {
        ensureUncompressed();
        return uncompressedData.getBooleanValue(index);
    }


    public final byte getByteValue(int index)
    {
        ensureUncompressed();
        return uncompressedData.getByteValue(index);
    }


    public final short getShortValue(int index)
    {
        ensureUncompressed();
        return uncompressedData.getShortValue(index);
    }


    public final int getIntValue(int index)
    {
        ensureUncompressed();
        return uncompressedData.getIntValue(index);
    }


    public final long getLongValue(int index)
    {
        ensureUncompressed();
        return uncompressedData.getLongValue(index);
    }


    public final float getFloatValue(int index)
    {
        ensureUncompressed();
        return uncompressedData.getFloatValue(index);
    }


    public final double getDoubleValue(int index)
    {
        ensureUncompressed();
        return uncompressedData.getDoubleValue(index);
    }


    public final String getStringValue(int index)
    {
        ensureUncompressed();
        return uncompressedData.getStringValue(index);
    }


    public final boolean getBooleanValue()
    {
        ensureUncompressed();
        return uncompressedData.getBooleanValue();
    }


    public final byte getByteValue()
    {
        ensureUncompressed();
        return uncompressedData.getByteValue();
    }


    public final short getShortValue()
    {
        ensureUncompressed();
        return uncompressedData.getShortValue();
    }


    public final int getIntValue()
    {
        ensureUncompressed();
        return uncompressedData.getIntValue();
    }


    public final long getLongValue()
    {
        ensureUncompressed();
        return uncompressedData.getLongValue();
    }


    public final float getFloatValue()
    {
        ensureUncompressed();
        return uncompressedData.getFloatValue();
    }


    public final double getDoubleValue()
    {
        ensureUncompressed();
        return uncompressedData.getDoubleValue();
    }


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


	public void setBooleanValue(int index, boolean value)
	{
	    throwUnsupportedException();
	}


	public void setByteValue(int index, byte value)
	{
	    throwUnsupportedException();
	}


	public void setShortValue(int index, short value)
	{
	    throwUnsupportedException();
	}


	public void setIntValue(int index, int value)
	{
	    throwUnsupportedException();
	}


	public void setLongValue(int index, long value)
	{
	    throwUnsupportedException();
	}


	public void setFloatValue(int index, float value)
	{
	    throwUnsupportedException();
	}


	public void setDoubleValue(int index, double value)
	{
	    throwUnsupportedException();
	}


	public void setStringValue(int index, String value)
	{
	    throwUnsupportedException();
	}


	public void setBooleanValue(boolean value)
	{
	    throwUnsupportedException();
	}


	public void setByteValue(byte value)
	{
	    throwUnsupportedException();
	}


	public void setShortValue(short value)
	{
	    throwUnsupportedException();
	}


	public void setIntValue(int value)
	{
	    throwUnsupportedException();
	}


	public void setLongValue(long value)
	{
	    throwUnsupportedException();
	}


	public void setFloatValue(float value)
	{
	    throwUnsupportedException();
	}


	public void setDoubleValue(double value)
	{
	    throwUnsupportedException();
	}


	public void setStringValue(String value)
	{
	    throwUnsupportedException();
	}
	
	
	private void throwUnsupportedException()
	{
	    throw new RuntimeException("Individual values of a compressed data block cannot be set directly");
	}
}
