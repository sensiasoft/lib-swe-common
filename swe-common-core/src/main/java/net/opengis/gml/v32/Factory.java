package net.opengis.gml.v32;



public interface Factory
{
    
    
    public StringOrRef newStringOrRef();
    
    
    public TimeInstant newTimeInstant();
    
    
    public TimePeriod newTimePeriod();
    
    
    public TimePosition newTimePosition();
    
    
    public TimeIntervalLength newTimeIntervalLength();
    
    
    public Envelope newEnvelope();
    
    
    public Point newPoint();
    
    
    public Reference newReference();
    
    
    public Code newCode();
    
    
    public CodeWithAuthority newCodeWithAuthority();
    
    
    public CodeList newCodeList();
    
    
    public CodeOrNilReasonList newCodeOrNilReasonList();


    public FeatureCollection newFeatureCollection();
}
