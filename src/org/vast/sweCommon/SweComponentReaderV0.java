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

import java.util.Hashtable;
import org.w3c.dom.*;
import org.vast.cdm.common.*;
import org.vast.data.*;
import org.vast.xml.*;


/**
 * <p><b>Title:</b><br/>
 * Swe Component Reader V0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Reads SWE Components structures made of Scalar Parameters,
 * DataRecord, DataArray, etc. This is for version 0 of the standard. 
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Dec 19, 2006
 * @version 1.0
 */
public class SweComponentReaderV0 implements DataComponentReader
{
    public static String tupleSeparator = " ";
    public static String tokenSeparator = ",";
    protected Hashtable<String, AbstractDataComponent> componentIds;
    protected AsciiDataParser parser;
    
    
    public SweComponentReaderV0()
    {
        componentIds = new Hashtable<String, AbstractDataComponent>();
        parser = new AsciiDataParser();
    }


    public AbstractDataComponent readComponentProperty(DOMHelper dom, Element propertyElement) throws CDMException
    {
        Element dataElement = dom.getFirstChildElement(propertyElement);
        String name = readPropertyName(dom, propertyElement);
        
        AbstractDataComponent container = readComponent(dom, dataElement);
        container.setName(name);
        
        return container;
    }
    
    
    public AbstractDataComponent readComponent(DOMHelper dom, Element dataElement) throws CDMException
    {
        AbstractDataComponent container = null;
        
        if (dataElement.getLocalName().endsWith("Range"))
        {
            container = readRange(dom, dataElement);
        }
        else if (dom.getFirstChildElement(dataElement) == null)
        {
            container = readParameter(dom, dataElement);
        }
        else if (dom.existAttribute(dataElement, "arraySize"))
        {
            container = readDataArray(dom, dataElement);
        }
        else
        {
            container = readDataRecord(dom, dataElement);
        } 
        
        // add id to hashtable if present
        String id = dom.getAttributeValue(dataElement, "id");
        if (id != null)
        	componentIds.put(id, container);

        return container;
    }


    /**
     * Reads a DataGroup structure and all its members
     * @param dataGroupElement Element
     * @throws CDMException
     * @return DataGroup
     */
    private DataGroup readDataRecord(DOMHelper dom, Element dataGroupElement) throws CDMException
    {
        // parse all members (can be a different name than member !!)
        NodeList componentList = dom.getAllChildElements(dataGroupElement);
        int groupSize = componentList.getLength();

        // error if no member present
        if (groupSize == 0)
            throw new CDMException("DataGroup without member");

        // create data block of the right size
        DataGroup dataGroup = new DataGroup(groupSize);

        // read attributes
        readAttributes(dom, dataGroup, dataGroupElement);

        // loop through all members
        for (int i = 0; i < groupSize; i++)
        {
            Element dataElement = (Element)componentList.item(i);                
            AbstractDataComponent dataComponent = readComponentProperty(dom, dataElement);
            dataGroup.addComponent(readPropertyName(dom, dataElement), dataComponent);
        }

        return dataGroup;
    }


    /**
     * Reads a DataArray structure the unique member
     * @param dataArrayElement Element
     * @throws CDMException
     * @return DataArray
     */
    private DataArray readDataArray(DOMHelper dom, Element dataArrayElement) throws CDMException
    {
        int arraySize = 1;
        NodeList children = dom.getAllChildElements(dataArrayElement);
        Element tupleValuesElement = null;
        DataArray dataArray = null;
        String arraySizeText = dom.getAttributeValue(dataArrayElement, "arraySize");
        
        try
        {
        	arraySize = Integer.parseInt(arraySizeText);
        	dataArray = new DataArray(arraySize);
        }
        catch (NumberFormatException e)
        {
            // try to lookup for an id
        	AbstractDataComponent sizeComponent = componentIds.get(arraySizeText.substring(1));
        	
        	if (sizeComponent == null)
        		throw new CDMException("Invalid array size");
        	else
        		dataArray = new DataArray((DataValue)sizeComponent, true);
        }
                        
        // loop through all children
        AbstractDataComponent dataComponent = null;
        for (int i = 0; i < children.getLength(); i++)
        {
            Element childElement = (Element)children.item(i);
            String childName = childElement.getLocalName();
            
            if (childName.equals("tupleValues"))
            	tupleValuesElement = childElement;
            else
            	dataComponent = readComponentProperty(dom, childElement);
        }

        readAttributes(dom, dataArray, dataArrayElement);
        dataArray.addComponent(dataComponent);
        
        // read tuple values if present
        if (tupleValuesElement != null)
            readArrayValues(dom, dataArray, tupleValuesElement);
        
        return dataArray;        
    }


