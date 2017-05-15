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

import net.opengis.OgcProperty;
import net.opengis.OgcPropertyList;


/**
 * POJO class for XML type AbstractSimpleComponentType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface SimpleComponent extends DataComponent
{
    
    
    /**
     * Gets the list of quality properties
     */
    public OgcPropertyList<SimpleComponent> getQualityList();
    
    
    /**
     * Returns number of quality properties
     */
    public int getNumQualitys();
    
    
    /**
     * Adds a new qualityAsQuantity property
     */
    public void addQuality(Quantity quality);
    
    
    /**
     * Adds a new qualityAsQuantityRange property
     */
    public void addQuality(QuantityRange quality);
    
    
    /**
     * Adds a new qualityAsCategory property
     */
    public void addQuality(Category quality);
    
    
    /**
     * Adds a new qualityAsText property
     */
    public void addQuality(Text quality);
    
    
    /**
     * Gets the nilValues property
     */
    public NilValues getNilValues();
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the nilValues property
     */
    public OgcProperty<NilValues> getNilValuesProperty();
    
    
    /**
     * Checks if nilValues is set
     */
    public boolean isSetNilValues();
    
    
    /**
     * Sets the nilValues property
     */
    public void setNilValues(NilValues nilValues);
    
    
    /**
     * Gets the referenceFrame property
     */
    public String getReferenceFrame();
    
    
    /**
     * Checks if referenceFrame is set
     */
    public boolean isSetReferenceFrame();
    
    
    /**
     * Sets the referenceFrame property
     */
    public void setReferenceFrame(String referenceFrame);
    
    
    /**
     * Gets the axisID property
     */
    public String getAxisID();
    
    
    /**
     * Checks if axisID is set
     */
    public boolean isSetAxisID();
    
    
    /**
     * Sets the axisID property
     */
    public void setAxisID(String axisID);
    
    
    /**
     * Gets the component data type
     */
    public DataType getDataType();


    /**
     * Sets the component data type
     */
    public void setDataType(DataType type);
    
    
    /**
     * Checks if value is set
     */
    public boolean isSetValue();
    
    
    /**
     * Unsets the value property
     */
    public void unSetValue();
}
