package org.vast.data;

import java.util.List;
import org.vast.cdm.common.CDMException;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.AllowedTokens;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.Text;


/**
 * POJO class for XML type TextType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class TextImpl extends DataValue implements Text
{
    static final long serialVersionUID = 1L;
    protected OgcProperty<AllowedTokens> constraint;
    
    
    public TextImpl()
    {
        this.dataType = DataType.UTF_STRING;
    }
    
    
    @Override
    public TextImpl copy()
    {
        TextImpl newObj = new TextImpl();
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
    public AllowedTokens getConstraint()
    {
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
        return (constraint != null && (constraint.getValue() != null || constraint.hasHref()));
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
    public void setValue(String value)
    {
        if (dataBlock == null)
            assignNewDataBlock();
        dataBlock.setStringValue(value);
    }


    @Override
    public boolean hasConstraints()
    {
        return (constraint != null);
    }
    
    
    @Override
    public void validateData(List<Exception> errorList)
    {
        if (constraint != null)
        {
            // validate value against constraint
            String value = dataBlock.getStringValue();
            List<String> allowedValues = constraint.getValue().getValueList();
            for (String allowedVal: allowedValues)
                if (allowedVal.equals(value))
                    return;
            
            // check against pattern
            // TODO precompile pattern
            String pattern = constraint.getValue().getPattern();
            if (pattern != null && value.matches(pattern))
                return;
            
            // add error if not valid
            errorList.add(new CDMException(getName(), "Value '" + dataBlock.getStringValue() + 
                    "' is not valid for component '" + getName() + "': " + getAssertionMessage()));
        }
    }
    
    
    protected String getAssertionMessage()
    {
        StringBuffer msg = new StringBuffer();
        msg.append("be one of {");
        
        int i = 0;
        List<String> allowedValues = constraint.getValue().getValueList();
        int lastItem = allowedValues.size() - 1; 
        for (String allowedValue: allowedValues)
        {
            msg.append(allowedValue);
            if (i < lastItem)
                msg.append(", ");
        }
        
        msg.append('}');
        return msg.toString();
    }
    
    
    @Override
    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append(indent);
        text.append("Text");                
        if (dataBlock != null)
            text.append(" = " + dataBlock.getStringValue());
        return text.toString();
    }
}
