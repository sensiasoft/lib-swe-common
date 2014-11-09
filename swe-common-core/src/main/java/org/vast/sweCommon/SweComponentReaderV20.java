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
import java.util.Hashtable;
import net.opengis.DateTimeDouble;
import net.opengis.IDateTime;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.AbstractDataComponent;
import net.opengis.swe.v20.AbstractEncoding;
import net.opengis.swe.v20.AbstractSimpleComponent;
import net.opengis.swe.v20.AllowedTimes;
import net.opengis.swe.v20.HasCodeSpace;
import net.opengis.swe.v20.HasConstraints;
import net.opengis.swe.v20.HasRefFrames;
import net.opengis.swe.v20.HasUom;
import org.w3c.dom.*;
import org.vast.cdm.common.*;
import org.vast.data.*;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.xlink.XlinkUtils;
import org.vast.unit.Unit;
import org.vast.unit.UnitParserUCUM;
import org.vast.util.DateTimeFormat;
import org.vast.xml.DOMHelper;
import org.vast.xml.IXMLReaderDOM;
import org.vast.xml.XMLReaderException;


/**
 * <p>
 * Reads SWE Components structures made of Scalar Parameters, DataRecord,
 * DataArray, and hard typed derived structures from a DOM tree.
 * This is for version 2.0 of the SWE Common specification.
 * This class is not thread-safe.
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin (Spot Image)
 * @since Feb 1, 2008
 * @version 1.0
 */
