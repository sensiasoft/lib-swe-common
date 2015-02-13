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



/**
 * POJO class for XML type StringOrRefType(@http://www.opengis.net/gml/3.2).
 *
 */
public interface StringOrRef extends net.opengis.OgcProperty<String>
{
        
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
