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
 * This object contains routines to create UnitConverters and
 * manage a local dictionary of units.
 * </p>
 *
 * @author Alex Robin
 * @since May 4, 2006
 * */
public class UnitConversion
{
    private static UnitParserUCUM ucumParser = new UnitParserUCUM();
    private static UnitParserURI uriParser = new UnitParserURI();
    
    
    /**
     * Helper mehod to get the converter directly from the uom string
     * @param uom
     * @return converter instance
     */
    public static UnitConverter createConverterToSI(String uom)
    {
        if (uom == null)
            return new GenericUnitConverter(1.0);
        
        Unit unit = null;
        
        if (uom.startsWith("urn") || uom.startsWith("http"))
            unit = uriParser.getUnit(uom);
        else            
            unit = ucumParser.getUnit(uom);
        
        return new GenericUnitConverter(unit, unit.getCompatibleSIUnit());
    }
    
    
    /**
     * Helper method to create the right converter given
     * the specified source and destination units.
     * @param sourceUnit
     * @param destUnit
     * @return converter instance
     */
    public static UnitConverter getConverter(Unit sourceUnit, Unit destUnit)
    {
        return new GenericUnitConverter(sourceUnit, destUnit);
    }
}
