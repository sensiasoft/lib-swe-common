/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.vast.ogc.gml.JTSUtils;
import com.vividsolutions.jts.geom.GeometryFactory;
import net.opengis.OgcPropertyList;
import net.opengis.gml.v32.CodeWithAuthority;
import net.opengis.gml.v32.Envelope;
import net.opengis.gml.v32.LinearRing;
import net.opengis.gml.v32.Polygon;
import net.opengis.gml.v32.Reference;


/**
 * <p>
 * Implementation of GML Polygon derived from JTS Polygon class.
 * </p>
 *
 * @author Alex Robin
 * @since Dec 23, 2014
 */
public class PolygonJTS extends com.vividsolutions.jts.geom.Polygon implements Polygon
{
    private static final long serialVersionUID = 3200774256059210125L;
    transient AbstractGeometryImpl geom = new AbstractGeometryImpl();
    transient LinearRing exterior;
    
    @SuppressWarnings("serial")
    transient List<LinearRing> interiorList = new ArrayList<LinearRing>() {
        @Override
        public boolean add(LinearRing interior)
        {
            LinearRingJTS interiorJTS = (LinearRingJTS)interior;
            ((JTSCoordinatesDoubleArray)interiorJTS.getCoordinateSequence()).setNumDimensions(geom.srsDimension);
            return super.add(interior);
        }        
    };
    
    
    public PolygonJTS(GeometryFactory jtsFactory, int numDims)
    {
        super(null, null, jtsFactory);
        geom.srsDimension = numDims;
    }
    
    
    @Override
    public final LinearRing getExterior()
    {
        return exterior;
    }
    
    
    @Override
    public final boolean isSetExterior()
    {
        return (exterior != null);
    }
    
    
    @Override
    public final void setExterior(LinearRing exterior)
    {
        this.exterior = exterior;
        this.shell = (LinearRingJTS)exterior;
        this.geometryChanged();
        geom.envelope = null;
        
        LinearRingJTS exteriorJTS = (LinearRingJTS)exterior;
        ((JTSCoordinatesDoubleArray)exteriorJTS.getCoordinateSequence()).setNumDimensions(geom.srsDimension);
    }
    
    
    @Override
    public final List<LinearRing> getInteriorList()
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
    public final void addInterior(LinearRing interior)
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
    public final OgcPropertyList<Serializable> getMetaDataPropertyList()
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
    public final int getSrsDimension()
    {
        return geom.getSrsDimension();
    }


    @Override
    public final boolean isSetSrsDimension()
    {
        return geom.isSetSrsDimension();
    }


    @Override
    public final String getDescription()
    {
        return geom.getDescription();
    }


    @Override
    public final void setSrsDimension(int srsDimension)
    {
        geom.setSrsDimension(srsDimension);
        
        if (exterior != null)
        {
            LinearRingJTS exteriorJTS = (LinearRingJTS)exterior;
            ((JTSCoordinatesDoubleArray)exteriorJTS.getCoordinateSequence()).setNumDimensions(srsDimension);
        }
        
        for (LinearRing interior: interiorList)
        {
            LinearRingJTS interiorJTS = (LinearRingJTS)interior;
            ((JTSCoordinatesDoubleArray)interiorJTS.getCoordinateSequence()).setNumDimensions(srsDimension);
        }
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
    public final List<CodeWithAuthority> getNameList()
    {
        return geom.getNameList();
    }


    @Override
    public final int getNumNames()
    {
        return geom.getNumNames();
    }


    @Override
    public final void addName(CodeWithAuthority name)
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
    
    
    @Override
    public Envelope getGeomEnvelope()
    {
        if (geom.envelope == null)
        {
            int nDims = geom.srsDimension;
            geom.envelope = AbstractGeometryImpl.addCoordinatesToEnvelope(geom.envelope, exterior.getPosList(), nDims);
            for (LinearRing interior: interiorList)
                AbstractGeometryImpl.addCoordinatesToEnvelope(geom.envelope, interior.getPosList(), nDims);
        }
        
        return geom.envelope;
    }
}
