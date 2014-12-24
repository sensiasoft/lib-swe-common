package net.opengis.gml.v32.impl;

import net.opengis.gml.v32.TimeInstant;
import net.opengis.gml.v32.TimePosition;


/**
 * POJO class for XML type TimeInstantType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public class TimeInstantImpl extends AbstractTimeGeometricPrimitiveImpl implements TimeInstant
{
    static final long serialVersionUID = 1L;
    protected TimePosition timePosition;
    
    
    public TimeInstantImpl()
    {
    }
    
    
    /**
     * Gets the timePosition property
     */
    @Override
    public TimePosition getTimePosition()
    {
        return timePosition;
    }
    
    
    /**
     * Sets the timePosition property
     */
    @Override
    public void setTimePosition(TimePosition timePosition)
    {
        this.timePosition = timePosition;
    }


    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof TimeInstant))
            return false;
        
        if (!timePosition.equals(((TimeInstant)obj).getTimePosition()))
            return false;
        
        return true;
    }
}
