package org.vast.data;

import java.util.List;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataType;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.AllowedValues;
import net.opengis.swe.v20.Count;


/**
 * <p>
 * Extended SWE Count implementation adapted to old VAST framework
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Aug 30, 2014
 */
public class CountImpl extends DataValue implements Count
{
    static final long serialVersionUID = 1L;
    protected OgcProperty<AllowedValues> constraint;
    
    
    public CountImpl()
    {
        this.dataType = DataType.INT;
    }
    
    
    @Override
    public CountImpl copy()
    {
        CountImpl newObj = new CountImpl();
        super.copyTo(newObj);
        
        if (constraint != null)
            newObj.constraint = constraint.copy();
        else
            newObj.constraint = null;
        
        return newObj;
    }
    
    
    /**
     * Gets the constraint property
     */
    @Override
    public AllowedValues getConstraint()
    {
        return constraint.getValue();
    }
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the constraint property
     */
    @Override
    public OgcProperty<AllowedValues> getConstraintProperty()
    {
        if (constraint == null)
            constraint = new OgcPropertyImpl<AllowedValues>();
        return constraint;
    }
    
    
    /**
     * Checks if constraint is set
     */
    @Override
    public boolean isSetConstraint()
    {
        return (constraint != null && (constraint.hasValue() || constraint.hasHref()));
    }
    
    
    /**
     * Sets the constraint property
     */
    @Override
    public void setConstraint(AllowedValues constraint)
    {
        if (this.constraint == null)
            this.constraint = new OgcPropertyImpl<AllowedValues>();
        this.constraint.setValue(constraint);
    }
    
    
    /**
     * Gets the value property
     */
    @Override
    public int getValue()
    {
        if (dataBlock == null)
            return 0;
        return dataBlock.getIntValue();
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
    public void setValue(int value)
    {
        if (dataBlock == null)
            assignNewDataBlock();
        dataBlock.setIntValue(value);
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
        return (constraint != null);
    }
    
    
    @Override
    public void validateData(List<CDMException> errorList)
    {
        if (constraint != null && isSetValue())
        {
            AllowedValuesImpl constraint = (AllowedValuesImpl)this.constraint;            
            if (!constraint.isValid(getValue()))
            {
                errorList.add(new CDMException(getName(), "Value '" + dataBlock.getStringValue() +
                        "' is not valid for component '" + getName() + "': " + constraint.getAssertionMessage()));
            }
        }
    }
    
    
    @Override
    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append(indent);
        text.append("Count");
        if (dataBlock != null)
            text.append(" = ").append(dataBlock.getIntValue());
        return text.toString();
    }
}
