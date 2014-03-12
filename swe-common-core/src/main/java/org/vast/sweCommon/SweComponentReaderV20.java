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

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.xml.namespace.QName;
import org.w3c.dom.*;
import org.vast.cdm.common.*;
import org.vast.data.*;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.GMLUnitReader;
import org.vast.ogc.xlink.CachedReference;
import org.vast.ogc.xlink.XlinkUtils;
import org.vast.sweCommon.IntervalConstraint;
import org.vast.unit.Unit;
import org.vast.unit.UnitParserUCUM;
import org.vast.util.DateTimeFormat;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;


/**
 * <p>
 * Reads SWE Components structures made of Scalar Parameters,
 * DataRecord, DataArray, and hard typed derived structures.
 * This is for version 2.0 of the SWE Common specification.
 * This class is not thread-safe.
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin (Spot Image)
 * @since Feb 1, 2008
 * @version 1.0
 */
public class SweComponentReaderV20 implements DataComponentReader
{
	public final static String SWE_NS = OGCRegistry.getNamespaceURI(SWECommonUtils.SWE, "2.0");
	protected Hashtable<String, DataComponent> componentIds;
    protected AsciiDataParser asciiParser;
    protected SweEncodingReaderV20 encodingReader = new SweEncodingReaderV20();
    
    
    public SweComponentReaderV20()
    {
        componentIds = new Hashtable<String, DataComponent>();
        asciiParser = new AsciiDataParser();
    }


