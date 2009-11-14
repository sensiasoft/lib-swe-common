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

/**
 * <p><b>Title:</b><br/>
 * Binary Component
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Binary Component holds the attributes of Swe Common Binary Component encodings.
 * It extends Binary Options.  
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin & Gregoire Berthiau
 * @since Sep 15, 2008
 * @version 1.0
 */

public class BinaryComponent extends BinaryOptions
{
    public final static String doubleURN = "urn:ogc:def:data:double";
    public final static String floatURN = "urn:ogc:def:data:float";
    public final static String booleanURN = "urn:ogc:def:data:boolean";
    public final static String byteURN = "urn:ogc:def:data:signedByte";
    public final static String shortURN = "urn:ogc:def:data:signedShort";
    public final static String intURN = "urn:ogc:def:data:signedInt";
    public final static String longURN = "urn:ogc:def:data:signedLong";
    public final static String ubyteURN = "urn:ogc:def:data:unsignedByte";
    public final static String ushortURN = "urn:ogc:def:data:unsignedShort";
    public final static String uintURN = "urn:ogc:def:data:unsignedInt";
    public final static String ulongURN = "urn:ogc:def:data:unsignedLong";
    public final static String asciiURN = "urn:ogc:def:data:string:ascii";
    public final static String utfURN = "urn:ogc:def:data:string:utf";
    
    public DataType type;
    public int bitLength; 
	
    
    public BinaryComponent()
    {
    }
    
    
    public BinaryComponent copy()
    {
    	BinaryComponent newVal = new BinaryComponent();
    	newVal.type = type;
    	newVal.bitLength = bitLength;
    	newVal.componentName = new String(componentName);
    	return newVal;
    }


    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append("BinaryValue: ");
                
        text.append(this.bitLength + " bits, " + this.type + "\n");

        return text.toString();
    }
}
