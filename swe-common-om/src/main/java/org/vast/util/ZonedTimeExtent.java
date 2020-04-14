/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2020 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.util;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;


/**
 * <p>
 * A {@link TimeExtent} with a time zone.
 * </p><p>
 * Begin and end time are always stored as UTC instants, but a zoned
 * </p>
 *
 * @author Alex Robin
 * @date Apr 13, 2020
 */
public class ZonedTimeExtent extends TimeExtent
{
    protected ZoneId timeZone = ZoneOffset.UTC;
    
    
    protected ZonedTimeExtent()
    {        
    }
    
    
    public ZoneId timeZone()
    {
        return timeZone;
    }
    
    
    public OffsetDateTime beginAtZone()
    {
        return OffsetDateTime.ofInstant(begin(), timeZone);
    }
    
    
    public OffsetDateTime endAtZone()
    {
        return OffsetDateTime.ofInstant(end(), timeZone);
    }


    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        
        if (!(obj instanceof ZonedTimeExtent))
            return false;
        
        ZonedTimeExtent other = (ZonedTimeExtent)obj;
        
        return Objects.equals(begin, other.begin())
            && Objects.equals(end, other.end())
            && Objects.equals(timeZone, other.timeZone());
    }
    
    
    @Override
    public int hashCode()
    {
        return Objects.hash(begin, end, timeZone);
    }
}
