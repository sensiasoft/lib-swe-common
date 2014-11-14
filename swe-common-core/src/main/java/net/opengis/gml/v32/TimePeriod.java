package net.opengis.gml.v32;

import net.opengis.IDateTime;
import net.opengis.OgcProperty;


/**
 * POJO class for XML type TimePeriodType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface TimePeriod extends AbstractTimeGeometricPrimitive
{
    
    
    /**
     * Gets the beginPosition property
     */
    public TimePosition getBeginPosition();
    
    
    /**
     * Checks if beginPosition is set
     */
    public boolean isSetBeginPosition();
    
    
    /**
     * Sets the beginPosition property
     */
    public void setBeginPosition(TimePosition beginPosition);
    
    
    /**
     * Gets the begin property
     */
    public TimeInstant getBegin();
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the begin property
     */
    public OgcProperty<TimeInstant> getBeginProperty();
    
    
    /**
     * Checks if begin is set
     */
    public boolean isSetBegin();
    
    
    /**
     * Sets the begin property
     */
    public void setBegin(TimeInstant begin);
    
    
    /**
     * Gets the endPosition property
     */
    public TimePosition getEndPosition();
    
    
    /**
     * Checks if endPosition is set
     */
    public boolean isSetEndPosition();
    
    
    /**
     * Sets the endPosition property
     */
    public void setEndPosition(TimePosition endPosition);
    
    
    /**
     * Gets the end property
     */
    public TimeInstant getEnd();
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the end property
     */
    public OgcProperty<TimeInstant> getEndProperty();
    
    
    /**
     * Checks if end is set
     */
    public boolean isSetEnd();
    
    
    /**
     * Sets the end property
     */
    public void setEnd(TimeInstant end);
    
    
    /**
     * Gets the duration property
     */
    public IDateTime getDuration();
    
    
    /**
     * Checks if duration is set
     */
    public boolean isSetDuration();
    
    
    /**
     * Sets the duration property
     */
    public void setDuration(IDateTime duration);
    
    
    /**
     * Gets the timeInterval property
     */
    public TimeIntervalLength getTimeInterval();
    
    
    /**
     * Checks if timeInterval is set
     */
    public boolean isSetTimeInterval();
    
    
    /**
     * Sets the timeInterval property
     */
    public void setTimeInterval(TimeIntervalLength timeInterval);
}
