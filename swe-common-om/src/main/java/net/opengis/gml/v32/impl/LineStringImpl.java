package net.opengis.gml.v32.impl;

import net.opengis.gml.v32.LineString;


/**
 * POJO class for XML type LineStringType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public class LineStringImpl extends AbstractCurveImpl implements LineString
{
    static final long serialVersionUID = 1L;
    protected double[] posList;
    
    
    public LineStringImpl()
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
