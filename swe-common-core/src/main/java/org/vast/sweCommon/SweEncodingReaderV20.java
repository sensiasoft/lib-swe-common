/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SWE Common Data Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sweCommon;

import java.nio.ByteOrder;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.BinaryBlock;
import net.opengis.swe.v20.BinaryComponent;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.ByteEncoding;
import net.opengis.swe.v20.TextEncoding;
import net.opengis.swe.v20.XMLEncoding;
import org.vast.data.BinaryBlockImpl;
import org.vast.data.BinaryComponentImpl;
import org.vast.data.BinaryEncodingImpl;
import org.vast.data.TextEncodingImpl;
import org.vast.data.XMLEncodingImpl;
import org.vast.xml.DOMHelper;
import org.vast.xml.IXMLReaderDOM;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p>
 * Reads SWE Encoding definition for TextBlock, XMLBlock and BinaryBlock.
 * This is for version 2.0 of the SWECommon standard.
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin (Spot Image)
 * @since Feb 1, 2008
 * @version 1.0
 */
public class SweEncodingReaderV20 implements IXMLReaderDOM<DataEncoding>
{
    
    public SweEncodingReaderV20()
    {
    }
    
    
    public DataEncoding read(DOMHelper dom, Element encodingElt) throws XMLReaderException
    {
        DataEncoding encoding = null;
    	String encodingName = encodingElt.getLocalName();
    	
        if (encodingName.equals("TextEncoding"))
        	encoding = readTextEncodingOptions(dom, encodingElt);
        else if (encodingName.equals("XMLEncoding"))
        	encoding = readXmlEncodingOptions(dom, encodingElt);
        else if (encodingName.equals("BinaryEncoding"))
        	encoding = readBinaryEncodingOptions(dom, encodingElt);
        else
        	throw new XMLReaderException("Encoding not supported: " + encodingName, encodingElt);

        return encoding;
    }
    
    
    private TextEncoding readTextEncodingOptions(DOMHelper dom, Element asciiEncodingElt) throws XMLReaderException
    {
        TextEncoding encoding = new TextEncodingImpl();
    	
    	String decimalSep = dom.getAttributeValue(asciiEncodingElt, "decimalSeparator");
    	if (decimalSep != null)
    		encoding.setDecimalSeparator(decimalSep);
    	
    	encoding.setTokenSeparator(dom.getAttributeValue(asciiEncodingElt, "tokenSeparator"));
    	encoding.setBlockSeparator(dom.getAttributeValue(asciiEncodingElt, "blockSeparator"));
    	
    	String colWS = dom.getAttributeValue(asciiEncodingElt, "collapseWhiteSpaces");
    	if (colWS != null)
    		encoding.setCollapseWhiteSpaces(Boolean.parseBoolean(colWS));
    	
    	return encoding;
    }
    
    
    private XMLEncoding readXmlEncodingOptions(DOMHelper dom, Element xmlEncodingElt) throws XMLReaderException
    {
        XMLEncoding encoding = new XMLEncodingImpl();    	
    	return encoding;
    }
    
    
    private BinaryEncoding readBinaryEncodingOptions(DOMHelper dom, Element binaryEncodingElt) throws XMLReaderException
    {
    	BinaryEncoding encoding = new BinaryEncodingImpl();
    	
    	// parse byte length
    	String byteLength = dom.getAttributeValue(binaryEncodingElt, "byteLength");
    	if (byteLength != null)
    		encoding.setByteLength(Long.parseLong(byteLength));
    	
    	// parse byte encoding
    	String byteEncoding = dom.getAttributeValue(binaryEncodingElt, "byteEncoding");
    	if (byteEncoding.equalsIgnoreCase("base64"))
        {
    		encoding.setByteEncoding(ByteEncoding.BASE_64);
        }
        else if (byteEncoding.equalsIgnoreCase("raw"))
        {
        	encoding.setByteEncoding(ByteEncoding.RAW);
        }
    	
    	// parse byte order
    	String byteOrder = dom.getAttributeValue(binaryEncodingElt, "byteOrder");
    	if (byteOrder.equalsIgnoreCase("bigEndian"))
        {
    		encoding.setByteOrder(ByteOrder.BIG_ENDIAN);
        }
        else if (byteOrder.equalsIgnoreCase("littleEndian"))
        {
            encoding.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        }
    	
    	// parse binary component and block settings
    	NodeList components = dom.getElements(binaryEncodingElt, "member/*");
    	for (int i=0; i<components.getLength(); i++)
    	{
    		Element componentElt = (Element)components.item(i);    		
    		if (componentElt.getLocalName().equals("Component"))
    		    encoding.addMemberAsComponent(readBinaryComponent(dom, componentElt));
    		else if (componentElt.getLocalName().equals("Block"))
                encoding.addMemberAsBlock(readBinaryBlock(dom, componentElt));
    	}
    	
    	return encoding;
    }
    

    private BinaryComponent readBinaryComponent(DOMHelper dom, Element componentElement) throws XMLReaderException
    {
        BinaryComponent binaryValue = new BinaryComponentImpl();
        
        // read component name
        String ref = dom.getAttributeValue(componentElement, "ref");
        binaryValue.setRef(ref);
                
        // read dataType attribute
        String dataType = dom.getAttributeValue(componentElement, "dataType");
        binaryValue.setDataType(dataType);
                
        // read bitLength
        String bitLength = dom.getAttributeValue(componentElement, "bitLength");
        if (bitLength != null)
        	binaryValue.setBitLength(Integer.parseInt(bitLength));
               
        // TODO: the other attibutes must be read and implemented...
        
        return binaryValue;
    }
    
    
    private BinaryBlock readBinaryBlock(DOMHelper dom, Element blockElt) throws XMLReaderException
    {
        BinaryBlock binaryBlock = new BinaryBlockImpl();

        binaryBlock.setRef(dom.getAttributeValue(blockElt, "ref"));        
        binaryBlock.setCompression(dom.getAttributeValue(blockElt, "compression"));
        
        // TODO: the other attibutes must be read and implemented...
        
        return binaryBlock;
    }
}
