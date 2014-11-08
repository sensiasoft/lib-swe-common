/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2014 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis;


/**
 * <p>
 * Immutable implementation of IDateTime storing value directly as a double
 * representing julian time (numer of decimal seconds past the 1970-01-01T00:00:00 epoch)
 * </p>
 *
 * <p>Copyright (c) 2014</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Oct 26, 2014
 */
public class DateTimeDouble implements IDateTime
{
    double julianTime;
    int timeZoneOffset;
    
    
    public DateTimeDouble()
    {
    }
    
    
    public DateTimeDouble(double julianTime)
    {
        this.julianTime = julianTime;
    }
    
    
    public DateTimeDouble(double julianTime, int timeZoneOffset)
    {
        this.julianTime = julianTime;
        this.timeZoneOffset = timeZoneOffset;
    }
    
    
    public DateTimeDouble copy()
    {
        return new DateTimeDouble(julianTime, timeZoneOffset);
    }
    
    
    public final int getTimeZoneOffset()
    {
        return timeZoneOffset;
    }


    public final double getAsDouble()
    {
        return julianTime;
    }
    
    
    @Override
    public final boolean equals(Object o)
    {
        if (o instanceof DateTimeDouble)
            return equals((DateTimeDouble)o);                
        return false;
    }
    
    
    @Override
    public final boolean equals(IDateTime other)
    {
        if (julianTime == other.getAsDouble())
            return true;
        
        return false;
    }
    
    
    @Override
    public final boolean isBefore(IDateTime other)
    {
        if (julianTime < other.getAsDouble())
            return true;        
        return false;
    }
    
    
    @Override
    public final boolean isBeforeOrEqual(IDateTime other)
    {
        if (julianTime <= other.getAsDouble())
            return true;
        return false;
    }
    
    
    @Override
    public final boolean isAfter(IDateTime other)
    {
        if (julianTime > other.getAsDouble())
            return true;
        return false;
    }
    
    
    @Override
    public final boolean isAfterOrEqual(IDateTime other)
    {
        if (julianTime >= other.getAsDouble())
            return true;
        return false;
    }
    
    
    @Override
    public final boolean isPositiveInfinity()
    {
        return (julianTime == Double.POSITIVE_INFINITY);
    }
    
    
    @Override
    public final boolean isNegativeInfinity()
    {
        return (julianTime == Double.NEGATIVE_INFINITY);
    }


    @Override
    public final boolean isNow()
    {
        return Double.isNaN(julianTime);
    }
}
