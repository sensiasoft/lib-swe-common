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

import java.util.ArrayList;
import java.util.List;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.OgcPropertyList;
import net.opengis.gml.v32.AbstractGML;
import net.opengis.gml.v32.AbstractMetaData;
import net.opengis.gml.v32.Code;
import net.opengis.gml.v32.CodeWithAuthority;
import net.opengis.gml.v32.Reference;
import net.opengis.gml.v32.StringOrRef;



/**
 * POJO class for XML type AbstractGMLType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public abstract class AbstractGMLImpl implements AbstractGML
{
    static final long serialVersionUID = 1L;
    public final static String UUID_CODE = "uid";
    protected OgcPropertyList<AbstractMetaData> metaDataPropertyList = new OgcPropertyList<AbstractMetaData>();
    protected OgcProperty<StringOrRef> description;
    protected Reference descriptionReference;
    protected CodeWithAuthority identifier;
    protected List<Code> nameList = new ArrayList<Code>();
    protected String id = "";
    
    
    public AbstractGMLImpl()
    {
    }
    
    
    /**
     * Gets the list of metaDataProperty properties
     */
    @Override
    public OgcPropertyList<AbstractMetaData> getMetaDataPropertyList()
    {
        return metaDataPropertyList;
    }
    
    
    /**
     * Returns number of metaDataProperty properties
     */
    @Override
    public int getNumMetaDataPropertys()
    {
        if (metaDataPropertyList == null)
            return 0;
        return metaDataPropertyList.size();
    }
    
    
    /**
     * Adds a new metaDataProperty property
     */
    @Override
    public void addMetaDataProperty(AbstractMetaData metaDataProperty)
    {
        this.metaDataPropertyList.add(metaDataProperty);
    }
    
    
    /**
     * Gets the description property
     */
    @Override
    public StringOrRef getDescription()
    {
        return description.getValue();
    }
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the description property
     */
    @Override
    public OgcProperty<StringOrRef> getDescriptionProperty()
    {
        if (description == null)
            description = new OgcPropertyImpl<StringOrRef>();
        return description;
    }
    
    
    /**
     * Checks if description is set
     */
    @Override
    public boolean isSetDescription()
    {
        return (description != null && description.getValue() != null);
    }
    
    
    /**
     * Sets the description property
     */
    @Override
    public void setDescription(StringOrRef description)
    {
        if (this.description == null)
            this.description = new OgcPropertyImpl<StringOrRef>();
        this.description.setValue(description);
    }
    
    
    @Override
    public void setDescription(String description)
    {
        setDescription(new StringOrRefImpl(description));        
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
    public List<Code> getNameList()
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
    public void addName(Code name)
    {
        this.nameList.add(name);
    }
    
    
    @Override
    public void setName(String name)
    {
        addName(new CodeImpl(name));
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
