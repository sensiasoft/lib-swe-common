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

import net.opengis.swe.v20.AbstractSWEIdentifiable;


/**
 * POJO class for XML type AbstractSWEIdentifiableType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public abstract class AbstractSWEIdentifiableImpl extends AbstractSWEImpl implements AbstractSWEIdentifiable
{
    private static final long serialVersionUID = 3952266605465337156L;
    protected String identifier;
    protected String label;
    protected String description;
    
    
    public AbstractSWEIdentifiableImpl()
    {
    }
    
    
    protected void copyTo(AbstractSWEIdentifiableImpl other)
    {
        super.copyTo(other);
        other.identifier = identifier;
        other.label = label;
        other.description = description;
    }
    
    
    /**
     * Gets the identifier property
     */
    @Override
    public String getIdentifier()
    {
        return identifier;
    }
    
    
    /**
     * Checks if identifier is set
     */
    @Override
    public boolean isSetIdentifier()
    {
        return (identifier != null);
    }
    
    
    /**
     * Sets the identifier property
     */
    @Override
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }
    
    
    /**
     * Gets the label property
     */
    @Override
    public String getLabel()
    {
        return label;
    }
    
    
    /**
     * Checks if label is set
     */
    @Override
    public boolean isSetLabel()
    {
        return (label != null);
    }
    
    
    /**
     * Sets the label property
     */
    @Override
    public void setLabel(String label)
    {
        this.label = label;
    }
    
    
    /**
     * Gets the description property
     */
    @Override
    public String getDescription()
    {
        return description;
    }
    
    
    /**
     * Checks if description is set
     */
    @Override
    public boolean isSetDescription()
    {
        return (description != null);
    }
    
    
    /**
     * Sets the description property
     */
    @Override
    public void setDescription(String description)
    {
        this.description = description;
    }
}
