package org.vast.data;

import java.util.List;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataType;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.AllowedValues;
import net.opengis.swe.v20.CountRange;


/**
 * <p>
 * Extended SWE CountRange implementation adapted to old VAST framework
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Aug 30, 2014
 */public class CountRangeImpl extends AbstractRangeComponentImpl implements CountRange
{
    static final long serialVersionUID = 1L;
    protected OgcProperty<AllowedValues> constraint;
    protected int[] tmpValue = new int[2];
    
    
    public CountRangeImpl()
    {
        this.dataType = DataType.INT;
        this.min = new CountImpl();
        this.max = new CountImpl();
    }
    
    
    @Override
    public CountRangeImpl copy()
    {
        CountRangeImpl newObj = new CountRangeImpl();
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
    public int[] getValue()
    {
        if (dataBlock == null)
            return null;
        tmpValue[0] = dataBlock.getIntValue(0);
        tmpValue[1] = dataBlock.getIntValue(1);
        return tmpValue;
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
    public void setValue(int[] value)
    {
        if (value == null)
        {
            dataBlock = null;
            return;
        }
        
        if (dataBlock == null)
            assignNewDataBlock();
        dataBlock.setIntValue(0, value[0]);
        dataBlock.setIntValue(1, value[1]);
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
            int min = dataBlock.getIntValue(0);
            int max = dataBlock.getIntValue(1);
            
            if (!constraint.isValid(min) || !constraint.isValid(max))
            {
                errorList.add(new CDMException(getName(), "Value '[" + min + " " + max + "]" +
                        "' is not valid for component '" + getName() + "': " + constraint.getAssertionMessage()));
            }
        }        
    }


    @Override
    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append(indent);
        text.append("CountRange");
        if (dataBlock != null)
        {
            text.append(" = [");
            text.append(dataBlock.getIntValue(0));
            text.append(' ');
            text.append(dataBlock.getIntValue(1));
            text.append(']');
        }
        return text.toString();
    }
}
