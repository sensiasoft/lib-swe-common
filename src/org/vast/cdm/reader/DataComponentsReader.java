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

package org.vast.cdm.reader;

import java.text.ParseException;
import java.util.Hashtable;
import org.w3c.dom.*;
import org.vast.cdm.common.*;
import org.vast.cdm.semantics.DictionaryURN;
import org.vast.data.*;
import org.vast.io.xml.*;
import org.vast.util.*;


/**
 * <p><b>Title:</b><br/>
 * Data Component Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Reads CDM Components structures made of Scalar Parameters,
 * DataGroup, DataArray. 
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Aug 16, 2005
 * @version 1.0
 */
public class DataComponentsReader implements DataComponentReader
{
    public static String tupleSeparator = " ";
    public static String tokenSeparator = ",";
    protected DOMReader dom;
    protected Hashtable<String, AbstractDataComponent> componentIds;
    
    
    public DataComponentsReader(DOMReader parentReader)
    {
        dom = parentReader;
        componentIds = new Hashtable<String, AbstractDataComponent>();
    }


    /**
     * Reads the component property in a data component
     * @param componentElement Element
     * @throws SMLReaderException
     * @return DataComponent
     */
    public AbstractDataComponent readComponentProperty(Element propertyElement) throws CDMException
    {
        Element dataElement = dom.getFirstChildElement(propertyElement);
        String name = readName(propertyElement);
        
        AbstractDataComponent container = readDataComponents(dataElement);
        container.setName(name);
        
        return container;
    }
    
    
    /**
     * Reads contents of any data component (group/array/scalar)
     * @param dataElement
     * @param componentName
     * @return DataContainer structure
     * @throws SMLReaderException
     */
    public AbstractDataComponent readDataComponents(Element dataElement) throws CDMException
    {
        AbstractDataComponent container = null;
        
        if (dataElement.getLocalName().endsWith("Range"))
        {
            container = readRange(dataElement);
        }
        else if (dom.getFirstChildElement(dataElement) == null)
        {
            container = readParameter(dataElement);
        }
        else if (dom.existAttribute(dataElement, "arraySize"))
        {
            container = readDataArray(dataElement);
        }
        else
        {
            container = readDataGroup(dataElement);
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
     * @throws SMLReaderException
     * @return DataGroup
     */
    private DataGroup readDataGroup(Element dataGroupElement) throws CDMException
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
        readAttributes(dataGroup, dataGroupElement);

        // loop through all members
        for (int i = 0; i < groupSize; i++)
        {
            Element dataElement = (Element)componentList.item(i);                
            AbstractDataComponent dataComponent = readComponentProperty(dataElement);
            dataGroup.addComponent(readName(dataElement), dataComponent);
        }

        return dataGroup;
    }


    /**
     * Reads a DataArray structure the unique member
     * @param dataArrayElement Element
     * @throws SMLReaderException
     * @return DataArray
     */
    private DataArray readDataArray(Element dataArrayElement) throws CDMException
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
        		dataArray = new DataArray((DataValue)sizeComponent);
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
            	dataComponent = readComponentProperty(childElement);
        }

        readAttributes(dataArray, dataArrayElement);
        dataArray.addComponent(dataComponent);
        
        // read tuple values if present
        if (tupleValuesElement != null)
            readArrayValues(dataArray, tupleValuesElement);
        
