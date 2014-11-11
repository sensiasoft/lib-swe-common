package net.opengis.swe.v20;

import org.vast.unit.Unit;



/**
 * POJO class for XML type UnitReference(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface UnitReference extends net.opengis.OgcProperty<Unit>
{
    
    /**
     * Gets the code property
     */
    public String getCode();
    
    
    /**
     * Checks if code is set
     */
    public boolean isSetCode();
    
    
    /**
     * Sets the code property
     */
    public void setCode(String code);
}
