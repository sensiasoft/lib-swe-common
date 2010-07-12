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

import java.util.Hashtable;
import org.w3c.dom.*;
import org.vast.cdm.common.AsciiEncoding;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataBlock;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataComponentWriter;
import org.vast.cdm.common.DataConstraint;
import org.vast.cdm.common.DataEncoding;
import org.vast.cdm.common.DataType;
import org.vast.data.*;
import org.vast.ogc.OGCRegistry;
import org.vast.unit.Unit;
import org.vast.util.DateTimeFormat;
import org.vast.xml.DOMHelper;
import org.vast.xml.QName;


/**
 * <p><b>Title:</b><br/>
 * SWE Data Component Writer v1.1
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Writes CDM Components structures including DataValue,
 * DataGroup, DataArray, DataChoice to an XML DOM tree. 
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin
 * @date Feb 29, 2008
 * @version 1.0
 */
public class SweComponentWriterV20 implements DataComponentWriter
{
	public static Hashtable<QName, SweCustomWriter> customWriters;
	protected String SWE_NS = OGCRegistry.getNamespaceURI(SWECommonUtils.SWE, "2.0");
    protected boolean writeInlineData = false;
    protected SweEncodingWriterV20 encodingWriter = new SweEncodingWriterV20();
    
    
    public SweComponentWriterV20()
    {
    }
    
        
    public Element writeComponent(DOMHelper dom, DataComponent dataComponents) throws CDMException
    {
    	return writeComponent(dom, dataComponents, false);
    }
    
    
    public Element writeComponent(DOMHelper dom, DataComponent dataComponent, boolean writeInlineData) throws CDMException
    {
        dom.addUserPrefix("swe", SWE_NS);
        dom.addUserPrefix("xlink", OGCRegistry.getNamespaceURI(OGCRegistry.XLINK));
        this.writeInlineData = writeInlineData;
        
        Element newElt = null;
        QName compQName = (QName)dataComponent.getProperty(SweConstants.COMP_QNAME);
        
        // soft or hard typed record component
        if (dataComponent instanceof DataGroup)
        {
        	QName recordQName = new QName(SWE_NS, "DataRecord");
        	boolean hardTyped = (compQName != null) && !compQName.equals(recordQName);
        	
        	if (hardTyped)
            {
	        	if (compQName.getNsUri().equals(SWE_NS) && compQName.getLocalName().endsWith("Range"))
	            	newElt = writeDataRange(dom, (DataGroup)dataComponent, compQName);
	            else
	            	newElt = writeDerivedRecord(dom, (DataGroup)dataComponent, compQName);
            }
            else
            	newElt = writeDataRecord(dom, (DataGroup)dataComponent);
        }
        
        // soft or hard typed choice component
        else if (dataComponent instanceof DataChoice)
        {
        	QName choiceQName = new QName(SWE_NS, "DataChoice");
        	boolean hardTyped = (compQName != null) && !compQName.equals(choiceQName);
        	
        	if (hardTyped)
        		newElt = writeDerivedChoice(dom, (DataChoice)dataComponent, compQName);
            else
            	newElt = writeDataChoice(dom, (DataChoice)dataComponent);
        }
        
        // soft or hard typed array component
        else if (dataComponent instanceof DataArray)
        {
        	QName arrayQName = new QName(SWE_NS, "DataArray");
        	boolean hardTyped = (compQName != null) && !compQName.equals(arrayQName);
        	
        	if (hardTyped)
        		newElt = writeDerivedArray(dom, (DataArray)dataComponent, compQName);
            else
            	newElt = writeDataArray(dom, (DataArray)dataComponent);
        }
        else if (dataComponent instanceof DataList)
        {
            // TODO write DataList
        }
        else if (dataComponent instanceof DataValue)
        {
            newElt = writeDataValue(dom, (DataValue)dataComponent);
        }

        return newElt;
    }


