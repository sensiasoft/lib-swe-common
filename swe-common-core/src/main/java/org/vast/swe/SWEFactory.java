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

package org.vast.swe;

import net.opengis.swe.v20.ByteEncoding;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.JSONEncoding;
import net.opengis.swe.v20.TextEncoding;
import net.opengis.swe.v20.XMLEncoding;
import org.vast.cdm.common.DataStreamParser;
import org.vast.cdm.common.DataStreamWriter;


public class SWEFactory
{
        
    /**
     * Helper method to instantiate the proper parser for the given encoding
     * @param encoding
     * @return instance of parser capable of handling the given encoding
     */
    public static DataStreamParser createDataParser(DataEncoding encoding)
    {
        DataStreamParser parser = null;
        
        if (encoding instanceof TextEncoding)
            parser = new AsciiDataParser();
        else if (encoding instanceof BinaryEncoding)
            parser = new BinaryDataParser();
        else if (encoding instanceof XMLEncoding)
            parser = new XmlDataParser();
        else if (encoding instanceof JSONEncoding)
            parser = new JSONDataParser();
        
        parser.setDataEncoding(encoding);
        return parser;
    }
    
    
    /**
     * Helper method to instantiate the proper writer for the given encoding
     * @param encoding
     * @return instance of writer capable of handling the given encoding
     */
    public static DataStreamWriter createDataWriter(DataEncoding encoding)
    {
        DataStreamWriter writer = null;
        
        if (encoding instanceof TextEncoding)
            writer = new AsciiDataWriter();
        else if (encoding instanceof BinaryEncoding)
            writer = new BinaryDataWriter();
        else if (encoding instanceof XMLEncoding)
            writer = new XmlDataWriter();
        else if (encoding instanceof JSONEncoding)
            writer = new JSONDataWriter();
        
        writer.setDataEncoding(encoding);
        return writer;
    }
    
    
    /**
     * Ajusts encoding settings to ensures that data produced with the encoding
     * can be embedded in XML (e.g. for binary encoding this enforces base64)
     * @param encoding
     * @return copy of encoding, reconfigured appropriately
     */
    public static DataEncoding ensureXmlCompatible(DataEncoding encoding)
    {
        // if binary data, ensure encoding is set to base64
        if (encoding instanceof BinaryEncoding)
        {
            encoding = encoding.copy();
            ((BinaryEncoding) encoding).setByteEncoding(ByteEncoding.BASE_64);
        }
        
        return encoding;
    }
}
