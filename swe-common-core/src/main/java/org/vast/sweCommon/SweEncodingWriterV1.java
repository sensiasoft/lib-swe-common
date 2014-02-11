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

import org.w3c.dom.Element;
import org.vast.cdm.common.AsciiEncoding;
import org.vast.cdm.common.BinaryBlock;
import org.vast.cdm.common.BinaryComponent;
import org.vast.cdm.common.BinaryEncoding;
import org.vast.cdm.common.DataEncoding;
import org.vast.cdm.common.DataEncodingWriter;
import org.vast.cdm.common.StandardFormatEncoding;
import org.vast.ogc.OGCRegistry;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLWriterException;


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
public class SweEncodingWriterV1 implements DataEncodingWriter
{
    
    public SweEncodingWriterV1()
    {        
    }
    
    
    private void enforceNS(DOMHelper dom)
    {
        dom.addUserPrefix("swe", OGCRegistry.getNamespaceURI(SWECommonUtils.SWE, "1.0"));
    }
    
    
    public Element writeEncoding(DOMHelper dom, DataEncoding dataEncoding) throws XMLWriterException
    {
        Element dataEncElt = null;
        enforceNS(dom);
        
        if (dataEncoding instanceof AsciiEncoding)
        {
            dataEncElt = writeAsciiBlock(dom, (AsciiEncoding)dataEncoding);
        }
        else if (dataEncoding instanceof BinaryEncoding)
        {
            dataEncElt = writeBinaryBlock(dom, (BinaryEncoding)dataEncoding);
        }
        else if (dataEncoding instanceof StandardFormatEncoding)
        {
            dataEncElt = writeStandardFormat(dom, (StandardFormatEncoding)dataEncoding);
        }
        else
            throw new XMLWriterException("Encoding not supported: " + dataEncoding.getClass().getCanonicalName());
        
        return dataEncElt;
    }
    
    
    private Element writeAsciiBlock(DOMHelper dom, AsciiEncoding asciiEncoding) throws XMLWriterException
    {
        Element dataEncElt = dom.createElement("swe:TextBlock");
    	
        dataEncElt.setAttribute("tokenSeparator", String.valueOf(asciiEncoding.tokenSeparator));
        dataEncElt.setAttribute("blockSeparator", String.valueOf(asciiEncoding.blockSeparator));
        dataEncElt.setAttribute("decimalSeparator", String.valueOf(asciiEncoding.decimalSeparator));
    	
    	return dataEncElt;
    }
    
    
    private Element writeStandardFormat(DOMHelper dom, StandardFormatEncoding formatEncoding) throws XMLWriterException
    {
        Element dataEncElt = dom.createElement("swe:StandardFormat");    	
        dataEncElt.setAttribute("mimeType", formatEncoding.getMimeType());        
    	return dataEncElt;
    }
    
    
    private Element writeBinaryBlock(DOMHelper dom, BinaryEncoding binaryEncoding) throws XMLWriterException
    {
    	Element binaryEncElt = dom.createElement("swe:BinaryBlock");
        
        // write byteEncoding attribute
        if (binaryEncoding.byteEncoding == BinaryEncoding.ByteEncoding.BASE64)
        	binaryEncElt.setAttribute("byteEncoding", "base64");
        else if (binaryEncoding.byteEncoding == BinaryEncoding.ByteEncoding.RAW)
            binaryEncElt.setAttribute("byteEncoding", "raw");
            	    	    	
    	// write byteOrder attribute
        if (binaryEncoding.byteOrder == BinaryEncoding.ByteOrder.BIG_ENDIAN)
            binaryEncElt.setAttribute("byteOrder", "bigEndian");
        else if (binaryEncoding.byteOrder == BinaryEncoding.ByteOrder.LITTLE_ENDIAN)
            binaryEncElt.setAttribute("byteOrder", "littleEndian");
        
        // write components encoding
        for (int i=0; i<binaryEncoding.componentEncodings.length; i++)
        {
            Element propElt = dom.addElement(binaryEncElt, "+swe:member");
            
            if(binaryEncoding.componentEncodings[i] instanceof BinaryComponent)
            {
            Element componentElt = writeBinaryValue(dom, (BinaryComponent)binaryEncoding.componentEncodings[i]);
            propElt.appendChild(componentElt);
            }
            
            if(binaryEncoding.componentEncodings[i] instanceof BinaryBlock)
            {
            Element componentElt = writeBinaryBlock(dom, (BinaryBlock)binaryEncoding.componentEncodings[i]);
            propElt.appendChild(componentElt);
            }
        }
         	
    	return binaryEncElt;
    }
    

