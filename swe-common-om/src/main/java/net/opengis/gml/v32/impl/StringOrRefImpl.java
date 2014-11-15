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
