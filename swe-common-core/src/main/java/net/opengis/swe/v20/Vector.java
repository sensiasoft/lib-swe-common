/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.swe.v20;

import net.opengis.OgcPropertyList;


/**
 * POJO class for XML type VectorType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface Vector extends DataComponent, HasRefFrames
{
    
    @Override
    public Vector copy();
    
    
    /**
     * Gets the list of coordinate properties
     */
    public OgcPropertyList<ScalarComponent> getCoordinateList();
    
    
    /**
     * Returns number of coordinate properties
     */
    public int getNumCoordinates();
    
    
    /**
     * Gets the coordinate property with the given name
     */
    public ScalarComponent getCoordinate(String name);
    
    
    /**
     * Adds a new coordinateAsCount property
     */
    public void addCoordinateAsCount(String name, Count coordinate);
    
    
    /**
     * Adds a new coordinateAsQuantity property
     */
    public void addCoordinateAsQuantity(String name, Quantity coordinate);
    
    
    /**
     * Adds a new coordinateAsTime property
     */
    public void addCoordinateAsTime(String name, Time coordinate);
    
    
    /**
     * Gets the component data type
     */
    public DataType getDataType();


    /**
     * Sets the component data type
     */
    public void setDataType(DataType type);

}
