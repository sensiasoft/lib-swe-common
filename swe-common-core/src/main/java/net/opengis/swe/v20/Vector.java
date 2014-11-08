package net.opengis.swe.v20;

import net.opengis.OgcPropertyList;


/**
 * POJO class for XML type VectorType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface Vector extends AbstractDataComponent, HasRefFrames
{
    
    /**
     * Gets the list of coordinate properties
     */
    public OgcPropertyList<AbstractSimpleComponent> getCoordinateList();
    
    
    /**
     * Returns number of coordinate properties
     */
    public int getNumCoordinates();
    
    
    /**
     * Gets the coordinate property with the given name
     */
    public AbstractSimpleComponent getCoordinate(String name);
    
    
    /**
     * Adds a new coordinateAsCount property
     */
    public void addCoordinateAsCount(String name, Count coordinate);
    
    
    /**
     * Adds a new coordinateAsQuantity property
     */
    public void addCoordinateAsQuantity(String name, Quantity coordinate);
    
    
    /**
     * Adds a new coordinateAsTime property
     */
    public void addCoordinateAsTime(String name, Time coordinate);
}
