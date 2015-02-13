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
import net.opengis.gml.v32.Code;
import net.opengis.gml.v32.CodeList;
import net.opengis.gml.v32.CodeOrNilReasonList;
import net.opengis.gml.v32.CodeWithAuthority;
import net.opengis.gml.v32.Envelope;
import net.opengis.gml.v32.FeatureCollection;
import net.opengis.gml.v32.LineString;
import net.opengis.gml.v32.LinearRing;
import net.opengis.gml.v32.Point;
import net.opengis.gml.v32.Polygon;
import net.opengis.gml.v32.Reference;
import net.opengis.gml.v32.StringOrRef;
import net.opengis.gml.v32.TimeInstant;
import net.opengis.gml.v32.TimeIntervalLength;
import net.opengis.gml.v32.TimePeriod;
import net.opengis.gml.v32.TimePosition;
import net.opengis.gml.v32.Factory;


public class GMLFactory implements Factory
{
    boolean useJTS;
    int geomIdCounter = 1;
    int timeIdCounter = 1;
    StringBuilder sb = new StringBuilder();
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
       
    
    public final StringOrRef newStringOrRef()
    {
        return new StringOrRefImpl();
    }
    
    
    public final FeatureCollection newFeatureCollection()
    {
        return new FeatureCollectionImpl();
    }
    
    
    public final TimeInstant newTimeInstant()
    {
        TimeInstant instant = new TimeInstantImpl();
        instant.setId(newTimeId());
        return instant;
    }
    
    
    public final TimeInstant newTimeInstant(TimePosition timePosition)
    {
        TimeInstant instant = newTimeInstant();
        instant.setTimePosition(timePosition);
        return instant;
    }
    
    
    public final TimePeriod newTimePeriod()
    {
        TimePeriod period = new TimePeriodImpl();
        period.setId(newTimeId());
        return period;
    }
    
    
    public final TimePeriod newTimePeriod(TimePosition beginPosition, TimePosition endPosition)
    {
        TimePeriod period = newTimePeriod();
        period.setBeginPosition(beginPosition);
        period.setEndPosition(endPosition);
        return period;
    }
    
    
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
    
    
    public final TimeIntervalLength newTimeIntervalLength()
    {
        return new TimeIntervalLengthImpl();
    }
        
    
    public final Envelope newEnvelope()
    {
        if (useJTS)
            return new EnvelopeJTS();
        else
            return new EnvelopeImpl();
    }
    
    
    public final Point newPoint()
    {
        if (useJTS)
            return new PointJTS(jtsFactory);
        else
            return new PointImpl();
    }
    
    
    public final Reference newReference()
    {
        return new ReferenceImpl();
    }
    
    
    public final Code newCode()
    {
        return new CodeImpl();
    }
    
    
    public final CodeWithAuthority newCodeWithAuthority()
    {
        return new CodeWithAuthorityImpl();
    }
    
    
    public final CodeList newCodeList()
    {
        return new CodeListImpl();
    }
    
    
    public final CodeOrNilReasonList newCodeOrNilReasonList()
    {
        return new CodeOrNilReasonListImpl();
    }


    @Override
    public final LinearRing newLinearRing()
    {
        if (useJTS)
            return new LinearRingJTS(jtsFactory);
        else
            return new LinearRingImpl();
    }


    @Override
    public final Polygon newPolygon()
    {
        if (useJTS)
            return new PolygonJTS(jtsFactory);
        else
            return new PolygonImpl();
    }


    @Override
    public final LineString newLineString()
    {
        if (useJTS)
            return new LineStringJTS(jtsFactory);
        else
            return new LineStringImpl();
    }
    
    
    public Envelope newEnvelope(String crs, double minX, double minY, double maxX, double maxY)
    {
        Envelope env = newEnvelope();
        env.setSrsName(crs);
        env.setLowerCorner(new double[] {minX, minY});
        env.setUpperCorner(new double[] {maxX, maxY});
        return env;
    }

    
    public final void resetGeomIdCounter(int geomIdCounter)
    {
        this.geomIdCounter = geomIdCounter;
    }


    public final void resetTimeIdCounter(int timeIdCounter)
    {
        this.timeIdCounter = timeIdCounter;
    }


    private final String newTimeId()
    {
        sb.setLength(0);
        sb.append('T');
        sb.append(timeIdCounter++);
        return sb.toString();
    }
}
