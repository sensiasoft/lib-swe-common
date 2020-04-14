/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are copyright (C) 2018, Sensia Software LLC
 All Rights Reserved. This software is the property of Sensia Software LLC.
 It cannot be duplicated, used, or distributed without the express written
 consent of Sensia Software LLC.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.namespace.QName;
import org.vast.ogc.xlink.IXlinkReference;
import org.vast.util.DateTimeFormat;
import org.vast.util.TimeExtent;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import gnu.trove.list.array.TDoubleArrayList;
import net.opengis.gml.v32.AbstractFeature;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.AbstractTimeGeometricPrimitive;
import net.opengis.gml.v32.Envelope;
import net.opengis.gml.v32.LineString;
import net.opengis.gml.v32.LinearRing;
import net.opengis.gml.v32.Point;
import net.opengis.gml.v32.Polygon;
import net.opengis.gml.v32.TimeInstant;
import net.opengis.gml.v32.TimePeriod;
import net.opengis.gml.v32.impl.GMLFactory;


/**
 * <p>
 * GeoJSON bindings using Gson JsonWriter/JsonReader.<br/>
 * This class is NOT threadsafe.
 * </p>
 *
 * @author Alex Robin
 * @date Nov 12, 2018
 */
public class GeoJsonBindings
{
    public static final String ERROR_UNSUPPORTED_TYPE = "Unsupported type: ";
    public static final String ERROR_INVALID_COORDINATES = "Invalid coordinate array";
    DecimalFormat formatter = new DecimalFormat(GMLFactory.COORDINATE_FORMAT);
    GMLFactory factory;
    
    
    public GeoJsonBindings()
    {
        this(false);
    }
    
    
    public GeoJsonBindings(boolean useJTS)
    {
        this(new GMLFactory(useJTS));
    }
    
    
    public GeoJsonBindings(GMLFactory factory)
    {
        this.factory = factory;
    }
    
    
    /////////////////////
    // Writing methods //
    /////////////////////
    
