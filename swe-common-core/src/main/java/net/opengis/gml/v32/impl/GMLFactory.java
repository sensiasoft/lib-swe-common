package net.opengis.gml.v32.impl;

import net.opengis.gml.v32.Code;
import net.opengis.gml.v32.CodeList;
import net.opengis.gml.v32.CodeOrNilReasonList;
import net.opengis.gml.v32.CodeWithAuthority;
import net.opengis.gml.v32.Envelope;
import net.opengis.gml.v32.FeatureCollection;
import net.opengis.gml.v32.Point;
import net.opengis.gml.v32.Reference;
import net.opengis.gml.v32.StringOrRef;
import net.opengis.gml.v32.TimeInstant;
import net.opengis.gml.v32.TimeIntervalLength;
import net.opengis.gml.v32.TimePeriod;
import net.opengis.gml.v32.TimePosition;
import net.opengis.gml.v32.Factory;


public class GMLFactory implements Factory
{
    
    public GMLFactory()
    {
    }
       
    
    public StringOrRef newStringOrRef()
    {
        return new StringOrRefImpl();
    }
    
    
    public FeatureCollection newFeatureCollection()
    {
        return new FeatureCollectionImpl();
    }
    
    
    public TimeInstant newTimeInstant()
    {
        return new TimeInstantImpl();
    }
    
    
    public TimePeriod newTimePeriod()
    {
        return new TimePeriodImpl();
    }
    
    
    public TimePosition newTimePosition()
    {
        return new TimePositionImpl();
    }
    
    
    public TimeIntervalLength newTimeIntervalLength()
    {
        return new TimeIntervalLengthImpl();
    }
        
    
    public Envelope newEnvelope()
    {
        return new EnvelopeImpl();
    }
    
    
    public Point newPoint()
    {
        return new PointImpl();
    }
    
    
    public Reference newReference()
    {
        return new ReferenceImpl();
    }
    
    
    public Code newCode()
    {
        return new CodeImpl();
    }
    
    
    public CodeWithAuthority newCodeWithAuthority()
    {
        return new CodeWithAuthorityImpl();
    }
    
    
    public CodeList newCodeList()
    {
        return new CodeListImpl();
    }
    
    
    public CodeOrNilReasonList newCodeOrNilReasonList()
    {
        return new CodeOrNilReasonListImpl();
    }

}
