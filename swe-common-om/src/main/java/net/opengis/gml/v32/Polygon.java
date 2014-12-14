package net.opengis.gml.v32;

import java.util.List;


/**
 * POJO class for XML type PolygonType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface Polygon extends AbstractSurface
{
    
    
    /**
     * Gets the exterior property
     */
    public AbstractRing getExterior();
    
    
    /**
     * Checks if exterior is set
     */
    public boolean isSetExterior();
    
    
    /**
     * Sets the exterior property
     */
    public void setExterior(AbstractRing exterior);
    
    
    /**
     * Gets the list of interior properties
     */
    public List<AbstractRing> getInteriorList();
    
    
    /**
     * Returns number of interior properties
     */
    public int getNumInteriors();
    
    
    /**
     * Adds a new interior property
     */
    public void addInterior(AbstractRing interior);
}
