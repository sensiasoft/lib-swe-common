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

import java.io.Serializable;

/**
 * <p>
 * Abstract class for all functions used to expressed non linear units.
 * Example of functions are log, ln, degF, degC, etc...
 * </p>
 *
 * @author Alex Robin
 * @since Feb 9, 2007
 * */
public abstract class UnitFunction implements Serializable
{
    private static final long serialVersionUID = -8673301679624127006L;
    protected String printSymbol;
    protected double scaleFactor;
    
    
    public static UnitFunction createFunction(String functionName, double scaleFactor)
    {
        UnitFunction func = null;
        
        if ("ln".equalsIgnoreCase(functionName))
        {
            func = new UnitFunctionLog();
            func.setScaleFactor(scaleFactor);
        }
        else if ("ld".equalsIgnoreCase(functionName))
        {
            func = new UnitFunctionLog(2);
            func.setScaleFactor(scaleFactor);
        }
        else if ("lg".equalsIgnoreCase(functionName))
        {
            func = new UnitFunctionLog(10);
            func.setScaleFactor(scaleFactor);
        }
        else if ("2lg".equalsIgnoreCase(functionName))
        {
            func = new UnitFunctionLog(10);
            func.setScaleFactor(0.5 * scaleFactor);
        }
        else if ("cel".equalsIgnoreCase(functionName))
        {
            func = new UnitFunctionOffset(-273.15);
            func.setScaleFactor(scaleFactor);
        }
        else if ("degf".equalsIgnoreCase(functionName))
        {
            func = new UnitFunctionOffset(-9*273.15+32*5);
            func.setScaleFactor(scaleFactor);
        }
        else if ("ph".equalsIgnoreCase(functionName))
        {
            func = new UnitFunctionLog(10);
            func.setScaleFactor(-scaleFactor);
        }
        else if ("hpx".equalsIgnoreCase(functionName))
        {
            func = new UnitFunctionLog(10);
            func.setScaleFactor(-scaleFactor);
        }
        else if ("hpc".equalsIgnoreCase(functionName))
        {
            func = new UnitFunctionLog(10);
            func.setScaleFactor(-scaleFactor);
        }
        
        return func;
    }
    
    
    public abstract double toProperUnit(double value);
    public abstract double fromProperUnit(double value);
    
    @Override
    public abstract boolean equals(Object obj);    
    
    @Override
    public abstract int hashCode();
    

    public double getScaleFactor()
    {
        return scaleFactor;
    }


    public void setScaleFactor(double scaleFactor)
    {
        this.scaleFactor = scaleFactor;
    }


    public String getPrintSymbol()
    {
        return printSymbol;
    }


    public void setPrintSymbol(String printSymbol)
    {
        this.printSymbol = printSymbol;
    }
    
}
