package org.vast.data;

import java.util.List;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataType;
import org.vast.util.DateTimeFormat;
import net.opengis.DateTimeDouble;
import net.opengis.IDateTime;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.AllowedTimes;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.TimeRange;
import net.opengis.swe.v20.UnitReference;


/**
 * <p>
 * Extended SWE TimeRange implementation adapted to old VAST framework
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Aug 30, 2014
 */
public class TimeRangeImpl extends AbstractRangeComponentImpl implements TimeRange
{
    static final long serialVersionUID = 1L;
    protected UnitReferenceImpl uom;
    protected OgcProperty<AllowedTimes> constraint;
    protected IDateTime referenceTime;
    protected String localFrame;
    protected IDateTime[] tmpValue = new IDateTime[2];
    
    
    public TimeRangeImpl()
    {
        this.dataType = DataType.DOUBLE;
        this.min = new TimeImpl();
        this.max = new TimeImpl();
    }
    
    
    @Override
    public TimeRangeImpl copy()
    {
        TimeRangeImpl newObj = new TimeRangeImpl();        
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
    public IDateTime[] getValue()
    {
        if (dataBlock == null)
            return null;
        tmpValue[0] = new DateTimeDouble(dataBlock.getDoubleValue(0));
        tmpValue[1] = new DateTimeDouble(dataBlock.getDoubleValue(1));
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
    public void setValue(IDateTime[] value)
    {
        if (value == null)
        {
            dataBlock = null;
            return;
        }
        
        if (dataBlock == null)
            assignNewDataBlock();
        dataBlock.setDoubleValue(0, value[0].getAsDouble());
        dataBlock.setDoubleValue(1, value[1].getAsDouble());
    }
    
    
    @Override
    public IDateTime getReferenceTime()
    {
        return referenceTime;
    }
    
    
    @Override
    public boolean isSetReferenceTime()
    {
        return (referenceTime != null);
    }
    
    
    @Override
    public void setReferenceTime(IDateTime referenceTime)
    {
        this.referenceTime = referenceTime;
    }
    
    
    @Override
    public String getLocalFrame()
    {
        return localFrame;
    }
    
    
    @Override
    public boolean isSetLocalFrame()
    {
        return (localFrame != null);
    }
    
    
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
    public void validateData(List<CDMException> errorList)
    {
        if (constraint != null)
        {
            AllowedTimesImpl constraint = (AllowedTimesImpl)this.constraint;
            IDateTime[] val = getValue();
            
            if (!constraint.isValid(val[0]) || !constraint.isValid(val[1]))
            {
                String minText, maxText;                
                if (Time.ISO_TIME_UNIT.equals(uom.getHref()))
                {
                    minText = DateTimeFormat.formatIso(dataBlock.getDoubleValue(0), 0);
                    maxText = DateTimeFormat.formatIso(dataBlock.getDoubleValue(1), 0);
                }
                else
                {
                    minText = dataBlock.getStringValue(0);
                    maxText = dataBlock.getStringValue(1);
                }
                
                errorList.add(new CDMException(getName(), "Value '[" + minText + " " + maxText + "]" +
                        "' is not valid for component '" + getName() + "': " + constraint.getAssertionMessage()));
            }
        }        
    }


    @Override
    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append(indent);
        text.append("TimeRange");                
        if (dataBlock != null)
        {
            text.append(" = [");
            double min = dataBlock.getDoubleValue(0);
            double max = dataBlock.getDoubleValue(1);
            if (Time.ISO_TIME_UNIT.equals(uom.getHref()))
            {
                text.append(DateTimeFormat.formatIso(min, 0)).append(' ');
                text.append(DateTimeFormat.formatIso(max, 0));
            }
            else
            {
                text.append(min).append(' ').append(max);
            }
            text.append(']');
        }
        return text.toString();
    }
}
