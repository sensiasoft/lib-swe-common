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
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import org.vast.ogc.OGCRegistry;
import org.vast.xml.DOMHelper;
import org.vast.xml.IXMLReaderDOM;
import org.vast.xml.IXMLWriterDOM;
import org.vast.xml.IndentingXMLStreamWriter;
import org.vast.xml.XMLImplFinder;
import org.vast.xml.XMLReaderException;
import org.vast.xml.XMLWriterException;
import org.w3c.dom.Element;


/**
 * <p>
 * Helper class providing a version agnostic access to SWE component
 * structure and encoding readers and writers. This class delegates
 * to version specific readers/writers.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Feb 2, 2007
 * */
public class SWECommonUtils
{
	public final static String SWE = "SWE";
	public final static String SWECOMMON = "SWECommon";
    public final static String DATACOMPONENT = "DataComponent";
    public final static String DATAENCODING = "DataEncoding";
    private String version = "2.0";
    private String encoding = "UTF-8";
    private boolean versionChanged;
    private DOMHelper previousDom;
    private IXMLReaderDOM<DataComponent> componentReader = null;
    private IXMLWriterDOM<DataComponent> componentWriter = null;
    private IXMLReaderDOM<DataEncoding> encodingReader = null;
    private IXMLWriterDOM<DataEncoding> encodingWriter = null;
	    
    
    public DataComponent readComponent(DOMHelper dom, Element componentElement) throws XMLReaderException
    {
        IXMLReaderDOM<DataComponent> reader = getDataComponentReader(dom, componentElement);
        return reader.read(dom, componentElement);
    }


    public DataComponent readComponentProperty(DOMHelper dom, Element propertyElement) throws XMLReaderException
    {
        Element componentElement = dom.getFirstChildElement(propertyElement);
        IXMLReaderDOM<DataComponent> reader = getDataComponentReader(dom, componentElement);
        return reader.read(dom, componentElement);
    }
    
    
    public DataEncoding readEncoding(DOMHelper dom, Element encodingElement) throws XMLReaderException
    {
        IXMLReaderDOM<DataEncoding> reader = getDataEncodingReader(dom, encodingElement);
        return reader.read(dom, encodingElement);
    }


    public DataEncoding readEncodingProperty(DOMHelper dom, Element propertyElement) throws XMLReaderException
    {
        Element componentElement = dom.getFirstChildElement(propertyElement);
        IXMLReaderDOM<DataEncoding> reader = getDataEncodingReader(dom, componentElement);
        return reader.read(dom, componentElement);
    }
    
    
    public Element writeComponent(DOMHelper dom, DataComponent dataComponents) throws XMLWriterException
    {
        IXMLWriterDOM<DataComponent> writer = getDataComponentWriter();
        return writer.write(dom, dataComponents);
    }
    
    
    public Element writeComponent(DOMHelper dom, DataComponent dataComponents, boolean writeInlineData) throws XMLWriterException
    {
        IXMLWriterDOM<DataComponent> writer = getDataComponentWriter();
        return writer.write(dom, dataComponents);
    }
    
    
    public Element writeEncoding(DOMHelper dom, DataEncoding dataEncoding) throws XMLWriterException
    {
        IXMLWriterDOM<DataEncoding> writer = getDataEncodingWriter();
        return writer.write(dom, dataEncoding);
    }
    
    
    public DataComponent readComponent(InputStream is) throws XMLReaderException
    {
        try
        {
            SWEStaxBindings staxReader = new SWEStaxBindings();
            XMLStreamReader reader = XMLImplFinder.getStaxInputFactory().createXMLStreamReader(is, encoding);
            reader.nextTag();
            return staxReader.readDataComponent(reader);
        }
        catch (XMLStreamException e)
        {
            throw new XMLReaderException("Error while reading SWE Common Component", e);
        }
    }
    
    
    public DataEncoding readEncoding(InputStream is) throws XMLReaderException
    {
        try
        {
            SWEStaxBindings staxReader = new SWEStaxBindings();
            XMLStreamReader reader = XMLImplFinder.getStaxInputFactory().createXMLStreamReader(is, encoding);
            reader.nextTag();
            return staxReader.readAbstractEncoding(reader);
        }
        catch (XMLStreamException e)
        {
            throw new XMLReaderException("Error while reading SWE Common Encoding", e);
        }
    }
    
    
    public void writeComponent(OutputStream os, DataComponent dataComponents, boolean writeInlineData, boolean indent) throws XMLWriterException, IOException
    {
        try
        {
            SWEStaxBindings sweWriter = new SWEStaxBindings();
            XMLOutputFactory factory = XMLImplFinder.getStaxOutputFactory();
            XMLStreamWriter writer = factory.createXMLStreamWriter(os, encoding);
            if (indent)
                writer = new IndentingXMLStreamWriter(writer);
            sweWriter.setNamespacePrefixes(writer);
            sweWriter.declareNamespacesOnRootElement();
            sweWriter.writeDataComponent(writer, dataComponents, writeInlineData);
            writer.close();
        }
        catch (XMLStreamException e)
        {
            throw new XMLWriterException("Error while writing SWE Common Component", e);
        }
    }
    
    
    public void writeEncoding(OutputStream os, DataEncoding dataEncoding, boolean indent) throws XMLWriterException, IOException
    {
        try
        {
            SWEStaxBindings sweWriter = new SWEStaxBindings();
            XMLOutputFactory factory = XMLImplFinder.getStaxOutputFactory();
            XMLStreamWriter writer = factory.createXMLStreamWriter(os, encoding);
            if (indent)
                writer = new IndentingXMLStreamWriter(writer);
            sweWriter.setNamespacePrefixes(writer);
            sweWriter.declareNamespacesOnRootElement();
            sweWriter.writeAbstractEncoding(writer, dataEncoding);
            writer.close();
        }
        catch (XMLStreamException e)
        {
            throw new XMLWriterException("Error while writing SWE Common Encoding", e);
        }
    }


