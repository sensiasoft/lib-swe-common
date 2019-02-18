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
import java.util.ArrayList;
import java.util.List;
import net.opengis.OgcPropertyList;
import net.opengis.gml.v32.AbstractGML;
import net.opengis.gml.v32.CodeWithAuthority;
import net.opengis.gml.v32.Reference;



/**
 * POJO class for XML type AbstractGMLType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public abstract class AbstractGMLImpl implements AbstractGML
{
    private static final long serialVersionUID = -5559560460456343028L;
    public static final String UUID_CODE = "uid";
    protected OgcPropertyList<Serializable> metaDataPropertyList = new OgcPropertyList<>();
    protected String description;
    protected Reference descriptionReference;
    protected CodeWithAuthority identifier;
    protected ArrayList<CodeWithAuthority> nameList = new ArrayList<>();
    protected String id = "";
    
    
    public AbstractGMLImpl()
    {
    }
    
    
    /**
     * Gets the list of metaDataProperty properties
     */
    @Override
    public OgcPropertyList<Serializable> getMetaDataPropertyList()
    {
        return metaDataPropertyList;
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
    
    
    @Override
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    
    /**
     * Gets the descriptionReference property
     */
    @Override
    public Reference getDescriptionReference()
    {
        return descriptionReference;
    }
    
    
    /**
     * Checks if descriptionReference is set
     */
    @Override
    public boolean isSetDescriptionReference()
    {
        return (descriptionReference != null);
    }
    
    
    /**
     * Sets the descriptionReference property
     */
    @Override
    public void setDescriptionReference(Reference descriptionReference)
    {
        this.descriptionReference = descriptionReference;
    }
    
    
    /**
     * Gets the identifier property
     */
    @Override
    public CodeWithAuthority getIdentifier()
    {
        return identifier;
    }
    
    
    /**
     * Gets the unique identifier
     */
    @Override
    public String getUniqueIdentifier()
    {
        if (identifier == null)
            return null;
        
        return identifier.getValue();
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
    public void setIdentifier(CodeWithAuthority identifier)
    {
        this.identifier = identifier;
    }
    

    @Override
    public void setUniqueIdentifier(String identifier)
    {
        setIdentifier(new CodeWithAuthorityImpl(UUID_CODE, identifier));        
    }


    /**
     * Gets the list of name properties
     */
    @Override
    public List<CodeWithAuthority> getNameList()
    {
        return nameList;
    }
    
    
    /**
     * Returns number of name properties
     */
    @Override
    public int getNumNames()
    {
        if (nameList == null)
            return 0;
        return nameList.size();
    }
    
    
    @Override
    public String getName()
    {
        if (getNumNames() > 0)
            return nameList.get(0).getValue();
        return null;
    }
    
    
    /**
     * Adds a new name property
     */
    @Override
    public void addName(CodeWithAuthority name)
    {
        this.nameList.add(name);
    }
    
    
    @Override
    public void setName(String name)
    {
        if (name != null)
        {
            if (nameList.isEmpty())
                addName(new CodeWithAuthorityImpl(name));
            else
                nameList.set(0, new CodeWithAuthorityImpl(name));
        }
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
     * Sets the id property
     */
    @Override
    public void setId(String id)
    {
        this.id = id;
    }
}
