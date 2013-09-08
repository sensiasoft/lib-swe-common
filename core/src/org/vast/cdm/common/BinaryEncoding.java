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

import java.io.Serializable;
import java.util.ArrayList;
import org.vast.data.DataValue;
import org.vast.data.ScalarIterator;


/**
 * <p><b>Title:</b>
 * BinaryEncoding
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO BinaryEncoding type description
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Dec 20, 2006
 * @version 1.0
 */
public class BinaryEncoding implements DataEncoding, Serializable
{
	private static final long serialVersionUID = -3230565687425849338L;

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
    
    
    public String toString()
	{
		StringBuffer buf = new StringBuffer("Binary Encoding:\n");
		
		buf.append("Encoding: ");
		buf.append(byteEncoding);
		buf.append('\n');
		
		buf.append("Order: ");
		buf.append(byteOrder);
		buf.append('\n');
		
		for (BinaryOptions binaryOpts: componentEncodings)
		{
			buf.append("  ");
			buf.append(binaryOpts.componentName);
			buf.append(" -> ");
			
			if (binaryOpts instanceof BinaryComponent)
			    buf.append(((BinaryComponent)binaryOpts).type);
			else if (binaryOpts instanceof BinaryBlock)
			    buf.append(((BinaryBlock)binaryOpts).compression);
			    
			buf.append('\n');
		}
		
		return buf.toString(); 
	}
    
    
    public static BinaryEncoding getDefaultEncoding(DataComponent dataComponents)
    {
        BinaryEncoding encoding = new BinaryEncoding();
        encoding.byteEncoding = ByteEncoding.RAW;
        encoding.byteOrder = ByteOrder.BIG_ENDIAN;
        
        ArrayList<BinaryOptions> encodingList = new ArrayList<BinaryOptions>();
        
        // use default encoding info for each data value
        ScalarIterator it = new ScalarIterator(dataComponents);
        while (it.hasNext())
        {
            DataComponent[] nextPath = it.nextPath();
            DataValue nextScalar = (DataValue)nextPath[nextPath.length-1];
            
            // build path
            StringBuffer pathString = new StringBuffer();
            for (DataComponent component: nextPath)
                pathString.append(component.getName() + '/');
            
            BinaryComponent binaryOpts = new BinaryComponent();
            binaryOpts.type = nextScalar.getDataType();
            binaryOpts.componentName = pathString.substring(0, pathString.length()-1);
            
            encodingList.add(binaryOpts);
            nextScalar.setEncodingInfo(binaryOpts);
        }
        
        encoding.componentEncodings = encodingList.toArray(new BinaryOptions[0]);
        
        return encoding;
    }
}
