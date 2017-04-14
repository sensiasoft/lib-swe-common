/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.swe.v20.bind;

import java.util.Map;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import net.opengis.AbstractXMLStreamBindings;
import net.opengis.IDateTime;
import net.opengis.HrefResolverXML;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.AbstractSWE;
import net.opengis.swe.v20.AbstractSWEIdentifiable;
import net.opengis.swe.v20.SimpleComponent;
import net.opengis.swe.v20.AllowedTimes;
import net.opengis.swe.v20.AllowedTokens;
import net.opengis.swe.v20.AllowedValues;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.BinaryBlock;
import net.opengis.swe.v20.BinaryMember;
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.ByteEncoding;
import net.opengis.swe.v20.ByteOrder;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.CategoryRange;
import net.opengis.swe.v20.BinaryComponent;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.CountRange;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.DataStream;
import net.opengis.swe.v20.EncodedValues;
import net.opengis.swe.v20.Matrix;
import net.opengis.swe.v20.NilValue;
import net.opengis.swe.v20.NilValues;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.QuantityRange;
import net.opengis.swe.v20.ScalarComponent;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.TextEncoding;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.TimeRange;
import net.opengis.swe.v20.UnitReference;
import net.opengis.swe.v20.Vector;
import net.opengis.swe.v20.XMLEncoding;
import net.opengis.swe.v20.Factory;


