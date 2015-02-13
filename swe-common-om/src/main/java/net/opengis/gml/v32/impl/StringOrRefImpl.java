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

import net.opengis.gml.v32.StringOrRef;


/**
 * POJO class for XML type StringOrRefType(@http://www.opengis.net/gml/3.2).
 *
 */
public class StringOrRefImpl extends net.opengis.OgcPropertyImpl<String> implements StringOrRef
{
    static final long serialVersionUID = 1L;
    protected String remoteSchema;
    
    
    public StringOrRefImpl()
    {
    }
    
    
    public StringOrRefImpl(String value)
    {
        this.value = value;
    }
    
    
    /**
     * Gets the remoteSchema property
     */
    @Override
    public String getRemoteSchema()
    {
        return remoteSchema;
    }
    
    
    /**
     * Checks if remoteSchema is set
     */
    @Override
    public boolean isSetRemoteSchema()
    {
        return (remoteSchema != null);
    }
    
    
    /**
     * Sets the remoteSchema property
     */
    @Override
    public void setRemoteSchema(String remoteSchema)
    {
        this.remoteSchema = remoteSchema;
    }
}
