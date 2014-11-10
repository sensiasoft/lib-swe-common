package org.vast.data;

import net.opengis.HasCopy;
import net.opengis.swe.v20.DataEncoding;


/**
 * POJO class for XML type AbstractEncodingType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public abstract class AbstractEncodingImpl extends AbstractSWEImpl implements DataEncoding, HasCopy
{
    static final long serialVersionUID = 1L;
    
    
    public AbstractEncodingImpl()
    {
    }
    
    
    public abstract AbstractEncodingImpl copy();
}
