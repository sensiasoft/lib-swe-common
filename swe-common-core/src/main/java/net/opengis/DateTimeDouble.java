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
 * Immutable implementation of IDateTime storing value directly as a double
 * representing julian time (numer of decimal seconds past the 1970-01-01T00:00:00 epoch)
 * </p>
 *
 * @author Alex Robin
 * @since Oct 26, 2014
 */
public class DateTimeDouble implements IDateTime
{
    private static final long serialVersionUID = 6784991803827485545L;
    double julianTime;
    byte timeZoneOffset;
    
    
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
        this.timeZoneOffset = (byte)timeZoneOffset;
    }
    
    
    public DateTimeDouble copy()
    {
        return new DateTimeDouble(julianTime, timeZoneOffset);
    }
    
    
    @Override
    public final int getTimeZoneOffset()
    {
        return timeZoneOffset;
    }


    @Override
    public final double getAsDouble()
    {
        return julianTime;
    }
    
    
    @Override
    public final boolean equals(Object other)
    {
        if (other instanceof DateTimePrecise)
        {
            double otherVal = ((DateTimeDouble)other).getAsDouble();
            if (Double.compare(julianTime, otherVal) == 0)
                return true;
        }
        
        return false;
    }
    
    
    @Override
    public final int hashCode()
    {
        return Double.hashCode(julianTime) + 31*timeZoneOffset;
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
