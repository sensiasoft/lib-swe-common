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
 * <p><b>Title:</b><br/>
 * Linear Unit Converter
 * </p>
 *
 * <p><b>Description:</b><br/>
 * This generic unit converter can apply both a scale and offset
 * on both source and destination units. This will especially 
 * convert units of temperature properly (degF, degC <-> K).
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date May 4, 2006
 * @version 1.0
 */
public class LinearUnitConverter extends AbstractUnitConverter
{
    protected double conversionFactor = 1.0;
	    
	
    /**
     * Default constructor using given units as source and destination
     * @param sourceUnit
     * @param destinationUnit
     */
    public LinearUnitConverter(Unit sourceUnit, Unit destinationUnit)
	{
        super(sourceUnit, destinationUnit);
        
        if (conversionPossible)
        {
            computeConversionFactor(sourceUnit, destinationUnit);
            if (conversionFactor != 1.0)
                conversionNeeded = true;
        }
	}
    
    
    /**
     * If units are compatible, this will compute the conversion
     * factor to go from this unit to the given unit.
     * @param sourceUnit
     * @param destUnit
     * @return
     */
    private void computeConversionFactor(Unit srcUnit, Unit destUnit)
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
			return value * conversionFactor;
		else
			return value;
	}


    public double getConversionFactor()
    {
        return conversionFactor;
    }


    public void setConversionFactor(double conversionFactor)
    {
        this.conversionFactor = conversionFactor;
        this.conversionPossible = true;
        this.conversionNeeded = true;
    }
}
