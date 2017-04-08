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

package org.vast.cdm.common;

import java.io.DataInput;
import java.io.IOException;


/**
 * <p>
 * Extension of DataInputStream to support reading unsigned int and long
 * values as well as ASCII (0 terminated) strings from byte stream.
 * </p>
 *
 * @author Alex Robin
 * @since Nov 28, 2005
 * */
public interface DataInputExt extends DataInput
{

    public int read() throws IOException;   
    
    public void mark(int readLimit) throws IOException;    
    
    public void reset() throws IOException;
    
    
    /**
	 * Reads 4 input bytes and returns a long value in the range 0-2^32.
	 * @return long value
	 * @throws IOException
	 */
	public long readUnsignedInt() throws IOException;
	
	
	/**
	 * Reads 8 input bytes and returns a long value in the range 0-2^63.
	 * Values greater than 2^63 won't be read correctly since they are not 
	 * supported by java.
	 * @return long value
	 * @throws IOException
	 */
	public long readUnsignedLong() throws IOException;
	
	
	/**
	 * Reads a 0 terminated ASCII string from input stream
	 * @return string value
	 * @throws IOException
	 */
	public String readASCII() throws IOException;
}
