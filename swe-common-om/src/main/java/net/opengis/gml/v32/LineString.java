package net.opengis.gml.v32;


/**
 * POJO class for XML type LineStringType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface LineString extends AbstractCurve
{
    
    /**
     * Gets the posList property
     * @return double array contanining all coordinates
     */
    public double[] getPosList();
    
    
    /**
     * Sets the posList property
     * @param posList double array containing all coordinates
     */
    public void setPosList(double[] posList); 
    
    
    /**
     * Checks if posList property is set
     * @return true is set 
     */
    public boolean isSetPosList();
}
