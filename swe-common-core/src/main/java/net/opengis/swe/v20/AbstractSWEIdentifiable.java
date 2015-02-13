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



/**
 * POJO class for XML type AbstractSWEIdentifiableType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface AbstractSWEIdentifiable extends AbstractSWE
{
    
    
    /**
     * Gets the identifier property
     */
    public String getIdentifier();
    
    
    /**
     * Checks if identifier is set
     */
    public boolean isSetIdentifier();
    
    
    /**
     * Sets the identifier property
     */
    public void setIdentifier(String identifier);
    
    
    /**
     * Gets the label property
     */
    public String getLabel();
    
    
    /**
     * Checks if label is set
     */
    public boolean isSetLabel();
    
    
    /**
     * Sets the label property
     */
    public void setLabel(String label);
    
    
    /**
     * Gets the description property
     */
    public String getDescription();
    
    
    /**
     * Checks if description is set
     */
    public boolean isSetDescription();
    
    
    /**
     * Sets the description property
     */
    public void setDescription(String description);
}
