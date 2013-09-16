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

import org.vast.cdm.common.AsciiEncoding;
import org.vast.cdm.common.BinaryBlock;
import org.vast.cdm.common.BinaryComponent;
import org.vast.cdm.common.BinaryEncoding;
import org.vast.cdm.common.BinaryOptions;
import org.vast.cdm.common.DataEncoding;
import org.vast.cdm.common.DataEncodingReader;
import org.vast.cdm.common.DataType;
import org.vast.cdm.common.StandardFormatEncoding;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p>
 * Reads SWE Encoding definition for ASCIIBlock and BinaryBlock.
 * This is for version 1 of the SWECommon standard.
 * </p>
 *
 * <p>Copyright (c) 2006</p>
 * @author Alexandre Robin
 * @since Dec 19, 2006
 * @version 1.0
 */
public class SweEncodingReaderV1 implements DataEncodingReader
{
    
    public SweEncodingReaderV1()
    {
    }
 

    public DataEncoding readEncodingProperty(DOMHelper dom, Element propertyElement) throws XMLReaderException
    {
    	Element encodingElement = dom.getFirstChildElement(propertyElement);
    	DataEncoding encoding = readEncoding(dom, encodingElement);        
        return encoding;
    }
    
    
    public DataEncoding readEncoding(DOMHelper dom, Element encodingElement) throws XMLReaderException
    {
    	DataEncoding encoding = null;
    	String encodingName = encodingElement.getLocalName();
        
        if (encodingName.equals("TextBlock"))
        {
        	encoding = readAsciiBlock(dom, encodingElement);
        }
        else if (encodingName.equals("BinaryBlock"))
        {
        	encoding = readBinaryBlock(dom, encodingElement);
        }
        else if (encodingName.equals("StandardFormat"))
        {
        	encoding = readStandardFormat(dom, encodingElement);
        }
        else
        	throw new XMLReaderException("Encoding not supported: " + encodingName, encodingElement);

        return encoding;
    }
    
    
    private AsciiEncoding readAsciiBlock(DOMHelper dom, Element asciiBlockElement) throws XMLReaderException
    {
    	AsciiEncoding encoding = new AsciiEncoding();
    	
    	encoding.decimalSeparator = dom.getAttributeValue(asciiBlockElement, "decimalSeparator").charAt(0);
    	encoding.tokenSeparator = dom.getAttributeValue(asciiBlockElement, "tokenSeparator");
    	encoding.blockSeparator = dom.getAttributeValue(asciiBlockElement, "blockSeparator");
    	
    	return encoding;
    }
    
    
    private StandardFormatEncoding readStandardFormat(DOMHelper dom, Element formatElt) throws XMLReaderException
    {
    	String mimeType = dom.getAttributeValue(formatElt, "mimeType");
    	if (mimeType == null)
    		throw new XMLReaderException("The MIME type must be specified", formatElt);
    	
    	StandardFormatEncoding encoding = new StandardFormatEncoding(mimeType);    	
    	return encoding;
    }
    
    
    private BinaryEncoding readBinaryBlock(DOMHelper dom, Element binaryBlockElement) throws XMLReaderException
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
    	int listSizeComponents = components.getLength();
    	
    	NodeList blocks = dom.getElements(binaryBlockElement, "member/Block");
    	int listSizeblocks = blocks.getLength();
    	int j = 0;
    	
    	BinaryOptions[] componentEncodings = new BinaryOptions[listSizeComponents+listSizeblocks];
    	
    	for (int i=0; i<components.getLength(); i++)
    	{
    		Element componentElt = (Element)components.item(i);
    		componentEncodings[i] = readComponent(dom, componentElt);
    		j++;
    	}
    	
    	for (int i=0; i<blocks.getLength(); i++)
    	{
    		Element blockElt = (Element)blocks.item(i);
    		componentEncodings[j+i] = readBlock(dom, blockElt);
    	}
    	
    	encoding.componentEncodings = componentEncodings;
    	
    	return encoding;
    }
    

    private BinaryComponent readComponent(DOMHelper dom, Element componentElement) throws XMLReaderException
    {
        BinaryComponent binaryValue = new BinaryComponent();
        
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
    
    private BinaryBlock readBlock(DOMHelper dom, Element blockElt) throws XMLReaderException
    {
        BinaryBlock binaryBlock = new BinaryBlock();

        binaryBlock.componentName = dom.getAttributeValue(blockElt, "ref");
        
        binaryBlock.compression = dom.getAttributeValue(blockElt, "compression");
        
        return binaryBlock;
        
        // TODO: the other attibutes must be read and implemented...
    }
    
    
    
}
