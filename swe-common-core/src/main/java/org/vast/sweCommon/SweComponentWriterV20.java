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

package org.vast.sweCommon;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.namespace.QName;
import org.vast.cdm.common.AsciiEncoding;
import org.vast.cdm.common.BinaryEncoding;
import org.vast.cdm.common.BinaryEncoding.ByteEncoding;
import org.vast.cdm.common.DataBlock;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataComponentWriter;
import org.vast.cdm.common.DataConstraint;
import org.vast.cdm.common.DataEncoding;
import org.vast.cdm.common.DataSource;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.cdm.common.DataType;
import org.vast.data.ConstraintList;
import org.vast.data.DataArray;
import org.vast.data.DataChoice;
import org.vast.data.DataGroup;
import org.vast.data.DataList;
import org.vast.data.DataValue;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.xlink.CachedReference;
import org.vast.ogc.xlink.XlinkUtils;
import org.vast.unit.Unit;
import org.vast.util.DateTimeFormat;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLWriterException;
import org.w3c.dom.Element;


/**
 * <p>
 * Writes CDM Components structures including DataValue,
 * DataGroup, DataArray, DataChoice to an XML DOM tree.
 * This class is not thread-safe.
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin
 * @since Feb 29, 2008
 * @version 1.0
 */
public class SweComponentWriterV20 implements DataComponentWriter
{
	protected String SWE_NS = OGCRegistry.getNamespaceURI(SWECommonUtils.SWE, "2.0");
    protected SweEncodingWriterV20 encodingWriter = new SweEncodingWriterV20();
    protected Map<String, Object> objectIds;
    protected boolean keepHref = true;
    
    
    public SweComponentWriterV20()
    {
        objectIds = new HashMap<String, Object>();
    }
    
        
    public Element writeComponent(DOMHelper dom, DataComponent dataComponents) throws XMLWriterException
    {
    	return writeComponent(dom, dataComponents, true);
    }
    
    
    public Element writeComponent(DOMHelper dom, DataComponent dataComponent, boolean writeInlineData) throws XMLWriterException
    {
        dom.addUserPrefix("swe", SWE_NS);
        dom.addUserPrefix("xlink", OGCRegistry.getNamespaceURI(OGCRegistry.XLINK));
        
        Element newElt = null;
        QName compQName = (QName)dataComponent.getProperty(SweConstants.COMP_QNAME);
        Object refFrame = dataComponent.getProperty(SweConstants.REF_FRAME);
            
        // soft or hard typed record component
        if (dataComponent instanceof DataGroup)
        {
            if (refFrame != null || (compQName != null && compQName.getLocalPart().equals("Vector")))
                newElt = writeVector(dom, (DataGroup)dataComponent, writeInlineData);
            else if (compQName != null && compQName.getLocalPart().endsWith("Range"))
                newElt = writeDataRange(dom, (DataGroup)dataComponent, compQName, writeInlineData);        	
        	else
        		newElt = writeDataRecord(dom, (DataGroup)dataComponent, writeInlineData);
        }
        
        // soft or hard typed choice component
        else if (dataComponent instanceof DataChoice)
        {
        	newElt = writeDataChoice(dom, (DataChoice)dataComponent, writeInlineData);
        }
        
        // soft or hard typed array component
        else if (dataComponent instanceof DataArray)
        {
        	if (refFrame != null || (compQName != null && compQName.getLocalPart().equals("Matrix")))
        		newElt = writeMatrix(dom, (DataArray)dataComponent, writeInlineData);
            else
            	newElt = writeDataArray(dom, (DataArray)dataComponent, writeInlineData);
        }
        else if (dataComponent instanceof DataList)
        {
            newElt = writeDataList(dom, (DataList)dataComponent, writeInlineData);
        }
        else if (dataComponent instanceof DataValue)
        {
            newElt = writeDataValue(dom, (DataValue)dataComponent, writeInlineData);
        }

        return newElt;
    }
    
    
    /**
     * Write a property containing a component properly handling xlink
     * @param dom
     * @param dataComponent
     * @param propQname
     * @param writeInlineData
     * @return
     * @throws XMLWriterException
     */
    public Element addComponentProperty(DOMHelper dom, Element parentElt, String propEltPath, DataComponent dataComponent, boolean writeInlineData) throws XMLWriterException
    {
        Element propElt = dom.addElement(parentElt, propEltPath);
        CachedReference<?> xlinkOptions = (CachedReference<?>)dataComponent.getProperty(SweConstants.COMP_XLINK);
        
        if (xlinkOptions != null && keepHref)
        {
            XlinkUtils.writeXlinkAttributes(dom, propElt, xlinkOptions);
        }
        else
        {
            Element qualElt = writeComponent(dom, dataComponent, writeInlineData);
            propElt.appendChild(qualElt);
        }
        
        return propElt;
    }


