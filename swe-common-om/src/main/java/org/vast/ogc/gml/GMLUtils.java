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

 Please Contact Alexandre Robin or 
 Mike Botts <mike.botts@botts-inc.net for more information.
 
 Contributor(s): 
    Alexandre Robin
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.time.ZoneOffset;
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
import net.opengis.gml.v32.TimePeriod;
import net.opengis.gml.v32.TimePosition;
import net.opengis.gml.v32.impl.GMLFactory;
import org.vast.util.Bbox;
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
    
    
    public void registerFeatureBinding(IFeatureStaxBindings<AbstractFeature> binding)
    {
        ((GMLStaxBindings)staxBindings).registerFeatureBinding(binding);
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
    public static TimeExtent timePrimitiveToTimeExtent(AbstractTimeGeometricPrimitive timePrimitive)
    {
        TimeExtent timeExtent = null;
        
        if (timePrimitive instanceof TimeInstant)
        {
            TimePosition timePos = ((TimeInstant) timePrimitive).getTimePosition();
            if (timePos.getIndeterminatePosition() == TimeIndeterminateValue.NOW)
                timeExtent = TimeExtent.now();
            else if (timePos.getIndeterminatePosition() == TimeIndeterminateValue.UNKNOWN)
                return null;
            else
                timeExtent = TimeExtent.instant(timePos.getDateTimeValue().toInstant());           
        }
        else if (timePrimitive instanceof TimePeriod)
        {
            TimePeriod timePeriod = ((TimePeriod) timePrimitive);
            TimePosition beginPos, endPos;
            boolean beginUnknown = false, beginNow = false;
            boolean endUnknown = false, endNow = false;
            
            // begin
            beginPos = timePeriod.getBeginPosition();
            if (beginPos.getIndeterminatePosition() == TimeIndeterminateValue.NOW)
                beginNow = true;
            else if (beginPos.getIndeterminatePosition() == TimeIndeterminateValue.UNKNOWN)
                beginUnknown = true;
            
            // end
            endPos = timePeriod.getEndPosition();
            if (endPos.getIndeterminatePosition() == TimeIndeterminateValue.NOW)
                endNow = true;
            else if (endPos.getIndeterminatePosition() == TimeIndeterminateValue.UNKNOWN)
                endUnknown = true;
            
            if (beginUnknown)
                timeExtent = TimeExtent.endAt(endPos.getDateTimeValue().toInstant());
            else if (beginNow)
                timeExtent = TimeExtent.beginNow(endPos.getDateTimeValue().toInstant());
            else if (endNow)
                timeExtent = TimeExtent.endNow(beginPos.getDateTimeValue().toInstant());
            else if (endUnknown)
                timeExtent = TimeExtent.beginAt(beginPos.getDateTimeValue().toInstant());
            else
                timeExtent = TimeExtent.period(
                    beginPos.getDateTimeValue().toInstant(),
                    endPos.getDateTimeValue().toInstant());
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
        // time instant
        if ((timeExtent == null || timeExtent.isInstant()) && !forcePeriod)
        {
            TimePosition timePosition = gmlFactory.newTimePosition();
            
            if (timeExtent == null)
                timePosition.setIndeterminatePosition(TimeIndeterminateValue.UNKNOWN);
            else if (timeExtent.isNow())
                timePosition.setIndeterminatePosition(TimeIndeterminateValue.NOW);
            else
                timePosition.setDateTimeValue(timeExtent.begin().atOffset(ZoneOffset.UTC));
            
            return gmlFactory.newTimeInstant(timePosition);
        }
        
        // time period
        else
        {
            TimePosition beginPosition = gmlFactory.newTimePosition();
            TimePosition endPosition = gmlFactory.newTimePosition();
            TimePeriod timePeriod = gmlFactory.newTimePeriod(beginPosition, endPosition);
            
            // case of null period
            if (timeExtent == null)
            {
                beginPosition.setIndeterminatePosition(TimeIndeterminateValue.UNKNOWN);
                endPosition.setIndeterminatePosition(TimeIndeterminateValue.UNKNOWN);
            }
            
            else
            {
                Instant begin = timeExtent.begin();
                Instant end = timeExtent.end();
                
                // begin
                if (!timeExtent.hasBegin())
                    beginPosition.setIndeterminatePosition(TimeIndeterminateValue.UNKNOWN);
                else if (timeExtent.beginsNow())
                    beginPosition.setIndeterminatePosition(TimeIndeterminateValue.NOW);
                else
                    beginPosition.setDateTimeValue(begin.atOffset(ZoneOffset.UTC));
                    
                // end
                if (!timeExtent.hasEnd())
                    endPosition.setIndeterminatePosition(TimeIndeterminateValue.UNKNOWN);
                else if (timeExtent.endsNow())
                    endPosition.setIndeterminatePosition(TimeIndeterminateValue.NOW);
                else
                    endPosition.setDateTimeValue(end.atOffset(ZoneOffset.UTC));
            }
                        
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
