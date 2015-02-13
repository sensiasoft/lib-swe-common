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

import java.util.List;
import net.opengis.IDateTime;


/**
 * POJO class for XML type AllowedTimesType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface AllowedTimes extends AbstractSWE, DataConstraint
{
    
    
    /**
     * Gets the list of value properties
     */
    public List<IDateTime> getValueList();
    
    
    /**
     * Returns number of value properties
     */
    public int getNumValues();
    
    
    /**
     * Adds a new value property
     */
    public void addValue(IDateTime value);
    
    
    /**
     * Gets the list of interval properties
     */
    public List<IDateTime[]> getIntervalList();
    
    
    /**
     * Returns number of interval properties
     */
    public int getNumIntervals();
    
    
    /**
     * Adds a new interval property
     */
    public void addInterval(IDateTime[] interval);
    
    
    /**
     * Gets the significantFigures property
     */
    public int getSignificantFigures();
    
    
    /**
     * Checks if significantFigures is set
     */
    public boolean isSetSignificantFigures();
    
    
    /**
     * Sets the significantFigures property
     */
    public void setSignificantFigures(int significantFigures);
    
    
    /**
     * Unsets the significantFigures property
     */
    public void unSetSignificantFigures();
}
