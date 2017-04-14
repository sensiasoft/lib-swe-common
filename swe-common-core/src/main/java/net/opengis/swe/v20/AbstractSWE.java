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

import java.io.Serializable;
import java.util.List;


/**
 * POJO class for XML type AbstractSWEType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface AbstractSWE extends Serializable
{
    
    
    /**
     * Gets the list of extension properties
     */
    public List<Object> getExtensionList();
    
    
    /**
     * Returns number of extension properties
     */
    public int getNumExtensions();
    
    
    /**
     * Adds a new extension property
     */
    public void addExtension(Object extension);
    
    
    /**
     * Gets the id property
     */
    public String getId();
    
    
    /**
     * Checks if id is set
     */
    public boolean isSetId();
    
    
    /**
     * Sets the id property
     */
    public void setId(String id);
}
