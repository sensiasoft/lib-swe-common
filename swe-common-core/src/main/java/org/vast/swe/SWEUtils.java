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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.DataStream;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLBindingsUtils;
import org.vast.xml.XMLReaderException;
import org.vast.xml.XMLWriterException;
import org.w3c.dom.Element;


/**
 * <p>
 * Helper class providing a version agnostic access to SWE component
 * structure and encoding readers and writers. This class delegates
 * to version specific bindings.
 * </p>
 *
 * @author Alex Robin
 * @since Feb 2, 2007
 * */
public class SWEUtils extends XMLBindingsUtils
{
    public final static String SWE = "SWE";
    public final static String V2_0 = "2.0";

    boolean writeInlineData;
    
    
    enum ObjectType
    {
        DataComponent { @Override
        public String toString() { return "SWE Data Component"; } },
        Encoding { @Override
        public String toString() { return "SWE Encoding"; } },
        DataStream { @Override
        public String toString() { return "SWE Data Stream"; } }
    }
    
    
    public SWEUtils(String version)
    {
        // TODO select proper bindings for selected version
        staxBindings = new SWEStaxBindings();
    }
    
    
    public DataComponent readComponent(DOMHelper dom, Element componentElt) throws XMLReaderException
    {
        return (DataComponent)readFromDom(dom, componentElt, ObjectType.DataComponent);
    }
    
    
    public DataComponent readComponent(InputStream is) throws XMLReaderException
    {
        return (DataComponent)readFromStream(is, ObjectType.DataComponent);
    }
    
    
    public DataStream readDataStream(DOMHelper dom, Element componentElt) throws XMLReaderException
    {
        return (DataStream)readFromDom(dom, componentElt, ObjectType.DataStream);
    }
    
    
    public DataStream readDataStream(InputStream is) throws XMLReaderException
    {
        return (DataStream)readFromStream(is, ObjectType.DataStream);
    }
    
    
    public DataEncoding readEncoding(DOMHelper dom, Element encodingElt) throws XMLReaderException
    {
        return (DataEncoding)readFromDom(dom, encodingElt, ObjectType.Encoding);
    }
    
    
    public DataEncoding readEncoding(InputStream is) throws XMLReaderException
    {
        return (DataEncoding)readFromStream(is, ObjectType.Encoding);
    }
    
    
    public Element writeComponent(DOMHelper dom, DataComponent dataComponents, boolean writeInlineData) throws XMLWriterException
    {
        this.writeInlineData = writeInlineData;
        return writeToDom(dom, dataComponents, ObjectType.DataComponent);
    }
    
    
    public void writeComponent(OutputStream os, DataComponent dataComponents, boolean writeInlineData, boolean indent) throws XMLWriterException, IOException
    {
        this.writeInlineData = writeInlineData;
        writeToStream(os, dataComponents, ObjectType.DataComponent, indent);
    }
    
    
    public Element writeDataStream(DOMHelper dom, DataStream dataStream) throws XMLWriterException
    {
        return writeToDom(dom, dataStream, ObjectType.DataStream);
    }
    
    
    public void writeDataStream(OutputStream os, DataStream dataStream, boolean indent) throws XMLWriterException, IOException
    {
        writeToStream(os, dataStream, ObjectType.DataStream, indent);
    }
    
    
    public Element writeEncoding(DOMHelper dom, DataEncoding dataEncoding) throws XMLWriterException
    {
        return writeToDom(dom, dataEncoding, ObjectType.Encoding);
    }
    
    
    public void writeEncoding(OutputStream os, DataEncoding dataEncoding, boolean indent) throws XMLWriterException, IOException
    {
        writeToStream(os, dataEncoding, ObjectType.Encoding, indent);
    }
    
    
    @Override
    protected Object readFromXmlStream(XMLStreamReader reader, Enum<?> eltType) throws XMLStreamException
    {
        reader.nextTag();
        SWEStaxBindings sweBindings = (SWEStaxBindings)staxBindings;
        
        switch ((ObjectType)eltType)
        {
            case DataComponent:
                return sweBindings.readDataComponent(reader);
                
            case DataStream:
                return sweBindings.readDataStream(reader);
                
            case Encoding:
                return sweBindings.readAbstractEncoding(reader);
        }
        
        return null;
    }
    
    
    @Override
    protected void writeToXmlStream(XMLStreamWriter writer, Object sweObj, Enum<?> eltType) throws XMLStreamException
    {
        SWEStaxBindings sweBindings = (SWEStaxBindings)staxBindings;
        
        switch ((ObjectType)eltType)
        {
            case DataComponent:
                sweBindings.writeDataComponent(writer, (DataComponent)sweObj, writeInlineData);
                return;
                
            case DataStream:
                sweBindings.writeDataStream(writer, (DataStream)sweObj);
                return;
                
            case Encoding:
                sweBindings.writeAbstractEncoding(writer, (DataEncoding)sweObj);
                return;
        }
    }
}
