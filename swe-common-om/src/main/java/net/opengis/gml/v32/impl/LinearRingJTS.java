package net.opengis.gml.v32.impl;

import com.vividsolutions.jts.geom.GeometryFactory;
import net.opengis.gml.v32.LinearRing;


/**
 * <p>
 * Implementation of GML LinearRing derived from JTS LinearRing class.
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Dec 23, 2014
 */
public class LinearRingJTS extends com.vividsolutions.jts.geom.LinearRing implements LinearRing
{
    static final long serialVersionUID = 1L;
    protected double[] posList;
    
    
    public LinearRingJTS(GeometryFactory jtsFactory)
    {
        super(new JTSCoordinatesDoubleArray(), jtsFactory);
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
        ((JTSCoordinatesDoubleArray)getCoordinateSequence()).setPosList(posList);
        this.geometryChanged();
    }    
    
    
    @Override
    public boolean isSetPosList()
    {
        return (posList != null);
    }
    
}
