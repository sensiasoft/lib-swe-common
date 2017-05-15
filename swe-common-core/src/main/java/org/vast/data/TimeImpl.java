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
import org.vast.util.DateTimeFormat;
import net.opengis.DateTimeDouble;
import net.opengis.IDateTime;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.AllowedTimes;
import net.opengis.swe.v20.DataComponentVisitor;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.UnitReference;
import net.opengis.swe.v20.ValidationException;


/**
 * <p>
 * Extended SWE Time implementation adapted to old VAST framework
 * </p>
 *
 * @author Alex Robin
 * @since Aug 30, 2014
 */
public class TimeImpl extends DataValue implements Time
{
    private static final long serialVersionUID = 2074207595957800332L;
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
    public IDateTime getValue()
    {
        if (dataBlock == null)
            return null;
        double val = dataBlock.getDoubleValue();
        return new DateTimeDouble(val);
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
        return isSetConstraint();
    }
    
    
    @Override
    public void validateData(List<ValidationException> errorList)
    {
        if (isSetConstraint())
        {
            AllowedTimesImpl constraint = (AllowedTimesImpl)getConstraint();            
            if (!constraint.isValid(getValue()))
            {
                String valText;                
                if (isIsoTime())
                    valText = new DateTimeFormat().formatIso(dataBlock.getDoubleValue(), 0);
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
        text.append("Time");                
        if (dataBlock != null)
        {
            text.append(" = ");
            if (isIsoTime())
                text.append(new DateTimeFormat().formatIso(dataBlock.getDoubleValue(), 0));
            else
                text.append(dataBlock.getDoubleValue());
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
