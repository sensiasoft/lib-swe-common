/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32;

import java.io.Serializable;
import net.opengis.IDateTime;



/**
 * POJO class for XML type TimePositionType(@http://www.opengis.net/gml/3.2).
 *
 * This is a union type. Instances are of one of the following types:
 *     net.opengis.DateTime
 *     java.lang.String
 *     double
 */
@SuppressWarnings("javadoc")
public interface TimePosition extends Serializable
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
    public void setDecimalValue(double value);
    
    public String getTextValue();
    public boolean isSetTextValue();
    public void setTextValue(String value);
}
