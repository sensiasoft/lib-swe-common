/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32;

import java.util.List;


/**
 * POJO class for XML type PolygonType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface Polygon extends AbstractSurface
{
    
    /**
     * Gets the exterior property
     */
    public LinearRing getExterior();
    
    
    /**
     * Checks if exterior is set
     */
    public boolean isSetExterior();
    
    
    /**
     * Sets the exterior property
     */
    public void setExterior(LinearRing exterior);
    
    
    /**
     * Gets the list of interior properties
     */
    public List<LinearRing> getInteriorList();
    
    
    /**
     * Returns number of interior properties
     */
    public int getNumInteriors();
    
    
    /**
     * Adds a new interior property
     */
    public void addInterior(LinearRing interior);
}
