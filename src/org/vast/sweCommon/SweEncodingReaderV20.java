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
    Alexandre Robin <alexandre.robin@spotimage.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sweCommon;

import org.w3c.dom.*;
import org.vast.cdm.common.*;
import org.vast.xml.DOMHelper;


/**
 * <p><b>Title:</b><br/>
 * SWE Encoding Reader v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Reads SWE Encoding definition for TextBlock, XMLBlock and BinaryBlock.
 * This is for version 2.0 of the SWECommon standard.
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin (Spot Image)
 * @date Feb 1, 2008
 * @version 1.0
 */
public class SweEncodingReaderV20 implements DataEncodingReader
{
    
    public SweEncodingReaderV20()
    {
    }
 

    public DataEncoding readEncodingProperty(DOMHelper dom, Element propertyElement) throws CDMException
    {
    	Element encodingElement = dom.getFirstChildElement(propertyElement);
    	DataEncoding encoding = readEncoding(dom, encodingElement);        
        return encoding;
    }
    
    
    public DataEncoding readEncoding(DOMHelper dom, Element encodingElt) throws CDMException
    {
    	DataEncoding encoding = null;
        
        if (encodingElt.getLocalName().equals("TextEncoding"))
        	encoding = readTextEncodingOptions(dom, encodingElt);
        else if (encodingElt.getLocalName().equals("XMLEncoding"))
        	encoding = readXmlEncodingOptions(dom, encodingElt);
        else if (encodingElt.getLocalName().equals("BinaryEncoding"))
        	encoding = readBinaryEncodingOptions(dom, encodingElt);
        else if (encodingElt.getLocalName().equals("StandardFormat"))
        	encoding = readStandardFormatOptions(dom, encodingElt);
        else
        	throw new CDMException("Encoding not supported");

        return encoding;
    }
    
    
    private AsciiEncoding readTextEncodingOptions(DOMHelper dom, Element asciiEncodingElt) throws CDMException
    {
    	AsciiEncoding encoding = new AsciiEncoding();
    	
    	encoding.decimalSeparator = dom.getAttributeValue(asciiEncodingElt, "decimalSeparator").charAt(0);
    	encoding.tokenSeparator = dom.getAttributeValue(asciiEncodingElt, "tokenSeparator");
    	encoding.blockSeparator = dom.getAttributeValue(asciiEncodingElt, "blockSeparator");
    	
    	String colWS = dom.getAttributeValue(asciiEncodingElt, "collapseWhiteSpaces");
    	if (colWS != null)
    		encoding.collapseWhiteSpaces = Boolean.parseBoolean(colWS);
    	
    	return encoding;
    }
    
    
    private XmlEncoding readXmlEncodingOptions(DOMHelper dom, Element xmlEncodingElt) throws CDMException
    {
    	XmlEncoding encoding = new XmlEncoding();
    	
    	String namespace = dom.getAttributeValue(xmlEncodingElt, "namespace");
    	encoding.namespace = namespace;
    	
    	return encoding;
    }
    
    
    private StandardFormatEncoding readStandardFormatOptions(DOMHelper dom, Element formatElt) throws CDMException
    {
    	String mimeType = dom.getAttributeValue(formatElt, "mimeType");
    	if (mimeType == null)
    		throw new CDMException("The MIME type must be specified");
    	
    	StandardFormatEncoding encoding = new StandardFormatEncoding(mimeType);    	
    	return encoding;
    }
    
    
    private BinaryEncoding readBinaryEncodingOptions(DOMHelper dom, Element binaryEncodingElt) throws CDMException
    {
    	BinaryEncoding encoding = new BinaryEncoding();
    	
    	// parse byte length
    	String byteLength = dom.getAttributeValue(binaryEncodingElt, "byteLength");
    	if (byteLength != null)
    		encoding.byteLength = Long.parseLong(byteLength);
    	
    	// parse byte encoding
    	String byteEncoding = dom.getAttributeValue(binaryEncodingElt, "byteEncoding");
    	if (byteEncoding.equalsIgnoreCase("base64"))
        {
    		encoding.byteEncoding = BinaryEncoding.ByteEncoding.BASE64;
        }
        else if (byteEncoding.equalsIgnoreCase("raw"))
        {
        	encoding.byteEncoding = BinaryEncoding.ByteEncoding.RAW;
        }
    	
    	// parse byte order
    	String byteOrder = dom.getAttributeValue(binaryEncodingElt, "byteOrder");
    	if (byteOrder.equalsIgnoreCase("bigEndian"))
        {
    		encoding.byteOrder = BinaryEncoding.ByteOrder.BIG_ENDIAN;
        }
        else if (byteOrder.equalsIgnoreCase("littleEndian"))
        {
        	encoding.byteOrder = BinaryEncoding.ByteOrder.LITTLE_ENDIAN;
        }
    	
    	// parse component encodings
    	NodeList components = dom.getElements(binaryEncodingElt, "member/Component");
    	int listSizeComponents = components.getLength();
    	
    	NodeList blocks = dom.getElements(binaryEncodingElt, "member/Block");
    	int listSizeblocks = blocks.getLength();
    	int j = 0;
    	
    	BinaryOptions[] componentEncodings = new BinaryOptions[listSizeComponents+listSizeblocks];
    	
    	for (int i=0; i<components.getLength(); i++)
    	{
    		Element componentElt = (Element)components.item(i);
    		componentEncodings[i] = readBinaryComponent(dom, componentElt);
    		j++;
    	}
    	
    	for (int i=0; i<blocks.getLength(); i++)
    	{
    		Element blockElt = (Element)blocks.item(i);
    		componentEncodings[j+i] = readBinaryBlock(dom, blockElt);
    	}
    	
    	encoding.componentEncodings = componentEncodings;
    	
    	return encoding;
    }
    

    private BinaryComponent readBinaryComponent(DOMHelper dom, Element componentElement) throws CDMException
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
    
    
    private BinaryBlock readBinaryBlock(DOMHelper dom, Element blockElt) throws CDMException
    {
        BinaryBlock binaryBlock = new BinaryBlock();

        binaryBlock.componentName = dom.getAttributeValue(blockElt, "ref");        
        binaryBlock.compression = dom.getAttributeValue(blockElt, "compression");
        
        return binaryBlock;
        
        // TODO: the other attibutes must be read and implemented...
    }
}
