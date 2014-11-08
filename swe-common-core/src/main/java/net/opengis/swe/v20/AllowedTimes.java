package net.opengis.swe.v20;

import java.util.List;
import net.opengis.IDateTime;


/**
 * POJO class for XML type AllowedTimesType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface AllowedTimes extends AbstractSWE, DataConstraint
{
    
    
    /**
     * Gets the list of value properties
     */
    public List<IDateTime> getValueList();
    
    
    /**
     * Returns number of value properties
     */
    public int getNumValues();
    
    
    /**
     * Adds a new value property
     */
    public void addValue(IDateTime value);
    
    
    /**
     * Gets the list of interval properties
     */
    public List<IDateTime[]> getIntervalList();
    
    
    /**
     * Returns number of interval properties
     */
    public int getNumIntervals();
    
    
    /**
     * Adds a new interval property
     */
    public void addInterval(IDateTime[] interval);
    
    
    /**
     * Gets the significantFigures property
     */
    public int getSignificantFigures();
    
    
    /**
     * Checks if significantFigures is set
     */
    public boolean isSetSignificantFigures();
    
    
    /**
     * Sets the significantFigures property
     */
    public void setSignificantFigures(int significantFigures);
    
    
    /**
     * Unsets the significantFigures property
     */
    public void unSetSignificantFigures();
}
