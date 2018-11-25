/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are copyright (C) 2018, Sensia Software LLC
 All Rights Reserved. This software is the property of Sensia Software LLC.
 It cannot be duplicated, used, or distributed without the express written
 consent of Sensia Software LLC.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import java.io.Serializable;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.vast.util.Asserts;
import org.vast.util.NumberUtils;
import com.google.common.base.Objects;


/**
 * <p>
 * Datatype representing union between ISO datetime and decimal time stamp
 * </p>
 *
 * @author Alex Robin
 * @date Apr 14, 2018
 */
public class DateTimeOrDouble implements Serializable, Comparable<DateTimeOrDouble>
{
    private static final long serialVersionUID = 3817896411351988594L;
    
    Instant utcDateTime;
    ZoneOffset zoneOffset = ZoneOffset.UTC;
    double decimalTime = Double.NaN;
    
    
    public DateTimeOrDouble()
    {        
    }
    
    
    public DateTimeOrDouble(double val)
    {
        this(val, false);
    }
    
    
    public DateTimeOrDouble(double val, boolean isDateTime)
    {
        if (isDateTime)
            setDateTime(val);
        else
            setDecimalTime(val);
    }
    
    
    public DateTimeOrDouble(OffsetDateTime dateTime)
    {
        setDateTime(dateTime);
    }


    @Override
    public int compareTo(DateTimeOrDouble other)
    {
        checkCompatibleTimeScale(other);
        
        if (utcDateTime != null)
            return getDateTime().compareTo(other.getDateTime());
        else
            return Double.compare(decimalTime, other.decimalTime);
    }
    
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        
        if (!(obj instanceof DateTimeOrDouble))
            return false;
        
        DateTimeOrDouble other = (DateTimeOrDouble)obj;
        
        return Objects.equal(utcDateTime, other.utcDateTime) &&
               Objects.equal(zoneOffset,  other.zoneOffset) &&
               NumberUtils.ulpEquals(decimalTime, other.decimalTime);
    }
    
    
    @Override
    public int hashCode()
    {
        return Objects.hashCode(
                utcDateTime,
                zoneOffset,
                decimalTime);
    }
    
    
    public boolean isBefore(DateTimeOrDouble other)
    {
        checkCompatibleTimeScale(other);
        
        if (utcDateTime != null)
            return getDateTime().isBefore(other.getDateTime());
        else
            return decimalTime <= other.decimalTime;
    }
    
    
    public boolean isAfter(DateTimeOrDouble other)
    {
        checkCompatibleTimeScale(other);
        
        if (utcDateTime != null)
            return getDateTime().isAfter(other.getDateTime());
        else
            return decimalTime >= other.decimalTime;
    }
    
    
    private void checkCompatibleTimeScale(DateTimeOrDouble other)
    {
        Asserts.checkArgument(
               (utcDateTime != null && other.utcDateTime == null) ||
               (utcDateTime == null && other.utcDateTime != null), 
                "Incompatible time scales");
    }
    
    
    public double getAsDouble()
    {
        if (utcDateTime != null)
        {
            if (Instant.MIN.equals(utcDateTime))
                return Double.NEGATIVE_INFINITY;
            else if (Instant.MAX.equals(utcDateTime))
                return Double.POSITIVE_INFINITY;
            else
                return utcDateTime.getEpochSecond() + utcDateTime.getNano()/1e9;
        }
        else
            return decimalTime;
    }
    
    
    public boolean isDateTime()
    {
        return utcDateTime != null;
    }


    public OffsetDateTime getDateTime()
    {
        if (utcDateTime == null)
            return null;
        else if (Instant.MIN.equals(utcDateTime))
            return OffsetDateTime.MIN;
        else if (Instant.MAX.equals(utcDateTime))
            return OffsetDateTime.MAX;
        else
            return OffsetDateTime.ofInstant(utcDateTime, zoneOffset);
    }


    public void setDateTime(Instant dateTime, ZoneOffset offset)
    {
        this.utcDateTime = dateTime;
        this.zoneOffset = offset;
    }
    
    
    public void setDateTime(OffsetDateTime dateTime)
    {
        if (OffsetDateTime.MIN.equals(dateTime))
            this.utcDateTime = Instant.MIN;
        else if (OffsetDateTime.MAX.equals(dateTime))
            this.utcDateTime = Instant.MAX;
        else
            this.utcDateTime = dateTime.withOffsetSameInstant(ZoneOffset.UTC).toInstant();
        
        this.zoneOffset = dateTime.getOffset();
    }
    
    
    public void setDateTime(double julianTime)
    {
        setDateTime(julianTime, ZoneOffset.UTC);
    }
    
    
    public void setDateTime(double julianTime, ZoneOffset offset)
    {
        long epochSeconds = (long)julianTime;
        // improve rounding error by pre-multiplying by 1000
        int nanos = (int)((julianTime*1000. - epochSeconds*1000L)*1e6);
        this.utcDateTime = Instant.ofEpochSecond(epochSeconds, nanos);
        this.zoneOffset = offset;
    }


    public double getDecimalTime()
    {
        return decimalTime;
    }


    public void setDecimalTime(double decimalTime)
    {
        this.decimalTime = decimalTime;
    }
    
    
    @Override
    public String toString()
    {
        if (utcDateTime != null)
            return getDateTime().toString();
        else
            return Double.toString(decimalTime);
    }
}
