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
 * Implementation of the base 10 logarithm function for special units
 * such as B, B[SPL], B[V], etc...
 * </p>
 *
 * @author Alex Robin
 * @since Feb 10, 2007
 * */
public class UnitFunctionLog extends UnitFunction
{
    private static final long serialVersionUID = -4017878542967337089L;
    protected boolean eBase;
    protected double logBase;
    
    
    public UnitFunctionLog()
    {
        this.eBase = true;
        this.printSymbol = "ln";
    }
    
    
    public UnitFunctionLog(double logBase)
    {
        this.logBase = logBase;
        this.printSymbol = "log" + Integer.toString((int)logBase);
    }
    
    
    @Override
    public double toProperUnit(double value)
    {
        if (eBase)
            return Math.exp(value);
        else
            return Math.pow(logBase, value*scaleFactor);
    }


    @Override
    public double fromProperUnit(double value)
    {
        if (eBase)
            return Math.log(value) / scaleFactor;
        else if (NumberUtils.ulpEquals(logBase, 10.0))
            return Math.log10(value) / scaleFactor;
        else
            return logN(logBase, value) / scaleFactor;
    }

    
    /**
     * Computes the logarithm base N of the given value
     * @param value
     * @param base
     * @return
     */
    private double logN(double base, double value)
    {
        return Math.log(value)/Math.log(base);
    }


    @Override
    public boolean equals(Object obj)
    {
        return (obj instanceof UnitFunctionLog &&
                this.eBase == ((UnitFunctionLog)obj).eBase &&
                NumberUtils.ulpEquals(this.logBase, ((UnitFunctionLog)obj).logBase));
    }
    
    
    @Override
    public int hashCode()
    {
        return Double.hashCode(logBase) + (eBase ? 31 : 0);
    }
}
