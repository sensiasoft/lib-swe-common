package org.vast.data;

import java.util.List;
import org.vast.util.DateTimeFormat;
import net.opengis.DateTimeDouble;
import net.opengis.IDateTime;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.AllowedTimes;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.UnitReference;
import net.opengis.swe.v20.ValidationException;


/**
 * <p>
 * Extended SWE Time implementation adapted to old VAST framework
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Aug 30, 2014
 */
public class TimeImpl extends DataValue implements Time
{
    static final long serialVersionUID = 1L;
    protected UnitReferenceImpl uom = new UnitReferenceImpl();
    protected OgcProperty<AllowedTimes> constraint;
    protected IDateTime referenceTime;
    protected String localFrame;
    
    
    public TimeImpl()
    {
        this(DataType.DOUBLE);
    }
    
    
    public TimeImpl(DataType dataType)
    {
        this.dataType = dataType;
    }
    
    
    @Override
    public TimeImpl copy()
    {
        TimeImpl newObj = new TimeImpl();        
        super.copyTo(newObj);
        
        if (uom != null)
            newObj.uom = uom.copy();
        else
            newObj.uom = null;
        
        if (constraint != null)
            newObj.constraint = constraint.copy();
        else
            newObj.constraint = null;
        
        newObj.referenceTime = referenceTime;
        newObj.localFrame = localFrame;
        
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
    public AllowedTimes getConstraint()
    {
        return constraint.getValue();
    }
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the constraint property
     */
    @Override
    public OgcProperty<AllowedTimes> getConstraintProperty()
    {
        if (constraint == null)
            constraint = new OgcPropertyImpl<AllowedTimes>();
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
    public void setConstraint(AllowedTimes constraint)
    {
        if (this.constraint == null)
            this.constraint = new OgcPropertyImpl<AllowedTimes>();
        this.constraint.setValue(constraint);
    }
    
    
    /**
     * Gets the value property
     */
    @Override
    public IDateTime getValue()
    {
        if (dataBlock == null)
            return null;
        double val = dataBlock.getDoubleValue();
        return new DateTimeDouble(val);
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
    public void setValue(IDateTime dateTime)
    {
        if (dataBlock == null)
            assignNewDataBlock();
        double val = dateTime.getAsDouble();
        dataBlock.setDoubleValue(val);
    }
    
    
    /**
     * Gets the referenceTime property
     */
    @Override
    public IDateTime getReferenceTime()
    {
        return referenceTime;
    }
    
    
    /**
     * Checks if referenceTime is set
     */
    @Override
    public boolean isSetReferenceTime()
    {
        return (referenceTime != null);
    }
    
    
    /**
     * Sets the referenceTime property
     */
    @Override
    public void setReferenceTime(IDateTime referenceTime)
    {
        this.referenceTime = referenceTime;
    }
    
    
    /**
     * Gets the localFrame property
     */
    @Override
    public String getLocalFrame()
    {
        return localFrame;
    }
    
    
    /**
     * Checks if localFrame is set
     */
    @Override
    public boolean isSetLocalFrame()
    {
        return (localFrame != null);
    }
    
    
    /**
     * Sets the localFrame property
     */
    @Override
    public void setLocalFrame(String localFrame)
    {
        this.localFrame = localFrame;
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
            AllowedTimesImpl constraint = (AllowedTimesImpl)this.constraint;            
            if (!constraint.isValid(getValue()))
            {
                String valText;                
                if (Time.ISO_TIME_UNIT.equals(uom.getHref()))
                    valText = DateTimeFormat.formatIso(dataBlock.getDoubleValue(), 0);
                else
                    valText = dataBlock.getStringValue();
                
                errorList.add(new ValidationException(getName(), "Value '" + valText +
                    "' is not valid for component '" + getName() + "': " + constraint.getAssertionMessage()));
            }
        }
    }
    
    
    @Override
    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append(indent);
        text.append("Time");                
        if (dataBlock != null)
        {
            text.append(" = ");
            if (Time.ISO_TIME_UNIT.equals(uom.getHref()))
                text.append(DateTimeFormat.formatIso(dataBlock.getDoubleValue(), 0));
            else
                text.append(dataBlock.getDoubleValue());
        }
        return text.toString();
    }
}
