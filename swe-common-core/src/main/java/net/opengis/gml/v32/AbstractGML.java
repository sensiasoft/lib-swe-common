package net.opengis.gml.v32;

import java.util.List;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyList;


/**
 * POJO class for XML type AbstractGMLType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface AbstractGML
{
        
    /**
     * Gets the list of metaDataProperty properties
     */
    public OgcPropertyList<AbstractMetaData> getMetaDataPropertyList();
    
    
    /**
     * Returns number of metaDataProperty properties
     */
    public int getNumMetaDataPropertys();
    
    
    /**
     * Adds a new metaDataProperty property
     */
    public void addMetaDataProperty(AbstractMetaData metaDataProperty);
    
    
    /**
     * Gets the description property
     */
    public StringOrRef getDescription();
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the description property
     */
    public OgcProperty<StringOrRef> getDescriptionProperty();
    
    
    /**
     * Checks if description is set
     */
    public boolean isSetDescription();
    
    
    /**
     * Sets the description property
     */
    public void setDescription(StringOrRef description);
    
    
    /**
     * Gets the descriptionReference property
     */
    public Reference getDescriptionReference();
    
    
    /**
     * Checks if descriptionReference is set
     */
    public boolean isSetDescriptionReference();
    
    
    /**
     * Sets the descriptionReference property
     */
    public void setDescriptionReference(Reference descriptionReference);
    
    
    /**
     * Gets the identifier property
     */
    public CodeWithAuthority getIdentifier();
    
    
    /**
     * Checks if identifier is set
     */
    public boolean isSetIdentifier();
    
    
    /**
     * Sets the identifier property
     */
    public void setIdentifier(CodeWithAuthority identifier);
    
    
    /**
     * Gets the list of name properties
     */
    public List<Code> getNameList();
    
    
    /**
     * Returns number of name properties
     */
    public int getNumNames();
    
    
    /**
     * Adds a new name property
     */
    public void addName(Code name);
    
    
    /**
     * Gets the id property
     */
    public String getId();
    
    
    /**
     * Sets the id property
     */
    public void setId(String id);
}
