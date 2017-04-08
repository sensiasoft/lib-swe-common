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


/**
 * <p>
 * Tagging interface for all block components
 * </p>
 *
 * @author Alex Robin
 * @since Nov 8, 2014
 */
@SuppressWarnings("javadoc")
public interface BlockComponent extends DataComponent
{
    
    /**
     * Gets the elementCount property
     */
    public Count getElementCount();
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the elementCount property
     */
    public OgcProperty<Count> getElementCountProperty();
    
    
    /**
     * Sets the elementCount property
     */
    public void setElementCount(Count elementCount);
    
    
    /**
     * Gets the elementType property
     */
    public DataComponent getElementType();
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the elementType property
     */
    public OgcProperty<DataComponent> getElementTypeProperty();
    
    
    /**
     * Sets the elementType property
     */
    public void setElementType(String name, DataComponent elementType);
    
    
    /**
     * Gets the encoding property
     */
    public DataEncoding getEncoding();
    
    
    /**
     * Checks if encoding is set
     */
    public boolean isSetEncoding();
    
    
    /**
     * Sets the encoding property
     */
    public void setEncoding(DataEncoding encoding);
    
    
    /**
     * Gets the values property
     */
    public EncodedValues getValues();
    
    
    /**
     * Checks if values is set
     */
    public boolean isSetValues();
    
    
    /**
     * Sets the values property
     */
    public void setValues(EncodedValues values);

}