        return dataArray;        
    }


    /**
     * Reads a parameter value and atributes (Quantity, Count, Term...)
     * @param parameterElement
     * @return DataValue encapsulating the value
     * @throws SMLReaderException
     */
    private DataValue readParameter(Element parameterElement) throws CDMException
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
    	readAttributes(paramValue, parameterElement);
    	
        // Parse the value
        if (valueText != null)
        {
        	paramValue.assignNewDataBlock();
        	parseToken(paramValue, valueText, '\0');
        }
        
        return paramValue;
    }
    
    
    private DataGroup readRange(Element rangeElement) throws CDMException
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
        readAttributes(paramVal, rangeElement);
        rangeValues.setProperty(DataComponent.DEF, "urn:ogc:def:data:range");
        
        // add params to DataGroup
        rangeValues.addComponent("min", paramVal);
        rangeValues.addComponent("max", paramVal.copy());
        
        // Parse the two values
        if (valueText != null)
        {
            rangeValues.assignNewDataBlock();
            try
            {
                String[] vals = valueText.split(" ");
                parseToken((DataValue)rangeValues.getComponent(0), vals[0], '\0');
                parseToken((DataValue)rangeValues.getComponent(1), vals[1], '\0');
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
    public String readName(Element propertyElement)
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
    private void readAttributes(AbstractDataComponent dataComponent, Element parameterElement) throws CDMException
    {
        // definition URI
        String defUri = readComponentDefinition(parameterElement);
        if (defUri != null)
            dataComponent.setProperty(DataComponent.DEF, defUri);
        
        // reference frame
        String refFrame = dom.getAttributeValue(parameterElement, "referenceFrame");
        if (refFrame != null)
            dataComponent.setProperty(DataComponent.REF, refFrame);
        
        // local frame
        String locFrame = dom.getAttributeValue(parameterElement, "localFrame");
        if (locFrame != null)
            dataComponent.setProperty(DataComponent.LOC, locFrame);
        
        // scale factor
        String scale = dom.getAttributeValue(parameterElement, "scale");        
        if (scale != null)
        	dataComponent.setProperty(DataComponent.SCALE, new Double(Double.parseDouble(scale)));
        
        // read unit attribute
        String unit = dom.getAttributeValue(parameterElement, "uom");
        if (unit != null)
        	dataComponent.setProperty(DataComponent.UOM, unit);
        
        // read axis code attribute
        String axisCode = dom.getAttributeValue(parameterElement, "axisCode");
        if (axisCode != null)
        	dataComponent.setProperty(DataComponent.AXIS, axisCode);
    }
    
    
    /**
     * Derives parameter definition URN from element name
     * @param parameterElement
     * @throws SMLReaderException
     */
    private String readComponentDefinition(Element parameterElement) throws CDMException
    {
        String defUri = dom.getAttributeValue(parameterElement, "definition");
        
        if (defUri != null)
            return defUri;
        
        // otherwise derive urn from element name
        String parameterName = parameterElement.getLocalName();
        
        // determine parameter type
        if (parameterName.indexOf("Boolean") != -1)
            return DictionaryURN.bool;
        else if (parameterName.indexOf("Count") != -1)
            return DictionaryURN.count;
        else if (parameterName.indexOf("Time") != -1)
            return DictionaryURN.time;
        else if (parameterName.indexOf("Quantity") != -1)
            return DictionaryURN.quantity;
        else if (parameterName.indexOf("Category") != -1)
            return DictionaryURN.category;
        else if (parameterName.indexOf("DataGroup") != -1)
            return DictionaryURN.group;
        else if (parameterName.indexOf("DataArray") != -1)
            return DictionaryURN.array;
        else
            return "none";
    }
    
    
    /**
     * Read fixed array values from tupleValues element
     * @param arrayStructure
     * @param tupleValuesElement
     * @throws SMLReaderException
     */
    private void readArrayValues(DataArray arrayStructure, Element tupleValuesElement) throws CDMException
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
            
            DataComponentsReader.parseToken(component, tuples[tokenCounter], '\0');           
            tokenCounter++;
        }
    }
    
    
    /**
     * Parse a token from a tuple depending on the corresponding Data Component Definition 
     * @param scalarInfo
     * @param token
     * @param decimalSep character to be used as the decimal separator. (don't change anything and assume '.' if 0)
     * @return a DataBlock containing the read data
     * @throws CDMException
     * @throws NumberFormatException
     */
    public static DataBlock parseToken(DataValue scalarInfo, String token, char decimalSep) throws CDMException, NumberFormatException
	{
		// get data block and its data type
		DataBlock data = scalarInfo.getData();
		DataType dataType = data.getDataType();
		
		// replace decimal separator by a '.'
		if (decimalSep != 0)
			token.replace(decimalSep, '.');
		
		// get scale factor if present
		Object propValue = scalarInfo.getProperty(DataComponent.SCALE);
		double scale = 1.0;
		if (propValue != null)
			scale = ((Double)propValue).doubleValue();
		
		try
		{
			switch (dataType)
			{
				case BOOLEAN:
					try
					{
						int intValue = Integer.parseInt(token);
						if ((intValue != 0) && (intValue != 1))
							throw new ParseException("", 0);
						
						data.setBooleanValue(intValue != 0);
					}
					catch (NumberFormatException e)
					{
						boolean boolValue = Boolean.parseBoolean(token);
						data.setBooleanValue(boolValue);
					} 
					break;
					
				case BYTE:
					byte byteValue = Byte.parseByte(token);
					data.setByteValue(byteValue);
					break;
					
				case SHORT:
					short shortValue = Short.parseShort(token);
					data.setShortValue(shortValue);
					break;
					
				case INT:
					int intValue = Integer.parseInt(token);
					data.setIntValue(intValue);
					break;
					
				case LONG:
					long longValue = Long.parseLong(token);
					data.setLongValue(longValue);
					break;
					
				case FLOAT:
					float floatValue = Float.parseFloat(token);
					data.setFloatValue(floatValue);
					break;
					
				case DOUBLE:
					try
					{
						double doubleValue = Double.parseDouble(token);
						data.setDoubleValue(doubleValue * scale);
					}
					catch (NumberFormatException e)
					{
						// case of ISO time
						double doubleValue;
						try
						{
							doubleValue = DateTimeFormat.parseIso(token);
						}
						catch (ParseException e1)
						{
							// TODO Improve this (ok only if NO_DATA character)
							doubleValue = Double.NaN;
						}
						data.setDoubleValue(doubleValue);							
					}						
					break;
					
				case UTF_STRING:
				case ASCII_STRING:
					data.setStringValue(token);
					break;
			}
		}
        catch (NumberFormatException e)
        {
            throw new CDMException("Invalid data " + token + " for parameter " + scalarInfo, e);
        }
        catch (ParseException e)
        {
        	throw new CDMException("Invalid data " + token + " for parameter " + scalarInfo, e);
        }
        
		return data;
	}
}