    /**
     * Writes a generic (soft-typed) DataRecord structure and all fields
     * @param dom
     * @param dataGroup
     * @return
     * @throws CDMException
     */
    private Element writeDataRecord(DOMHelper dom, DataGroup dataGroup, boolean writeInlineData) throws XMLWriterException
    {
    	Element dataGroupElt = dom.createElement("swe:DataRecord");
        
    	// write common stuffs
        writeBaseProperties(dom, dataGroup, dataGroupElt);
        writeCommonAttributes(dom, dataGroup, dataGroupElt);
        
        // write each field
        int groupSize = dataGroup.getComponentCount();
        for (int i=0; i<groupSize; i++)
        {
        	DataComponent component = dataGroup.getComponent(i);
        	
        	// add field property
        	Element fieldElt = addComponentProperty(dom, dataGroupElt, "+swe:field", component, writeInlineData);
        	fieldElt.setAttribute("name", component.getName());
        }

        return dataGroupElt;
    }
    
    
    /**
     * Writes a Vector structure and all its coordinates
     * @param dom
     * @param dataGroup
     * @return
     * @throws CDMException
     */
    private Element writeVector(DOMHelper dom, DataGroup dataGroup, boolean writeInlineData) throws XMLWriterException
    {
    	Element dataGroupElt = dom.createElement("swe:Vector");
        
    	// write common stuffs
        writeBaseProperties(dom, dataGroup, dataGroupElt);
        writeCommonAttributes(dom, dataGroup, dataGroupElt);
        
        // write each coordinate
        int groupSize = dataGroup.getComponentCount();
        for (int i=0; i<groupSize; i++)
        {
        	DataComponent component = dataGroup.getComponent(i);
        	Element fieldElt = dom.addElement(dataGroupElt, "+swe:coordinate");
        	fieldElt.setAttribute("name", component.getName());
    		
        	Element componentElt = writeComponent(dom, component, writeInlineData);
            fieldElt.appendChild(componentElt);
        }

        return dataGroupElt;
    }
    
    
    /**
     * Writes generic (soft-typed) DataChoice structure and all items
     * @param dom
     * @param dataChoice
     * @return
     * @throws CDMException
     */
    private Element writeDataChoice(DOMHelper dom, DataChoice dataChoice, boolean writeInlineData) throws XMLWriterException
    {
        Element dataChoiceElt = dom.createElement("swe:DataChoice");
        
        // write common stuffs
        writeBaseProperties(dom, dataChoice, dataChoiceElt);
        writeCommonAttributes(dom, dataChoice, dataChoiceElt);
        
        // write each choice item
        int listSize = dataChoice.getComponentCount();
        for (int i=0; i<listSize; i++)
        {
            DataComponent component = dataChoice.getComponent(i);            
            Element propElt = addComponentProperty(dom, dataChoiceElt, "+swe:item", component, false);
        	propElt.setAttribute("name", component.getName());        
        }       

        return dataChoiceElt;
    }
        
    
    /**
     * Writes generic (soft-typed) DataArray structure 
     * @param dom
     * @param dataArray
     * @return
     * @throws CDMException
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
     * @throws CDMException
     */
    private Element writeMatrix(DOMHelper dom, DataArray dataArray, boolean writeInlineData) throws XMLWriterException
    {
        Element dataArrayElt = dom.createElement("swe:Matrix");
		writeArrayContent(dom, dataArray, dataArrayElt, writeInlineData);
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
        int arraySize = dataArray.getComponentCount();
        Element eltCountElt = dom.addElement(arrayElt, "swe:elementCount");
        DataValue sizeData = dataArray.getSizeComponent();
        
        // case of variable size
        if (dataArray.isVariableSize())
        {
        	// case of implicit Count field
        	if (sizeData.getParent() == null)
        	{
        		Element countElt = writeDataValue(dom, sizeData, false);
        		eltCountElt.appendChild(countElt);
        	}
        	
        	// case of explicitly referenced field
        	else
        	{
	        	String sizeCompID = (String)sizeData.getProperty(SweConstants.ID);
	        	if (sizeCompID != null)
	        		dom.setAttributeValue(eltCountElt, "xlink:href", "#" + sizeCompID);
	        	else
	        		throw new XMLWriterException("Component used for storing variable array size MUST have an ID");
        	}
        }
        
        // case of fixed size
        else
        {
        	Element countElt = writeDataValue(dom, sizeData, true);
    		dom.setElementValue(countElt, "swe:value", Integer.toString(arraySize));
    		eltCountElt.appendChild(countElt);
        }
                	
        // write array component
        DataComponent arrayComponent = dataArray.getArrayComponent();
        Element propElt = addComponentProperty(dom, arrayElt, "swe:elementType", arrayComponent, false);
    	
    	// add name attribute if different from 'elementType'
        String fieldName = arrayComponent.getName();
        if (fieldName != null && !fieldName.equals("elementType"))
            propElt.setAttribute("name", fieldName);
    	
    	// restore state of writeInlineData
        writeInlineData = saveWriteInlineDataState;
                
        // write tuple values if present
        if (dataArray.getData() != null && writeInlineData)
        {
        	DataEncoding dataEncoding = (DataEncoding)dataArray.getProperty(SweConstants.ENCODING_TYPE);
        	if (dataEncoding == null)
        		dataEncoding = new AsciiEncoding("\n", " ");
        	
        	Element encPropElt = dom.addElement(arrayElt, "swe:encoding");
        	Element encElt = encodingWriter.writeEncoding(dom, dataEncoding);
        	encPropElt.appendChild(encElt);
        	
        	Element tupleValuesElt = writeArrayValues(dom, dataArray, arrayComponent, dataEncoding);
        	arrayElt.appendChild(tupleValuesElt);
        }
    }
    
    
    /**
     * Writes generic (soft-typed) DataList structure 
     * @param dom
     * @param dataArray
     * @return
     * @throws CDMException
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
        Element propElt = dom.addElement(streamElt, "swe:elementType");
        DataComponent listComponent = dataList.getListComponent();
        
        // add name attribute if different from 'elementType'
        String fieldName = listComponent.getName();
        if (fieldName != null && !fieldName.equals("elementType"))
            propElt.setAttribute("name", fieldName);
        
        // write steam component definition
        Element componentElt = writeComponent(dom, listComponent, false);
        propElt.appendChild(componentElt);
        
        // write encoding
        DataEncoding encoding = (DataEncoding)dataList.getProperty(SweConstants.ENCODING_TYPE);        
        Element encPropElt = dom.addElement(streamElt, "swe:encoding");
        Element encElt = encodingWriter.writeEncoding(dom, encoding);
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
            Element tupleValuesElt = writeArrayValues(dom, dataList, listComponent, encoding);
            streamElt.appendChild(tupleValuesElt);
        }
        
        return streamElt;
    }
    
    
    /**
     * Writes any scalar component
     * @param dom
     * @param dataValue
     * @return
     * @throws CDMException
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
            String val = getValueAsString(dataValue);
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
     * @throws CDMException
     */
    private Element writeDataRange(DOMHelper dom, DataGroup dataGroup, QName rangeQName, boolean writeInlineData) throws XMLWriterException
    {
    	DataValue min = (DataValue)dataGroup.getComponent(0);
    	DataValue max = (DataValue)dataGroup.getComponent(1);
    	
    	// create right range element
        Element rangeElt = dom.createElement("swe:" + rangeQName.getLocalPart());
        
        // write group properties
        writeCommonAttributes(dom, dataGroup, rangeElt);
        writeBaseProperties(dom, dataGroup, rangeElt);
        
        // extracted some from min component and not from DataGroup!!
        writeUom(dom, min, rangeElt);
        writeCodeSpace(dom, min, rangeElt);
    	writeConstraints(dom, min, rangeElt);
    	writeQuality(dom, min, rangeElt);
        
        // write min/max values if necessary
        if (writeInlineData && min.getData() != null && max.getData() != null)
        {
            String minVal = getValueAsString(min);
            String maxVal = getValueAsString(max);
            if (minVal != null && maxVal != null)
                dom.setElementValue(rangeElt, "swe:value", minVal + " " + maxVal);
        }
        
        return rangeElt;
    }
    
    
    private String getValueAsString(DataValue dataValue)
    {
        DataBlock data = dataValue.getData();
        if (data == null)
            return null;
        
        String val;
        String uomUri = (String)dataValue.getProperty(SweConstants.UOM_URI);
        if (uomUri != null && uomUri.equals(SweConstants.ISO_TIME_DEF))
            val = AsciiDataWriter.getDoubleAsString(data.getDoubleValue(), true);
        else if (dataValue.getDataType() == DataType.DOUBLE)
            val = AsciiDataWriter.getDoubleAsString(data.getDoubleValue(), false);
        else
            val = data.getStringValue();
        
        return val;
    }
    
    
    /**
     * Generates the right element name from info in data component
     * @param dataValue
     * @return
     */
    private String getElementName(DataValue dataValue)
    {
    	QName valueQName = (QName)dataValue.getProperty(SweConstants.COMP_QNAME);
    	    	
    	// return QName if specified
        if (valueQName != null)
	        return "swe:" + valueQName.getLocalPart();
        
        String def = (String)dataValue.getProperty(SweConstants.DEF_URI);
        String uomUri = (String)dataValue.getProperty(SweConstants.UOM_URI);
        boolean isoUnit = (uomUri != null) && uomUri.equals(SweConstants.ISO_TIME_DEF);
    	String eltName;
    	
        if (isoUnit || (def != null && def.toLowerCase().contains("time")))
            eltName = "swe:Time";
        else if (dataValue.getDataType() == DataType.BOOLEAN)
            eltName = "swe:Boolean";
        else if (dataValue.getDataType() == DataType.DOUBLE || dataValue.getDataType() == DataType.FLOAT || hasUom(dataValue))
            eltName = "swe:Quantity";
        else if (dataValue.getDataType() == DataType.ASCII_STRING || dataValue.getDataType() == DataType.UTF_STRING)
            eltName = "swe:Category";
        else
            eltName = "swe:Count";
        
        return eltName;
    }
    
    
    private boolean hasUom(DataValue dataValue)
    {
        if (dataValue.getProperty(SweConstants.UOM_CODE) != null)
            return true;
        
        if (dataValue.getProperty(SweConstants.UOM_URI) != null)
            return true;
        
        if (dataValue.getProperty(SweConstants.UOM_OBJ) != null)
            return true;
        
        return false;
    }
    
    
    public void writeName(Element propertyElement, String name)
    {
        if (name != null)
            propertyElement.setAttribute("name", name);
    }
    
    
    private void writeBaseProperties(DOMHelper dom, DataComponent dataComponent, Element dataComponentElt) throws XMLWriterException
    {
    	// id
    	String id = (String)dataComponent.getProperty(SweConstants.ID);
    	if (id != null)
    		dom.setAttributeValue(dataComponentElt, "@id", id);
    	
    	// label
    	String name = (String)dataComponent.getProperty(SweConstants.NAME);
    	if (name != null)
    		dom.setElementValue(dataComponentElt, "swe:label", name);
    	
    	// description
    	String description = (String)dataComponent.getProperty(SweConstants.DESC);
    	if (description != null)
    		dom.setElementValue(dataComponentElt, "swe:description", description);
    }
    
    
    private void writeCommonAttributes(DOMHelper dom, DataComponent dataComponent, Element dataComponentElt) throws XMLWriterException
    {
    	// definition URI
        Object defUri = dataComponent.getProperty(SweConstants.DEF_URI);
        if (defUri != null)
            dataComponentElt.setAttribute("definition", (String)defUri);
        
        // updatable
        Boolean updatable = (Boolean)dataComponent.getProperty(SweConstants.UPDATABLE);
        if (updatable != null)
            dataComponentElt.setAttribute("updatable", updatable.toString());
        
        // optional
        if (dataComponent.getParent() instanceof DataGroup)
        {
            Boolean optional = (Boolean)dataComponent.getProperty(SweConstants.OPTIONAL);
            if (optional != null)
                dataComponentElt.setAttribute("optional", optional.toString());
        }
        
        // crs
        String crs = (String)dataComponent.getProperty(SweConstants.CRS);
        if (crs != null)
            dataComponentElt.setAttribute("crs", crs);
        
        // reference frame
        String refFrame = (String)dataComponent.getProperty(SweConstants.REF_FRAME);
        if (refFrame != null)
            dataComponentElt.setAttribute("referenceFrame", refFrame);
        
        // reference time
        Double refTime = (Double)dataComponent.getProperty(SweConstants.REF_TIME);
        if (refTime != null)
            dataComponentElt.setAttribute("referenceTime", DateTimeFormat.formatIso(refTime, 0));
        
        // local frame
        Object locFrame = dataComponent.getProperty(SweConstants.LOCAL_FRAME);
        if (locFrame != null)
            dataComponentElt.setAttribute("localFrame", (String)locFrame);
        
        // axis code attribute
        Object axisCode = dataComponent.getProperty(SweConstants.AXIS_CODE);
        if (axisCode != null)
            dataComponentElt.setAttribute("axisID", (String)axisCode);
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
            if (((BinaryEncoding) encoding).byteEncoding == ByteEncoding.RAW)
                ((BinaryEncoding) encoding).byteEncoding = ByteEncoding.BASE64;
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
    
    
    private void writeUom(DOMHelper dom, DataValue dataValue, Element dataValueElt) throws XMLWriterException
    {
        String uomCode = (String)dataValue.getProperty(SweConstants.UOM_CODE);
        String uomUri = (String)dataValue.getProperty(SweConstants.UOM_URI);
        Unit uomObj = (Unit)dataValue.getProperty(SweConstants.UOM_OBJ);
    	
        if (uomCode != null)
        	dom.setAttributeValue(dataValueElt, "swe:uom/@code", uomCode);
        else if (uomUri != null)
        	dom.setAttributeValue(dataValueElt, "swe:uom/@xlink:href", uomUri);
        else if (uomObj != null)
        	dom.setAttributeValue(dataValueElt, "swe:uom/@code", uomObj.getExpression());
    }
    
    
    private void writeCodeSpace(DOMHelper dom, DataValue dataValue, Element categoryElt) throws XMLWriterException
    {
        String codeSpace = (String)dataValue.getProperty(SweConstants.DIC_URI);
    	
        if (codeSpace != null)
        	dom.setAttributeValue(categoryElt, "swe:codeSpace/@xlink:href", codeSpace);
    }
    
    
    private void writeConstraints(DOMHelper dom, DataValue dataValue, Element dataValueElt) throws XMLWriterException
    {
    	ConstraintList constraints = dataValue.getConstraints();
    	
    	if (constraints != null && !constraints.isEmpty())
    	{
    		Element constraintElt = dom.addElement(dataValueElt, "swe:constraint");
    		
    		boolean writeIntegers = (dataValue.getDataType() != DataType.DOUBLE && dataValue.getDataType() != DataType.FLOAT);
    		boolean isTime = dataValueElt.getLocalName().startsWith("Time");
    		String uomUri = (String)dataValue.getProperty(SweConstants.UOM_URI);
    		boolean useIso = (uomUri != null && uomUri.equals(SweConstants.ISO_TIME_DEF));
    		
    		Element allowedValuesElt;
    		if (dataValue.getDataType() == DataType.ASCII_STRING ||
    			dataValue.getDataType() == DataType.UTF_STRING)
    			allowedValuesElt = dom.addElement(constraintElt, "swe:AllowedTokens");
    		else if (isTime)
    		    allowedValuesElt = dom.addElement(constraintElt, "swe:AllowedTimes");
    		else
    			allowedValuesElt = dom.addElement(constraintElt, "swe:AllowedValues");
    		
    		// write all enumeration constraints
    		for (int c=0; c<constraints.size(); c++)
    		{
    			DataConstraint constraint = constraints.get(c);
    			
    			if (constraint instanceof EnumTokenConstraint)
    				writeTokenEnumConstraint(dom, (EnumTokenConstraint)constraint, allowedValuesElt);
    			else if (constraint instanceof EnumNumberConstraint)
    				writeNumberEnumConstraint(dom, (EnumNumberConstraint)constraint, allowedValuesElt, writeIntegers, useIso);
    		} 
    		
    		// write all other constraints
    		for (int c=0; c<constraints.size(); c++)
    		{
    			DataConstraint constraint = constraints.get(c);
    			
    			if (constraint instanceof IntervalConstraint)
    				writeIntervalConstraint(dom, (IntervalConstraint)constraint, allowedValuesElt, writeIntegers, useIso);
    			else if (constraint instanceof PatternConstraint)
    				writePatternConstraint(dom, (PatternConstraint)constraint, allowedValuesElt);
    		}
    		
    		// TODO write significantFigures
    	}
    }
    
    
    private void writeIntervalConstraint(DOMHelper dom, IntervalConstraint constraint, Element constraintElt, boolean writeIntegers, boolean useIso) throws XMLWriterException
    {
    	Element intervalElt = dom.addElement(constraintElt, "+swe:interval");
    	
    	String min, max;
    	if (writeIntegers)
    	{
    	    min = Integer.toString((int)constraint.getMin());
    	    max = Integer.toString((int)constraint.getMax());
    	}
    	else
    	{
    	    min = AsciiDataWriter.getDoubleAsString(constraint.getMin(), useIso);
    	    max = AsciiDataWriter.getDoubleAsString(constraint.getMax(), useIso);
    	}
    	
    	dom.setElementValue(intervalElt, min + " " + max);
    }
    
    
    private void writeNumberEnumConstraint(DOMHelper dom, EnumNumberConstraint constraint, Element constraintElt, boolean writeIntegers, boolean useIso) throws XMLWriterException
    {
    	double[] valueList = constraint.getValueList();
    	if (writeIntegers)
    	{
    	    for (int i=0; i<valueList.length; i++)
                dom.setElementValue(constraintElt, "+swe:value", Integer.toString((int)valueList[i]));
    	}
    	else
    	{
    	    for (int i=0; i<valueList.length; i++)
    	        dom.setElementValue(constraintElt, "+swe:value", AsciiDataWriter.getDoubleAsString(valueList[i], useIso));
    	}
    }
    
    
    private void writeTokenEnumConstraint(DOMHelper dom, EnumTokenConstraint constraint, Element constraintElt) throws XMLWriterException
    {
    	String[] valueList = constraint.getValueList();    	
    	for (int i=0; i<valueList.length; i++)
    		dom.setElementValue(constraintElt, "+swe:value", valueList[i]);
    }
    
    
    private void writePatternConstraint(DOMHelper dom, PatternConstraint constraint, Element constraintElt) throws XMLWriterException
    {
    	Element patternElt = dom.addElement(constraintElt, "+swe:pattern");
    	dom.setElementValue(patternElt, constraint.getPattern());
    }
    
    
    private void writeQuality(DOMHelper dom, DataValue dataValue, Element dataValueElt) throws XMLWriterException
    {
    	List<DataComponent> qualList = (List<DataComponent>)dataValue.getProperty(SweConstants.QUALITY);
    	if (qualList == null)
    	    return;
    	
    	for (DataComponent qualComp: qualList)
    	    addComponentProperty(dom, dataValueElt, "+swe:quality", qualComp, true);
    }
    
    
    private void writeNilValues(DOMHelper dom, DataValue dataValue, Element dataValueElt) throws XMLWriterException
    {
        NilValues nilValues = (NilValues)dataValue.getProperty(SweConstants.NIL_VALUES);
        if (nilValues == null)
            return;
        
        Element nilPropElt = dom.addElement(dataValueElt, "swe:nilValues");
        CachedReference<?> xlinkOptions = (CachedReference<?>)dataValue.getProperty(SweConstants.NIL_XLINK);
        if (xlinkOptions != null)
        {
            XlinkUtils.writeXlinkAttributes(dom, nilPropElt, xlinkOptions);
        }
        else
        {
            Element nilValuesElt = dom.addElement(nilPropElt, "swe:NilValues");
            
            // id
            if (nilValues.getId() != null)
                dom.setAttributeValue(nilValuesElt, "id", nilValues.getId());
            
            // reason -> reserved value mapping
            for (Entry<String, Object> nils: nilValues.getReasonsToValues().entrySet())
            {
                String token = null;
                Object nilObj = nils.getValue();
                if (nilObj instanceof Double)
                    token = AsciiDataWriter.getDoubleAsString((Double)nilObj, false);
                else
                    token = nilObj.toString();
                
                Element nilElt = dom.addElement(nilValuesElt, "+swe:nilValue");
                dom.setAttributeValue(nilElt, "reason", nils.getKey());
                dom.setElementValue(nilElt, token);
            }
        }
    }
    
    
    public void setExpandHref(boolean expandHref)
    {
        this.keepHref = !expandHref;
    }
}
