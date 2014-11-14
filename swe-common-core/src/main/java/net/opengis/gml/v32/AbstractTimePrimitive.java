package net.opengis.gml.v32;

import net.opengis.OgcPropertyList;


/**
 * POJO class for XML type AbstractTimePrimitiveType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface AbstractTimePrimitive extends AbstractGML
{
    
    
    /**
     * Gets the list of relatedTime properties
     */
    public OgcPropertyList<AbstractTimePrimitive> getRelatedTimeList();
    
    
    /**
     * Returns number of relatedTime properties
     */
    public int getNumRelatedTimes();
    
    
    /**
     * Adds a new relatedTime property
     */
    public void addRelatedTime(AbstractTimePrimitive relatedTime);
}