    public void writeFeature(JsonWriter writer, AbstractFeature bean) throws IOException
    {
        writer.beginObject();        
        writeStandardFeatureProperties(writer, bean);
        
        // custom properties
        boolean hasCustomProperties = bean instanceof GenericFeature && !((GenericFeature)bean).getProperties().isEmpty();
        if (hasCustomProperties)
        {
            writer.name("properties").beginObject();
            
            for (Entry<QName, Object> prop: ((GenericFeature)bean).getProperties().entrySet())
            {
                // prop name
                QName propName = prop.getKey();
                 
                // prop value
                Object val = prop.getValue();
                
                if (val instanceof Boolean)
                {
                    writer.name(propName.getLocalPart());
                    writer.value((Boolean)val);
                }
                else if (val instanceof Number)
                {
                    writer.name(propName.getLocalPart());
                    writer.value((Number)val);
                }
                else if (val instanceof String)
                {
                    writer.name(propName.getLocalPart());
                    writer.value((String)val);
                }
                else if (val instanceof IXlinkReference<?>)
                {
                    String href = ((IXlinkReference<?>) val).getHref();
                    if (href != null) 
                    {
                        writer.name(propName.getLocalPart());
                        writer.value(href);
                    }
                }
                else if (val instanceof AbstractGeometry)
                {
                    writer.name(propName.getLocalPart());
                    writeGeometry(writer, (AbstractGeometry)val);
                }
                else if (val instanceof AbstractTimeGeometricPrimitive)
                {
                    writer.name(propName.getLocalPart());
                    writeTimePrimitive(writer, (AbstractTimeGeometricPrimitive)val);
                }
            }
            
            writer.endObject();
        }
        
        writer.endObject();
    }
    
    
    protected void writeStandardFeatureProperties(JsonWriter writer, AbstractFeature bean) throws IOException
    {
        writer.name("type").value(bean.getQName().getLocalPart());
        
        // common properties
        writer.name("id").value(encodeFeatureID(bean));
        
        if (bean.isSetIdentifier())
            writer.name("uid").value(bean.getUniqueIdentifier());
     
        if (bean.getNumNames() > 0)
            writer.name("name").value(bean.getName());
        
        if (bean.isSetDescription())
            writer.name("description").value(bean.getDescription());
        
        // bbox
        if (bean.isSetBoundedBy())
        {
            writer.name("bbox");
            writeEnvelope(writer, bean.getBoundedBy());
        }
        
        // geometry
        if (bean.isSetGeometry())
        {
            writer.name("geometry");
            writeGeometry(writer, bean.getGeometry());
        }
        
        // geometry
        if (bean instanceof ITemporalFeature && ((ITemporalFeature) bean).getValidTime() != null)
        {
            writer.name("validTime");
            writeTimePeriod(writer, ((ITemporalFeature)bean).getValidTime());
        }
    }
    
    
    protected String encodeFeatureID(AbstractFeature bean) throws IOException
    {
        return bean.getId();
    }
    
    
    public void writeEnvelope(JsonWriter writer, Envelope bean) throws IOException
    {
        writer.beginArray();
        double[] low = bean.getLowerCorner();
        writer.jsonValue(formatter.format(low[0]));
        writer.jsonValue(formatter.format(low[1]));
        double[] high = bean.getUpperCorner();
        writer.jsonValue(formatter.format(high[0]));
        writer.jsonValue(formatter.format(high[1]));
        writer.endArray();
    }
    
    
    public void writeGeometry(JsonWriter writer, AbstractGeometry bean) throws IOException
    {
        if (bean instanceof Point)
            writePoint(writer, (Point)bean);
        else if (bean instanceof LineString)
            writeLineString(writer, (LineString)bean);
        else if (bean instanceof Polygon)
            writePolygon(writer, (Polygon)bean);
        else
            throw new JsonParseException(ERROR_UNSUPPORTED_TYPE + bean.getClass().getCanonicalName());
    }
    
    
    public void writeCommonGeometryProperties(JsonWriter writer, AbstractGeometry bean) throws IOException
    {
        if (bean.isSetSrsName())
            writer.name("crs").value(bean.getSrsName());
    }
    
    
    public void writePoint(JsonWriter writer, Point bean) throws IOException
    {
        writer.beginObject();
        writer.name("type").value(Point.class.getSimpleName());
        writeCommonGeometryProperties(writer, bean);
        
        // coordinates
        if (bean.isSetPos())
        {
            writer.name("coordinates");
            writeCoordinates(writer, bean.getPos(), 0, bean.getSrsDimension());
        }
        
        writer.endObject();
    }
    
    
    public void writeLineString(JsonWriter writer, LineString bean) throws IOException
    {
        writer.beginObject();
        writer.name("type").value(LineString.class.getSimpleName());
        writeCommonGeometryProperties(writer, bean);
        
        // coordinates
        if (bean.isSetPosList())
        {
            writer.name("coordinates");
            writer.beginArray();
            for (int i = 0; i < bean.getPosList().length; i += bean.getSrsDimension())
                writeCoordinates(writer, bean.getPosList(), i, bean.getSrsDimension());
            writer.endArray();
        }
        
        writer.endObject();
    }
    
    
    public void writePolygon(JsonWriter writer, Polygon bean) throws IOException
    {
        writer.beginObject();
        writer.name("type").value(Polygon.class.getSimpleName());
        writeCommonGeometryProperties(writer, bean);
        
        // coordinates
        if (bean.isSetExterior())
        {
            writer.name("coordinates");
            
            // there is one more level of array nesting in polygons
            writer.beginArray(); 
                        
            // exterior
            if (bean.isSetExterior())
                writeLinearRing(writer, bean.getExterior(), bean.getSrsDimension());
            
            // interior holes
            int numHoles = bean.getNumInteriors();
            for (int i = 0; i < numHoles; i++)
            {
                LinearRing item = bean.getInteriorList().get(i);
                writeLinearRing(writer, item, bean.getSrsDimension());
            }
            
            writer.endArray();
        }
        
        writer.endObject();
    }
    

    public void writeLinearRing(JsonWriter writer, LinearRing bean, int dims) throws IOException
    {
        writer.beginArray();
        for (int i = 0; i < bean.getPosList().length; i += dims)
            writeCoordinates(writer, bean.getPosList(), i, dims);
        writer.endArray();
    }
    

