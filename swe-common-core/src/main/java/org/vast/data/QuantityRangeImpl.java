package org.vast.data;

import java.util.List;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.AllowedValues;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.QuantityRange;
import net.opengis.swe.v20.UnitReference;
import net.opengis.swe.v20.ValidationException;


/**
 * <p>
 * Extended SWE QuantityRange implementation adapted to old VAST framework
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Aug 30, 2014
 */
public class QuantityRangeImpl extends AbstractRangeComponentImpl implements QuantityRange
{
    static final long serialVersionUID = 1L;
    protected UnitReferenceImpl uom = new UnitReferenceImpl();
    protected OgcProperty<AllowedValues> constraint;
    protected double[] tmpValue = new double[2];
    
    
    public QuantityRangeImpl()
    {
        this(DataType.DOUBLE);
    }
    
    
    public QuantityRangeImpl(DataType dataType)
    {
        this.dataType = dataType;
        this.min = new QuantityImpl(dataType);
        this.max = new QuantityImpl(dataType);
    }
    
    
    @Override
    public QuantityRangeImpl copy()
    {
        QuantityRangeImpl newObj = new QuantityRangeImpl();
        super.copyTo(newObj);
        
        if (uom != null)
            newObj.uom = uom.copy();
        else
            newObj.uom = null;
        
        if (constraint != null)
            newObj.constraint = constraint.copy();
        else
            newObj.constraint = null;
        
        return newObj;
    }
    
    
    /**
     * Gets the uom property
     */
    @Override
    public UnitReference getUom()
    {
        return uom;
    }
    
    
    /**
     * Sets the uom property
     */
    @Override
    public void setUom(UnitReference uom)
    {
        this.uom = (UnitReferenceImpl)uom;
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
    public double[] getValue()
    {
        if (dataBlock == null)
            return null;
        tmpValue[0] = dataBlock.getDoubleValue(0);
        tmpValue[1] = dataBlock.getDoubleValue(1);
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
    public void setValue(double[] value)
    {
        if (value == null)
        {
            dataBlock = null;
            return;
        }
        
        if (dataBlock == null)
            assignNewDataBlock();
        dataBlock.setDoubleValue(0, value[0]);
        dataBlock.setDoubleValue(1, value[1]);
    }


    @Override
    public boolean hasConstraints()
    {
        return (constraint != null);
    }
    
    
    @Override
    public void validateData(List<ValidationException> errorList)
    {
        if (constraint != null)
        {
            AllowedValuesImpl constraint = (AllowedValuesImpl)this.constraint;
            double min = dataBlock.getDoubleValue(0);
            double max = dataBlock.getDoubleValue(1);
            
            if (!constraint.isValid(min) || !constraint.isValid(max))
            {
                errorList.add(new ValidationException(getName(), "Value '[" + min + " " + max + "]" +
                        "' is not valid for component '" + getName() + "': " + constraint.getAssertionMessage()));
            }
        }
    }


    @Override
    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append(indent);
        text.append("QuantityRange");                
        if (dataBlock != null)
        {
            text.append(" = [");
            text.append(dataBlock.getStringValue(0));
            text.append(' ');
            text.append(dataBlock.getStringValue(1));
            text.append(']');
        }
        return text.toString();
    }
}
