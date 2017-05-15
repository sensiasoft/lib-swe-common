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

import java.io.Serializable;
import net.opengis.gml.v32.Reference;


/**
 * POJO class for XML type ReferenceType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public class ReferenceImpl extends net.opengis.OgcPropertyImpl<Serializable> implements Reference
{
    private static final long serialVersionUID = 8584947819891402530L;
    protected Boolean owns;
    protected String remoteSchema;
    
    
    public ReferenceImpl()
    {
    }
    
    
    public ReferenceImpl(String href)
    {
        this.href = href;
    }
    
    
    /**
     * Gets the owns property
     */
    @Override
    public boolean getOwns()
    {
        return owns;
    }
    
    
    /**
     * Checks if owns is set
     */
    @Override
    public boolean isSetOwns()
    {
        return (owns != null);
    }
    
    
    /**
     * Sets the owns property
     */
    @Override
    public void setOwns(boolean owns)
    {
        this.owns = owns;
    }
    
    
    /**
     * Unsets the owns property
     */
    @Override
    public void unSetOwns()
    {
        this.owns = null;
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
