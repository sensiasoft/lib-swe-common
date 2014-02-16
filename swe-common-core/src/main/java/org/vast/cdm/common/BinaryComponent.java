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
 * <p>
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
    private static final long serialVersionUID = -3816840058163340347L;
    public final static String doubleURI = "http://www.opengis.net/def/dataType/OGC/0/double";
    public final static String floatURI = "http://www.opengis.net/def/dataType/OGC/0/float32";
    public final static String booleanURI = "http://www.opengis.net/def/dataType/OGC/0/boolean";
    public final static String byteURI = "http://www.opengis.net/def/dataType/OGC/0/signedByte";
    public final static String shortURI = "http://www.opengis.net/def/dataType/OGC/0/signedShort";
    public final static String intURI = "http://www.opengis.net/def/dataType/OGC/0/signedInt";
    public final static String longURI = "http://www.opengis.net/def/dataType/OGC/0/signedLong";
    public final static String ubyteURI = "http://www.opengis.net/def/dataType/OGC/0/unsignedByte";
    public final static String ushortURI = "http://www.opengis.net/def/dataType/OGC/0/unsignedShort";
    public final static String uintURI = "http://www.opengis.net/def/dataType/OGC/0/unsignedInt";
    public final static String ulongURI = "http://www.opengis.net/def/dataType/OGC/0/unsignedLong";
    public final static String asciiURI = "http://www.opengis.net/def/dataType/OGC/0/string-ascii";
    public final static String utfURI = "http://www.opengis.net/def/dataType/OGC/0/string-utf-8";
    
    public DataType type;
    public int bitLength; 
	
    
    public BinaryComponent()
    {
    }
    
    
    public BinaryComponent(String componentName, DataType type)
    {
        this.componentName = componentName;
        this.type = type;
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
