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
import net.opengis.swe.v20.AbstractEncoding;
import net.opengis.swe.v20.BinaryBlock;
import net.opengis.swe.v20.BinaryComponent;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.BinaryMember;
import net.opengis.swe.v20.ByteEncoding;
import net.opengis.swe.v20.TextEncoding;
import net.opengis.swe.v20.XMLEncoding;
import org.vast.ogc.OGCRegistry;
import org.vast.xml.DOMHelper;
import org.vast.xml.IXMLWriterDOM;
import org.vast.xml.XMLWriterException;
import org.w3c.dom.Element;


/**
 * <p>
 * Writes the encoding section of the SWE DataDefinition element.
 * This class handles ASCIIBlock and BinaryBlock.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @since Feb 10, 2006
 * @version 1.0
 */
public class SweEncodingWriterV20 implements IXMLWriterDOM<AbstractEncoding>
{
    
    public SweEncodingWriterV20()
    {
    }
    
    
    private void enforceNS(DOMHelper dom)
    {
        dom.addUserPrefix("swe", OGCRegistry.getNamespaceURI(SWECommonUtils.SWE, "2.0"));
    }
    
    
    public Element write(DOMHelper dom, AbstractEncoding dataEncoding) throws XMLWriterException
    {
        Element dataEncElt = null;
        enforceNS(dom);
        
        if (dataEncoding instanceof TextEncoding)
            dataEncElt = writeTextEncodingOptions(dom, (TextEncoding)dataEncoding);
        else if (dataEncoding instanceof BinaryEncoding)
            dataEncElt = writeBinaryEncoding(dom, (BinaryEncoding)dataEncoding);
        else if (dataEncoding instanceof XMLEncoding)
            dataEncElt = writeXmlEncodingOptions(dom, (XMLEncoding)dataEncoding);
        else
            throw new XMLWriterException("Encoding not supported: " + dataEncoding.getClass().getCanonicalName());
        
        return dataEncElt;
    }
    
    
    private Element writeTextEncodingOptions(DOMHelper dom, TextEncoding asciiEncoding) throws XMLWriterException
    {
        Element dataEncElt = dom.createElement("swe:TextEncoding");
    	
        dataEncElt.setAttribute("tokenSeparator", asciiEncoding.getTokenSeparator());
        dataEncElt.setAttribute("blockSeparator", asciiEncoding.getBlockSeparator());
        dataEncElt.setAttribute("decimalSeparator", asciiEncoding.getDecimalSeparator());
        dataEncElt.setAttribute("collapseWhiteSpaces", asciiEncoding.getCollapseWhiteSpaces() ? "true" : "false");
    	
    	return dataEncElt;
    }
    
    
    private Element writeXmlEncodingOptions(DOMHelper dom, XMLEncoding xmlEncoding) throws XMLWriterException
    {
        Element dataEncElt = dom.createElement("swe:XMLEncoding");       
    	return dataEncElt;
    }
    
    
    private Element writeBinaryEncoding(DOMHelper dom, BinaryEncoding binaryEncoding) throws XMLWriterException
    {
    	Element binaryEncElt = dom.createElement("swe:BinaryEncoding");
        
        // write byteEncoding attribute
        if (binaryEncoding.getByteEncoding() == ByteEncoding.BASE_64)
            binaryEncElt.setAttribute("byteEncoding", "base64");
        else if (binaryEncoding.getByteEncoding() == ByteEncoding.RAW)
            binaryEncElt.setAttribute("byteEncoding", "raw");
            	    	
    	// write byteOrder attribute
        if (binaryEncoding.getByteOrder() == ByteOrder.BIG_ENDIAN)
            binaryEncElt.setAttribute("byteOrder", "bigEndian");
        else if (binaryEncoding.getByteOrder() == ByteOrder.LITTLE_ENDIAN)
            binaryEncElt.setAttribute("byteOrder", "littleEndian");
        
        // write components encoding
        for (BinaryMember member: binaryEncoding.getMemberList())
        {
            Element propElt = dom.addElement(binaryEncElt, "+swe:member");
            
            if (member instanceof BinaryComponent)
            {
	            Element componentElt = writeBinaryComponent(dom, (BinaryComponent)member);
	            propElt.appendChild(componentElt);
            }
            
            if (member instanceof BinaryBlock)
            {
	            Element componentElt = writeBinaryBlock(dom, (BinaryBlock)member);
	            propElt.appendChild(componentElt);
            }
        }
         	
    	return binaryEncElt;
    }
    

    private Element writeBinaryComponent(DOMHelper dom, BinaryComponent binaryOptions) throws XMLWriterException
    {
        Element binaryEncElt = dom.createElement("swe:Component");
        
        // write component ref
        binaryEncElt.setAttribute("ref", binaryOptions.getRef());
                
        // write dataType attribute
        binaryEncElt.setAttribute("dataType", binaryOptions.getDataType());
        
        // write byteLength if any
        if (binaryOptions.isSetByteLength())
    		binaryEncElt.setAttribute("byteLength", Integer.toString(binaryOptions.getByteLength()));
    	        
        // write bitLength if any
        if (binaryOptions.isSetBitLength())
        	binaryEncElt.setAttribute("bitLength", Integer.toString(binaryOptions.getBitLength()));
        
        return binaryEncElt;
    }
    
    
    private Element writeBinaryBlock(DOMHelper dom, BinaryBlock binaryOptions) throws XMLWriterException
    {
        Element binaryEncElt = dom.createElement("swe:Block");
        
        // write block ref
        binaryEncElt.setAttribute("ref", binaryOptions.getRef());
        
        // write block compression if any
        if(binaryOptions.isSetEncryption())
        	binaryEncElt.setAttribute("compression", binaryOptions.getCompression());
                
        // write block byteLength if any
        if(binaryOptions.isSetByteLength())
    		binaryEncElt.setAttribute("byteLength", Long.toString(binaryOptions.getByteLength()));
    	        
        // write block paddingBefore if any
        if(binaryOptions.isSetPaddingBytesBefore())
    		binaryEncElt.setAttribute("paddingBytesBefore", Integer.toString(binaryOptions.getPaddingBytesBefore()));
    	        
        // write block paddingAfter if any
        if(binaryOptions.isSetPaddingBytesAfter())
        	binaryEncElt.setAttribute("paddingBytesAfter", Integer.toString(binaryOptions.getPaddingBytesAfter()));
        
        // write block encryption if any
        if(binaryOptions.isSetEncryption())
        	binaryEncElt.setAttribute("encryption", binaryOptions.getEncryption());
               
        return binaryEncElt;
    }
}
