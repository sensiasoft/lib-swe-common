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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.vast.cdm.common.DataOutputExt;


/**
 * <p>
 * Equivalent of DataOutputStream to write in little endian order
 * (i.e Least Significant Byte first)
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Jan 10, 2009
 * */
public class DataOutputStreamLI extends FilterOutputStream implements DataOutputExt
{
    byte[] tmpBuf = new byte[8];
    
    
	public DataOutputStreamLI(OutputStream is)
	{
		super(is);
	}
	
	
	public void writeBoolean(boolean v) throws IOException
    {
	    this.write(v ? 1 : 0);        
    }
	
	
	public void writeByte(byte v) throws IOException
	{
	    this.write(v);
	}
	
	
	public void writeByte(int v) throws IOException
    {
        this.write(v);        
    }
	
	
    public void writeUnsignedByte(short v) throws IOException
    {
        byte b = (byte)(0xff & v);
        this.write(b);
    }
    
    
    public void writeShort(int v) throws IOException
    {
        this.writeShort((short)v);
    }
    
    
    public void writeShort(short v) throws IOException
    {
        // LSB first
        tmpBuf[1] = (byte)(0xff & (v >> 8));
        tmpBuf[0] = (byte)(0xff & v);
        
        this.write(tmpBuf, 0, 2);
    }
    
    
    public void writeUnsignedShort(int v) throws IOException
    {
        // LSB first
        tmpBuf[1] = (byte)(0xff & (v >> 8));
        tmpBuf[0] = (byte)(0xff & v);
        
        this.write(tmpBuf, 0, 2);
    }
    
    
    public void writeInt(int v) throws IOException
    {
        // LSB first
        tmpBuf[3] = (byte)(0xff & (v >> 24));
        tmpBuf[2] = (byte)(0xff & (v >> 16));
        tmpBuf[1] = (byte)(0xff & (v >> 8));
        tmpBuf[0] = (byte)(0xff & v);
        
        this.write(tmpBuf, 0, 4);
    }
    
    
	public void writeUnsignedInt(long v) throws IOException
	{
	    // LSB first
	    tmpBuf[3] = (byte)(0xff & (v >> 24));
	    tmpBuf[2] = (byte)(0xff & (v >> 16));
	    tmpBuf[1] = (byte)(0xff & (v >> 8));
	    tmpBuf[0] = (byte)(0xff & v);
        
        this.write(tmpBuf, 0, 4);
	}
	
	
	public void writeLong(long v) throws IOException
    {
	    // LSB first
	    tmpBuf[7] = (byte)(0xff & (v >> 56));
	    tmpBuf[6] = (byte)(0xff & (v >> 48));
	    tmpBuf[5] = (byte)(0xff & (v >> 40));
	    tmpBuf[4] = (byte)(0xff & (v >> 32));
	    tmpBuf[3] = (byte)(0xff & (v >> 24));
	    tmpBuf[2] = (byte)(0xff & (v >> 16));
	    tmpBuf[1] = (byte)(0xff & (v >> 8));
	    tmpBuf[0] = (byte)(0xff & v);

	    this.write(tmpBuf, 0, 8);
    }
	
	
	public void writeUnsignedLong(long v) throws IOException
	{
		this.writeLong(v);
	}
	
	
	public void writeFloat(float v) throws IOException
    {
        writeInt(Float.floatToIntBits(v));
    }
	
	
	public void writeDouble(double v) throws IOException
    {
	    writeLong(Double.doubleToLongBits(v));
    }
	
	
	public void writeChar(int c) throws IOException
    {
        this.writeInt(c);
    }
	
	
	public void writeASCII(String s) throws IOException
	{
		this.writeBytes(s);
		this.write(0);
    }
	
	
	public void writeUTF(String s) throws IOException
	{
	    this.writeInt(s.length());
	    this.write(s.getBytes());
	}


    public void writeChars(String s) throws IOException
    {
        char[] chars = s.toCharArray();
        for (char c: chars)
            this.writeChar(c);
    }
    
    
    public void writeBytes(String s) throws IOException
    {
        char[] chars = s.toCharArray();
        for (char c: chars)
            this.write(0xff & c);
    }
}
