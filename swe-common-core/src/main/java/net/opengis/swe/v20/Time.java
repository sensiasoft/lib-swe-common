package net.opengis.swe.v20;

import net.opengis.IDateTime;


/**
 * POJO class for XML type TimeType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface Time extends ScalarComponent, HasRefFrames, HasUom, HasConstraints<AllowedTimes>
{
    public static final String ISO_TIME_UNIT = "http://www.opengis.net/def/uom/ISO-8601/0/Gregorian";
    
    
    @Override
    public Time copy();
    
    
    /**
     * Gets the value property
     */
    public IDateTime getValue();
    
    
    /**
     * Checks if value is set
     */
    public boolean isSetValue();
    
    
    /**
     * Sets the value property
     */
    public void setValue(IDateTime value);
    
    
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
}
