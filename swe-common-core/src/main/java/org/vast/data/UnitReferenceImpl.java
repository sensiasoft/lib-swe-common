package org.vast.data;

import org.vast.unit.Unit;
import net.opengis.swe.v20.UnitReference;


/**
 * POJO class for XML type UnitReference(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class UnitReferenceImpl extends net.opengis.OgcPropertyImpl<Object> implements UnitReference
{
    static final long serialVersionUID = 1L;
    protected String code;
    protected Unit unitObj;
    
    
    public UnitReferenceImpl()
    {        
    }
    
    
    public UnitReferenceImpl(String codeOrUri)
    {
        if (codeOrUri.startsWith("http") || codeOrUri.startsWith("urn"))
            this.href = codeOrUri;
        else
            this.code = codeOrUri;
    }
    
    
    public UnitReferenceImpl copy()
    {
        UnitReferenceImpl newObj = new UnitReferenceImpl();
        super.copyTo(newObj);
        newObj.code = code;
        return newObj;
    }
    
    
    /**
     * Gets the code property
     */
    @Override
    public String getCode()
    {
        return code;
    }
    
    
    /**
     * Checks if code is set
     */
    @Override
    public boolean isSetCode()
    {
        return (code != null);
    }
    
    
    /**
     * Sets the code property
     */
    @Override
    public void setCode(String code)
    {
        this.code = code;
    }


    public Unit getUnitObject()
    {
        return unitObj;
    }


    public void setUnitObject(Unit unitObj)
    {
        this.unitObj = unitObj;
    }
}
