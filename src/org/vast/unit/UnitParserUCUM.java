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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p><b>Title:</b>
 * Unit Parser UCUM
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Parses a string containing a UCUM unit definition and create
 * a Unit object with the right scale factors and powers.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date May 4, 2006
 * @version 1.0
 */
public class UnitParserUCUM implements UnitParser
{
	private static Hashtable<String, Double> prefixTable;
    private static String unitRegex = "[./]?[0-9 a-z A-Z \\[ \\] \\- \\+ \\! \\# \\* \\_]+";
    private static String intRegex = "[./]?[-+]?[0-9]+([-+][0-9])?";
    private Hashtable<String, Unit> unitTable = new Hashtable<String, Unit>();
    
    
    static
    {
        prefixTable = new Hashtable<String, Double>();
        
        // load prefix scale factors
        prefixTable.put("Y", 1e24);
        prefixTable.put("Z", 1e21);
        prefixTable.put("E", 1e18);
        prefixTable.put("P", 1e15);
        prefixTable.put("T", 1e12);
        prefixTable.put("G", 1e9);
        prefixTable.put("M", 1e6);
        prefixTable.put("k", 1e3);
        prefixTable.put("h", 1e2);
        prefixTable.put("da", 1e1);
        prefixTable.put("d", 1e-1);
        prefixTable.put("c", 1e-2);
        prefixTable.put("m", 1e-3);
        prefixTable.put("u", 1e-6);
        prefixTable.put("n", 1e-9);
        prefixTable.put("p", 1e-12);
        prefixTable.put("f", 1e-15);
        prefixTable.put("a", 1e-18);
        prefixTable.put("z", 1e-21);
        prefixTable.put("y", 1e-24);
    }
    
    
    public Unit getUnit(String ucumDef)
    {
        Unit uom = new Unit();
        
        // separate tokens between '/' and '.'
        Pattern pattern = Pattern.compile(unitRegex);
        Matcher matcher = pattern.matcher(ucumDef);
        
        // take care of all following tokens
        while (matcher.find())
        {
            // get matching substring
            String token = matcher.group();
            //System.out.println(token);
            
            // detect if integer number
            if (token.matches(intRegex))
                parseNumber(token, uom);
            
            else if (token.contains("10*"))
                parsePower10(token, uom);
            
            else
                parseUnit(token, uom);
        }
        
        return uom;
    }
    
    
    private void parseUnit(String token, Unit uom)
    {
        Unit unit = null;
        double power = 1.0;
        double prefixScale = 1.0;
        
        // extract trailing power
        int expIndex = this.findExponentIndex(token);
        String expString = token.substring(expIndex);
        String unitString = token.substring(0, expIndex);
        if (expString.length() != 0)
            power = Double.parseDouble(expString);
        
        // see if we had '.' or '/'
        if (unitString.charAt(0) == '/')
        {
            power = -power;
            unitString = unitString.substring(1);
        }
        else if ((token.charAt(0) == '.'))
        {
            unitString = unitString.substring(1);
        }
                
        // try all prefixes and see if they match
        boolean prefixFound = false;
        Enumeration<String> prefixList = prefixTable.keys();
        while (prefixList.hasMoreElements())
        {
            String prefix = prefixList.nextElement();
            if (unitString.startsWith(prefix))
            {
                // get remaining of string after prefix
                String unitAtom = unitString.substring(prefix.length());                
                                
                // only if remaining of string also matches a unit
                if (unitTable.containsKey(unitAtom))
                {
                    // get unit from table
                    unit = unitTable.get(unitAtom);
                    
                    // make sure the unit is metric
                    if (!unit.isMetric())
                        continue;
                    
                    // get prefix scale factor
                    prefixScale = prefixTable.get(prefix);                    
                    prefixFound = true;
                    
                    break;
                }
            }
        }
        
        // get unit if no prefix was found
        if (prefixFound)
            unit = unit.copy();
        else
        {
            if (!unitTable.containsKey(unitString))
                throw new IllegalStateException("Unknown unit: " + unitString);
            
            unit = unitTable.get(unitString).copy();
        }
        
        // update unit scale factor
        unit.scaleToSI *= prefixScale;
        
        // raise unit to given power
        if (power != 1.0)
            unit.power(power);
        
        // combine unit with current uom
        uom.multiply(unit);
    }
    
    
    private int findExponentIndex(String token)
    {
        int i = token.length();
        char c;
        
        do
        {
            i--;
            
            if (i < 0)
                break;
            
            c = token.charAt(i);
        }
        while (c == '+' || c == '-' || (c >= '0' && c <= '9'));        
        
        return i+1;
    }
    
    
    private void parsePower10(String token, Unit uom)
    {
        String powString = token.substring(token.indexOf('*')+1); 
        double power = Double.parseDouble(powString);
        
        if (token.charAt(0) == '/')
            uom.scaleToSI /= Math.pow(10, power);
        else
            uom.scaleToSI *= Math.pow(10, power);        
    }
    
    
    private void parseNumber(String token, Unit uom)
    {
        double val;
        
        if (token.charAt(0) == '/')
            val = 1.0 / Double.parseDouble(token.substring(1));            
        else if ((token.charAt(0) == '.'))
            val = Double.parseDouble(token.substring(1));
        else
            val = Double.parseDouble(token);           
        
        uom.scaleToSI *= val;
    }


    public Hashtable<String, Unit> getUnitTable()
    {
        return unitTable;
    }
    
    
    public void preloadSIUnits()
    {
        Unit unit;
        
        // PI
        unit = new Unit();
        unit.setPi(1.0);
        unitTable.put("[pi]", unit);
        
        // meter
        unit = new Unit();
        unit.setMeter(1.0);
        unit.setMetric(true);
        unitTable.put("m", unit);
        
        // gram
        unit = new Unit();
        unit.setKilogram(1.0);
        unit.setScaleToSI(1e-3);
        unit.setMetric(true);
        unitTable.put("g", unit);
        
        // second
        unit = new Unit();
        unit.setSecond(1.0);
        unit.setMetric(true);
        unitTable.put("s", unit);
        
        // Kelvin
        unit = new Unit();
        unit.setKelvin(1.0);
        unit.setMetric(true);
        unitTable.put("K", unit);
        
        // Candela
        unit = new Unit();
        unit.setCandela(1.0);
        unit.setMetric(true);
        unitTable.put("cd", unit);
        
        // Ampere
        unit = new Unit();
        unit.setAmpere(1.0);
        unit.setMetric(true);
        unitTable.put("A", unit);
        
        // Radian
        unit = new Unit();
        unit.setRadian(1.0);
        unit.setMetric(true);
        unitTable.put("rad", unit);
        
        // Mole
        unit = new Unit();
        unit.setMole(1.0);
        unit.setMetric(true);
        unitTable.put("mol", unit);
        
        // Coulomb
        unit = new Unit();
        unit.setAmpere(1.0);
        unit.setSecond(1.0);
        unit.setMetric(true);
        unitTable.put("C", unit);
    }
}
