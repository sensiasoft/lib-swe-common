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

import java.io.Serializable;

/**
 * <p>
 * Common interface for classes used to store a date/time epoch.
 * All implementations must be immutable.
 * </p>
 *
 * @author Alex Robin
 * @since Nov 7, 2014
 */
public interface IDateTime extends Serializable
{  
    
    /**
     * Get the time zone offset associated to this date
     * @return integer offset in hours
     */
    public int getTimeZoneOffset();


    /**
     * Get this date as julian time.
     * This is expressed in seconds past 01-01-1970 the epoch.
     * @return double representation of date
     */
    public double getAsDouble();
    
    
    /**
     * Check if this date is equal to the specified date
     * @param other
     * @return true if condition is satisfied
     */
    @Override
    public boolean equals(Object other);
    
    
    /**
     * Check if this date is before the specified date
     * @param other
     * @return true if condition is satisfied
     */
    public boolean isBefore(IDateTime other);
    
    
    /**
     * Check if this date is before or equal to the specified date
     * @param other
     * @return true if condition is satisfied
     */
    public boolean isBeforeOrEqual(IDateTime other);
    
    
    /**
     * Check if this date is after the specified date
     * @param other
     * @return true if condition is satisfied
     */
    public boolean isAfter(IDateTime other);
    
    
    /**
     * Check if this date is after or equal to the specified date
     * @param other
     * @return true if condition is satisfied
     */
    public boolean isAfterOrEqual(IDateTime other);
    
    
    /**
     * @return true if this date is set to positive infinity, false otherwise
     */
    public boolean isPositiveInfinity();
    
    
    /**
     * @return true if this date is set to negative infinity, false otherwise
     */
    public boolean isNegativeInfinity();


    /**
     * @return true if this date is set to the 'now' special value, false otherwise
     */
    public boolean isNow();
}
