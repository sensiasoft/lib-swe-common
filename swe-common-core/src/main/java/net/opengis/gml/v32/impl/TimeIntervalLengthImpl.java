package net.opengis.gml.v32.impl;

import net.opengis.gml.v32.TimeIntervalLength;


/**
 * POJO class for XML type TimeIntervalLengthType(@http://www.opengis.net/gml/3.2).
 *
 */
public class TimeIntervalLengthImpl implements TimeIntervalLength
{
    static final long serialVersionUID = 1L;
    protected Object unit;
    protected Integer radix;
    protected Integer factor;
    protected double value;
    
    
    public TimeIntervalLengthImpl()
    {
    }
    
    
    /**
     * Gets the unit property
     */
    @Override
    public Object getUnit()
    {
        return unit;
    }
    
    
    /**
     * Sets the unit property
     */
    @Override
    public void setUnit(Object unit)
    {
        this.unit = unit;
    }
    
    
    /**
     * Gets the radix property
     */
    @Override
    public int getRadix()
    {
        return radix;
    }
    
    
    /**
     * Checks if radix is set
     */
    @Override
    public boolean isSetRadix()
    {
        return (radix != null);
    }
    
    
    /**
     * Sets the radix property
     */
    @Override
    public void setRadix(int radix)
    {
        this.radix = radix;
    }
    
    
    /**
     * Unsets the radix property
     */
    @Override
    public void unSetRadix()
    {
        this.radix = null;
    }
    
    
    /**
     * Gets the factor property
     */
    @Override
    public int getFactor()
    {
        return factor;
    }
    
    
    /**
     * Checks if factor is set
     */
    @Override
    public boolean isSetFactor()
    {
        return (factor != null);
    }
    
    
    /**
     * Sets the factor property
     */
    @Override
    public void setFactor(int factor)
    {
        this.factor = factor;
    }
    
    
    /**
     * Unsets the factor property
     */
    @Override
    public void unSetFactor()
    {
        this.factor = null;
    }
    
    
    /**
     * Gets the inline value
     */
    @Override
    public double getValue()
    {
        return value;
    }
    
    
    /**
     * Sets the inline value
     */
    @Override
    public void setValue(double value)
    {
        this.value = value;
    }
}
