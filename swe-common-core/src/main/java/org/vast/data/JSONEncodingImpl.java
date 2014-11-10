package org.vast.data;

import net.opengis.swe.v20.XMLEncoding;


/**
 * POJO class for XML type JSONEncodingType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class JSONEncodingImpl extends AbstractEncodingImpl implements XMLEncoding
{
    static final long serialVersionUID = 1L;
    boolean pretty;
    
    
    public JSONEncodingImpl()
    {        
    }
    
    
    public JSONEncodingImpl(boolean pretty)
    {
        this.pretty = pretty;
    }
    
    
    @Override
    public JSONEncodingImpl copy()
    {
        JSONEncodingImpl newObj = new JSONEncodingImpl(pretty);
        return newObj;
    }


    public boolean isPretty()
    {
        return pretty;
    }


    public void setPretty(boolean pretty)
    {
        this.pretty = pretty;
    }
}
