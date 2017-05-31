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
import java.util.Objects;
import org.vast.util.NumberUtils;

/**
 * <p>
 * Unit object containing relationship to SI base units.
 * </p>
 *
 * @author Alex Robin
 * @since May 4, 2006
 * */
public class Unit implements Serializable
{
    private static final long serialVersionUID = -5219489147804299847L;
    
    protected String name;
    protected String code;
    protected String printSymbol;
    protected String property;
    protected String description;
    protected String expression;
    
    // base unit powers
    protected double meter = 0.0;
    protected double kilogram = 0.0;
    protected double second = 0.0;
    protected double ampere = 0.0;
    protected double kelvin = 0.0;
    protected double mole = 0.0;
    protected double candela = 0.0;
    protected double radian = 0.0;
    protected double pi = 0.0;
    
    // function for special units
    protected double scaleToSI = 1.0;
    protected boolean metric = false;
    protected UnitFunction function;
    

    /**
     * Copies this unit integrally
     * @return a copy of this unit
     */
    public Unit copy()
    {
        Unit newUnit = new Unit();
        
        newUnit.name = this.name;
        newUnit.code = this.code;        
        newUnit.printSymbol = this.printSymbol;
        newUnit.property = this.property;
        newUnit.description = this.description;
        newUnit.expression = this.expression;
        
        newUnit.meter = this.meter;
        newUnit.kilogram = this.kilogram;
        newUnit.second = this.second;
        newUnit.radian = this.radian;
        newUnit.ampere = this.ampere;
        newUnit.kelvin = this.kelvin;
        newUnit.mole = this.mole;
        newUnit.candela = this.candela;
        newUnit.pi = this.pi;
        newUnit.scaleToSI = this.scaleToSI;
        newUnit.metric = this.metric;
        newUnit.function = this.function;
        
        return newUnit;
    }
    
    
    /**
     * Checks that this unit is physically compatible with the
     * given unit, which also means that it is possible to
     * convert from one to the other.
     * @param unit
     * @return true if unit is compatible with this unit
     */
    public boolean isCompatible(Unit unit)
    {
        // check that powers are equal for all SI base units
        return (NumberUtils.ulpEquals(this.meter, unit.meter) &&
                NumberUtils.ulpEquals(this.kilogram, unit.kilogram) &&
                NumberUtils.ulpEquals(this.second, unit.second) &&
                NumberUtils.ulpEquals(this.radian, unit.radian) &&
                NumberUtils.ulpEquals(this.ampere, unit.ampere) &&
                NumberUtils.ulpEquals(this.kelvin, unit.kelvin) &&
                NumberUtils.ulpEquals(this.mole, unit.mole) &&
                NumberUtils.ulpEquals(this.candela, unit.candela));
    }
    
    
    /**
     * @param unit
     * @return true if this unit is equivalent to this unit
     */
    public boolean isEquivalent(Unit unit)
    {
        return (isCompatible(unit) &&
                NumberUtils.ulpEquals(this.scaleToSI, unit.scaleToSI) &&
                NumberUtils.ulpEquals(this.pi, unit.pi) &&
                Objects.equals(this.function, unit.function));
    }
    
    
    /**
     * Raises this unit to the given power, thus modifying
     * all powers of metric base SI coefs.
     * @param power
     */
    public void power(double power)
    {
        this.meter *= power;
        this.kilogram *= power;
        this.second *= power;
        this.radian *= power;
        this.ampere *= power;
        this.kelvin *= power;
        this.mole *= power;
        this.candela *= power;
        this.pi *= power;        
        this.scaleToSI = Math.pow(this.scaleToSI, power);
    }
    
    
    /**
     * Multiply this unit by another unit to generate a complex unit
     * @param unit
     */
    public void multiply(Unit unit)
    {
        this.meter += unit.meter;
        this.kilogram += unit.kilogram;
        this.second += unit.second;
        this.radian += unit.radian;
        this.ampere += unit.ampere;
        this.kelvin += unit.kelvin;
        this.mole += unit.mole;
        this.candela += unit.candela;
        this.pi += unit.pi;
        this.scaleToSI *= unit.scaleToSI;
        if (unit.function != null)
            this.function = unit.function;
    }
    
    
    public Unit getCompatibleSIUnit()
    {
        Unit siUnit = this.copy();
        siUnit.scaleToSI = 1.0;
        siUnit.expression = getUCUMExpression();
        siUnit.code = null;
        siUnit.name = null;
        siUnit.description = null;
        siUnit.function = null;        
        return siUnit;
    }
    
    
    public void multiply(double scale)
    {
        this.scaleToSI *= scale;
    }
    
    
    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }
    
    
    public String getCode()
    {
        return code;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


	/**
	* @return the print symbol of this Unit. If the print symbol is not set 
	* (i.e. null or empty), then the expression will be returned (if that is
	* not null and not empty).
	*/
	public String getPrintSymbol()
	{
        // use printSymbol if set
		if (printSymbol != null)
			return printSymbol;
		
		// try to use code
		if (code != null)
		{
			// handle special case of weird unit codes
			if (code.equals("KiBy"))
				printSymbol = "KB";
			
			else if (code.equals("MiBy"))
				printSymbol = "MB";
			
			else if (code.equals("GiBy"))
				printSymbol = "GB";
		
			else if (code.equals("Cel"))
				printSymbol = "°C";
		}
        
		// try to use expression
		else if (expression != null)
			printSymbol = getExpression();
        
        return printSymbol;
    }


    public void setPrintSymbol(String symbol)
    {
        this.printSymbol = symbol;
    }
    
    
    public String getExpression()
    {
        if (expression == null)
        	expression = getUCUMExpression();
        
    	return expression;
    }


    public void setExpression(String expression)
    {
        this.expression = expression;
    }


    public String getProperty()
    {
        return property;
    }


    public void setProperty(String property)
    {
        this.property = property;
    }
    
    
    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public boolean isMetric()
    {
        return metric;
    }


    public void setMetric(boolean metric)
    {
        this.metric = metric;
    }


    public double getAmpere()
    {
        return ampere;
    }


    public void setAmpere(double ampere)
    {
        this.ampere = ampere;
    }


    public double getCandela()
    {
        return candela;
    }


    public void setCandela(double candela)
    {
        this.candela = candela;
    }


    public double getKelvin()
    {
        return kelvin;
    }


    public void setKelvin(double kelvin)
    {
        this.kelvin = kelvin;
    }


    public double getKilogram()
    {
        return kilogram;
    }


    public void setKilogram(double kilogram)
    {
        this.kilogram = kilogram;
    }


    public double getMeter()
    {
        return meter;
    }


    public void setMeter(double meter)
    {
        this.meter = meter;
    }


    public double getMole()
    {
        return mole;
    }


    public void setMole(double mole)
    {
        this.mole = mole;
    }


    public double getPi()
    {
        return pi;
    }


    public void setPi(double pi)
    {
        this.pi = pi;
    }


    public double getRadian()
    {
        return radian;
    }


    public void setRadian(double radian)
    {
        this.radian = radian;
    }


    public double getSecond()
    {
        return second;
    }


    public void setSecond(double second)
    {
        this.second = second;
    }
    
    
    public double getScaleToSI()
    {
        return scaleToSI * Math.pow(Math.PI, pi);
    }


    public void setScaleToSI(double scaleToSI)
    {
        this.scaleToSI = scaleToSI;
    }
    
    
    public UnitFunction getFunction()
    {
        return function;
    }


    public void setFunction(UnitFunction function)
    {
        this.function = function;
    }
    
    
    public String getUCUMExpression()
    {
        if (getCode() != null)
            return getCode();
        
        StringBuilder buf = new StringBuilder();
        
        addUnitString(buf, radian, "rad");
        addUnitString(buf, meter, "m");
        addUnitString(buf, kilogram, "kg");
        addUnitString(buf, second, "s");
        addUnitString(buf, ampere, "A");
        addUnitString(buf, kelvin, "K");
        addUnitString(buf, mole, "mol");
        addUnitString(buf, candela, "cd");
        
        if (!NumberUtils.ulpEquals(scaleToSI, 1.0))
            buf.insert(0, getScaleToSI() + "*");
        
        return buf.toString();
    }
    
    
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        
        addUnitString(buf, radian, "rad");
        addUnitString(buf, meter, "m");
        addUnitString(buf, kilogram, "kg");
        addUnitString(buf, second, "s");
        addUnitString(buf, ampere, "A");
        addUnitString(buf, kelvin, "K");
        addUnitString(buf, mole, "mol");
        addUnitString(buf, candela, "cd");
        
        // insert scale factor
        if (!NumberUtils.ulpEquals(scaleToSI, 1.0))
            buf.insert(0, getScaleToSI() + "*");
        
        // insert function symbol
        if (function != null)
        {
            buf.insert(0, 1./function.getScaleFactor() + "*" + function.getPrintSymbol() + "(");
            buf.append(")");
        }
        
        // insert code =
        if (getCode() != null)
            buf.insert(0, getCode() + " = ");            
        
        // also append unit name and printSymbol
        buf.append("  (" + getName() + " - " + getPrintSymbol() + ")");
        
        return buf.toString();
    }
    
    
    private void addUnitString(StringBuilder buf, double val, String sym)
    {
        int ival = (int)val;
        
        if (ival != 0)
        {
            if (buf.length() > 0)
                buf.append('.');
            
            buf.append(sym);
            if (ival != 1)
                buf.append(ival);                        
        }
    }
}
