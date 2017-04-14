/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import java.util.ArrayList;
import java.util.List;
import net.opengis.swe.v20.AbstractSWE;


/**
 * POJO class for XML type AbstractSWEType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public abstract class AbstractSWEImpl implements AbstractSWE
{
    private static final long serialVersionUID = 2263301651066134450L;
    protected ArrayList<Object> extensionList = new ArrayList<Object>();
    protected String id;
    
    
    public AbstractSWEImpl()
    {
    }
    
    
    protected void copyTo(AbstractSWEImpl other)
    {
        other.id = id;
        other.extensionList.addAll(extensionList);
    }
    
    
    /**
     * Gets the list of extension properties
     */
    @Override
    public List<Object> getExtensionList()
    {
        return extensionList;
    }
    
    
    /**
     * Returns number of extension properties
     */
    @Override
    public int getNumExtensions()
    {
        if (extensionList == null)
            return 0;
        return extensionList.size();
    }
    
    
    /**
     * Adds a new extension property
     */
    @Override
    public void addExtension(Object extension)
    {
        this.extensionList.add(extension);
    }
    
    
    /**
     * Gets the id property
     */
    @Override
    public String getId()
    {
        return id;
    }
    
    
    /**
     * Checks if id is set
     */
    @Override
    public boolean isSetId()
    {
        return (id != null);
    }
    
    
    /**
     * Sets the id property
     */
    @Override
    public void setId(String id)
    {
        this.id = id;
    }
}
