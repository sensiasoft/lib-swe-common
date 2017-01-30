/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis;

import java.text.ParseException;
import org.vast.util.DateTimeFormat;


/**
 * <p>
 * Base for all bindings, provide utility methods.
 * This class is not thread safe.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Dec 14, 2014
 */
public abstract class AbstractBindings
{
    static String PLUS_INFINITY = "+INF";
    static String MINUS_INFINITY = "-INF";
    static String TIME_NOW = "now";
    
    StringBuilder sb = new StringBuilder();
    DateTimeFormat isoFormat = new DateTimeFormat();
    
            
    public AbstractBindings()
    {
        super();
    }
    
    
    protected boolean getBooleanFromString(String val)
    {
        return Boolean.parseBoolean(val.trim());
    }
    
    
    protected byte getByteFromString(String val)
    {
        return Byte.parseByte(val.trim());
    }
    
    
    protected short getShortFromString(String val)
    {
        return Short.parseShort(val.trim());
    }
    
    
    protected int getIntFromString(String val)
    {
        return Integer.parseInt(val.trim());
    }
    
    
    protected long getLongFromString(String val)
    {
        return Long.parseLong(val.trim());
    }
    
    
    protected float getFloatFromString(String val)
    {
        val = val.trim();
        
        if (val.equals(MINUS_INFINITY))
            return Float.NEGATIVE_INFINITY;
        
        if (val.equals("INF") || val.equals(PLUS_INFINITY))
            return Float.POSITIVE_INFINITY;
            
        return Float.parseFloat(val);
    }
    
    
    protected double getDoubleFromString(String val)
    {
        val = val.trim();
        
        if (val.equals(MINUS_INFINITY))
            return Double.NEGATIVE_INFINITY;
        
        if (val.equals("INF") || val.equals(PLUS_INFINITY))
            return Double.POSITIVE_INFINITY;
            
        return Double.parseDouble(val);
    }
    
    
    protected IDateTime getDateTimeFromString(String val)
    {
        double time;
        
        try { time = getDoubleFromString(val); }
        catch (NumberFormatException e)
        {
            try
            {
                time = isoFormat.parseIso(val.trim());
            }
            catch (ParseException e1)
            {
                throw new RuntimeException("Invalid ISO or decimal date/time", e1);
            }
        }
        
        return new DateTimeDouble(time);      
    }
    
    
    protected double getDurationFromString(String val)
    {
        double duration;
        
        try { duration = getDoubleFromString(val); }
        catch (NumberFormatException e)
        {
            try
            {
                duration = isoFormat.parseIsoPeriod(val.trim());
            }
            catch (ParseException e1)
            {
                throw new RuntimeException("Invalid ISO or decimal duration", e1);
            }
        }
        
        return duration;      
    }
    
    
    protected boolean[] getBooleanArrayFromString(String val)
    {
        String[] items = val.trim().split(" ");
        boolean[] ret = new boolean[items.length];
        for (int i=0; i<items.length; i++)
            ret[i] = getBooleanFromString(items[i]);
        return ret;
    }
    
    
    protected int[] getIntArrayFromString(String val)
    {
        String[] items = val.trim().split(" ");
        int[] ret = new int[items.length];
        for (int i=0; i<items.length; i++)
            ret[i] = getIntFromString(items[i]);
        return ret;
    }
    
    
    protected double[] getDoubleArrayFromString(String val)
    {
        String[] items = val.trim().split(" ");
        double[] ret = new double[items.length];
        for (int i=0; i<items.length; i++)
            ret[i] = getDoubleFromString(items[i]);
        return ret;
    }
    
    
    protected String[] getStringArrayFromString(String val)
    {
        return val.trim().split(" ");
    }
    
    
    protected IDateTime[] getDateTimeArrayFromString(String val)
    {
        String[] items = val.trim().split(" ");
        IDateTime[] ret = new IDateTime[items.length];
        for (int i=0; i<items.length; i++)
            ret[i] = getDateTimeFromString(items[i]);
        return ret;
    }


