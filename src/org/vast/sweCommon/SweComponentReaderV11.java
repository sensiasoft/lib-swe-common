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
    Alexandre Robin <alexandre.robin@spotimage.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sweCommon;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import org.w3c.dom.*;
import org.vast.cdm.common.*;
import org.vast.data.*;
import org.vast.xml.*;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.gml.GMLException;
import org.vast.ows.gml.GMLUnitReader;
import org.vast.sweCommon.IntervalConstraint;
import org.vast.unit.Unit;
import org.vast.unit.UnitParserUCUM;
import org.vast.util.DateTimeFormat;


/**
 * <p><b>Title:</b><br/>
 * Swe Component Reader V11
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Reads SWE Components structures made of Scalar Parameters,
 * DataRecord, DataArray, and hard typed dervied structures.
 * This is for version 1.1 of the SWE Common specification.
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin (Spot Image)
 * @date Feb 1, 2008
 * @version 1.0
 */
public class SweComponentReaderV11 implements DataComponentReader
{
	public static Hashtable<QName, SweCustomReader> customReaders;
	protected final static String GML_NS = OGCRegistry.getNamespaceURI(OGCRegistry.GML);
	protected final static String SWE_NS = OGCRegistry.getNamespaceURI(OGCRegistry.SWE, "1.1");
	protected final static String RECORD_TYPE = "Record";
	protected final static String ARRAY_TYPE = "Array";
	protected final static String FIELD_TYPE = "field";
	protected Hashtable<String, AbstractDataComponent> componentIds;
    protected AsciiDataParser asciiParser;
    
    
    public SweComponentReaderV11()
    {
        componentIds = new Hashtable<String, AbstractDataComponent>();
        asciiParser = new AsciiDataParser();
        customReaders = new Hashtable<QName, SweCustomReader>();
    }


    public AbstractDataComponent readComponentProperty(DOMHelper dom, Element propertyElt) throws CDMException
    {
        Element dataElement = dom.getFirstChildElement(propertyElt);
        String name = readPropertyName(dom, propertyElt);
        AbstractDataComponent container = readComponent(dom, dataElement);
        container.setName(name);
        
        return container;
    }
    
    
    public AbstractDataComponent readComponent(DOMHelper dom, Element componentElt) throws CDMException
    {
        AbstractDataComponent container = null;
        String eltName = componentElt.getLocalName();
        
        String sweType = "";
        if (dom.existAttribute(componentElt, "@is"))
        	sweType = dom.getAttributeValue(componentElt, "@is");
        
        // set type
        QName objectType = new QName(componentElt.getNamespaceURI(), eltName);
        container.setProperty(DataComponent.TYPE, objectType);
        
        // try to call a custom reader for this particular object type
        SweCustomReader customReader = customReaders.get(objectType);
        if (customReader != null)
        {
        	container = customReader.readComponent(dom, componentElt);
        }
        else // no custom reader found
	    {
	        // call the right default method depending on type
	        if (eltName.endsWith("DataRecord") || sweType.equalsIgnoreCase(RECORD_TYPE))
	        {
	            container = readDataRecord(dom, componentElt);
	        }
	        else if (eltName.endsWith("DataArray") || sweType.equalsIgnoreCase(ARRAY_TYPE))
	        {
	            container = readDataArray(dom, componentElt);
	        }
	        else if (eltName.endsWith("Range"))
	        {
	            container = readRange(dom, componentElt);
	        }
	        else // default to scalar
	        {
	            container = readScalar(dom, componentElt);
	        }
        }
        
        // add id to hashtable if present
        String id = dom.getAttributeValue(componentElt, "id");
        if (id != null)
        	componentIds.put(id, container);

        return container;
    }


