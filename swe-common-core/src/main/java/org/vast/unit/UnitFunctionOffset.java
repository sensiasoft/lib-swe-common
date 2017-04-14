/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.unit;

import org.vast.util.NumberUtils;

/**
 * <p>
 * Implementation of an offset function for special units
 * such as Cel...
 * </p>
 *
 * @author Alex Robin
 * @since Feb 10, 2007
 * */
public class UnitFunctionOffset extends UnitFunction
{
    private static final long serialVersionUID = 3668307227162151405L;
    protected double offset;
    
    
    public UnitFunctionOffset(double offset)
    {
        this.offset = offset;
        this.printSymbol = "offset";
    }
    
    
    @Override
    public double toProperUnit(double value)
    {
        return value*scaleFactor - offset;
    }


    @Override
    public double fromProperUnit(double value)
    {
        return (value + offset) / scaleFactor;
    }
    
    
    @Override
    public boolean equals(Object obj)
    {
        return (obj instanceof UnitFunctionOffset &&
                NumberUtils.ulpEquals(this.offset, ((UnitFunctionOffset)obj).offset));
    }
    
    
    @Override
    public int hashCode()
    {
        return Double.hashCode(offset);
    }
}
