package net.opengis.swe.v20;

import net.opengis.OgcProperty;


/**
 * POJO class for XML type TextType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface Text extends AbstractSimpleComponent
{
    
    
    /**
     * Gets the constraint property
     */
    public AllowedTokens getConstraint();
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the constraint property
     */
    public OgcProperty<AllowedTokens> getConstraintProperty();
    
    
    /**
     * Checks if constraint is set
     */
    public boolean isSetConstraint();
    
    
    /**
     * Sets the constraint property
     */
    public void setConstraint(AllowedTokens constraint);
    
    
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
