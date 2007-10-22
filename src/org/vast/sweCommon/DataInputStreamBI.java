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

import java.io.IOException;
import java.io.InputStream;
import java.io.DataInputStream;


/**
 * <p><b>Title:</b><br/>
 * Data Input Stream for Big Endian
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Extension of DataInputStream to support reading unsigned int and long
 * values as well as ASCII (0 terminated) strings from byte stream.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 28, 2005
 * @version 1.0
 */
public class DataInputStreamBI extends DataInputStream implements DataInputExt
{
	
	public DataInputStreamBI(InputStream is)
	{
		super(is);
	}
	
	
	public long readUnsignedInt() throws IOException
	{
		byte[] b = new byte[4];
		this.read(b);
		 
        return (((long)(b[0] & 0xff) << 24) +
                ((long)(b[1] & 0xff) << 16) +
                ((long)(b[2] & 0xff) <<  8) +
                ((long)(b[3] & 0xff)));
	}
	
	
	public long readUnsignedLong() throws IOException
	{
		return readLong();
	}
	
	
	public String readASCII() throws IOException
	{
		int val;
		StringBuffer buf = new StringBuffer();
		
		while ((val = in.read()) != 0) 
		{
			buf.append((char)val);
		}
		
		return buf.toString();
	}
}
