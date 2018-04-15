/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.swe.v20;

import java.time.OffsetDateTime;
import org.vast.data.DateTimeOrDouble;


/**
 * POJO class for XML type TimeRangeType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface TimeRange extends RangeComponent, HasRefFrames, HasUom, HasConstraints<AllowedTimes>
{
    
    @Override
    public TimeRange copy();
    
    
    /**
     * Gets the value property
     */
    public DateTimeOrDouble[] getValue();
    
    
    /**
     * Sets the value property
     */
    public void setValue(DateTimeOrDouble[] value);
    
    
    /**
     * Gets the referenceTime property
     */
    public OffsetDateTime getReferenceTime();
    
    
    /**
     * Checks if referenceTime is set
     */
    public boolean isSetReferenceTime();
    
    
    /**
     * Sets the referenceTime property
     */
    public void setReferenceTime(OffsetDateTime referenceTime);
    
    
    /**
     * @return true if time is encoded as ISO8601 string
     */
    public boolean isIsoTime();
}
