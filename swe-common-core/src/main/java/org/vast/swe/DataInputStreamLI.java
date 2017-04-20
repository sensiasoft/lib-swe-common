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

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.vast.cdm.common.DataInputExt;


/**
 * <p>
 * Equivalent of DataInputStream to read little endian ordered streams
 * (i.e. Lower Significant Byte first)
 * </p>
 *
 * @author Alex Robin
 * @since Nov 28, 2005
 * */
public class DataInputStreamLI extends FilterInputStream implements DataInputExt
{
    private byte[] b = new byte[8];
    
    
	public DataInputStreamLI(InputStream is)
	{
		super(is);
	}
    
    
	@Override
    public boolean readBoolean() throws IOException
    {
        int ch = in.read();
        if (ch < 0)
            throw new EOFException();
        return ch != 0;
    }
    
    
    @Override
    public byte readByte() throws IOException
    {
        int ch = in.read();
        if (ch < 0)
            throw new EOFException();
        return (byte)(ch);
    }


    @Override
    public int readUnsignedByte() throws IOException
    {
        int ch = in.read();
        if (ch < 0)
            throw new EOFException();
        return ch;
    }
    
    
    @Override
    public short readShort() throws IOException
    {
        readFully(b, 0, 2);
        return (short) (((b[1] & 0xff) << 8) | (b[0] & 0xff));
    }


    @Override
    public int readUnsignedShort() throws IOException
    {
        readFully(b, 0, 2);
        return ((b[1] & 0xff) << 8) | (b[0] & 0xff);
    }
    
    
    @Override
    public char readChar() throws IOException
    {
        readFully(b, 0, 2);        
        return (char) (((b[1] & 0xff) << 8) | (b[0] & 0xff));
    }
    
    
    @Override
    public int readInt() throws IOException
    {
        readFully(b, 0, 4);        
        return ((b[3] & 0xff) << 24) |
               ((b[2] & 0xff) << 16) |
               ((b[1] & 0xff) <<  8) |
               (b[0] & 0xff);
    }
    
    
    @Override
    public long readUnsignedInt() throws IOException
	{
        readFully(b, 0, 4);		 
        return ((long)(b[3] & 0xff) << 24) |
               ((long)(b[2] & 0xff) << 16) |
               ((long)(b[1] & 0xff) <<  8) |
               (b[0] & 0xff);
	}
    
    
    @Override
    public long readLong() throws IOException
    {
        this.readFully(b, 0, 8);        
        return ((long)(b[7] & 0xff) << 56) |
               ((long)(b[6] & 0xff) << 48) |
               ((long)(b[5] & 0xff) << 40) |
               ((long)(b[4] & 0xff) << 32) |
               ((long)(b[3] & 0xff) << 24) |
               ((long)(b[2] & 0xff) << 16) |
               ((long)(b[1] & 0xff) <<  8) |
               (b[0] & 0xff);
    }
    
    
    @Override
    public long readUnsignedLong() throws IOException
    {
        return readLong();
    }


    @Override
    public float readFloat() throws IOException
    {
        return Float.intBitsToFloat(this.readInt());
    }


    @Override
    public double readDouble() throws IOException
    {
        return Double.longBitsToDouble(this.readLong());
    }


    @Override
    public String readLine() throws IOException
    {
        return null;
    }


    @Override
    public String readUTF() throws IOException
    {
        return DataInputStream.readUTF(this);
    }
	
	
    @Override
    public String readASCII() throws IOException
	{
		int val;
		StringBuilder buf = new StringBuilder();
		while ((val = in.read()) != 0) 
		    buf.append((char)val);
		
		return buf.toString();
	}


    @Override
    public void readFully(byte[] b) throws IOException
    {
        readFully(b, 0, b.length);        
    }


    @Override
    public void readFully(byte[] b, int off, int len) throws IOException
    {
        if (len < 0)
            throw new IndexOutOfBoundsException();
        
        int n = 0;
        while (n < len)
        {
            int count = in.read(b, off + n, len - n);
            if (count < 0)
                throw new EOFException();
            n += count;
        }
    }


    @Override
    public int skipBytes(int n) throws IOException
    {
        int total = 0;
        int cur = 0;
        long remaining = (long)n;
        
        while ((total < n) && ((cur = (int) in.skip(remaining)) > 0))
        {
            total += cur;
            remaining -= cur;
        }

        return total;
    }
}