@SuppressWarnings("javadoc")
public class XMLStreamBindings extends AbstractXMLStreamBindings
{
    public final static String NS_URI = "http://www.opengis.net/swe/2.0";
    protected Factory factory;
    
    
    public XMLStreamBindings(Factory factory)
    {
        this.factory = factory;
    }
    
    
    /**
     * Read method for DataRecordType complex type
     */
    public DataRecord readDataRecordType(XMLStreamReader reader) throws XMLStreamException
    {
        DataRecord bean = factory.newDataRecord();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readDataRecordTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readDataRecordTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of DataRecordType complex type
     */
    public void readDataRecordTypeAttributes(Map<String, String> attrMap, DataRecord bean) throws XMLStreamException
    {
        this.readAbstractDataComponentTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of DataRecordType complex type
     */
    public void readDataRecordTypeElements(final XMLStreamReader reader, DataRecord bean) throws XMLStreamException
    {
        this.readAbstractDataComponentTypeElements(reader, bean);
        
        boolean found;
        
        // field
        do
        {
            found = checkElementName(reader, "field");
            if (found)
            {
                final OgcProperty<DataComponent> fieldProp = new OgcPropertyImpl<DataComponent>();
                readPropertyAttributes(reader, fieldProp);
                
                reader.nextTag();
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
                {
                    fieldProp.setValue(this.readDataComponent(reader));                    
                    reader.nextTag(); // end property tag
                }
                else if (fieldProp.hasHref())
                {
                    fieldProp.setHrefResolver(new HrefResolverXML() {
                        @Override
                        public void parseContent(XMLStreamReader reader) throws XMLStreamException
                        {
                            fieldProp.setValue(readDataComponent(reader));
                        }
                    });
                }

                bean.getFieldList().add(fieldProp);
                reader.nextTag();
            }
        }
        while (found);
    }
    
    
    /**
     * Write method for DataRecordType complex type
     */
    public void writeDataRecordType(XMLStreamWriter writer, DataRecord bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeDataRecordTypeAttributes(writer, bean);
        this.writeDataRecordTypeElements(writer, bean, writeInlineValues);
    }
    
    
    /**
     * Writes attributes of DataRecordType complex type
     */
    public void writeDataRecordTypeAttributes(XMLStreamWriter writer, DataRecord bean) throws XMLStreamException
    {
        this.writeAbstractDataComponentTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of DataRecordType complex type
     */
    public void writeDataRecordTypeElements(XMLStreamWriter writer, DataRecord bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeAbstractDataComponentTypeElements(writer, bean);
        int numItems;
        
        // field
        numItems = bean.getFieldList().size();
        for (int i = 0; i < numItems; i++)
        {
            OgcProperty<DataComponent> item = bean.getFieldList().getProperty(i);
            writer.writeStartElement(NS_URI, "field");
            writePropertyAttributes(writer, item);
            if (item.hasValue() && !item.hasHref())
                this.writeDataComponent(writer, item.getValue(), writeInlineValues);
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for VectorType complex type
     */
    public Vector readVectorType(XMLStreamReader reader) throws XMLStreamException
    {
        Vector bean = factory.newVector();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readVectorTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readVectorTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of VectorType complex type
     */
    public void readVectorTypeAttributes(Map<String, String> attrMap, Vector bean) throws XMLStreamException
    {
        this.readAbstractDataComponentTypeAttributes(attrMap, bean);
        
        String val;
        
        // referenceframe
        val = attrMap.get("referenceFrame");
        if (val != null)
            bean.setReferenceFrame(val);
        
        // localframe
        val = attrMap.get("localFrame");
        if (val != null)
            bean.setLocalFrame(val);
    }
    
    
    /**
     * Reads elements of VectorType complex type
     */
    public void readVectorTypeElements(XMLStreamReader reader, Vector bean) throws XMLStreamException
    {
        this.readAbstractDataComponentTypeElements(reader, bean);
        
        boolean found;
        
        // coordinate
        do
        {
            found = checkElementName(reader, "coordinate");
            if (found)
            {
                OgcProperty<ScalarComponent> coordProp = new OgcPropertyImpl<ScalarComponent>();
                readPropertyAttributes(reader, coordProp);
                
                reader.nextTag();
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
                {
                    String localName = reader.getName().getLocalPart();
                    
                    if (localName.equals("Count"))
                    {
                        Count coordinate = this.readCount(reader);
                        coordProp.setValue(coordinate);
                    }
                    else if (localName.equals("Quantity"))
                    {
                        Quantity coordinate = this.readQuantity(reader);
                        coordProp.setValue(coordinate);
                    }
                    else if (localName.equals("Time"))
                    {
                        Time coordinate = this.readTime(reader);
                        coordProp.setValue(coordinate);
                    }
                    else
                        throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
                    
                    reader.nextTag(); // end property tag
                }
                
                bean.getCoordinateList().add(coordProp);                
                reader.nextTag();
            }
        }
        while (found);
    }
    
    
    /**
     * Write method for VectorType complex type
     * @param writeInlineValues 
     */
    public void writeVectorType(XMLStreamWriter writer, Vector bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeVectorTypeAttributes(writer, bean);
        this.writeVectorTypeElements(writer, bean, writeInlineValues);
    }
    
    
    /**
     * Writes attributes of VectorType complex type
     */
    public void writeVectorTypeAttributes(XMLStreamWriter writer, Vector bean) throws XMLStreamException
    {
        this.writeAbstractDataComponentTypeAttributes(writer, bean);
        
        // referenceFrame
        writer.writeAttribute("referenceFrame", getStringValue(bean.getReferenceFrame()));
        
        // localFrame
        if (bean.isSetLocalFrame())
            writer.writeAttribute("localFrame", getStringValue(bean.getLocalFrame()));
    }
    
    
    /**
     * Writes elements of VectorType complex type
     */
    public void writeVectorTypeElements(XMLStreamWriter writer, Vector bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeAbstractDataComponentTypeElements(writer, bean);
        int numItems;
        
        // coordinate
        numItems = bean.getCoordinateList().size();
        for (int i = 0; i < numItems; i++)
        {
            OgcProperty<ScalarComponent> item = bean.getCoordinateList().getProperty(i);
            writer.writeStartElement(NS_URI, "coordinate");
            writePropertyAttributes(writer, item);
            
            if (item.hasValue() && !item.hasHref())
            {
                if (item.getValue() instanceof Count)
                    this.writeCount(writer, (Count)item.getValue(), writeInlineValues);
                else if (item.getValue() instanceof Quantity)
                    this.writeQuantity(writer, (Quantity)item.getValue(), writeInlineValues);
                else if (item.getValue() instanceof Time)
                    this.writeTime(writer, (Time)item.getValue(), writeInlineValues);
            }
            
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for DataArrayType complex type
     */
    public DataArray readDataArrayType(XMLStreamReader reader) throws XMLStreamException
    {
        DataArray bean = factory.newDataArray();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readDataArrayTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readDataArrayTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of DataArrayType complex type
     */
    public void readDataArrayTypeAttributes(Map<String, String> attrMap, DataArray bean) throws XMLStreamException
    {
        this.readAbstractDataComponentTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of DataArrayType complex type
     */
    public void readDataArrayTypeElements(XMLStreamReader reader, DataArray bean) throws XMLStreamException
    {
        this.readAbstractDataComponentTypeElements(reader, bean);
        
        boolean found;
        
        // elementCount
        found = checkElementName(reader, "elementCount");
        if (found)
        {
            OgcProperty<Count> elementCountProp = bean.getElementCountProperty();
            readPropertyAttributes(reader, elementCountProp);
            
            reader.nextTag();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                elementCountProp.setValue(this.readCount(reader));
                reader.nextTag(); // end property tag
            }
            
            reader.nextTag();
        }
        
        // elementType
        found = checkElementName(reader, "elementType");
        if (found)
        {
            OgcProperty<DataComponent> elementTypeProp = bean.getElementTypeProperty();
            readPropertyAttributes(reader, elementTypeProp);
            
            reader.nextTag();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                elementTypeProp.setValue(this.readDataComponent(reader));
                reader.nextTag(); // end property tag
            }
            
            reader.nextTag();
        }
        
        // encoding
        found = checkElementName(reader, "encoding");
        if (found)
        {
            reader.nextTag();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                bean.setEncoding(this.readAbstractEncoding(reader));               
                reader.nextTag(); // end property tag
            }
                
            reader.nextTag();
        }
        
        // values
        found = checkElementName(reader, "values");
        if (found)
        {
            EncodedValues values = this.readEncodedValuesPropertyType(reader, bean, bean.getEncoding());
            bean.setValues(values);            
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for DataArrayType complex type
     * @param writeInlineValues 
     */
    public void writeDataArrayType(XMLStreamWriter writer, DataArray bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeDataArrayTypeAttributes(writer, bean);
        this.writeDataArrayTypeElements(writer, bean, writeInlineValues);
    }
    
    
    /**
     * Writes attributes of DataArrayType complex type
     */
    public void writeDataArrayTypeAttributes(XMLStreamWriter writer, DataArray bean) throws XMLStreamException
    {
        this.writeAbstractDataComponentTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of DataArrayType complex type
     */
    public void writeDataArrayTypeElements(XMLStreamWriter writer, DataArray bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeAbstractDataComponentTypeElements(writer, bean);
                
        // elementCount
        writer.writeStartElement(NS_URI, "elementCount");
        OgcProperty<Count> elementCountProp = bean.getElementCountProperty();
        writePropertyAttributes(writer, elementCountProp);
        if (elementCountProp.hasValue() && !elementCountProp.hasHref())
            this.writeCount(writer, bean.getElementCount(), true);
        writer.writeEndElement();
        
        // elementType
        writer.writeStartElement(NS_URI, "elementType");
        OgcProperty<DataComponent> elementTypeProp = bean.getElementTypeProperty();
        writePropertyAttributes(writer, elementTypeProp);
        if (elementTypeProp.hasValue() && !elementTypeProp.hasHref())
            this.writeDataComponent(writer, bean.getElementType(), false);
        writer.writeEndElement();
        
        if (writeInlineValues)
        {
            // encoding
            if (bean.isSetEncoding())
            {
                writer.writeStartElement(NS_URI, "encoding");
                this.writeAbstractEncoding(writer, bean.getEncoding());
                writer.writeEndElement();
                
                // values
                if (bean.isSetValues())
                {
                    writer.writeStartElement(NS_URI, "values");
                    this.writeEncodedValuesPropertyType(writer, bean, bean.getEncoding(), bean.getValues());
                    writer.writeEndElement();
                }
            }
        }
    }
    
    
    /**
     * Read method for MatrixType complex type
     */
    public Matrix readMatrixType(XMLStreamReader reader) throws XMLStreamException
    {
        Matrix bean = factory.newMatrix();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readMatrixTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readMatrixTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of MatrixType complex type
     */
    public void readMatrixTypeAttributes(Map<String, String> attrMap, Matrix bean) throws XMLStreamException
    {
        this.readDataArrayTypeAttributes(attrMap, bean);
        
        String val;
        
        // referenceframe
        val = attrMap.get("referenceFrame");
        if (val != null)
            bean.setReferenceFrame(val);
        
        // localframe
        val = attrMap.get("localFrame");
        if (val != null)
            bean.setLocalFrame(val);
    }
    
    
    /**
     * Reads elements of MatrixType complex type
     */
    public void readMatrixTypeElements(XMLStreamReader reader, Matrix bean) throws XMLStreamException
    {
        this.readDataArrayTypeElements(reader, bean);
        
    }
    
    
    /**
     * Write method for MatrixType complex type
     * @param writeInlineValues 
     */
    public void writeMatrixType(XMLStreamWriter writer, Matrix bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeMatrixTypeAttributes(writer, bean);
        this.writeMatrixTypeElements(writer, bean, writeInlineValues);
    }
    
    
    /**
     * Writes attributes of MatrixType complex type
     */
    public void writeMatrixTypeAttributes(XMLStreamWriter writer, Matrix bean) throws XMLStreamException
    {
        this.writeDataArrayTypeAttributes(writer, bean);
        
        // referenceFrame
        if (bean.isSetReferenceFrame())
            writer.writeAttribute("referenceFrame", getStringValue(bean.getReferenceFrame()));
        
        // localFrame
        if (bean.isSetLocalFrame())
            writer.writeAttribute("localFrame", getStringValue(bean.getLocalFrame()));
    }
    
    
    /**
     * Writes elements of MatrixType complex type
     * @param writeInlineValues 
     */
    public void writeMatrixTypeElements(XMLStreamWriter writer, Matrix bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeDataArrayTypeElements(writer, bean, writeInlineValues);
    }
    
    
    /**
     * Read method for DataStreamType complex type
     */
    public DataStream readDataStreamType(XMLStreamReader reader) throws XMLStreamException
    {
        DataStream bean = factory.newDataStream();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readDataStreamTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readDataStreamTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of DataStreamType complex type
     */
    public void readDataStreamTypeAttributes(Map<String, String> attrMap, DataStream bean) throws XMLStreamException
    {
        this.readAbstractSWEIdentifiableTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of DataStreamType complex type
     */
    public void readDataStreamTypeElements(XMLStreamReader reader, DataStream bean) throws XMLStreamException
    {
        this.readAbstractSWEIdentifiableTypeElements(reader, bean);
        
        boolean found;
        
        // elementCount
        found = checkElementName(reader, "elementCount");
        if (found)
        {
            reader.nextTag();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                bean.setElementCount(this.readCount(reader));                  
                reader.nextTag(); // end property tag
            }
            
            reader.nextTag();
        }
        
        // elementType
        found = checkElementName(reader, "elementType");
        if (found)
        {
            OgcProperty<DataComponent> elementTypeProp = bean.getElementTypeProperty();
            readPropertyAttributes(reader, elementTypeProp);
            
            reader.nextTag();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                elementTypeProp.setValue(this.readDataComponent(reader));
                reader.nextTag(); // end property tag
            }
            
            reader.nextTag();
        }
        
        // encoding
        found = checkElementName(reader, "encoding");
        if (found)
        {
            reader.nextTag();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                bean.setEncoding(this.readAbstractEncoding(reader));                  
                reader.nextTag(); // end property tag
            }
            
            reader.nextTag();
        }
        
        // values
        found = checkElementName(reader, "values");
        if (found)
        {
            EncodedValues values = this.readEncodedValuesPropertyType(reader, bean, bean.getEncoding());
            bean.setValues(values);            
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for DataStreamType complex type
     */
    public void writeDataStreamType(XMLStreamWriter writer, DataStream bean) throws XMLStreamException
    {
        this.writeDataStreamTypeAttributes(writer, bean);
        this.writeDataStreamTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of DataStreamType complex type
     */
    public void writeDataStreamTypeAttributes(XMLStreamWriter writer, DataStream bean) throws XMLStreamException
    {
        this.writeAbstractSWEIdentifiableTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of DataStreamType complex type
     */
    public void writeDataStreamTypeElements(XMLStreamWriter writer, DataStream bean) throws XMLStreamException
    {
        this.writeAbstractSWEIdentifiableTypeElements(writer, bean);
        
        // elementCount
        if (bean.isSetElementCount())
        {
            writer.writeStartElement(NS_URI, "elementCount");
            this.writeCount(writer, bean.getElementCount(), true);
            writer.writeEndElement();
        }
        
        // elementType
        writer.writeStartElement(NS_URI, "elementType");
        OgcProperty<DataComponent> elementTypeProp = bean.getElementTypeProperty();
        writePropertyAttributes(writer, elementTypeProp);
        if (elementTypeProp.hasValue() && !elementTypeProp.hasHref())
            this.writeDataComponent(writer, bean.getElementType(), false);
        writer.writeEndElement();
        
        // encoding
        writer.writeStartElement(NS_URI, "encoding");
        this.writeAbstractEncoding(writer, bean.getEncoding());
        writer.writeEndElement();
        
        // values
        writer.writeStartElement(NS_URI, "values");
        if (bean.isSetValues())
            this.writeEncodedValuesPropertyType(writer, bean, bean.getEncoding(), bean.getValues());
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for BlockType complex type
     */
    public BinaryBlock readBlockType(XMLStreamReader reader) throws XMLStreamException
    {
        BinaryBlock bean = factory.newBinaryBlock();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readBlockTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readBlockTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of BlockType complex type
     */
    public void readBlockTypeAttributes(Map<String, String> attrMap, BinaryBlock bean) throws XMLStreamException
    {
        this.readAbstractSWETypeAttributes(attrMap, bean);
        
        String val;
        
        // compression
        val = attrMap.get("compression");
        if (val != null)
            bean.setCompression(val);
        
        // encryption
        val = attrMap.get("encryption");
        if (val != null)
            bean.setEncryption(val);
        
        // paddingbytesafter
        val = attrMap.get("paddingBytes-after");
        if (val != null)
            bean.setPaddingBytesAfter(getIntFromString(val));
        
        // paddingbytesbefore
        val = attrMap.get("paddingBytes-before");
        if (val != null)
            bean.setPaddingBytesBefore(getIntFromString(val));
        
        // bytelength
        val = attrMap.get("byteLength");
        if (val != null)
            bean.setByteLength(getIntFromString(val));
        
        // ref
        val = attrMap.get("ref");
        if (val != null)
            bean.setRef(val);
    }
    
    
    /**
     * Reads elements of BlockType complex type
     */
    public void readBlockTypeElements(XMLStreamReader reader, BinaryBlock bean) throws XMLStreamException
    {
        this.readAbstractSWETypeElements(reader, bean);
        
    }
    
    
    /**
     * Write method for BlockType complex type
     */
    public void writeBlockType(XMLStreamWriter writer, BinaryBlock bean) throws XMLStreamException
    {
        this.writeBlockTypeAttributes(writer, bean);
        this.writeBlockTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of BlockType complex type
     */
    public void writeBlockTypeAttributes(XMLStreamWriter writer, BinaryBlock bean) throws XMLStreamException
    {
        this.writeAbstractSWETypeAttributes(writer, bean);
        
        // compression
        if (bean.isSetCompression())
            writer.writeAttribute("compression", getStringValue(bean.getCompression()));
        
        // encryption
        if (bean.isSetEncryption())
            writer.writeAttribute("encryption", getStringValue(bean.getEncryption()));
        
        // paddingBytes-after
        if (bean.isSetPaddingBytesAfter())
            writer.writeAttribute("paddingBytes-after", getStringValue(bean.getPaddingBytesAfter()));
        
        // paddingBytes-before
        if (bean.isSetPaddingBytesBefore())
            writer.writeAttribute("paddingBytes-before", getStringValue(bean.getPaddingBytesBefore()));
        
        // byteLength
        if (bean.isSetByteLength())
            writer.writeAttribute("byteLength", getStringValue(bean.getByteLength()));
        
        // ref
        writer.writeAttribute("ref", getStringValue(bean.getRef()));
    }
    
    
    /**
     * Writes elements of BlockType complex type
     */
    public void writeBlockTypeElements(XMLStreamWriter writer, BinaryBlock bean) throws XMLStreamException
    {
        this.writeAbstractSWETypeElements(writer, bean);
    }
    
    
    /**
     * Read method for BinaryEncodingType complex type
     */
    public BinaryEncoding readBinaryEncodingType(XMLStreamReader reader) throws XMLStreamException
    {
        BinaryEncoding bean = factory.newBinaryEncoding();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readBinaryEncodingTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readBinaryEncodingTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of BinaryEncodingType complex type
     */
    public void readBinaryEncodingTypeAttributes(Map<String, String> attrMap, BinaryEncoding bean) throws XMLStreamException
    {
        this.readAbstractEncodingTypeAttributes(attrMap, bean);
        
        String val;
        
        // byteorder
        val = attrMap.get("byteOrder");
        if (val != null)
            bean.setByteOrder(ByteOrder.fromString(val));

        // byteencoding
        val = attrMap.get("byteEncoding");
        if (val != null)
            bean.setByteEncoding(ByteEncoding.fromString(val));
        
        // bytelength
        val = attrMap.get("byteLength");
        if (val != null)
            bean.setByteLength(getIntFromString(val));
    }
    
    
    /**
     * Reads elements of BinaryEncodingType complex type
     */
    public void readBinaryEncodingTypeElements(XMLStreamReader reader, BinaryEncoding bean) throws XMLStreamException
    {
        this.readAbstractEncodingTypeElements(reader, bean);
        
        boolean found;
        
        // member
        do
        {
            found = checkElementName(reader, "member");
            if (found)
            {
                reader.nextTag();
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
                {
                    String localName = reader.getName().getLocalPart();
                    
                    if (localName.equals("Component"))
                    {
                        BinaryComponent member = this.readComponent(reader);
                        bean.addMemberAsComponent(member);
                    }
                    else if (localName.equals("Block"))
                    {
                        BinaryBlock member = this.readBlock(reader);
                        bean.addMemberAsBlock(member);
                    }
                    else
                        throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
                    
                    reader.nextTag(); // end property tag
                }

                reader.nextTag();
            }
        }
        while (found);
    }
    
    
    /**
     * Write method for BinaryEncodingType complex type
     */
    public void writeBinaryEncodingType(XMLStreamWriter writer, BinaryEncoding bean) throws XMLStreamException
    {
        this.writeBinaryEncodingTypeAttributes(writer, bean);
        this.writeBinaryEncodingTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of BinaryEncodingType complex type
     */
    public void writeBinaryEncodingTypeAttributes(XMLStreamWriter writer, BinaryEncoding bean) throws XMLStreamException
    {
        this.writeAbstractEncodingTypeAttributes(writer, bean);
        
        // byteOrder
        String byteOrderString = null;
        if (bean.getByteOrder() == ByteOrder.BIG_ENDIAN)
            byteOrderString = "bigEndian";
        else if (bean.getByteOrder() == ByteOrder.LITTLE_ENDIAN)
            byteOrderString = "littleEndian";
        if (byteOrderString != null)
            writer.writeAttribute("byteOrder", byteOrderString);
        
        // byteEncoding
        writer.writeAttribute("byteEncoding", getStringValue(bean.getByteEncoding()));
        
        // byteLength
        if (bean.isSetByteLength())
            writer.writeAttribute("byteLength", getStringValue(bean.getByteLength()));
    }
    
    
    /**
     * Writes elements of BinaryEncodingType complex type
     */
    public void writeBinaryEncodingTypeElements(XMLStreamWriter writer, BinaryEncoding bean) throws XMLStreamException
    {
        this.writeAbstractEncodingTypeElements(writer, bean);
        int numItems;
        
        // member
        numItems = bean.getMemberList().size();
        for (int i = 0; i < numItems; i++)
        {
            BinaryMember item = bean.getMemberList().get(i);
            writer.writeStartElement(NS_URI, "member");
            
            if (item instanceof BinaryComponent)
                this.writeComponent(writer, (BinaryComponent)item);
            else if (item instanceof BinaryBlock)
                this.writeBlock(writer, (BinaryBlock)item);
            
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for ComponentType complex type
     */
    public BinaryComponent readComponentType(XMLStreamReader reader) throws XMLStreamException
    {
        BinaryComponent bean = factory.newBinaryComponent();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readComponentTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readComponentTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of ComponentType complex type
     */
    public void readComponentTypeAttributes(Map<String, String> attrMap, BinaryComponent bean) throws XMLStreamException
    {
        this.readAbstractSWETypeAttributes(attrMap, bean);
        
        String val;
        
        // encryption
        val = attrMap.get("encryption");
        if (val != null)
            bean.setEncryption(val);
        
        // significantbits
        val = attrMap.get("significantBits");
        if (val != null)
            bean.setSignificantBits(getIntFromString(val));
        
        // bitlength
        val = attrMap.get("bitLength");
        if (val != null)
            bean.setBitLength(getIntFromString(val));
        
        // bytelength
        val = attrMap.get("byteLength");
        if (val != null)
            bean.setByteLength(getIntFromString(val));
        
        // datatype
        val = attrMap.get("dataType");
        if (val != null)
            bean.setDataType(val);
        
        // ref
        val = attrMap.get("ref");
        if (val != null)
            bean.setRef(val);
    }
    
    
    /**
     * Reads elements of ComponentType complex type
     */
    public void readComponentTypeElements(XMLStreamReader reader, BinaryComponent bean) throws XMLStreamException
    {
        this.readAbstractSWETypeElements(reader, bean);
        
    }
    
    
    /**
     * Write method for ComponentType complex type
     */
    public void writeComponentType(XMLStreamWriter writer, BinaryComponent bean) throws XMLStreamException
    {
        this.writeComponentTypeAttributes(writer, bean);
        this.writeComponentTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of ComponentType complex type
     */
    public void writeComponentTypeAttributes(XMLStreamWriter writer, BinaryComponent bean) throws XMLStreamException
    {
        this.writeAbstractSWETypeAttributes(writer, bean);
        
        // encryption
        if (bean.isSetEncryption())
            writer.writeAttribute("encryption", getStringValue(bean.getEncryption()));
        
        // significantBits
        if (bean.isSetSignificantBits())
            writer.writeAttribute("significantBits", getStringValue(bean.getSignificantBits()));
        
        // bitLength
        if (bean.isSetBitLength())
            writer.writeAttribute("bitLength", getStringValue(bean.getBitLength()));
        
        // byteLength
        if (bean.isSetByteLength())
            writer.writeAttribute("byteLength", getStringValue(bean.getByteLength()));
        
        // dataType
        writer.writeAttribute("dataType", getStringValue(bean.getDataType()));
        
        // ref
        writer.writeAttribute("ref", getStringValue(bean.getRef()));
    }
    
    
    /**
     * Writes elements of ComponentType complex type
     */
    public void writeComponentTypeElements(XMLStreamWriter writer, BinaryComponent bean) throws XMLStreamException
    {
        this.writeAbstractSWETypeElements(writer, bean);
    }
    
    
    /**
     * Read method for DataChoiceType complex type
     */
    public DataChoice readDataChoiceType(XMLStreamReader reader) throws XMLStreamException
    {
        DataChoice bean = factory.newDataChoice();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readDataChoiceTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readDataChoiceTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of DataChoiceType complex type
     */
    public void readDataChoiceTypeAttributes(Map<String, String> attrMap, DataChoice bean) throws XMLStreamException
    {
        this.readAbstractDataComponentTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of DataChoiceType complex type
     */
    public void readDataChoiceTypeElements(XMLStreamReader reader, DataChoice bean) throws XMLStreamException
    {
        this.readAbstractDataComponentTypeElements(reader, bean);
        
        boolean found;
        
        // choiceValue
        found = checkElementName(reader, "choiceValue");
        if (found)
        {
            reader.nextTag();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                bean.setChoiceValue(this.readCategory(reader));
                reader.nextTag(); // end property tag 
            }
            
            reader.nextTag();
        }
        
        // item
        do
        {
            found = checkElementName(reader, "item");
            if (found)
            {
                OgcProperty<DataComponent> itemProp = new OgcPropertyImpl<DataComponent>();
                readPropertyAttributes(reader, itemProp);
                
                reader.nextTag();
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
                {
                    itemProp.setValue(this.readDataComponent(reader));
                    reader.nextTag(); // end property tag 
                }                
                
                bean.getItemList().add(itemProp);
                reader.nextTag();
            }
        }
        while (found);
    }
    
    
    /**
     * Write method for DataChoiceType complex type
     */
    public void writeDataChoiceType(XMLStreamWriter writer, DataChoice bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeDataChoiceTypeAttributes(writer, bean);
        this.writeDataChoiceTypeElements(writer, bean, writeInlineValues);
    }
    
    
    /**
     * Writes attributes of DataChoiceType complex type
     */
    public void writeDataChoiceTypeAttributes(XMLStreamWriter writer, DataChoice bean) throws XMLStreamException
    {
        this.writeAbstractDataComponentTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of DataChoiceType complex type
     */
    public void writeDataChoiceTypeElements(XMLStreamWriter writer, DataChoice bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeAbstractDataComponentTypeElements(writer, bean);
        int numItems;
        
        // choiceValue
        if (bean.isSetChoiceValue())
        {
            writer.writeStartElement(NS_URI, "choiceValue");
            this.writeCategory(writer, bean.getChoiceValue(), false);
            writer.writeEndElement();
        }
        
        // item
        numItems = bean.getItemList().size();
        for (int i = 0; i < numItems; i++)
        {
            OgcProperty<DataComponent> item = bean.getItemList().getProperty(i);
            writer.writeStartElement(NS_URI, "item");
            writePropertyAttributes(writer, item);
            if (item.hasValue() && !item.hasHref())
                this.writeDataComponent(writer, item.getValue(), writeInlineValues);
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for CountType complex type
     */
    public Count readCountType(XMLStreamReader reader) throws XMLStreamException
    {
        Count bean = factory.newCount();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readCountTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readCountTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of CountType complex type
     */
    public void readCountTypeAttributes(Map<String, String> attrMap, Count bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of CountType complex type
     */
    public void readCountTypeElements(XMLStreamReader reader, Count bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeElements(reader, bean);
        
        boolean found;
        String val;
        
        // constraint
        found = checkElementName(reader, "constraint");
        if (found)
        {
            OgcProperty<AllowedValues> constraintProp = bean.getConstraintProperty();
            readPropertyAttributes(reader, constraintProp);
            
            reader.nextTag();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                constraintProp.setValue(this.readAllowedValues(reader));
                reader.nextTag(); // end property tag
            }
            
            reader.nextTag();
        }
        
        // value
        found = checkElementName(reader, "value");
        if (found)
        {
            val = reader.getElementText();
            if (val != null)
                bean.setValue(getIntFromString(val));
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for CountType complex type
     * @param writeInlineValue 
     */
    public void writeCountType(XMLStreamWriter writer, Count bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeCountTypeAttributes(writer, bean);
        this.writeCountTypeElements(writer, bean, writeInlineValues);
    }
    
    
    /**
     * Writes attributes of CountType complex type
     */
    public void writeCountTypeAttributes(XMLStreamWriter writer, Count bean) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of CountType complex type
     */
    public void writeCountTypeElements(XMLStreamWriter writer, Count bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeElements(writer, bean);
        
        // constraint
        if (bean.isSetConstraint())
        {
            writer.writeStartElement(NS_URI, "constraint");
            OgcProperty<AllowedValues> constraintProp = bean.getConstraintProperty();
            writePropertyAttributes(writer, constraintProp);
            if (constraintProp.hasValue() && !constraintProp.hasHref())
                this.writeAllowedValues(writer, bean.getConstraint(), true);
            writer.writeEndElement();
        }
        
        // value
        if (bean.isSetValue() && writeInlineValues)
        {
            writer.writeStartElement(NS_URI, "value");
            writer.writeCharacters(getStringValue(bean.getValue()));
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for CategoryRangeType complex type
     */
    public CategoryRange readCategoryRangeType(XMLStreamReader reader) throws XMLStreamException
    {
        CategoryRange bean = factory.newCategoryRange();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readCategoryRangeTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readCategoryRangeTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of CategoryRangeType complex type
     */
    public void readCategoryRangeTypeAttributes(Map<String, String> attrMap, CategoryRange bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of CategoryRangeType complex type
     */
    public void readCategoryRangeTypeElements(XMLStreamReader reader, CategoryRange bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeElements(reader, bean);
        
        boolean found;
        String val;
        
        // codeSpace
        found = checkElementName(reader, "codeSpace");
        if (found)
        {
            Map<String, String> attrMap = collectAttributes(reader);
            bean.setCodeSpace(attrMap.get("href"));            
            reader.nextTag(); // end property tag
            reader.nextTag();
        }
        
        // constraint
        found = checkElementName(reader, "constraint");
        if (found)
        {
            OgcProperty<AllowedTokens> constraintProp = bean.getConstraintProperty();
            readPropertyAttributes(reader, constraintProp);
            
            reader.nextTag();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                constraintProp.setValue(this.readAllowedTokens(reader));
                reader.nextTag(); // end property tag
            }
            
            reader.nextTag();
        }
        
        // value
        found = checkElementName(reader, "value");
        if (found)
        {
            val = reader.getElementText();
            if (val != null)
                bean.setValue(getStringArrayFromString(val));
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for CategoryRangeType complex type
     * @param writeInlineValues 
     */
    public void writeCategoryRangeType(XMLStreamWriter writer, CategoryRange bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeCategoryRangeTypeAttributes(writer, bean);
        this.writeCategoryRangeTypeElements(writer, bean, writeInlineValues);
    }
    
    
    /**
     * Writes attributes of CategoryRangeType complex type
     */
    public void writeCategoryRangeTypeAttributes(XMLStreamWriter writer, CategoryRange bean) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of CategoryRangeType complex type
     * @param writeInlineValues 
     */
    public void writeCategoryRangeTypeElements(XMLStreamWriter writer, CategoryRange bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeElements(writer, bean);
        
        // codeSpace
        if (bean.isSetCodeSpace())
        {
            writer.writeStartElement(NS_URI, "codeSpace");
            writer.writeAttribute(XLINK_NS_URI, "href", bean.getCodeSpace());
            writer.writeEndElement();
        }
        
        // constraint
        if (bean.isSetConstraint())
        {
            writer.writeStartElement(NS_URI, "constraint");
            OgcProperty<AllowedTokens> constraintProp = bean.getConstraintProperty();
            writePropertyAttributes(writer, constraintProp);
            if (constraintProp.hasValue() && !constraintProp.hasHref())
                this.writeAllowedTokens(writer, bean.getConstraint());
            writer.writeEndElement();
        }
        
        // value
        if (bean.isSetValue() && writeInlineValues)
        {
            writer.writeStartElement(NS_URI, "value");
            writer.writeCharacters(getStringValue(bean.getValue()));
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Reads attributes of AbstractSimpleComponentType complex type
     */
    public void readAbstractSimpleComponentTypeAttributes(Map<String, String> attrMap, SimpleComponent bean) throws XMLStreamException
    {
        this.readAbstractDataComponentTypeAttributes(attrMap, bean);
        
        String val;
        
        // referenceframe
        val = attrMap.get("referenceFrame");
        if (val != null)
            bean.setReferenceFrame(val);
        
        // axisid
        val = attrMap.get("axisID");
        if (val != null)
            bean.setAxisID(val);
    }
    
    
    /**
     * Reads elements of AbstractSimpleComponentType complex type
     */
    public void readAbstractSimpleComponentTypeElements(XMLStreamReader reader, SimpleComponent bean) throws XMLStreamException
    {
        this.readAbstractDataComponentTypeElements(reader, bean);
        
        boolean found;
        
        // quality
        do
        {
            found = checkElementName(reader, "quality");
            if (found)
            {
                OgcProperty<SimpleComponent> qualityProp = new OgcPropertyImpl<SimpleComponent>();
                readPropertyAttributes(reader, qualityProp);
                
                reader.nextTag();
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
                {
                    String localName = reader.getName().getLocalPart();
                    
                    if (localName.equals("Quantity"))
                    {
                        Quantity quality = this.readQuantity(reader);
                        qualityProp.setValue(quality);
                    }
                    else if (localName.equals("QuantityRange"))
                    {
                        QuantityRange quality = this.readQuantityRange(reader);
                        qualityProp.setValue(quality);
                    }
                    else if (localName.equals("Category"))
                    {
                        Category quality = this.readCategory(reader);
                        qualityProp.setValue(quality);
                    }
                    else if (localName.equals("Text"))
                    {
                        Text quality = this.readText(reader);
                        qualityProp.setValue(quality);
                    }
                    else
                        throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
                    
                    reader.nextTag(); // end property tag
                }  
                
                bean.getQualityList().add(qualityProp);
                reader.nextTag();
            }
        }
        while (found);
        
        // nilValues
        found = checkElementName(reader, "nilValues");
        if (found)
        {
            OgcProperty<NilValues> nilValuesProp = bean.getNilValuesProperty();
            readPropertyAttributes(reader, nilValuesProp);
            
            reader.nextTag();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                nilValuesProp.setValue(this.readNilValues(reader));
                reader.nextTag(); // end property tag
            }
            
            reader.nextTag();
        }
    }
    
    
    /**
     * Writes attributes of AbstractSimpleComponentType complex type
     */
    public void writeAbstractSimpleComponentTypeAttributes(XMLStreamWriter writer, SimpleComponent bean) throws XMLStreamException
    {
        this.writeAbstractDataComponentTypeAttributes(writer, bean);
        
        // referenceFrame
        if (bean.isSetReferenceFrame())
            writer.writeAttribute("referenceFrame", getStringValue(bean.getReferenceFrame()));
        
        // axisID
        if (bean.isSetAxisID())
            writer.writeAttribute("axisID", getStringValue(bean.getAxisID()));
    }
    
    
    /**
     * Writes elements of AbstractSimpleComponentType complex type
     */
    public void writeAbstractSimpleComponentTypeElements(XMLStreamWriter writer, SimpleComponent bean) throws XMLStreamException
    {
        this.writeAbstractDataComponentTypeElements(writer, bean);
        int numItems;
        
        // quality
        numItems = bean.getQualityList().size();
        for (int i = 0; i < numItems; i++)
        {
            OgcProperty<SimpleComponent> item = bean.getQualityList().getProperty(i);
            writer.writeStartElement(NS_URI, "quality");
            writePropertyAttributes(writer, item);
            
            if (item.hasValue() && !item.hasHref())
            {
                if (item.getValue() instanceof Quantity)
                    this.writeQuantity(writer, (Quantity)item.getValue(), true);
                else if (item.getValue() instanceof QuantityRange)
                    this.writeQuantityRange(writer, (QuantityRange)item.getValue(), true);
                else if (item.getValue() instanceof Category)
                    this.writeCategory(writer, (Category)item.getValue(), true);
                else if (item.getValue() instanceof Text)
                    this.writeText(writer, (Text)item.getValue(), true);
            }
            
            writer.writeEndElement();
        }
        
        // nilValues
        if (bean.isSetNilValues())
        {
            writer.writeStartElement(NS_URI, "nilValues");
            OgcProperty<NilValues> nilValuesProp = bean.getNilValuesProperty();
            writePropertyAttributes(writer, nilValuesProp);
            if (nilValuesProp.hasValue() && !nilValuesProp.hasHref())
                this.writeNilValues(writer, bean.getNilValues());
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for QuantityRangeType complex type
     */
    public QuantityRange readQuantityRangeType(XMLStreamReader reader) throws XMLStreamException
    {
        QuantityRange bean = factory.newQuantityRange();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readQuantityRangeTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readQuantityRangeTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of QuantityRangeType complex type
     */
    public void readQuantityRangeTypeAttributes(Map<String, String> attrMap, QuantityRange bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of QuantityRangeType complex type
     */
    public void readQuantityRangeTypeElements(XMLStreamReader reader, QuantityRange bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeElements(reader, bean);
        
        boolean found;
        String val;
        
        // uom
        found = checkElementName(reader, "uom");
        if (found)
        {
            bean.setUom(this.readUnitReference(reader));            
            reader.nextTag(); // end property tag
            reader.nextTag();
        }
        
        // constraint
        found = checkElementName(reader, "constraint");
        if (found)
        {
            OgcProperty<AllowedValues> constraintProp = bean.getConstraintProperty();
            readPropertyAttributes(reader, constraintProp);
            
            reader.nextTag();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                constraintProp.setValue(this.readAllowedValues(reader));
                reader.nextTag(); // end property tag
            }
            
            reader.nextTag();
        }
        
        // value
        found = checkElementName(reader, "value");
        if (found)
        {
            val = reader.getElementText();
            if (val != null)
                bean.setValue(getDoubleArrayFromString(val));
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for QuantityRangeType complex type
     * @param writeInlineValues 
     */
    public void writeQuantityRangeType(XMLStreamWriter writer, QuantityRange bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeQuantityRangeTypeAttributes(writer, bean);
        this.writeQuantityRangeTypeElements(writer, bean, writeInlineValues);
    }
    
    
    /**
     * Writes attributes of QuantityRangeType complex type
     */
    public void writeQuantityRangeTypeAttributes(XMLStreamWriter writer, QuantityRange bean) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of QuantityRangeType complex type
     * @param writeInlineValues 
     */
    public void writeQuantityRangeTypeElements(XMLStreamWriter writer, QuantityRange bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeElements(writer, bean);
        
        // uom
        writer.writeStartElement(NS_URI, "uom");
        this.writeUnitReference(writer, bean.getUom());
        writer.writeEndElement();
        
        // constraint
        if (bean.isSetConstraint())
        {
            writer.writeStartElement(NS_URI, "constraint");
            OgcProperty<AllowedValues> constraintProp = bean.getConstraintProperty();
            writePropertyAttributes(writer, constraintProp);
            if (constraintProp.hasValue() && !constraintProp.hasHref())
                this.writeAllowedValues(writer, bean.getConstraint(), false);
            writer.writeEndElement();
        }
        
        // value
        if (bean.isSetValue() && writeInlineValues)
        {
            writer.writeStartElement(NS_URI, "value");
            writer.writeCharacters(getStringValue(bean.getValue()));
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for TimeType complex type
     */
    public Time readTimeType(XMLStreamReader reader) throws XMLStreamException
    {
        Time bean = factory.newTime();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readTimeTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readTimeTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of TimeType complex type
     */
    public void readTimeTypeAttributes(Map<String, String> attrMap, Time bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeAttributes(attrMap, bean);
        
        String val;
        
        // referencetime
        val = attrMap.get("referenceTime");
        if (val != null)
            bean.setReferenceTime(getDateTimeFromString(val));
        
        // localframe
        val = attrMap.get("localFrame");
        if (val != null)
            bean.setLocalFrame(val);
    }
    
    
    /**
     * Reads elements of TimeType complex type
     */
    public void readTimeTypeElements(XMLStreamReader reader, Time bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeElements(reader, bean);
        
        boolean found;
        String val;
        
        // uom
        found = checkElementName(reader, "uom");
        if (found)
        {
            bean.setUom(this.readUnitReference(reader));            
            reader.nextTag(); // end property tag
            reader.nextTag();
        }
        
        // constraint
        found = checkElementName(reader, "constraint");
        if (found)
        {
            OgcProperty<AllowedTimes> constraintProp = bean.getConstraintProperty();
            readPropertyAttributes(reader, constraintProp);
            
            reader.nextTag();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                constraintProp.setValue(this.readAllowedTimes(reader));
                reader.nextTag(); // end property tag
            }
            
            reader.nextTag();
        }
        
        // value
        found = checkElementName(reader, "value");
        if (found)
        {
            val = reader.getElementText();
            if (val != null)
                bean.setValue(getDateTimeFromString(val));            
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for TimeType complex type
     * @param writeInlineValues 
     */
    public void writeTimeType(XMLStreamWriter writer, Time bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeTimeTypeAttributes(writer, bean);
        this.writeTimeTypeElements(writer, bean, writeInlineValues);
    }
    
    
    /**
     * Writes attributes of TimeType complex type
     */
    public void writeTimeTypeAttributes(XMLStreamWriter writer, Time bean) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeAttributes(writer, bean);
        
        // referenceTime
        if (bean.isSetReferenceTime())
            writer.writeAttribute("referenceTime", getStringValue(bean.getReferenceTime()));
        
        // localFrame
        if (bean.isSetLocalFrame())
            writer.writeAttribute("localFrame", getStringValue(bean.getLocalFrame()));
    }
    
    
    /**
     * Writes elements of TimeType complex type
     * @param writeInlineValues 
     */
    public void writeTimeTypeElements(XMLStreamWriter writer, Time bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeElements(writer, bean);
        
        // uom
        writer.writeStartElement(NS_URI, "uom");
        this.writeUnitReference(writer, bean.getUom());
        writer.writeEndElement();
        
        // constraint
        if (bean.isSetConstraint())
        {
            writer.writeStartElement(NS_URI, "constraint");
            OgcProperty<AllowedTimes> constraintProp = bean.getConstraintProperty();
            writePropertyAttributes(writer, constraintProp);
            if (constraintProp.hasValue() && !constraintProp.hasHref())
                this.writeAllowedTimes(writer, bean.getConstraint());
            writer.writeEndElement();
        }
        
        // value
        if (bean.isSetValue() && writeInlineValues)
        {
            writer.writeStartElement(NS_URI, "value");
            String timeString;
            if (bean.getUom().isSetCode())
                timeString = getStringValue(bean.getValue().getAsDouble());
            else
                timeString = getStringValue(bean.getValue());
            writer.writeCharacters(timeString);
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for TimeRangeType complex type
     */
    public TimeRange readTimeRangeType(XMLStreamReader reader) throws XMLStreamException
    {
        TimeRange bean = factory.newTimeRange();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readTimeRangeTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readTimeRangeTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of TimeRangeType complex type
     */
    public void readTimeRangeTypeAttributes(Map<String, String> attrMap, TimeRange bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeAttributes(attrMap, bean);
        
        String val;
        
        // referencetime
        val = attrMap.get("referenceTime");
        if (val != null)
            bean.setReferenceTime(getDateTimeFromString(val));
        
        // localframe
        val = attrMap.get("localFrame");
        if (val != null)
            bean.setLocalFrame(val);
    }
    
    
    /**
     * Reads elements of TimeRangeType complex type
     */
    public void readTimeRangeTypeElements(XMLStreamReader reader, TimeRange bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeElements(reader, bean);
        
        boolean found;
        String val;
        
        // uom
        found = checkElementName(reader, "uom");
        if (found)
        {
            bean.setUom(this.readUnitReference(reader));            
            reader.nextTag(); // end property tag
            reader.nextTag();
        }
        
        // constraint
        found = checkElementName(reader, "constraint");
        if (found)
        {
            OgcProperty<AllowedTimes> constraintProp = bean.getConstraintProperty();
            readPropertyAttributes(reader, constraintProp);
            
            reader.nextTag();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                constraintProp.setValue(this.readAllowedTimes(reader));
                reader.nextTag(); // end property tag
            }
            
            reader.nextTag();
        }
        
        // value
        found = checkElementName(reader, "value");
        if (found)
        {
            val = reader.getElementText();
            if (val != null)
                bean.setValue(getDateTimeArrayFromString(val));
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for TimeRangeType complex type
     * @param writeInlineValues 
     */
    public void writeTimeRangeType(XMLStreamWriter writer, TimeRange bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeTimeRangeTypeAttributes(writer, bean);
        this.writeTimeRangeTypeElements(writer, bean, writeInlineValues);
    }
    
    
    /**
     * Writes attributes of TimeRangeType complex type
     */
    public void writeTimeRangeTypeAttributes(XMLStreamWriter writer, TimeRange bean) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeAttributes(writer, bean);
        
        // referenceTime
        if (bean.isSetReferenceTime())
            writer.writeAttribute("referenceTime", getStringValue(bean.getReferenceTime()));
        
        // localFrame
        if (bean.isSetLocalFrame())
            writer.writeAttribute("localFrame", getStringValue(bean.getLocalFrame()));
    }
    
    
    /**
     * Writes elements of TimeRangeType complex type
     * @param writeInlineValues 
     */
    public void writeTimeRangeTypeElements(XMLStreamWriter writer, TimeRange bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeElements(writer, bean);
        
        // uom
        writer.writeStartElement(NS_URI, "uom");
        this.writeUnitReference(writer, bean.getUom());
        writer.writeEndElement();
        
        // constraint
        if (bean.isSetConstraint())
        {
            writer.writeStartElement(NS_URI, "constraint");
            OgcProperty<AllowedTimes> constraintProp = bean.getConstraintProperty();
            writePropertyAttributes(writer, constraintProp);
            if (constraintProp.hasValue() && !constraintProp.hasHref())
                this.writeAllowedTimes(writer, bean.getConstraint());
            writer.writeEndElement();
        }
        
        // value
        if (bean.isSetValue() && writeInlineValues)
        {
            writer.writeStartElement(NS_URI, "value");
            String valueString;
            if (bean.getUom().isSetCode())
                valueString = getStringValueAsDoubles(bean.getValue());
            else
                valueString = getStringValue(bean.getValue());
            writer.writeCharacters(valueString);
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for BooleanType complex type
     */
    public Boolean readBooleanType(XMLStreamReader reader) throws XMLStreamException
    {
        Boolean bean = factory.newBoolean();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readBooleanTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readBooleanTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of BooleanType complex type
     */
    public void readBooleanTypeAttributes(Map<String, String> attrMap, Boolean bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of BooleanType complex type
     */
    public void readBooleanTypeElements(XMLStreamReader reader, Boolean bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeElements(reader, bean);
        
        boolean found;
        String val;
        
        // value
        found = checkElementName(reader, "value");
        if (found)
        {
            val = reader.getElementText();
            if (val != null)
                bean.setValue(getBooleanFromString(val));
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for BooleanType complex type
     * @param writeInlineValues 
     */
    public void writeBooleanType(XMLStreamWriter writer, Boolean bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeBooleanTypeAttributes(writer, bean);
        this.writeBooleanTypeElements(writer, bean, writeInlineValues);
    }
    
    
    /**
     * Writes attributes of BooleanType complex type
     */
    public void writeBooleanTypeAttributes(XMLStreamWriter writer, Boolean bean) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of BooleanType complex type
     * @param writeInlineValues 
     */
    public void writeBooleanTypeElements(XMLStreamWriter writer, Boolean bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeElements(writer, bean);
        
        // value
        if (bean.isSetValue() && writeInlineValues)
        {
            writer.writeStartElement(NS_URI, "value");
            writer.writeCharacters(getStringValue(bean.getValue()));
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for TextType complex type
     */
    public Text readTextType(XMLStreamReader reader) throws XMLStreamException
    {
        Text bean = factory.newText();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readTextTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readTextTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of TextType complex type
     */
    public void readTextTypeAttributes(Map<String, String> attrMap, Text bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of TextType complex type
     */
    public void readTextTypeElements(XMLStreamReader reader, Text bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeElements(reader, bean);
        
        boolean found;
        String val;
        
        // constraint
        found = checkElementName(reader, "constraint");
        if (found)
        {
            OgcProperty<AllowedTokens> constraintProp = bean.getConstraintProperty();
            readPropertyAttributes(reader, constraintProp);
            
            reader.nextTag();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                constraintProp.setValue(this.readAllowedTokens(reader));
                reader.nextTag(); // end property tag
            }
            
            reader.nextTag();
        }
        
        // value
        found = checkElementName(reader, "value");
        if (found)
        {
            val = reader.getElementText();
            if (val != null)
                bean.setValue(trimStringValue(val));
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for TextType complex type
     * @param writeInlineValues 
     */
    public void writeTextType(XMLStreamWriter writer, Text bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeTextTypeAttributes(writer, bean);
        this.writeTextTypeElements(writer, bean, writeInlineValues);
    }
    
    
    /**
     * Writes attributes of TextType complex type
     */
    public void writeTextTypeAttributes(XMLStreamWriter writer, Text bean) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of TextType complex type
     * @param writeInlineValues 
     */
    public void writeTextTypeElements(XMLStreamWriter writer, Text bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeElements(writer, bean);
        
        // constraint
        if (bean.isSetConstraint())
        {
            writer.writeStartElement(NS_URI, "constraint");
            OgcProperty<AllowedTokens> constraintProp = bean.getConstraintProperty();
            writePropertyAttributes(writer, constraintProp);
            if (constraintProp.hasValue() && !constraintProp.hasHref())
                this.writeAllowedTokens(writer, bean.getConstraint());
            writer.writeEndElement();
        }
        
        // value
        if (bean.isSetValue() && writeInlineValues)
        {
            writer.writeStartElement(NS_URI, "value");
            writer.writeCharacters(bean.getValue());
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for CategoryType complex type
     */
    public Category readCategoryType(XMLStreamReader reader) throws XMLStreamException
    {
        Category bean = factory.newCategory();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readCategoryTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readCategoryTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of CategoryType complex type
     */
    public void readCategoryTypeAttributes(Map<String, String> attrMap, Category bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of CategoryType complex type
     */
    public void readCategoryTypeElements(XMLStreamReader reader, Category bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeElements(reader, bean);
        
        boolean found;
        String val;
        
        // codeSpace
        found = checkElementName(reader, "codeSpace");
        if (found)
        {
            Map<String, String> attrMap = collectAttributes(reader);
            bean.setCodeSpace(attrMap.get("href"));            
            reader.nextTag(); // end property tag
            reader.nextTag();
        }
        
        // constraint
        found = checkElementName(reader, "constraint");
        if (found)
        {
            OgcProperty<AllowedTokens> constraintProp = bean.getConstraintProperty();
            readPropertyAttributes(reader, constraintProp);
            
            reader.nextTag();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                constraintProp.setValue(this.readAllowedTokens(reader));
                reader.nextTag(); // end property tag
            }
            
            reader.nextTag();
        }
        
        // value
        found = checkElementName(reader, "value");
        if (found)
        {
            val = reader.getElementText();
            if (val != null)
                bean.setValue(trimStringValue(val));
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for CategoryType complex type
     * @param writeInlineValues 
     */
    public void writeCategoryType(XMLStreamWriter writer, Category bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeCategoryTypeAttributes(writer, bean);
        this.writeCategoryTypeElements(writer, bean, writeInlineValues);
    }
    
    
    /**
     * Writes attributes of CategoryType complex type
     */
    public void writeCategoryTypeAttributes(XMLStreamWriter writer, Category bean) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of CategoryType complex type
     * @param writeInlineValues 
     */
    public void writeCategoryTypeElements(XMLStreamWriter writer, Category bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeElements(writer, bean);
        
        // codeSpace
        if (bean.isSetCodeSpace())
        {
            writer.writeStartElement(NS_URI, "codeSpace");
            writer.writeAttribute(XLINK_NS_URI, "href", bean.getCodeSpace());
            writer.writeEndElement();
        }
        
        // constraint
        if (bean.isSetConstraint())
        {
            writer.writeStartElement(NS_URI, "constraint");
            OgcProperty<AllowedTokens> constraintProp = bean.getConstraintProperty();
            writePropertyAttributes(writer, constraintProp);
            if (constraintProp.hasValue() && !constraintProp.hasHref())
                this.writeAllowedTokens(writer, bean.getConstraint());
            writer.writeEndElement();
        }
        
        // value
        if (bean.isSetValue() && writeInlineValues)
        {
            writer.writeStartElement(NS_URI, "value");
            writer.writeCharacters(bean.getValue());
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for QuantityType complex type
     */
    public Quantity readQuantityType(XMLStreamReader reader) throws XMLStreamException
    {
        Quantity bean = factory.newQuantity();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readQuantityTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readQuantityTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of QuantityType complex type
     */
    public void readQuantityTypeAttributes(Map<String, String> attrMap, Quantity bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of QuantityType complex type
     */
    public void readQuantityTypeElements(XMLStreamReader reader, Quantity bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeElements(reader, bean);
        
        boolean found;
        String val;
        
        // uom
        found = checkElementName(reader, "uom");
        if (found)
        {
            bean.setUom(this.readUnitReference(reader));            
            reader.nextTag(); // end property tag
            reader.nextTag();
        }
        
        // constraint
        found = checkElementName(reader, "constraint");
        if (found)
        {
            OgcProperty<AllowedValues> constraintProp = bean.getConstraintProperty();
            readPropertyAttributes(reader, constraintProp);
            
            reader.nextTag();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                constraintProp.setValue(this.readAllowedValues(reader));
                reader.nextTag(); // end property tag
            }
            
            reader.nextTag();
        }
        
        // value
        found = checkElementName(reader, "value");
        if (found)
        {
            val = reader.getElementText();
            if (val != null)
                bean.setValue(getDoubleFromString(val));
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for QuantityType complex type
     * @param writeInlineValues 
     */
    public void writeQuantityType(XMLStreamWriter writer, Quantity bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeQuantityTypeAttributes(writer, bean);
        this.writeQuantityTypeElements(writer, bean, writeInlineValues);
    }
    
    
    /**
     * Writes attributes of QuantityType complex type
     */
    public void writeQuantityTypeAttributes(XMLStreamWriter writer, Quantity bean) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of QuantityType complex type
     * @param writeInlineValues 
     */
    public void writeQuantityTypeElements(XMLStreamWriter writer, Quantity bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeElements(writer, bean);
        
        // uom
        writer.writeStartElement(NS_URI, "uom");
        this.writeUnitReference(writer, bean.getUom());
        writer.writeEndElement();
        
        // constraint
        if (bean.isSetConstraint())
        {
            writer.writeStartElement(NS_URI, "constraint");
            OgcProperty<AllowedValues> constraintProp = bean.getConstraintProperty();
            writePropertyAttributes(writer, constraintProp);
            if (constraintProp.hasValue() && !constraintProp.hasHref())
                this.writeAllowedValues(writer, bean.getConstraint(), false);
            writer.writeEndElement();
        }
        
        // value
        if (bean.isSetValue() && writeInlineValues)
        {
            writer.writeStartElement(NS_URI, "value");
            writer.writeCharacters(getStringValue(bean.getValue()));
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Reads attributes of AbstractDataComponentType complex type
     */
    public void readAbstractDataComponentTypeAttributes(Map<String, String> attrMap, DataComponent bean) throws XMLStreamException
    {
        this.readAbstractSWEIdentifiableTypeAttributes(attrMap, bean);
        
        String val;
        
        // updatable
        val = attrMap.get("updatable");
        if (val != null)
            bean.setUpdatable(getBooleanFromString(val));
        
        // optional
        val = attrMap.get("optional");
        if (val != null)
            bean.setOptional(getBooleanFromString(val));
        
        // definition
        val = attrMap.get("definition");
        if (val != null)
            bean.setDefinition(val);
    }
    
    
    /**
     * Reads elements of AbstractDataComponentType complex type
     */
    public void readAbstractDataComponentTypeElements(XMLStreamReader reader, DataComponent bean) throws XMLStreamException
    {
        this.readAbstractSWEIdentifiableTypeElements(reader, bean);
        
    }
    
    
    /**
     * Writes attributes of AbstractDataComponentType complex type
     */
    public void writeAbstractDataComponentTypeAttributes(XMLStreamWriter writer, DataComponent bean) throws XMLStreamException
    {
        this.writeAbstractSWEIdentifiableTypeAttributes(writer, bean);
        
        // updatable
        if (bean.isSetUpdatable())
            writer.writeAttribute("updatable", getStringValue(bean.getUpdatable()));
        
        // optional
        if (bean.isSetOptional())
            writer.writeAttribute("optional", getStringValue(bean.getOptional()));
        
        // definition
        if (bean.isSetDefinition())
            writer.writeAttribute("definition", getStringValue(bean.getDefinition()));
    }
    
    
    /**
     * Writes elements of AbstractDataComponentType complex type
     */
    public void writeAbstractDataComponentTypeElements(XMLStreamWriter writer, DataComponent bean) throws XMLStreamException
    {
        this.writeAbstractSWEIdentifiableTypeElements(writer, bean);
    }
    
    
    /**
     * Read method for CountRangeType complex type
     */
    public CountRange readCountRangeType(XMLStreamReader reader) throws XMLStreamException
    {
        CountRange bean = factory.newCountRange();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readCountRangeTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readCountRangeTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of CountRangeType complex type
     */
    public void readCountRangeTypeAttributes(Map<String, String> attrMap, CountRange bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of CountRangeType complex type
     */
    public void readCountRangeTypeElements(XMLStreamReader reader, CountRange bean) throws XMLStreamException
    {
        this.readAbstractSimpleComponentTypeElements(reader, bean);
        
        boolean found;
        String val;
        
        // constraint
        found = checkElementName(reader, "constraint");
        if (found)
        {
            OgcProperty<AllowedValues> constraintProp = bean.getConstraintProperty();
            readPropertyAttributes(reader, constraintProp);
            
            reader.nextTag();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
            {
                constraintProp.setValue(this.readAllowedValues(reader));
                reader.nextTag(); // end property tag
            }
            
            reader.nextTag();
        }
        
        // value
        found = checkElementName(reader, "value");
        if (found)
        {
            val = reader.getElementText();
            if (val != null)
                bean.setValue(getIntArrayFromString(val));
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for CountRangeType complex type
     * @param writeInlineValues 
     */
    public void writeCountRangeType(XMLStreamWriter writer, CountRange bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeCountRangeTypeAttributes(writer, bean);
        this.writeCountRangeTypeElements(writer, bean, writeInlineValues);
    }
    
    
    /**
     * Writes attributes of CountRangeType complex type
     */
    public void writeCountRangeTypeAttributes(XMLStreamWriter writer, CountRange bean) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of CountRangeType complex type
     */
    public void writeCountRangeTypeElements(XMLStreamWriter writer, CountRange bean, boolean writeInlineValues) throws XMLStreamException
    {
        this.writeAbstractSimpleComponentTypeElements(writer, bean);
        
        // constraint
        if (bean.isSetConstraint())
        {
            writer.writeStartElement(NS_URI, "constraint");
            OgcProperty<AllowedValues> constraintProp = bean.getConstraintProperty();
            writePropertyAttributes(writer, constraintProp);
            if (constraintProp.hasValue() && !constraintProp.hasHref())
                this.writeAllowedValues(writer, bean.getConstraint(), true);
            writer.writeEndElement();
        }
        
        // value
        if (bean.isSetValue() && writeInlineValues)
        {
            writer.writeStartElement(NS_URI, "value");
            writer.writeCharacters(getStringValue(bean.getValue()));
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for NilValuesType complex type
     */
    public NilValues readNilValuesType(XMLStreamReader reader) throws XMLStreamException
    {
        NilValues bean = factory.newNilValues();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readNilValuesTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readNilValuesTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of NilValuesType complex type
     */
    public void readNilValuesTypeAttributes(Map<String, String> attrMap, NilValues bean) throws XMLStreamException
    {
        this.readAbstractSWETypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of NilValuesType complex type
     */
    public void readNilValuesTypeElements(XMLStreamReader reader, NilValues bean) throws XMLStreamException
    {
        this.readAbstractSWETypeElements(reader, bean);
        
        boolean found;
        
        // nilValue
        do
        {
            found = checkElementName(reader, "nilValue");
            if (found)
            {
                bean.addNilValue(this.readNilValue(reader));
                reader.nextTag();
            }
        }
        while (found);
    }
    
    
    /**
     * Write method for NilValuesType complex type
     */
    public void writeNilValuesType(XMLStreamWriter writer, NilValues bean) throws XMLStreamException
    {
        this.writeNilValuesTypeAttributes(writer, bean);
        this.writeNilValuesTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of NilValuesType complex type
     */
    public void writeNilValuesTypeAttributes(XMLStreamWriter writer, NilValues bean) throws XMLStreamException
    {
        this.writeAbstractSWETypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of NilValuesType complex type
     */
    public void writeNilValuesTypeElements(XMLStreamWriter writer, NilValues bean) throws XMLStreamException
    {
        this.writeAbstractSWETypeElements(writer, bean);
        int numItems;
        
        // nilValue
        numItems = bean.getNilValueList().size();
        for (int i = 0; i < numItems; i++)
        {
            NilValue item = bean.getNilValueList().get(i);
            writer.writeStartElement(NS_URI, "nilValue");
            this.writeNilValue(writer, item);
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for AllowedTokensType complex type
     */
    public AllowedTokens readAllowedTokensType(XMLStreamReader reader) throws XMLStreamException
    {
        AllowedTokens bean = factory.newAllowedTokens();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readAllowedTokensTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readAllowedTokensTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of AllowedTokensType complex type
     */
    public void readAllowedTokensTypeAttributes(Map<String, String> attrMap, AllowedTokens bean) throws XMLStreamException
    {
        this.readAbstractSWETypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of AllowedTokensType complex type
     */
    public void readAllowedTokensTypeElements(XMLStreamReader reader, AllowedTokens bean) throws XMLStreamException
    {
        this.readAbstractSWETypeElements(reader, bean);
        
        boolean found;
        String val;
        
        // value
        do
        {
            found = checkElementName(reader, "value");
            if (found)
            {
                val = reader.getElementText();
                if (val != null)
                    bean.addValue(trimStringValue(val));
                reader.nextTag();
            }
        }
        while (found);
        
        // pattern
        found = checkElementName(reader, "pattern");
        if (found)
        {
            val = reader.getElementText();
            if (val != null)
                bean.setPattern(trimStringValue(val));
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for AllowedTokensType complex type
     */
    public void writeAllowedTokensType(XMLStreamWriter writer, AllowedTokens bean) throws XMLStreamException
    {
        this.writeAllowedTokensTypeAttributes(writer, bean);
        this.writeAllowedTokensTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of AllowedTokensType complex type
     */
    public void writeAllowedTokensTypeAttributes(XMLStreamWriter writer, AllowedTokens bean) throws XMLStreamException
    {
        this.writeAbstractSWETypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of AllowedTokensType complex type
     */
    public void writeAllowedTokensTypeElements(XMLStreamWriter writer, AllowedTokens bean) throws XMLStreamException
    {
        this.writeAbstractSWETypeElements(writer, bean);
        int numItems;
        
        // value
        numItems = bean.getValueList().size();
        for (int i = 0; i < numItems; i++)
        {
            String item = bean.getValueList().get(i);
            writer.writeStartElement(NS_URI, "value");
            writer.writeCharacters(item);
            writer.writeEndElement();
        }
        
        // pattern
        if (bean.isSetPattern())
        {
            writer.writeStartElement(NS_URI, "pattern");
            writer.writeCharacters(bean.getPattern());
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for AllowedValuesType complex type
     */
    public AllowedValues readAllowedValuesType(XMLStreamReader reader) throws XMLStreamException
    {
        AllowedValues bean = factory.newAllowedValues();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readAllowedValuesTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readAllowedValuesTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of AllowedValuesType complex type
     */
    public void readAllowedValuesTypeAttributes(Map<String, String> attrMap, AllowedValues bean) throws XMLStreamException
    {
        this.readAbstractSWETypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of AllowedValuesType complex type
     */
    public void readAllowedValuesTypeElements(XMLStreamReader reader, AllowedValues bean) throws XMLStreamException
    {
        this.readAbstractSWETypeElements(reader, bean);
        
        boolean found;
        String val;
        
        // value
        do
        {
            found = checkElementName(reader, "value");
            if (found)
            {
                val = reader.getElementText();
                if (val != null)
                    bean.addValue(getDoubleFromString(val));
                reader.nextTag();
            }
        }
        while (found);
        
        // interval
        do
        {
            found = checkElementName(reader, "interval");
            if (found)
            {
                val = reader.getElementText();
                if (val != null)
                    bean.addInterval(getDoubleArrayFromString(val));
                reader.nextTag();
            }
        }
        while (found);
        
        // significantFigures
        found = checkElementName(reader, "significantFigures");
        if (found)
        {
            val = reader.getElementText();
            if (val != null)
                bean.setSignificantFigures(getIntFromString(val));
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for AllowedValuesType complex type
     */
    public void writeAllowedValuesType(XMLStreamWriter writer, AllowedValues bean, boolean writeIntegers) throws XMLStreamException
    {
        this.writeAllowedValuesTypeAttributes(writer, bean);
        this.writeAllowedValuesTypeElements(writer, bean, writeIntegers);
    }
    
    
    /**
     * Writes attributes of AllowedValuesType complex type
     */
    public void writeAllowedValuesTypeAttributes(XMLStreamWriter writer, AllowedValues bean) throws XMLStreamException
    {
        this.writeAbstractSWETypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of AllowedValuesType complex type
     */
    public void writeAllowedValuesTypeElements(XMLStreamWriter writer, AllowedValues bean, boolean writeIntegers) throws XMLStreamException
    {
        this.writeAbstractSWETypeElements(writer, bean);
        int numItems;
        
        // value
        numItems = bean.getValueList().size();
        for (int i = 0; i < numItems; i++)
        {
            double item = bean.getValueList().get(i);
            writer.writeStartElement(NS_URI, "value");
            String text;
            if (writeIntegers)
                text = getStringValue((int)item);
            else
                text = getStringValue(item);
            writer.writeCharacters(text);
            writer.writeEndElement();
        }
        
        // interval
        numItems = bean.getIntervalList().size();
        for (int i = 0; i < numItems; i++)
        {
            double[] item = bean.getIntervalList().get(i);
            writer.writeStartElement(NS_URI, "interval");
            String text;
            if (writeIntegers)
            {
                int[] data = new int[item.length];
                for (int t=0; t<item.length; t++)
                    data[t] = (int)item[t];
                text = getStringValue(data);
            }
            else
                text = getStringValue(item);
            writer.writeCharacters(text);
            writer.writeEndElement();
        }
        
        // significantFigures
        if (bean.isSetSignificantFigures())
        {
            writer.writeStartElement(NS_URI, "significantFigures");
            writer.writeCharacters(getStringValue(bean.getSignificantFigures()));
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for AllowedTimesType complex type
     */
    public AllowedTimes readAllowedTimesType(XMLStreamReader reader) throws XMLStreamException
    {
        AllowedTimes bean = factory.newAllowedTimes();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readAllowedTimesTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readAllowedTimesTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of AllowedTimesType complex type
     */
    public void readAllowedTimesTypeAttributes(Map<String, String> attrMap, AllowedTimes bean) throws XMLStreamException
    {
        this.readAbstractSWETypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of AllowedTimesType complex type
     */
    public void readAllowedTimesTypeElements(XMLStreamReader reader, AllowedTimes bean) throws XMLStreamException
    {
        this.readAbstractSWETypeElements(reader, bean);
        
        boolean found;
        String val;
        
        // value
        do
        {
            found = checkElementName(reader, "value");
            if (found)
            {
                val = reader.getElementText();
                if (val != null)
                    bean.addValue(getDateTimeFromString(val));
                reader.nextTag();
            }
        }
        while (found);
        
        // interval
        do
        {
            found = checkElementName(reader, "interval");
            if (found)
            {
                val = reader.getElementText();
                if (val != null)
                    bean.addInterval(getDateTimeArrayFromString(val));
                reader.nextTag();
            }
        }
        while (found);
        
        // significantFigures
        found = checkElementName(reader, "significantFigures");
        if (found)
        {
            val = reader.getElementText();
            if (val != null)
                bean.setSignificantFigures(getIntFromString(val));
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for AllowedTimesType complex type
     */
    public void writeAllowedTimesType(XMLStreamWriter writer, AllowedTimes bean) throws XMLStreamException
    {
        this.writeAllowedTimesTypeAttributes(writer, bean);
        this.writeAllowedTimesTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of AllowedTimesType complex type
     */
    public void writeAllowedTimesTypeAttributes(XMLStreamWriter writer, AllowedTimes bean) throws XMLStreamException
    {
        this.writeAbstractSWETypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of AllowedTimesType complex type
     */
    public void writeAllowedTimesTypeElements(XMLStreamWriter writer, AllowedTimes bean) throws XMLStreamException
    {
        this.writeAbstractSWETypeElements(writer, bean);
        int numItems;
        
        // value
        numItems = bean.getValueList().size();
        for (int i = 0; i < numItems; i++)
        {
            IDateTime item = bean.getValueList().get(i);
            writer.writeStartElement(NS_URI, "value");
            writer.writeCharacters(getStringValue(item));
            writer.writeEndElement();
        }
        
        // interval
        numItems = bean.getIntervalList().size();
        for (int i = 0; i < numItems; i++)
        {
            IDateTime[] item = bean.getIntervalList().get(i);
            writer.writeStartElement(NS_URI, "interval");
            writer.writeCharacters(getStringValue(item));
            writer.writeEndElement();
        }
        
        // significantFigures
        if (bean.isSetSignificantFigures())
        {
            writer.writeStartElement(NS_URI, "significantFigures");
            writer.writeCharacters(getStringValue(bean.getSignificantFigures()));
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Reads attributes of AbstractEncodingType complex type
     */
    public void readAbstractEncodingTypeAttributes(Map<String, String> attrMap, DataEncoding bean) throws XMLStreamException
    {
        this.readAbstractSWETypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of AbstractEncodingType complex type
     */
    public void readAbstractEncodingTypeElements(XMLStreamReader reader, DataEncoding bean) throws XMLStreamException
    {
        this.readAbstractSWETypeElements(reader, bean);
        
    }
    
    
    /**
     * Writes attributes of AbstractEncodingType complex type
     */
    public void writeAbstractEncodingTypeAttributes(XMLStreamWriter writer, DataEncoding bean) throws XMLStreamException
    {
        this.writeAbstractSWETypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of AbstractEncodingType complex type
     */
    public void writeAbstractEncodingTypeElements(XMLStreamWriter writer, DataEncoding bean) throws XMLStreamException
    {
        this.writeAbstractSWETypeElements(writer, bean);
    }
    
    
    /**
     * Read method for XMLEncodingType complex type
     */
    public XMLEncoding readXMLEncodingType(XMLStreamReader reader) throws XMLStreamException
    {
        XMLEncoding bean = factory.newXMLEncoding();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readXMLEncodingTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readXMLEncodingTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of XMLEncodingType complex type
     */
    public void readXMLEncodingTypeAttributes(Map<String, String> attrMap, XMLEncoding bean) throws XMLStreamException
    {
        this.readAbstractEncodingTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of XMLEncodingType complex type
     */
    public void readXMLEncodingTypeElements(XMLStreamReader reader, XMLEncoding bean) throws XMLStreamException
    {
        this.readAbstractEncodingTypeElements(reader, bean);
        
    }
    
    
    /**
     * Write method for XMLEncodingType complex type
     */
    public void writeXMLEncodingType(XMLStreamWriter writer, XMLEncoding bean) throws XMLStreamException
    {
        this.writeXMLEncodingTypeAttributes(writer, bean);
        this.writeXMLEncodingTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of XMLEncodingType complex type
     */
    public void writeXMLEncodingTypeAttributes(XMLStreamWriter writer, XMLEncoding bean) throws XMLStreamException
    {
        this.writeAbstractEncodingTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of XMLEncodingType complex type
     */
    public void writeXMLEncodingTypeElements(XMLStreamWriter writer, XMLEncoding bean) throws XMLStreamException
    {
        this.writeAbstractEncodingTypeElements(writer, bean);
    }
    
    
    /**
     * Read method for TextEncodingType complex type
     */
    public TextEncoding readTextEncodingType(XMLStreamReader reader) throws XMLStreamException
    {
        TextEncoding bean = factory.newTextEncoding();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readTextEncodingTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readTextEncodingTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of TextEncodingType complex type
     */
    public void readTextEncodingTypeAttributes(Map<String, String> attrMap, TextEncoding bean) throws XMLStreamException
    {
        this.readAbstractEncodingTypeAttributes(attrMap, bean);
        
        String val;
        
        // collapsewhitespaces
        val = attrMap.get("collapseWhiteSpaces");
        if (val != null)
            bean.setCollapseWhiteSpaces(getBooleanFromString(val));
        
        // decimalseparator
        val = attrMap.get("decimalSeparator");
        if (val != null)
            bean.setDecimalSeparator(val);
        
        // tokenseparator
        val = attrMap.get("tokenSeparator");
        if (val != null)
            bean.setTokenSeparator(val);
        
        // blockseparator
        val = attrMap.get("blockSeparator");
        if (val != null)
            bean.setBlockSeparator(val);
    }
    
    
    /**
     * Reads elements of TextEncodingType complex type
     */
    public void readTextEncodingTypeElements(XMLStreamReader reader, TextEncoding bean) throws XMLStreamException
    {
        this.readAbstractEncodingTypeElements(reader, bean);
        
    }
    
    
    /**
     * Write method for TextEncodingType complex type
     */
    public void writeTextEncodingType(XMLStreamWriter writer, TextEncoding bean) throws XMLStreamException
    {
        this.writeTextEncodingTypeAttributes(writer, bean);
        this.writeTextEncodingTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of TextEncodingType complex type
     */
    public void writeTextEncodingTypeAttributes(XMLStreamWriter writer, TextEncoding bean) throws XMLStreamException
    {
        this.writeAbstractEncodingTypeAttributes(writer, bean);
        
        // collapseWhiteSpaces
        if (bean.isSetCollapseWhiteSpaces())
            writer.writeAttribute("collapseWhiteSpaces", getStringValue(bean.getCollapseWhiteSpaces()));
        
        // decimalSeparator
        if (bean.isSetDecimalSeparator())
            writer.writeAttribute("decimalSeparator", getStringValue(bean.getDecimalSeparator()));
        
        // tokenSeparator
        writer.writeAttribute("tokenSeparator", getStringValue(bean.getTokenSeparator()));
        
        // blockSeparator
        writer.writeAttribute("blockSeparator", getStringValue(bean.getBlockSeparator()));
    }
    
    
    /**
     * Writes elements of TextEncodingType complex type
     */
    public void writeTextEncodingTypeElements(XMLStreamWriter writer, TextEncoding bean) throws XMLStreamException
    {
        this.writeAbstractEncodingTypeElements(writer, bean);
    }
    
    
    /**
     * Reads attributes of AbstractSWEType complex type
     */
    public void readAbstractSWETypeAttributes(Map<String, String> attrMap, AbstractSWE bean) throws XMLStreamException
    {
        String val;
        
        // id
        val = attrMap.get("id");
        if (val != null)
        {
            bean.setId(val);
            idrefMap.put(val, bean);
        }
    }
    
    
    /**
     * Reads elements of AbstractSWEType complex type
     */
    public void readAbstractSWETypeElements(XMLStreamReader reader, AbstractSWE bean) throws XMLStreamException
    {
        boolean found;
        
        // extension
        do
        {
            found = checkElementName(reader, "extension");
            if (found)
            {
                reader.nextTag();
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
                {
                    Object extension = this.readExtension(reader);
                    if (extension != null)
                        bean.addExtension(extension);
                }
                
                reader.nextTag();
            }
        }
        while (found);
    }
    
    
    /**
     * Writes attributes of AbstractSWEType complex type
     */
    public void writeAbstractSWETypeAttributes(XMLStreamWriter writer, AbstractSWE bean) throws XMLStreamException
    {        
        // id
        if (bean.isSetId())
            writer.writeAttribute("id", getStringValue(bean.getId()));
    }
    
    
    /**
     * Writes elements of AbstractSWEType complex type
     */
    public void writeAbstractSWETypeElements(XMLStreamWriter writer, AbstractSWE bean) throws XMLStreamException
    {
        int numItems;
        
        // extension
        numItems = bean.getExtensionList().size();
        for (int i = 0; i < numItems; i++)
        {
            Object item = bean.getExtensionList().get(i);
            if (canWriteExtension(item))
            {
                writer.writeStartElement(NS_URI, "extension");
                this.writeExtension(writer, item);
                writer.writeEndElement();
            }
        }
    }
    
    
    /**
     * Reads attributes of AbstractSWEIdentifiableType complex type
     */
    public void readAbstractSWEIdentifiableTypeAttributes(Map<String, String> attrMap, AbstractSWEIdentifiable bean) throws XMLStreamException
    {
        this.readAbstractSWETypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of AbstractSWEIdentifiableType complex type
     */
    public void readAbstractSWEIdentifiableTypeElements(XMLStreamReader reader, AbstractSWEIdentifiable bean) throws XMLStreamException
    {
        this.readAbstractSWETypeElements(reader, bean);
        
        boolean found;
        String val;
        
        // identifier
        // need to check for NS URI here because there is a conflict with SensorML identifier in IdentifierList
        found = checkElementQName(reader, NS_URI, "identifier");
        if (found)
        {
            val = reader.getElementText();
            if (val != null)
                bean.setIdentifier(trimStringValue(val));
            reader.nextTag();
        }
        
        // label
        found = checkElementName(reader, "label");
        if (found)
        {
            val = reader.getElementText();
            if (val != null)
                bean.setLabel(trimStringValue(val));
            reader.nextTag();
        }
        
        // description
        found = checkElementName(reader, "description");
        if (found)
        {
            val = reader.getElementText();
            if (val != null)
                bean.setDescription(trimStringValue(val));
            reader.nextTag();
        }
    }
    
    
    /**
     * Writes attributes of AbstractSWEIdentifiableType complex type
     */
    public void writeAbstractSWEIdentifiableTypeAttributes(XMLStreamWriter writer, AbstractSWEIdentifiable bean) throws XMLStreamException
    {
        this.writeAbstractSWETypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of AbstractSWEIdentifiableType complex type
     */
    public void writeAbstractSWEIdentifiableTypeElements(XMLStreamWriter writer, AbstractSWEIdentifiable bean) throws XMLStreamException
    {
        this.writeAbstractSWETypeElements(writer, bean);
        
        // identifier
        if (bean.isSetIdentifier())
        {
            writer.writeStartElement(NS_URI, "identifier");
            writer.writeCharacters(bean.getIdentifier());
            writer.writeEndElement();
        }
        
        // label
        if (bean.isSetLabel())
        {
            writer.writeStartElement(NS_URI, "label");
            writer.writeCharacters(bean.getLabel());
            writer.writeEndElement();
        }
        
        // description
        if (bean.isSetDescription())
        {
            writer.writeStartElement(NS_URI, "description");
            writer.writeCharacters(bean.getDescription());
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for UnitReference complex type
     */
    public UnitReference readUnitReference(XMLStreamReader reader) throws XMLStreamException
    {
        UnitReference bean = factory.newUnitReference();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readUnitReferenceAttributes(attrMap, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of UnitReference complex type
     */
    public void readUnitReferenceAttributes(Map<String, String> attrMap, UnitReference bean) throws XMLStreamException
    {
        readPropertyAttributes(attrMap, bean);
        
        String val;
        
        // code
        val = attrMap.get("code");
        if (val != null)
            bean.setCode(val);
    }
    
    
    /**
     * Write method for UnitReference complex type
     */
    public void writeUnitReference(XMLStreamWriter writer, UnitReference bean) throws XMLStreamException
    {
        this.writeUnitReferenceAttributes(writer, bean);
    }
    
    
    /**
     * Writes attributes of UnitReference complex type
     */
    public void writeUnitReferenceAttributes(XMLStreamWriter writer, UnitReference bean) throws XMLStreamException
    {
        writePropertyAttributes(writer, bean);
        
        // code
        if (bean.isSetCode())
            writer.writeAttribute("code", getStringValue(bean.getCode()));
    }
    
    
    /**
     * Read method for NilValue complex type
     */
    public NilValue readNilValue(XMLStreamReader reader) throws XMLStreamException
    {
        NilValue bean = factory.newNilValue();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readNilValueAttributes(attrMap, bean);
        
        String val = reader.getElementText();
        if (val != null)
            bean.setValue(trimStringValue(val));
        
        return bean;
    }
    
    
    /**
     * Reads attributes of NilValue complex type
     */
    public void readNilValueAttributes(Map<String, String> attrMap, NilValue bean) throws XMLStreamException
    {
        String val;
        
        // reason
        val = attrMap.get("reason");
        if (val != null)
            bean.setReason(val);
    }
    
    
    /**
     * Write method for NilValue complex type
     */
    public void writeNilValue(XMLStreamWriter writer, NilValue bean) throws XMLStreamException
    {
        this.writeNilValueAttributes(writer, bean);
        
        writer.writeCharacters(getStringValue(bean.getValue()));
    }
    
    
    /**
     * Writes attributes of NilValue complex type
     */
    public void writeNilValueAttributes(XMLStreamWriter writer, NilValue bean) throws XMLStreamException
    {
        
        // reason
        writer.writeAttribute("reason", getStringValue(bean.getReason()));
    }
    
    
    /**
     * Read method for EncodedValuesPropertyType complex type
     */
    public EncodedValues readEncodedValuesPropertyType(XMLStreamReader reader, AbstractSWEIdentifiable blockComponent, DataEncoding encoding) throws XMLStreamException
    {
        EncodedValues bean = factory.newEncodedValuesProperty();
        
        Map<String, String> attrMap = collectAttributes(reader);
        readPropertyAttributes(attrMap, bean);
        
        String text = reader.getElementText();
        if (text != null && text.trim().length() > 0)
        {
            if (blockComponent instanceof DataArray)
                bean.setAsText((DataArray)blockComponent, encoding, text);
            else if (blockComponent instanceof DataStream)
                bean.setAsText((DataStream)blockComponent, encoding, text);
        }
        else if (!bean.hasHref())
            return null;
        
        return bean;
    }
    
    
    /**
     * Write method for EncodedValuesPropertyType complex type
     */
    public void writeEncodedValuesPropertyType(XMLStreamWriter writer, AbstractSWEIdentifiable blockComponent, DataEncoding encoding, EncodedValues bean) throws XMLStreamException
    {
        writePropertyAttributes(writer, bean);
        
        if (!bean.hasHref())
        {
            String text = null;        
            if (blockComponent instanceof DataArray)
                text = bean.getAsText((DataArray)blockComponent, encoding);
            else if (blockComponent instanceof DataStream)
                text = bean.getAsText((DataStream)blockComponent, encoding);        
            
            if (text != null)
                writer.writeCharacters(text);
        }
    }
        
    
    /**
     * Read method for DataRecord elements
     */
    public DataRecord readDataRecord(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "DataRecord");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readDataRecordType(reader);
    }
    
    
    /**
     * Write method for DataRecord element
     * @param writeInlineValue 
     */
    public void writeDataRecord(XMLStreamWriter writer, DataRecord bean, boolean writeInlineValues) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "DataRecord");
        this.writeNamespaces(writer);
        this.writeDataRecordType(writer, bean, writeInlineValues);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Vector elements
     */
    public Vector readVector(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Vector");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readVectorType(reader);
    }
    
    
    /**
     * Write method for Vector element
     * @param writeInlineValues 
     */
    public void writeVector(XMLStreamWriter writer, Vector bean, boolean writeInlineValues) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Vector");
        this.writeNamespaces(writer);
        this.writeVectorType(writer, bean, writeInlineValues);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for DataArray elements
     */
    public DataArray readDataArray(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "DataArray");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readDataArrayType(reader);
    }
    
    
    /**
     * Write method for DataArray element
     * @param writeInlineValues 
     */
    public void writeDataArray(XMLStreamWriter writer, DataArray bean, boolean writeInlineValues) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "DataArray");
        this.writeNamespaces(writer);
        this.writeDataArrayType(writer, bean, writeInlineValues);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Matrix elements
     */
    public Matrix readMatrix(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Matrix");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readMatrixType(reader);
    }
    
    
    /**
     * Write method for Matrix element
     * @param writeInlineValues 
     */
    public void writeMatrix(XMLStreamWriter writer, Matrix bean, boolean writeInlineValues) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Matrix");
        this.writeNamespaces(writer);
        this.writeMatrixType(writer, bean, writeInlineValues);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for DataStream elements
     */
    public DataStream readDataStream(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "DataStream");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readDataStreamType(reader);
    }
    
    
    /**
     * Write method for DataStream element
     */
    public void writeDataStream(XMLStreamWriter writer, DataStream bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "DataStream");
        this.writeNamespaces(writer);
        this.writeDataStreamType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Block elements
     */
    public BinaryBlock readBlock(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Block");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readBlockType(reader);
    }
    
    
    /**
     * Write method for Block element
     */
    public void writeBlock(XMLStreamWriter writer, BinaryBlock bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Block");
        this.writeNamespaces(writer);
        this.writeBlockType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for BinaryEncoding elements
     */
    public BinaryEncoding readBinaryEncoding(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "BinaryEncoding");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readBinaryEncodingType(reader);
    }
    
    
    /**
     * Write method for BinaryEncoding element
     */
    public void writeBinaryEncoding(XMLStreamWriter writer, BinaryEncoding bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "BinaryEncoding");
        this.writeNamespaces(writer);
        this.writeBinaryEncodingType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Component elements
     */
    public BinaryComponent readComponent(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Component");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readComponentType(reader);
    }
    
    
    /**
     * Write method for Component element
     */
    public void writeComponent(XMLStreamWriter writer, BinaryComponent bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Component");
        this.writeNamespaces(writer);
        this.writeComponentType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for DataChoice elements
     */
    public DataChoice readDataChoice(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "DataChoice");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readDataChoiceType(reader);
    }
    
    
    /**
     * Write method for DataChoice element
     * @param writeInlineValues 
     */
    public void writeDataChoice(XMLStreamWriter writer, DataChoice bean, boolean writeInlineValues) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "DataChoice");
        this.writeNamespaces(writer);
        this.writeDataChoiceType(writer, bean, writeInlineValues);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Count elements
     */
    public Count readCount(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Count");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readCountType(reader);
    }
    
    
    /**
     * Write method for Count element
     * @param writeInlineValue 
     */
    public void writeCount(XMLStreamWriter writer, Count bean, boolean writeInlineValues) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Count");
        this.writeNamespaces(writer);
        this.writeCountType(writer, bean, writeInlineValues);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for CategoryRange elements
     */
    public CategoryRange readCategoryRange(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "CategoryRange");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readCategoryRangeType(reader);
    }
    
    
    /**
     * Write method for CategoryRange element
     * @param writeInlineValues 
     */
    public void writeCategoryRange(XMLStreamWriter writer, CategoryRange bean, boolean writeInlineValues) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "CategoryRange");
        this.writeNamespaces(writer);
        this.writeCategoryRangeType(writer, bean, writeInlineValues);
        writer.writeEndElement();
    }
    
    
    /**
     * Dispatcher method for reading elements derived from AbstractSimpleComponent
     */
    public SimpleComponent readAbstractSimpleComponent(XMLStreamReader reader) throws XMLStreamException
    {
        String localName = reader.getName().getLocalPart();
        
        if (localName.equals("Count"))
            return readCount(reader);
        else if (localName.equals("CategoryRange"))
            return readCategoryRange(reader);
        else if (localName.equals("QuantityRange"))
            return readQuantityRange(reader);
        else if (localName.equals("Time"))
            return readTime(reader);
        else if (localName.equals("TimeRange"))
            return readTimeRange(reader);
        else if (localName.equals("Boolean"))
            return readBoolean(reader);
        else if (localName.equals("Text"))
            return readText(reader);
        else if (localName.equals("Category"))
            return readCategory(reader);
        else if (localName.equals("Quantity"))
            return readQuantity(reader);
        else if (localName.equals("CountRange"))
            return readCountRange(reader);
        
        throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
    }
    
    
    /**
     * Dispatcher method for writing classes derived from AbstractSimpleComponent
     */
    public void writeAbstractSimpleComponent(XMLStreamWriter writer, SimpleComponent bean, boolean writeInlineValues) throws XMLStreamException
    {
        if (bean instanceof Count)
            writeCount(writer, (Count)bean, writeInlineValues);
        else if (bean instanceof CategoryRange)
            writeCategoryRange(writer, (CategoryRange)bean, writeInlineValues);
        else if (bean instanceof QuantityRange)
            writeQuantityRange(writer, (QuantityRange)bean, writeInlineValues);
        else if (bean instanceof Time)
            writeTime(writer, (Time)bean, writeInlineValues);
        else if (bean instanceof TimeRange)
            writeTimeRange(writer, (TimeRange)bean, writeInlineValues);
        else if (bean instanceof Boolean)
            writeBoolean(writer, (Boolean)bean, writeInlineValues);
        else if (bean instanceof Text)
            writeText(writer, (Text)bean, writeInlineValues);
        else if (bean instanceof Category)
            writeCategory(writer, (Category)bean, writeInlineValues);
        else if (bean instanceof Quantity)
            writeQuantity(writer, (Quantity)bean, writeInlineValues);
        else if (bean instanceof CountRange)
            writeCountRange(writer, (CountRange)bean, writeInlineValues);
    }
    
    
    /**
     * Read method for QuantityRange elements
     */
    public QuantityRange readQuantityRange(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "QuantityRange");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readQuantityRangeType(reader);
    }
    
    
    /**
     * Write method for QuantityRange element
     * @param writeInlineValues 
     */
    public void writeQuantityRange(XMLStreamWriter writer, QuantityRange bean, boolean writeInlineValues) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "QuantityRange");
        this.writeNamespaces(writer);
        this.writeQuantityRangeType(writer, bean, writeInlineValues);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Time elements
     */
    public Time readTime(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Time");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readTimeType(reader);
    }
    
    
    /**
     * Write method for Time element
     * @param writeInlineValues 
     */
    public void writeTime(XMLStreamWriter writer, Time bean, boolean writeInlineValues) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Time");
        this.writeNamespaces(writer);
        this.writeTimeType(writer, bean, writeInlineValues);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for TimeRange elements
     */
    public TimeRange readTimeRange(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "TimeRange");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readTimeRangeType(reader);
    }
    
    
    /**
     * Write method for TimeRange element
     * @param writeInlineValues 
     */
    public void writeTimeRange(XMLStreamWriter writer, TimeRange bean, boolean writeInlineValues) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "TimeRange");
        this.writeNamespaces(writer);
        this.writeTimeRangeType(writer, bean, writeInlineValues);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Boolean elements
     */
    public Boolean readBoolean(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Boolean");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readBooleanType(reader);
    }
    
    
    /**
     * Write method for Boolean element
     * @param writeInlineValues 
     */
    public void writeBoolean(XMLStreamWriter writer, Boolean bean, boolean writeInlineValues) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Boolean");
        this.writeNamespaces(writer);
        this.writeBooleanType(writer, bean, writeInlineValues);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Text elements
     */
    public Text readText(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Text");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readTextType(reader);
    }
    
    
    /**
     * Write method for Text element
     * @param writeInlineValues 
     */
    public void writeText(XMLStreamWriter writer, Text bean, boolean writeInlineValues) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Text");
        this.writeNamespaces(writer);
        this.writeTextType(writer, bean, writeInlineValues);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Category elements
     */
    public Category readCategory(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Category");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readCategoryType(reader);
    }
    
    
    /**
     * Write method for Category element
     * @param writeInlineValues 
     */
    public void writeCategory(XMLStreamWriter writer, Category bean, boolean writeInlineValues) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Category");
        this.writeNamespaces(writer);
        this.writeCategoryType(writer, bean, writeInlineValues);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Quantity elements
     */
    public Quantity readQuantity(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Quantity");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readQuantityType(reader);
    }
    
    
    /**
     * Write method for Quantity element
     * @param writeInlineValues 
     */
    public void writeQuantity(XMLStreamWriter writer, Quantity bean, boolean writeInlineValues) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Quantity");
        this.writeNamespaces(writer);
        this.writeQuantityType(writer, bean, writeInlineValues);
        writer.writeEndElement();
    }
    
    
    /**
     * Dispatcher method for reading elements derived from AbstractDataComponent
     */
    public DataComponent readDataComponent(XMLStreamReader reader) throws XMLStreamException
    {
        String localName = reader.getName().getLocalPart();
        
        if (localName.equals("DataRecord"))
            return readDataRecord(reader);
        else if (localName.equals("Vector"))
            return readVector(reader);
        else if (localName.equals("DataArray"))
            return readDataArray(reader);
        else if (localName.equals("Matrix"))
            return readMatrix(reader);
        else if (localName.equals("DataChoice"))
            return readDataChoice(reader);
        else if (localName.equals("Count"))
            return readCount(reader);
        else if (localName.equals("CategoryRange"))
            return readCategoryRange(reader);
        else if (localName.equals("QuantityRange"))
            return readQuantityRange(reader);
        else if (localName.equals("Time"))
            return readTime(reader);
        else if (localName.equals("TimeRange"))
            return readTimeRange(reader);
        else if (localName.equals("Boolean"))
            return readBoolean(reader);
        else if (localName.equals("Text"))
            return readText(reader);
        else if (localName.equals("Category"))
            return readCategory(reader);
        else if (localName.equals("Quantity"))
            return readQuantity(reader);
        else if (localName.equals("CountRange"))
            return readCountRange(reader);
        
        throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
    }
    
    
    /**
     * Dispatcher method for writing classes derived from AbstractDataComponent
     */
    public void writeDataComponent(XMLStreamWriter writer, DataComponent bean, boolean writeInlineValues) throws XMLStreamException
    {
        if (bean instanceof DataRecord)
            writeDataRecord(writer, (DataRecord)bean, writeInlineValues);
        else if (bean instanceof Vector)
            writeVector(writer, (Vector)bean, writeInlineValues);
        else if (bean instanceof Matrix)
            writeMatrix(writer, (Matrix)bean, writeInlineValues);
        else if (bean instanceof DataArray)
            writeDataArray(writer, (DataArray)bean, writeInlineValues);
        else if (bean instanceof DataChoice)
            writeDataChoice(writer, (DataChoice)bean, writeInlineValues);
        else if (bean instanceof Count)
            writeCount(writer, (Count)bean, writeInlineValues);
        else if (bean instanceof CategoryRange)
            writeCategoryRange(writer, (CategoryRange)bean, writeInlineValues);
        else if (bean instanceof QuantityRange)
            writeQuantityRange(writer, (QuantityRange)bean, writeInlineValues);
        else if (bean instanceof Time)
            writeTime(writer, (Time)bean, writeInlineValues);
        else if (bean instanceof TimeRange)
            writeTimeRange(writer, (TimeRange)bean, writeInlineValues);
        else if (bean instanceof Boolean)
            writeBoolean(writer, (Boolean)bean, writeInlineValues);
        else if (bean instanceof Text)
            writeText(writer, (Text)bean, writeInlineValues);
        else if (bean instanceof Category)
            writeCategory(writer, (Category)bean, writeInlineValues);
        else if (bean instanceof Quantity)
            writeQuantity(writer, (Quantity)bean, writeInlineValues);
        else if (bean instanceof CountRange)
            writeCountRange(writer, (CountRange)bean, writeInlineValues);
    }
    
    
    /**
     * Read method for CountRange elements
     */
    public CountRange readCountRange(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "CountRange");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readCountRangeType(reader);
    }
    
    
    /**
     * Write method for CountRange element
     * @param writeInlineValues 
     */
    public void writeCountRange(XMLStreamWriter writer, CountRange bean, boolean writeInlineValues) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "CountRange");
        this.writeNamespaces(writer);
        this.writeCountRangeType(writer, bean, writeInlineValues);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for NilValues elements
     */
    public NilValues readNilValues(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "NilValues");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readNilValuesType(reader);
    }
    
    
    /**
     * Write method for NilValues element
     */
    public void writeNilValues(XMLStreamWriter writer, NilValues bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "NilValues");
        this.writeNamespaces(writer);
        this.writeNilValuesType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for AllowedTokens elements
     */
    public AllowedTokens readAllowedTokens(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "AllowedTokens");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readAllowedTokensType(reader);
    }
    
    
    /**
     * Write method for AllowedTokens element
     */
    public void writeAllowedTokens(XMLStreamWriter writer, AllowedTokens bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "AllowedTokens");
        this.writeNamespaces(writer);
        this.writeAllowedTokensType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for AllowedValues elements
     */
    public AllowedValues readAllowedValues(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "AllowedValues");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readAllowedValuesType(reader);
    }
    
    
    /**
     * Write method for AllowedValues element
     */
    public void writeAllowedValues(XMLStreamWriter writer, AllowedValues bean, boolean writeIntegers) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "AllowedValues");
        this.writeNamespaces(writer);
        this.writeAllowedValuesType(writer, bean, writeIntegers);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for AllowedTimes elements
     */
    public AllowedTimes readAllowedTimes(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "AllowedTimes");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readAllowedTimesType(reader);
    }
    
    
    /**
     * Write method for AllowedTimes element
     */
    public void writeAllowedTimes(XMLStreamWriter writer, AllowedTimes bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "AllowedTimes");
        this.writeNamespaces(writer);
        this.writeAllowedTimesType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Dispatcher method for reading elements derived from AbstractEncoding
     */
    public DataEncoding readAbstractEncoding(XMLStreamReader reader) throws XMLStreamException
    {
        String localName = reader.getName().getLocalPart();
        
        if (localName.equals("BinaryEncoding"))
            return readBinaryEncoding(reader);
        else if (localName.equals("XMLEncoding"))
            return readXMLEncoding(reader);
        else if (localName.equals("TextEncoding"))
            return readTextEncoding(reader);
        
        throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
    }
    
    
    /**
     * Dispatcher method for writing classes derived from AbstractEncoding
     */
    public void writeAbstractEncoding(XMLStreamWriter writer, DataEncoding bean) throws XMLStreamException
    {
        if (bean instanceof BinaryEncoding)
            writeBinaryEncoding(writer, (BinaryEncoding)bean);
        else if (bean instanceof XMLEncoding)
            writeXMLEncoding(writer, (XMLEncoding)bean);
        else if (bean instanceof TextEncoding)
            writeTextEncoding(writer, (TextEncoding)bean);
    }
    
    
    /**
     * Read method for XMLEncoding elements
     */
    public XMLEncoding readXMLEncoding(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "XMLEncoding");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readXMLEncodingType(reader);
    }
    
    
    /**
     * Write method for XMLEncoding element
     */
    public void writeXMLEncoding(XMLStreamWriter writer, XMLEncoding bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "XMLEncoding");
        this.writeNamespaces(writer);
        this.writeXMLEncodingType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for TextEncoding elements
     */
    public TextEncoding readTextEncoding(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "TextEncoding");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readTextEncodingType(reader);
    }
    
    
    /**
     * Write method for TextEncoding element
     */
    public void writeTextEncoding(XMLStreamWriter writer, TextEncoding bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "TextEncoding");
        this.writeNamespaces(writer);
        this.writeTextEncodingType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Dispatcher method for reading elements derived from AbstractSWE
     */
    public AbstractSWE readAbstractSWE(XMLStreamReader reader) throws XMLStreamException
    {
        String localName = reader.getName().getLocalPart();
        
        if (localName.equals("DataRecord"))
            return readDataRecord(reader);
        else if (localName.equals("Vector"))
            return readVector(reader);
        else if (localName.equals("DataArray"))
            return readDataArray(reader);
        else if (localName.equals("Matrix"))
            return readMatrix(reader);
        else if (localName.equals("DataStream"))
            return readDataStream(reader);
        else if (localName.equals("Block"))
            return readBlock(reader);
        else if (localName.equals("BinaryEncoding"))
            return readBinaryEncoding(reader);
        else if (localName.equals("Component"))
            return readComponent(reader);
        else if (localName.equals("DataChoice"))
            return readDataChoice(reader);
        else if (localName.equals("Count"))
            return readCount(reader);
        else if (localName.equals("CategoryRange"))
            return readCategoryRange(reader);
        else if (localName.equals("QuantityRange"))
            return readQuantityRange(reader);
        else if (localName.equals("Time"))
            return readTime(reader);
        else if (localName.equals("TimeRange"))
            return readTimeRange(reader);
        else if (localName.equals("Boolean"))
            return readBoolean(reader);
        else if (localName.equals("Text"))
            return readText(reader);
        else if (localName.equals("Category"))
            return readCategory(reader);
        else if (localName.equals("Quantity"))
            return readQuantity(reader);
        else if (localName.equals("CountRange"))
            return readCountRange(reader);
        else if (localName.equals("NilValues"))
            return readNilValues(reader);
        else if (localName.equals("AllowedTokens"))
            return readAllowedTokens(reader);
        else if (localName.equals("AllowedValues"))
            return readAllowedValues(reader);
        else if (localName.equals("AllowedTimes"))
            return readAllowedTimes(reader);
        else if (localName.equals("XMLEncoding"))
            return readXMLEncoding(reader);
        else if (localName.equals("TextEncoding"))
            return readTextEncoding(reader);
        
        throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
    }
    
    
    /**
     * Dispatcher method for reading elements derived from AbstractSWEIdentifiable
     */
    public AbstractSWEIdentifiable readAbstractSWEIdentifiable(XMLStreamReader reader) throws XMLStreamException
    {
        String localName = reader.getName().getLocalPart();
        
        if (localName.equals("DataRecord"))
            return readDataRecord(reader);
        else if (localName.equals("Vector"))
            return readVector(reader);
        else if (localName.equals("DataArray"))
            return readDataArray(reader);
        else if (localName.equals("Matrix"))
            return readMatrix(reader);
        else if (localName.equals("DataStream"))
            return readDataStream(reader);
        else if (localName.equals("DataChoice"))
            return readDataChoice(reader);
        else if (localName.equals("Count"))
            return readCount(reader);
        else if (localName.equals("CategoryRange"))
            return readCategoryRange(reader);
        else if (localName.equals("QuantityRange"))
            return readQuantityRange(reader);
        else if (localName.equals("Time"))
            return readTime(reader);
        else if (localName.equals("TimeRange"))
            return readTimeRange(reader);
        else if (localName.equals("Boolean"))
            return readBoolean(reader);
        else if (localName.equals("Text"))
            return readText(reader);
        else if (localName.equals("Category"))
            return readCategory(reader);
        else if (localName.equals("Quantity"))
            return readQuantity(reader);
        else if (localName.equals("CountRange"))
            return readCountRange(reader);
        
        throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
    }
}
