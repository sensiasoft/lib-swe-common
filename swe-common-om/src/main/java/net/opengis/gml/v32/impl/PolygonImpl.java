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

import java.util.ArrayList;
import java.util.List;
import net.opengis.gml.v32.Envelope;
import net.opengis.gml.v32.LinearRing;
import net.opengis.gml.v32.Polygon;


/**
 * POJO class for XML type PolygonType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public class PolygonImpl extends AbstractGeometryImpl implements Polygon
{
    private static final long serialVersionUID = -3961633226145543311L;
    protected LinearRing exterior;
    protected ArrayList<LinearRing> interiorList = new ArrayList<LinearRing>();
    
    
    @SuppressWarnings("unused")
    private PolygonImpl()
    {
    }
    
    
    public PolygonImpl(int numDims)
    {
        this.srsDimension = numDims;
    }
    
    
    /**
     * Gets the exterior property
     */
    @Override
    public LinearRing getExterior()
    {
        return exterior;
    }
    
    
    /**
     * Checks if exterior is set
     */
    @Override
    public boolean isSetExterior()
    {
        return (exterior != null);
    }
    
    
    /**
     * Sets the exterior property
     */
    @Override
    public void setExterior(LinearRing exterior)
    {
        this.exterior = exterior;
        this.envelope = null;
    }
    
    
    /**
     * Gets the list of interior properties
     */
    @Override
    public List<LinearRing> getInteriorList()
    {
        return interiorList;
    }
    
    
    /**
     * Returns number of interior properties
     */
    @Override
    public int getNumInteriors()
    {
        if (interiorList == null)
            return 0;
        return interiorList.size();
    }
    
    
    /**
     * Adds a new interior property
     */
    @Override
    public void addInterior(LinearRing interior)
    {
        this.interiorList.add(interior);
        this.envelope = null;
    }
    
    
    @Override
    public Envelope getGeomEnvelope()
    {
        if (envelope == null)
        {
            envelope = addCoordinatesToEnvelope(envelope, exterior.getPosList(), srsDimension);
            for (LinearRing interior: interiorList)
                addCoordinatesToEnvelope(envelope, interior.getPosList(), srsDimension);
        }
        
        return envelope;
    }
}
