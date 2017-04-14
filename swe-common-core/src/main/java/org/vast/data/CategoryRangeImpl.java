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
import net.opengis.swe.v20.CategoryRange;
import net.opengis.swe.v20.DataComponentVisitor;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.ValidationException;


/**
 * <p>
 * Extended SWE CategoryRange implementation adapted to old VAST framework
 * </p>
 *
 * @author Alex Robin
 * @since Aug 30, 2014
 */
public class CategoryRangeImpl extends AbstractRangeComponentImpl implements CategoryRange
{
    private static final long serialVersionUID = 814746758956938727L;
    protected String codeSpace;
    protected OgcProperty<AllowedTokens> constraint;
    protected String[] tmpValue = new String[2];
    
    
    public CategoryRangeImpl()
    {
        this.dataType = DataType.UTF_STRING;
        this.min = new CategoryImpl();
        this.max = new CategoryImpl();
    }
    
    
    @Override
    public CategoryRangeImpl copy()
    {
        CategoryRangeImpl newObj = new CategoryRangeImpl();
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
    
    
    @Override
    public String getCodeSpace()
    {
        return codeSpace;
    }
    
    
    @Override
    public boolean isSetCodeSpace()
    {
        return (codeSpace != null);
    }
    
    
    @Override
    public void setCodeSpace(String codeSpace)
    {
        this.codeSpace = codeSpace;
    }
    
    
    @Override
    public AllowedTokens getConstraint()
    {
        if (constraint == null)
            return null;
        return constraint.getValue();
    }
    
    
    @Override
    public OgcProperty<AllowedTokens> getConstraintProperty()
    {
        if (constraint == null)
            constraint = new OgcPropertyImpl<AllowedTokens>();
        return constraint;
    }
    
    
    @Override
    public boolean isSetConstraint()
    {
        return (constraint != null && (constraint.hasValue() || constraint.hasHref()));
    }
    
    
    @Override
    public void setConstraint(AllowedTokens constraint)
    {
        if (this.constraint == null)
            this.constraint = new OgcPropertyImpl<AllowedTokens>();
        this.constraint.setValue(constraint);
    }
    
    
    @Override
    public String[] getValue()
    {
        if (dataBlock == null)
            return null;
        tmpValue[0] = dataBlock.getStringValue(0);
        tmpValue[1] = dataBlock.getStringValue(1);
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
    public void setValue(String[] value)
    {
        if (value == null)
        {
            dataBlock = null;
            return;
        }
        
        if (dataBlock == null)
            assignNewDataBlock();
        dataBlock.setStringValue(0, value[0]);
        dataBlock.setStringValue(1, value[1]);
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
            String min = dataBlock.getStringValue(0);
            String max = dataBlock.getStringValue(1);
            
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
        text.append("CategoryRange: ");                
        if (dataBlock != null)
        {
            text.append(" = [");
            text.append(dataBlock.getStringValue(0));
            text.append(' ');
            text.append(dataBlock.getStringValue(1));
            text.append(']');
        }
        text.append("\n");
        return text.toString();
    }


    @Override
    public void accept(DataComponentVisitor visitor)
    {
        visitor.visit(this);
    }
}
