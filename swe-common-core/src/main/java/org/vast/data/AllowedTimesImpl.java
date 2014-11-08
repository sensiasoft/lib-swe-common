package org.vast.data;

import java.util.ArrayList;
import java.util.List;
import net.opengis.IDateTime;
import net.opengis.HasCopy;
import net.opengis.swe.v20.AllowedTimes;


/**
 * POJO class for XML type AllowedTimesType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class AllowedTimesImpl extends AbstractSWEImpl implements AllowedTimes, HasCopy
{
    static final long serialVersionUID = 1L;
    protected List<IDateTime> valueList = new ArrayList<IDateTime>();
    protected List<IDateTime[]> intervalList = new ArrayList<IDateTime[]>();
    protected Integer significantFigures;
    
    
    public AllowedTimesImpl()
    {
    }
    
    
    public AllowedTimesImpl copy()
    {
        AllowedTimesImpl newObj = new AllowedTimesImpl();
        for (IDateTime val: valueList)
            newObj.valueList.add(val);
        for (IDateTime[] interval: intervalList)
            newObj.intervalList.add(new IDateTime[]{interval[0], interval[1]});
        newObj.significantFigures = significantFigures;
        return newObj;
    }
    
    
    public boolean isValid(IDateTime value)
    {
        for (IDateTime allowedValue: valueList)
            if (allowedValue.equals(value))
                return true;
        
        for (IDateTime[] allowedRange: intervalList)
            if (value.isAfter(allowedRange[0]) && value.isBefore(allowedRange[1]))
                return true;
        
        return false;
    }
    
    
    public String getAssertionMessage()
    {
        StringBuffer msg = new StringBuffer();
        
        if (valueList.size() > 0)
        {
            msg.append("be one of {");
            int i = 0;
            int lastItem = valueList.size() - 1;
            
            for (IDateTime allowedValue: valueList)
            {
                msg.append(Double.toString(allowedValue.getAsDouble()));
                if (i < lastItem)
                    msg.append(", ");
            }
            
            msg.append('}');
        }
        
        if (intervalList.size() > 0)
        {
            if (valueList.size() > 0)
                msg.append(" OR ");
                
            msg.append("be within one of {");
            int i = 0;
            int lastItem = intervalList.size() - 1;
            
            for (IDateTime[] allowedRange: intervalList)
            {
                msg.append('[');
                msg.append(Double.toString(allowedRange[0].getAsDouble()));
                msg.append(' ');
                msg.append(Double.toString(allowedRange[1].getAsDouble()));
                msg.append(']');
                if (i < lastItem)
                    msg.append(", ");
            }
            
            msg.append('}');
        }
              
        return msg.toString();
    }
    
    
    /**
     * Gets the list of value properties
     */
    @Override
    public List<IDateTime> getValueList()
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
    public void addValue(IDateTime value)
    {
        this.valueList.add(value);
    }
    
    
    /**
     * Gets the list of interval properties
     */
    @Override
    public List<IDateTime[]> getIntervalList()
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
    public void addInterval(IDateTime[] interval)
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
