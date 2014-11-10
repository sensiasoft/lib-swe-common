package org.vast.data;

import java.util.List;
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.DataType;


/**
 * <p>
 * Extended SWE Boolean implementation adapted to old VAST framework
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Aug 30, 2014
 */
public class BooleanImpl extends DataValue implements Boolean
{
    static final long serialVersionUID = 1L;
    
    
    public BooleanImpl()
    {
        this.dataType = DataType.BOOLEAN;
    }
    
    
    @Override
    public BooleanImpl copy()
    {
        BooleanImpl newObj = new BooleanImpl();
        super.copyTo(newObj);
        return newObj;
    }
    
    
    /**
     * Gets the value property
     */
    @Override
    public boolean getValue()
    {
        if (dataBlock == null)
            return false;
        return dataBlock.getBooleanValue();
    }
    
    
    /**
     * Checks if value is set
     */
    @Override
    public boolean isSetValue()
    {
        return (dataBlock != null);
    }
    
    
    /**
     * Sets the value property
     */
    @Override
    public void setValue(boolean value)
    {
        if (dataBlock == null)
            assignNewDataBlock();
        dataBlock.setBooleanValue(value);
    }
    
    
    /**
     * Unsets the value property
     */
    @Override
    public void unSetValue()
    {
        dataBlock = null;
    }


    @Override
    public boolean hasConstraints()
    {
        return false;
    }
    
    
    @Override
    public void validateData(List<Exception> errorList)
    {        
    }
    
    
    @Override
    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append("Boolean ");                
        if (dataBlock != null)
            text.append(" = " + dataBlock.getStringValue());
        return text.toString();
    }
}