    /**
     * Reads a scalar value (Quantity, Count, Term...) and atributes
     * @param parameterElement
     * @return DataValue encapsulating the value
     * @throws CDMException
     */
    private DataValue readParameter(DOMHelper dom, Element parameterElement) throws CDMException
    {
        DataValue paramValue = null;
        String valueText = dom.getElementValue(parameterElement, "");      
        String eltName = parameterElement.getLocalName();
        
        // Create Data component Object
    	if (eltName.equals("Quantity") || eltName.contains("Time"))
        {
            paramValue = new DataValue(DataType.DOUBLE);            
        }
        else if (eltName.equals("Count"))
        {
            paramValue = new DataValue(DataType.INT);
        }
        else if (eltName.equals("Boolean"))
        {
        	paramValue = new DataValue(DataType.BOOLEAN);
        }
        else if (eltName.equals("Category"))
        {
        	paramValue = new DataValue(DataType.UTF_STRING);
        }
        else
        {
            paramValue = new DataValue(DataType.OTHER);
        }
        
    	// read attributes
        paramValue.setProperty(SweConstants.COMP_QNAME, eltName);
    	readAttributes(dom, paramValue, parameterElement);
    	
        // Parse the value
        if (valueText != null)
        {
        	paramValue.assignNewDataBlock();
        	parser.parseToken(paramValue, valueText, '\0');
        }
        
        return paramValue;
    }
    
    
    /**
     * Reads a Range component
     * @param dom
     * @param rangeElement
     * @return
     * @throws CDMException
     */
    private DataGroup readRange(DOMHelper dom, Element rangeElement) throws CDMException
    {
        DataValue paramVal = null;
        DataGroup rangeValues = new DataGroup(2);
        String valueText = dom.getElementValue(rangeElement, "");      
        String eltName = rangeElement.getLocalName();
        
        // Create Data component Object
        if (eltName.startsWith("Quantity") || eltName.startsWith("Time"))
        {
            paramVal = new DataValue(DataType.DOUBLE);
        }
        else if (eltName.startsWith("Count"))
        {
            paramVal = new DataValue(DataType.INT);
        }
        else
            throw new CDMException("Only Quantity, Time and Count ranges are allowed");
        
        // read attributes
        readAttributes(dom, paramVal, rangeElement);
        rangeValues.setProperty(SweConstants.DEF_URI, "urn:ogc:def:data:range");
        
        // add params to DataGroup
        rangeValues.addComponent(SweConstants.MIN_VALUE_NAME, paramVal);
        rangeValues.addComponent(SweConstants.MAX_VALUE_NAME, paramVal.copy());
        
        // Parse the two values
        if (valueText != null)
        {
            rangeValues.assignNewDataBlock();
            try
            {
                String[] vals = valueText.split(" ");
                parser.parseToken((DataValue)rangeValues.getComponent(0), vals[0], '\0');
                parser.parseToken((DataValue)rangeValues.getComponent(1), vals[1], '\0');
            }
            catch (Exception e)
            {
                throw new CDMException("Error while parsing range values", e);
            }
        }
        
        return rangeValues;
    }
    
    
    /**
     * Reads name from element name or 'name' attribute
     * @param propertyElement
     * @return
     */
    public String readPropertyName(DOMHelper dom, Element propertyElement)
    {
        String name = dom.getAttributeValue(propertyElement, "name");
        
        if (name == null)
            name = propertyElement.getLocalName();
        
        return name;
    }
    
    
    /**
     * Retrieve data component properties 
     * (definition uri, reference frame, axis code, unit...)
     * @param dataComponent DataContainer
     * @param dataElement Element
     */
    private void readAttributes(DOMHelper dom, AbstractDataComponent dataComponent, Element parameterElement) throws CDMException
    {
        // definition URI
        String defUri = readComponentDefinition(dom, parameterElement);
        if (defUri != null)
            dataComponent.setProperty(SweConstants.DEF_URI, defUri);
        
        // reference frame
        String refFrame = dom.getAttributeValue(parameterElement, "referenceFrame");
        if (refFrame != null)
            dataComponent.setProperty(SweConstants.REF_FRAME, refFrame);
        
        // reference frame
        String refTimeFrame = dom.getAttributeValue(parameterElement, "referenceTimeFrame");
        if (refTimeFrame != null)
            dataComponent.setProperty(SweConstants.REF_FRAME, refTimeFrame);
        
        // local frame
        String locFrame = dom.getAttributeValue(parameterElement, "localFrame");
        if (locFrame != null)
            dataComponent.setProperty(SweConstants.LOCAL_FRAME, locFrame);
        
        // scale factor
        String scale = dom.getAttributeValue(parameterElement, "scale");        
        if (scale != null)
        	dataComponent.setProperty("scale", new Double(Double.parseDouble(scale)));
        
        // read unit attribute
        String unit = dom.getAttributeValue(parameterElement, "uom");
        if (unit != null)
        	dataComponent.setProperty(SweConstants.UOM_CODE, unit);
        
        // read axis code attribute
        String axisCode = dom.getAttributeValue(parameterElement, "axisCode");
        if (axisCode != null)
        	dataComponent.setProperty(SweConstants.AXIS_CODE, axisCode);
    }
    
    
    /**
     * Derives parameter definition URN from element name
     * @param parameterElement
     * @throws CDMException
     */
    private String readComponentDefinition(DOMHelper dom, Element parameterElement) throws CDMException
    {
        String defUri = dom.getAttributeValue(parameterElement, "definition");
        return defUri;
    }
    
    
    /**
     * Read fixed array values from tupleValues element
     * @param arrayStructure
     * @param tupleValuesElement
     * @throws CDMException
     */
    private void readArrayValues(DOMHelper dom, DataArray arrayStructure, Element tupleValuesElement) throws CDMException
    {
        // parse tuple values
        String valueText = dom.getElementValue(tupleValuesElement, "");
        String [] tupleList = valueText.split(tupleSeparator);
        int size = tupleList.length;
        
        // compute right array size and build internal data block
        arrayStructure.setSize(size);
        arrayStructure.assignNewDataBlock();
        
        // get list of DataValues
        ScalarIterator it = new ScalarIterator(arrayStructure);
        int tupleCounter = 0;
        int tokenCounter = 0;
        int tokenCount = 0;
        String [] tuples = null;
        
        // loop and read each array token        
        while (it.hasNext())
        {
        	DataValue component = (DataValue)it.next();
            
            if (tokenCounter >= tokenCount)
            {
                tokenCounter = 0;
                tuples = tupleList[tupleCounter].split(tokenSeparator);
                tokenCount = tuples.length;
                tupleCounter++;
            }
            
            parser.parseToken(component, tuples[tokenCounter], '\0');           
            tokenCounter++;
        }
    }
}
