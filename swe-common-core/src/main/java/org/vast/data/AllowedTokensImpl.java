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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import net.opengis.swe.v20.AllowedTokens;


/**
 * POJO class for XML type AllowedTokensType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class AllowedTokensImpl extends AbstractSWEImpl implements AllowedTokens
{
    private static final long serialVersionUID = -1524114903160966177L;
    protected ArrayList<String> valueList = new ArrayList<String>();
    protected String pattern;
    protected Pattern compiledPattern;
    
    
    public AllowedTokensImpl()
    {
    }
    
    
    @Override
    public AllowedTokensImpl copy()
    {
        AllowedTokensImpl newObj = new AllowedTokensImpl();
        for (String val: valueList)
            newObj.valueList.add(val);
        newObj.pattern = pattern;
        return newObj;
    }
    
    
    public boolean isValid(String value)
    {
        // validate value against constraint
        for (String allowedVal: valueList)
            if (allowedVal.equals(value))
                return true;
        
        // check against pattern
        if (pattern != null)
        {
            if (compiledPattern == null)
                compiledPattern = Pattern.compile(pattern);                
            if (compiledPattern.matcher(value).matches())
                return true;
        }
        
        return false;
    }
    
    
    protected String getAssertionMessage()
    {
        StringBuffer msg = new StringBuffer();
        msg.append("It should ");
                
        if (!valueList.isEmpty())
        {
            msg.append("be one of {");
            
            int i = 0;
            for (String allowedValue: valueList)
            {
                if (i++ > 0)
                    msg.append(", ");    
                msg.append(allowedValue);
            }
            
            msg.append('}');
        }
        
        if (pattern != null)
        {
            if (!valueList.isEmpty())
                msg.append(" OR ");
                
            msg.append("match the pattern '");
            msg.append(pattern);
            msg.append("'");
        }
        
        return msg.toString();
    }
    
    
    /**
     * Gets the list of value properties
     */
    @Override
    public List<String> getValueList()
    {
        return valueList;
    }
    
    
    /**
     * Returns number of value properties
     */
    @Override
    public int getNumValues()
    {
        if (valueList == null)
            return 0;
        return valueList.size();
    }
    
    
    /**
     * Adds a new value property
     */
    @Override
    public void addValue(String value)
    {
        this.valueList.add(value);
    }
    
    
    /**
     * Gets the pattern property
     */
    @Override
    public String getPattern()
    {
        return pattern;
    }
    
    
    /**
     * Checks if pattern is set
     */
    @Override
    public boolean isSetPattern()
    {
        return (pattern != null);
    }
    
    
    /**
     * Sets the pattern property
     */
    @Override
    public void setPattern(String pattern)
    {
        this.pattern = pattern;
        compiledPattern = null;
    }
}