    protected String getStringValue(Object obj)
    {
        return obj.toString();
    }
    
    
    protected String getStringValue(IDateTime dateTime)
    {
        if (dateTime.isPositiveInfinity())
            return PLUS_INFINITY;
        
        if (dateTime.isNegativeInfinity())
            return MINUS_INFINITY;
        
        if (dateTime.isNow())
            return TIME_NOW;
                    
        return isoFormat.formatIso(dateTime.getAsDouble(), dateTime.getTimeZoneOffset());
    }
    
    
    protected String getIsoDurationString(double val)
    {
        return isoFormat.formatIsoPeriod(val);
    }


    protected String getStringValue(boolean val)
    {
        return Boolean.toString(val);
    }


    protected String getStringValue(byte val)
    {
        return Byte.toString(val);
    }


    protected String getStringValue(short val)
    {
        return Short.toString(val);
    }


    protected String getStringValue(char val)
    {
        return Character.toString(val);
    }


    protected String getStringValue(int val)
    {
        return Integer.toString(val);
    }


    protected String getStringValue(long val)
    {
        return Long.toString(val);
    }


    protected String getStringValue(float val)
    {
        return Float.toString(val);
    }


    protected String getStringValue(double val)
    {
        if (val == Double.NEGATIVE_INFINITY)
            return MINUS_INFINITY;
        
        if (val == Double.POSITIVE_INFINITY)
            return PLUS_INFINITY;
        
        return Double.toString(val);
    }


    protected String getStringValue(boolean[] array)
    {
        sb.setLength(0);
        
        for (boolean val: array) {
            sb.append(getStringValue(val));
            sb.append(' ');
        }
        
        sb.setLength(sb.length()-1);
        return sb.toString();
    }


    protected String getStringValue(byte[] array)
    {
        sb.setLength(0);
        
        for (byte val: array) {
            sb.append(getStringValue(val));
            sb.append(' ');
        }
        
        sb.setLength(sb.length()-1);
        return sb.toString();
    }


    protected String getStringValue(short[] array)
    {
        sb.setLength(0);
        
        for (short val: array) {
            sb.append(getStringValue(val));
            sb.append(' ');
        }
        
        sb.setLength(sb.length()-1);
        return sb.toString();
    }


    protected String getStringValue(char[] array)
    {
        sb.setLength(0);
        
        for (char val: array) {
            sb.append(getStringValue(val));
            sb.append(' ');
        }
        
        sb.setLength(sb.length()-1);
        return sb.toString();
    }


    protected String getStringValue(int[] array)
    {
        sb.setLength(0);
        
        for (int val: array) {
            sb.append(getStringValue(val));
            sb.append(' ');
        }
        
        sb.setLength(sb.length()-1);
        return sb.toString();
    }


    protected String getStringValue(long[] array)
    {
        sb.setLength(0);
        
        for (long val: array) {
            sb.append(getStringValue(val));
            sb.append(' ');
        }
        
        sb.setLength(sb.length()-1);
        return sb.toString();
    }


    protected String getStringValue(float[] array)
    {
        sb.setLength(0);
        
        for (float val: array) {
            sb.append(getStringValue(val));
            sb.append(' ');
        }
        
        sb.setLength(sb.length()-1);
        return sb.toString();
    }


    protected String getStringValue(double[] array)
    {
        sb.setLength(0);
        
        for (double val: array) {
            sb.append(getStringValue(val));
            sb.append(' ');
        }
        
        sb.setLength(sb.length()-1);
        return sb.toString();
    }


    protected String getStringValue(String[] array)
    {
        sb.setLength(0);
        
        for (String val: array) {
            sb.append(val);
            sb.append(' ');
        }
        
        sb.setLength(sb.length()-1);
        return sb.toString();
    }
    
    
    protected String getStringValue(IDateTime[] dateTimeList)
    {
        sb.setLength(0);
        
        for (IDateTime val: dateTimeList) {
            sb.append(getStringValue(val));
            sb.append(' ');
        }
        
        sb.setLength(sb.length()-1);
        return sb.toString();
    }
    
    
    protected String getStringValueAsDoubles(IDateTime[] dateTimeList)
    {
        sb.setLength(0);
        
        for (IDateTime val: dateTimeList) {
            sb.append(getStringValue(val.getAsDouble()));
            sb.append(' ');
        }
        
        sb.setLength(sb.length()-1);
        return sb.toString();
    }
    
    
    protected String trimStringValue(String text)
    {
        if (text == null)
            return null;
        
        return text.trim();
    }
}