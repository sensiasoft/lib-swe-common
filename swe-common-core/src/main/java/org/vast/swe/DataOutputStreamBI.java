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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.vast.cdm.common.DataOutputExt;


/**
 * <p>
 * Extension of DataOutputStream to support writing unsigned int and long
 * values as well as ASCII (0 terminated) strings as byte sequence. This
 * is for big endian order (i.e. Most Significant Byte first)
 * </p>
 *
 * @author Alex Robin
 * @since Feb 10, 2006
 * */
public class DataOutputStreamBI extends DataOutputStream implements DataOutputExt
{
    byte[] tmpBuf = new byte[4];
    
    
	public DataOutputStreamBI(OutputStream is)
	{
		super(is);
	}
	
	
    @Override
    public void writeUnsignedByte(short v) throws IOException
    {
        byte b = (byte)(0xff & v);
        this.write(b);
    }
    
    
    @Override
    public void writeUnsignedShort(int v) throws IOException
    {
        // MSB first
        tmpBuf[0] = (byte)(0xff & (v >> 8));
        tmpBuf[1] = (byte)(0xff & v);
        
        this.write(tmpBuf, 0, 2);
    }
    
    
    @Override
    public void writeUnsignedInt(long v) throws IOException
	{
	    // MSB first
	    tmpBuf[0] = (byte)(0xff & (v >> 24));
	    tmpBuf[1] = (byte)(0xff & (v >> 16));
	    tmpBuf[2] = (byte)(0xff & (v >> 8));
	    tmpBuf[3] = (byte)(0xff & v);
	    
        this.write(tmpBuf, 0, 4);
	}
		

    @Override
    public void writeUnsignedLong(long v) throws IOException
	{
		this.writeLong(v);
	}
	
	
    @Override
    public void writeASCII(String s) throws IOException
	{
		this.writeBytes(s);
		this.writeByte(0);
    }
}
