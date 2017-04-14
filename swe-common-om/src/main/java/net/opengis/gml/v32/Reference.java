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

import java.io.Serializable;
import net.opengis.OgcProperty;

/**
 * POJO class for XML type ReferenceType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface Reference extends OgcProperty<Serializable>
{
    
    /**
     * Gets the owns property
     */
    public boolean getOwns();
    
    
    /**
     * Checks if owns is set
     */
    public boolean isSetOwns();
    
    
    /**
     * Sets the owns property
     */
    public void setOwns(boolean owns);
    
    
    /**
     * Unsets the owns property
     */
    public void unSetOwns();
    
    
    /**
     * Gets the remoteSchema property
     */
    public String getRemoteSchema();
    
    
    /**
     * Checks if remoteSchema is set
     */
    public boolean isSetRemoteSchema();
    
    
    /**
     * Sets the remoteSchema property
     */
    public void setRemoteSchema(String remoteSchema);
}
