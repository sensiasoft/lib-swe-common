package org.vast.data;

import java.util.List;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataType;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.AllowedTokens;
import net.opengis.swe.v20.CategoryRange;


/**
 * <p>
 * Extended SWE CategoryRange implementation adapted to old VAST framework
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Aug 30, 2014
 */
public class CategoryRangeImpl extends AbstractRangeComponentImpl implements CategoryRange, HasCodeSpace
{
    static final long serialVersionUID = 1L;
    protected String codeSpace;
    protected OgcProperty<AllowedTokens> constraint;
    protected String[] tmpValue = new String[2];
    
    
    public CategoryRangeImpl()
    {
        this.dataType = DataType.UTF_STRING;
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
        return (constraint != null);
    }
    
    
    @Override
    public void validateData(List<CDMException> errorList)
    {
        if (constraint != null && isSetValue())
        {
            AllowedTokensImpl constraint = (AllowedTokensImpl)this.constraint;
            String min = dataBlock.getStringValue(0);
            String max = dataBlock.getStringValue(1);
            
            if (!constraint.isValid(min) || !constraint.isValid(max))
            {
                errorList.add(new CDMException(name, "Value '[" + min + " " + max + "]" + 
                    "' is not valid for component '" + name + "': " + constraint.getAssertionMessage()));
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
            text.append(" ");
            text.append(dataBlock.getStringValue(1));
            text.append("]");
        }
        text.append("\n");
        return text.toString();
    }
}