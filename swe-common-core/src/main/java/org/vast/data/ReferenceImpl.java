package org.vast.data;

import net.opengis.swe.v20.Reference;


/**
 * POJO class for XML type Reference(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class ReferenceImpl extends net.opengis.OgcPropertyImpl<Object> implements Reference
{
    static final long serialVersionUID = 1L;
    
    
    public ReferenceImpl()
    {
    }
    
    
    public ReferenceImpl(String href)
    {
        this.href = href;
    }
    
    
    public ReferenceImpl(String href, String role)
    {
        this.href = href;
        this.role = role;
    }
}
