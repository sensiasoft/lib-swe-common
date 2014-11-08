package net.opengis.swe.v20;

import net.opengis.IDateTime;
import net.opengis.OgcProperty;


/**
 * POJO class for XML type TimeRangeType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface TimeRange extends AbstractSimpleComponent
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
    public AllowedTimes getConstraint();
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the constraint property
     */
    public OgcProperty<AllowedTimes> getConstraintProperty();
    
    
    /**
     * Checks if constraint is set
     */
    public boolean isSetConstraint();
    
    
    /**
     * Sets the constraint property
     */
    public void setConstraint(AllowedTimes constraint);
    
    
    /**
     * Gets the value property
     */
    public IDateTime[] getValue();
    
    
    /**
     * Checks if value is set
     */
    public boolean isSetValue();
    
    
    /**
     * Sets the value property
     */
    public void setValue(IDateTime[] value);
    
    
    /**
     * Gets the referenceTime property
     */
    public IDateTime getReferenceTime();
    
    
    /**
     * Checks if referenceTime is set
     */
    public boolean isSetReferenceTime();
    
    
    /**
     * Sets the referenceTime property
     */
    public void setReferenceTime(IDateTime referenceTime);
    
    
    /**
     * Gets the localFrame property
     */
    public String getLocalFrame();
    
    
    /**
     * Checks if localFrame is set
     */
    public boolean isSetLocalFrame();
    
    
    /**
     * Sets the localFrame property
     */
    public void setLocalFrame(String localFrame);
}
