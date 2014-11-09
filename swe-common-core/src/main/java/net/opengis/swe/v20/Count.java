package net.opengis.swe.v20;


/**
 * POJO class for XML type CountType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface Count extends ScalarComponent, HasConstraints<AllowedValues>
{
        
    /**
     * Gets the value property
     */
    public int getValue();
    
    
    /**
     * Checks if value is set
     */
    public boolean isSetValue();
    
    
    /**
     * Sets the value property
     */
    public void setValue(int value);
    
    
    /**
     * Unsets the value property
     */
    public void unSetValue();
}
