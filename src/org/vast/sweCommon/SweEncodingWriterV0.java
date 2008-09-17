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
import org.vast.ogc.OGCRegistry;
import org.vast.xml.DOMHelper;


/**
 * <p><b>Title:</b><br/>
 * SWE Encoding Writer v0.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Writes the encoding section of the SWE DataDefinition element.
 * This class handles ASCIIBlock and BinaryBlock.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Feb 10, 2006
 * @version 1.0
 */
public class SweEncodingWriterV0 implements DataEncodingWriter
{
    
    public SweEncodingWriterV0()
    {        
    }
    
    
    private void enforceNS(DOMHelper dom)
    {
        dom.addUserPrefix("swe", OGCRegistry.getNamespaceURI(OGCRegistry.SWE, "0.0"));
    }
    
    
    public Element writeEncoding(DOMHelper dom, DataEncoding dataEncoding) throws CDMException
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
        else
            throw new CDMException("Encoding not supported");
        
        return dataEncElt;
    }
    
    
    /**
     * Create DOM Element for an AsciiBlock encoding definition
     * @param dom
     * @param asciiEncoding
     * @return
     * @throws CDMException
     */
    private Element writeAsciiBlock(DOMHelper dom, AsciiEncoding asciiEncoding) throws CDMException
    {
        Element dataEncElt = dom.createElement("swe:AsciiBlock");
    	
        dataEncElt.setAttribute("tokenSeparator", String.valueOf(asciiEncoding.tokenSeparator));
        dataEncElt.setAttribute("tupleSeparator", String.valueOf(asciiEncoding.blockSeparator));
        dataEncElt.setAttribute("decimalSeparator", String.valueOf(asciiEncoding.decimalSeparator));
    	
    	return dataEncElt;
    }
    
    
    /**
     * Create DOM Element for a BinaryBlock encoding definition
     * @param dom
     * @param binaryEncoding
     * @return
     * @throws CDMException
     */
    private Element writeBinaryBlock(DOMHelper dom, BinaryEncoding binaryEncoding) throws CDMException
    {
    	Element binaryEncElt = dom.createElement("swe:BinaryBlock");
        
        // write byteEncoding attribute
        if (binaryEncoding.byteEncoding == BinaryEncoding.ByteEncoding.BASE64)
        {
            binaryEncElt.setAttribute("byteEncoding", "base64");
        }
        else if (binaryEncoding.byteEncoding == BinaryEncoding.ByteEncoding.RAW)
        {
            binaryEncElt.setAttribute("byteEncoding", "raw");
        }
    	    	    	
    	// write byteOrder attribute
        if (binaryEncoding.byteOrder == BinaryEncoding.ByteOrder.BIG_ENDIAN)
        {
            binaryEncElt.setAttribute("byteOrder", "bigEndian");
        }
        else if (binaryEncoding.byteOrder == BinaryEncoding.ByteOrder.LITTLE_ENDIAN)
        {
            binaryEncElt.setAttribute("byteOrder", "littleEndian");
        }
        
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
    

    private Element writeBinaryValue(DOMHelper dom, BinaryComponent binaryOptions) throws CDMException
    {
        Element binaryEncElt = dom.createElement("swe:Component");
        
        // write component ref
        binaryEncElt.setAttribute("ref", binaryOptions.componentName);
                
        // write dataType attribute
        String dataTypeUrn = "";
        switch (binaryOptions.type)
        {
            case BOOLEAN: 
                dataTypeUrn = BinaryComponent.booleanURN;
                break;
            
            case DOUBLE: 
                dataTypeUrn = BinaryComponent.doubleURN;
                break;
            
            case FLOAT: 
                dataTypeUrn = BinaryComponent.floatURN;
                break;
                
            case BYTE: 
                dataTypeUrn = BinaryComponent.byteURN;
                break;
                
            case UBYTE: 
                dataTypeUrn = BinaryComponent.ubyteURN;
                break;
                
            case SHORT: 
                dataTypeUrn = BinaryComponent.shortURN;
                break;
                
            case USHORT: 
                dataTypeUrn = BinaryComponent.ushortURN;
                break;
                
            case INT: 
                dataTypeUrn = BinaryComponent.intURN;
                break;
                
            case UINT: 
                dataTypeUrn = BinaryComponent.uintURN;
                break;
                
            case LONG: 
                dataTypeUrn = BinaryComponent.longURN;
                break;
                
            case ULONG: 
                dataTypeUrn = BinaryComponent.ulongURN;
                break;
                
            case ASCII_STRING: 
                dataTypeUrn = BinaryComponent.asciiURN;
                break;
                
            case UTF_STRING: 
                dataTypeUrn = BinaryComponent.utfURN;
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
    
    private Element writeBinaryBlock(DOMHelper dom, BinaryBlock binaryOptions) throws CDMException
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
