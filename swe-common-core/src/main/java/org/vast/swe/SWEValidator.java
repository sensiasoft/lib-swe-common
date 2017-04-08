/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe;

import java.util.ArrayList;
import java.util.List;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.BinaryBlock;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.BinaryMember;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.DataType;
import org.vast.cdm.common.CDMException;
import org.vast.data.BinaryComponentImpl;
import org.vast.data.BooleanImpl;
import org.vast.data.CategoryImpl;
import org.vast.data.CountImpl;
import org.vast.data.QuantityImpl;
import org.vast.data.TextImpl;
import org.vast.data.TimeImpl;


/**
 * <p>
 * Validator for SWE Common structures already parsed from XML.
 * This checks things that cannot be validated with XML schema.
 * </p>
 *
 * @author Alex Robin
 * @since Mar 5, 2014
 */
public class SWEValidator
{
    
    public SWEValidator()
    {
        
    }
    
    
    public List<Exception> validateComponent(DataComponent component, List<Exception> errors)
    {
        // TODO implement component validation
        return new ArrayList<Exception>();
    }
    
    
    public List<Exception> validateEncoding(DataEncoding encoding, DataComponent component, List<Exception> errors)
    {
        if (encoding instanceof BinaryEncoding)
            return validateEncoding((BinaryEncoding)encoding, component, errors);
        
        return new ArrayList<Exception>();
    }
    
    
    public List<Exception> validateEncoding(BinaryEncoding encoding, DataComponent component, List<Exception> errors)
    {
        if (errors == null)
            errors = new ArrayList<Exception>();
        
        for (BinaryMember opts: encoding.getMemberList())
        {
            try
            {
                DataComponent childComp = SWEHelper.findComponentByPath(component, opts.getRef());
                String componentPath = opts.getRef();
                
                if (opts instanceof BinaryComponentImpl)
                {
                    DataType dataType = ((BinaryComponentImpl)opts).getCdmDataType();
                    
                    if (childComp instanceof BooleanImpl)
                    {
                        if (!dataType.isIntegralType() && !(dataType == DataType.BOOLEAN))
                            errors.add(new CDMException("Incompatible datatype for Boolean component " + componentPath));
                    }
                    else if (childComp instanceof CountImpl)
                    {
                        if (!dataType.isIntegralType())
                            errors.add(new CDMException("Incompatible datatype for Count component " + componentPath));
                    }
                    else if (childComp instanceof QuantityImpl)
                    {
                        if (!dataType.isNumberType())
                            errors.add(new CDMException("Incompatible datatype for Quantity component " + componentPath));
                    }
                    else if (childComp instanceof CategoryImpl)
                    {
                        if (!dataType.isTextType())
                            errors.add(new CDMException("Incompatible datatype for Category component " + componentPath));
                    }
                    else if (childComp instanceof TextImpl)
                    {
                        if (!dataType.isTextType())
                            errors.add(new CDMException("Incompatible datatype for Text component " + componentPath));
                    }
                    else if (childComp instanceof TimeImpl)
                    {
                        if (!dataType.isNumberType() && !dataType.isTextType())
                            errors.add(new CDMException("Incompatible datatype for Time component " + componentPath));
                    }
                    else if (childComp instanceof DataRecord || childComp instanceof DataArray || childComp instanceof DataChoice)
                    {
                        errors.add(new CDMException("Cannot use scalar binary options for block component " + componentPath));
                    }
                }
                else if (opts instanceof BinaryBlock)
                {
                    if (!(childComp instanceof DataRecord || childComp instanceof DataArray || childComp instanceof DataChoice))
                    {
                        errors.add(new CDMException("Cannot use block binary options for scalar component " + componentPath));
                    }
                }
            }
            catch (Exception e)
            {
                errors.add(e);
            }
        }
        
        return errors;
    }
}
