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
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.DataComponentVisitor;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.ValidationException;


/**
 * <p>
 * Extended SWE Boolean implementation adapted to old VAST framework
 * </p>
 *
 * @author Alex Robin
 * @since Aug 30, 2014
 */
public class BooleanImpl extends DataValue implements Boolean
{
    private static final long serialVersionUID = 1009356953574382554L;


    public BooleanImpl()
    {
        this.dataType = DataType.BOOLEAN;
    }
    
    
    @Override
    public BooleanImpl copy()
    {
        BooleanImpl newObj = new BooleanImpl();
        super.copyTo(newObj);
        return newObj;
    }
    
    
    /**
     * Gets the value property
     */
    @Override
    public boolean getValue()
    {
        if (dataBlock == null)
            return false;
        return dataBlock.getBooleanValue();
    }
    
    
    /**
     * Sets the value property
     */
    @Override
    public void setValue(boolean value)
    {
        if (dataBlock == null)
            assignNewDataBlock();
        dataBlock.setBooleanValue(value);
    }
    
    
    /**
     * Unsets the value property
     */
    @Override
    public void unSetValue()
    {
        dataBlock = null;
    }


    @Override
    public boolean hasConstraints()
    {
        return false;
    }
    
    
    @Override
    public void validateData(List<ValidationException> errorList)
    {        
    }
    
    
    @Override
    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append("Boolean ");                
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
