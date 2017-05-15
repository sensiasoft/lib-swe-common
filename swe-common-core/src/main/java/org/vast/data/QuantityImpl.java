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
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.AllowedValues;
import net.opengis.swe.v20.DataComponentVisitor;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.UnitReference;
import net.opengis.swe.v20.ValidationException;


/**
 * <p>
 * Extended SWE Quantity implementation adapted to old VAST framework
 * </p>
 *
 * @author Alex Robin
 * @since Aug 30, 2014
 */
public class QuantityImpl extends DataValue implements Quantity
{
    private static final long serialVersionUID = -6958705141485869941L;
    protected UnitReferenceImpl uom = new UnitReferenceImpl();
    protected OgcProperty<AllowedValues> constraint;
    
    
    public QuantityImpl()
    {
        this.dataType = DataType.DOUBLE;
    }
    
    
    public QuantityImpl(DataType dataType)
    {
        this.dataType = dataType;
    }
    
    
    @Override
    public QuantityImpl copy()
    {
        QuantityImpl newObj = new QuantityImpl();
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
    public double getValue()
    {
        if (dataBlock == null)
            return Double.NaN;
        return dataBlock.getDoubleValue();
    }
    
    
    /**
     * Sets the value property
     */
    @Override
    public void setValue(double value)
    {
        if (dataBlock == null)
            assignNewDataBlock();
        dataBlock.setDoubleValue(value);
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
            if (!constraint.isValid(getValue()))
            {
                errorList.add(new ValidationException(getName(), "Value '" + dataBlock.getStringValue() +
                        "' is not valid for component '" + getName() + "': " + constraint.getAssertionMessage()));
            }
        }
    }
    
    
    @Override
    public String toString(String indent)
    {
        StringBuilder text = new StringBuilder();
        text.append("Quantity");                
        if (dataBlock != null)
            text.append(" = " + dataBlock.getStringValue());
        return text.toString();
    }


    @Override
    public void accept(DataComponentVisitor visitor)
    {
        visitor.visit(this);
    }
}
