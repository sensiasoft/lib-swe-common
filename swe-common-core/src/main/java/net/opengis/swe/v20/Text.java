package net.opengis.swe.v20;


/**
 * POJO class for XML type TextType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface Text extends ScalarComponent, HasConstraints<AllowedTokens>
{
    
    @Override
    public Text copy();
    
    
    /**
     * Gets the value property
     */
    public String getValue();
    
    
    /**
     * Checks if value is set
     */
    public boolean isSetValue();
    
    
    /**
     * Sets the value property
     */
    public void setValue(String value);
}
