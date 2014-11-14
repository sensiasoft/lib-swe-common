package net.opengis.gml.v32;

import net.opengis.IDateTime;



/**
 * POJO class for XML type TimePositionType(@http://www.opengis.net/gml/3.2).
 *
 * This is a union type. Instances are of one of the following types:
 *     net.opengis.DateTime
 *     short
 *     short
 *     net.opengis.DateTime
 *     net.opengis.DateTime
 *     java.lang.String
 *     double
 */
public interface TimePosition
{
    
    
    /**
     * Gets the frame property
     */
    public String getFrame();
    
    
    /**
     * Checks if frame is set
     */
    public boolean isSetFrame();
    
    
    /**
     * Sets the frame property
     */
    public void setFrame(String frame);
    
    
    /**
     * Gets the calendarEraName property
     */
    public String getCalendarEraName();
    
    
    /**
     * Checks if calendarEraName is set
     */
    public boolean isSetCalendarEraName();
    
    
    /**
     * Sets the calendarEraName property
     */
    public void setCalendarEraName(String calendarEraName);
    
    
    /**
     * Gets the indeterminatePosition property
     */
    public TimeIndeterminateValue getIndeterminatePosition();
    
    
    /**
     * Checks if indeterminatePosition is set
     */
    public boolean isSetIndeterminatePosition();
    
    
    /**
     * Sets the indeterminatePosition property
     */
    public void setIndeterminatePosition(TimeIndeterminateValue indeterminatePosition);
    
    
    public IDateTime getDateTimeValue();
    public boolean isSetDateTimeValue();
    public void setDateTimeValue(IDateTime value);
    
    public double getDecimalValue();
    public boolean isSetDecimalValue();
    public void setDecimalValue(double value);
    
    public String getTextValue();
    public boolean isSetTextValue();
    public void setTextValue(String value);
}
