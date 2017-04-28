/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32.bind;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import net.opengis.AbstractXMLStreamBindings;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.gml.v32.AbstractCurve;
import net.opengis.gml.v32.AbstractFeature;
import net.opengis.gml.v32.AbstractGML;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.AbstractSurface;
import net.opengis.gml.v32.AbstractTimeGeometricPrimitive;
import net.opengis.gml.v32.AbstractTimePrimitive;
import net.opengis.gml.v32.CodeWithAuthority;
import net.opengis.gml.v32.CodeList;
import net.opengis.gml.v32.CodeOrNilReasonList;
import net.opengis.gml.v32.Envelope;
import net.opengis.gml.v32.FeatureCollection;
import net.opengis.gml.v32.LineString;
import net.opengis.gml.v32.LinearRing;
import net.opengis.gml.v32.Point;
import net.opengis.gml.v32.Polygon;
import net.opengis.gml.v32.Reference;
import net.opengis.gml.v32.TimeIndeterminateValue;
import net.opengis.gml.v32.TimeInstant;
import net.opengis.gml.v32.TimeIntervalLength;
import net.opengis.gml.v32.TimePeriod;
import net.opengis.gml.v32.TimePosition;
import net.opengis.gml.v32.Factory;
import net.opengis.gml.v32.TimeUnit;


@SuppressWarnings("javadoc")
public class XMLStreamBindings extends AbstractXMLStreamBindings
{
    public final static String NS_URI = "http://www.opengis.net/gml/3.2";
    protected Factory factory;
    
    
    public XMLStreamBindings(Factory factory)
    {
        this.factory = factory;
    }
    
    
    /**
     * Reads AbstractFeatureType content to a concrete feature instance
     */
    public AbstractFeature readAbstractFeatureType(XMLStreamReader reader, AbstractFeature bean) throws XMLStreamException
    {
        Map<String, String> attrMap = collectAttributes(reader);
        this.readAbstractFeatureTypeAttributes(attrMap, bean);
        this.readAbstractFeatureTypeElements(reader, bean);
        return bean;
    }
    
    
    /**
     * Reads attributes of AbstractFeatureType complex type
     */
    public void readAbstractFeatureTypeAttributes(Map<String, String> attrMap, AbstractFeature bean) throws XMLStreamException
    {
        this.readAbstractGMLTypeAttributes(attrMap, bean);        
    }
    
    
    /**
     * Reads elements of AbstractFeatureType complex type
     */
    public void readAbstractFeatureTypeElements(XMLStreamReader reader, AbstractFeature bean) throws XMLStreamException
    {
        this.readAbstractGMLTypeElements(reader, bean);
        
        boolean found;
        
        // boundedBy
        found = checkElementName(reader, "boundedBy");
        if (found)
        {
            reader.nextTag();
            String localName = reader.getName().getLocalPart();
            
            if (localName.equals("Envelope"))
            {
                Envelope boundedBy = this.readEnvelope(reader);
                bean.setBoundedByAsEnvelope(boundedBy);
            }
            /*else if (localName.equals("Object"))
            {
                Object boundedBy = this.readNull(reader);
                bean.setBoundedByAsNull(boundedBy);
            }*/
            else
                throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
            
            reader.nextTag();
            reader.nextTag();
        }
        
        // location
        found = checkElementName(reader, "location");
        if (found)
        {
            reader.nextTag();
            
            AbstractGeometry location = this.readAbstractGeometry(reader);
            bean.setLocation(location);
            
            reader.nextTag();
            reader.nextTag();
        }
    }
    

