/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2014 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis;


/**
 * <p>
 * Immutable implementation of a DateTime object
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Oct 26, 2014
 */
public class DateTimePrecise
{
    long seconds;
    double fractionOfSeconds;
    byte timeZoneOffset;
    
    
    public DateTimePrecise()
    {
    }
    
    
    public DateTimePrecise copy()
    {
        return new DateTimePrecise(seconds, fractionOfSeconds, timeZoneOffset);
    }
    
    
    public DateTimePrecise(double time)
    {
        if (time == Double.POSITIVE_INFINITY)
            this.seconds = Long.MAX_VALUE;
        else if (time == Double.NEGATIVE_INFINITY)
            this.seconds = Long.MIN_VALUE;
        else
            this.seconds = (long)time;
        
        this.fractionOfSeconds = time - this.seconds;
    }
    
    
    public DateTimePrecise(long seconds, double fractionOfSeconds)
    {
        this.seconds = seconds;
        this.fractionOfSeconds = fractionOfSeconds;
    }
    
    
    public DateTimePrecise(long seconds, double fractionOfSeconds, byte timeZoneOffset)
    {
        this.seconds = seconds;
        this.fractionOfSeconds = fractionOfSeconds;
        this.timeZoneOffset = timeZoneOffset;
    }


    public long getSeconds()
    {
        return seconds;
    }


    public double getFractionOfSeconds()
    {
        return fractionOfSeconds;
    }
    
    
    public byte getTimeZoneOffset()
    {
        return timeZoneOffset;
    }


    public double getAsDouble()
    {
        return seconds + fractionOfSeconds;
    }
    
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof DateTimePrecise)
            return equals((DateTimePrecise)o);
                
        return false;
    }
    
    
    public boolean equals(DateTimePrecise other)
    {
        if (other.seconds == seconds && other.fractionOfSeconds == fractionOfSeconds)
            return true;
        
        return false;
    }
    
    
    /**
     * Check if this date is before the specified date
     * @param other
     * @return true if condition is satisfied
     */
    public boolean isBefore(DateTimePrecise other)
    {
        if (seconds < other.seconds)
            return true;
        
        if (seconds == other.seconds && fractionOfSeconds < other.fractionOfSeconds)
            return true;
        
        return false;
    }
    
    
    /**
     * Check if this date is before or equal to the specified date
     * @param other
     * @return true if condition is satisfied
     */
    public boolean isBeforeOrEqual(DateTimePrecise other)
    {
        if (equals(other) || isBefore(other))
            return true;
        return false;
    }
    
    
    /**
     * Check if this date is after the specified date
     * @param other
     * @return true if condition is satisfied
     */
    public boolean isAfter(DateTimePrecise other)
    {
        if (seconds > other.seconds)
            return true;
        
        if (seconds == other.seconds && fractionOfSeconds > other.fractionOfSeconds)
            return true;
        
        return false;
    }
    
    
    /**
     * Check if this date is after or equal to the specified date
     * @param other
     * @return true if condition is satisfied
     */
    public boolean isAfterOrEqual(DateTimePrecise other)
    {
        if (equals(other) || isAfter(other))
            return true;
        return false;
    }
    
    
    public boolean isPositiveInfinity()
    {
        return (seconds == Long.MAX_VALUE);
    }
    
    
    public boolean isNegativeInfinity()
    {
        return (seconds == Long.MIN_VALUE);
    }


    public boolean isNow()
    {
        // TODO add 'now' support
        return false;
    }
}
