package net.opengis.gml.v32.impl;

import net.opengis.gml.v32.Point;


/**
 * POJO class for XML type PointType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public class PointImpl extends AbstractGeometryImpl implements Point
{
    static final long serialVersionUID = 1L;
    protected double[] pos;
    
    
    public PointImpl()
    {
    }
    
    
    /**
     * Gets the pos property
     */
    @Override
    public double[] getPos()
    {
        return pos;
    }
    
    
    /**
     * Checks if pos is set
     */
    @Override
    public boolean isSetPos()
    {
        return (pos != null);
    }
    
    
    /**
     * Sets the pos property
     */
    @Override
    public void setPos(double[] pos)
    {
        this.pos = pos;
    }
}
