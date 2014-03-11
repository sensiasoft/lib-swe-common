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

package org.vast.sweCommon;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.vast.cdm.common.BinaryBlock;
import org.vast.cdm.common.BinaryComponent;
import org.vast.cdm.common.BinaryEncoding;
import org.vast.cdm.common.BinaryOptions;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataEncoding;
import org.vast.cdm.common.DataType;
import org.vast.data.DataArray;
import org.vast.data.DataChoice;
import org.vast.data.DataComponentHelper;
import org.vast.data.DataGroup;


/**
 * <p>
 * Validator for SWE Common structures already parsed from XML.
 * This checks things that cannot be validated with XML schema.
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Mar 5, 2014
 */
public class SweValidator
{
    
    public SweValidator()
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
        
        for (BinaryOptions opts: encoding.componentEncodings)
        {
            try
            {
                DataComponent childComp = DataComponentHelper.findComponentByPath(opts.componentName, component);                
                QName compQName = (QName)childComp.getProperty(SweConstants.COMP_QNAME);
                        
                if (opts instanceof BinaryComponent)
                {
                    if (compQName != null && compQName.getLocalPart().equals(SweConstants.BOOL_COMPONENT_TAG))
                    {
                        if (!((BinaryComponent) opts).type.isIntegralType() && !(((BinaryComponent) opts).type == DataType.BOOLEAN))
                            errors.add(new CDMException("Incompatible datatype for Boolean component " + opts.componentName));
                    }
                    else if (compQName != null && compQName.getLocalPart().equals(SweConstants.COUNT_COMPONENT_TAG))
                    {
                        if (!((BinaryComponent) opts).type.isIntegralType())
                            errors.add(new CDMException("Incompatible datatype for Count component " + opts.componentName));
                    }
                    else if (compQName != null && compQName.getLocalPart().equals(SweConstants.QUANTITY_COMPONENT_TAG))
                    {
                        if (!((BinaryComponent) opts).type.isNumberType())
                            errors.add(new CDMException("Incompatible datatype for Quantity component " + opts.componentName));
                    }
                    else if (compQName != null && compQName.getLocalPart().equals(SweConstants.CATEGORY_COMPONENT_TAG))
                    {
                        if (!((BinaryComponent) opts).type.isTextType())
                            errors.add(new CDMException("Incompatible datatype for Category component " + opts.componentName));
                    }
                    else if (compQName != null && compQName.getLocalPart().equals(SweConstants.TEXT_COMPONENT_TAG))
                    {
                        if (!((BinaryComponent) opts).type.isTextType())
                            errors.add(new CDMException("Incompatible datatype for Text component " + opts.componentName));
                    }
                    else if (compQName != null && compQName.getLocalPart().equals(SweConstants.TIME_COMPONENT_TAG))
                    {
                        if (!((BinaryComponent) opts).type.isNumberType() && !((BinaryComponent) opts).type.isTextType())
                            errors.add(new CDMException("Incompatible datatype for Time component " + opts.componentName));
                    }
                    else if (childComp instanceof DataGroup || childComp instanceof DataArray || childComp instanceof DataChoice)
                    {
                        errors.add(new CDMException("Cannot use scalar binary options for block component " + opts.componentName));
                    }
                }
                else if (opts instanceof BinaryBlock)
                {
                    if (!(childComp instanceof DataGroup && childComp instanceof DataArray && childComp instanceof DataChoice))
                    {
                        errors.add(new CDMException("Cannot use block binary options for scalar component " + opts.componentName));
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
