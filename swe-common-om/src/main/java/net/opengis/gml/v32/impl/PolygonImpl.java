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
import net.opengis.gml.v32.AbstractRing;
import net.opengis.gml.v32.Polygon;


/**
 * POJO class for XML type PolygonType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public class PolygonImpl extends AbstractGeometryImpl implements Polygon
{
    static final long serialVersionUID = 1L;
    protected AbstractRing exterior;
    protected List<AbstractRing> interiorList = new ArrayList<AbstractRing>();
    
    
    public PolygonImpl()
    {
    }
    
    
    /**
     * Gets the exterior property
     */
    @Override
    public AbstractRing getExterior()
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
    public void setExterior(AbstractRing exterior)
    {
        this.exterior = exterior;
    }
    
    
    /**
     * Gets the list of interior properties
     */
    @Override
    public List<AbstractRing> getInteriorList()
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
    public void addInterior(AbstractRing interior)
    {
        this.interiorList.add(interior);
    }
}
