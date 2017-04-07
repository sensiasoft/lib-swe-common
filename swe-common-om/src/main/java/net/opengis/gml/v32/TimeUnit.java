/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32;



/**
 * POJO class for XML type TimeUnitType(@http://www.opengis.net/gml/3.2).
 *
 */
@SuppressWarnings("javadoc")
public enum TimeUnit
{
    YEAR("year"),
    MONTH("month"),
    DAY("day"),
    HOUR("hour"),
    MINUTE("minute"),
    SECOND("second");
    
    private final String text;
    
    
    
    /**
     * Private constructor for storing string representation
     */
    private TimeUnit(String s)
    {
        this.text = s;
    }
    
    
    
    /**
     * To convert an enum constant to its String representation
     */
    @Override
    public String toString()
    {
        return text;
    }
    
    
    
    /**
     * To get the enum constant corresponding to the given String representation
     */
    public static TimeUnit fromString(String s)
    {
        if (s.equals("year"))
            return YEAR;
        else if (s.equals("month"))
            return MONTH;
        else if (s.equals("day"))
            return DAY;
        else if (s.equals("hour"))
            return HOUR;
        else if (s.equals("minute"))
            return MINUTE;
        else if (s.equals("second"))
            return SECOND;
        
        throw new IllegalArgumentException("Invalid token " + s + " for enum TimeUnit");
    }
}
