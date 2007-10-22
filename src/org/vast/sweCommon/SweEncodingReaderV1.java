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

import org.w3c.dom.*;
import org.vast.cdm.common.*;
import org.vast.xml.DOMHelper;


/**
 * <p><b>Title:</b><br/>
 * Swe Encoding Reader V1
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Reads SWE Encoding definition for ASCIIBlock and BinaryBlock.
 * This is for version 1 of the SWECommon standard.
 * </p>
 *
 * <p>Copyright (c) 2006</p>
 * @author Alexandre Robin
 * @date Dec 19, 2006
 * @version 1.0
 */
public class SweEncodingReaderV1 implements DataEncodingReader
{
    
    public SweEncodingReaderV1()
    {
    }
 

    public DataEncoding readEncodingProperty(DOMHelper dom, Element propertyElement) throws CDMException
    {
    	Element encodingElement = dom.getFirstChildElement(propertyElement);
    	DataEncoding encoding = readEncoding(dom, encodingElement);        
        return encoding;
    }
    
    
    public DataEncoding readEncoding(DOMHelper dom, Element encodingElement) throws CDMException
    {
    	DataEncoding encoding = null;
        
        if (encodingElement.getLocalName().equals("TextBlock"))
        {
        	encoding = readAsciiBlock(dom, encodingElement);
        }
        else if (encodingElement.getLocalName().equals("BinaryBlock"))
        {
        	encoding = readBinaryBlock(dom, encodingElement);
        }
        else
        	throw new CDMException("Encoding not supported");

        return encoding;
    }
    
    
    private AsciiEncoding readAsciiBlock(DOMHelper dom, Element asciiBlockElement) throws CDMException
    {
    	AsciiEncoding encoding = new AsciiEncoding();
    	
    	encoding.decimalSeparator = dom.getAttributeValue(asciiBlockElement, "decimalSeparator").charAt(0);
    	encoding.tokenSeparator = dom.getAttributeValue(asciiBlockElement, "tokenSeparator");
    	encoding.blockSeparator = dom.getAttributeValue(asciiBlockElement, "blockSeparator");
    	
    	return encoding;
    }
    
    
    private BinaryEncoding readBinaryBlock(DOMHelper dom, Element binaryBlockElement) throws CDMException
    {
    	BinaryEncoding encoding = new BinaryEncoding();
    	
    	// parse byte length
    	String byteLength = dom.getAttributeValue(binaryBlockElement, "byteLength");
    	if (byteLength != null)
    		encoding.byteLength = Long.parseLong(byteLength);
    	
    	// parse byte encoding
    	String byteEncoding = dom.getAttributeValue(binaryBlockElement, "byteEncoding");
    	if (byteEncoding.equalsIgnoreCase("base64"))
        {
    		encoding.byteEncoding = BinaryEncoding.ByteEncoding.BASE64;
        }
        else if (byteEncoding.equalsIgnoreCase("raw"))
        {
        	encoding.byteEncoding = BinaryEncoding.ByteEncoding.RAW;
        }
    	
    	// parse byte order
    	String byteOrder = dom.getAttributeValue(binaryBlockElement, "byteOrder");
    	if (byteOrder.equalsIgnoreCase("bigEndian"))
        {
    		encoding.byteOrder = BinaryEncoding.ByteOrder.BIG_ENDIAN;
        }
        else if (byteOrder.equalsIgnoreCase("littleEndian"))
        {
        	encoding.byteOrder = BinaryEncoding.ByteOrder.LITTLE_ENDIAN;
        }
    	
    	// parse component encodings
    	NodeList components = dom.getElements(binaryBlockElement, "member/Component");
    	int listSize = components.getLength();
    	BinaryOptions[] componentEncodings = new BinaryOptions[listSize];
    	for (int i=0; i<components.getLength(); i++)
    	{
    		Element componentElt = (Element)components.item(i);
    		componentEncodings[i] = readBinaryValue(dom, componentElt);
    	}
    	encoding.componentEncodings = componentEncodings;
    	
    	return encoding;
    }
    

    private BinaryOptions readBinaryValue(DOMHelper dom, Element componentElement) throws CDMException
    {
        BinaryOptions binaryValue = new BinaryOptions();
        
        // read component name
        String name = dom.getAttributeValue(componentElement, "ref");
        binaryValue.componentName = name;
                
        // read dataType attribute
        String dataType = dom.getAttributeValue(componentElement, "dataType");
        
        if (dataType.endsWith(":boolean"))
        {
            binaryValue.type = DataType.BOOLEAN;
        }
        else if (dataType.endsWith(":signedByte"))
        {
        	binaryValue.type = DataType.BYTE;
        }
        else if (dataType.endsWith(":unsignedByte"))
        {
        	binaryValue.type = DataType.UBYTE;
        }
        else if (dataType.endsWith(":signedShort"))
        {
        	binaryValue.type = DataType.SHORT;
        }
        else if (dataType.endsWith(":unsignedShort"))
        {
        	binaryValue.type = DataType.USHORT;
        }
        else if (dataType.endsWith(":signedInt"))
        {
        	binaryValue.type = DataType.INT;
        }
        else if (dataType.endsWith(":unsignedInt"))
        {
        	binaryValue.type = DataType.UINT;
        }
        else if (dataType.endsWith(":signedLong"))
        {
        	binaryValue.type = DataType.LONG;
        }
        else if (dataType.endsWith(":unsignedLong"))
        {
        	binaryValue.type = DataType.ULONG;
        }
        else if (dataType.endsWith(":float"))
        {
        	binaryValue.type = DataType.FLOAT;
        }
        else if (dataType.endsWith(":double"))
        {
        	binaryValue.type = DataType.DOUBLE;
        }
        else if (dataType.endsWith(":string:utf"))
        {
        	binaryValue.type = DataType.UTF_STRING;
        }
        else if (dataType.endsWith(":string:ascii"))
        {
        	binaryValue.type = DataType.ASCII_STRING;
        }
        else
        	binaryValue.type = DataType.DISCARD;
                
        // read bitLength
        String bitLength = dom.getAttributeValue(componentElement, "bitLength");
        if (bitLength != null)
        	binaryValue.bitLength = Integer.parseInt(bitLength);
               
        return binaryValue;
    }
}
