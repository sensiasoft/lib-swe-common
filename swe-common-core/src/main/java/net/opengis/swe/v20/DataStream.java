package net.opengis.swe.v20;

import net.opengis.OgcProperty;


/**
 * POJO class for XML type DataStreamType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface DataStream extends AbstractSWEIdentifiable
{
    
    
    /**
     * Gets the elementCount property
     */
    public Count getElementCount();
    
    
    /**
     * Checks if elementCount is set
     */
    public boolean isSetElementCount();
    
    
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
     * Sets the encoding property
     */
    public void setEncoding(AbstractEncoding encoding);
    
    
    /**
     * Gets the values property
     */
    public EncodedValues getValues();
    
    
    /**
     * Sets the values property
     */
    public void setValues(EncodedValues values);
}