    /**
     * Reads a DataRecord structure and all its fields
     * @param recordElt Element
     * @throws CDMException
     * @return DataGroup
     */
    private DataGroup readDataRecord(DOMHelper dom, Element recordElt) throws CDMException
    {
        // parse all fields (can be a different element name than "field" if hard typed!!)
        NodeList componentList = dom.getAllChildElements(recordElt);
        int childCount = componentList.getLength();
        
        // create DataGroup to hold field definitions
        DataGroup dataGroup = new DataGroup(2);
        
        // loop through all child elements
        for (int i = 0; i < childCount; i++)
        {
            Element childElt = (Element)componentList.item(i);
            
            // skip everything in GML namespace (gml:metadata, gml:description, gml:name)
            if (childElt.getNamespaceURI().contains(GML_NS))
                continue;
            
            // add field components
            DataComponent dataComponent = readComponentProperty(dom, childElt);
            dataGroup.addComponent(readPropertyName(dom, childElt), dataComponent);
        }        
        
        // error if no field present
        if (dataGroup.getComponentCount() == 0)
            throw new CDMException("Invalid DataRecord: Must have AT LEAST ONE field");

        // read common stuffs
        readGmlProperties(dataGroup, dom, recordElt);
        readCommonAttributes(dataGroup, dom, recordElt);

        return dataGroup;
    }


    /**
     * Reads a DataArray structure the unique member
     * @param arrayElt Element
     * @throws CDMException
     * @return DataArray
     */
    private DataArray readDataArray(DOMHelper dom, Element arrayElt) throws CDMException
    {
        int arraySize = 1;
        DataArray dataArray = null;
        
        // if elementCount is referencing another component
        String countId = dom.getAttributeValue(arrayElt, "elementCount/@ref");
        if (countId != null)
        {
            DataComponent sizeComponent = componentIds.get(countId);
            if (sizeComponent == null)
                throw new CDMException("Invalid elementCount: The elementCount property must reference an existing Count component");
            dataArray = new DataArray((DataValue)sizeComponent);
        }
        
        // else if elementCount is given inline
        else
        {
            try
            {
                String countValue = dom.getElementValue(arrayElt, "elementCount/Count/value");
                arraySize = Integer.parseInt(countValue);
                if (arraySize < 0)
                    throw new CDMException("Invalid elementCount: The elementCount must specify a positive integer value");                
                dataArray = new DataArray(arraySize);
            }
            catch (Exception e)
            {
                throw new CDMException("Invalid elementCount: The elementCount must specify a positive integer value");
            }
        }
                        
        // read array component
        Element elementTypeElt = dom.getElement(arrayElt, "elementType");
        DataComponent dataComponent = readComponentProperty(dom, elementTypeElt);
        dataArray.addComponent(dataComponent);
        
        // read common stuffs
        readGmlProperties(dataArray, dom, arrayElt);
        readCommonAttributes(dataArray, dom, arrayElt);
        
        // read encoding and parse values (if both present) using the appropriate parser
        Element encodingElt = dom.getElement(arrayElt, "encoding");
        Element valuesElt = dom.getElement(arrayElt, "values");
        if (encodingElt != null && valuesElt != null)
        {
            SweEncodingReaderV1 encodingReader = new SweEncodingReaderV1();
            DataEncoding encoding = encodingReader.readEncodingProperty(dom, encodingElt);
            DataStreamParser parser = SWEFactory.createDataParser(encoding);
            parser.setDataComponents(dataArray);
            parser.reset();
            InputStream is = getDataStream(dom, valuesElt);
            parser.parse(is);
        }
        
        return dataArray;        
    }
    
    
    /**
     * Gets the right input stream for href or inline values
     * @param dom
     * @param valuesElt
     * @return
     * @throws CDMException
     */
    private InputStream getDataStream(DOMHelper dom, Element valuesElt) throws CDMException
    {
        String href = dom.getAttributeValue(valuesElt, "@href");
        if (href != null)
        {
            return URIStreamHandler.openStream(href);
        }
        else
        {
            String values = dom.getElementValue(valuesElt, "");
            return(new ByteArrayInputStream(values.getBytes()));
        }
    }


