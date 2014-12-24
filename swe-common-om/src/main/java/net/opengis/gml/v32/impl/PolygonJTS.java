package net.opengis.gml.v32.impl;

import java.util.ArrayList;
import java.util.List;
import com.vividsolutions.jts.geom.GeometryFactory;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyList;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.AbstractMetaData;
import net.opengis.gml.v32.AbstractRing;
import net.opengis.gml.v32.Code;
import net.opengis.gml.v32.CodeWithAuthority;
import net.opengis.gml.v32.Polygon;
import net.opengis.gml.v32.Reference;
import net.opengis.gml.v32.StringOrRef;


/**
 * <p>
 * Implementation of GML Polygon derived from JTS Polygon class.
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Dec 23, 2014
 */
public class PolygonJTS extends com.vividsolutions.jts.geom.Polygon implements Polygon
{
    static final long serialVersionUID = 1L;
    AbstractGeometry geom = new AbstractGeometryImpl() { };
    protected AbstractRing exterior;
    protected List<AbstractRing> interiorList = new ArrayList<AbstractRing>();
    
    
    public PolygonJTS(GeometryFactory jtsFactory)
    {
        super(null, null, jtsFactory);
    }
    
    
    @Override
    public final AbstractRing getExterior()
    {
        return exterior;
    }
    
    
    @Override
    public final boolean isSetExterior()
    {
        return (exterior != null);
    }
    
    
    @Override
    public final void setExterior(AbstractRing exterior)
    {
        this.exterior = exterior;
        this.shell = (LinearRingJTS)exterior;
        this.geometryChanged();
    }
    
    
    @Override
    public final List<AbstractRing> getInteriorList()
    {
        return interiorList;
    }
    
    
    @Override
    public final int getNumInteriors()
    {
        if (interiorList == null)
            return 0;
        return interiorList.size();
    }
    
    
    @Override
    public final void addInterior(AbstractRing interior)
    {
        this.interiorList.add(interior);
        this.holes = this.interiorList.toArray(this.holes);
        this.geometryChanged();
    }


    @Override
    public final String getSrsName()
    {
        return geom.getSrsName();
    }


    @Override
    public final OgcPropertyList<AbstractMetaData> getMetaDataPropertyList()
    {
        return geom.getMetaDataPropertyList();
    }


    @Override
    public final boolean isSetSrsName()
    {
        return geom.isSetSrsName();
    }


    @Override
    public final void setSrsName(String srsName)
    {
        geom.setSrsName(srsName);
        super.setSRID(JTSUtils.getSRIDFromSrsName(srsName));
    }
    
    
    @Override
    public void setSRID(int srid)
    {
        super.setSRID(srid);
        geom.setSrsName(JTSUtils.getSrsNameFromSRID(srid));
    }


    @Override
    public final int getNumMetaDataPropertys()
    {
        return geom.getNumMetaDataPropertys();
    }


    @Override
    public final int getSrsDimension()
    {
        return geom.getSrsDimension();
    }


    @Override
    public final void addMetaDataProperty(AbstractMetaData metaDataProperty)
    {
        geom.addMetaDataProperty(metaDataProperty);
    }


    @Override
    public final boolean isSetSrsDimension()
    {
        return geom.isSetSrsDimension();
    }


    @Override
    public final StringOrRef getDescription()
    {
        return geom.getDescription();
    }


    @Override
    public final void setSrsDimension(int srsDimension)
    {
        geom.setSrsDimension(srsDimension);
    }


    @Override
    public final OgcProperty<StringOrRef> getDescriptionProperty()
    {
        return geom.getDescriptionProperty();
    }


    @Override
    public final void unSetSrsDimension()
    {
        geom.unSetSrsDimension();
    }


    @Override
    public final String[] getAxisLabels()
    {
        return geom.getAxisLabels();
    }


    @Override
    public final boolean isSetDescription()
    {
        return geom.isSetDescription();
    }


    @Override
    public final boolean isSetAxisLabels()
    {
        return geom.isSetAxisLabels();
    }


    @Override
    public final void setDescription(StringOrRef description)
    {
        geom.setDescription(description);
    }


    @Override
    public final void setAxisLabels(String[] axisLabels)
    {
        geom.setAxisLabels(axisLabels);
    }


    @Override
    public final void setDescription(String description)
    {
        geom.setDescription(description);
    }


    @Override
    public final String[] getUomLabels()
    {
        return geom.getUomLabels();
    }


    @Override
    public final Reference getDescriptionReference()
    {
        return geom.getDescriptionReference();
    }


    @Override
    public final boolean isSetUomLabels()
    {
        return geom.isSetUomLabels();
    }


    @Override
    public final boolean isSetDescriptionReference()
    {
        return geom.isSetDescriptionReference();
    }


    @Override
    public final void setUomLabels(String[] uomLabels)
    {
        geom.setUomLabels(uomLabels);
    }


    @Override
    public final void setDescriptionReference(Reference descriptionReference)
    {
        geom.setDescriptionReference(descriptionReference);
    }


    @Override
    public final CodeWithAuthority getIdentifier()
    {
        return geom.getIdentifier();
    }


    @Override
    public final String getUniqueIdentifier()
    {
        return geom.getUniqueIdentifier();
    }


    @Override
    public final boolean isSetIdentifier()
    {
        return geom.isSetIdentifier();
    }


    @Override
    public final void setIdentifier(CodeWithAuthority identifier)
    {
        geom.setIdentifier(identifier);
    }


    @Override
    public final void setUniqueIdentifier(String identifier)
    {
        geom.setUniqueIdentifier(identifier);
    }


    @Override
    public final List<Code> getNameList()
    {
        return geom.getNameList();
    }


    @Override
    public final int getNumNames()
    {
        return geom.getNumNames();
    }


    @Override
    public final void addName(Code name)
    {
        geom.addName(name);
    }


    @Override
    public final void setName(String name)
    {
        geom.setName(name);
    }


    @Override
    public final String getName()
    {
        return geom.getName();
    }


    @Override
    public final String getId()
    {
        return geom.getId();
    }


    @Override
    public final void setId(String id)
    {
        geom.setId(id);
    }
}
