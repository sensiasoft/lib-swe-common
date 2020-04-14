/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2019 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.util;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;
import com.google.common.collect.Range;


/**
 * <p>
 * Immutable class for storing a time instant or time period.
 * </p><p>
 * This class also supports special cases of time instants at 'now', time
 * periods beginning or ending at 'now', and open-ended time periods.
 * </p><p>
 * Note that no time zone information is retained. It usually means begin and
 * end times are UTC unless otherwise specified by the application. See 
 * {@link ZonedTimeExtent} if you need to store a time extent with a time zone.
 * </p>
 *
 * @author Alex Robin
 * @since Apr 12, 2020
 * */
public class TimeExtent
{
    protected Instant begin = null; // null means 'now', Instant.MIN means unbounded
    protected Instant end = null; // null means 'now', Instant.MAX means unbounded
    transient Instant now = null;
    
    
    /**
     * @return A time extent representing the 'now' instant
     */
    public static TimeExtent now()
    {
        return new TimeExtent();
    }


    /**
     * @param t Time instant
     * @return A time extent representing a time instant
     */
    public static TimeExtent instant(Instant t)
    {
        Asserts.checkNotNull(t, Instant.class);
        TimeExtent time = new TimeExtent();
        time.begin = time.end = Asserts.checkNotNull(t, Instant.class);
        return time;
    }
    
    
    /**
     * @param begin Beginning of time period
     * @param end End of time period
     * @return A time extent representing a time period 
     */
    public static TimeExtent period(Instant begin, Instant end)
    {
        TimeExtent time = new TimeExtent();
        time.begin = Asserts.checkNotNull(begin, "begin");
        time.end = Asserts.checkNotNull(end, "end");
        return time;
    }
    
    
    /**
     * @return A time extent representing all times
     */
    public static TimeExtent allTimes()
    {
        return TimeExtent.period(Instant.MIN, Instant.MAX);
    }
    
    
    /**
     * @param begin Begin time instant
     * @return An open-ended time extent starting at the specified time
     */
    public static TimeExtent beginAt(Instant begin)
    {
        return TimeExtent.period(begin, Instant.MAX);
    }
    
    
    /**
     * @param end End time instant
     * @return An open time extent ending at the specified time
     */
    public static TimeExtent endAt(Instant end)
    {
        return TimeExtent.period(Instant.MIN, end);
    }
    
    
    /**
     * @param end End time instant
     * @return A time extent starting 'now' and ending at the specified time
     */
    public static TimeExtent beginNow(Instant end)
    {
        TimeExtent time = new TimeExtent();
        time.begin = null;
        time.end = Asserts.checkNotNull(end, "end");
        return time;
    }
    
    
    /**
     * @param begin Begin time instant
     * @return A time extent starting at the specified time and ending 'now' 
     */
    public static TimeExtent endNow(Instant begin)
    {
        TimeExtent time = new TimeExtent();
        time.begin = Asserts.checkNotNull(begin, "begin");
        time.end = null;
        return time;
    }
    
    
    protected TimeExtent()
    {        
    }    


    /**
     * @return True if begin time is defined, false otherwise
     */
    public boolean hasBegin()
    {
        return begin != Instant.MIN;
    }
    
    
    /**
     * @return The beginning instant of {@link Instant.MIN} if undefined.
     * If {@link #beginsAtNow()} also returns true, the current system time is returned.
     */
    public Instant begin()
    {
        return begin != null ? begin : getNow();
    }


    /**
     * @return True if end time is defined, false otherwise
     */
    public boolean hasEnd()
    {
        return end != Instant.MAX;
    }


    /**
     * @return The end instant of {@link Instant.MAX} if undefined.
     * If {@link #endsAtNow()} also returns true, the current system time is returned.
     */
    public Instant end()
    {
        return end != null ? end : getNow();
    }
    
    
    /**
     * @return True if this time extent represents a time instant,
     * false if it represents a time period
     */
    public boolean isInstant()
    {
        return Objects.equals(begin,  end);
    }
    
    
    /**
     * @return True if this time extent represents 'now', false otherwise
     */
    public boolean isNow()
    {
        return begin == null && end == null;
    }
    
    
    /**
     * @return True if this time extent begins at 'now', false otherwise
     */
    public boolean beginsNow()
    {
        return begin == null;
    }
    
    
    /**
     * @return True if this time extent ends at 'now', false otherwise
     */
    public boolean endsNow()
    {
        return end == null;
    }


    /**
     * @return The duration of this time extent
     */
    public Duration duration()
    {
        return Duration.between(begin(), end());
    }
    
    
    /**
     * @param other Another time extent 
     * @return True if the specified time extent is contained within this
     * time extent, false otherwise
     */
    public boolean contains(TimeExtent other)
    {
        Asserts.checkNotNull(other, TimeExtent.class);
        return asRange().encloses(other.asRange());
    }
    
    
    /**
     * @param t A time instant 
     * @return True if the specified time instant is contained within this
     * time extent, false otherwise
     */
    public boolean contains(Instant t)
    {
        Asserts.checkNotNull(t, Instant.class);
        
        return t.equals(begin()) ||
            t.equals(end()) ||
            (t.isAfter(begin())) && (t.isBefore(end()));
    }
    
    
    /**
     * @param other Another time extent 
     * @return True if the specified time extent intersects this time extent,
     * false otherwise
     */
    public boolean intersects(TimeExtent other)
    {
        Asserts.checkNotNull(other, TimeExtent.class);
        return asRange().isConnected(other.asRange());
    }

    
    /**
     * @param keepNow Keep the 'now' string if set to true, otherwise use the current time
     * @return The ISO8601 representation of this time extent
     */
    public String isoStringUTC(boolean keepNow)
    {
        if (isInstant())
            return begin.toString();
        
        StringBuilder sb = new StringBuilder();
        sb.append(begin != null ? begin : keepNow ? "now" : begin())
          .append('/')
          .append(end != null ? end : keepNow ? "now" : end());
        return sb.toString();
    }
    
    
    /**
     * @return This time extent as a {@link Range} of two time instants.<br/>
     * Note that calling this method is invalid if this time extent either begins
     * or ends at 'now'
     */
    public Range<Instant> asRange()
    {
        if (beginsNow() || endsNow())
            throw new IllegalStateException("A time extent relative to 'now' cannot be expressed as a range");
        
        if (begin == Instant.MIN)
            return Range.atMost(end());
        else if (end == Instant.MAX)
            return Range.atLeast(begin());
        else
            return Range.closed(begin(), end());
    }
    
    
    /**
     * @param timeZone The desired time zone
     * @return A new time extent with the associated time zone
     */
    public ZonedTimeExtent atZone(ZoneId timeZone)
    {
        ZonedTimeExtent te = new ZonedTimeExtent();
        te.begin = this.begin;
        te.end = this.end;
        te.timeZone = Asserts.checkNotNull(timeZone, ZoneId.class);
        return te;
    }


    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        
        if (!(obj instanceof TimeExtent))
            return false;
        
        TimeExtent other = (TimeExtent)obj;
        
        return Objects.equals(begin, other.begin())
            && Objects.equals(end, other.end());
    }
    
    
    @Override
    public int hashCode()
    {
        return Objects.hash(begin, end);
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append('[')
            .append(begin == null ? "now" : begin == Instant.MIN ? "-∞" : begin)
            .append(' ')
            .append(end == null ? "now" : end == Instant.MAX ? "+∞" : end)
            .append(']');
        return sb.toString();
    }
    
    
    Instant getNow()
    {
        if (now == null)
            now = Instant.now();
        return now;
    }
}
