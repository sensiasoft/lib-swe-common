/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32.impl;

import java.time.OffsetDateTime;
import org.vast.util.Asserts;
import org.vast.util.BaseBuilder;
import com.vividsolutions.jts.geom.GeometryFactory;
import net.opengis.gml.v32.CodeWithAuthority;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.CodeList;
import net.opengis.gml.v32.CodeOrNilReasonList;
import net.opengis.gml.v32.Envelope;
import net.opengis.gml.v32.FeatureCollection;
import net.opengis.gml.v32.LineString;
import net.opengis.gml.v32.LinearRing;
import net.opengis.gml.v32.Point;
import net.opengis.gml.v32.Polygon;
import net.opengis.gml.v32.Reference;
import net.opengis.gml.v32.TimeInstant;
import net.opengis.gml.v32.TimeIntervalLength;
import net.opengis.gml.v32.TimePeriod;
import net.opengis.gml.v32.TimePosition;
import net.opengis.gml.v32.Factory;


public class GMLFactory implements Factory
{
    static final String MIN_COORDINATES_ERROR_MSG = "Must have at least {} coordinates";
    static final String INVALID_NUMBER_COORDINATES_ERROR_MSG = "Invalid number of coordinates. Must be multiple of SRS dimension";
    public static final String COORDINATE_FORMAT = "0.########";
    
    boolean useJTS;
    GeometryFactory jtsFactory;
    
    
    public GMLFactory()
    {
        this(false);
    }
    
    
    public GMLFactory(boolean useJTS)
    {
        this.useJTS = useJTS;
        if (useJTS)
            jtsFactory = new GeometryFactory();
    }
    
    
    @Override
    public final FeatureCollection newFeatureCollection()
    {
        return new FeatureCollectionImpl();
    }
    
    
    @Override
    public final TimeInstant newTimeInstant()
    {
        return new TimeInstantImpl();
    }
    
    
    public final TimeInstant newTimeInstant(TimePosition timePosition)
    {
        TimeInstant instant = newTimeInstant();
        instant.setTimePosition(timePosition);
        return instant;
    }
    
    
    @Override
    public final TimePeriod newTimePeriod()
    {
        return new TimePeriodImpl();
    }
    
    
    public final TimePeriod newTimePeriod(TimePosition beginPosition, TimePosition endPosition)
    {
        TimePeriod period = newTimePeriod();
        period.setBeginPosition(beginPosition);
        period.setEndPosition(endPosition);
        return period;
    }
    
    
    @Override
    public final TimePosition newTimePosition()
    {
        return new TimePositionImpl();
    }
    
    
    public final TimePosition newTimePosition(double julianTime)
    {
        TimePosition timePos = newTimePosition();
        timePos.setDecimalValue(julianTime);
        return timePos;
    }
    
    
    public final TimePosition newTimePosition(OffsetDateTime dateTime)
    {
        TimePosition timePos = newTimePosition();
        timePos.setDateTimeValue(dateTime);
        return timePos;
    }
    
    
    @Override
    public final TimeIntervalLength newTimeIntervalLength()
    {
        return new TimeIntervalLengthImpl();
    }
        
    
    @Override
    public final Envelope newEnvelope()
    {
        return new EnvelopeImpl(2);
    }
    
    
    @Override
    public final Reference newReference()
    {
        return new ReferenceImpl();
    }
    
    
    @Override
    public final CodeWithAuthority newCode()
    {
        return new CodeWithAuthorityImpl();
    }
    
    
    @Override
    public final CodeList newCodeList()
    {
        return new CodeListImpl();
    }
    
    
    @Override
    public final CodeOrNilReasonList newCodeOrNilReasonList()
    {
        return new CodeOrNilReasonListImpl();
    }


    @Override
    public final Point newPoint()
    {
        Point point;
        
        if (useJTS)
            point = new PointJTS(jtsFactory, 3);
        else
            point = new PointImpl(3);
        
        return point;
    }
    
    
    public final Point newPoint(double... pos)
    {
        Point p = newPoint();
        p.setPos(pos);
        return p;
    }
    
    
    @Override
    public final LinearRing newLinearRing()
    {
        if (useJTS)
            return new LinearRingJTS(jtsFactory, 2);
        else
            return new LinearRingImpl();
    }


    @Override
    public final Polygon newPolygon()
    {
        Polygon poly;
        
        if (useJTS)
            poly = new PolygonJTS(jtsFactory, 2);
        else
            poly = new PolygonImpl(2);
        
        return poly;
    }


