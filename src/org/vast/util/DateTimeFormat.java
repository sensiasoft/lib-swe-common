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

package org.vast.util;

import java.text.*;
import java.util.*;


/**
 * <p><b>Title:</b><br/>
 * Date Time Formater
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Formatter for Date and Time quantities.
 * Contains helper routines to convert to and from ISO8601
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 29, 2005
 * @version 1.0
 */
public class DateTimeFormat extends SimpleDateFormat
{
	public static final int LOCAL = 255;
	static final long serialVersionUID = 0;	
	
	
	public DateTimeFormat()
	{
		super();
	}
	
	
	public DateTimeFormat(String pattern)
	{
		super(pattern);
	}
	
	
    /**
     * Parses an ISO8601 period string and return the length of the period.
     * @param iso8601
     * @return length of period in seconds 
     * @throws ParseException
     */
    static public double parseIsoPeriod(String iso8601) throws ParseException
    {
        if (iso8601.charAt(0) != 'P')
            return 0;
        
        iso8601.trim();
        
        double periodInSeconds = 0.0;
        StringBuffer numBuffer = new StringBuffer();
        int index = 1;
        boolean time = false;
        
        try
        {
            while (index < iso8601.length())
            {
                char nextChar = iso8601.charAt(index);
                
                switch (nextChar)
                {
                    case 'T':
                        time = true;
                        break;
                
                    case 'Y':
                        int years = Integer.parseInt(numBuffer.toString());
                        periodInSeconds += years * 365 * 24 * 3600;
                        numBuffer.setLength(0);
                        break;
                        
                    case 'M':
                        if (time)
                        {
                            int minutes = Integer.parseInt(numBuffer.toString());
                            periodInSeconds += minutes * 60;
                            numBuffer.setLength(0);
                        }
                        else
                        {
                            int months = Integer.parseInt(numBuffer.toString());
                            periodInSeconds += months * 30 * 24 * 3600;
                            numBuffer.setLength(0);
                        }
                        break;
                        
                    case 'D':
                        int days = Integer.parseInt(numBuffer.toString());
                        periodInSeconds += days * 24 * 3600;
                        numBuffer.setLength(0);
                        break;
                        
                    case 'H':
                        int hours = Integer.parseInt(numBuffer.toString());
                        periodInSeconds += hours * 3600;
                        numBuffer.setLength(0);
                        break;
                        
                    case 'S':
                        int seconds = Integer.parseInt(numBuffer.toString());
                        periodInSeconds += seconds;
                        numBuffer.setLength(0);
                        break;
                        
                    // otherwise it's a number
                    default:
                        numBuffer.append(nextChar);
                        break;
                }
                
                index++;
            }
        }
        catch (NumberFormatException e)
        {
            throw new ParseException("Invalid ISO 8601 period string", index);
        }
        
        return periodInSeconds;
    }
    
    
    /**
     * Parses an ISO8601 data/time string and return the corresponding unix time
     * @param iso8601
     * @return unix/julian time stamp
     * @throws ParseException
     */
	static public double parseIso(String iso8601) throws ParseException
	{
		try
		{
			Calendar calendar = new GregorianCalendar();
			
			int hour = 0;
			int minute = 0;
			int second = 0;
			double secondFraction = 0;
			
			// parse date
			int year = Integer.parseInt(iso8601.substring(0, 4));
			int day = Integer.parseInt(iso8601.substring(8, 10));
			int month = Integer.parseInt(iso8601.substring(5, 7));

			// parse time if present
			if (iso8601.indexOf('T') != -1)
			{
				hour = Integer.parseInt(iso8601.substring(11, 13));
				minute = Integer.parseInt(iso8601.substring(14, 16));
				second = Integer.parseInt(iso8601.substring(17, 19));
			}
			
			// parse time zone if present
			String timeZone = "GMT+0000";
			int zoneIndex;
			int decimalIndex = iso8601.indexOf('.');
			
			if ( ((zoneIndex = iso8601.indexOf('+', 18)) != -1) || ((zoneIndex = iso8601.indexOf('-', 18)) != -1))
			{
				timeZone = "GMT" + iso8601.substring(zoneIndex);
				
				if (decimalIndex != -1)
					secondFraction = Double.parseDouble(iso8601.substring(decimalIndex, zoneIndex));
			}
			else if ((zoneIndex = iso8601.indexOf('Z')) != -1)
			{
				timeZone = "GMT+0000";
				
				if (decimalIndex != -1)
					secondFraction = Double.parseDouble(iso8601.substring(decimalIndex, zoneIndex));
			}
			else
			{
				if (decimalIndex != -1)
					secondFraction = Double.parseDouble(iso8601.substring(decimalIndex));
			}
						
			// set up the calendar
			calendar.setTimeInMillis(0);
			calendar.set(year, month-1, day, hour, minute, second);			
			calendar.setTimeZone(TimeZone.getTimeZone(timeZone));

			return ((double)calendar.getTimeInMillis()) / 1000.0 + secondFraction;
		}
		catch (Exception e)
		{
			throw new ParseException("Invalid ISO 8601 time string", 0);
		}
	}
	
	
	static public String formatIso(double julianTime, int timeZone)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		
		if (timeZone != LOCAL)
		{
			if (timeZone >= 0)
				formatter.setTimeZone(TimeZone.getTimeZone("GMT+" + timeZone));
			else
				formatter.setTimeZone(TimeZone.getTimeZone("GMT" + timeZone));
		}
		
		StringBuffer isoString = new StringBuffer(30);
		
		long time = (long)(julianTime*1e3);
		formatter.format(new Date(time), isoString, new FieldPosition(0));
		
		// add the : in the zone (seems to be XML schema way)
		if (isoString.length() > 24)
			isoString.insert(26, ':');
		
		// replace +00:00 by Z (looks better)
		String zone = isoString.substring(23,29);
		if (zone.equals("+00:00"))
		{
			isoString.setCharAt(23, 'Z');
			isoString.setLength(24);
		}
		
		// remove .000 if no milliseconds
		String millis = isoString.substring(19,23);
		if (millis.equals(".000"))
		{
			isoString.replace(19,23,"");
		}
		
		return isoString.toString();
	}
}
