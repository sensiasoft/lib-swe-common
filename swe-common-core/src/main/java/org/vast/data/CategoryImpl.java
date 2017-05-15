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
import net.opengis.swe.v20.AllowedTokens;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.DataComponentVisitor;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.ValidationException;


/**
 * <p>
 * Extended SWE Category implementation adapted to old VAST framework
 * </p>
 *
 * @author Alex Robin
 * @since Aug 30, 2014
 */
public class CategoryImpl extends DataValue implements Category
{
    private static final long serialVersionUID = -8819399062433113779L;
    protected String codeSpace;
    protected OgcProperty<AllowedTokens> constraint;
    
    
    public CategoryImpl()
    {
        this.dataType = DataType.UTF_STRING;
    }
    
    
    @Override
    public CategoryImpl copy()
    {
        CategoryImpl newObj = new CategoryImpl();
        super.copyTo(newObj);
        
        if (codeSpace != null)
            newObj.codeSpace = codeSpace;
        else
            newObj.codeSpace = null;
        
        if (constraint != null)
            newObj.constraint = constraint.copy();
        else
            newObj.constraint = null;
        
        return newObj;
    }
    
    
    /**
     * Gets the codeSpace property
     */
    @Override
    public String getCodeSpace()
    {
        return codeSpace;
    }
    
    
    /**
     * Checks if codeSpace is set
     */
    @Override
    public boolean isSetCodeSpace()
    {
        return (codeSpace != null);
    }
    
    
    /**
     * Sets the codeSpace property
     */
    @Override
    public void setCodeSpace(String codeSpace)
    {
        this.codeSpace = codeSpace;
    }
    
    
    /**
     * Gets the constraint property
     */
    @Override
    public AllowedTokens getConstraint()
    {
        if (constraint == null)
            return null;
        return constraint.getValue();
    }
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the constraint property
     */
    @Override
    public OgcProperty<AllowedTokens> getConstraintProperty()
    {
        if (constraint == null)
            constraint = new OgcPropertyImpl<AllowedTokens>();
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
    public void setConstraint(AllowedTokens constraint)
    {
        if (this.constraint == null)
            this.constraint = new OgcPropertyImpl<AllowedTokens>();
        this.constraint.setValue(constraint);
    }
    
    
    /**
     * Gets the value property
     */
    @Override
    public String getValue()
    {
        if (dataBlock == null)
            return null;
        return dataBlock.getStringValue();
    }
    
    
    /**
     * Sets the value property
     */
    @Override
    public void setValue(String value)
    {
        if (dataBlock == null)
            assignNewDataBlock();
        dataBlock.setStringValue(value);
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
            AllowedTokensImpl constraint = (AllowedTokensImpl)getConstraint();            
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
        StringBuffer text = new StringBuffer();
        text.append("Category");                
        if (dataBlock != null)
        {
            text.append(" = ");
            text.append(dataBlock.getStringValue());
        }
        return text.toString();
    }


    @Override
    public void accept(DataComponentVisitor visitor)
    {
        visitor.visit(this);
    }
}
