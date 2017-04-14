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
import java.util.List;
import org.vast.ogc.gml.JTSUtils;
import com.vividsolutions.jts.geom.GeometryFactory;
import net.opengis.OgcPropertyList;
import net.opengis.gml.v32.CodeWithAuthority;
import net.opengis.gml.v32.Envelope;
import net.opengis.gml.v32.Point;
import net.opengis.gml.v32.Reference;


/**
 * <p>
 * Implementation of GML Point derived from JTS Point class.
 * </p>
 *
 * @author Alex Robin
 * @since Dec 23, 2014
 */
public class PointJTS extends com.vividsolutions.jts.geom.Point implements Point
{
    private static final long serialVersionUID = -5151123418510314756L;
    transient AbstractGeometryImpl geom = new AbstractGeometryImpl();
    double[] pos;
    
    
    public PointJTS(GeometryFactory jtsFactory, int numDims)
    {
        super(new JTSCoordinatesDoubleArray(numDims), jtsFactory);
        geom.srsDimension = numDims;
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
        ((JTSCoordinatesDoubleArray)getCoordinateSequence()).numDims = pos.length;
        ((JTSCoordinatesDoubleArray)getCoordinateSequence()).setPosList(pos);
        this.geometryChanged();
        geom.envelope = null;
    }
    
    
    @Override
    public String getSrsName()
    {
        return geom.getSrsName();
    }


    @Override
    public OgcPropertyList<Serializable> getMetaDataPropertyList()
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
        super.setSRID(JTSUtils.getSRIDFromSrsName(srsName));
    }
    
    
    @Override
    public void setSRID(int srid)
    {
        super.setSRID(srid);
        geom.setSrsName(JTSUtils.getSrsNameFromSRID(srid));
    }


    @Override
    public int getSrsDimension()
    {
        return geom.getSrsDimension();
    }


    @Override
    public boolean isSetSrsDimension()
    {
        return geom.isSetSrsDimension();
    }


    @Override
    public String getDescription()
    {
        return geom.getDescription();
    }


    @Override
    public void setSrsDimension(int srsDimension)
    {
        geom.setSrsDimension(srsDimension);
        ((JTSCoordinatesDoubleArray)getCoordinateSequence()).setNumDimensions(srsDimension);
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
    public List<CodeWithAuthority> getNameList()
    {
        return geom.getNameList();
    }


    @Override
    public int getNumNames()
    {
        return geom.getNumNames();
    }


    @Override
    public void addName(CodeWithAuthority name)
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
    
    
    @Override
    public Envelope getGeomEnvelope()
    {
        if (geom.envelope == null)
            geom.envelope = AbstractGeometryImpl.addCoordinatesToEnvelope(geom.envelope, pos, geom.srsDimension);
            
        return geom.envelope;
    }
}
