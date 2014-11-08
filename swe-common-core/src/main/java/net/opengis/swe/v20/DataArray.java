package net.opengis.swe.v20;

import net.opengis.OgcProperty;


/**
 * POJO class for XML type DataArrayType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface DataArray extends AbstractDataComponent
{
    
    
    /**
     * Gets the elementCount property
     */
    public Count getElementCount();
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the elementCount property
     */
    public OgcProperty<Count> getElementCountProperty();
    
    
    /**
     * Sets the elementCount property
     */
    public void setElementCount(Count elementCount);
    
    
    /**
     * Gets the elementType property
     */
    public AbstractDataComponent getElementType();
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the elementType property
     */
    public OgcProperty<AbstractDataComponent> getElementTypeProperty();
    
    
    /**
     * Sets the elementType property
     */
    public void setElementType(String name, AbstractDataComponent elementType);
    
    
    /**
     * Gets the encoding property
     */
    public AbstractEncoding getEncoding();
    
    
    /**
     * Checks if encoding is set
     */
    public boolean isSetEncoding();
    
    
    /**
     * Sets the encoding property
     */
    public void setEncoding(AbstractEncoding encoding);
    
    
    /**
     * Gets the values property
     */
    public EncodedValues getValues();
    
    
    /**
     * Checks if values is set
     */
    public boolean isSetValues();
    
    
    /**
     * Sets the values property
     */
    public void setValues(EncodedValues values);
}
