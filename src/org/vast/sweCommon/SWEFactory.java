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

import org.vast.cdm.common.DataEncoding;
import org.vast.cdm.common.DataStreamParser;
import org.vast.cdm.common.DataStreamWriter;


public class SWEFactory
{
        
    public static DataStreamParser createDataParser(DataEncoding encoding)
    {
        DataStreamParser parser = null;
        
        switch (encoding.getEncodingType())
        {
            case ASCII:
                parser = new AsciiDataParser();
                break;
                
            case BINARY:
                parser = new BinaryDataParser();
                break;
                
            case XML:
                parser = new XmlDataParser();
                break;
                
            case MIME_FORMAT:
            	// TODO develop framework to plugin mime format parsers
                return null;
        }
        
        parser.setDataEncoding(encoding);
        return parser;
    }
    
    
    public static DataStreamWriter createDataWriter(DataEncoding encoding)
    {
        DataStreamWriter writer = null;
        
        switch (encoding.getEncodingType())
        {
            case ASCII:
                writer = new AsciiDataWriter();
                break;
                
            case BINARY:
                writer = new BinaryDataWriter();
                break;
                
            case XML:
            	writer = new XmlDataWriter();
                break;
                
            case MIME_FORMAT:
            	// TODO develop framework to plug mime format writers
                return null;
        }
        
        writer.setDataEncoding(encoding);
        return writer;
    }
}
