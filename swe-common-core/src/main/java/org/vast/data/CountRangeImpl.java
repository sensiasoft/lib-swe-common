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
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.AllowedValues;
import net.opengis.swe.v20.CountRange;
import net.opengis.swe.v20.DataComponentVisitor;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.ValidationException;


/**
 * <p>
 * Extended SWE CountRange implementation adapted to old VAST framework
 * </p>
 *
 * @author Alex Robin
 * @since Aug 30, 2014
 */
public class CountRangeImpl extends AbstractRangeComponentImpl implements CountRange
{
    private static final long serialVersionUID = -6977589403453119057L;
    protected OgcProperty<AllowedValues> constraint;
    protected int[] tmpValue = new int[2];
    
    
    public CountRangeImpl()
    {
        this(DataType.INT);
    }
    
    
    public CountRangeImpl(DataType dataType)
    {
        this.dataType = dataType;
        this.min = new CountImpl(dataType);
        this.max = new CountImpl(dataType);
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
        if (constraint == null)
            return null;
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
     * Sets the value property
     */
    @Override
    public void setValue(int[] value)
    {
        Asserts.checkNotNull(value, "value");
        if (dataBlock == null)
            assignNewDataBlock();
        dataBlock.setIntValue(0, value[0]);
        dataBlock.setIntValue(1, value[1]);
    }


    @Override
    public boolean hasConstraints()
    {
        return isSetConstraint();
    }


    @Override
    public void validateData(List<ValidationException> errorList)
    {
        if (isSetConstraint() && isSetValue())
        {
            AllowedValuesImpl constraint = (AllowedValuesImpl)getConstraint();
            int min = dataBlock.getIntValue(0);
            int max = dataBlock.getIntValue(1);
            
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


    @Override
    public void accept(DataComponentVisitor visitor)
    {
        visitor.visit(this);
    }
}
