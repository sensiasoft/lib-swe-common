/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.util;

import java.util.regex.Pattern;

/**
 * <p>
 * Utility math functions
 * </p>
 *
 * @author Alex Robin
 * @since Apr 6, 2017
 */
public class NumberUtils
{
    static final Pattern NUMBER_REGEX = Pattern.compile("^[+-]?(\\d+(\\.\\d+)?|\\.\\d+)$");
    static final Pattern NUMBER_REGEX_EXP = Pattern.compile("^[+-]?(\\d+(\\.\\d+)?|\\.\\d+)([eE][+-]?\\d+)?$");
    
    
    private NumberUtils() {};
    
    
    /**
     * Checks that float arguments are within one ULP of each other
     * @param f1
     * @param f2
     * @return true if both numbers are within 1 ULP, false otherwise
     */
    public static boolean ulpEquals(float f1, float f2)
    {
        // handle special cases with NaN and infinity
        if (Float.isNaN(f1) && Float.isNaN(f2))
            return true;        
        if (Float.isInfinite(f1) || Float.isInfinite(f2))
            return f1 == f2;
        
        float max = Math.max(Math.abs(f1), Math.abs(f2));
        return Math.abs(f1 - f2) <= Math.ulp(max);
    }
    
    
    /**
     * Checks that double arguments are within one ULP of each other
     * @param d1
     * @param d2
     * @return true if both numbers are within 1 ULP of each other, false otherwise
     */
    public static boolean ulpEquals(double d1, double d2)
    {
        // handle special cases with NaN and infinity
        if (Double.isNaN(d1) && Double.isNaN(d2))
            return true;        
        if (Double.isInfinite(d1) || Double.isInfinite(d2))
            return d1 == d2;
        
        double max = Math.max(Math.abs(d1), Math.abs(d2));
        return Math.abs(d1 - d2) <= Math.ulp(max);
    }
    
    
    /**
     * Exact float equality check
     * @param f1
     * @param f2
     * @return true if float arguments have the same exact binary value
     */
    public static boolean exactEquals(float f1, float f2)
    {
        return Float.compare(f1, f2) == 0;
    }
    
    
    /**
     * Exact double equality check
     * @param d1
     * @param d2
     * @return true if float arguments have the same exact binary value
     */
    public static boolean exactEquals(double d1, double d2)
    {
        return Double.compare(d1, d2) == 0;
    }
    
    
    /**
     * Checks that text is a number
     * @param text
     * @return true if the string is a valid base 10 integer or decimal
     * number w/o exponent
     */
    public static boolean isNumeric(String text)
    {
        return NUMBER_REGEX.matcher(text).matches();
    }
    
    
    /**
     * Checks that text is a number allowing for exponent
     * @param text
     * @return true if the string is a valid base 10 integer or decimal
     * number, optionally with exponent notation
     */
    public static boolean isNumericExp(String text)
    {
        return NUMBER_REGEX_EXP.matcher(text).matches();
    }
    
}
