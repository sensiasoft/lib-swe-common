/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe;

import java.text.ParseException;
import org.vast.util.DateTimeFormat;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.ScalarComponent;
import net.opengis.swe.v20.Time;


/**
 * <p>
 * Helper methods to read/write complex data types (e.g. unions)
 * </p>
 *
 * @author Alex Robin
 * @since Dec 14, 2014
 */
public class SWEDataTypeUtils
{
    DateTimeFormat timeFormat = new DateTimeFormat();
    
    
    /**
     * Improves on Java Double.parseDouble() method to include +INF/-INF
     * This is needed because Java Infinity is not allowed by XML schema
     * @param text string to decode value from
     * @return decoded value
     * @throws NumberFormatException if argument doesn't contain a valid double string
     */
    public static final double parseDoubleOrInf(String text) throws NumberFormatException
    {
        double val;
        
        if (text.equals("INF") || text.equals("+INF"))
            val = Double.POSITIVE_INFINITY;
        else if (text.equals("-INF"))
            val = Double.NEGATIVE_INFINITY;
        else
            val = Double.parseDouble(text);
        
        return val;
    }
    
    
    /**
     * @param val
     * @return String representation of double value as per SWE specification
     * (i.e. actually the XML Schema standard representation for infinity)
     */
    public static final String getDoubleOrInfAsString(double val)
    {
        if (val == Double.POSITIVE_INFINITY)
            return "+INF";
        else if (val == Double.NEGATIVE_INFINITY)
            return "-INF";
        else if (Double.isNaN(val))
            return "NaN";
        else
            return Double.toString(val);
    }
    
    
    /**
     * Allows parsing a double or ISO encoded date/time value
     * @param text string to decode value from
     * @return decoded value
     * @throws NumberFormatException 
     */
    public final double parseDoubleOrInfOrIsoTime(String text) throws NumberFormatException
    {
        double val;
        
        try
        {
            val = parseDoubleOrInf(text);
        }
        catch (NumberFormatException e)
        {
            try
            {
                val = timeFormat.parseIso(text);
            }
            catch (ParseException e1)
            {
                if (text.equals("NO_DATA"))
                    return Double.NaN;
                else
                    throw e;
            } 
        }
        
        return val;
    }
    
    
    public final double parseIsoTime(String text) throws ParseException
    {
        return timeFormat.parseIso(text);
    }
    
    
    public final String getDoubleOrTimeAsString(double val, boolean useIso)
    {
        if (val == Double.POSITIVE_INFINITY)
            return "+INF";
        else if (val == Double.NEGATIVE_INFINITY)
            return "-INF";
        else if (useIso)
            return timeFormat.formatIso(val, 0);
        else
            return Double.toString(val);
    }
    
    
    /**
     * Retrieve string representation of value of component
     * This will convert to an ISO string for appropriate time components
     * @param component
     * @return string representation of component value
     */
    public final String getStringValue(ScalarComponent component)
    {   
        if (!component.hasData())
            return null;
        
        DataBlock data = component.getData();
        DataType dataType = data.getDataType();
        String val;
        
        // case of time component
        String uom = null;
        if (component instanceof Time)
            uom = ((Time)component).getUom().getHref();
                
        if (uom != null && uom.equals(Time.ISO_TIME_UNIT))
            val = getDoubleOrTimeAsString(data.getDoubleValue(), true);
        else if (dataType == DataType.DOUBLE || dataType == DataType.FLOAT)
            val = getDoubleOrTimeAsString(data.getDoubleValue(), false);
        else
            val = data.getStringValue();
        
        return val;
    }
}