    /**
     * Reads a scalar value and atributes (Quantity, Count, Term...)
     * @param scalarElt
     * @return DataValue encapsulating the value
     * @throws SMLReaderException
     */
    private DataValue readScalar(DOMHelper dom, Element scalarElt) throws CDMException
    {
        DataValue dataValue = null;              
        String eltName = scalarElt.getLocalName();
        
        // Create DataValue Object with appropriate type
    	if (eltName.equals("Quantity") || eltName.equals("Time"))
    	    dataValue = new DataValue(DataType.DOUBLE);
        else if (eltName.equals("Count"))
            dataValue = new DataValue(DataType.INT);
        else if (eltName.equals("Boolean"))
        	dataValue = new DataValue(DataType.BOOLEAN);
        else if (eltName.equals("Category") || eltName.equals("Text"))
        	dataValue = new DataValue(DataType.UTF_STRING);
        else
            throw new CDMException("Invalid component: " + eltName);
        
    	// read common stuffs        
        readGmlProperties(dataValue, dom, scalarElt);
    	readCommonAttributes(dataValue, dom, scalarElt);
        readUom(dataValue, dom, scalarElt);
        readCodeSpace(dataValue, dom, scalarElt);
        readQuality(dataValue, dom, scalarElt);
        readConstraints(dataValue, dom, scalarElt);
        
        // Parse the value
        String value = dom.getElementValue(scalarElt, "value");
        if (value != null)
        {
        	dataValue.assignNewDataBlock();
            asciiParser.parseToken(dataValue, value, '\0');
        }
        
        return dataValue;
    }
    
    
    private DataGroup readRange(DOMHelper dom, Element rangeElt) throws CDMException
    {
        DataValue paramVal = null;
        DataGroup rangeValues = new DataGroup(2);        
        String eltName = rangeElt.getLocalName();
        
        // Create Data component Object
        if (eltName.startsWith("Quantity"))
        {
            paramVal = new DataValue(DataType.DOUBLE);
            paramVal.setProperty(DataComponent.TYPE, "Quantity");
        }
        else if (eltName.startsWith("Count"))
        {
            paramVal = new DataValue(DataType.INT);
            paramVal.setProperty(DataComponent.TYPE, "Count");
        }
        else if (eltName.startsWith("Time"))
        {
            paramVal = new DataValue(DataType.DOUBLE);
            paramVal.setProperty(DataComponent.TYPE, "Time");
        }
        else
            throw new CDMException("Only Quantity, Time and Count ranges are allowed");
        
        // read attributes
        readCommonAttributes(paramVal, dom, rangeElt);
        rangeValues.setProperty(DataComponent.DEF_URI, "urn:ogc:def:data:OGC:range");
        
        // add params to DataGroup
        rangeValues.addComponent("min", paramVal);
        rangeValues.addComponent("max", paramVal.copy());
        
        // Parse the two values
        String valueText = dom.getElementValue(rangeElt, "value");
        if (valueText != null)
        {
            rangeValues.assignNewDataBlock();
            try
            {
                String[] vals = valueText.split(" ");
                asciiParser.parseToken((DataValue)rangeValues.getComponent(0), vals[0], '\0');
                asciiParser.parseToken((DataValue)rangeValues.getComponent(1), vals[1], '\0');
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
     * @param propertyElt
     * @return
     */
    private String readPropertyName(DOMHelper dom, Element propertyElt)
    {
        String name = dom.getAttributeValue(propertyElt, "name");
        
        if (name == null)
            name = propertyElt.getLocalName();
        
        return name;
    }
    
    
    /**
     * Reads gml properties and attributes common to all SWE components
     * @param dataComponent
     * @param dom
     * @param componentElt
     * @throws CDMException
     */
    private void readGmlProperties(DataComponent dataComponent, DOMHelper dom, Element componentElt) throws CDMException
    {
        dom.addUserPrefix("gml", GML_NS);
        
        // gml metadata?
        
        // gml description
        String description = dom.getElementValue(componentElt, "gml:description");
        if (description != null)
            dataComponent.setProperty(DataComponent.DESC, description);
        
        // gml names
        NodeList nameList = dom.getElements(componentElt, "gml:name");
        int listSize = nameList.getLength();
        if (listSize != 0)
        {
            ArrayList<QName> names = new ArrayList<QName>(listSize);
            for (int i=0; i<listSize; i++)
            {
                Element nextElt = (Element)nameList.item(i);
                String value = dom.getElementValue(nextElt, "");
                String codeSpace = dom.getAttributeValue(nextElt, "@codeSpace");
                QName name = new QName(codeSpace, value);
                names.add(name);
            }
            dataComponent.setProperty(DataComponent.NAMES, names);
        }
    }
    
    
    /**
     * Reads common component properties 
     * (definition uri, reference frame, axisID, unit...)
     * @param dataComponent DataContainer
     * @param dataElement Element
     */
    private void readCommonAttributes(DataComponent dataComponent, DOMHelper dom, Element componentElt) throws CDMException
    {
        // definition URI
        String defUri = readComponentDefinition(dom, componentElt);
        if (defUri != null)
            dataComponent.setProperty(DataComponent.DEF_URI, defUri);
        
        // reference frame
        String refFrame = dom.getAttributeValue(componentElt, "referenceFrame");
        if (refFrame != null)
            dataComponent.setProperty(DataComponent.REF_FRAME, refFrame);
        
        // reference time
        try
        {
            String refTime = dom.getAttributeValue(componentElt, "referenceTime");
            if (refTime != null)
                dataComponent.setProperty(DataComponent.REF_TIME, DateTimeFormat.parseIso(refTime));
        }
        catch (ParseException e)
        {
            throw new CDMException("Invalid reference time", e);
        }
        
        // local frame
        String locFrame = dom.getAttributeValue(componentElt, "localFrame");
        if (locFrame != null)
            dataComponent.setProperty(DataComponent.LOC, locFrame);
        
        // read axis code attribute
        String axisCode = dom.getAttributeValue(componentElt, "axisID");
        if (axisCode != null)
        	dataComponent.setProperty(DataComponent.AXIS, axisCode);
    }
    
    
    /**
     * Derives parameter definition URN from element name
     * @param componentElement
     * @throws SMLReaderException
     */
    private String readComponentDefinition(DOMHelper dom, Element componentElement) throws CDMException
    {
        String defUri = dom.getAttributeValue(componentElement, "definition");
        return defUri;
    }
    
    
    /**
     * Reads the uom code, href or inline content for the given scalar component
     * @param dataComponent
     * @param dom
     * @param componentElt
     */
    private void readUom(DataComponent dataComponent, DOMHelper dom, Element scalarElt) throws CDMException
    {
        if (!dom.existElement(scalarElt, "uom"))
            return;
        
        String ucumCode = dom.getAttributeValue(scalarElt, "uom/@code");
        String href = dom.getAttributeValue(scalarElt, "uom/@href");           
                
        // uom code        
        if (ucumCode != null)
        {
            dataComponent.setProperty(DataComponent.UOM_CODE, ucumCode);
            
            // also create unit object
            UnitParserUCUM ucumParser = new UnitParserUCUM();
            Unit unit = ucumParser.getUnit(ucumCode);
            if (unit != null)
                dataComponent.setProperty(DataComponent.UOM_OBJ, unit);
        }
        
        // if no code, read href
        else if (href != null)
        {
            dataComponent.setProperty(DataComponent.UOM_URI, href);
        }
        
        // inline unit
        else
        {
            Element unitElt = dom.getElement(scalarElt, "uom/*");
            GMLUnitReader unitReader = new GMLUnitReader();
            try
            {
                Unit unit = unitReader.readUnit(dom, unitElt);
                if (unit != null)
                    dataComponent.setProperty(DataComponent.UOM_OBJ, unit);
            }
            catch (GMLException e)
            {
                throw new CDMException("Invalid Inline Unit", e);
            }
        }
    }
    
    
    /**
     * Reads codeSpace URI in a Category
     * @param dataComponent
     * @param dom
     * @param scalarElt
     * @throws CDMException
     */
    private void readCodeSpace(DataComponent dataComponent, DOMHelper dom, Element scalarElt) throws CDMException
    {
        // codeSpace URI
        String codeSpaceUri = dom.getAttributeValue(scalarElt, "codeSpace/@href");
        if (codeSpaceUri != null)
            dataComponent.setProperty(DataComponent.DIC_URI, codeSpaceUri);
    }
    
    
    /**
     * Reads the quality component if present inline
     * @param dom
     * @param scalarElement
     * @throws CDMException
     */
    private void readQuality(DataComponent dataComponent, DOMHelper dom, Element scalarElt) throws CDMException
    {
        if (!dom.existElement(scalarElt, "quality"))
            return;
        
        Element qualityElt = dom.getElement(scalarElt, "quality/*");
        DataComponent quality = readScalar(dom, qualityElt);
        quality.setName("quality");
        
        dataComponent.setProperty(DataComponent.QUALITY, quality);
    }
    
    
    /**
     * Reads the constrain list for the given scalar component
     * @param dom
     * @param parameterElement
     * @return
     * @throws CDMException
     */
    private void readConstraints(DataComponent dataComponent, DOMHelper dom, Element scalarElement) throws CDMException
    {
    	NodeList constraintElts = dom.getElements(scalarElement, "constraint");
    	ConstraintList constraintList = new ConstraintList();
    	
    	for (int i=0; i<constraintElts.getLength(); i++)
    	{
    		SweConstraint constraint = null;
    		Element constraintElt = (Element)constraintElts.item(i);
    		
    		if (dom.existElement(constraintElt, "AllowedValues/interval"))
    			constraint = readIntervalConstraint(dom, constraintElt);
    		else if (dom.existElement(constraintElt, "AllowedValues/valueList"))
    			constraint = readNumberEnumConstraint(dom, constraintElt);
    		else if (dom.existElement(constraintElt, "AllowedValues/min"))
    			constraint = readMinMaxConstraint(dom, constraintElt);
    		else if (dom.existElement(constraintElt, "AllowedValues/max"))
    			constraint = readMinMaxConstraint(dom, constraintElt);
    		else if (dom.existElement(constraintElt, "AllowedTokens/valueList"))
    			constraint = readTokenEnumConstraint(dom, constraintElt);
    		
    		if (constraint != null)
    			constraintList.add(constraint);
    	}
    	
    	if (!constraintList.isEmpty())
    		dataComponent.setProperty(DataComponent.CONSTRAINTS, constraintList);
    }
    
    
    /**
     * Reads a numerical interval constraint
     * @param dom
     * @param constraintElement
     * @return
     */
    private IntervalConstraint readIntervalConstraint(DOMHelper dom, Element constraintElement) throws CDMException
    {
    	String rangeText = dom.getElementValue(constraintElement, "AllowedValues/interval");
    	
		try
		{
			String[] rangeValues = rangeText.split(" ");
			double min = Double.parseDouble(rangeValues[0]);
			double max = Double.parseDouble(rangeValues[1]);
			return new IntervalConstraint(min, max);
		}
		catch (Exception e)
		{
			throw new CDMException("Invalid Interval Constraint: " + rangeText);
		}
    }
    
    
    private IntervalConstraint readMinMaxConstraint(DOMHelper dom, Element constraintElement) throws CDMException
    {
    	String minText = dom.getElementValue(constraintElement, "AllowedValues/min");
    	String maxText = dom.getElementValue(constraintElement, "AllowedValues/max");
    	
		try
		{
			double min, max;
			
			if (minText != null)
				min = Double.parseDouble(minText);
			else
				min = Double.NEGATIVE_INFINITY;
			
			if (maxText != null)
				max = Double.parseDouble(maxText);
			else
				max = Double.POSITIVE_INFINITY;
			
			return new IntervalConstraint(min, max);
		}
		catch (Exception e)
		{
			throw new CDMException("Invalid Interval Constraint: min=" + minText + ", max=" + maxText);
		}
    }
    
    
    private NumberEnumConstraint readNumberEnumConstraint(DOMHelper dom, Element constraintElement) throws CDMException
    {
    	String values = dom.getElementValue(constraintElement, "AllowedValues/valueList");
    	
    	try
		{
			String[] valueList = values.split(" ");
			double[] valueArray = new double[valueList.length];
			
			for (int i=0; i<valueArray.length; i++)
				valueArray[i] = Double.parseDouble(valueList[i]);
			
			return new NumberEnumConstraint(valueArray);
		}
		catch (Exception e)
		{
			throw new CDMException("Invalid Number Enumeration constraint: " + values);
		}
    }
    
    
    private TokenEnumConstraint readTokenEnumConstraint(DOMHelper dom, Element constraintElement) throws CDMException
    {
    	String values = dom.getElementValue(constraintElement, "AllowedTokens/valueList");
    	String[] valueList = values.split(" ");
		return new TokenEnumConstraint(valueList);
    }    
}
