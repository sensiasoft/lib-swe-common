/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import java.util.ArrayList;
import java.util.List;
import net.opengis.swe.v20.AllowedTimes;


/**
 * POJO class for XML type AllowedTimesType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class AllowedTimesImpl extends AbstractSWEImpl implements AllowedTimes
{
    private static final long serialVersionUID = -6032939377548399837L;
    protected ArrayList<DateTimeOrDouble> valueList = new ArrayList<DateTimeOrDouble>();
    protected ArrayList<DateTimeOrDouble[]> intervalList = new ArrayList<DateTimeOrDouble[]>();
    protected Integer significantFigures;
    
    
    public AllowedTimesImpl()
    {
    }
    
    
    @Override
    public AllowedTimesImpl copy()
    {
        AllowedTimesImpl newObj = new AllowedTimesImpl();
        for (DateTimeOrDouble val: valueList)
            newObj.valueList.add(val);
        for (DateTimeOrDouble[] interval: intervalList)
            newObj.intervalList.add(new DateTimeOrDouble[]{interval[0], interval[1]});
        newObj.significantFigures = significantFigures;
        return newObj;
    }
    
    
    public boolean isValid(DateTimeOrDouble value)
    {
        for (DateTimeOrDouble allowedValue: valueList)
            if (allowedValue.equals(value))
                return true;
        
        for (DateTimeOrDouble[] allowedRange: intervalList)
            if (value.isAfter(allowedRange[0]) && value.isBefore(allowedRange[1]))
                return true;
        
        return false;
    }
    
    
    public String getAssertionMessage()
    {
        StringBuffer msg = new StringBuffer();
        msg.append("It should ");
        
        if (!valueList.isEmpty())
        {
            msg.append("be one of {");
            int i = 0;
            
            for (DateTimeOrDouble allowedValue: valueList)
            {
                if (i++ > 0)
                    msg.append(", ");    
                msg.append(allowedValue.toString());
            }
            
            msg.append('}');
        }
        
        if (!intervalList.isEmpty())
        {
            if (!valueList.isEmpty())
                msg.append(" OR ");
                
            msg.append("be within one of {");
            int i = 0;
            
            for (DateTimeOrDouble[] allowedRange: intervalList)
            {
                if (i++ > 0)
                    msg.append(", ");    
                msg.append('[');
                msg.append(allowedRange[0].toString());
                msg.append('/');
                msg.append(allowedRange[1].toString());
                msg.append(']');
            }
            
            msg.append('}');
        }
              
        return msg.toString();
    }
    
    
    /**
     * Gets the list of value properties
     */
    @Override
    public List<DateTimeOrDouble> getValueList()
    {
        return valueList;
    }
    
    
    /**
     * Returns number of value properties
     */
    @Override
    public int getNumValues()
    {
        if (valueList == null)
            return 0;
        return valueList.size();
    }
    
    
    /**
     * Adds a new value property
     */
    @Override
    public void addValue(DateTimeOrDouble value)
    {
        this.valueList.add(value);
    }
    
    
    /**
     * Gets the list of interval properties
     */
    @Override
    public List<DateTimeOrDouble[]> getIntervalList()
    {
        return intervalList;
    }
    
    
    /**
     * Returns number of interval properties
     */
    @Override
    public int getNumIntervals()
    {
        if (intervalList == null)
            return 0;
        return intervalList.size();
    }
    
    
    /**
     * Adds a new interval property
     */
    @Override
    public void addInterval(DateTimeOrDouble[] interval)
    {
        this.intervalList.add(interval);
    }
    
    
    /**
     * Gets the significantFigures property
     */
    @Override
    public int getSignificantFigures()
    {
        return significantFigures;
    }
    
    
    /**
     * Checks if significantFigures is set
     */
    @Override
    public boolean isSetSignificantFigures()
    {
        return (significantFigures != null);
    }
    
    
    /**
     * Sets the significantFigures property
     */
    @Override
    public void setSignificantFigures(int significantFigures)
    {
        this.significantFigures = significantFigures;
    }
    
    
    /**
     * Unsets the significantFigures property
     */
    @Override
    public void unSetSignificantFigures()
    {
        this.significantFigures = null;
    }
}
