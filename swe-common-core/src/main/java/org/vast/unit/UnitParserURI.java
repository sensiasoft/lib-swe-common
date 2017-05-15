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

import org.vast.swe.SWEConstants;

/**
 * <p>
 * Uses a URN to lookup for a unit definition and create
 * the corresponding Unit object ready for conversion.
 * </p>
 *
 * @author Alex Robin
 * @since May 4, 2006
 * */
public class UnitParserURI implements UnitParser
{
    protected UnitParserUCUM ucumParser = new UnitParserUCUM();
    
    
    @Override
    public Unit getUnit(String uri)
    {
        if (SWEConstants.UOM_ANY.equals(uri))
            return null;        
        
        String ucumCode = getUCUMCode(uri);
        return ucumParser.getUnit(ucumCode);
    }


    /**
     * Map URIs to UCUM codes
     * @param unitURI
     * @return
     */
    protected String getUCUMCode(String unitURI)
    {
        /////////////////
        // angle units //
        /////////////////
        if (unitURI.contains(":radian"))
            return "rad";
        else if (unitURI.contains(":degree"))
            return "deg";

        ////////////////////
        // distance units //
        ////////////////////
        else if (unitURI.contains(":meter"))
            return "m";
        else if (unitURI.contains(":kilometer"))
            return "km";
        else if (unitURI.contains(":centimeter"))
            return "cm";
        else if (unitURI.contains(":millimeter"))
            return "mm";
        else if (unitURI.contains(":micrometer") || unitURI.contains(":micron"))
            return "um";
        else if (unitURI.contains(":nanometer"))
            return "nm";
        else if (unitURI.contains(":feet") || unitURI.contains(":foot"))
            return "[ft_i]";
        else if (unitURI.contains(":inch"))
            return "[in_i]";
        else if (unitURI.contains(":mile"))
            return "[mi_i]";

        /////////////////////////////////////
        // time units - convert to seconds //
        /////////////////////////////////////
        else if (unitURI.contains(":second"))
            return "s";
        else if (unitURI.contains(":minute"))
            return "min";
        else if (unitURI.contains(":hour"))
            return "h";
        else if (unitURI.contains(":day"))
            return "d";
        else if (unitURI.contains(":year"))
            return "a";
        else if (unitURI.contains(":millisecond"))
            return "ms";
        else if (unitURI.contains(":microsecond"))
            return "us";
        else if (unitURI.contains(":nanosecond"))
            return "ns";
        
        // ISO time
        else if (unitURI.contains("8601"))
            return "s";
        
        // TODO delegates to custom unit registry if needed
        
        else return "1";
    }
}