    /**
     * Writes attributes of AbstractFeatureType complex type
     */
    public void writeAbstractFeatureTypeAttributes(XMLStreamWriter writer, AbstractFeature bean) throws XMLStreamException
    {
        this.writeAbstractGMLTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of AbstractFeatureType complex type
     */
    public void writeAbstractFeatureTypeElements(XMLStreamWriter writer, AbstractFeature bean) throws XMLStreamException
    {
        this.writeAbstractGMLTypeElements(writer, bean);
        
        // boundedBy
        if (bean.isSetBoundedBy())
        {
            writer.writeStartElement(NS_URI, "boundedBy");
            this.writeEnvelope(writer, bean.getBoundedBy());
            writer.writeEndElement();
        }
        
        // location
        if (bean.isSetLocation())
        {
            writer.writeStartElement(NS_URI, "location");
            writePropertyAttributes(writer, bean.getLocationProperty());            
            if (bean.getLocationProperty().hasValue())
                this.writeAbstractGeometry(writer, bean.getLocation());            
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Reads attributes of AbstractFeatureCollectionType complex type
     */
    public void readFeatureCollectionTypeAttributes(Map<String, String> attrMap, FeatureCollection bean) throws XMLStreamException
    {
        this.readAbstractFeatureTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of AbstractFeatureCollectionType complex type
     */
    public void readFeatureCollectionTypeElements(XMLStreamReader reader, FeatureCollection bean) throws XMLStreamException
    {
        this.readAbstractFeatureTypeElements(reader, bean);
        
        boolean found;
        
        // featureMember
        do
        {
            found = checkElementName(reader, "featureMember");
            if (found)
            {
                OgcProperty<AbstractFeature> featureMemberProp = new OgcPropertyImpl<AbstractFeature>();
                readPropertyAttributes(reader, featureMemberProp);
                
                if (featureMemberProp.getHref() == null)
                {
                    reader.nextTag();
                    featureMemberProp.setValue(this.readAbstractFeature(reader));
                }
                bean.getFeatureMemberList().add(featureMemberProp);
                
                reader.nextTag(); // end property tag
                reader.nextTag();
            }
        }
        while (found);
    }
    
    
    /**
     * Writes attributes of AbstractFeatureCollectionType complex type
     */
    public void writeFeatureCollectionTypeAttributes(XMLStreamWriter writer, FeatureCollection bean) throws XMLStreamException
    {
        this.writeAbstractFeatureTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of AbstractFeatureCollectionType complex type
     */
    public void writeFeatureCollectionTypeElements(XMLStreamWriter writer, FeatureCollection bean) throws XMLStreamException
    {
        this.writeAbstractFeatureTypeElements(writer, bean);
        int numItems;
        
        // featureMember
        numItems = bean.getFeatureMemberList().size();
        for (int i = 0; i < numItems; i++)
        {
            OgcProperty<AbstractFeature> item = bean.getFeatureMemberList().getProperty(i);
            writer.writeStartElement(NS_URI, "featureMember");
            writePropertyAttributes(writer, item);
            this.writeAbstractFeature(writer, item.getValue());
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for FeatureCollectionType complex type
     */
    public FeatureCollection readFeatureCollectionType(XMLStreamReader reader) throws XMLStreamException
    {
        FeatureCollection bean = factory.newFeatureCollection();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readFeatureCollectionTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readFeatureCollectionTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Write method for FeatureCollectionType complex type
     */
    public void writeFeatureCollectionType(XMLStreamWriter writer, FeatureCollection bean) throws XMLStreamException
    {
        this.writeFeatureCollectionTypeAttributes(writer, bean);
        this.writeFeatureCollectionTypeElements(writer, bean);
    }
    
    
    /**
     * Reads attributes of AbstractTimePrimitiveType complex type
     */
    public void readAbstractTimePrimitiveTypeAttributes(Map<String, String> attrMap, AbstractTimePrimitive bean) throws XMLStreamException
    {
        this.readAbstractGMLTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of AbstractTimePrimitiveType complex type
     */
    public void readAbstractTimePrimitiveTypeElements(XMLStreamReader reader, AbstractTimePrimitive bean) throws XMLStreamException
    {
        this.readAbstractGMLTypeElements(reader, bean);
        
        boolean found;
        
        // relatedTime
        do
        {
            found = checkElementName(reader, "relatedTime");
            if (found)
            {
                OgcProperty<AbstractTimePrimitive> relatedTimeProp = new OgcPropertyImpl<AbstractTimePrimitive>();
                readPropertyAttributes(reader, relatedTimeProp);
                
                if (relatedTimeProp.getHref() == null)
                {
                    reader.nextTag();
                    relatedTimeProp.setValue(this.readAbstractTimeGeometricPrimitive(reader));
                }
                bean.getRelatedTimeList().add(relatedTimeProp);
                
                reader.nextTag(); // end property tag
                reader.nextTag();
            }
        }
        while (found);
    }
    
    
    /**
     * Writes attributes of AbstractTimePrimitiveType complex type
     */
    public void writeAbstractTimePrimitiveTypeAttributes(XMLStreamWriter writer, AbstractTimePrimitive bean) throws XMLStreamException
    {
        this.writeAbstractGMLTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of AbstractTimePrimitiveType complex type
     */
    public void writeAbstractTimePrimitiveTypeElements(XMLStreamWriter writer, AbstractTimePrimitive bean) throws XMLStreamException
    {
        this.writeAbstractGMLTypeElements(writer, bean);
        int numItems;
        
        // relatedTime
        numItems = bean.getRelatedTimeList().size();
        for (int i = 0; i < numItems; i++)
        {
            OgcProperty<AbstractTimePrimitive> item = bean.getRelatedTimeList().getProperty(i);
            writer.writeStartElement(NS_URI, "relatedTime");
            writePropertyAttributes(writer, item);
            this.writeAbstractTimeGeometricPrimitive(writer, (AbstractTimeGeometricPrimitive)item.getValue());
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Reads attributes of AbstractTimeGeometricPrimitiveType complex type
     */
    public void readAbstractTimeGeometricPrimitiveTypeAttributes(Map<String, String> attrMap, AbstractTimeGeometricPrimitive bean) throws XMLStreamException
    {
        this.readAbstractTimePrimitiveTypeAttributes(attrMap, bean);
        
        String val;
        
        // frame
        val = attrMap.get("frame");
        if (val != null)
            bean.setFrame(val);
    }
    
    
    /**
     * Reads elements of AbstractTimeGeometricPrimitiveType complex type
     */
    public void readAbstractTimeGeometricPrimitiveTypeElements(XMLStreamReader reader, AbstractTimeGeometricPrimitive bean) throws XMLStreamException
    {
        this.readAbstractTimePrimitiveTypeElements(reader, bean);
        
    }
    
    
    /**
     * Writes attributes of AbstractTimeGeometricPrimitiveType complex type
     */
    public void writeAbstractTimeGeometricPrimitiveTypeAttributes(XMLStreamWriter writer, AbstractTimeGeometricPrimitive bean) throws XMLStreamException
    {
        this.writeAbstractTimePrimitiveTypeAttributes(writer, bean);
        
        // frame
        if (bean.isSetFrame())
            writer.writeAttribute("frame", getStringValue(bean.getFrame()));
    }
    
    
    /**
     * Writes elements of AbstractTimeGeometricPrimitiveType complex type
     */
    public void writeAbstractTimeGeometricPrimitiveTypeElements(XMLStreamWriter writer, AbstractTimeGeometricPrimitive bean) throws XMLStreamException
    {
        this.writeAbstractTimePrimitiveTypeElements(writer, bean);
    }
    
    
    /**
     * Read method for TimeInstantType complex type
     */
    public TimeInstant readTimeInstantType(XMLStreamReader reader) throws XMLStreamException
    {
        TimeInstant bean = factory.newTimeInstant();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readTimeInstantTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readTimeInstantTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of TimeInstantType complex type
     */
    public void readTimeInstantTypeAttributes(Map<String, String> attrMap, TimeInstant bean) throws XMLStreamException
    {
        this.readAbstractTimeGeometricPrimitiveTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of TimeInstantType complex type
     */
    public void readTimeInstantTypeElements(XMLStreamReader reader, TimeInstant bean) throws XMLStreamException
    {
        this.readAbstractTimeGeometricPrimitiveTypeElements(reader, bean);
        
        boolean found;
        
        // timePosition
        found = checkElementName(reader, "timePosition");
        if (found)
        {
            TimePosition timePosition = this.readTimePositionType(reader);
            if (timePosition != null)
                bean.setTimePosition(timePosition);
            
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for TimeInstantType complex type
     */
    public void writeTimeInstantType(XMLStreamWriter writer, TimeInstant bean) throws XMLStreamException
    {
        this.writeTimeInstantTypeAttributes(writer, bean);
        this.writeTimeInstantTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of TimeInstantType complex type
     */
    public void writeTimeInstantTypeAttributes(XMLStreamWriter writer, TimeInstant bean) throws XMLStreamException
    {
        this.writeAbstractTimeGeometricPrimitiveTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of TimeInstantType complex type
     */
    public void writeTimeInstantTypeElements(XMLStreamWriter writer, TimeInstant bean) throws XMLStreamException
    {
        this.writeAbstractTimeGeometricPrimitiveTypeElements(writer, bean);
        
        // timePosition
        writer.writeStartElement(NS_URI, "timePosition");
        this.writeTimePositionType(writer, bean.getTimePosition());
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for TimePeriodType complex type
     */
    public TimePeriod readTimePeriodType(XMLStreamReader reader) throws XMLStreamException
    {
        TimePeriod bean = factory.newTimePeriod();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readTimePeriodTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readTimePeriodTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of TimePeriodType complex type
     */
    public void readTimePeriodTypeAttributes(Map<String, String> attrMap, TimePeriod bean) throws XMLStreamException
    {
        this.readAbstractTimeGeometricPrimitiveTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of TimePeriodType complex type
     */
    public void readTimePeriodTypeElements(XMLStreamReader reader, TimePeriod bean) throws XMLStreamException
    {
        this.readAbstractTimeGeometricPrimitiveTypeElements(reader, bean);
        
        boolean found;
        String val;
        
        // beginPosition
        found = checkElementName(reader, "beginPosition");
        if (found)
        {
            TimePosition beginPosition = this.readTimePositionType(reader);
            if (beginPosition != null)
                bean.setBeginPosition(beginPosition);
            
            reader.nextTag();
        }
        
        // begin
        found = checkElementName(reader, "begin");
        if (found)
        {
            OgcProperty<TimeInstant> beginProp = bean.getBeginProperty();
            readPropertyAttributes(reader, beginProp);
            
            if (beginProp.getHref() == null)
            {
                reader.nextTag();
                beginProp.setValue(this.readTimeInstant(reader));
            }
            
            reader.nextTag(); // end property tag
            reader.nextTag();
        }
        
        // endPosition
        found = checkElementName(reader, "endPosition");
        if (found)
        {
            TimePosition endPosition = this.readTimePositionType(reader);
            if (endPosition != null)
                bean.setEndPosition(endPosition);
            
            reader.nextTag();
        }
        
        // end
        found = checkElementName(reader, "end");
        if (found)
        {
            OgcProperty<TimeInstant> endProp = bean.getEndProperty();
            readPropertyAttributes(reader, endProp);
            
            if (endProp.getHref() == null)
            {
                reader.nextTag();
                endProp.setValue(this.readTimeInstant(reader));
            }
            
            reader.nextTag(); // end property tag
            reader.nextTag();
        }
        
        // duration
        found = checkElementName(reader, "duration");
        if (found)
        {
            val = reader.getElementText();
            if (val != null)
                bean.setDuration(getDurationFromString(val));
            reader.nextTag();
        }
        
        // timeInterval
        found = checkElementName(reader, "timeInterval");
        if (found)
        {
            TimeIntervalLength timeInterval = this.readTimeIntervalLengthType(reader);
            if (timeInterval != null)
                bean.setTimeInterval(timeInterval);
            
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for TimePeriodType complex type
     */
    public void writeTimePeriodType(XMLStreamWriter writer, TimePeriod bean) throws XMLStreamException
    {
        this.writeTimePeriodTypeAttributes(writer, bean);
        this.writeTimePeriodTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of TimePeriodType complex type
     */
    public void writeTimePeriodTypeAttributes(XMLStreamWriter writer, TimePeriod bean) throws XMLStreamException
    {
        this.writeAbstractTimeGeometricPrimitiveTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of TimePeriodType complex type
     */
    public void writeTimePeriodTypeElements(XMLStreamWriter writer, TimePeriod bean) throws XMLStreamException
    {
        this.writeAbstractTimeGeometricPrimitiveTypeElements(writer, bean);
        
        // beginPosition
        if (bean.isSetBeginPosition())
        {
            writer.writeStartElement(NS_URI, "beginPosition");
            this.writeTimePositionType(writer, bean.getBeginPosition());
            writer.writeEndElement();
        }
        
        // begin
        if (bean.isSetBegin())
        {
            writer.writeStartElement(NS_URI, "begin");
            writePropertyAttributes(writer, bean.getBeginProperty());
            this.writeTimeInstant(writer, bean.getBegin());
            writer.writeEndElement();
        }
        
        // endPosition
        if (bean.isSetEndPosition())
        {
            writer.writeStartElement(NS_URI, "endPosition");
            this.writeTimePositionType(writer, bean.getEndPosition());
            writer.writeEndElement();
        }
        
        // end
        if (bean.isSetEnd())
        {
            writer.writeStartElement(NS_URI, "end");
            writePropertyAttributes(writer, bean.getEndProperty());
            this.writeTimeInstant(writer, bean.getEnd());
            writer.writeEndElement();
        }
        
        // duration
        if (bean.isSetDuration())
        {
            writer.writeStartElement(NS_URI, "duration");
            writer.writeCharacters(getIsoDurationString(bean.getDuration()));
            writer.writeEndElement();
        }
        
        // timeInterval
        if (bean.isSetTimeInterval())
        {
            writer.writeStartElement(NS_URI, "timeInterval");
            this.writeTimeIntervalLengthType(writer, bean.getTimeInterval());
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for TimePositionType complex type
     */
    public TimePosition readTimePositionType(XMLStreamReader reader) throws XMLStreamException
    {
        TimePosition bean = factory.newTimePosition();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readTimePositionTypeAttributes(attrMap, bean);
        
        String val = reader.getElementText();
        if (val != null && val.trim().length() > 0)
            bean.setDateTimeValue(getDateTimeFromString(val));
        
        return bean;
    }
    
    
    /**
     * Reads attributes of TimePositionType complex type
     */
    public void readTimePositionTypeAttributes(Map<String, String> attrMap, TimePosition bean) throws XMLStreamException
    {
        String val;
        
        // frame
        val = attrMap.get("frame");
        if (val != null)
            bean.setFrame(val);
        
        // calendareraname
        val = attrMap.get("calendarEraName");
        if (val != null)
            bean.setCalendarEraName(val);
        
        // indeterminateposition
        val = attrMap.get("indeterminatePosition");
        if (val != null)
            bean.setIndeterminatePosition(TimeIndeterminateValue.fromString(val));
    }
    
    
    /**
     * Write method for TimePositionType complex type
     */
    public void writeTimePositionType(XMLStreamWriter writer, TimePosition bean) throws XMLStreamException
    {
        this.writeTimePositionTypeAttributes(writer, bean);
        if (bean.getDateTimeValue() != null)
            writer.writeCharacters(getStringValue(bean.getDateTimeValue()));
    }
    
    
    /**
     * Writes attributes of TimePositionType complex type
     */
    public void writeTimePositionTypeAttributes(XMLStreamWriter writer, TimePosition bean) throws XMLStreamException
    {
        
        // frame
        if (bean.isSetFrame())
            writer.writeAttribute("frame", getStringValue(bean.getFrame()));
        
        // calendarEraName
        if (bean.isSetCalendarEraName())
            writer.writeAttribute("calendarEraName", getStringValue(bean.getCalendarEraName()));
        
        // indeterminatePosition
        if (bean.isSetIndeterminatePosition())
            writer.writeAttribute("indeterminatePosition", getStringValue(bean.getIndeterminatePosition()));
    }
    
    
    /**
     * Read method for TimeIntervalLengthType complex type
     */
    public TimeIntervalLength readTimeIntervalLengthType(XMLStreamReader reader) throws XMLStreamException
    {
        TimeIntervalLength bean = factory.newTimeIntervalLength();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readTimeIntervalLengthTypeAttributes(attrMap, bean);
        
        String val = reader.getElementText();
        if (val != null)
            bean.setValue(getDoubleFromString(val));
        
        return bean;
    }
    
    
    /**
     * Reads attributes of TimeIntervalLengthType complex type
     */
    public void readTimeIntervalLengthTypeAttributes(Map<String, String> attrMap, TimeIntervalLength bean) throws XMLStreamException
    {
        String val;
        
        // unit
        val = attrMap.get("unit");
        if (val != null)
            bean.setUnit(TimeUnit.fromString(val));
        
        // radix
        val = attrMap.get("radix");
        if (val != null)
            bean.setRadix(getIntFromString(val));
        
        // factor
        val = attrMap.get("factor");
        if (val != null)
            bean.setFactor(getIntFromString(val));
    }
    
    
    /**
     * Write method for TimeIntervalLengthType complex type
     */
    public void writeTimeIntervalLengthType(XMLStreamWriter writer, TimeIntervalLength bean) throws XMLStreamException
    {
        this.writeTimeIntervalLengthTypeAttributes(writer, bean);
        
        writer.writeCharacters(getStringValue(bean.getValue()));
    }
    
    
    /**
     * Writes attributes of TimeIntervalLengthType complex type
     */
    public void writeTimeIntervalLengthTypeAttributes(XMLStreamWriter writer, TimeIntervalLength bean) throws XMLStreamException
    {
        
        // unit
        writer.writeAttribute("unit", getStringValue(bean.getUnit()));
        
        // radix
        if (bean.isSetRadix())
            writer.writeAttribute("radix", getStringValue(bean.getRadix()));
        
        // factor
        if (bean.isSetFactor())
            writer.writeAttribute("factor", getStringValue(bean.getFactor()));
    }
    
    
    /**
     * Reads attributes of AbstractGeometryType complex type
     */
    public void readAbstractGeometryTypeAttributes(Map<String, String> attrMap, AbstractGeometry bean) throws XMLStreamException
    {
        this.readAbstractGMLTypeAttributes(attrMap, bean);
        
        String val;
        
        // srsname
        val = attrMap.get("srsName");
        if (val != null)
            bean.setSrsName(val);
        
        // srsdimension
        val = attrMap.get("srsDimension");
        if (val != null)
            bean.setSrsDimension(getIntFromString(val));
        
        // axislabels
        val = attrMap.get("axisLabels");
        if (val != null)
            bean.setAxisLabels(getStringArrayFromString(val));
        
        // uomlabels
        val = attrMap.get("uomLabels");
        if (val != null)
            bean.setUomLabels(getStringArrayFromString(val));
    }
    
    
    /**
     * Reads elements of AbstractGeometryType complex type
     */
    public void readAbstractGeometryTypeElements(XMLStreamReader reader, AbstractGeometry bean) throws XMLStreamException
    {
        this.readAbstractGMLTypeElements(reader, bean);
        
    }
    
    
    /**
     * Writes attributes of AbstractGeometryType complex type
     */
    public void writeAbstractGeometryTypeAttributes(XMLStreamWriter writer, AbstractGeometry bean) throws XMLStreamException
    {
        this.writeAbstractGMLTypeAttributes(writer, bean);
        
        // srsName
        if (bean.isSetSrsName())
            writer.writeAttribute("srsName", getStringValue(bean.getSrsName()));
        
        // srsDimension
        if (bean.isSetSrsDimension())
            writer.writeAttribute("srsDimension", getStringValue(bean.getSrsDimension()));
        
        // axisLabels
        if (bean.isSetAxisLabels())
            writer.writeAttribute("axisLabels", getStringValue(bean.getAxisLabels()));
        
        // uomLabels
        if (bean.isSetUomLabels())
            writer.writeAttribute("uomLabels", getStringValue(bean.getUomLabels()));
    }
    
    
    /**
     * Writes elements of AbstractGeometryType complex type
     */
    public void writeAbstractGeometryTypeElements(XMLStreamWriter writer, AbstractGeometry bean) throws XMLStreamException
    {
        this.writeAbstractGMLTypeElements(writer, bean);
    }
    
    
    /**
     * Read method for EnvelopeType complex type
     */
    public Envelope readEnvelopeType(XMLStreamReader reader) throws XMLStreamException
    {
        Envelope bean = factory.newEnvelope();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readEnvelopeTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readEnvelopeTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of EnvelopeType complex type
     */
    public void readEnvelopeTypeAttributes(Map<String, String> attrMap, Envelope bean) throws XMLStreamException
    {
        String val;
        
        // srsname
        val = attrMap.get("srsName");
        if (val != null)
            bean.setSrsName(val);
        
        // srsdimension
        val = attrMap.get("srsDimension");
        if (val != null)
            bean.setSrsDimension(getIntFromString(val));
        
        // axislabels
        val = attrMap.get("axisLabels");
        if (val != null)
            bean.setAxisLabels(getStringArrayFromString(val));
        
        // uomlabels
        val = attrMap.get("uomLabels");
        if (val != null)
            bean.setUomLabels(getStringArrayFromString(val));
    }
    
    
    /**
     * Reads elements of EnvelopeType complex type
     */
    public void readEnvelopeTypeElements(XMLStreamReader reader, Envelope bean) throws XMLStreamException
    {
        boolean found;
        
        // lowerCorner
        found = checkElementName(reader, "lowerCorner");
        if (found)
        {
            String lowerCorner = reader.getElementText();
            if (lowerCorner != null)
                bean.setLowerCorner(getDoubleArrayFromString(lowerCorner));
            
            reader.nextTag();
        }
        
        // upperCorner
        found = checkElementName(reader, "upperCorner");
        if (found)
        {
            String upperCorner = reader.getElementText();
            if (upperCorner != null)
                bean.setUpperCorner(getDoubleArrayFromString(upperCorner));
            
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for EnvelopeType complex type
     */
    public void writeEnvelopeType(XMLStreamWriter writer, Envelope bean) throws XMLStreamException
    {
        this.writeEnvelopeTypeAttributes(writer, bean);
        this.writeEnvelopeTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of EnvelopeType complex type
     */
    public void writeEnvelopeTypeAttributes(XMLStreamWriter writer, Envelope bean) throws XMLStreamException
    {
        
        // srsName
        if (bean.isSetSrsName())
            writer.writeAttribute("srsName", getStringValue(bean.getSrsName()));
        
        // srsDimension
        if (bean.isSetSrsDimension())
            writer.writeAttribute("srsDimension", getStringValue(bean.getSrsDimension()));
        
        // axisLabels
        if (bean.isSetAxisLabels())
            writer.writeAttribute("axisLabels", getStringValue(bean.getAxisLabels()));
        
        // uomLabels
        if (bean.isSetUomLabels())
            writer.writeAttribute("uomLabels", getStringValue(bean.getUomLabels()));
    }
    
    
    /**
     * Writes elements of EnvelopeType complex type
     */
    public void writeEnvelopeTypeElements(XMLStreamWriter writer, Envelope bean) throws XMLStreamException
    {
        
        // lowerCorner
        if (bean.isSetLowerCorner())
        {
            writer.writeStartElement(NS_URI, "lowerCorner");
            writer.writeCharacters(getStringValue(bean.getLowerCorner()));
            writer.writeEndElement();
        }
        
        // upperCorner
        if (bean.isSetUpperCorner())
        {
            writer.writeStartElement(NS_URI, "upperCorner");
            writer.writeCharacters(getStringValue(bean.getUpperCorner()));
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for PointType complex type
     */
    public Point readPointType(XMLStreamReader reader) throws XMLStreamException
    {
        Point bean = factory.newPoint();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readPointTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readPointTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of PointType complex type
     */
    public void readPointTypeAttributes(Map<String, String> attrMap, Point bean) throws XMLStreamException
    {
        this.readAbstractGeometryTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of PointType complex type
     */
    public void readPointTypeElements(XMLStreamReader reader, Point bean) throws XMLStreamException
    {
        this.readAbstractGeometryTypeElements(reader, bean);
        
        boolean found;
        
        // pos
        found = checkElementName(reader, "pos");
        if (found)
        {
            String pos = reader.getElementText();
            if (pos != null)
                bean.setPos(getDoubleArrayFromString(pos));
            
            reader.nextTag();
        }
        
        // coordinates
        found = checkElementName(reader, "coordinates");
        if (found)
        {
            String pos = reader.getElementText();
            if (pos != null)
                bean.setPos(getDoubleArrayFromString(pos));
            
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for PointType complex type
     */
    public void writePointType(XMLStreamWriter writer, Point bean) throws XMLStreamException
    {
        this.writePointTypeAttributes(writer, bean);
        this.writePointTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of PointType complex type
     */
    public void writePointTypeAttributes(XMLStreamWriter writer, Point bean) throws XMLStreamException
    {
        this.writeAbstractGeometryTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of PointType complex type
     */
    public void writePointTypeElements(XMLStreamWriter writer, Point bean) throws XMLStreamException
    {
        this.writeAbstractGeometryTypeElements(writer, bean);
        
        // pos
        if (bean.isSetPos())
        {
            writer.writeStartElement(NS_URI, "pos");
            writer.writeCharacters(getStringValue(bean.getPos()));
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for LineStringType complex type
     */
    public LineString readLineStringType(XMLStreamReader reader) throws XMLStreamException
    {
        LineString bean = factory.newLineString();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readLineStringTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readLineStringTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of LineStringType complex type
     */
    public void readLineStringTypeAttributes(Map<String, String> attrMap, LineString bean) throws XMLStreamException
    {
        this.readAbstractGeometryTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of LineStringType complex type
     */
    public void readLineStringTypeElements(XMLStreamReader reader, LineString bean) throws XMLStreamException
    {
        this.readAbstractGeometryTypeElements(reader, bean);
        bean.setPosList(readPositionElements(reader));
    }
    
    
    /**
     * Reads coordinate array from a choice of pos/posList/coordinates properties
     * @param reader
     * @return
     * @throws XMLStreamException
     */
    public double[] readPositionElements(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found;
        double[] posList = null;
        
        // pos 0..*
        found = checkElementName(reader, "pos");
        if (found)
        {
            ArrayList<double[]> tmpList = new ArrayList<double[]>();
            int arraySize = 0;
            
            do
            {
                found = checkElementName(reader, "pos");
                if (found)
                {
                    String pos = reader.getElementText();
                    if (pos != null)
                    {
                        double[] values = getDoubleArrayFromString(pos);
                        arraySize += values.length;
                        tmpList.add(values);
                    }
                    
                    reader.nextTag();
                }
            }
            while (found);
        
            // aggregate to a single array
            posList = new double[arraySize];
            int i = 0;
            for (double[] pos: tmpList)
            {
                System.arraycopy(pos, 0, posList, i, pos.length);
                i += pos.length;
            }
        }
        
        // pointProperty (skip for now)
        while (checkElementName(reader, "pointProperty"))
            skipElementAndAllChildren(reader);
        
        // pointRep (skip for now)
        while (checkElementName(reader, "pointRep"))
            skipElementAndAllChildren(reader);
        
        // posList
        found = checkElementName(reader, "posList");
        if (found)
        {
            String posString = reader.getElementText();
            if (posString != null)
                posList = getDoubleArrayFromString(posString);
            
            reader.nextTag();
        }
        
        // coordinates
        found = checkElementName(reader, "coordinates");
        if (found)
        {
            String posString = reader.getElementText();
            if (posString != null)
                posList = getDoubleArrayFromString(posString);
            
            reader.nextTag();
        }
        
        return posList;
    }
    
    
    /**
     * Write method for LineStringType complex type
     */
    public void writeLineStringType(XMLStreamWriter writer, LineString bean) throws XMLStreamException
    {
        this.writeLineStringTypeAttributes(writer, bean);
        this.writeLineStringTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of LineStringType complex type
     */
    public void writeLineStringTypeAttributes(XMLStreamWriter writer, LineString bean) throws XMLStreamException
    {
        this.writeAbstractGeometryTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of LineStringType complex type
     */
    public void writeLineStringTypeElements(XMLStreamWriter writer, LineString bean) throws XMLStreamException
    {
        this.writeAbstractGeometryTypeElements(writer, bean);
        
        // posList
        if (bean.isSetPosList())
        {
            writer.writeStartElement(NS_URI, "posList");
            writer.writeCharacters(getStringValue(bean.getPosList()));
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for PolygonType complex type
     */
    public Polygon readPolygonType(XMLStreamReader reader) throws XMLStreamException
    {
        Polygon bean = factory.newPolygon();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readPolygonTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readPolygonTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of PolygonType complex type
     */
    public void readPolygonTypeAttributes(Map<String, String> attrMap, Polygon bean) throws XMLStreamException
    {
        this.readAbstractGeometryTypeAttributes(attrMap, bean);
        
    }
    
    
    /**
     * Reads elements of PolygonType complex type
     */
    public void readPolygonTypeElements(XMLStreamReader reader, Polygon bean) throws XMLStreamException
    {
        this.readAbstractGeometryTypeElements(reader, bean);
        
        boolean found;
        
        // exterior
        found = checkElementName(reader, "exterior");
        if (found)
        {
            reader.nextTag();
            LinearRing exterior = this.readLinearRing(reader);
            if (exterior != null)
                bean.setExterior(exterior);
            
            reader.nextTag(); // end property tag
            reader.nextTag();
        }
        
        // interior
        do
        {
            found = checkElementName(reader, "interior");
            if (found)
            {
                reader.nextTag();
                LinearRing interior = this.readLinearRing(reader);
                if (interior != null)
                    bean.addInterior(interior);
                
                reader.nextTag(); // end property tag
                reader.nextTag();
            }
        }
        while (found);
    }
    
    
    /**
     * Write method for PolygonType complex type
     */
    public void writePolygonType(XMLStreamWriter writer, Polygon bean) throws XMLStreamException
    {
        this.writePolygonTypeAttributes(writer, bean);
        this.writePolygonTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of PolygonType complex type
     */
    public void writePolygonTypeAttributes(XMLStreamWriter writer, Polygon bean) throws XMLStreamException
    {
        this.writeAbstractGeometryTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes elements of PolygonType complex type
     */
    public void writePolygonTypeElements(XMLStreamWriter writer, Polygon bean) throws XMLStreamException
    {
        this.writeAbstractGeometryTypeElements(writer, bean);
        int numItems;
        
        // exterior
        if (bean.isSetExterior())
        {
            writer.writeStartElement(NS_URI, "exterior");
            this.writeLinearRing(writer, bean.getExterior());
            writer.writeEndElement();
        }
        
        // interior
        numItems = bean.getInteriorList().size();
        for (int i = 0; i < numItems; i++)
        {
            LinearRing item = bean.getInteriorList().get(i);
            writer.writeStartElement(NS_URI, "interior");
            this.writeLinearRing(writer, item);
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for LinearRingType complex type
     */
    public LinearRing readLinearRingType(XMLStreamReader reader) throws XMLStreamException
    {
        LinearRing bean = factory.newLinearRing();
        
        reader.nextTag();
        this.readLinearRingTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads elements of LinearRingType complex type
     */
    public void readLinearRingTypeElements(XMLStreamReader reader, LinearRing bean) throws XMLStreamException
    {
        bean.setPosList(readPositionElements(reader));
    }
    
    
    /**
     * Write method for LinearRingType complex type
     */
    public void writeLinearRingType(XMLStreamWriter writer, LinearRing bean) throws XMLStreamException
    {
        this.writeLinearRingTypeElements(writer, bean);
    }
    
    
    /**
     * Writes elements of LinearRingType complex type
     */
    public void writeLinearRingTypeElements(XMLStreamWriter writer, LinearRing bean) throws XMLStreamException
    {
        // posList
        if (bean.isSetPosList())
        {
            writer.writeStartElement(NS_URI, "posList");
            writer.writeCharacters(getStringValue(bean.getPosList()));
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Reads attributes of AbstractGMLType complex type
     */
    public void readAbstractGMLTypeAttributes(Map<String, String> attrMap, AbstractGML bean) throws XMLStreamException
    {
        String val;
        
        // id
        val = attrMap.get("id");
        if (val != null)
            bean.setId(val);
    }
    
    
    /**
     * Reads elements of AbstractGMLType complex type
     */
    public void readAbstractGMLTypeElements(XMLStreamReader reader, AbstractGML bean) throws XMLStreamException
    {
        boolean found;
        
        // metaDataProperty
        do
        {
            found = checkElementName(reader, "metaDataProperty");
            if (found)
            {
                OgcProperty<Serializable> metaDataPropertyProp = new OgcPropertyImpl<Serializable>();
                readPropertyAttributes(reader, metaDataPropertyProp);
                
                if (metaDataPropertyProp.getHref() == null)
                {
                    reader.nextTag();
                    metaDataPropertyProp.setValue(this.readExtension(reader));
                }
                
                if (metaDataPropertyProp.hasValue() || metaDataPropertyProp.hasHref())
                    bean.getMetaDataPropertyList().add(metaDataPropertyProp);
                
                reader.nextTag(); // end property tag
                reader.nextTag();
            }
        }
        while (found);
        
        // description
        found = checkElementName(reader, "description");
        if (found)
        {
            String val = reader.getElementText();
            if (val != null)
                bean.setDescription(trimStringValue(val));            
            reader.nextTag();
        }
        
        // descriptionReference
        found = checkElementName(reader, "descriptionReference");
        if (found)
        {
            Reference descriptionReference = this.readReferenceType(reader);
            if (descriptionReference != null)
                bean.setDescriptionReference(descriptionReference);
            
            reader.nextTag(); // end property tag
            reader.nextTag();
        }
        
        // identifier
        found = checkElementName(reader, "identifier");
        if (found)
        {
            CodeWithAuthority identifier = this.readCodeType(reader);
            if (identifier != null)
                bean.setIdentifier(identifier);
            
            reader.nextTag();
        }
        
        // name
        do
        {
            found = checkElementName(reader, "name");
            if (found)
            {
                CodeWithAuthority name = this.readCodeType(reader);
                if (name != null)
                    bean.addName(name);
                
                reader.nextTag();
            }
        }
        while (found);
    }
    
    
    /**
     * Writes attributes of AbstractGMLType complex type
     */
    public void writeAbstractGMLTypeAttributes(XMLStreamWriter writer, AbstractGML bean) throws XMLStreamException
    {
        // id
        writer.writeAttribute(NS_URI, "id", getStringValue(bean.getId()));
    }
    
    
    /**
     * Writes elements of AbstractGMLType complex type
     */
    public void writeAbstractGMLTypeElements(XMLStreamWriter writer, AbstractGML bean) throws XMLStreamException
    {
        int numItems;
        
        // metaDataProperty
        numItems = bean.getMetaDataPropertyList().size();
        for (int i = 0; i < numItems; i++)
        {
            OgcProperty<Serializable> item = bean.getMetaDataPropertyList().getProperty(i);
            if (!item.hasValue() || canWriteExtension(item.getValue()))
            {
                writer.writeStartElement(NS_URI, "metaDataProperty");
                writePropertyAttributes(writer, item);
                if (item.hasValue())
                    this.writeExtension(writer, item.getValue());
                writer.writeEndElement();
            }
        }
        
        // description
        if (bean.isSetDescription())
        {
            writer.writeStartElement(NS_URI, "description");
            writer.writeCharacters(bean.getDescription());
            writer.writeEndElement();
        }
        
        // descriptionReference
        if (bean.isSetDescriptionReference())
        {
            writer.writeStartElement(NS_URI, "descriptionReference");
            this.writeReferenceType(writer, bean.getDescriptionReference());
            writer.writeEndElement();
        }
        
        // identifier
        if (bean.isSetIdentifier())
        {
            writer.writeStartElement(NS_URI, "identifier");
            this.writeCodeType(writer, bean.getIdentifier());
            writer.writeEndElement();
        }
        
        // name
        numItems = bean.getNameList().size();
        for (int i = 0; i < numItems; i++)
        {
            CodeWithAuthority item = bean.getNameList().get(i);
            writer.writeStartElement(NS_URI, "name");
            this.writeCodeType(writer, item);
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for ReferenceType complex type
     */
    public Reference readReferenceType(XMLStreamReader reader) throws XMLStreamException
    {
        Reference bean = factory.newReference();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readReferenceTypeAttributes(attrMap, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of ReferenceType complex type
     */
    public void readReferenceTypeAttributes(Map<String, String> attrMap, Reference bean) throws XMLStreamException
    {
        readPropertyAttributes(attrMap, bean);
        
        String val;
        
        // owns
        val = attrMap.get("owns");
        if (val != null)
            bean.setOwns(getBooleanFromString(val));
        
        // remoteschema
        val = attrMap.get("remoteSchema");
        if (val != null)
            bean.setRemoteSchema(val);
    }
    
    
    /**
     * Write method for ReferenceType complex type
     */
    public void writeReferenceType(XMLStreamWriter writer, Reference bean) throws XMLStreamException
    {
        this.writeReferenceTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes attributes of ReferenceType complex type
     */
    public void writeReferenceTypeAttributes(XMLStreamWriter writer, Reference bean) throws XMLStreamException
    {
        writePropertyAttributes(writer, bean);
        
        // owns
        if (bean.isSetOwns())
            writer.writeAttribute("owns", getStringValue(bean.getOwns()));
        
        // remoteSchema
        if (bean.isSetRemoteSchema())
            writer.writeAttribute("remoteSchema", getStringValue(bean.getRemoteSchema()));
    }
    
    
    /**
     * Read method for CodeType complex type
     */
    public CodeWithAuthority readCodeType(XMLStreamReader reader) throws XMLStreamException
    {
        CodeWithAuthority bean = factory.newCode();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readCodeTypeAttributes(attrMap, bean);
        
        String val = reader.getElementText();
        if (val != null)
            bean.setValue(trimStringValue(val));
        
        return bean;
    }
    
    
    /**
     * Reads attributes of CodeType complex type
     */
    public void readCodeTypeAttributes(Map<String, String> attrMap, CodeWithAuthority bean) throws XMLStreamException
    {
        String val;
        
        // codespace
        val = attrMap.get("codeSpace");
        if (val != null)
            bean.setCodeSpace(val);
    }
    
    
    /**
     * Write method for CodeType complex type
     */
    public void writeCodeType(XMLStreamWriter writer, CodeWithAuthority bean) throws XMLStreamException
    {
        this.writeCodeTypeAttributes(writer, bean);
        
        writer.writeCharacters(getStringValue(bean.getValue()));
    }
    
    
    /**
     * Writes attributes of CodeType complex type
     */
    public void writeCodeTypeAttributes(XMLStreamWriter writer, CodeWithAuthority bean) throws XMLStreamException
    {
        
        // codeSpace
        if (bean.isSetCodeSpace())
            writer.writeAttribute("codeSpace", getStringValue(bean.getCodeSpace()));
    }
    
    
    /**
     * Read method for CodeListType complex type
     */
    public CodeList readCodeListType(XMLStreamReader reader) throws XMLStreamException
    {
        CodeList bean = factory.newCodeList();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readCodeListTypeAttributes(attrMap, bean);
        
        String val = reader.getElementText();
        if (val != null)
            bean.setValue(getStringArrayFromString(val));
        
        return bean;
    }
    
    
    /**
     * Reads attributes of CodeListType complex type
     */
    public void readCodeListTypeAttributes(Map<String, String> attrMap, CodeList bean) throws XMLStreamException
    {
        String val;
        
        // codespace
        val = attrMap.get("codeSpace");
        if (val != null)
            bean.setCodeSpace(val);
    }
    
    
    /**
     * Write method for CodeListType complex type
     */
    public void writeCodeListType(XMLStreamWriter writer, CodeList bean) throws XMLStreamException
    {
        this.writeCodeListTypeAttributes(writer, bean);
        
        writer.writeCharacters(getStringValue(bean.getValue()));
    }
    
    
    /**
     * Writes attributes of CodeListType complex type
     */
    public void writeCodeListTypeAttributes(XMLStreamWriter writer, CodeList bean) throws XMLStreamException
    {
        
        // codeSpace
        if (bean.isSetCodeSpace())
            writer.writeAttribute("codeSpace", getStringValue(bean.getCodeSpace()));
    }
    
    
    /**
     * Read method for CodeOrNilReasonListType complex type
     */
    public CodeOrNilReasonList readCodeOrNilReasonListType(XMLStreamReader reader) throws XMLStreamException
    {
        // TODO readCodeOrNilReasonListType
        /*CodeOrNilReasonList bean = factory.newCodeOrNilReasonList();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readCodeOrNilReasonListTypeAttributes(attrMap, bean);
        
        String val = reader.getElementText();
        if (val != null)
            bean.setValue(getObjectArrayFromString(val));
        
        return bean;*/
        return null;
    }
    
    
    /**
     * Reads attributes of CodeOrNilReasonListType complex type
     */
    public void readCodeOrNilReasonListTypeAttributes(Map<String, String> attrMap, CodeOrNilReasonList bean) throws XMLStreamException
    {
        String val;
        
        // codespace
        val = attrMap.get("codeSpace");
        if (val != null)
            bean.setCodeSpace(val);
    }
    
    
    /**
     * Write method for CodeOrNilReasonListType complex type
     */
    public void writeCodeOrNilReasonListType(XMLStreamWriter writer, CodeOrNilReasonList bean) throws XMLStreamException
    {
        // TODO writeCodeOrNilReasonListType
        /*this.writeCodeOrNilReasonListTypeAttributes(writer, bean);
        
        writer.writeCharacters(getStringValue(bean.getValue()));*/
    }
    
    
    /**
     * Writes attributes of CodeOrNilReasonListType complex type
     */
    public void writeCodeOrNilReasonListTypeAttributes(XMLStreamWriter writer, CodeOrNilReasonList bean) throws XMLStreamException
    {
        
        // codeSpace
        if (bean.isSetCodeSpace())
            writer.writeAttribute("codeSpace", getStringValue(bean.getCodeSpace()));
    }
    
    
    /**
     * Dispatcher method for reading elements derived from AbstractMetaData
     */
    /*public AbstractMetaData readAbstractMetaData(XMLStreamReader reader) throws XMLStreamException
    {
        String localName = reader.getName().getLocalPart();
        
        if (localName.equals("GenericMetaData"))
            return readGenericMetaData(reader);
        
        throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
    }*/
    
    
    /**
     * Dispatcher method for writing classes derived from AbstractMetaData
     */
    /*public void writeAbstractMetaData(XMLStreamWriter writer, AbstractMetaData bean) throws XMLStreamException
    {
        if (bean instanceof GenericMetaData)
            writeGenericMetaData(writer, (GenericMetaData)bean);
    }*/
    
    
    /**
     * Dispatcher method for reading elements derived from AbstractTimeGeometricPrimitive
     */
    public AbstractTimeGeometricPrimitive readAbstractTimeGeometricPrimitive(XMLStreamReader reader) throws XMLStreamException
    {
        String localName = reader.getName().getLocalPart();
        
        if (localName.equals("TimeInstant"))
            return readTimeInstant(reader);
        else if (localName.equals("TimePeriod"))
            return readTimePeriod(reader);
        
        throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
    }
    
    
    /**
     * Dispatcher method for writing classes derived from AbstractTimeGeometricPrimitive
     */
    public void writeAbstractTimeGeometricPrimitive(XMLStreamWriter writer, AbstractTimeGeometricPrimitive bean) throws XMLStreamException
    {
        if (bean instanceof TimeInstant)
            writeTimeInstant(writer, (TimeInstant)bean);
        else if (bean instanceof TimePeriod)
            writeTimePeriod(writer, (TimePeriod)bean);
    }
    
    
    /**
     * Read method for TimeInstant elements
     */
    public TimeInstant readTimeInstant(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "TimeInstant");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readTimeInstantType(reader);
    }
    
    
    /**
     * Write method for TimeInstant element
     */
    public void writeTimeInstant(XMLStreamWriter writer, TimeInstant bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "TimeInstant");
        this.writeNamespaces(writer);
        this.writeTimeInstantType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for TimePeriod elements
     */
    public TimePeriod readTimePeriod(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "TimePeriod");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readTimePeriodType(reader);
    }
    
    
    /**
     * Write method for TimePeriod element
     */
    public void writeTimePeriod(XMLStreamWriter writer, TimePeriod bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "TimePeriod");
        this.writeNamespaces(writer);
        this.writeTimePeriodType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for TimePosition elements
     */
    public TimePosition readTimePosition(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "TimePosition");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readTimePositionType(reader);
    }
    
    
    /**
     * Write method for TimePosition element
     */
    public void writeTimePosition(XMLStreamWriter writer, TimePosition bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "TimePosition");
        this.writeNamespaces(writer);
        this.writeTimePositionType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for TimeInterval elements
     */
    public TimeIntervalLength readTimeInterval(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "TimeInterval");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readTimeIntervalLengthType(reader);
    }
    
    
    /**
     * Write method for TimeInterval element
     */
    public void writeTimeInterval(XMLStreamWriter writer, TimeIntervalLength bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "TimeInterval");
        this.writeNamespaces(writer);
        this.writeTimeIntervalLengthType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Dispatcher method for reading elements derived from AbstractFeature
     */
    public AbstractFeature readAbstractFeature(XMLStreamReader reader) throws XMLStreamException
    {
        String localName = reader.getName().getLocalPart();
        
        if (localName.equals("FeatureCollection"))
            return readFeatureCollection(reader);
        
        throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
    }
    
    
    /**
     * Dispatcher method for writing classes derived from AbstractFeature
     */
    public void writeAbstractFeature(XMLStreamWriter writer, AbstractFeature bean) throws XMLStreamException
    {
        if (bean instanceof FeatureCollection)
            writeFeatureCollection(writer, (FeatureCollection)bean);
        else
        {
            QName qName = bean.getQName();
            writer.writeStartElement(qName.getNamespaceURI(), qName.getLocalPart());
            this.writeNamespaces(writer);
            this.writeAbstractFeatureTypeAttributes(writer, bean);
            this.writeAbstractFeatureTypeElements(writer, bean);
            writer.writeEndElement();
        }
    }
    
    
    /**
     * Read method for FeatureCollection elements
     */
    public FeatureCollection readFeatureCollection(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "FeatureCollection");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readFeatureCollectionType(reader);
    }
    
    
    /**
     * Write method for FeatureCollection element
     */
    public void writeFeatureCollection(XMLStreamWriter writer, FeatureCollection bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "FeatureCollection");
        this.writeNamespaces(writer);
        this.writeFeatureCollectionType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Dispatcher method for reading elements derived from AbstractGeometry
     */
    public AbstractGeometry readAbstractGeometry(XMLStreamReader reader) throws XMLStreamException
    {
        String localName = reader.getName().getLocalPart();
        
        /*if (localName.equals("GeometricComplex"))
            return readGeometricComplex(reader);
        else if (localName.equals("CompositeCurve"))
            return readCompositeCurve(reader);
        else if (localName.equals("CompositeSurface"))
            return readCompositeSurface(reader);
        else if (localName.equals("CompositeSolid"))
            return readCompositeSolid(reader);
        else if (localName.equals("Grid"))
            return readGrid(reader);
        else if (localName.equals("RectifiedGrid"))
            return readRectifiedGrid(reader);
        else if (localName.equals("MultiGeometry"))
            return readMultiGeometry(reader);
        else if (localName.equals("MultiPoint"))
            return readMultiPoint(reader);
        else if (localName.equals("MultiCurve"))
            return readMultiCurve(reader);
        else if (localName.equals("MultiSurface"))
            return readMultiSurface(reader);
        else if (localName.equals("MultiSolid"))
            return readMultiSolid(reader);
        else */if (localName.equals("Point"))
            return readPoint(reader);
        else if (localName.equals("LineString"))
            return readLineString(reader);
        /*else if (localName.equals("Curve"))
            return readCurve(reader);
        else if (localName.equals("OrientableCurve"))
            return readOrientableCurve(reader);
        else if (localName.equals("Surface"))
            return readSurface(reader);
        else if (localName.equals("OrientableSurface"))
            return readOrientableSurface(reader);
        else if (localName.equals("Surface"))
            return readSurface(reader);
        else if (localName.equals("Surface"))
            return readSurface(reader);
        else if (localName.equals("Tin"))
            return readTin(reader);
        else if (localName.equals("Solid"))
            return readSolid(reader);*/
        else if (localName.equals("Polygon"))
            return readPolygon(reader);
        
        throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
    }
    
    
    /**
     * Dispatcher method for writing classes derived from AbstractGeometry
     */
    public void writeAbstractGeometry(XMLStreamWriter writer, AbstractGeometry bean) throws XMLStreamException
    {
        /*if (bean instanceof GeometricComplex)
            writeGeometricComplex(writer, (GeometricComplex)bean);
        else if (bean instanceof CompositeCurve)
            writeCompositeCurve(writer, (CompositeCurve)bean);
        else if (bean instanceof CompositeSurface)
            writeCompositeSurface(writer, (CompositeSurface)bean);
        else if (bean instanceof CompositeSolid)
            writeCompositeSolid(writer, (CompositeSolid)bean);
        else if (bean instanceof Grid)
            writeGrid(writer, (Grid)bean);
        else if (bean instanceof RectifiedGrid)
            writeRectifiedGrid(writer, (RectifiedGrid)bean);
        else if (bean instanceof MultiGeometry)
            writeMultiGeometry(writer, (MultiGeometry)bean);
        else if (bean instanceof MultiPoint)
            writeMultiPoint(writer, (MultiPoint)bean);
        else if (bean instanceof MultiCurve)
            writeMultiCurve(writer, (MultiCurve)bean);
        else if (bean instanceof MultiSurface)
            writeMultiSurface(writer, (MultiSurface)bean);
        else if (bean instanceof MultiSolid)
            writeMultiSolid(writer, (MultiSolid)bean);
        else if (bean instanceof Curve)
            writeCurve(writer, (Curve)bean);
        else if (bean instanceof OrientableCurve)
            writeOrientableCurve(writer, (OrientableCurve)bean);
        else if (bean instanceof Surface)
            writeSurface(writer, (Surface)bean);
        else if (bean instanceof OrientableSurface)
            writeOrientableSurface(writer, (OrientableSurface)bean);
        else if (bean instanceof Surface)
            writeSurface(writer, (Surface)bean);
        else if (bean instanceof Surface)
            writeSurface(writer, (Surface)bean);
        else if (bean instanceof Tin)
            writeTin(writer, (Tin)bean);
        else if (bean instanceof Solid)
            writeSolid(writer, (Solid)bean);*/
        if (bean instanceof Point)
            writePoint(writer, (Point)bean);
        else if (bean instanceof LineString)
            writeLineString(writer, (LineString)bean);
        else if (bean instanceof Polygon)
            writePolygon(writer, (Polygon)bean);
        else
            throw new XMLStreamException(ERROR_UNSUPPORTED_TYPE + bean.getClass().getCanonicalName());
    }
    
    
    /**
     * Read method for Envelope elements
     */
    public Envelope readEnvelope(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Envelope");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readEnvelopeType(reader);
    }
    
    
    /**
     * Write method for Envelope element
     */
    public void writeEnvelope(XMLStreamWriter writer, Envelope bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Envelope");
        this.writeNamespaces(writer);
        this.writeEnvelopeType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Point elements
     */
    public Point readPoint(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Point");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readPointType(reader);
    }
    
    
    /**
     * Write method for Point element
     */
    public void writePoint(XMLStreamWriter writer, Point bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Point");
        this.writeNamespaces(writer);
        this.writePointType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Dispatcher method for reading elements derived from AbstractCurve
     */
    public AbstractCurve readAbstractCurve(XMLStreamReader reader) throws XMLStreamException
    {
        String localName = reader.getName().getLocalPart();
        
        /*if (localName.equals("CompositeCurve"))
            return readCompositeCurve(reader);
        else if (localName.equals("Curve"))
            return readCurve(reader);
        else if (localName.equals("OrientableCurve"))
            return readOrientableCurve(reader);*/
        if (localName.equals("LineString"))
            return readLineString(reader);
            
        throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
    }
    
    
    /**
     * Dispatcher method for writing classes derived from AbstractCurve
     */
    public void writeAbstractCurve(XMLStreamWriter writer, AbstractCurve bean) throws XMLStreamException
    {
        /*if (bean instanceof CompositeCurve)
            writeCompositeCurve(writer, (CompositeCurve)bean);
        else if (bean instanceof Curve)
            writeCurve(writer, (Curve)bean);
        else if (bean instanceof OrientableCurve)
            writeOrientableCurve(writer, (OrientableCurve)bean);*/
        if (bean instanceof LineString)
            writeLineString(writer, (LineString)bean);
        else
            throw new XMLStreamException(ERROR_UNSUPPORTED_TYPE + bean.getClass().getCanonicalName());
    }
    
    
    /**
     * Read method for LineString elements
     */
    public LineString readLineString(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "LineString");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readLineStringType(reader);
    }
    
    
    /**
     * Write method for LineString element
     */
    public void writeLineString(XMLStreamWriter writer, LineString bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "LineString");
        this.writeNamespaces(writer);
        this.writeLineStringType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Dispatcher method for reading elements derived from AbstractSurface
     */
    public AbstractSurface readAbstractSurface(XMLStreamReader reader) throws XMLStreamException
    {
        String localName = reader.getName().getLocalPart();
        
        /*if (localName.equals("CompositeSurface"))
            return readCompositeSurface(reader);
        else if (localName.equals("Surface"))
            return readSurface(reader);
        else if (localName.equals("OrientableSurface"))
            return readOrientableSurface(reader);
        else if (localName.equals("Surface"))
            return readSurface(reader);
        else if (localName.equals("Surface"))
            return readSurface(reader);
        else if (localName.equals("Tin"))
            return readTin(reader);*/
        if (localName.equals("Polygon"))
            return readPolygon(reader);
        
        throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
    }
    
    
    /**
     * Dispatcher method for writing classes derived from AbstractSurface
     */
    public void writeAbstractSurface(XMLStreamWriter writer, AbstractSurface bean) throws XMLStreamException
    {
        /*if (bean instanceof CompositeSurface)
            writeCompositeSurface(writer, (CompositeSurface)bean);
        else if (bean instanceof Surface)
            writeSurface(writer, (Surface)bean);
        else if (bean instanceof OrientableSurface)
            writeOrientableSurface(writer, (OrientableSurface)bean);
        else if (bean instanceof Surface)
            writeSurface(writer, (Surface)bean);
        else if (bean instanceof Surface)
            writeSurface(writer, (Surface)bean);
        else if (bean instanceof Tin)
            writeTin(writer, (Tin)bean);*/
        if (bean instanceof Polygon)
            writePolygon(writer, (Polygon)bean);
        else
            throw new XMLStreamException(ERROR_UNSUPPORTED_TYPE + bean.getClass().getCanonicalName());
    }
    
    
    /**
     * Read method for Polygon elements
     */
    public Polygon readPolygon(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Polygon");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readPolygonType(reader);
    }
    
    
    /**
     * Write method for Polygon element
     */
    public void writePolygon(XMLStreamWriter writer, Polygon bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Polygon");
        this.writeNamespaces(writer);
        this.writePolygonType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for LinearRing elements
     */
    public LinearRing readLinearRing(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "LinearRing");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readLinearRingType(reader);
    }
    
    
    /**
     * Write method for LinearRing element
     */
    public void writeLinearRing(XMLStreamWriter writer, LinearRing bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "LinearRing");
        this.writeNamespaces(writer);
        this.writeLinearRingType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for LocationString elements
     */
    public String readLocationString(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "LocationString");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return trimStringValue(reader.getElementText());
    }
    
    
    /**
     * Write method for LocationString element
     */
    public void writeLocationString(XMLStreamWriter writer, String bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "LocationString");
        this.writeNamespaces(writer);
        writer.writeCharacters(bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for LocationKeyWord elements
     */
    public CodeWithAuthority readLocationKeyWord(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "LocationKeyWord");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readCodeType(reader);
    }
    
    
    /**
     * Write method for LocationKeyWord element
     */
    public void writeLocationKeyWord(XMLStreamWriter writer, CodeWithAuthority bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "LocationKeyWord");
        this.writeNamespaces(writer);
        this.writeCodeType(writer, bean);
        writer.writeEndElement();
    }
}