    /**
     * Reuses or creates the DataComponent reader corresponding to
     * the version specified by the SWE namespace URI
     * @param dom
     * @param componentElt
     * @return
     */
    private IXMLReaderDOM<DataComponent> getDataComponentReader(DOMHelper dom, Element componentElt)
    {
        if (dom == previousDom && componentReader != null)
        {
            return componentReader;
        }
        else
        {
            IXMLReaderDOM<DataComponent> reader = 
                (IXMLReaderDOM<DataComponent>)OGCRegistry.createReader(SWECOMMON, DATACOMPONENT, getVersion(dom, componentElt));
            componentReader = reader;
            return reader;
        }
    }
    
    
    /**
     * Reuses or creates the DataEncodingReader corresponding to
     * the version specified by the SWE namespace URI
     * @param dom
     * @param componentElt
     * @return
     */
    private IXMLReaderDOM<DataEncoding> getDataEncodingReader(DOMHelper dom, Element componentElt)
    {
        if (dom == previousDom && encodingReader != null)
        {
            return encodingReader;
        }
        else
        {
            IXMLReaderDOM<DataEncoding> reader =
                (IXMLReaderDOM<DataEncoding>)OGCRegistry.createReader(SWECOMMON, DATAENCODING, getVersion(dom, componentElt));
            encodingReader = reader;
            return reader;
        }
    }
    
    
    /**
     * Reuses or creates the DataComponent writer corresponding to
     * the specified version (previously set by setOutputVersion)
     * @return
     */
    private IXMLWriterDOM<DataComponent> getDataComponentWriter()
    {
        if (!versionChanged && componentWriter != null)
        {
            return componentWriter;
        }
        else
        {
            IXMLWriterDOM<DataComponent> writer =
                (IXMLWriterDOM<DataComponent>)OGCRegistry.createWriter(SWECOMMON, DATACOMPONENT, this.version);
            componentWriter = writer;
            versionChanged = false;
            return writer;
        }
    }
    
    
    /**
     * Reuses or creates the DataEncoding writer corresponding to
     * the specified version (previously set by setOutputVersion)
     * @return
     */
    private IXMLWriterDOM<DataEncoding> getDataEncodingWriter()
    {
        if (!versionChanged && encodingWriter != null)
        {
            return encodingWriter;
        }
        else
        {
            IXMLWriterDOM<DataEncoding> writer = 
                (IXMLWriterDOM<DataEncoding>)OGCRegistry.createWriter(SWECOMMON, DATAENCODING, this.version);
            encodingWriter = writer;
            versionChanged = false;
            return writer;
        }
    }
    
    
    /**
     * Logic to guess SWE version from namespace
     * @param dom
     * @param sweElt 
     * @return version
     */
    public String getVersion(DOMHelper dom, Element sweElt)
    {
        // get version from the last part of namespace URI
        //String sweUri = dom.getXmlDocument().getNSUri("swe");
        String sweUri = sweElt.getNamespaceURI();
        String version = sweUri.substring(sweUri.lastIndexOf('/') + 1);
        
        // check if version is a valid version number otherwise defaults to 0
        if (!version.matches("^\\d+(\\.\\d+)?(\\.\\d+)?$"))
            version = "0.0";
        
        previousDom = dom;
        return version;
    }
    
    
    /**
     * Used to set the SWECommon version to use for XML output
     * @param version
     */
    public void setOutputVersion(String version)
    {
        this.version = version;
        this.versionChanged = true;
    }
    
    
    /**
     * To set output encoding (defaults to UTF-8)
     * @param encoding
     */
    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }
}
