package org.vast.data;

import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.AbstractDataComponent;
import net.opengis.swe.v20.AbstractEncoding;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataStream;
import net.opengis.swe.v20.EncodedValues;


/**
 * POJO class for XML type DataStreamType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class DataStreamImpl extends AbstractSWEIdentifiableImpl implements DataStream
{
    static final long serialVersionUID = 1L;
    protected Count elementCount;
    protected OgcProperty<AbstractDataComponent> elementType = new OgcPropertyImpl<AbstractDataComponent>();
    protected AbstractEncoding encoding;
    protected EncodedValues values;
    
    
    public DataStreamImpl()
    {
    }
    
    
    /**
     * Gets the elementCount property
     */
    @Override
    public Count getElementCount()
    {
        return elementCount;
    }
    
    
    /**
     * Checks if elementCount is set
     */
    @Override
    public boolean isSetElementCount()
    {
        return (elementCount != null);
    }
    
    
    /**
     * Sets the elementCount property
     */
    @Override
    public void setElementCount(Count elementCount)
    {
        this.elementCount = elementCount;
    }
    
    
    /**
     * Gets the elementType property
     */
    @Override
    public AbstractDataComponent getElementType()
    {
        return elementType.getValue();
    }
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the elementType property
     */
    @Override
    public OgcProperty<AbstractDataComponent> getElementTypeProperty()
    {
        if (elementType == null)
            elementType = new OgcPropertyImpl<AbstractDataComponent>();
        return elementType;
    }
    
    
    /**
     * Sets the elementType property
     */
    @Override
    public void setElementType(String name, AbstractDataComponent elementType)
    {
        this.elementType.setValue(elementType);
        this.elementType.setName(name);
    }
    
    
    /**
     * Gets the encoding property
     */
    @Override
    public AbstractEncoding getEncoding()
    {
        return encoding;
    }
    
    
    /**
     * Sets the encoding property
     */
    @Override
    public void setEncoding(AbstractEncoding encoding)
    {
        this.encoding = encoding;
    }
    
    
    /**
     * Gets the values property
     */
    @Override
    public EncodedValues getValues()
    {
        return values;
    }
    
    
    /**
     * Sets the values property
     */
    @Override
    public void setValues(EncodedValues values)
    {
        this.values = values;
    }
}