    /**
     * Writes a generic (soft-typed) DataRecord structure and all fields
     * @param dom
     * @param dataGroup
     * @return
     * @throws CDMException
     */
    private Element writeDataRecord(DOMHelper dom, DataGroup dataGroup) throws CDMException
    {
    	Element dataGroupElt = dom.createElement("swe:DataRecord");
        
    	// write common stuffs
        writeGmlProperties(dom, dataGroup, dataGroupElt);
        writeCommonAttributes(dom, dataGroup, dataGroupElt);
        
        // write each field
        int groupSize = dataGroup.getComponentCount();
        for (int i=0; i<groupSize; i++)
        {
        	DataComponent component = dataGroup.getComponent(i);
        	Element fieldElt = dom.addElement(dataGroupElt, "+swe:field");
        	fieldElt.setAttribute("name", component.getName());
    		
        	// write optional flag
        	Boolean optional = (Boolean)component.getProperty(SweConstants.OPTIONAL);
        	if (optional != null)
        		fieldElt.setAttribute("optional", (optional ? "true" : "false"));
    		
            Element componentElt = writeComponent(dom, component, writeInlineData);
            fieldElt.appendChild(componentElt);
        }

        return dataGroupElt;
    }
    
    
    /**
     * Write derived (hard-typed) Record structure and all fields
     * @param dom
     * @param dataGroup
     * @param recordQName
     * @return
     * @throws CDMException
     */
    private Element writeDerivedRecord(DOMHelper dom, DataGroup dataGroup, QName recordQName) throws CDMException
    {
    	// create element with the right QName
    	dom.addUserPrefix(recordQName.getPrefix(), recordQName.getNsUri());
    	Element dataGroupElt = dom.createElement(recordQName.getFullName());
		dom.setAttributeValue(dataGroupElt, "@is", "Record");
        
        // write common stuffs
        writeGmlProperties(dom, dataGroup, dataGroupElt);
        writeCommonAttributes(dom, dataGroup, dataGroupElt);
        
        // write each field
        int groupSize = dataGroup.getComponentCount();
        for (int i=0; i<groupSize; i++)
        {
        	DataComponent component = dataGroup.getComponent(i);
        	
        	// use the right QName
        	QName fieldQName = (QName)component.getProperty(SweConstants.FIELD_QNAME);
        	Element fieldElt;
        	
        	//TODO if (fieldQName != null)
        	dom.addUserPrefix(fieldQName.getPrefix(), fieldQName.getNsUri());
        	fieldElt = dom.addElement(dataGroupElt, "+" + fieldQName.getFullName());
        	dom.setAttributeValue(fieldElt, "@is", "field");
        	
        	// add name attribute if different from QName
        	String fieldName = component.getName();
        	if (!fieldName.equals(fieldQName.getLocalName()))
        		fieldElt.setAttribute("name", fieldName);
        	
        	// write optional flag
        	Boolean optional = (Boolean)component.getProperty(SweConstants.OPTIONAL);
        	if (optional != null)
        		fieldElt.setAttribute("optional", (optional ? "true" : "false"));
        	
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
    private Element writeDataChoice(DOMHelper dom, DataChoice dataChoice) throws CDMException
    {
        Element dataChoiceElt = dom.createElement("swe:DataChoice");
        
        // write common stuffs
        writeGmlProperties(dom, dataChoice, dataChoiceElt);
        writeCommonAttributes(dom, dataChoice, dataChoiceElt);
        
        // write each choice item
        int listSize = dataChoice.getComponentCount();
        for (int i=0; i<listSize; i++)
        {
            DataComponent component = dataChoice.getComponent(i);            
        	
            Element propElt = dom.addElement(dataChoiceElt, "+swe:item");
        	propElt.setAttribute("name", component.getName());
        	
            Element componentElt = writeComponent(dom, component, writeInlineData);
            propElt.appendChild(componentElt);        
        }       

        return dataChoiceElt;
    }
    
    
    /**
     * Writes derived (hard-typed) Choice structure and all items
     * @param dom
     * @param dataChoice
     * @param choiceQName
     * @return
     * @throws CDMException
     */
    private Element writeDerivedChoice(DOMHelper dom, DataChoice dataChoice, QName choiceQName) throws CDMException
    {
    	// create element with the right QName
    	dom.addUserPrefix(choiceQName.getPrefix(), choiceQName.getNsUri());
    	Element dataChoiceElt = dom.createElement(choiceQName.getFullName());
		dom.setAttributeValue(dataChoiceElt, "@is", "Choice");
        
        // write common stuffs
        writeGmlProperties(dom, dataChoice, dataChoiceElt);
        writeCommonAttributes(dom, dataChoice, dataChoiceElt);
        
        // write each field
        int groupSize = dataChoice.getComponentCount();
        for (int i=0; i<groupSize; i++)
        {
        	DataComponent component = dataChoice.getComponent(i);
        	
        	// use the right QName
        	QName fieldQName = (QName)component.getProperty(SweConstants.FIELD_QNAME);
        	dom.addUserPrefix(fieldQName.getPrefix(), fieldQName.getNsUri());
        	Element fieldElt = dom.addElement(dataChoiceElt, fieldQName.getFullName());
        	dom.setAttributeValue(fieldElt, "@is", "item");
        	
            Element componentElt = writeComponent(dom, component, writeInlineData);
            fieldElt.appendChild(componentElt);
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
    private Element writeDataArray(DOMHelper dom, DataArray dataArray) throws CDMException
    {
        Element dataArrayElt = dom.createElement("swe:DataArray");
		writeArrayContent(dom, dataArray, dataArrayElt);
        return dataArrayElt;
    }
    
    
    /**
     * Writes derived (hard-typed) Array structure
     * @param dom
     * @param dataArray
     * @param arrayQName
     * @return
     * @throws CDMException
     */
    private Element writeDerivedArray(DOMHelper dom, DataArray dataArray, QName arrayQName) throws CDMException
    {
    	// create element with the right QName
    	dom.addUserPrefix(arrayQName.getPrefix(), arrayQName.getNsUri());
    	Element arrayElt = dom.createElement(arrayQName.getFullName());
		dom.setAttributeValue(arrayElt, "@is", "Array");
		
		// append array content
		writeArrayContent(dom, dataArray, arrayElt);
    	
    	return arrayElt;
    }
    
    
    private void writeArrayContent(DOMHelper dom, DataArray dataArray, Element arrayElt) throws CDMException
    {
    	// write common stuffs
        writeGmlProperties(dom, dataArray, arrayElt);
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
        		Element countElt = writeDataValue(dom, sizeData);
        		eltCountElt.appendChild(countElt);
        	}
        	
        	// case of explicitly referenced field
        	else
        	{
	        	String sizeCompID = (String)sizeData.getProperty(SweConstants.ID);
	        	if (sizeCompID != null)
	        		dom.setAttributeValue(eltCountElt, "ref", sizeCompID);
	        	else
	        		throw new CDMException("Component used for storing variable array size MUST have an ID");
        	}
        }
        
        // case of fixed size
        else
        {
        	Element countElt = writeDataValue(dom, sizeData);
    		dom.setElementValue(countElt, "swe:value", Integer.toString(arraySize));
    		eltCountElt.appendChild(countElt);
        }
                	
        // write array component
        Element propElt = dom.addElement(arrayElt, "swe:elementType");
        DataComponent component = dataArray.getArrayComponent();
        
        // add name attribute if different from 'elementType'
    	String fieldName = component.getName();
    	if (!fieldName.equals("elementType"))
    		propElt.setAttribute("name", fieldName);
    	
    	Element componentElt = writeComponent(dom, component, writeInlineData);
    	propElt.appendChild(componentElt);
    	
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
        	
        	Element tupleValuesElt = writeArrayValues(dom, dataArray);
        	arrayElt.appendChild(tupleValuesElt);
        }
    }
    
    
    /**
     * Writes any scalar component
     * @param dom
     * @param dataValue
     * @return
     * @throws CDMException
     */
    private Element writeDataValue(DOMHelper dom, DataValue dataValue) throws CDMException
    {
        // create right element
        String eltName = getElementName(dataValue);
        Element dataValueElt = dom.createElement(eltName);
        
        // write all properties
        writeCommonAttributes(dom, dataValue, dataValueElt);
        writeGmlProperties(dom, dataValue, dataValueElt);    	
    	writeUom(dom, dataValue, dataValueElt);
    	writeCodeSpace(dom, dataValue, dataValueElt);
    	writeConstraints(dom, dataValue, dataValueElt);
    	writeQuality(dom, dataValue, dataValueElt);
    	
        // write value if necessary
    	DataBlock data = dataValue.getData();
    	if (writeInlineData && data != null)
            dom.setElementValue(dataValueElt, "swe:value", data.getStringValue());
        
        return dataValueElt;
    }
    
    
    /**
     * Writes a range which is special DataGroup
     * @param dom
     * @param dataGroup
     * @return
     * @throws CDMException
     */
    private Element writeDataRange(DOMHelper dom, DataGroup dataGroup, QName rangeQName) throws CDMException
    {
    	DataValue min = (DataValue)dataGroup.getComponent(0);
    	DataValue max = (DataValue)dataGroup.getComponent(1);
    	
    	// create right range element
        Element rangeElt = dom.createElement(rangeQName.getFullName());
        
        // write group properties
        writeCommonAttributes(dom, dataGroup, rangeElt);
        writeGmlProperties(dom, dataGroup, rangeElt);
        
        // extracted some from min component and not from DataGroup!!
        writeUom(dom, min, rangeElt);
    	writeConstraints(dom, min, rangeElt);
    	writeQuality(dom, min, rangeElt);
        
        // write min/max values if necessary
        if (writeInlineData && min.getData() != null && max.getData() != null)
        	dom.setElementValue(rangeElt, "swe:value", 
        			min.getData().getStringValue() + " " + max.getData().getStringValue());

        return rangeElt;
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
	        return valueQName.getFullName();
        
        Object def = dataValue.getProperty("definition");
    	String eltName;
    	
        if (def != null && ((String)def).contains("time"))
            eltName = "swe:Time";
        else if (dataValue.getDataType() == DataType.BOOLEAN)
            eltName = "swe:Boolean";
        else if (dataValue.getDataType() == DataType.DOUBLE || dataValue.getDataType() == DataType.FLOAT)
            eltName = "swe:Quantity";
        else if (dataValue.getDataType() == DataType.ASCII_STRING || dataValue.getDataType() == DataType.UTF_STRING)
            eltName = "swe:Category";
        else
            eltName = "swe:Count";
        
        return eltName;
    }
    
    
    public void writeName(Element propertyElement, String name)
    {
        if (name != null)
            propertyElement.setAttribute("name", name);
    }
    
    
    private void writeGmlProperties(DOMHelper dom, DataComponent dataComponent, Element dataComponentElt) throws CDMException
    {
    	dom.addUserPrefix("gml", OGCRegistry.getNamespaceURI(OGCRegistry.GML, "3.2.1"));
    	
    	// gml:id
    	String id = (String)dataComponent.getProperty(SweConstants.ID);
    	if (id != null)
    		dom.setAttributeValue(dataComponentElt, "@gml:id", id);
    	
    	// gml metadata?
    	
    	// description
    	String description = (String)dataComponent.getProperty(SweConstants.DESC);
    	if (description != null)
    		dom.setElementValue(dataComponentElt, "gml:description", description);
    	
    	// name
    	String name = (String)dataComponent.getProperty(SweConstants.NAME);
    	if (name != null)
    		dom.setElementValue(dataComponentElt, "gml:name", name);
    }
    
    
    private void writeCommonAttributes(DOMHelper dom, DataComponent dataComponent, Element dataComponentElt) throws CDMException
    {
    	// definition URI
        Object defUri = dataComponent.getProperty(SweConstants.DEF_URI);
        if (defUri != null)
            dataComponentElt.setAttribute("definition", (String)defUri);
        
        // updatable
        Boolean updatable = (Boolean)dataComponent.getProperty(SweConstants.UPDATABLE);
        if (updatable != null)
            dataComponentElt.setAttribute("updatable", updatable.toString());
        
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
    
    
    private Element writeArrayValues(DOMHelper dom, DataComponent arrayStructure) throws CDMException
    {
        // TODO write values using SWE writers depending on encoding!
    	Element tupleValuesElement = dom.createElement("swe:values");       
        DataBlock data = arrayStructure.getData();
        StringBuffer buffer = new StringBuffer();
       
        for (int i=0; i<data.getAtomCount(); i++)
        {
            if (i != 0)
                buffer.append(" ");
            
            String text = data.getStringValue(i);
            buffer.append(text);
        }
        
        tupleValuesElement.setTextContent(buffer.toString());
        
        return tupleValuesElement;
    }
    
    
    private void writeUom(DOMHelper dom, DataValue dataValue, Element dataValueElt) throws CDMException
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
    
    
    private void writeCodeSpace(DOMHelper dom, DataValue dataValue, Element categoryElt) throws CDMException
    {
        String codeSpace = (String)dataValue.getProperty(SweConstants.DIC_URI);
    	
        if (codeSpace != null)
        	dom.setAttributeValue(categoryElt, "swe:codeSpace/@xlink:href", codeSpace);
    }
    
    
    private void writeConstraints(DOMHelper dom, DataValue dataValue, Element dataValueElt) throws CDMException
    {
    	ConstraintList constraints = dataValue.getConstraints();
    	
    	if (constraints != null && !constraints.isEmpty())
    	{
    		Element constraintElt = dom.addElement(dataValueElt, "swe:constraint");
    		Element allowedValuesElt;
    		
    		if (dataValue.getDataType() == DataType.ASCII_STRING ||
    			dataValue.getDataType() == DataType.UTF_STRING)
    			allowedValuesElt = dom.addElement(constraintElt, "swe:AllowedTokens");
    		else
    			allowedValuesElt = dom.addElement(constraintElt, "swe:AllowedValues");
    		
    		// write all enumeration constraints
    		for (int c=0; c<constraints.size(); c++)
    		{
    			DataConstraint constraint = constraints.get(c);
    			
    			if (constraint instanceof EnumTokenConstraint)
    				writeTokenEnumConstraint(dom, (EnumTokenConstraint)constraint, allowedValuesElt);
    			else if (constraint instanceof EnumNumberConstraint)
    				writeNumberEnumConstraint(dom, (EnumNumberConstraint)constraint, allowedValuesElt);
    		} 
    		
    		// write all other constraints
    		for (int c=0; c<constraints.size(); c++)
    		{
    			DataConstraint constraint = constraints.get(c);
    			
    			if (constraint instanceof IntervalConstraint)
    				writeIntervalConstraint(dom, (IntervalConstraint)constraint, allowedValuesElt);
    			else if (constraint instanceof PatternConstraint)
    				writePatternConstraint(dom, (PatternConstraint)constraint, allowedValuesElt);
    		}
    		
    		// TODO write significantFigures
    	}
    }
    
    
    private void writeIntervalConstraint(DOMHelper dom, IntervalConstraint constraint, Element constraintElt) throws CDMException
    {
    	Element intervalElt = dom.addElement(constraintElt, "+swe:interval");
    	String text = constraint.getMin() + " " + constraint.getMax();
    	dom.setElementValue(intervalElt, text);
    }
    
    
    private void writeNumberEnumConstraint(DOMHelper dom, EnumNumberConstraint constraint, Element constraintElt) throws CDMException
    {
    	double[] valueList = constraint.getValueList();    	
    	for (int i=0; i<valueList.length; i++)
    		dom.setElementValue(constraintElt, "+swe:value", Double.toString(valueList[i]));
    }
    
    
    private void writeTokenEnumConstraint(DOMHelper dom, EnumTokenConstraint constraint, Element constraintElt) throws CDMException
    {
    	String[] valueList = constraint.getValueList();    	
    	for (int i=0; i<valueList.length; i++)
    		dom.setElementValue(constraintElt, "+swe:value", valueList[i]);
    }
    
    
    private void writePatternConstraint(DOMHelper dom, PatternConstraint constraint, Element constraintElt) throws CDMException
    {
    	Element patternElt = dom.addElement(constraintElt, "+swe:pattern");
    	dom.setElementValue(patternElt, constraint.getPattern());
    }
    
    
    private void writeQuality(DOMHelper dom, DataValue dataValue, Element dataValueElt) throws CDMException
    {
    	// TODO write Quality 
    }
}
