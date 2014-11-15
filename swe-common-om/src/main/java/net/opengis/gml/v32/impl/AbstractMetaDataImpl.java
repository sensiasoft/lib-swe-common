package net.opengis.gml.v32.impl;

import net.opengis.gml.v32.AbstractMetaData;


/**
 * POJO class for XML type AbstractMetaDataType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public abstract class AbstractMetaDataImpl implements AbstractMetaData
{
    static final long serialVersionUID = 1L;
    protected String id;
    
    
    public AbstractMetaDataImpl()
    {
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
