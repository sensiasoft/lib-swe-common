/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import java.util.List;
import org.vast.util.Asserts;
import org.vast.util.DateTimeFormat;
import net.opengis.DateTimeDouble;
import net.opengis.IDateTime;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.AllowedTimes;
import net.opengis.swe.v20.DataComponentVisitor;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.TimeRange;
import net.opengis.swe.v20.UnitReference;
import net.opengis.swe.v20.ValidationException;


/**
 * <p>
 * Extended SWE TimeRange implementation adapted to old VAST framework
 * </p>
 *
 * @author Alex Robin
 * @since Aug 30, 2014
 */
public class TimeRangeImpl extends AbstractRangeComponentImpl implements TimeRange
{
    private static final long serialVersionUID = -8005621421004922445L;
    protected UnitReferenceImpl uom = new UnitReferenceImpl();
    protected OgcProperty<AllowedTimes> constraint;
    protected IDateTime referenceTime;
    protected String localFrame;
    protected transient IDateTime[] tmpValue = new IDateTime[2];
    
    
    public TimeRangeImpl()
    {
        this(DataType.DOUBLE);
    }
    
    
    public TimeRangeImpl(DataType dataType)
    {
        this.dataType = dataType;
        this.min = new TimeImpl(dataType);
        this.max = new TimeImpl(dataType);
    }
    
    
    @Override
    public TimeRangeImpl copy()
    {
        TimeRangeImpl newObj = new TimeRangeImpl();        
        super.copyTo(newObj);
        
        if (uom != null)
            newObj.setUom(uom.copy());
        
        if (constraint != null)
            newObj.constraint = constraint.copy();
        
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
        ((Time)this.min).setUom(uom);
        ((Time)this.max).setUom(uom);
    }
    
    
    /**
     * Gets the constraint property
     */
    @Override
    public AllowedTimes getConstraint()
    {
        if (constraint == null)
            return null;
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
     * Sets the value property
     */
    @Override
    public void setValue(IDateTime[] value)
    {
        Asserts.checkNotNull(value, "value");
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
        return isSetConstraint();
    }


    @Override
    public void validateData(List<ValidationException> errorList)
    {
        if (isSetConstraint())
        {
            AllowedTimesImpl constraint = (AllowedTimesImpl)getConstraint();
            IDateTime[] val = getValue();
            
            if (!constraint.isValid(val[0]) || !constraint.isValid(val[1]))
            {
                String minText, maxText;                
                if (isIsoTime())
                {
                    DateTimeFormat isoFormat = new DateTimeFormat();
                    minText = isoFormat.formatIso(dataBlock.getDoubleValue(0), 0);
                    maxText = isoFormat.formatIso(dataBlock.getDoubleValue(1), 0);
                }
                else
                {
                    minText = dataBlock.getStringValue(0);
                    maxText = dataBlock.getStringValue(1);
                }
                
                errorList.add(new ValidationException(getName(), "Value '[" + minText + " " + maxText + "]" +
                        "' is not valid for component '" + getName() + "': " + constraint.getAssertionMessage()));
            }
        }        
    }


    @Override
    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append("TimeRange");                
        if (dataBlock != null)
        {
            text.append(" = [");
            double min = dataBlock.getDoubleValue(0);
            double max = dataBlock.getDoubleValue(1);
            if (isIsoTime())
            {
                DateTimeFormat isoFormat = new DateTimeFormat();
                text.append(isoFormat.formatIso(min, 0)).append(' ');
                text.append(isoFormat.formatIso(max, 0));
            }
            else
            {
                text.append(min).append(' ').append(max);
            }
            text.append(']');
        }
        return text.toString();
    }


    @Override
    public void accept(DataComponentVisitor visitor)
    {
        visitor.visit(this);
    }
    
    
    @Override
    public boolean isIsoTime()
    {
        return Time.ISO_TIME_UNIT.equals(uom.getHref());
    }
}
