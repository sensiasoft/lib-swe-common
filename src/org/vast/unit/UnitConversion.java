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

import java.util.Hashtable;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.vast.process.ProcessException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p><b>Title:</b>
 * Unit Conversion Object
 * </p>
 *
 * <p><b>Description:</b><br/>
 * This object contains routines to create UnitConverters and
 * manage a local dictionary of units.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date May 4, 2006
 * @version 1.0
 */
public class UnitConversion
{
    protected static Hashtable<String, Unit> urnToUnitMap = null;
    protected static Hashtable<String, Unit> codeToUnitMap = null;
        
    
    /**
     * Helper mehod to get the converter directly from the uom string
     * @param uom
     * @return
     */
    public static UnitConverter createConverterToSI(String uom)
    {
        return new ScaleUnitConverter(getFactorToSI(uom));
    }
    
    
    /**
     * Main static method to create the right converter given
     * the specified source and destination units.
     * @param sourceUnit
     * @param destUnit
     * @return
     */
    public static UnitConverter getConverter(Unit sourceUnit, Unit destUnit)
    {
        // create a log converter if one of the units is on a log scale
        if (sourceUnit.logScale != 0 || destUnit.logScale != 0)
            return new LogUnitConverter(sourceUnit, destUnit);
        
        // create a linear converter if units contains offsets
        if (needsOffset(sourceUnit, destUnit))
            return new LinearUnitConverter(sourceUnit, destUnit);
        
        // otherwise default to a simple scale converter
        return new ScaleUnitConverter(sourceUnit, destUnit);
    }
    
    
    /**
     * Checks if offsets to SI base units are different in
     * source and destination units.
     * @param srcUnit
     * @param destUnit
     * @return
     */
    private static boolean needsOffset(Unit srcUnit, Unit destUnit)
    {
        // TODO
        return false;
    }
    
	
	/**
     * Gives the conversion factor from the given unit to SI
     * @param String unit unit URI
     * @return the conversion factor (double)
     * @throws SMLReaderException
     */
    public static double getFactorToSI(String unit)
    {
        // if no unit use default unit
        if (unit == null) return 1.0;

        //////////////////////////////////////
        // angle units - convert to radians //
        //////////////////////////////////////
        else if (unit.endsWith(":radian")) return 1.0;
        else if (unit.equals("rad")) return 1.0;
        
        else if (unit.endsWith(":degree")) return Math.PI/180.0;
        else if (unit.equals("deg")) return Math.PI/180.0;

        ////////////////////////////////////////
        // distance units - convert to meters //
        ////////////////////////////////////////
        else if (unit.contains(":meter")) return 1.0;
        else if (unit.equals("m")) return 1.0;
        
        else if (unit.contains(":kilometer")) return 1e3;
        else if (unit.equals("km")) return 1e3;
        
        else if (unit.contains(":centimeter")) return 1e-2;
        else if (unit.equals("cm")) return 1e-2;
        
        else if (unit.contains(":millimeter")) return 1e-3;
        else if (unit.equals("mm")) return 1e-3;
        
        else if (unit.contains(":micrometer")) return 1e-6;
        else if (unit.contains(":micron")) return 1e-6;
        else if (unit.equals("um")) return 1e-6;
        
        else if (unit.contains(":nanometer")) return 1e-9;
        else if (unit.equals("nm")) return 1e-9;       
        
        else if (unit.contains(":feet")) return 0.3048;
        else if (unit.contains(":foot")) return 0.3048;
        else if (unit.equals("ft")) return 0.3048;
        
        else if (unit.contains(":inch")) return 0.0254;
        else if (unit.equals("in")) return 0.0254;
        
        else if (unit.contains(":mile")) return 1609.344;

        /////////////////////////////////////
        // time units - convert to seconds //
        /////////////////////////////////////
        else if (unit.contains(":second")) return 1.0;
        else if (unit.equals("s")) return 1.0;
        
        else if (unit.contains(":minute")) return 60;
        else if (unit.equals("min")) return 60;
        
        else if (unit.contains(":hour")) return 3600;
        else if (unit.equals("h")) return 3600;
        
        else if (unit.contains(":day")) return 3600*24;
        else if (unit.contains(":year")) return 3600*24*365.25;
        
        else if (unit.contains(":millisecond")) return 1e-3;
        else if (unit.equals("ms")) return 1e-3;
        
        else if (unit.contains(":microsecond")) return 1e-6;
        else if (unit.equals("us")) return 1e-6;
        
        else if (unit.contains(":nanosecond")) return 1e-9;
        else if (unit.equals("ns")) return 1e-9;
        
        // if unknown unit use 1.0
        else return 1.0;
    }
    
    
    /**
     * Loads the URI to Unit Class map using the provided XML file.
     * Existing entries are replaced only if the replace argument is true.
     * @param unitFileUrl
     * @param replace
     * @throws SMLException
     */
    public static synchronized void loadMaps(String unitFileUrl, boolean replace) throws ProcessException
    {
        try
        {
            // create unit hashtable entries
            DOMHelper dom = new DOMHelper(unitFileUrl, false);
            NodeList unitElts = dom.getElements("Unit");
            
            if (urnToUnitMap == null)
                urnToUnitMap = new Hashtable<String,Unit>(unitElts.getLength());
            
            for (int i=0; i<unitElts.getLength(); i++)
            {
                Element unitElt = (Element)unitElts.item(i);
                
                // create the unit object
                Unit unit = new Unit();
                unit.setName(dom.getElementValue(unitElt, "name"));
                unit.setSymbol(dom.getElementValue(unitElt, "symbol"));
                
                // adds all uri as keys in the hashtable
                NodeList uriElts = dom.getElements(unitElt, "uri");         
                for (int j=0; j<uriElts.getLength(); j++)
                {
                    Element uriElt = (Element)uriElts.item(j);
                    String uri = dom.getElementValue(uriElt, "");                    
                    
                    if (replace || (urnToUnitMap.get(uri) == null))
                        urnToUnitMap.put(uri, unit);
                }
                
                // also add the symbol entry in the hash table for faster lookup
                urnToUnitMap.put(unit.getSymbol(), unit);
            }
        }
        catch (DOMHelperException e)
        {
            throw new ProcessException("Error while reading Unit Map File", e);
        }
    }
}
