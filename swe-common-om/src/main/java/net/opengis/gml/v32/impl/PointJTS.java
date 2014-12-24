package net.opengis.gml.v32.impl;

import java.util.List;
import com.vividsolutions.jts.geom.GeometryFactory;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyList;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.AbstractMetaData;
import net.opengis.gml.v32.Code;
import net.opengis.gml.v32.CodeWithAuthority;
import net.opengis.gml.v32.Point;
import net.opengis.gml.v32.Reference;
import net.opengis.gml.v32.StringOrRef;


/**
 * <p>
 * Implementation of GML Point derived from JTS Point class.
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Dec 23, 2014
 */
public class PointJTS extends com.vividsolutions.jts.geom.Point implements Point
{
    static final long serialVersionUID = 1L;
    AbstractGeometry geom = new AbstractGeometryImpl() { };
    protected double[] pos;
    
    
    public PointJTS(GeometryFactory jtsFactory)
    {
        super(new JTSCoordinatesDoubleArray(), jtsFactory);
    }
    
    
    @Override
    public double[] getPos()
    {
        return pos;
    }
    
    
    @Override
    public boolean isSetPos()
    {
        return (pos != null);
    }
    
    
    @Override
    public void setPos(double[] pos)
    {
        this.pos = pos;
        ((JTSCoordinatesDoubleArray)getCoordinateSequence()).setPosList(pos);
        this.geometryChanged();
    }
    
    
    @Override
    public String getSrsName()
    {
        return geom.getSrsName();
    }


    @Override
    public OgcPropertyList<AbstractMetaData> getMetaDataPropertyList()
    {
        return geom.getMetaDataPropertyList();
    }


    @Override
    public boolean isSetSrsName()
    {
        return geom.isSetSrsName();
    }


    @Override
    public final void setSrsName(String srsName)
    {
        geom.setSrsName(srsName);
        this.setSRID(JTSUtils.getSRIDFromSrsName(srsName));
    }
    
    
    @Override
    public void setSRID(int srid)
    {
        super.setSRID(srid);
        geom.setSrsName(JTSUtils.getSrsNameFromSRID(srid));
    }


    @Override
    public int getNumMetaDataPropertys()
    {
        return geom.getNumMetaDataPropertys();
    }


    @Override
    public int getSrsDimension()
    {
        return geom.getSrsDimension();
    }


    @Override
    public void addMetaDataProperty(AbstractMetaData metaDataProperty)
    {
        geom.addMetaDataProperty(metaDataProperty);
    }


    @Override
    public boolean isSetSrsDimension()
    {
        return geom.isSetSrsDimension();
    }


    @Override
    public StringOrRef getDescription()
    {
        return geom.getDescription();
    }


    @Override
    public void setSrsDimension(int srsDimension)
    {
        geom.setSrsDimension(srsDimension);
    }


    @Override
    public OgcProperty<StringOrRef> getDescriptionProperty()
    {
        return geom.getDescriptionProperty();
    }


    @Override
    public void unSetSrsDimension()
    {
        geom.unSetSrsDimension();
    }


    @Override
    public String[] getAxisLabels()
    {
        return geom.getAxisLabels();
    }


    @Override
    public boolean isSetDescription()
    {
        return geom.isSetDescription();
    }


    @Override
    public boolean isSetAxisLabels()
    {
        return geom.isSetAxisLabels();
    }


    @Override
    public void setDescription(StringOrRef description)
    {
        geom.setDescription(description);
    }


    @Override
    public void setAxisLabels(String[] axisLabels)
    {
        geom.setAxisLabels(axisLabels);
    }


    @Override
    public void setDescription(String description)
    {
        geom.setDescription(description);
    }


    @Override
    public String[] getUomLabels()
    {
        return geom.getUomLabels();
    }


    @Override
    public Reference getDescriptionReference()
    {
        return geom.getDescriptionReference();
    }


    @Override
    public boolean isSetUomLabels()
    {
        return geom.isSetUomLabels();
    }


    @Override
    public boolean isSetDescriptionReference()
    {
        return geom.isSetDescriptionReference();
    }


    @Override
    public void setUomLabels(String[] uomLabels)
    {
        geom.setUomLabels(uomLabels);
    }


    @Override
    public void setDescriptionReference(Reference descriptionReference)
    {
        geom.setDescriptionReference(descriptionReference);
    }


    @Override
    public CodeWithAuthority getIdentifier()
    {
        return geom.getIdentifier();
    }


    @Override
    public String getUniqueIdentifier()
    {
        return geom.getUniqueIdentifier();
    }


    @Override
    public boolean isSetIdentifier()
    {
        return geom.isSetIdentifier();
    }


    @Override
    public void setIdentifier(CodeWithAuthority identifier)
    {
        geom.setIdentifier(identifier);
    }


    @Override
    public void setUniqueIdentifier(String identifier)
    {
        geom.setUniqueIdentifier(identifier);
    }


    @Override
    public List<Code> getNameList()
    {
        return geom.getNameList();
    }


    @Override
    public int getNumNames()
    {
        return geom.getNumNames();
    }


    @Override
    public void addName(Code name)
    {
        geom.addName(name);
    }


    @Override
    public void setName(String name)
    {
        geom.setName(name);
    }


    @Override
    public String getName()
    {
        return geom.getName();
    }


    @Override
    public String getId()
    {
        return geom.getId();
    }


    @Override
    public void setId(String id)
    {
        geom.setId(id);
    }
}
