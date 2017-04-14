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


/**
 * <p>
 * Base abstract class for all unit converters
 * </p>
 *
 * @author Alex Robin
 * @since May 4, 2006
 * */
public abstract class AbstractUnitConverter implements UnitConverter
{
    protected Unit srcUnit, destUnit;
    protected boolean conversionNeeded = false;
    protected boolean conversionPossible = false;
    
    
    public AbstractUnitConverter()
    {        
    }
    
    
    public AbstractUnitConverter(Unit sourceUnit, Unit destinationUnit)
    {
        this.srcUnit = sourceUnit;
        this.destUnit = destinationUnit;        
        conversionPossible = sourceUnit.isCompatible(destinationUnit);
    }
    
    
    /**
     * Converts a double value expressed in sourceUnit to
     * the corresponding value expressed in destinationUnit.
     */
    @Override
    public abstract double convert(double value);
    

    public boolean isConversionNeeded()
    {
        return conversionNeeded;
    }
    
    
    public boolean isConversionPossible()
    {
        return conversionPossible;
    }


    public Unit getDestUnit()
    {
        return destUnit;
    }


    public void setDestUnit(Unit destinationUnit)
    {
        this.destUnit = destinationUnit;
    }


    public Unit getSrcUnit()
    {
        return srcUnit;
    }


    public void setSrcUnit(Unit sourceUnit)
    {
        this.srcUnit = sourceUnit;
    }
}
