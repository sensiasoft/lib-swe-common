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
import net.opengis.gml.v32.LineString;
import net.opengis.gml.v32.Reference;


/**
 * <p>
 * Implementation of GML LineString derived from JTS LineString class.
 * </p>
 *
 * @author Alex Robin
 * @since Dec 23, 2014
 */
public class LineStringJTS extends com.vividsolutions.jts.geom.LineString implements LineString
{
    private static final long serialVersionUID = -8843421116255408427L;
    transient AbstractGeometryImpl geom = new AbstractGeometryImpl();
    double[] posList;
    
    
    public LineStringJTS(GeometryFactory jtsFactory, int numDims)
    {
        super(new JTSCoordinatesDoubleArray(numDims), jtsFactory);
        geom.srsDimension = numDims;
    }


    @Override
    public final double[] getPosList()
    {
        return posList;
    }


    @Override
    public final void setPosList(double[] posList)
    {
        this.posList = posList;
        ((JTSCoordinatesDoubleArray)getCoordinateSequence()).setPosList(posList);
        this.geometryChanged();
        geom.envelope = null;
    }    
    
    
    @Override
    public final boolean isSetPosList()
    {
        return (posList != null);
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
        ((JTSCoordinatesDoubleArray)getCoordinateSequence()).setNumDimensions(srsDimension);
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
            geom.envelope = AbstractGeometryImpl.addCoordinatesToEnvelope(geom.envelope, posList, geom.srsDimension);
            
        return geom.envelope;
    }
}