    private Element writeBinaryValue(DOMHelper dom, BinaryComponent binaryOptions) throws XMLWriterException
    {
        Element binaryEncElt = dom.createElement("swe:Component");
        
        // write component ref
        binaryEncElt.setAttribute("ref", binaryOptions.componentName);
                
        // write dataType attribute
        String dataTypeUrn = "";
        switch (binaryOptions.type)
        {
            case BOOLEAN: 
                dataTypeUrn = BinaryComponent.booleanURI;
                break;
            
            case DOUBLE: 
                dataTypeUrn = BinaryComponent.doubleURI;
                break;
            
            case FLOAT: 
                dataTypeUrn = BinaryComponent.floatURI;
                break;
                
            case BYTE: 
                dataTypeUrn = BinaryComponent.byteURI;
                break;
                
            case UBYTE: 
                dataTypeUrn = BinaryComponent.ubyteURI;
                break;
                
            case SHORT: 
                dataTypeUrn = BinaryComponent.shortURI;
                break;
                
            case USHORT: 
                dataTypeUrn = BinaryComponent.ushortURI;
                break;
                
            case INT: 
                dataTypeUrn = BinaryComponent.intURI;
                break;
                
            case UINT: 
                dataTypeUrn = BinaryComponent.uintURI;
                break;
                
            case LONG: 
                dataTypeUrn = BinaryComponent.longURI;
                break;
                
            case ULONG: 
                dataTypeUrn = BinaryComponent.ulongURI;
                break;
                
            case ASCII_STRING: 
                dataTypeUrn = BinaryComponent.asciiURI;
                break;
                
            case UTF_STRING: 
                dataTypeUrn = BinaryComponent.utfURI;
                break;
        }
        binaryEncElt.setAttribute("dataType", dataTypeUrn);
        
        // write block byteLength if any
        if(binaryOptions.byteLength != 0)
    	{
        	binaryEncElt.setAttribute("byteLength", Integer.toString(binaryOptions.byteLength));
    	}
        
        // write block paddingBefore if any
        if(binaryOptions.paddingBefore != 0)
    	{
        	binaryEncElt.setAttribute("paddingBefore", Integer.toString(binaryOptions.paddingBefore));
    	}
        
        // write block paddingAfter if any
        if(binaryOptions.paddingAfter != 0)
        {
        	binaryEncElt.setAttribute("paddingAfter", Integer.toString(binaryOptions.paddingAfter));
        }
        
     // write block paddingAfter if any
        if(binaryOptions.bitLength != 0)
        {
        	binaryEncElt.setAttribute("bitLength", Integer.toString(binaryOptions.bitLength));
        }
        
        return binaryEncElt;
    }
    
    private Element writeBinaryBlock(DOMHelper dom, BinaryBlock binaryOptions) throws XMLWriterException
    {
        Element binaryEncElt = dom.createElement("swe:Block");
        
        // write block ref
        binaryEncElt.setAttribute("ref", binaryOptions.componentName);
        
        // write block compression if any
        if(binaryOptions.compression != null)
        	{
        	 binaryEncElt.setAttribute("compression", binaryOptions.compression);
        	}
        
        // write block byteLength if any
        if(binaryOptions.byteLength != 0)
    	{
        	binaryEncElt.setAttribute("byteLength", Integer.toString(binaryOptions.byteLength));
    	}
        
        // write block paddingBefore if any
        if(binaryOptions.paddingBefore != 0)
    	{
        	binaryEncElt.setAttribute("paddingBefore", Integer.toString(binaryOptions.paddingBefore));
    	}
        
        // write block paddingAfter if any
        if(binaryOptions.paddingAfter != 0)
        {
        	binaryEncElt.setAttribute("paddingAfter", Integer.toString(binaryOptions.paddingAfter));
        }
        
        // write block encryption if any
        if(binaryOptions.encryption != null)
        {
        	binaryEncElt.setAttribute("encryption", binaryOptions.encryption);
        }
               
        return binaryEncElt;
    }
    
}
