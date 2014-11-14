package net.opengis.gml.v32;



/**
 * POJO class for XML type PointType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface Point extends AbstractGeometry
{
        
    /**
     * Gets the pos property
     */
    public double[] getPos();
    
    
    /**
     * Checks if pos is set
     */
    public boolean isSetPos();
    
    
    /**
     * Sets the pos property
     */
    public void setPos(double[] coords);
}
