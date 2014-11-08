package net.opengis.swe.v20;

import java.util.List;


/**
 * POJO class for XML type AbstractSWEType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface AbstractSWE
{
    
    
    /**
     * Gets the list of extension properties
     */
    public List<Object> getExtensionList();
    
    
    /**
     * Returns number of extension properties
     */
    public int getNumExtensions();
    
    
    /**
     * Adds a new extension property
     */
    public void addExtension(Object extension);
    
    
    /**
     * Gets the id property
     */
    public String getId();
    
    
    /**
     * Checks if id is set
     */
    public boolean isSetId();
    
    
    /**
     * Sets the id property
     */
    public void setId(String id);
}