    public void writeCoordinates(JsonWriter writer, double[] coords, int index, int dims) throws IOException
    {
        writer.beginArray();
        for (int i = index; i < index + dims; i++)
            writer.jsonValue(formatter.format(coords[i]));
        writer.endArray();
    }
    
    
    public void writeTimePeriod(JsonWriter writer, TimeExtent bean) throws IOException
    {
        writer.beginArray();
        writeDateTimeValue(writer, bean.begin().atOffset(ZoneOffset.UTC));
        writeDateTimeValue(writer, bean.end().atOffset(ZoneOffset.UTC));
        writer.endArray();
    }
    
    
    public void writeTimePrimitive(JsonWriter writer, AbstractTimeGeometricPrimitive bean) throws IOException
    {
        if (bean instanceof TimeInstant)
            writeTimeInstant(writer, (TimeInstant)bean);
        else if (bean instanceof TimePeriod)
            writeTimePeriod(writer, (TimePeriod)bean);
    }
    
    
    public void writeTimeInstant(JsonWriter writer, TimeInstant bean) throws IOException
    {
        writeDateTimeValue(writer, bean.getTimePosition().getDateTimeValue());
    }
    
    
    public void writeTimePeriod(JsonWriter writer, TimePeriod bean) throws IOException
    {
        writer.beginArray();
        writeDateTimeValue(writer, bean.getBeginPosition().getDateTimeValue());
        writeDateTimeValue(writer, bean.getEndPosition().getDateTimeValue());
        writer.endArray();
    }
    
    
    protected void writeDateTimeValue(JsonWriter writer, OffsetDateTime dateTime) throws IOException
    {
        String isoString = dateTime.format(DateTimeFormat.ISO_DATE_OR_TIME_FORMAT);
        writer.value(isoString);
    }
    
    
    /////////////////////
    // Reading methods //
    /////////////////////
    
    public GenericFeature readFeature(JsonReader reader) throws IOException
    {
        reader.beginObject();
        
        String type = readObjectType(reader);
        GenericFeature f = createFeatureObject(type);
        
        while (reader.hasNext())
        {
            String name = reader.nextName();            
            if (!readObjectProperty(reader, f, name))
                reader.skipValue();
        }
        
        reader.endObject();
        return f;
    }
    
    
    protected GenericFeature createFeatureObject(String type)
    {
        if (!"Feature".equals(type))
            throw new JsonParseException("The type of a GeoJSON feature must be 'Feature'");
        
        return new GenericTemporalFeatureImpl(new QName(type));
    }
    
    
    protected String readObjectType(JsonReader reader) throws IOException
    {
        if (!reader.hasNext() || !"type".equals(reader.nextName()))
            throw new JsonParseException("'type' must be the first property of a GeoJSON object");
        
        return reader.nextString();
    }

    
    protected boolean readObjectProperty(JsonReader reader, AbstractFeature f, String name) throws IOException
    {
        if ("id".equals(name))
            f.setId(reader.nextString());
        
        else if ("uid".equals(name))
            f.setUniqueIdentifier(reader.nextString());
        
        else if ("bbox".equals(name))
            f.setBoundedByAsEnvelope(readEnvelope(reader));
        
        else if ("geometry".equals(name))
            f.setGeometry(readGeometry(reader));
        
        // also support other attributes root        
        else if ("name".equals(name))
            f.setName(reader.nextString());
        
        else if ("description".equals(name))
            f.setDescription(reader.nextString());
        
        else if (f instanceof GenericTemporalFeatureImpl && "validTime".equals(name))
            readValidTime(reader, (GenericTemporalFeatureImpl)f);
        
        else if ("properties".equals(name) && f instanceof GenericFeature)
            readCustomProperties(reader, ((GenericFeature)f).getProperties());
        
        else
            return false;
        
        return true;
    }
    
    
    protected String decodeFeatureID(String idVal, AbstractFeature bean) throws IOException
    {
        return idVal;
    }
    
    
    public void readCustomProperties(JsonReader reader, Map<QName, Object> map) throws IOException
    {
        reader.beginObject();
        
        while (reader.hasNext())
        {
            QName name = new QName(reader.nextName());
            JsonToken type = reader.peek();
            
            if (type == JsonToken.NUMBER)
            {
                map.put(name, (Double)reader.nextDouble());
            }
            else if (type == JsonToken.STRING)
            {
                // TODO add support for date/time as ISO string
                String val = reader.nextString();
                map.put(name, val);
            }
            /*else if (type == JsonToken.BEGIN_OBJECT)
            {
                reader.beginObject();
                Map<QName, Object> objMap = new HashMap<>();
                readCustomObjectProperties(reader, objMap);                
                reader.endObject();
                map.put(name, objMap);
            }*/
            else
                reader.skipValue();
        }
        
        reader.endObject();
    }
    
    
    public Envelope readEnvelope(JsonReader reader) throws IOException
    {
        reader.beginArray();
        Envelope env = factory.newEnvelope();
        env.setLowerCorner(new double[] {reader.nextDouble(), reader.nextDouble()});
        env.setUpperCorner(new double[] {reader.nextDouble(), reader.nextDouble()});
        reader.endArray();
        return env;
    }
    
    
    public AbstractGeometry readGeometry(JsonReader reader) throws IOException
    {
        reader.beginObject();
        String type = readObjectType(reader);
        
        AbstractGeometry geom = null;
        String crs = null;
        while (reader.hasNext())
        {
            String name = reader.nextName();
            
            if ("crs".equals(name))
                crs = reader.nextString();
            
            else if ("coordinates".equals(name))
            {
                if (Point.class.getSimpleName().equals(type))
                {
                    geom = factory.newPoint();
                    readCoordinates(reader, (Point)geom);
                }
                else if (LineString.class.getSimpleName().equals(type))
                {
                    geom = factory.newLineString();
                    readCoordinates(reader, (LineString)geom);
                }
                else if (Polygon.class.getSimpleName().equals(type))
                {
                    geom = factory.newPolygon();
                    readCoordinates(reader, (Polygon)geom);
                }
                else
                    throw new JsonParseException(ERROR_UNSUPPORTED_TYPE + type);                
            }                
                
            else
                reader.skipValue();
        }
        
        if (geom == null)
            throw new IOException("Missing coordinates array");
        
        if (crs != null)
            geom.setSrsName(crs);
        
        reader.endObject();
        return geom;
    }
    
    
    public void readCoordinates(JsonReader reader, Point geom) throws IOException
    {
        TDoubleArrayList posList = new TDoubleArrayList(3);
        int numDims = readCoordinates(reader, posList, 0);
        
        geom.setPos(posList.toArray());
        geom.setSrsDimension(numDims);
    }
    
    
    public void readCoordinates(JsonReader reader, LineString geom) throws IOException
    {
        int numDims = 0;
        
        reader.beginArray();
        TDoubleArrayList posList = new TDoubleArrayList(10);
        while (reader.hasNext())
            numDims = readCoordinates(reader, posList, numDims);
        reader.endArray();
        
        geom.setPosList(posList.toArray());
        geom.setSrsDimension(numDims);
    }
    
    
    public void readCoordinates(JsonReader reader, Polygon geom) throws IOException
    {
        int numDims = 0;
        
        reader.beginArray();
        numDims = readLinearRing(reader, geom, false, numDims);
        while (reader.hasNext())
            numDims = readLinearRing(reader, geom, true, numDims);
        
        reader.endArray();
    }
    

