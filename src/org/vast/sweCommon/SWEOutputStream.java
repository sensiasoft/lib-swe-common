/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sweCommon;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * <p><b>Title:</b><br/>
 * CDM Output Stream
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Extension of DataOutputStream to support writing unsigned int and long
 * values as well as ASCII (0 terminated) strings as byte sequence.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Feb 10, 2006
 * @version 1.0
 */
public class SWEOutputStream extends DataOutputStream
{
	
	public SWEOutputStream(OutputStream is)
	{
		super(is);
	}
	
	
    public void writeUnsignedByte(short val) throws IOException
    {
        //TODO
    }
    
    
    public void writeUnsignedShort(int val) throws IOException
    {
        //TODO
    }
    
    
	public void writeUnsignedInt(long val) throws IOException
	{
		//TODO
	}
	
	

	public void writeUnsignedLong(long val) throws IOException
	{
		this.writeLong(val);
	}
	
	
	/**
     * Writes a zero terminated ascii string to the stream 
     * @param asciiString
     * @throws IOException
	 */
	public void writeASCII(String asciiString) throws IOException
	{
		this.writeBytes(asciiString);
		this.writeByte(0);
    }
}
