package net.opengis.swe.v20;



/**
 * POJO class for XML type AbstractDataComponentType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface AbstractDataComponent extends AbstractSWEIdentifiable
{
    
    
    /**
     * Gets the updatable property
     */
    public boolean getUpdatable();
    
    
    /**
     * Checks if updatable is set
     */
    public boolean isSetUpdatable();
    
    
    /**
     * Sets the updatable property
     */
    public void setUpdatable(boolean updatable);
    
    
    /**
     * Unsets the updatable property
     */
    public void unSetUpdatable();
    
    
    /**
     * Gets the optional property
     */
    public boolean getOptional();
    
    
    /**
     * Checks if optional is set
     */
    public boolean isSetOptional();
    
    
    /**
     * Sets the optional property
     */
    public void setOptional(boolean optional);
    
    
    /**
     * Unsets the optional property
     */
    public void unSetOptional();
    
    
    /**
     * Gets the definition property
     */
    public String getDefinition();
    
    
    /**
     * Checks if definition is set
     */
    public boolean isSetDefinition();
    
    
    /**
     * Sets the definition property
     */
    public void setDefinition(String definition);
}
