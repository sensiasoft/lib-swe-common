/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.cdm.writer;

import org.w3c.dom.*;
import org.vast.cdm.common.*;
import org.vast.io.xml.*;


/**
 * <p><b>Title:</b><br/>
 * Encoding Writer
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
public class EncodingWriter implements DataEncodingWriter
{
    DOMWriter dom;
    
    
    public EncodingWriter(DOMWriter parentWriter)
    {
        dom = parentWriter;
        dom.addNS("http://www.opengis.net/swe", "swe");
    }
    
    
    public Element writeDataEncoding(DataEncoding dataEncoding) throws CDMException
    {
        Element dataEncElt = null;
        
        if (dataEncoding instanceof AsciiEncoding)
        {
            dataEncElt = writeAsciiBlock((AsciiEncoding)dataEncoding);
        }
        else if (dataEncoding instanceof BinaryEncoding)
        {
            dataEncElt = writeBinaryBlock((BinaryEncoding)dataEncoding);
        }
        else
            throw new CDMException("Encoding not supported");
        
        return dataEncElt;
    }
    
    
    private Element writeAsciiBlock(AsciiEncoding asciiEncoding) throws CDMException
    {
        Element dataEncElt = dom.createElement("swe:AsciiBlock");
    	
        dataEncElt.setAttribute("tokenSeparator", String.valueOf(asciiEncoding.tokenSeparator));
        dataEncElt.setAttribute("tupleSeparator", String.valueOf(asciiEncoding.tupleSeparator));
        dataEncElt.setAttribute("decimalSeparator", String.valueOf(asciiEncoding.decimalSeparator));
    	
    	return dataEncElt;
    }
    
    
    private Element writeBinaryBlock(BinaryEncoding binaryEncoding) throws CDMException
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
            Element componentElt = writeBinaryValue(binaryEncoding.componentEncodings[i]);
            propElt.appendChild(componentElt);
        }
         	
    	return binaryEncElt;
    }
    

    private Element writeBinaryValue(BinaryOptions binaryOptions) throws CDMException
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
