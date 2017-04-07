/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis;


/**
 * <p>
 * Immutable implementation of a DateTime object
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
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
    public boolean equals(Object other)
    {
        if (other instanceof DateTimePrecise)
        {
            DateTimePrecise otherDate = (DateTimePrecise)other;
            if (otherDate.seconds == seconds && Double.compare(fractionOfSeconds, otherDate.fractionOfSeconds) == 0)
                return true;
        }
        
        return false;
    }
    
    
    @Override
    public final int hashCode()
    {
        return Long.hashCode(seconds) + 31*Double.hashCode(fractionOfSeconds) + 67*timeZoneOffset;
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
