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

package org.vast.swe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.opengis.IDateTime;
import net.opengis.OgcProperty;
import net.opengis.swe.v20.AllowedTimes;
import net.opengis.swe.v20.AllowedTokens;
import net.opengis.swe.v20.AllowedValues;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.ByteEncoding;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.CategoryRange;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.CountRange;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataConstraint;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.HasCodeSpace;
import net.opengis.swe.v20.HasConstraints;
import net.opengis.swe.v20.HasRefFrames;
import net.opengis.swe.v20.HasUom;
import net.opengis.swe.v20.Matrix;
import net.opengis.swe.v20.NilValue;
import net.opengis.swe.v20.NilValues;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.QuantityRange;
import net.opengis.swe.v20.RangeComponent;
import net.opengis.swe.v20.SimpleComponent;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.TimeRange;
import net.opengis.swe.v20.Vector;
import org.vast.cdm.common.DataSource;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.data.DataList;
import org.vast.data.DataValue;
import org.vast.data.TextEncodingImpl;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.xlink.XlinkUtils;
import org.vast.xml.DOMHelper;
import org.vast.xml.IXMLWriterDOM;
import org.vast.xml.XMLWriterException;
import org.w3c.dom.Element;


/**
 * <p>
 * Writes CDM Components structures including DataValue,
 * DataGroup, DataArray, DataChoice to an XML DOM tree.
 * This class is not thread-safe.
 * 11-2014: Modified to work with new auto-generated SWE Common classes
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Feb 29, 2008
 * @deprecated use new bindings {@link org.vast.swe.SWEStaxBindings} instead
 */
public class SweComponentWriterV20 implements IXMLWriterDOM<DataComponent>
{
	protected String SWE_NS = OGCRegistry.getNamespaceURI(SWECommonUtils.SWE, "2.0");
    protected SweEncodingWriterV20 encodingWriter = new SweEncodingWriterV20();
    protected SWEDataTypeUtils dataTypeUtils = new SWEDataTypeUtils();
    protected Map<String, Object> objectIds;
    protected boolean keepHref = true;
    
    
    public SweComponentWriterV20()
    {
        objectIds = new HashMap<String, Object>();
    }
    
        
    public Element write(DOMHelper dom, DataComponent dataComponents) throws XMLWriterException
    {
    	return writeComponent(dom, dataComponents, true);
    }
    
    
    public Element writeComponent(DOMHelper dom, DataComponent dataComponent, boolean writeInlineData) throws XMLWriterException
    {
        dom.addUserPrefix("swe", SWE_NS);
        dom.addUserPrefix("xlink", OGCRegistry.getNamespaceURI(OGCRegistry.XLINK));
        Element newElt = null;
            
        // soft or hard typed record component
        if (dataComponent instanceof DataRecord)
            newElt = writeDataRecord(dom, (DataRecord)dataComponent, writeInlineData);
        else if (dataComponent instanceof Vector)
            newElt = writeVector(dom, (Vector)dataComponent, writeInlineData);
        else if (dataComponent instanceof RangeComponent)
            newElt = writeDataRange(dom, (RangeComponent)dataComponent, writeInlineData);
        else if (dataComponent instanceof DataChoice)
        	newElt = writeDataChoice(dom, (DataChoice)dataComponent, writeInlineData);
        else if (dataComponent instanceof Matrix)
            newElt = writeMatrix(dom, (Matrix)dataComponent, writeInlineData);
        else if (dataComponent instanceof DataList)
            newElt = writeDataList(dom, (DataList)dataComponent, writeInlineData);
        else if (dataComponent instanceof DataArray)
            newElt = writeDataArray(dom, (DataArray)dataComponent, writeInlineData);        
        else if (dataComponent instanceof DataValue)
            newElt = writeDataValue(dom, (DataValue)dataComponent, writeInlineData);

        return newElt;
    }
    
    
    public Element addComponentProperty(DOMHelper dom, Element parentElt, String propEltPath, OgcProperty<?> prop, boolean writeInlineData) throws XMLWriterException
    {
        Element propElt = dom.addElement(parentElt, propEltPath);
        
        // write property name
        if (prop.getName() != null)
            propElt.setAttribute("name", prop.getName());
        
        // handle xlink
        XlinkUtils.writeXlinkAttributes(dom, propElt, prop);
           
        // write value if no xlink is set
        if (!prop.hasHref() || !keepHref)
        {
            propElt.removeAttributeNS(dom.getXmlDocument().getNSUri("xlink"), "href");
            Element valueElt = writeComponent(dom, (DataComponent)prop.getValue(), writeInlineData);
            propElt.appendChild(valueElt);
        }
        
        return propElt;
    }


