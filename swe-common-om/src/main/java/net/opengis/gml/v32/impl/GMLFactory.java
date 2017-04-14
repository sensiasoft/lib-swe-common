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

import com.vividsolutions.jts.geom.GeometryFactory;
import net.opengis.IDateTime;
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
import net.opengis.gml.v32.TimeInstant;
import net.opengis.gml.v32.TimeIntervalLength;
import net.opengis.gml.v32.TimePeriod;
import net.opengis.gml.v32.TimePosition;
import net.opengis.gml.v32.Factory;


public class GMLFactory implements Factory
{
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
    
    
    public final TimePosition newTimePosition(IDateTime dateTime)
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
}
