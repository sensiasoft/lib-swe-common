package net.opengis.gml.v32.impl;

import net.opengis.gml.v32.LinearRing;


/**
 * POJO class for XML type LinearRingType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public class LinearRingImpl extends AbstractRingImpl implements LinearRing
{
    static final long serialVersionUID = 1L;
    protected double[] posList;
    
    
    public LinearRingImpl()
    {
    }


    @Override
    public double[] getPosList()
    {
        return posList;
    }


    @Override
    public void setPosList(double[] posList)
    {
        this.posList = posList;        
    }    
    
    
    @Override
    public boolean isSetPosList()
    {
        return (posList != null);
    }
    
}
