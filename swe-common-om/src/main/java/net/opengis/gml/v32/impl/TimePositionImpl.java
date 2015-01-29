package net.opengis.gml.v32.impl;

import net.opengis.DateTimeDouble;
import net.opengis.IDateTime;
import net.opengis.gml.v32.TimeIndeterminateValue;
import net.opengis.gml.v32.TimePosition;


/**
 * POJO class for XML type TimePositionType(@http://www.opengis.net/gml/3.2).
 *
 * This is a union type. Instances are of one of the following types:
 *     net.opengis.DateTime
 *     java.lang.String
 *     double
 */
public class TimePositionImpl implements TimePosition
{
    static final long serialVersionUID = 1L;
    protected String frame;
    protected String calendarEraName;
    protected TimeIndeterminateValue indeterminatePosition;
    protected IDateTime dateTimeValue;
    protected String textValue;
    
    
    public TimePositionImpl()
    {
    }
    
    
    /**
     * Gets the frame property
     */
    @Override
    public String getFrame()
    {
        return frame;
    }
    
    
    /**
     * Checks if frame is set
     */
    @Override
    public boolean isSetFrame()
    {
        return (frame != null);
    }
    
    
    /**
     * Sets the frame property
     */
    @Override
    public void setFrame(String frame)
    {
        this.frame = frame;
    }
    
    
    /**
     * Gets the calendarEraName property
     */
    @Override
    public String getCalendarEraName()
    {
        return calendarEraName;
    }
    
    
    /**
     * Checks if calendarEraName is set
     */
    @Override
    public boolean isSetCalendarEraName()
    {
        return (calendarEraName != null);
    }
    
    
    /**
     * Sets the calendarEraName property
     */
    @Override
    public void setCalendarEraName(String calendarEraName)
    {
        this.calendarEraName = calendarEraName;
    }
    
    
    /**
     * Gets the indeterminatePosition property
     */
    @Override
    public TimeIndeterminateValue getIndeterminatePosition()
    {
        return indeterminatePosition;
    }
    
    
    /**
     * Checks if indeterminatePosition is set
     */
    @Override
    public boolean isSetIndeterminatePosition()
    {
        return (indeterminatePosition != null);
    }
    
    
    /**
     * Sets the indeterminatePosition property
     */
    @Override
    public void setIndeterminatePosition(TimeIndeterminateValue indeterminatePosition)
    {
        this.indeterminatePosition = indeterminatePosition;
    }
    
    
    @Override
    public IDateTime getDateTimeValue()
    {
        return dateTimeValue;
    }
    
    
    @Override
    public boolean isSetDateTimeValue()
    {
        return (dateTimeValue != null);
    }
    
    
    @Override
    public void setDateTimeValue(IDateTime value)
    {
        this.dateTimeValue = value;        
    }


    @Override
    public double getDecimalValue()
    {
        if (dateTimeValue != null)
            return dateTimeValue.getAsDouble();
        
        return Double.NaN;
    }
    
    
    @Override
    public void setDecimalValue(double value)
    {
        this.dateTimeValue = new DateTimeDouble(value);        
    }


    @Override
    public String getTextValue()
    {
        return textValue;
    }


    @Override
    public boolean isSetTextValue()
    {
        return (textValue != null);
    }
    
    
    @Override
    public void setTextValue(String value)
    {
        this.textValue = value;        
    }


    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof TimePosition))
            return false;
        
        TimePosition other = (TimePosition)obj;
        
        if (getDecimalValue() != other.getDecimalValue())
            return false;
        
        if (getTextValue() != other.getTextValue())
            return false;
        
        return true;
    }
}
