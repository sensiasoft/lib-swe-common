package org.vast.data;

import net.opengis.swe.v20.NilValue;


/**
 * POJO class for XML type NilValue(@http://www.opengis.net/swe/2.0).
 *
 */
public class NilValueImpl implements NilValue
{
    static final long serialVersionUID = 1L;
    protected String reason = "";
    protected Object value;
    
    
    public NilValueImpl()
    {
    }
    
    
    public NilValueImpl(String reason, Object value)
    {
        this.reason = reason;
        this.value = value;
    }
    
    
    /**
     * Gets the reason property
     */
    @Override
    public String getReason()
    {
        return reason;
    }
    
    
    /**
     * Sets the reason property
     */
    @Override
    public void setReason(String reason)
    {
        this.reason = reason;
    }
    
    
    /**
     * Gets the inline value
     */
    @Override
    public Object getValue()
    {
        return value;
    }
    
    
    /**
     * Sets the inline value
     */
    @Override
    public void setValue(Object value)
    {
        this.value = value;
    }
}
