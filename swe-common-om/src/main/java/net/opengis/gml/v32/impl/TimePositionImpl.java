/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32.impl;

import java.util.Objects;
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
    private static final long serialVersionUID = 3817504721461988594L;
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
        return obj instanceof TimePosition &&
               Objects.equals(getFrame(), ((TimePosition)obj).getFrame()) &&
               Objects.equals(getCalendarEraName(), ((TimePosition)obj).getCalendarEraName()) &&
               Objects.equals(getDecimalValue(), ((TimePosition)obj).getDecimalValue()) &&
               Objects.equals(getTextValue(), ((TimePosition)obj).getTextValue()) &&
               Objects.equals(getIndeterminatePosition(), ((TimePosition)obj).getIndeterminatePosition());
    }
    
    
    @Override
    public int hashCode()
    {
        return Objects.hash(getFrame(),
                            getCalendarEraName(),
                            getDecimalValue(),
                            getTextValue(),
                            getIndeterminatePosition());
    }
}
