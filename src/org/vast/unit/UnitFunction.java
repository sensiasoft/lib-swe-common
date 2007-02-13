/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.unit;


/**
 * <p><b>Title:</b>
 * Unit Function
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Abstract class for all functions used to expressed non linear units.
 * Example of functions are log, ln, degF, degC, etc...
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Feb 9, 2007
 * @version 1.0
 */
public abstract class UnitFunction
{
    protected String printSymbol;
    protected double scaleFactor;
    
    
    public static UnitFunction createFunction(String functionName, double scaleFactor)
    {
        UnitFunction func = null;
        
        if (functionName.equalsIgnoreCase("ln"))
        {
            func = new UnitFunctionLog();
            func.setScaleFactor(scaleFactor);
        }
        else if (functionName.equalsIgnoreCase("ld"))
        {
            func = new UnitFunctionLog(2);
            func.setScaleFactor(scaleFactor);
        }
        else if (functionName.equalsIgnoreCase("lg"))
        {
            func = new UnitFunctionLog(10);
            func.setScaleFactor(scaleFactor);
        }
        else if (functionName.equalsIgnoreCase("2lg"))
        {
            func = new UnitFunctionLog(10);
            func.setScaleFactor(0.5 * scaleFactor);
        }
        else if (functionName.equalsIgnoreCase("cel"))
        {
            func = new UnitFunctionOffset(-273.15);
            func.setScaleFactor(scaleFactor);
        }
        else if (functionName.equalsIgnoreCase("degf"))
        {
            func = new UnitFunctionOffset(-9*273.15+32*5);
            func.setScaleFactor(scaleFactor);
        }
        else if (functionName.equalsIgnoreCase("ph"))
        {
            func = new UnitFunctionLog(10);
            func.setScaleFactor(-scaleFactor);
        }
        else if (functionName.equalsIgnoreCase("hpx"))
        {
            func = new UnitFunctionLog(10);
            func.setScaleFactor(-scaleFactor);
        }
        else if (functionName.equalsIgnoreCase("hpc"))
        {
            func = new UnitFunctionLog(10);
            func.setScaleFactor(-scaleFactor);
        }
        
        return func;
    }
    
    
    public abstract double toProperUnit(double value);
    public abstract double fromProperUnit(double value);
    public abstract boolean equals(Object obj);
    

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
