package net.opengis.swe.v20;

import net.opengis.IDateTime;


/**
 * POJO class for XML type TimeRangeType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface TimeRange extends RangeComponent, HasRefFrames, HasUom, HasConstraints<AllowedTimes>
{
    
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
}
