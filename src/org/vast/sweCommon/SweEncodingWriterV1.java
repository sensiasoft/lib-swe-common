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
 * SWE Encoding Writer v1.0
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
public class SweEncodingWriterV1 implements DataEncodingWriter
{
    
    public SweEncodingWriterV1()
    {        
    }
    
    
    private void enforceNS(DOMHelper dom)
    {
        dom.addUserPrefix("swe", OGCRegistry.getNamespaceURI(OGCRegistry.SWE, "1.0"));
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
        else if (dataEncoding instanceof StandardFormatEncoding)
        {
            dataEncElt = writeStandardFormat(dom, (StandardFormatEncoding)dataEncoding);
        }
        else
            throw new CDMException("Encoding not supported");
        
        return dataEncElt;
    }
    
    
    private Element writeAsciiBlock(DOMHelper dom, AsciiEncoding asciiEncoding) throws CDMException
    {
        Element dataEncElt = dom.createElement("swe:AsciiBlock");
    	
        dataEncElt.setAttribute("tokenSeparator", String.valueOf(asciiEncoding.tokenSeparator));
        dataEncElt.setAttribute("tupleSeparator", String.valueOf(asciiEncoding.blockSeparator));
        dataEncElt.setAttribute("decimalSeparator", String.valueOf(asciiEncoding.decimalSeparator));
    	
    	return dataEncElt;
    }
    
    
    private Element writeStandardFormat(DOMHelper dom, StandardFormatEncoding formatEncoding) throws CDMException
    {
        Element dataEncElt = dom.createElement("swe:StandardFormat");    	
        dataEncElt.setAttribute("mimeType", formatEncoding.getMimeType());        
    	return dataEncElt;
    }
    
    
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
            Element componentElt = writeBinaryValue(dom, binaryEncoding.componentEncodings[i]);
            propElt.appendChild(componentElt);
        }
         	
    	return binaryEncElt;
    }
    

    private Element writeBinaryValue(DOMHelper dom, BinaryOptions binaryOptions) throws CDMException
    {
        Element binaryEncElt = dom.createElement("swe:Component");
        
        // write component ref
        binaryEncElt.setAttribute("ref", binaryOptions.componentName);
                
        // write dataType attribute
        String dataTypeUrn = "";
        switch (binaryOptions.type)
        {
            case BOOLEAN: 
                dataTypeUrn = BinaryOptions.booleanURN;
                break;
            
            case DOUBLE: 
                dataTypeUrn = BinaryOptions.doubleURN;
                break;
            
            case FLOAT: 
                dataTypeUrn = BinaryOptions.floatURN;
                break;
                
            case BYTE: 
                dataTypeUrn = BinaryOptions.byteURN;
                break;
                
            case UBYTE: 
                dataTypeUrn = BinaryOptions.ubyteURN;
                break;
                
            case SHORT: 
                dataTypeUrn = BinaryOptions.shortURN;
                break;
                
            case USHORT: 
                dataTypeUrn = BinaryOptions.ushortURN;
                break;
                
            case INT: 
                dataTypeUrn = BinaryOptions.intURN;
                break;
                
            case UINT: 
                dataTypeUrn = BinaryOptions.uintURN;
                break;
                
            case LONG: 
                dataTypeUrn = BinaryOptions.longURN;
                break;
                
            case ULONG: 
                dataTypeUrn = BinaryOptions.ulongURN;
                break;
                
            case ASCII_STRING: 
                dataTypeUrn = BinaryOptions.asciiURN;
                break;
                
            case UTF_STRING: 
                dataTypeUrn = BinaryOptions.utfURN;
                break;
        }
        binaryEncElt.setAttribute("dataType", dataTypeUrn);
        
        // TODO write other attributes (padding, encryption, compression...)
               
        return binaryEncElt;
    }
}
