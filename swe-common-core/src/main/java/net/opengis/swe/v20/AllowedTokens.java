package net.opengis.swe.v20;

import java.util.List;


/**
 * POJO class for XML type AllowedTokensType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface AllowedTokens extends AbstractSWE, DataConstraint
{
    
    
    /**
     * Gets the list of value properties
     */
    public List<String> getValueList();
    
    
    /**
     * Returns number of value properties
     */
    public int getNumValues();
    
    
    /**
     * Adds a new value property
     */
    public void addValue(String value);
    
    
    /**
     * Gets the pattern property
     */
    public String getPattern();
    
    
    /**
     * Checks if pattern is set
     */
    public boolean isSetPattern();
    
    
    /**
     * Sets the pattern property
     */
    public void setPattern(String pattern);
}
