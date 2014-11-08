package net.opengis.swe.v20;

import net.opengis.OgcProperty;


/**
 * POJO class for XML type QuantityType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface Quantity extends AbstractSimpleComponent
{
    
    
    /**
     * Gets the uom property
     */
    public UnitReference getUom();
    
    
    /**
     * Sets the uom property
     */
    public void setUom(UnitReference uom);
    
    
    /**
     * Gets the constraint property
     */
    public AllowedValues getConstraint();
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the constraint property
     */
    public OgcProperty<AllowedValues> getConstraintProperty();
    
    
    /**
     * Checks if constraint is set
     */
    public boolean isSetConstraint();
    
    
    /**
     * Sets the constraint property
     */
    public void setConstraint(AllowedValues constraint);
    
    
    /**
     * Gets the value property
     */
    public double getValue();
    
    
    /**
     * Checks if value is set
     */
    public boolean isSetValue();
    
    
    /**
     * Sets the value property
     */
    public void setValue(double value);
    
    
    /**
     * Unsets the value property
     */
    public void unSetValue();
}
