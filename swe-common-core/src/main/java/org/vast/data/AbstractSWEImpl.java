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
    static final long serialVersionUID = 1L;
    protected List<Object> extensionList = new ArrayList<Object>();
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
