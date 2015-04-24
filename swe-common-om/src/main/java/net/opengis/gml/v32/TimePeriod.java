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

import net.opengis.OgcProperty;


/**
 * POJO class for XML type TimePeriodType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface TimePeriod extends AbstractTimeGeometricPrimitive
{
    
    /**
     * Gets the beginPosition property
     */
    public TimePosition getBeginPosition();
    
    
    /**
     * Checks if beginPosition is set
     */
    public boolean isSetBeginPosition();
    
    
    /**
     * Sets the beginPosition property
     */
    public void setBeginPosition(TimePosition beginPosition);
    
    
    /**
     * Gets the begin property
     */
    public TimeInstant getBegin();
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the begin property
     */
    public OgcProperty<TimeInstant> getBeginProperty();
    
    
    /**
     * Checks if begin is set
     */
    public boolean isSetBegin();
    
    
    /**
     * Sets the begin property
     */
    public void setBegin(TimeInstant begin);
    
    
    /**
     * Gets the endPosition property
     */
    public TimePosition getEndPosition();
    
    
    /**
     * Checks if endPosition is set
     */
    public boolean isSetEndPosition();
    
    
    /**
     * Sets the endPosition property
     */
    public void setEndPosition(TimePosition endPosition);
    
    
    /**
     * Gets the end property
     */
    public TimeInstant getEnd();
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the end property
     */
    public OgcProperty<TimeInstant> getEndProperty();
    
    
    /**
     * Checks if end is set
     */
    public boolean isSetEnd();
    
    
    /**
     * Sets the end property
     */
    public void setEnd(TimeInstant end);
    
    
    /**
     * Gets the duration property
     */
    public double getDuration();
    
    
    /**
     * Checks if duration is set
     */
    public boolean isSetDuration();
    
    
    /**
     * Sets the duration property
     */
    public void setDuration(double duration);
    
    
    /**
     * Gets the timeInterval property
     */
    public TimeIntervalLength getTimeInterval();
    
    
    /**
     * Checks if timeInterval is set
     */
    public boolean isSetTimeInterval();
    
    
    /**
     * Sets the timeInterval property
     */
    public void setTimeInterval(TimeIntervalLength timeInterval);
    
    
    /**
     * Sets the time interval in seconds
     */
    public void setTimeInterval(double seconds);
    
    
    /**
     * Sets the time interval in the specified unit
     * @param value
     * @param unit
     */
    public void setTimeInterval(double value, TimeUnit unit);
}