    @Override
    public final LineString newLineString()
    {
        LineString line;
        
        if (useJTS)
            line = new LineStringJTS(jtsFactory, 3);
        else
            line = new LineStringImpl(3);
        
        return line;
    }
    
    
    public Envelope newEnvelope(String crs, double minX, double minY, double maxX, double maxY)
    {
        Envelope env = newEnvelope();
        env.setSrsName(crs);
        env.setLowerCorner(new double[] {minX, minY});
        env.setUpperCorner(new double[] {maxX, maxY});
        return env;
    }
    
    
    public Envelope newEnvelope(String crs, double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
    {
        Envelope env = newEnvelope();
        env.setSrsName(crs);
        env.setLowerCorner(new double[] {minX, minY, minZ});
        env.setUpperCorner(new double[] {maxX, maxY, maxZ});
        return env;
    }
    
    
    public final PointBuilder pointBuilder()
    {
        return new PointBuilder();
    }
    
    
    public final LineStringBuilder lineStringBuilder()
    {
        return new LineStringBuilder();
    }
    
    
    public final PolygonBuilder polygonBuilder()
    {
        return new PolygonBuilder();
    }
    
    
    public class PointBuilder extends GeometryBuilder<PointBuilder, Point>
    {
        protected PointBuilder()
        {
            super(newPoint());
        }
        
        public PointBuilder withCoordinates(double... posList)
        {
            instance.setPos(posList);
            return this;
        }
        
        @Override
        public Point build()
        {
            Asserts.checkNotNull(instance.getPos(), "pos");
            Asserts.checkState(instance.getPos().length >= 2, MIN_COORDINATES_ERROR_MSG, 2);
            Asserts.checkState(instance.getPos().length == instance.getSrsDimension(), INVALID_NUMBER_COORDINATES_ERROR_MSG);
            return super.build();
        }
    }
    
    
    public class LineStringBuilder extends GeometryBuilder<LineStringBuilder, LineString>
    {
        protected LineStringBuilder()
        {
            super(newLineString());
        }
        
        public LineStringBuilder withCoordinates(double... posList)
        {
            instance.setPosList(posList);
            return this;
        }
        
        @Override
        public LineString build()
        {
            Asserts.checkNotNull(instance.getPosList(), "posList");
            Asserts.checkState(instance.getPosList().length >= 4, MIN_COORDINATES_ERROR_MSG, 4);
            Asserts.checkState(instance.getPosList().length % instance.getSrsDimension() == 0, INVALID_NUMBER_COORDINATES_ERROR_MSG);
            return super.build();
        }
    }
    
    
    public class PolygonBuilder extends GeometryBuilder<PolygonBuilder, Polygon>
    {       
        protected PolygonBuilder()
        {
            super(newPolygon());
        }        
        
        public PolygonBuilder withExterior(double... posList)
        {
            LinearRing exterior = newLinearRing();
            exterior.setPosList(posList);
            instance.setExterior(exterior);
            return this;
        }
        
        public PolygonBuilder withInterior(double... posList)
        {
            LinearRing hole = newLinearRing();
            hole.setPosList(posList);
            instance.addInterior(hole);
            return this;
        }
        
        @Override
        public Polygon build()
        {
            Asserts.checkState(instance.isSetExterior(), "No exterior set");
            Asserts.checkNotNull(instance.getExterior().getPosList(), "posList");
            Asserts.checkState(instance.getExterior().getPosList().length >= 6, MIN_COORDINATES_ERROR_MSG, 3);
            Asserts.checkState(instance.getExterior().getPosList().length % instance.getSrsDimension() == 0, INVALID_NUMBER_COORDINATES_ERROR_MSG);
            return super.build();
        }
    }
    
    
    protected abstract static class GeometryBuilder<B extends GeometryBuilder<B, T>, T extends AbstractGeometry> extends BaseBuilder<T>
    {
        protected GeometryBuilder(T instance)
        {
            super(instance);
        }
        
        @SuppressWarnings("unchecked")
        public B withDimension(int numDims)
        {
            instance.setSrsDimension(numDims);
            return (B)this;
        }
        
        @SuppressWarnings("unchecked")
        public B withSrs(String srsName, int numDims)
        {
            instance.setSrsName(srsName);
            instance.setSrsDimension(numDims);
            return (B)this;
        }
    }
    
}