    public int readLinearRing(JsonReader reader, Polygon geom, boolean interior, int prevNumDims) throws IOException
    {
        int numDims = 0;
        
        reader.beginArray();
        TDoubleArrayList posList = new TDoubleArrayList(10);
        while (reader.hasNext())
            numDims = readCoordinates(reader, posList, numDims);
        reader.endArray();
        
        LinearRing ring = factory.newLinearRing();
        ring.setPosList(posList.toArray());

        geom.setSrsDimension(numDims);
        if (interior)
            geom.addInterior(ring);
        else
            geom.setExterior(ring);
        
        return numDims;
    }
    

    /*
     * Read coordinate tuple and return number of dimensions
     */
    public int readCoordinates(JsonReader reader, TDoubleArrayList coords, int prevNumDims) throws IOException
    {
        int numDims = 0;
        
        try
        {
            reader.beginArray();
            while (reader.hasNext())
            {
                coords.add(reader.nextDouble());
                numDims++;
            }
            reader.endArray();
        }
        catch (Exception e)
        {
            throw new JsonParseException(ERROR_INVALID_COORDINATES);
        }
        
        // check dimensionality is same as previous tuple in same geometry!
        if (prevNumDims > 0 && prevNumDims != numDims)
            throw new JsonParseException(ERROR_INVALID_COORDINATES + ": Tuples have different dimensionality");
        
        return numDims;
    }
    
    
    public void readValidTime(JsonReader reader, GenericTemporalFeatureImpl bean) throws IOException
    {
        reader.beginArray();
        OffsetDateTime beginTime = OffsetDateTime.parse(reader.nextString(), DateTimeFormat.ISO_DATE_OR_TIME_FORMAT);
        OffsetDateTime endTime = OffsetDateTime.parse(reader.nextString(), DateTimeFormat.ISO_DATE_OR_TIME_FORMAT);
        reader.endArray();
        bean.setValidTimePeriod(beginTime, endTime);
    }
}