    /**
     * Writes a generic (soft-typed) DataRecord structure and all fields
     * @param dom
     * @param dataGroup
     * @return
     * @throws XMLWriterException
     */
    private Element writeDataRecord(DOMHelper dom, DataRecord dataGroup, boolean writeInlineData) throws XMLWriterException
    {
    	Element dataGroupElt = dom.createElement("swe:DataRecord");
        
    	// write common stuffs
        writeBaseProperties(dom, dataGroup, dataGroupElt);
        writeCommonAttributes(dom, dataGroup, dataGroupElt);
        
        // write each field
        int listSize = dataGroup.getNumFields();
        for (int i=0; i<listSize; i++)
        {
        	OgcProperty<?> prop = dataGroup.getFieldList().getProperty(i);
        	addComponentProperty(dom, dataGroupElt, "+swe:field", prop, writeInlineData);
        }

        return dataGroupElt;
    }
    
    
    /**
     * Writes a Vector structure and all its coordinates
     * @param dom
     * @param vector
     * @return
     * @throws XMLWriterException
     */
    private Element writeVector(DOMHelper dom, Vector vector, boolean writeInlineData) throws XMLWriterException
    {
    	Element dataGroupElt = dom.createElement("swe:Vector");
        
    	// write common stuffs
        writeBaseProperties(dom, vector, dataGroupElt);
        writeCommonAttributes(dom, vector, dataGroupElt);
        
        // write each coordinate
        int listSize = vector.getNumCoordinates();
        for (int i=0; i<listSize; i++)
        {
            OgcProperty<?> prop = vector.getCoordinateList().getProperty(i);
            addComponentProperty(dom, dataGroupElt, "+swe:coordinate", prop, writeInlineData);
        }

        return dataGroupElt;
    }
    
    
    /**
     * Writes generic (soft-typed) DataChoice structure and all items
     * @param dom
     * @param dataChoice
     * @return
     * @throws XMLWriterException
     */
    private Element writeDataChoice(DOMHelper dom, DataChoice dataChoice, boolean writeInlineData) throws XMLWriterException
    {
        Element dataChoiceElt = dom.createElement("swe:DataChoice");
        
        // write common stuffs
        writeBaseProperties(dom, dataChoice, dataChoiceElt);
        writeCommonAttributes(dom, dataChoice, dataChoiceElt);
        
        // write each choice item
        int listSize = dataChoice.getNumItems();
        for (int i=0; i<listSize; i++)
        {
            OgcProperty<?> prop = dataChoice.getItemList().getProperty(i);
            addComponentProperty(dom, dataChoiceElt, "+swe:item", prop, false);
        }       

        return dataChoiceElt;
    }
        
    
    /**
     * Writes generic (soft-typed) DataArray structure 
     * @param dom
     * @param dataArray
     * @return
     * @throws XMLWriterException
     */
    private Element writeDataArray(DOMHelper dom, DataArray dataArray, boolean writeInlineData) throws XMLWriterException
    {
        Element dataArrayElt = dom.createElement("swe:DataArray");
		writeArrayContent(dom, dataArray, dataArrayElt, writeInlineData);
        return dataArrayElt;
    }
    
    
    /**
     * Writes a Matrix structure 
     * @param dom
     * @param dataArray
     * @return
     * @throws XMLWriterException
     */
    private Element writeMatrix(DOMHelper dom, Matrix matrix, boolean writeInlineData) throws XMLWriterException
    {
        Element dataArrayElt = dom.createElement("swe:Matrix");
		writeArrayContent(dom, matrix, dataArrayElt, writeInlineData);
        return dataArrayElt;
    }
    
    
    private void writeArrayContent(DOMHelper dom, DataArray dataArray, Element arrayElt, boolean writeInlineData) throws XMLWriterException
    {
    	// write common stuffs
        writeBaseProperties(dom, dataArray, arrayElt);
    	writeCommonAttributes(dom, dataArray, arrayElt);
    	
    	// make sure we disable writing data inline
        boolean saveWriteInlineDataState = writeInlineData;
        writeInlineData = false;
        
    	// write elementCount
        addComponentProperty(dom, arrayElt, "swe:elementCount", dataArray.getElementCountProperty(), true);
                	
        // write array component
        addComponentProperty(dom, arrayElt, "swe:elementType", dataArray.getElementTypeProperty(), false);
    	
    	// restore state of writeInlineData
        writeInlineData = saveWriteInlineDataState;
                
        // write tuple values if present
        if (dataArray.isSetValues() && writeInlineData)
        {
        	DataEncoding dataEncoding = dataArray.getEncoding();
        	if (dataEncoding == null)
        		dataEncoding = new TextEncodingImpl("\n", " ");
        	
        	Element encPropElt = dom.addElement(arrayElt, "swe:encoding");
        	Element encElt = encodingWriter.write(dom, dataEncoding);
        	encPropElt.appendChild(encElt);
        	
        	Element tupleValuesElt = writeArrayValues(dom, dataArray, dataArray.getElementType(), dataEncoding);
        	arrayElt.appendChild(tupleValuesElt);
        }
    }
    
    
    /**
     * Writes generic (soft-typed) DataList structure 
     * @param dom
     * @param dataArray
     * @return
     * @throws XMLWriterException
     */
    private Element writeDataList(DOMHelper dom, DataList dataList, boolean writeInlineData) throws XMLWriterException
    {
        Element streamElt = dom.createElement("swe:DataStream");
        
        // write common stuffs
        writeBaseProperties(dom, dataList, streamElt);
        writeCommonAttributes(dom, dataList, streamElt);
                
        // write elementCount
        int arraySize = dataList.getComponentCount();
        if (arraySize > 0)
            dom.setElementValue(streamElt, "swe:elementCount/swe:Count/swe:value", Integer.toString(arraySize));        
        
        // write array component
        addComponentProperty(dom, streamElt, "swe:elementType", dataList.getElementTypeProperty(), false);
        
        // write encoding
        DataEncoding encoding = dataList.getEncoding();        
        Element encPropElt = dom.addElement(streamElt, "swe:encoding");
        Element encElt = encodingWriter.write(dom, encoding);
        encPropElt.appendChild(encElt);
        
        // write values
        if (dataList instanceof SWEData && (((SWEData)dataList).getDataSource() instanceof DataSourceURI))
        {
            DataSource dataSrc = ((SWEData)dataList).getDataSource();
            String uri = ((DataSourceURI)dataSrc).streamUri;
            dom.setAttributeValue(streamElt, "swe:values/xlink:href", uri);
        }        
        else if (dataList.getData() != null && writeInlineData)
        {   
            Element tupleValuesElt = writeArrayValues(dom, dataList, dataList.getElementType(), encoding);
            streamElt.appendChild(tupleValuesElt);
        }
        
        return streamElt;
    }
    
    
    /**
     * Writes any scalar component
     * @param dom
     * @param dataValue
     * @return
     * @throws XMLWriterException
     */
    private Element writeDataValue(DOMHelper dom, DataValue dataValue, boolean writeInlineData) throws XMLWriterException
    {
        // create right element
        String eltName = getElementName(dataValue);
        Element dataValueElt = dom.createElement(eltName);
        
        // write all properties
        writeCommonAttributes(dom, dataValue, dataValueElt);
        writeBaseProperties(dom, dataValue, dataValueElt);
        writeQuality(dom, dataValue, dataValueElt);
        writeNilValues(dom, dataValue, dataValueElt);
    	writeUom(dom, dataValue, dataValueElt);
    	writeCodeSpace(dom, dataValue, dataValueElt);
    	writeConstraints(dom, dataValue, dataValueElt);
    	
        // write value if necessary    	
    	if (writeInlineData)
    	{
            String val = dataTypeUtils.getStringValue(dataValue);
            if (val != null)
                dom.setElementValue(dataValueElt, "swe:value", val);
    	}
    	
        return dataValueElt;
    }
    
    
    /**
     * Writes a range which is special DataGroup
     * @param dom
     * @param dataGroup
     * @return
     * @throws XMLWriterException
     */
    private Element writeDataRange(DOMHelper dom, RangeComponent range, boolean writeInlineData) throws XMLWriterException
    {        
        // create right element
        String eltName = getElementName(range);
        Element dataValueElt = dom.createElement(eltName);
        
        // write all properties
        writeCommonAttributes(dom, range, dataValueElt);
        writeBaseProperties(dom, range, dataValueElt);
        writeQuality(dom, range, dataValueElt);
        writeNilValues(dom, range, dataValueElt);
        writeUom(dom, range, dataValueElt);
        writeCodeSpace(dom, range, dataValueElt);
        writeConstraints(dom, range, dataValueElt);
        
        // write value if necessary
        if (writeInlineData)
        {
            String val = getValuePairAsString(range);
            if (val != null)
                dom.setElementValue(dataValueElt, "swe:value", val);
        }
        
        return dataValueElt;
    }
    
    
    private String getValuePairAsString(RangeComponent range)
    {
        DataBlock data = ((DataComponent)range).getData();
        if (data == null)
            return null;
        
        StringBuffer buf = new StringBuffer();
       
        if (range instanceof TimeRange && Time.ISO_TIME_UNIT.equals(((TimeRange)range).getUom().getHref()))
        {
            buf.append(dataTypeUtils.getDoubleOrTimeAsString(data.getDoubleValue(0), true));
            buf.append(' ');
            buf.append(dataTypeUtils.getDoubleOrTimeAsString(data.getDoubleValue(1), true));
        }
        else if (range instanceof TimeRange || range instanceof QuantityRange)
        {
            buf.append(dataTypeUtils.getDoubleOrTimeAsString(data.getDoubleValue(0), false));
            buf.append(' ');
            buf.append(dataTypeUtils.getDoubleOrTimeAsString(data.getDoubleValue(1), false));
        }
        else
        {
            buf.append(data.getStringValue(0));
            buf.append(' ');
            buf.append(data.getStringValue(1));
        }
        
        return buf.toString();
    }
    
    
    private String getElementName(SimpleComponent dataValue)
    {
    	String eltName = null;
    	
    	if (dataValue instanceof RangeComponent)
    	{
    	    if (dataValue instanceof QuantityRange)
                eltName = "swe:QuantityRange";
    	    else if (dataValue instanceof TimeRange)
                eltName = "swe:TimeRange";
            else if (dataValue instanceof CategoryRange)
                eltName = "swe:CategoryRange";
            else if (dataValue instanceof CountRange)
                eltName = "swe:CountRange";
    	}
    	else
    	{
            if (dataValue instanceof net.opengis.swe.v20.Boolean)
                eltName = "swe:Boolean";
            else if (dataValue instanceof Quantity)
                eltName = "swe:Quantity";
            else if (dataValue instanceof Time)
                eltName = "swe:Time";
            else if (dataValue instanceof Category)
                eltName = "swe:Category";
            else if (dataValue instanceof Count)
                eltName = "swe:Count";
            else if (dataValue instanceof Text)
                eltName = "swe:Text";
    	}
    	
        return eltName;
    }
    
    
    public void writeName(Element propertyElement, String name)
    {
        if (name != null)
            propertyElement.setAttribute("name", name);
    }
    
    
    private void writeBaseProperties(DOMHelper dom, DataComponent dataComponent, Element dataComponentElt) throws XMLWriterException
    {
    	// id
    	String id = dataComponent.getId();
    	if (id != null)
    		dom.setAttributeValue(dataComponentElt, "@id", id);
    	
    	// label
    	String name = dataComponent.getLabel();
    	if (name != null)
    		dom.setElementValue(dataComponentElt, "swe:label", name);
    	
    	// description
    	String description = dataComponent.getDescription();
    	if (description != null)
    		dom.setElementValue(dataComponentElt, "swe:description", description);
    }
    
    
    private void writeCommonAttributes(DOMHelper dom, DataComponent dataComponent, Element dataComponentElt) throws XMLWriterException
    {
    	// definition URI
        String defUri = dataComponent.getDefinition();
        if (defUri != null)
            dataComponentElt.setAttribute("definition", defUri);
        
        // updatable
        if (dataComponent.isSetUpdatable())
            dataComponentElt.setAttribute("updatable", Boolean.toString(dataComponent.getUpdatable()));
        
        // optional
        if (dataComponent.isSetOptional())
            dataComponentElt.setAttribute("optional", Boolean.toString(dataComponent.getOptional()));
        
        if (dataComponent instanceof HasRefFrames)
        {
            // reference frame
            String refFrame = ((HasRefFrames)dataComponent).getReferenceFrame();
            if (refFrame != null)
                dataComponentElt.setAttribute("referenceFrame", refFrame);
            
            // local frame
            String localFrame = ((HasRefFrames)dataComponent).getLocalFrame();
            if (localFrame != null)
                dataComponentElt.setAttribute("localFrame", localFrame);
            
            if (dataComponent instanceof Time)
            {
                // reference time
                if (((Time)dataComponent).isSetReferenceTime())
                {
                    double refTime = ((Time)dataComponent).getReferenceTime().getAsDouble();
                    dataComponentElt.setAttribute("referenceTime", dataTypeUtils.getDoubleOrTimeAsString(refTime, true));
                }
            }
        }
        else if (dataComponent instanceof SimpleComponent)
        {
            // reference frame
            String refFrame = ((SimpleComponent)dataComponent).getReferenceFrame();
            if (refFrame != null)
                dataComponentElt.setAttribute("referenceFrame", refFrame);
            
            // axis code attribute
            String axisCode = ((SimpleComponent)dataComponent).getAxisID();
            if (axisCode != null)
                dataComponentElt.setAttribute("axisID", axisCode);
        }
    }
    
    
    /**
     * Writes encoded array values to the values element
     * TODO add support for XML encoding
     * @param dom
     * @param array
     * @param elementType
     * @param encoding
     * @return
     * @throws XMLWriterException
     */
    private Element writeArrayValues(DOMHelper dom, DataComponent array, DataComponent elementType, DataEncoding encoding) throws XMLWriterException
    {
        Element tupleValuesElement = dom.createElement("swe:values");
        ByteArrayOutputStream os = new ByteArrayOutputStream(array.getData().getAtomCount()*10);
        
        // force base64 if byte encoding is raw
        if (encoding instanceof BinaryEncoding)
        {
            if (((BinaryEncoding)encoding).getByteEncoding() == ByteEncoding.RAW)
                ((BinaryEncoding)encoding).setByteEncoding(ByteEncoding.BASE_64);
        }
        
        // write values with proper encoding to byte array
        try
        {
            DataStreamWriter writer = SWEFactory.createDataWriter(encoding);
            writer.setDataComponents(elementType.copy());
            writer.setOutput(os);
            
            for (int i = 0; i < array.getComponentCount(); i++)
            {
                DataBlock nextBlock = array.getComponent(i).getData();
                writer.write(nextBlock);
            }
            
            writer.flush();
        }
        catch (IOException e)
        {
            throw new XMLWriterException("Error while writing array values", e);
        }
        
        // copy byte array into element text
        tupleValuesElement.setTextContent(os.toString());        
        return tupleValuesElement;
    }
    
    
    private void writeUom(DOMHelper dom, SimpleComponent dataValue, Element dataValueElt) throws XMLWriterException
    {
        if (!(dataValue instanceof HasUom))
            return;
        
        String uomCode = ((HasUom)dataValue).getUom().getCode();
        String uomUri = ((HasUom)dataValue).getUom().getHref();
    	
        if (uomCode != null)
        	dom.setAttributeValue(dataValueElt, "swe:uom/@code", uomCode);
        else if (uomUri != null)
        	dom.setAttributeValue(dataValueElt, "swe:uom/@xlink:href", uomUri);
    }
    
    
    private void writeCodeSpace(DOMHelper dom, SimpleComponent dataValue, Element categoryElt) throws XMLWriterException
    {
        if (!(dataValue instanceof HasCodeSpace))
            return;
        
        String codeSpace = ((HasCodeSpace)dataValue).getCodeSpace();    	
        if (codeSpace != null)
        	dom.setAttributeValue(categoryElt, "swe:codeSpace/@xlink:href", codeSpace);
    }
    
    
    @SuppressWarnings("rawtypes")
    private void writeConstraints(DOMHelper dom, SimpleComponent dataValue, Element dataValueElt) throws XMLWriterException
    {
    	if (!(dataValue instanceof HasConstraints))
    	    return;
        
        if (((HasConstraints)dataValue).isSetConstraint())
    	{
            DataConstraint constraints = ((HasConstraints)dataValue).getConstraint();
            Element constraintElt = dom.addElement(dataValueElt, "swe:constraint");
    		
            boolean writeIntegers = (dataValue instanceof Count);
    		    		
    		if (constraints instanceof AllowedTokens)
    		{
    		    Element allowedTokensElt = dom.addElement(constraintElt, "swe:AllowedTokens");
    		    writeTokenEnumConstraints(dom, ((AllowedTokens)constraints).getValueList(), allowedTokensElt);
    		    
    		    if (((AllowedTokens)constraints).isSetPattern())
    		        dom.setElementValue(allowedTokensElt, "swe:pattern", ((AllowedTokens)constraints).getPattern());
    		}
    		
    		else if (constraints instanceof AllowedValues)
            {
                Element allowedValuesElt = dom.addElement(constraintElt, "swe:AllowedValues");
                
                for (double val: ((AllowedValues)constraints).getValueList())
                    writeNumberEnumConstraint(dom, val, allowedValuesElt, writeIntegers, false);
                
                for (double[] interval: ((AllowedValues)constraints).getIntervalList())
                    writeIntervalConstraint(dom, interval[0], interval[1], allowedValuesElt, writeIntegers, false);
                
                if (((AllowedValues)constraints).isSetSignificantFigures())
                {
                    String digits = Integer.toString(((AllowedValues)constraints).getSignificantFigures());
                    dom.setElementValue(allowedValuesElt, "swe:significantFigures", digits);
                }
            }
    		
    		else if (constraints instanceof AllowedTimes)
            {
    		    String uomUri = ((HasUom)dataValue).getUom().getHref();
                boolean useIso = (uomUri != null && uomUri.equals(Time.ISO_TIME_UNIT));    		    
    		    Element allowedValuesElt = dom.addElement(constraintElt, "swe:AllowedTimes");
    		    
    		    for (IDateTime time: ((AllowedTimes)constraints).getValueList())
                    writeNumberEnumConstraint(dom, time.getAsDouble(), allowedValuesElt, writeIntegers, useIso);                
                
                for (IDateTime[] interval: ((AllowedTimes)constraints).getIntervalList())
                    writeIntervalConstraint(dom, interval[0].getAsDouble(), interval[1].getAsDouble(), allowedValuesElt, writeIntegers, useIso);
                
                if (((AllowedTimes)constraints).isSetSignificantFigures())
                {
                    String digits = Integer.toString(((AllowedTimes)constraints).getSignificantFigures());
                    dom.setElementValue(allowedValuesElt, "swe:significantFigures", digits);
                }
            }
    	}
    }
    
    
    private void writeIntervalConstraint(DOMHelper dom, double min, double max, Element constraintElt, boolean writeIntegers, boolean useIso) throws XMLWriterException
    {
    	Element intervalElt = dom.addElement(constraintElt, "+swe:interval");
    	
    	String minText, maxText;
    	if (writeIntegers)
    	{
    	    minText = Integer.toString((int)min);
    	    maxText = Integer.toString((int)max);
    	}
    	else
    	{
    	    minText = dataTypeUtils.getDoubleOrTimeAsString(min, useIso);
    	    maxText = dataTypeUtils.getDoubleOrTimeAsString(max, useIso);
    	}
    	
    	dom.setElementValue(intervalElt, minText + " " + maxText);
    }
    
    
    private void writeNumberEnumConstraint(DOMHelper dom, double val, Element constraintElt, boolean writeIntegers, boolean useIso) throws XMLWriterException
    {
    	if (writeIntegers)
    	    dom.setElementValue(constraintElt, "+swe:value", Integer.toString((int)val));
    	else
    	    dom.setElementValue(constraintElt, "+swe:value", dataTypeUtils.getDoubleOrTimeAsString(val, useIso));
    }
    
    
    private void writeTokenEnumConstraints(DOMHelper dom, List<String> valueList, Element constraintElt) throws XMLWriterException
    {
    	for (String val: valueList)
    		dom.setElementValue(constraintElt, "+swe:value", val);
    }
    
    
    private void writeQuality(DOMHelper dom, SimpleComponent dataValue, Element dataValueElt) throws XMLWriterException
    {
    	int listSize = dataValue.getNumQualitys();
        for (int i=0; i<listSize; i++)
            addComponentProperty(dom, dataValueElt, "+swe:quality", dataValue.getQualityList().getProperty(i), true);
    }
    
    
    private void writeNilValues(DOMHelper dom, SimpleComponent dataValue, Element dataValueElt) throws XMLWriterException
    {
        if (!dataValue.isSetNilValues())
            return;
        
        Element nilPropElt = dom.addElement(dataValueElt, "swe:nilValues");
        OgcProperty<NilValues> nilProp = dataValue.getNilValuesProperty();
        
        if (nilProp.hasHref())
        {
            XlinkUtils.writeXlinkAttributes(dom, nilPropElt, nilProp);
        }
        else
        {
            Element nilValuesElt = dom.addElement(nilPropElt, "swe:NilValues");
            NilValues nilValues = nilProp.getValue();
                    
            // id
            if (nilValues.getId() != null)
                dom.setAttributeValue(nilValuesElt, "id", nilValues.getId());
            
            // reason -> reserved value mapping
            for (NilValue nil: nilValues.getNilValueList())
            {
                String token = null;
                Object nilObj = nil.getValue();
                if (nilObj instanceof Double)
                    token = dataTypeUtils.getDoubleOrTimeAsString((Double)nilObj, false);
                else
                    token = nilObj.toString();
                
                Element nilElt = dom.addElement(nilValuesElt, "+swe:nilValue");
                dom.setAttributeValue(nilElt, "reason", nil.getReason());
                dom.setElementValue(nilElt, token);
            }
        }
    }
    
    
    public void setExpandHref(boolean expandHref)
    {
        this.keepHref = !expandHref;
    }
}
