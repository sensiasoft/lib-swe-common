package net.opengis.gml.v32.impl;

import net.opengis.OgcPropertyList;
import net.opengis.gml.v32.AbstractTimePrimitive;


/**
 * POJO class for XML type AbstractTimePrimitiveType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public abstract class AbstractTimePrimitiveImpl extends AbstractGMLImpl implements AbstractTimePrimitive
{
    static final long serialVersionUID = 1L;
    protected OgcPropertyList<AbstractTimePrimitive> relatedTimeList = new OgcPropertyList<AbstractTimePrimitive>();
    
    
    public AbstractTimePrimitiveImpl()
    {
    }
    
    
    /**
     * Gets the list of relatedTime properties
     */
    @Override
    public OgcPropertyList<AbstractTimePrimitive> getRelatedTimeList()
    {
        return relatedTimeList;
    }
    
    
    /**
     * Returns number of relatedTime properties
     */
    @Override
    public int getNumRelatedTimes()
    {
        if (relatedTimeList == null)
            return 0;
        return relatedTimeList.size();
    }
    
    
    /**
     * Adds a new relatedTime property
     */
    @Override
    public void addRelatedTime(AbstractTimePrimitive relatedTime)
    {
        this.relatedTimeList.add(relatedTime);
    }
}
