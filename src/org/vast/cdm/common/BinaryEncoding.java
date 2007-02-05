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

package org.vast.cdm.common;

import org.vast.sweCommon.BinaryDataParser;


/**
 * <p><b>Title:</b>
 * BinaryEncoding
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO BinaryEncoding type description
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Dec 20, 2006
 * @version 1.0
 */
public class BinaryEncoding implements DataEncoding
{
	public enum ByteEncoding
	{
		RAW,
		BASE16,
		BASE64
	}

	public enum ByteOrder
	{
		LITTLE_ENDIAN,
		BIG_ENDIAN
	}
    
    
   	public ByteEncoding byteEncoding;
	public ByteOrder byteOrder;
	public long byteLength = -1;
	public BinaryOptions[] componentEncodings;	
	
	
	public EncodingType getEncodingType()
	{
		return EncodingType.BINARY;
	}
    
    
    public DataStreamParser createDataParser()
    {
        DataStreamParser parser = new BinaryDataParser();
        parser.setDataEncoding(this);
        return parser;
    }
	
	
	public String toString()
	{
		StringBuffer buf = new StringBuffer("Binary Encoding:\n");
		
		buf.append("Encoding: ");
		buf.append(byteEncoding);
		buf.append('\n');
		
		buf.append("Order: ");
		buf.append(byteOrder);
		buf.append('\n');
		
		for (int i=0; i<componentEncodings.length; i++)
		{
			buf.append("  ");
			buf.append(componentEncodings[i].componentName);
			buf.append(" -> ");
			buf.append(componentEncodings[i].type);
			buf.append('\n');
		}
		
		return buf.toString(); 
	}
}
