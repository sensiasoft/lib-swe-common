/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2014 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis;


/**
 * <p>
 * Common interface for classes used to store a date/time epoch.
 * All implementations must be immutable.
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Nov 7, 2014
 */
public interface IDateTime
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
    public boolean equals(IDateTime other);
    
    
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