public class SweComponentReaderV20 implements IXMLReaderDOM<DataComponent>
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
    
    
    @Override
    public DataComponent read(DOMHelper dom, Element componentElt) throws XMLReaderException
    {
        dom.addUserPrefix("swe", SWE_NS);
    	
    	DataComponent component = null;
        String eltName = componentElt.getLocalName();
    	
    	// call the right default method depending on type
    	if (eltName.equals(SweConstants.DATARECORD_COMPONENT_TAG))
        	component = readDataRecord(dom, componentElt);
    	else if (eltName.equals(SweConstants.VECTOR_COMPONENT_TAG))
        	component = readVector(dom, componentElt);
        else if (eltName.equals(SweConstants.DATAARRAY_COMPONENT_TAG) || eltName.equals(SweConstants.MATRIX_COMPONENT_TAG))
            component = readDataArray(dom, componentElt);
        else if (eltName.equals(SweConstants.DATASTREAM_COMPONENT_TAG))
            component = readDataStream(dom, componentElt);
        else if (eltName.equals(SweConstants.DATACHOICE_COMPONENT_TAG))
            component = readDataChoice(dom, componentElt);
        else if (eltName.endsWith("Range"))
            component = readRange(dom, componentElt);
        else // default to scalar
            component = readScalar(dom, componentElt);
        
        // add id to hashtable if present
        String id = dom.getAttributeValue(componentElt, "id");
        if (id != null)
        	componentIds.put(id, component);

        return component;
    }
    
    
    public void readComponentProperty(OgcProperty<AbstractDataComponent> prop, DOMHelper dom, Element propertyElt) throws XMLReaderException
    {
        // name attribute
        String name = readPropertyName(dom, propertyElt);
        if (name != null)
            prop.setName(name);
        
        // property value
        Element dataElement = dom.getFirstChildElement(propertyElt);
        if (dataElement != null)
        {
            DataComponent component = read(dom, dataElement);
            prop.setValue((AbstractDataComponent)component);
        }
        
        // also parse and save xlink attributes if present
        XlinkUtils.readXlinkAttributes(dom, propertyElt, prop);   
    }
    
    
    /**
     * Reads a generic (soft-typed) DataRecord structure and all its fields
     * @param recordElt Element
     * @throws CDMException
     * @return DataGroup
     */
    private DataRecordImpl readDataRecord(DOMHelper dom, Element recordElt) throws XMLReaderException
    {
       // parse all fields elements
		NodeList fieldList = dom.getElements(recordElt, "swe:field");
        int fieldCount = fieldList.getLength();
        DataRecordImpl dataRecord = new DataRecordImpl(fieldCount);
		
		// loop through all fields
        for (int i = 0; i < fieldCount; i++)
        {
            Element fieldElt = (Element)fieldList.item(i);

            // add field components
            OgcProperty<AbstractDataComponent> fieldProp = new OgcPropertyImpl<AbstractDataComponent>();
            readComponentProperty(fieldProp, dom, fieldElt);
            dataRecord.getFieldList().add(fieldProp);
        }
    	
        // error if no field present
        if (dataRecord.getComponentCount() == 0)
            throw new XMLReaderException("Invalid DataRecord: Must have AT LEAST ONE field", recordElt);

        // read common stuffs
        readBaseProperties(dataRecord, dom, recordElt);
        readCommonAttributes(dataRecord, dom, recordElt);

        return dataRecord;
    }
    
    
    /**
     * Reads a generic (soft-typed) DataRecord structure and all its fields
     * @param recordElt Element
     * @throws CDMException
     * @return DataGroup
     */
    @SuppressWarnings("rawtypes")
    private VectorImpl readVector(DOMHelper dom, Element vectorElt) throws XMLReaderException
    {
        // parse all fields elements
		NodeList coordList = dom.getElements(vectorElt, "swe:coordinate");
        int coordCount = coordList.getLength();
        VectorImpl vector = new VectorImpl(coordCount);
		
		// loop through all coordinates
        for (int i = 0; i < coordCount; i++)
        {
            Element coordElt = (Element)coordList.item(i);

            // add coordinate components
            OgcProperty fieldProp = new OgcPropertyImpl();
            readComponentProperty(fieldProp, dom, coordElt);
            vector.getCoordinateList().add(fieldProp);
        }
    	
        // error if no coordinate present
        if (vector.getComponentCount() == 0)
            throw new XMLReaderException("Invalid Vector: Must have AT LEAST ONE coordinate", vectorElt);

        // read common stuffs
        readBaseProperties(vector, dom, vectorElt);
        readCommonAttributes(vector, dom, vectorElt);

        return vector;
    }
        
    
    /**
     * Reads a generic (soft-typed) DataChoice structure and all its items
     * @param dom
     * @param choiceElt
     * @return
     * @throws CDMException
     */
    private DataChoiceImpl readDataChoice(DOMHelper dom, Element choiceElt) throws XMLReaderException
    {
    	// parse all items elements
		NodeList itemList = dom.getElements(choiceElt, "swe:item");
        int itemCount = itemList.getLength();
        DataChoiceImpl dataChoice = new DataChoiceImpl(itemCount);
		
		// loop through all items
        for (int i = 0; i < itemCount; i++)
        {
            Element itemElt = (Element)itemList.item(i);
            
            // add item components
            OgcProperty<AbstractDataComponent> itemProp = new OgcPropertyImpl<AbstractDataComponent>();
            readComponentProperty(itemProp, dom, itemElt);
            dataChoice.getItemList().add(itemProp);
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
     * @return 
     */
    private AbstractArrayImpl readDataArray(DOMHelper dom, Element arrayElt) throws XMLReaderException
    {
        DataArrayImpl dataArray;
        
        if (arrayElt.getLocalName().equals(SweConstants.MATRIX_COMPONENT_TAG))
            dataArray = new MatrixImpl();
        else
            dataArray = new DataArrayImpl();
        
        // case of elementCount referencing another component
        String countId = dom.getAttributeValue(arrayElt, "elementCount/@href");
        if (countId != null)
        {
            DataComponent sizeComponent = componentIds.get(countId.replace("#", ""));
            if (sizeComponent == null)
                throw new XMLReaderException("Invalid elementCount: The elementCount property must reference an existing Count component", arrayElt);
            dataArray.setElementCount((CountImpl)sizeComponent);
        }
        
        // case of elementCount with a Count inline
        else
        {
            try
            {
                Element countElt = dom.getElement(arrayElt, "elementCount/Count");
                DataValue sizeComponent = this.readScalar(dom, countElt);
                dataArray.setElementCount((CountImpl)sizeComponent);
            }
            catch (Exception e)
            {
                throw new XMLReaderException("Invalid elementCount: The elementCount must specify a positive integer value", arrayElt);
            }
        }
                        
        // read array component
        Element elementTypeElt = dom.getElement(arrayElt, "elementType");
        OgcProperty<AbstractDataComponent> eltTypeProp = dataArray.getElementTypeProperty();
        readComponentProperty(eltTypeProp, dom, elementTypeElt);
        
        // read common stuffs
        readBaseProperties(dataArray, dom, arrayElt);
        readCommonAttributes(dataArray, dom, arrayElt);
        
        // read encoding and parse values (if both present) using the appropriate parser
        Element encodingElt = dom.getElement(arrayElt, "encoding/*");
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
    public void readArrayValues(DOMHelper dom, Element encodingElt, Element valuesElt, DataArrayImpl arrayObj) throws XMLReaderException
    {
        try
        {
            // TODO add support for XML encoding?
            AbstractEncoding encoding = encodingReader.read(dom, encodingElt);
            DataStreamParser parser = SWEFactory.createDataParser(encoding);
            parser.setParentArray(arrayObj);
            InputStream is = new DataSourceDOM(dom, valuesElt).getDataStream();
            parser.parse(is);
            arrayObj.setEncoding(encoding);
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
        
        // read element count
        Element countElt = dom.getElement(streamElt, "elementCount/Count");
        if (countElt != null)
        {
            DataValue sizeComponent = this.readScalar(dom, countElt);
            sweData.setElementCount((CountImpl)sizeComponent);
        }
        
        // read stream component
        Element elementTypeElt = dom.getElement(streamElt, "elementType");
        OgcProperty<AbstractDataComponent> eltTypeProp = sweData.getElementTypeProperty();
        readComponentProperty(eltTypeProp, dom, elementTypeElt);
        
        // read encoding
        Element encodingElt = dom.getElement(streamElt, "encoding/*");
        AbstractEncoding encoding = encodingReader.read(dom, encodingElt);
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
    	if (eltName.equals(SweConstants.QUANTITY_COMPONENT_TAG) || eltName.equals("ObservableProperty"))
    	    dataValue = new QuantityImpl();
    	else if (eltName.equals(SweConstants.TIME_COMPONENT_TAG))
            dataValue = new TimeImpl();
        else if (eltName.equals(SweConstants.COUNT_COMPONENT_TAG))
            dataValue = new CountImpl();
        else if (eltName.equals(SweConstants.BOOL_COMPONENT_TAG))
        	dataValue = new BooleanImpl();
        else if (eltName.equals(SweConstants.CATEGORY_COMPONENT_TAG))
        	dataValue = new CategoryImpl();
        else if (eltName.equals(SweConstants.TEXT_COMPONENT_TAG))
            dataValue = new TextImpl();
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
    private AbstractSimpleComponentImpl readRange(DOMHelper dom, Element rangeElt) throws XMLReaderException
    {
        String eltName = rangeElt.getLocalName();
        AbstractRangeComponentImpl range;
        
        // Create Data component Object
        if (eltName.startsWith(SweConstants.QUANTITY_COMPONENT_TAG))
            range = new QuantityRangeImpl();
        else if (eltName.startsWith(SweConstants.COUNT_COMPONENT_TAG))
            range = new CountRangeImpl();
        else if (eltName.startsWith(SweConstants.TIME_COMPONENT_TAG))
            range = new TimeRangeImpl();
        else if (eltName.startsWith(SweConstants.CATEGORY_COMPONENT_TAG))
            range = new CategoryRangeImpl();
        else
            throw new XMLReaderException("Only Quantity, Time, Count and Category ranges are allowed", rangeElt);
        
        // read group attributes
        readBaseProperties(range, dom, rangeElt);
        readCommonAttributes(range, dom, rangeElt);
        
        // also assign attributes to scalar value
        readCommonAttributes(range, dom, rangeElt);
        readUom(range, dom, rangeElt);
        readCodeSpace(range, dom, rangeElt);
        readQuality(range, dom, rangeElt);
        readConstraints(range, dom, rangeElt);
        readNilValues(range, dom, rangeElt);
        
        // Parse the two values
        String valueText = dom.getElementValue(rangeElt, "value");
        if (valueText != null)
        {
            range.assignNewDataBlock();
            try
            {
                String[] vals = valueText.split(" ");
                asciiParser.parseToken(range, vals[0], '.', range.getData(), 0);
                asciiParser.parseToken(range, vals[1], '.', range.getData(), 1);
            }
            catch (Exception e)
            {
                throw new XMLReaderException("Error while parsing range values", rangeElt, e);
            }
        }
        
        return range;
    }
    
    
    /*
     * Reads name from element name or 'name' attribute
     */
    private String readPropertyName(DOMHelper dom, Element propertyElt)
    {
        return dom.getAttributeValue(propertyElt, "name");
    }
    
    
    /*
     * Reads gml properties and attributes common to all SWE components
     */
    private void readBaseProperties(AbstractDataComponentImpl dataComponent, DOMHelper dom, Element componentElt) throws XMLReaderException
    {
        // label
        String label = dom.getElementValue(componentElt, "swe:label");
        if (label != null)
            dataComponent.setLabel(label);
        
        // description
        String description = dom.getElementValue(componentElt, "swe:description");
        if (description != null)
            dataComponent.setDescription(description);
    }
    
    
    /*
     * Reads common component attributes 
     */
    private void readCommonAttributes(AbstractDataComponentImpl dataComponent, DOMHelper dom, Element componentElt) throws XMLReaderException
    {
    	// id
    	String id = dom.getAttributeValue(componentElt, "@id");
        if (id != null)
            dataComponent.setId(id);
        
    	// definition URI
        String defUri = dom.getAttributeValue(componentElt, "definition");
        if (defUri != null)
            dataComponent.setDefinition(defUri);
        
        // updatable flag
        Boolean updatable = getBooleanAttribute(dom, componentElt, "updatable");
        if (updatable != null)
        	dataComponent.setUpdatable(updatable);
        
        // optional flag
        Boolean optional = getBooleanAttribute(dom, componentElt, "optional");
        if (optional != null)
            dataComponent.setOptional(optional);
        
        // reference time
        try
        {
            String refTime = dom.getAttributeValue(componentElt, "referenceTime");
            if (refTime != null && dataComponent instanceof TimeImpl)
                ((TimeImpl)dataComponent).setReferenceTime(new DateTimeDouble(DateTimeFormat.parseIso(refTime)));
        }
        catch (ParseException e)
        {
            throw new XMLReaderException("Invalid reference time", componentElt, e);
        }
        
        // reference frame
        String refFrame = dom.getAttributeValue(componentElt, "referenceFrame");
        if (refFrame != null && dataComponent instanceof DataValue)
            ((DataValue)dataComponent).setReferenceFrame(refFrame);
        else if (refFrame != null && dataComponent instanceof HasRefFrames)
            ((HasRefFrames)dataComponent).setReferenceFrame(refFrame);
        
        // local frame
        String localFrame = dom.getAttributeValue(componentElt, "localFrame");
        if (localFrame != null && dataComponent instanceof HasRefFrames)
            ((HasRefFrames)dataComponent).setLocalFrame(localFrame);
        
        // read axis code attribute
        String axisCode = dom.getAttributeValue(componentElt, "axisID");
        if (axisCode != null && dataComponent instanceof DataValue)
        	((DataValue)dataComponent).setAxisID(axisCode);
    }
    
    
    private Boolean getBooleanAttribute(DOMHelper dom, Element parentElt, String attName)
    {
    	String boolText = dom.getAttributeValue(parentElt, attName);
    	
    	if (boolText == null)
    		return null;
    	
    	boolean flag = boolText.equalsIgnoreCase("true") || boolText.equals("1");
    	return flag;
    }
    
    
    private void readUom(AbstractDataComponentImpl dataComponent, DOMHelper dom, Element scalarElt) throws XMLReaderException
    {
        if (!(dataComponent instanceof HasUom))
            return;
        
        if (!dom.existElement(scalarElt, "uom"))
            return;
        
        String ucumCode = dom.getAttributeValue(scalarElt, "uom/@code");
        String href = dom.getAttributeValue(scalarElt, "uom/@href");
        UnitReferenceImpl uom = new UnitReferenceImpl();
        
        // uom code        
        if (ucumCode != null)
        {
            try
            {
                uom.setCode(ucumCode);
                
                // also create unit object
                UnitParserUCUM ucumParser = new UnitParserUCUM();
                Unit unit = ucumParser.getUnit(ucumCode);
                if (unit != null)
                    uom.setUnitObject(unit);
            }
            catch (Exception e)
            {
                throw new XMLReaderException("Error reading UCUM unit code", scalarElt, e);
            }
        }
        
        // if no code, read href
        else if (href != null)
        {
            uom.setHref(href);
        }
        
        ((HasUom)dataComponent).setUom(uom);
    }
    
    
    private void readCodeSpace(AbstractSimpleComponent dataComponent, DOMHelper dom, Element scalarElt) throws XMLReaderException
    {
        if (!(dataComponent instanceof HasCodeSpace))
            return;
        
        // codeSpace URI
        String codeSpaceUri = dom.getAttributeValue(scalarElt, "codeSpace/@href");
        if (codeSpaceUri != null)
            ((HasCodeSpace)dataComponent).setCodeSpace(codeSpaceUri);
    }
    
    
    @SuppressWarnings("rawtypes")
    private void readQuality(AbstractSimpleComponent dataComponent, DOMHelper dom, Element scalarElt) throws XMLReaderException
    {
        if (!dom.existElement(scalarElt, "quality"))
            return;
        
        NodeList qualityElts = dom.getElements(scalarElt, "quality");
        int numQualityElts = qualityElts.getLength();
        for (int i=0; i<numQualityElts; i++)
        {
            Element qualityElt = (Element)qualityElts.item(i);
            OgcProperty qualityProp = new OgcPropertyImpl();
            readComponentProperty(qualityProp, dom, qualityElt);
            dataComponent.getQualityList().add(qualityProp);
        }
    }
    
    
    private void readNilValues(AbstractSimpleComponentImpl dataValue, DOMHelper dom, Element scalarElt) throws XMLReaderException
    {
        Element nilPropElt = dom.getElement(scalarElt, "nilValues");
        if (nilPropElt == null)
            return;
        
        Element nilValuesElt = dom.getElement(nilPropElt, "NilValues");
        NilValuesImpl nilValues = new NilValuesImpl();
        
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
                nilValues.addNilValue(new NilValueImpl(reason, val));
            }
            catch (Exception e)
            {
                throw new XMLReaderException("Incorrect NIL value '" + token + "' for component " + dataValue, scalarElt, e);
            }
        }
        
        dataValue.setNilValues(nilValues);
        
        // also parse xlink attr to rewrite properly
        if (dom.existAttribute(nilPropElt, "href"))
            XlinkUtils.readXlinkAttributes(dom, nilPropElt, dataValue.getNilValuesProperty());
    }
    
    
    @SuppressWarnings("rawtypes")
    private void readConstraints(AbstractSimpleComponent dataValue, DOMHelper dom, Element scalarElt) throws XMLReaderException
    {
        if (!(dataValue instanceof HasConstraints))
            return;
        
        Element constraintElt = dom.getElement(scalarElt, "constraint/*");
    	if (constraintElt == null)
    		return;
    	
    	boolean tokenConstraints = dom.hasQName(constraintElt, "swe:AllowedTokens");
    	boolean numericConstraints = dom.hasQName(constraintElt, "swe:AllowedValues");
        boolean timeConstraints = dom.hasQName(constraintElt, "swe:AllowedTimes");
    	
    	if (tokenConstraints)
		{
			AllowedTokensImpl constraint = new AllowedTokensImpl();
			
    	    Element patternElt = dom.getElement(constraintElt, "pattern");
			if (patternElt != null)
			    constraint.setPattern(dom.getElementValue(patternElt));
						
    		NodeList valueElts = dom.getElements(constraintElt, "value");
    		if (valueElts.getLength() > 0)
    		{
	    		for (int i=0; i<valueElts.getLength(); i++)
	    		    constraint.addValue(dom.getElementValue((Element)valueElts.item(i)));
    		}
    		
    		((HasConstraints)dataValue).setConstraint(constraint);
		}
		else if (timeConstraints)
		{
		    AllowedTimesImpl constraint = new AllowedTimesImpl();
            
            NodeList intervalElts = dom.getElements(constraintElt, "interval");
    		for (int i=0; i<intervalElts.getLength(); i++)
    		    readTimeIntervalConstraint(constraint, dom, (Element)intervalElts.item(i));
			
			NodeList valueElts = dom.getElements(constraintElt, "value");
    		if (valueElts.getLength() > 0)
    		{
	    		for (int i=0; i<valueElts.getLength(); i++)
	    		{
	    			String valText = dom.getElementValue((Element)valueElts.item(i));
    				try
		        	{
		    			double val = AsciiDataParser.parseDoubleOrInfOrIsoTime(valText);
		    			constraint.addValue(new DateTimeDouble(val));
		        	}
                    catch (Exception e)
                    {
                        throw new XMLReaderException("Invalid time enumeration constraint: " + valText, constraintElt);
                    }
	    		}
    		}
    		
    		((HasConstraints)dataValue).setConstraint(constraint);
		}
        else if (numericConstraints)
		{
		    AllowedValuesImpl constraint = new AllowedValuesImpl();
            
		    NodeList intervalElts = dom.getElements(constraintElt, "interval");
    		for (int i=0; i<intervalElts.getLength(); i++)
    		    readIntervalConstraint(constraint, dom, (Element)intervalElts.item(i));
			    		
    		NodeList valueElts = dom.getElements(constraintElt, "value");
    		if (valueElts.getLength() > 0)
    		{
	    		for (int i=0; i<valueElts.getLength(); i++)
	    		{
	    			String valText = dom.getElementValue((Element)valueElts.item(i));
    				try
		        	{
		    			double val = AsciiDataParser.parseDoubleOrInf(valText);
		    			constraint.addValue(val);
		    		}
					catch (Exception e)
					{
						throw new XMLReaderException("Invalid number enumeration constraint: " + valText, constraintElt);
					}
	    		}
    		}
    		
    		// TODO read significantFigures
    		
    		((HasConstraints)dataValue).setConstraint(constraint);
		}
    }
    
    
    private void readIntervalConstraint(AllowedValuesImpl constraint, DOMHelper dom, Element constraintElt) throws XMLReaderException
    {
    	String rangeText = dom.getElementValue(constraintElt);
    	
		try
		{
			String[] rangeValues = rangeText.split(" ");
			double min = AsciiDataParser.parseDoubleOrInf(rangeValues[0]);
			double max = AsciiDataParser.parseDoubleOrInf(rangeValues[1]);
			constraint.addInterval(new double[] {min, max});
		}
		catch (Exception e)
		{
			throw new XMLReaderException("Invalid interval constraint: " + rangeText, constraintElt);
		}
    }
    
    
    private void readTimeIntervalConstraint(AllowedTimes constraint, DOMHelper dom, Element constraintElt) throws XMLReaderException
    {
    	String rangeText = dom.getElementValue(constraintElt);
    	
		try
		{
			String[] rangeValues = rangeText.split(" ");
			double min = AsciiDataParser.parseDoubleOrInfOrIsoTime(rangeValues[0]);
			double max = AsciiDataParser.parseDoubleOrInfOrIsoTime(rangeValues[1]);
			constraint.addInterval(new IDateTime[] {new DateTimeDouble(min), new DateTimeDouble(max)});
		}
		catch (Exception e)
		{
			throw new XMLReaderException("Invalid time interval constraint: " + rangeText, constraintElt);
		}
    }
}
