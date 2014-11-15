package net.opengis.gml.v32;



/**
 * POJO class for XML type ReferenceType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface Reference extends net.opengis.OgcProperty<Object>
{
    
    
    /**
     * Gets the owns property
     */
    public boolean getOwns();
    
    
    /**
     * Checks if owns is set
     */
    public boolean isSetOwns();
    
    
    /**
     * Sets the owns property
     */
    public void setOwns(boolean owns);
    
    
    /**
     * Unsets the owns property
     */
    public void unSetOwns();
    
    
    /**
     * Gets the remoteSchema property
     */
    public String getRemoteSchema();
    
    
    /**
     * Checks if remoteSchema is set
     */
    public boolean isSetRemoteSchema();
    
    
    /**
     * Sets the remoteSchema property
     */
    public void setRemoteSchema(String remoteSchema);
}
