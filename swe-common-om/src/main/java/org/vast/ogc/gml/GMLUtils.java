/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.

 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or 
 Mike Botts <mike.botts@botts-inc.net for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import net.opengis.gml.v32.AbstractFeature;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.AbstractTimeGeometricPrimitive;
import net.opengis.gml.v32.Envelope;
import net.opengis.gml.v32.TimeIndeterminateValue;
import net.opengis.gml.v32.TimeInstant;
import net.opengis.gml.v32.TimeIntervalLength;
import net.opengis.gml.v32.TimePeriod;
import net.opengis.gml.v32.TimePosition;
import net.opengis.gml.v32.impl.GMLFactory;
import org.vast.util.Bbox;
import org.vast.util.NumberUtils;
import org.vast.util.TimeExtent;
import org.vast.xml.DOMHelper;
import org.vast.xml.IndentingXMLStreamWriter;
import org.vast.xml.XMLBindingsUtils;
import org.vast.xml.XMLImplFinder;
import org.vast.xml.XMLReaderException;
import org.vast.xml.XMLWriterException;
import org.w3c.dom.Element;


/**
 * <p>
 * Utility methods to read/write GML documents.
 * This class is not thread-safe.
 * </p>
 *
 * @author Alex Robin
 * @since Feb 8, 2014
 */