    public DataComponent readComponentProperty(DOMHelper dom, Element propertyElt) throws XMLReaderException
    {
        Element dataElement = dom.getFirstChildElement(propertyElt);
        DataComponent container = readComponent(dom, dataElement);
        String name = readPropertyName(dom, propertyElt);
        container.setName(name);
        
        // if href is present also parse and save xlink attr group
        CachedReference<?> xlinkOptions = new CachedReference<Object>();
        XlinkUtils.readXlinkAttributes(dom, propertyElt, xlinkOptions);        
        if (xlinkOptions.getHref() != null || xlinkOptions.getArcRole() != null || xlinkOptions.getRole() != null)
            container.setProperty(SweConstants.COMP_XLINK, xlinkOptions);
               
        return container;
    }
    
    
    public DataComponent readComponent(DOMHelper dom, Element componentElt) throws XMLReaderException
    {
        dom.addUserPrefix("swe", SWE_NS);
    	
    	DataComponent container = null;
    	String nsUri = componentElt.lookupNamespaceURI(componentElt.getPrefix());
        QName objectType = new QName(nsUri, componentElt.getLocalName(), componentElt.getPrefix());
        
    	// get element name
    	String eltName = componentElt.getLocalName();
    	
    	// call the right default method depending on type
    	if (eltName.equals(SweConstants.DATARECORD_COMPONENT_TAG))
        	container = readDataRecord(dom, componentElt);
    	else if (eltName.equals(SweConstants.VECTOR_COMPONENT_TAG))
        	container = readVector(dom, componentElt);
        else if (eltName.equals(SweConstants.DATAARRAY_COMPONENT_TAG) || eltName.equals(SweConstants.MATRIX_COMPONENT_TAG))
            container = readDataArray(dom, componentElt);
        else if (eltName.equals(SweConstants.DATASTREAM_COMPONENT_TAG))
            container = readDataStream(dom, componentElt);
        else if (eltName.equals(SweConstants.DATACHOICE_COMPONENT_TAG))
            container = readDataChoice(dom, componentElt);
        else if (eltName.endsWith("Range"))
            container = readRange(dom, componentElt);
        else // default to scalar
            container = readScalar(dom, componentElt);
        
        // set type to element QName        
        container.setProperty(SweConstants.COMP_QNAME, objectType);
        
        // add id to hashtable if present
        String id = dom.getAttributeValue(componentElt, "id");
        if (id != null)
        	componentIds.put(id, container);

        return container;
    }
    
    
    /**
     * Reads a generic (soft-typed) DataRecord structure and all its fields
     * @param recordElt Element
     * @throws CDMException
     * @return DataGroup
     */
    private DataGroup readDataRecord(DOMHelper dom, Element recordElt) throws XMLReaderException
    {
       // parse all fields elements
		NodeList fieldList = dom.getElements(recordElt, "swe:field");
        int fieldCount = fieldList.getLength();
        DataGroup dataGroup = new DataGroup(fieldCount);
		
		// loop through all fields
        for (int i = 0; i < fieldCount; i++)
        {
            Element fieldElt = (Element)fieldList.item(i);

            // add field components
            DataComponent dataComponent = readComponentProperty(dom, fieldElt);
            dataGroup.addComponent(readPropertyName(dom, fieldElt), dataComponent);
        }
    	
        // error if no field present
        if (dataGroup.getComponentCount() == 0)
            throw new XMLReaderException("Invalid DataRecord: Must have AT LEAST ONE field", recordElt);

        // read common stuffs
        readBaseProperties(dataGroup, dom, recordElt);
        readCommonAttributes(dataGroup, dom, recordElt);

        return dataGroup;
    }
    
    
    /**
     * Reads a generic (soft-typed) DataRecord structure and all its fields
     * @param recordElt Element
     * @throws CDMException
     * @return DataGroup
     */
    private DataGroup readVector(DOMHelper dom, Element vectorElt) throws XMLReaderException
    {
        // parse all fields elements
		NodeList coordList = dom.getElements(vectorElt, "swe:coordinate");
        int coordCount = coordList.getLength();
        DataGroup dataGroup = new DataGroup(coordCount);
		
		// loop through all coordinates
        for (int i = 0; i < coordCount; i++)
        {
            Element coordElt = (Element)coordList.item(i);

            // add coordinate components
            DataComponent dataComponent = readComponentProperty(dom, coordElt);
            dataGroup.addComponent(readPropertyName(dom, coordElt), dataComponent);
        }
    	
        // error if no coordinate present
        if (dataGroup.getComponentCount() == 0)
            throw new XMLReaderException("Invalid Vector: Must have AT LEAST ONE coordinate", vectorElt);

        // read common stuffs
        readBaseProperties(dataGroup, dom, vectorElt);
        readCommonAttributes(dataGroup, dom, vectorElt);

        return dataGroup;
    }
        
    
    /**
     * Reads a generic (soft-typed) DataChoice structure and all its items
     * @param dom
     * @param choiceElt
     * @return
     * @throws CDMException
     */
    private DataChoice readDataChoice(DOMHelper dom, Element choiceElt) throws XMLReaderException
    {
    	// parse all items elements
		NodeList itemList = dom.getElements(choiceElt, "swe:item");
        int itemCount = itemList.getLength();
		DataChoice dataChoice = new DataChoice(itemCount);
		
		// loop through all items
        for (int i = 0; i < itemCount; i++)
        {
            Element fieldElt = (Element)itemList.item(i);
            
            // add item components
            DataComponent dataComponent = readComponentProperty(dom, fieldElt);
            dataChoice.addComponent(readPropertyName(dom, fieldElt), dataComponent);
        }
        
        // read common stuffs
        readBaseProperties(dataChoice, dom, choiceElt);
        readCommonAttributes(dataChoice, dom, choiceElt);
        
    	return dataChoice;
    }
    
    
    /**
     * Reads a generic (soft-typed) DataArray structure
     * @param dom
     * @param arrayElt Element
     * @throws CDMException
     * @return DataArray
     */
    private DataArray readDataArray(DOMHelper dom, Element arrayElt) throws XMLReaderException
    {
        DataArray dataArray = null;
        
        // case of elementCount referencing another component
        String countId = dom.getAttributeValue(arrayElt, "elementCount/@href");
        if (countId != null)
        {
            DataComponent sizeComponent = componentIds.get(countId.replace("#", ""));
            if (sizeComponent == null)
                throw new XMLReaderException("Invalid elementCount: The elementCount property must reference an existing Count component", arrayElt);
            dataArray = new DataArray((DataValue)sizeComponent, true);
        }
        
        // case of elementCount with a Count inline
        else
        {
            try
            {
                Element countElt = dom.getElement(arrayElt, "elementCount/Count");
                DataValue sizeComponent = this.readScalar(dom, countElt);
                String countValue = dom.getElementValue(countElt, "value");
                
                // case of value provided -> fixed size
                if (countValue != null && !countValue.trim().equals(""))
                	dataArray = new DataArray(sizeComponent, false);
                
                // case of no value -> implicit variable size component
                else
                	dataArray = new DataArray(sizeComponent, true);             
            }
            catch (Exception e)
            {
                throw new XMLReaderException("Invalid elementCount: The elementCount must specify a positive integer value", arrayElt);
            }
        }
                        
        // read array component
        Element elementTypeElt = dom.getElement(arrayElt, "elementType");
        DataComponent dataComponent = readComponentProperty(dom, elementTypeElt);
        dataArray.addComponent(dataComponent);
        
        // read common stuffs
        readBaseProperties(dataArray, dom, arrayElt);
        readCommonAttributes(dataArray, dom, arrayElt);
        
        // read encoding and parse values (if both present) using the appropriate parser
        Element encodingElt = dom.getElement(arrayElt, "encoding");
        Element valuesElt = dom.getElement(arrayElt, "values");
        if (encodingElt != null && valuesElt != null)
            readArrayValues(dom, encodingElt, valuesElt, dataArray);
        
        return dataArray;
    }
    
    
    /**
     * Reads array values and fill up the data array
     * @param dom
     * @param encodingElt XML element containing encoding definition
     * @param valuesElt XML element containing the inline values
     * @param arrayObj DataArray object to fill with values (the component structure is obtained from the array definition)
     * @throws XMLReaderException
     */
    public void readArrayValues(DOMHelper dom, Element encodingElt, Element valuesElt, DataArray arrayObj) throws XMLReaderException
    {
        try
        {
            // TODO add support for XML encoding?
            DataEncoding encoding = encodingReader.readEncodingProperty(dom, encodingElt);
            DataStreamParser parser = SWEFactory.createDataParser(encoding);
            parser.setParentArray(arrayObj);
            InputStream is = new DataSourceDOM(dom, valuesElt).getDataStream();
            parser.parse(is);
            arrayObj.setProperty(SweConstants.ENCODING_TYPE, encoding);
        }
        catch (IOException e)
        {
            throw new XMLReaderException("Error while parsing array values", valuesElt, e);
        }
    }
    
    
    /**
     * Reads a DataStream and all its content
     * Data will be parsed only if included inline. If included through href, values are not parsed
     * but this can be done easily on demand by calling SWEData.parseData()
     * @param dom
     * @param streamElt
     * @return SWEData object containing component structure, encoding and values if included inline
     * @throws XMLReaderException
     */
    protected SWEData readDataStream(DOMHelper dom, Element streamElt) throws XMLReaderException
    {
        SWEData sweData = new SWEData();
        
        // read common stuff
        readBaseProperties(sweData, dom, streamElt);
        readCommonAttributes(sweData, dom, streamElt);
        
        // read stream component
        Element elementTypeElt = dom.getElement(streamElt, "elementType");
        DataComponent dataComponent = readComponentProperty(dom, elementTypeElt);
        sweData.setElementType(dataComponent);
                
        // read encoding
        Element encodingElt = dom.getElement(streamElt, "encoding");
        DataEncoding encoding = encodingReader.readEncodingProperty(dom, encodingElt);
        sweData.setEncoding(encoding);
        
        // parse values
        Element valuesElt = dom.getElement(streamElt, "values");
        if (valuesElt != null)
        {
            try
            {
                DataSource dataSrc;
                String href = dom.getAttributeValue(valuesElt, "href");
                if (href != null)
                {
                    dataSrc = new DataSourceURI(href);
                    sweData.setDataSource(dataSrc);
                }
                else
                {
                    dataSrc = new DataSourceDOM(dom, valuesElt);
                    sweData.parseData(dataSrc);
                }
            }
            catch (IOException e)
            {
                throw new XMLReaderException("Error while parsing stream values", e);
            }
        }
        
        return sweData;
    }
    
    
    /**
     * Reads a scalar value and atributes (Quantity, Count, Term...)
     * @param scalarElt
     * @return DataValue encapsulating the value
     * @throws SMLReaderException
     */
    private DataValue readScalar(DOMHelper dom, Element scalarElt) throws XMLReaderException
    {
        DataValue dataValue = null;              
        String eltName = scalarElt.getLocalName();
        
        // Create DataValue Object with appropriate type
    	if (eltName.equals(SweConstants.QUANTITY_COMPONENT_TAG) || eltName.equals(SweConstants.TIME_COMPONENT_TAG) || eltName.equals("ObservableProperty"))
    	    dataValue = new DataValue(DataType.DOUBLE);
        else if (eltName.equals(SweConstants.COUNT_COMPONENT_TAG))
            dataValue = new DataValue(DataType.INT);
        else if (eltName.equals(SweConstants.BOOL_COMPONENT_TAG))
        	dataValue = new DataValue(DataType.BOOLEAN);
        else if (eltName.equals(SweConstants.CATEGORY_COMPONENT_TAG) || eltName.equals(SweConstants.TEXT_COMPONENT_TAG))
        	dataValue = new DataValue(DataType.UTF_STRING);
        else
            throw new XMLReaderException("Invalid scalar component: " + eltName, scalarElt);
    	
    	// read common stuffs        
        readBaseProperties(dataValue, dom, scalarElt);
    	readCommonAttributes(dataValue, dom, scalarElt);
        readUom(dataValue, dom, scalarElt);
        readCodeSpace(dataValue, dom, scalarElt);
        readQuality(dataValue, dom, scalarElt);
        readConstraints(dataValue, dom, scalarElt);
        readNilValues(dataValue, dom, scalarElt);
        
        // Parse the value
        String value = dom.getElementValue(scalarElt, "value");
        if (value != null)
        {
        	dataValue.assignNewDataBlock();
        	try
            {
                asciiParser.parseToken(dataValue, value, '\0');
            }
            catch (IOException e)
            {
                throw new XMLReaderException(e.getMessage(), scalarElt);
            }
        }
        
        return dataValue;
    }
    
    
    /**
     * Reads a scalar range in a DataGroup structure
     * @param dom
     * @param rangeElt
     * @return
     * @throws CDMException
     */
    private DataGroup readRange(DOMHelper dom, Element rangeElt) throws XMLReaderException
    {
        DataValue paramVal = null;
        DataGroup range = new DataGroup(2);
        String eltName = rangeElt.getLocalName();
        
        // save range QName
    	QName compQName = new QName(rangeElt.getNamespaceURI(), rangeElt.getTagName());
    	range.setProperty(SweConstants.COMP_QNAME, compQName);
        
        // Create Data component Object
        if (eltName.startsWith(SweConstants.QUANTITY_COMPONENT_TAG))
        	paramVal = new DataValue(DataType.DOUBLE);
        else if (eltName.startsWith(SweConstants.COUNT_COMPONENT_TAG))
            paramVal = new DataValue(DataType.INT);
        else if (eltName.startsWith(SweConstants.TIME_COMPONENT_TAG))
            paramVal = new DataValue(DataType.DOUBLE);
        else if (eltName.startsWith(SweConstants.CATEGORY_COMPONENT_TAG))
            paramVal = new DataValue(DataType.UTF_STRING);
        else
            throw new XMLReaderException("Only Quantity, Time, Count and Category ranges are allowed", rangeElt);
        
        // generate fake QName for min/max components
        String localName = eltName.substring(0, eltName.indexOf("Range"));
        QName newQName = new QName(rangeElt.getNamespaceURI(), localName);
        paramVal.setProperty(SweConstants.COMP_QNAME, newQName);
        
        // read group attributes
        readBaseProperties(range, dom, rangeElt);
        readCommonAttributes(range, dom, rangeElt);
        
        // also assign attributes to scalar value
        readCommonAttributes(paramVal, dom, rangeElt);
        readUom(paramVal, dom, rangeElt);
        readCodeSpace(paramVal, dom, rangeElt);
        readQuality(paramVal, dom, rangeElt);
        readConstraints(paramVal, dom, rangeElt);
        
        // add params to DataGroup
        range.addComponent(SweConstants.MIN_VALUE_NAME, paramVal);
        range.addComponent(SweConstants.MAX_VALUE_NAME, paramVal.copy());
        
        // Parse the two values
        String valueText = dom.getElementValue(rangeElt, "value");
        if (valueText != null)
        {
            range.assignNewDataBlock();
            try
            {
                String[] vals = valueText.split(" ");
                asciiParser.parseToken((DataValue)range.getComponent(0), vals[0], '\0');
                asciiParser.parseToken((DataValue)range.getComponent(1), vals[1], '\0');
            }
            catch (Exception e)
            {
                throw new XMLReaderException("Error while parsing range values", rangeElt, e);
            }
        }
        
        return range;
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
    private void readBaseProperties(DataComponent dataComponent, DOMHelper dom, Element componentElt) throws XMLReaderException
    {
        // label
        String name = dom.getElementValue(componentElt, "swe:label");
        if (name != null)
            dataComponent.setProperty(SweConstants.NAME, name);
        
        // description
        String description = dom.getElementValue(componentElt, "swe:description");
        if (description != null)
            dataComponent.setProperty(SweConstants.DESC, description);
    }
    
    
    /**
     * Reads common component attributes 
     * (definition uri, reference frame, axisID, ...)
     * @param dataComponent DataContainer
     * @param dataElement Element
     */
    private void readCommonAttributes(DataComponent dataComponent, DOMHelper dom, Element componentElt) throws XMLReaderException
    {
    	// id
    	String id = dom.getAttributeValue(componentElt, "@id");
        if (id != null)
            dataComponent.setProperty(SweConstants.ID, id);
        
    	// definition URI
        String defUri = readComponentDefinition(dom, componentElt);
        if (defUri != null)
            dataComponent.setProperty(SweConstants.DEF_URI, defUri);
        
        // updatable flag
        Boolean updatable = getBooleanAttribute(dom, componentElt, "updatable");
        if (updatable != null)
        	dataComponent.setProperty(SweConstants.UPDATABLE, updatable);
        
        // optional flag
        Boolean optional = getBooleanAttribute(dom, componentElt, "optional");
        if (optional != null)
            dataComponent.setProperty(SweConstants.OPTIONAL, optional);
        
        // reference frame
        String refFrame = dom.getAttributeValue(componentElt, "referenceFrame");
        if (refFrame != null)
            dataComponent.setProperty(SweConstants.REF_FRAME, refFrame);
        
        // reference time
        try
        {
            String refTime = dom.getAttributeValue(componentElt, "referenceTime");
            if (refTime != null)
                dataComponent.setProperty(SweConstants.REF_TIME, DateTimeFormat.parseIso(refTime));
        }
        catch (ParseException e)
        {
            throw new XMLReaderException("Invalid reference time", componentElt, e);
        }
        
        // local frame
        String locFrame = dom.getAttributeValue(componentElt, "localFrame");
        if (locFrame != null)
            dataComponent.setProperty(SweConstants.LOCAL_FRAME, locFrame);
        
        // read axis code attribute
        String axisCode = dom.getAttributeValue(componentElt, "axisID");
        if (axisCode != null)
        	dataComponent.setProperty(SweConstants.AXIS_CODE, axisCode);
    }
    
    
    private Boolean getBooleanAttribute(DOMHelper dom, Element parentElt, String attName)
    {
    	String boolText = dom.getAttributeValue(parentElt, attName);
    	
    	if (boolText == null)
    		return null;
    	
    	boolean flag = boolText.equalsIgnoreCase("true") || boolText.equals("1");
    	return flag;
    }
    
    
    /**
     * Derives parameter definition URN from element name
     * @param componentElement
     * @throws SMLReaderException
     */
    private String readComponentDefinition(DOMHelper dom, Element componentElement) throws XMLReaderException
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
    private void readUom(DataComponent dataComponent, DOMHelper dom, Element scalarElt) throws XMLReaderException
    {
        if (!dom.existElement(scalarElt, "uom"))
            return;
        
        String ucumCode = dom.getAttributeValue(scalarElt, "uom/@code");                   
        String href = dom.getAttributeValue(scalarElt, "uom/@href");
        
        // uom code        
        if (ucumCode != null)
        {
            try
            {
                dataComponent.setProperty(SweConstants.UOM_CODE, ucumCode);
                
                // also create unit object
                UnitParserUCUM ucumParser = new UnitParserUCUM();
                Unit unit = ucumParser.getUnit(ucumCode);
                if (unit != null)
                    dataComponent.setProperty(SweConstants.UOM_OBJ, unit);
            }
            catch (Exception e)
            {
                throw new XMLReaderException("Error reading UCUM unit code", scalarElt, e);
            }
        }
        
        // if no code, read href
        else if (href != null)
        {
            dataComponent.setProperty(SweConstants.UOM_URI, href);
        }
        
        // inline unit
        else
        {
            try
            {
                Element unitElt = dom.getElement(scalarElt, "uom/*");
                GMLUnitReader unitReader = new GMLUnitReader();
                Unit unit = unitReader.readUnit(dom, unitElt);
                if (unit != null)
                    dataComponent.setProperty(SweConstants.UOM_OBJ, unit);            
                dataComponent.setProperty(SweConstants.UOM_CODE, unit.getCode());
            }
            catch (Exception e)
            {
                throw new XMLReaderException("Error reading inline unit", scalarElt, e);
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
    private void readCodeSpace(DataComponent dataComponent, DOMHelper dom, Element scalarElt) throws XMLReaderException
    {
        // codeSpace URI
        String codeSpaceUri = dom.getAttributeValue(scalarElt, "codeSpace/@href");
        if (codeSpaceUri != null)
            dataComponent.setProperty(SweConstants.DIC_URI, codeSpaceUri);
    }
    
    
    /**
     * Reads the quality component if present inline
     * @param dom
     * @param scalarElement
     * @throws CDMException
     */
    private void readQuality(DataComponent dataComponent, DOMHelper dom, Element scalarElt) throws XMLReaderException
    {
        if (!dom.existElement(scalarElt, "quality"))
            return;
        
        NodeList qualityElts = dom.getElements(scalarElt, "quality");
        int numQualityElts = qualityElts.getLength();
        List<DataComponent> qualityComponents = new ArrayList<DataComponent>(numQualityElts);        
        for (int i=0; i<numQualityElts; i++)
        {
            Element qualityElt = (Element)qualityElts.item(i);
            DataComponent quality = readComponentProperty(dom, qualityElt);
            qualityComponents.add(quality);
        }
        
        if (!qualityComponents.isEmpty())
            dataComponent.setProperty(SweConstants.QUALITY, qualityComponents);
    }
    
    
    /**
     * Reads the nilValues element if present
     * @param dataComponent
     * @param dom
     * @param scalarElt
     * @throws XMLReaderException
     */
    private void readNilValues(DataValue dataValue, DOMHelper dom, Element scalarElt) throws XMLReaderException
    {
        Element nilPropElt = dom.getElement(scalarElt, "nilValues");
        if (nilPropElt == null)
            return;
        
        Element nilValuesElt = dom.getElement(nilPropElt, "NilValues");
        NilValues nilValues = new NilValues();
        
        // id
        nilValues.setId(dom.getAttributeValue(nilValuesElt, "id"));
        
        // reason -> reserved value mapping
        NodeList nilElts = dom.getElements(nilValuesElt, "nilValue");
        int numNils = nilElts.getLength();
        for (int i=0; i<numNils; i++)
        {
            Element nilValElt = (Element)nilElts.item(i);
            String token = dom.getElementValue(nilValElt);
            Object val;
            
            try
            {
                switch (dataValue.getDataType())
                {
                    case BYTE:
                        val = Byte.parseByte(token);
                        break;
                        
                    case SHORT:
                    case UBYTE:
                        val = Short.parseShort(token);;
                        break;
                        
                    case INT:
                    case USHORT:
                        val = Integer.parseInt(token);
                        break;
                        
                    case LONG:
                    case UINT:
                    case ULONG:
                        val = Long.parseLong(token);
                        break;
                        
                    case FLOAT:
                        val = Float.parseFloat(token);
                        break;
                        
                    case DOUBLE:
                        val = AsciiDataParser.parseDoubleOrInfOrIsoTime(token);              
                        break;
                        
                    case UTF_STRING:
                    case ASCII_STRING:
                        val = token;
                        break;
                        
                    default:
                        throw new RuntimeException("Unssuported NIL value datatype " + dataValue.getDataType());
                }
                
                String reason = dom.getAttributeValue(nilValElt, "reason");
                nilValues.addNilValue(reason, val);
            }
            catch (Exception e)
            {
                throw new XMLReaderException("Incorrect NIL value '" + token + "' for component " + dataValue, scalarElt, e);
            }
        }
        
        dataValue.setProperty(SweConstants.NIL_VALUES, nilValues);
        
        // also parse xlink attr to rewrite properly
        if (dom.existAttribute(nilPropElt, "href"))
        {
            CachedReference<?> nilRef = new CachedReference<Object>();
            XlinkUtils.readXlinkAttributes(dom, nilPropElt, nilRef);
            dataValue.setProperty(SweConstants.NIL_XLINK, nilRef);
        }
    }
    
    
    /**
     * Reads the constrain list for the given scalar component
     * @param dom
     * @param parameterElement
     * @return
     * @throws CDMException
     */
    private void readConstraints(DataValue dataValue, DOMHelper dom, Element scalarElt) throws XMLReaderException
    {
    	Element constraintElt = dom.getElement(scalarElt, "constraint/*");
    	if (constraintElt == null)
    		return;
    	
    	ConstraintList constraintList = readConstraintList(dom, constraintElt);
        if (!constraintList.isEmpty())
            dataValue.setConstraints(constraintList);	
    }
    
    
    public ConstraintList readConstraintList(DOMHelper dom, Element constraintElt) throws XMLReaderException
    {    	
    	boolean tokenConstraints = dom.hasQName(constraintElt, "swe:AllowedTokens");
    	boolean timeConstraints = dom.hasQName(constraintElt, "swe:AllowedTimes");
    	ConstraintList constraintList = new ConstraintList();
    	DataConstraint constraint = null;
    	
    	if (tokenConstraints)
		{
			Element patternElt = dom.getElement(constraintElt, "pattern");
			if (patternElt != null)
			{
				constraint = new PatternConstraint(dom.getElementValue(patternElt));
				constraintList.add(constraint);
			}
			
    		NodeList valueElts = dom.getElements(constraintElt, "value");
    		if (valueElts.getLength() > 0)
    		{
	    		String[] values = new String[valueElts.getLength()];
	    		for (int i=0; i<valueElts.getLength(); i++)
	    			values[i] = dom.getElementValue((Element)valueElts.item(i));
	    		constraintList.add(new EnumTokenConstraint(values));
    		}
		}
		else if (timeConstraints)
		{
			NodeList intervalElts = dom.getElements(constraintElt, "interval");
    		for (int i=0; i<intervalElts.getLength(); i++)
    		{
    			constraint = readTimeIntervalConstraint(dom, (Element)intervalElts.item(i));
	    		constraintList.add(constraint);
    		}
			
			NodeList valueElts = dom.getElements(constraintElt, "value");
    		if (valueElts.getLength() > 0)
    		{
	    		double[] values = new double[valueElts.getLength()];
	    		for (int i=0; i<valueElts.getLength(); i++)
	    		{
	    			String val = dom.getElementValue((Element)valueElts.item(i));
    				try
		        	{
		    			values[i] = AsciiDataParser.parseDoubleOrInfOrIsoTime(val);
		        	}
                    catch (Exception e)
                    {
                        throw new XMLReaderException("Invalid time enumeration constraint: " + val, constraintElt);
                    }
	    		}
	    		constraintList.add(new EnumNumberConstraint(values));
    		}
		}
		else
		{
			NodeList intervalElts = dom.getElements(constraintElt, "interval");
    		for (int i=0; i<intervalElts.getLength(); i++)
    		{
    			constraint = readIntervalConstraint(dom, (Element)intervalElts.item(i));
	    		constraintList.add(constraint);
    		}
			    		
    		NodeList valueElts = dom.getElements(constraintElt, "value");
    		if (valueElts.getLength() > 0)
    		{
	    		double[] values = new double[valueElts.getLength()];
	    		for (int i=0; i<valueElts.getLength(); i++)
	    		{
	    			String val = dom.getElementValue((Element)valueElts.item(i));
    				try
		        	{
		    			values[i] = AsciiDataParser.parseDoubleOrInf(val);
		    		}
					catch (Exception e)
					{
						throw new XMLReaderException("Invalid number enumeration constraint: " + val, constraintElt);
					}
	    		}
	    		constraintList.add(new EnumNumberConstraint(values));
    		}
    		
    		// TODO read significantFigures
		}
    	
    	return constraintList;
    }
    
    
    /**
     * Reads a numerical interval constraint
     * @param dom
     * @param constraintElement
     * @return
     */
    private IntervalConstraint readIntervalConstraint(DOMHelper dom, Element constraintElt) throws XMLReaderException
    {
    	String rangeText = dom.getElementValue(constraintElt);
    	
		try
		{
			String[] rangeValues = rangeText.split(" ");
			double min = AsciiDataParser.parseDoubleOrInf(rangeValues[0]);
			double max = AsciiDataParser.parseDoubleOrInf(rangeValues[1]);
			return new IntervalConstraint(min, max);
		}
		catch (Exception e)
		{
			throw new XMLReaderException("Invalid interval constraint: " + rangeText, constraintElt);
		}
    }
    
    
    /**
     * Reads a time interval constraint
     * @param dom
     * @param constraintElement
     * @return
     */
    private IntervalConstraint readTimeIntervalConstraint(DOMHelper dom, Element constraintElt) throws XMLReaderException
    {
    	String rangeText = dom.getElementValue(constraintElt);
    	
		try
		{
			String[] rangeValues = rangeText.split(" ");
			double min = AsciiDataParser.parseDoubleOrInfOrIsoTime(rangeValues[0]);
			double max = AsciiDataParser.parseDoubleOrInfOrIsoTime(rangeValues[1]);
			return new IntervalConstraint(min, max);
		}
		catch (Exception e)
		{
			throw new XMLReaderException("Invalid time interval constraint: " + rangeText, constraintElt);
		}
    }
}
