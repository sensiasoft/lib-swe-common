package net.opengis.gml.v32.impl;

import net.opengis.gml.v32.Reference;


/**
 * POJO class for XML type ReferenceType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public class ReferenceImpl extends net.opengis.OgcPropertyImpl<Object> implements Reference
{
    static final long serialVersionUID = 1L;
    protected Boolean owns;
    protected String remoteSchema;
    
    
    public ReferenceImpl()
    {
    }
    
    
    public ReferenceImpl(String href)
    {
        super(href);
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