public class GMLUtils extends XMLBindingsUtils
{
    public final static String GML = "GML";
    public final static String V3_2 = "3.2";
    GMLFactory gmlFactory;
    
    
    enum ObjectType
    {
        Envelope { @Override public String toString() { return "GML Envelope"; } },
        Geometry { @Override public String toString() { return "GML Geometry"; } },
        TimePrimitive { @Override public String toString() { return "GML Time Primitive"; } },
        Feature { @Override public String toString() { return "GML Feature"; } }
    }
    
    
    public GMLUtils(String version)
    {
        // TODO load correct bindings for desired version
        staxBindings = new GMLStaxBindings(true);
        gmlFactory = ((GMLStaxBindings)staxBindings).getFactory();
    }
    
    
    public GMLUtils(String version, IFeatureStaxBindings... featureBindings)
    {
        this(version);
        ((GMLStaxBindings)staxBindings).registerFeatureBindings(featureBindings);
    }
    
    
    /**
     * Reads a GML generic feature directly from an input stream
     * @param inputStream input stream to parse from
     * @return the new feature instance
     * @throws XMLReaderException
     */
    public GenericFeature readFeature(InputStream inputStream) throws XMLReaderException
    {
        return (GenericFeature)readFromStream(inputStream, ObjectType.Feature);
    }
    
    
    /**
     * Reads a GML generic feature from a DOM element
     * @param dom parent DOM helper instance
     * @param featureElt element to parse from
     * @return the new feature instance
     * @throws XMLReaderException
     */
    public GenericFeature readFeature(DOMHelper dom, Element featureElt) throws XMLReaderException
    {
        return (GenericFeature)readFromDom(dom, featureElt, ObjectType.Feature);
    }
    
    
    /**
     * Reads a GML geometry from a DOM element
     * @param dom parent DOM helper instance
     * @param geomElt element to parse from
     * @return the new geometry instance
     * @throws XMLReaderException
     */
    public AbstractGeometry readGeometry(DOMHelper dom, Element geomElt) throws XMLReaderException
    {
        return (AbstractGeometry)readFromDom(dom, geomElt, ObjectType.Geometry);
    }
    
    
    /**
     * Reads a GML time primitive from a DOM element
     * @param dom parent DOM helper instance
     * @param timeElt element to parse from
     * @return the new time primitive instance
     * @throws XMLReaderException
     */
    public AbstractTimeGeometricPrimitive readTimePrimitive(DOMHelper dom, Element timeElt) throws XMLReaderException
    {
        return (AbstractTimeGeometricPrimitive)readFromDom(dom, timeElt, ObjectType.TimePrimitive);
    }
    
    
    /**
     * Reads a GML time primitive from a DOM element as a {@link TimeExtent} object
     * @param dom parent DOM helper instance
     * @param timeElt element to parse from
     * @return the new TimeExtent instance
     * @throws XMLReaderException
     */
    public TimeExtent readTimePrimitiveAsTimeExtent(DOMHelper dom, Element timeElt) throws XMLReaderException
    {
        AbstractTimeGeometricPrimitive timePrimitive = readTimePrimitive(dom, timeElt);
        return timePrimitiveToTimeExtent(timePrimitive);
    }
    
    
    /**
     * Reads a GML envelope from a DOM element
     * @param dom parent DOM helper instance
     * @param envElt element to parse from
     * @return the new envelope instance
     * @throws XMLReaderException
     */
    public Envelope readEnvelope(DOMHelper dom, Element envElt) throws XMLReaderException
    {
        return (Envelope)readFromDom(dom, envElt, ObjectType.Envelope);
    }
    
    
    /**
     * Reads a GML {@link Envelope} from a DOM element as a {@link Bbox} object
     * @param dom parent DOM helper instance
     * @param envElt element to parse from
     * @return the new Bbox instance
     * @throws XMLReaderException
     */
    public Bbox readEnvelopeAsBbox(DOMHelper dom, Element envElt) throws XMLReaderException
    {
        Envelope env = readEnvelope(dom, envElt);
        return envelopeToBbox(env);
    }
    
    
    /**
     * Writes a GML feature as a DOM element
     * @param dom parent DOM helper instance
     * @param feature generic feature instance to serialize
     * @return the newly created DOM element
     * @throws XMLWriterException
     */
    public Element writeFeature(DOMHelper dom, AbstractFeature feature) throws XMLWriterException
    {
        dom.addNSDeclaration(GMLStaxBindings.NS_PREFIX_GML, GMLStaxBindings.NS_URI);
        return writeToDom(dom, feature, ObjectType.Feature);
    }
    
    
    public void writeFeature(OutputStream os, AbstractFeature feature, boolean indent) throws XMLWriterException, IOException
    {
        try
        {
            XMLOutputFactory factory = XMLImplFinder.getStaxOutputFactory();
            XMLStreamWriter writer = factory.createXMLStreamWriter(os, encoding);
            if (indent)
                writer = new IndentingXMLStreamWriter(writer);
            staxBindings.setNamespacePrefixes(writer);
            staxBindings.declareNamespacesOnRootElement();
            writeToXmlStream(writer, feature, ObjectType.Feature);
            writer.close();
        }
        catch (XMLStreamException e)
        {
            throw new XMLWriterException("Error while writing " + ObjectType.Feature + " to output stream", e);
        }
    }
    
    
    /**
     * Writes a GML geometry as a DOM element
     * @param dom parent DOM helper instance
     * @param geom geometry instance to serialize
     * @return the newly created DOM element
     * @throws XMLWriterException
     */
    public Element writeGeometry(DOMHelper dom, AbstractGeometry geom) throws XMLWriterException
    {
        dom.addNSDeclaration(GMLStaxBindings.NS_PREFIX_GML, GMLStaxBindings.NS_URI);
        return writeToDom(dom, geom, ObjectType.Geometry);
    }
    
    
    /**
     * Writes a GML time primitive as a DOM element
     * @param dom parent DOM helper instance
     * @param timePrimitive time primitive instance to serialize
     * @return the newly created DOM element
     * @throws XMLWriterException
     */
    public Element writeTimePrimitive(DOMHelper dom, AbstractTimeGeometricPrimitive timePrimitive) throws XMLWriterException
    {
        dom.addNSDeclaration(GMLStaxBindings.NS_PREFIX_GML, GMLStaxBindings.NS_URI);
        return writeToDom(dom, timePrimitive, ObjectType.TimePrimitive);
    }
    
    
    /**
     * Writes a {@link TimeExtent} object as a GML time period DOM element
     * @param dom parent DOM helper instance
     * @param timeExtent TimeExtent instance to serialize
     * @return the newly created DOM element
     * @throws XMLWriterException
     */
    public Element writeTimeExtentAsTimePeriod(DOMHelper dom, TimeExtent timeExtent) throws XMLWriterException
    {
        AbstractTimeGeometricPrimitive timePrimitive = timeExtentToTimePrimitive(timeExtent, true);
        return writeTimePrimitive(dom, timePrimitive);
    }
    
    
    /**
     * Writes a {@link TimeExtent} object as a GML time primitive DOM element
     * @param dom parent DOM helper instance
     * @param timeExtent TimeExtent instance to serialize
     * @return the newly created DOM element
     * @throws XMLWriterException
     */
    public Element writeTimeExtentAsTimePrimitive(DOMHelper dom, TimeExtent timeExtent) throws XMLWriterException
    {
        AbstractTimeGeometricPrimitive timePrimitive = timeExtentToTimePrimitive(timeExtent, false);
        return writeTimePrimitive(dom, timePrimitive);
    }
    
    
    /**
     * Writes a GML envelope as a DOM element
     * @param dom parent DOM helper instance
     * @param env envelope instance to serialize
     * @return the newly created DOM element
     * @throws XMLWriterException
     */
    public Element writeEnvelope(DOMHelper dom, Envelope env) throws XMLWriterException
    {
        dom.addNSDeclaration(GMLStaxBindings.NS_PREFIX_GML, GMLStaxBindings.NS_URI);
        return writeToDom(dom, env, ObjectType.Envelope);
    }
    
    
    /**
     * Writes a {@link Bbox} object as a GML {@link Envelope} DOM element
     * @param dom parent DOM helper instance
     * @param bbox Bbox instance to serialize
     * @return the newly created DOM element
     * @throws XMLWriterException
     */
    public Element writeBboxAsEnvelope(DOMHelper dom, Bbox bbox) throws XMLWriterException
    {
        Envelope env = bboxToEnvelope(bbox, gmlFactory);
        return writeEnvelope(dom, env);
    }
        
    
    @Override
    protected Object readFromXmlStream(XMLStreamReader reader, Enum<?> eltType) throws XMLStreamException
    {
        reader.nextTag();
        GMLStaxBindings gmlBindings = (GMLStaxBindings)staxBindings;
        
        switch ((ObjectType)eltType)
        {
            case Envelope:
                return gmlBindings.readEnvelope(reader);
                
            case Geometry:
                return gmlBindings.readAbstractGeometry(reader);
                
            case TimePrimitive:
                return gmlBindings.readAbstractTimeGeometricPrimitive(reader);
                                
            case Feature:
                return gmlBindings.readAbstractFeature(reader);
        }
        
        return null;
    }
    
    
    @Override
    protected void writeToXmlStream(XMLStreamWriter writer, Object gmlObj, Enum<?> eltType) throws XMLStreamException
    {
        GMLStaxBindings gmlBindings = (GMLStaxBindings)staxBindings;
        
        switch ((ObjectType)eltType)
        {
            case Envelope:
                gmlBindings.writeEnvelope(writer, (Envelope)gmlObj);
                return;
                
            case Geometry:
                gmlBindings.writeAbstractGeometry(writer, (AbstractGeometry)gmlObj);
                return;
                
            case TimePrimitive:
                gmlBindings.writeAbstractTimeGeometricPrimitive(writer, (AbstractTimeGeometricPrimitive)gmlObj);
                return;
                
            case Feature:
                gmlBindings.writeAbstractFeature(writer, (AbstractFeature)gmlObj);
                return;
        }
    }
    
    
    /**
     * Utility method to convert a {@link AbstractTimeGeometricPrimitive} to a {@link TimeExtent} object
     * @param timePrimitive GML time primitive
     * @return new TimeExtent instance
     */
    public TimeExtent timePrimitiveToTimeExtent(AbstractTimeGeometricPrimitive timePrimitive)
    {
        TimePosition timePos;
        TimeExtent timeExtent = new TimeExtent();
        boolean beginUnknown = false;
        boolean endUnknown = false;
        
        if (timePrimitive instanceof TimeInstant)
        {
            timePos = ((TimeInstant) timePrimitive).getTimePosition();
            if (timePos.getIndeterminatePosition() == TimeIndeterminateValue.NOW)
                timeExtent.setBaseAtNow(true);
            else if (timePos.getIndeterminatePosition() == TimeIndeterminateValue.UNKNOWN)
                timeExtent.nullify();
            else
                timeExtent.setBaseTime(timePos.getDecimalValue());            
        }
        else if (timePrimitive instanceof TimePeriod)
        {
            TimePeriod timePeriod = ((TimePeriod) timePrimitive);
            
            // begin
            timePos = timePeriod.getBeginPosition();
            if (timePos.getIndeterminatePosition() == TimeIndeterminateValue.NOW)
                timeExtent.setBeginNow(true);
            else if (timePos.getIndeterminatePosition() == TimeIndeterminateValue.UNKNOWN)
                beginUnknown = true;
            else
                timeExtent.setStartTime(timePos.getDecimalValue());
            
            // end
            timePos = timePeriod.getEndPosition();
            if (timePos.getIndeterminatePosition() == TimeIndeterminateValue.NOW)
                timeExtent.setEndNow(true);
            else if (timePos.getIndeterminatePosition() == TimeIndeterminateValue.UNKNOWN)
                endUnknown = true;
            else
                timeExtent.setStopTime(timePos.getDecimalValue());
            
            // handle case of period specified with unknown begin or end time
            if (timePeriod.isSetDuration())
            {
                double duration = timePeriod.getDuration();
                if (beginUnknown)
                    timeExtent.setLagTimeDelta(duration);
                if (endUnknown)
                    timeExtent.setLeadTimeDelta(duration);
            }
            else if (beginUnknown && endUnknown)
                timeExtent.nullify();
            
            // get time step from timeInterval
            if (timePeriod.isSetTimeInterval())
            {
                TimeIntervalLength interval = timePeriod.getTimeInterval();
                timeExtent.setTimeStep(interval.getValue()); // for now we assume it's in seconds
            }
        }
        
        return timeExtent;
    }
    
    
    /**
     * Utility method to convert a {@link TimeExtent} to a {@link AbstractTimeGeometricPrimitive} object
     * @param timeExtent TimeExtent object
     * @param forcePeriod Set to true to force output to be a GML time period
     * @return new GML time primitive instance
     */
    public AbstractTimeGeometricPrimitive timeExtentToTimePrimitive(TimeExtent timeExtent, boolean forcePeriod)
    {
        double begin = timeExtent.getStartTime();
        double end = timeExtent.getStopTime();
        
        // time instant
        if (timeExtent.isTimeInstant() && !forcePeriod)
        {
            TimePosition timePosition = gmlFactory.newTimePosition();
            
            if (timeExtent.isNull())
                timePosition.setIndeterminatePosition(TimeIndeterminateValue.UNKNOWN);
            else if (timeExtent.isBeginNow() || timeExtent.isEndNow() || timeExtent.isBaseAtNow())
                timePosition.setIndeterminatePosition(TimeIndeterminateValue.NOW);
            else
                timePosition.setDecimalValue(begin);
            
            return gmlFactory.newTimeInstant(timePosition);
        }
        
        // time period
        else
        {
            TimePosition beginPosition = gmlFactory.newTimePosition();
            TimePosition endPosition = gmlFactory.newTimePosition();
            TimePeriod timePeriod = gmlFactory.newTimePeriod(beginPosition, endPosition);
            
            // case of null period
            if (timeExtent.isNull())
            {
                beginPosition.setIndeterminatePosition(TimeIndeterminateValue.UNKNOWN);
                endPosition.setIndeterminatePosition(TimeIndeterminateValue.UNKNOWN);
            }
            
            // case of relative begin or end (now +/- period)
            else if (timeExtent.isBaseAtNow())
            {
                if (timeExtent.getLeadTimeDelta() > 0.0 && timeExtent.getLagTimeDelta() > 0.0)
                {
                    double now = System.currentTimeMillis() / 1000.;
                    beginPosition.setDecimalValue(now - timeExtent.getLagTimeDelta());
                    endPosition.setDecimalValue(now + timeExtent.getLeadTimeDelta());
                }
                else if (NumberUtils.ulpEquals(timeExtent.getLagTimeDelta(), 0.0))
                {
                    beginPosition.setIndeterminatePosition(TimeIndeterminateValue.NOW);
                    endPosition.setIndeterminatePosition(TimeIndeterminateValue.AFTER);
                    timePeriod.setDuration(timeExtent.getLeadTimeDelta());
                }
                else if (NumberUtils.ulpEquals(timeExtent.getLeadTimeDelta(), 0.0))
                {
                    beginPosition.setIndeterminatePosition(TimeIndeterminateValue.BEFORE);
                    endPosition.setIndeterminatePosition(TimeIndeterminateValue.NOW);
                    timePeriod.setDuration(timeExtent.getLagTimeDelta());
                }             
            }
            
            // case of absolute begin and end
            else
            {
                // begin
                if (timeExtent.isBeginNow())
                    beginPosition.setIndeterminatePosition(TimeIndeterminateValue.NOW);
                else
                    beginPosition.setDecimalValue(begin);
                    
                // end
                if (timeExtent.isEndNow())
                    endPosition.setIndeterminatePosition(TimeIndeterminateValue.NOW);
                else
                    endPosition.setDecimalValue(end);
            }
            
            // time step
            if (!NumberUtils.ulpEquals(timeExtent.getTimeStep(), 0.0))
                timePeriod.setTimeInterval(timeExtent.getTimeStep());
                        
            return timePeriod;
        }
    }
    
    
    /**
     * Utility method to convert a {@link Bbox} object to a GML {@link Envelope}
     * @param bbox Bbox object
     * @return GML envelope
     */
    public Envelope bboxToEnvelope(Bbox bbox)
    {
        return gmlFactory.newEnvelope(bbox.getCrs(), bbox.getMinX(), bbox.getMinY(), bbox.getMaxX(), bbox.getMaxY());
    }
    
    
    /**
     * Utility method to convert a {@link Bbox} object to a GML {@link Envelope}
     * @param bbox Bbox object
     * @param gmlFac FActory used to create GML objects
     * @return GML envelope
     */
    public static Envelope bboxToEnvelope(Bbox bbox, GMLFactory gmlFac)
    {
        return gmlFac.newEnvelope(bbox.getCrs(), bbox.getMinX(), bbox.getMinY(), bbox.getMaxX(), bbox.getMaxY());
    }
    
    
    /**
     * Utility method to convert from a GML {@link Envelope} to a {@link Bbox} object
     * @param env GML envelope
     * @return Bbox instance
     */
    public static Bbox envelopeToBbox(Envelope env)
    {
        Bbox bbox = new Bbox();
        bbox.setCrs(env.getSrsName());
        
        double[] lowerCorner = env.getLowerCorner();
        bbox.setMinX(lowerCorner[0]);
        bbox.setMinY(lowerCorner[1]);
        if (lowerCorner.length > 2)
            bbox.setMinZ(lowerCorner[2]);
        
        double[] upperCorner = env.getUpperCorner();
        bbox.setMaxX(upperCorner[0]);
        bbox.setMaxY(upperCorner[1]);
        if (upperCorner.length > 2)
            bbox.setMaxZ(upperCorner[2]);
        
        return bbox;
    }


    public GMLFactory getGmlFactory()
    {
        return gmlFactory;
    }
}
