package net.opengis.swe.v20;

import java.util.List;


/**
 * POJO class for XML type AllowedValuesType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface AllowedValues extends AbstractSWE
{
    
    
    /**
     * Gets the list of value properties
     */
    public List<Double> getValueList();
    
    
    /**
     * Returns number of value properties
     */
    public int getNumValues();
    
    
    /**
     * Adds a new value property
     */
    public void addValue(double value);
    
    
    /**
     * Gets the list of interval properties
     */
    public List<double[]> getIntervalList();
    
    
    /**
     * Returns number of interval properties
     */
    public int getNumIntervals();
    
    
    /**
     * Adds a new interval property
     */
    public void addInterval(double[] interval);
    
    
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
