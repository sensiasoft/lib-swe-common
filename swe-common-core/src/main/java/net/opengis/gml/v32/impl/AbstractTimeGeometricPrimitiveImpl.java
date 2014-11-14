package net.opengis.gml.v32.impl;

import net.opengis.gml.v32.AbstractTimeGeometricPrimitive;


/**
 * POJO class for XML type AbstractTimeGeometricPrimitiveType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public abstract class AbstractTimeGeometricPrimitiveImpl extends AbstractTimePrimitiveImpl implements AbstractTimeGeometricPrimitive
{
    static final long serialVersionUID = 1L;
    protected String frame;
    
    
    public AbstractTimeGeometricPrimitiveImpl()
    {
    }
    
    
    /**
     * Gets the frame property
     */
    @Override
    public String getFrame()
    {
        return frame;
    }
    
    
    /**
     * Checks if frame is set
     */
    @Override
    public boolean isSetFrame()
    {
        return (frame != null);
    }
    
    
    /**
     * Sets the frame property
     */
    @Override
    public void setFrame(String frame)
    {
        this.frame = frame;
    }
}
