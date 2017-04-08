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
 *  * <p>
 * Can convert anyone unit to another by using a single scale factor
 * and custom functions.
 * </p>
 *
 * @author Alex Robin
 * @since May 4, 2006
 * */
public class GenericUnitConverter extends AbstractUnitConverter
{
    protected double conversionFactor = 1.0;
    protected boolean noFunctions = true;
    
    
    /**
     * Default constructor using given scalefactor
     * @param conversionFactor
     */
    public GenericUnitConverter(double conversionFactor)
    {
        this.conversionFactor = conversionFactor;
        if (!NumberUtils.ulpEquals(conversionFactor, 1.0))
            conversionNeeded = true;
        conversionPossible = true;
    }
    
    
    /**
     * Default constructor using given units as source and destination
     * @param srcUnit
     * @param destUnit
     */
    public GenericUnitConverter(Unit srcUnit, Unit destUnit)
	{
        super(srcUnit, destUnit);
        
        if (conversionPossible)
        {
            computeConversionFactor();
            
            if (!NumberUtils.ulpEquals(conversionFactor, 1.0))
                conversionNeeded = true;
            
            // if src or dest or both have functions            
            if (srcUnit.function != null || destUnit.function != null)
            {
                noFunctions = false;
                conversionNeeded = true;
                
                if (srcUnit.function != null && destUnit.function != null)
                    if (srcUnit.function.equals(destUnit.function))
                        conversionNeeded = false;
            }
        }
	}
    
    
    private void computeConversionFactor()
    {
        double srcFactor = srcUnit.getScaleToSI();
        double destFactor = destUnit.getScaleToSI();
        conversionFactor = srcFactor / destFactor;
    }
	
	
    @Override
	public double convert(double value)
	{
		if (!conversionPossible)
            throw new IllegalStateException("Units are not compatible: Conversion is impossible");
        
        if (conversionNeeded)
        {
			if (noFunctions)
            {
			    // simply use precomputed factor
                return value * conversionFactor;
            }
            else
            {
                double tempVal = value;
                
                // apply source scale and function -> converts to SI                                
                if (srcUnit.function != null)
                    tempVal = srcUnit.function.toProperUnit(tempVal);
                tempVal = tempVal * srcUnit.getScaleToSI();
                
                // apply dest function and scale -> converts from SI
                tempVal = tempVal / destUnit.getScaleToSI();
                if (destUnit.function != null)
                    tempVal = destUnit.function.fromProperUnit(tempVal);                
                
                return tempVal;
            }
        }
        else
            return value;
	}
}
