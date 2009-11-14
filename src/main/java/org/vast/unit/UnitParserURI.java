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
 * <p><b>Title:</b>
 * Unit Parser URN
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Uses a URN to lookup for a unit definition and create
 * the corresponding Unit object ready for conversion.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date May 4, 2006
 * @version 1.0
 */
public class UnitParserURI implements UnitParser
{
    protected UnitParserUCUM ucumParser = new UnitParserUCUM();
    
    
    public Unit getUnit(String urn)
    {
        String ucumCode = getUCUMCode(urn);
        return ucumParser.getUnit(ucumCode);
    }


    /**
     * Map to URNS to UCUM codes
     * @param unit
     * @return
     */
    protected String getUCUMCode(String unit)
    {
        /////////////////
        // angle units //
        /////////////////
        if (unit.contains(":radian"))
            return "rad";
        else if (unit.contains(":degree"))
            return "deg";

        ////////////////////
        // distance units //
        ////////////////////
        else if (unit.contains(":meter"))
            return "m";
        else if (unit.contains(":kilometer"))
            return "km";
        else if (unit.contains(":centimeter"))
            return "cm";
        else if (unit.contains(":millimeter"))
            return "mm";
        else if (unit.contains(":micrometer") || unit.contains(":micron"))
            return "um";
        else if (unit.contains(":nanometer"))
            return "nm";
        else if (unit.contains(":feet") || unit.contains(":foot"))
            return "[ft_i]";
        else if (unit.contains(":inch"))
            return "[in_i]";
        else if (unit.contains(":mile"))
            return "[mi_i]";

        /////////////////////////////////////
        // time units - convert to seconds //
        /////////////////////////////////////
        else if (unit.contains(":second"))
            return "s";
        else if (unit.contains(":minute"))
            return "min";
        else if (unit.contains(":hour"))
            return "h";
        else if (unit.contains(":day"))
            return "d";
        else if (unit.contains(":year"))
            return "a";
        else if (unit.contains(":millisecond"))
            return "ms";
        else if (unit.contains(":microsecond"))
            return "us";
        else if (unit.contains(":nanosecond"))
            return "ns";
        
        else return "1";
    }
}
