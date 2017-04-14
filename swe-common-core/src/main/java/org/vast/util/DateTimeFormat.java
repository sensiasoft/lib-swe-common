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

package org.vast.util;

import java.text.*;
import java.util.*;


/**
 * <p>
 * Formatter for Date and Time quantities.
 * Contains helper routines to convert to and from ISO8601
 * </p>
 *
 * @author Alex Robin
 * @since Nov 29, 2005
 * */
public class DateTimeFormat extends SimpleDateFormat
{
    static final long serialVersionUID = 0;
    
    public static final int LOCAL = 255;
	public static final int SECONDS_IN_MINUTE = 60;
	public static final int SECONDS_IN_HOUR = 60 * SECONDS_IN_MINUTE;
	public static final int SECONDS_IN_DAY = 24 * SECONDS_IN_HOUR;
	public static final int SECONDS_IN_MONTH = 30 * SECONDS_IN_DAY;
	public static final int SECONDS_IN_YEAR = 365 * SECONDS_IN_MONTH;
	
	Calendar calendar = new GregorianCalendar();
	
	
	public DateTimeFormat()
	{
		super("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
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
    public double parseIsoPeriod(String iso8601) throws ParseException
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
	public double parseIso(String iso8601) throws ParseException
	{
		try
		{
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

			return (calendar.getTimeInMillis()) / 1000.0 + secondFraction;
		}
		catch (Exception e)
		{
		    ParseException pe = new ParseException("Invalid ISO 8601 time string: " + iso8601, 0);
		    pe.initCause(e);
		    throw pe;
		}
	}
	
	
    /**
     * Formats a period in seconds to ISO8601 time period standard
     * using only seconds
     * @param periodInSeconds
     * @return ISO string representing the period
     */
    public String formatIsoPeriod(double periodInSeconds)
    {
        StringBuffer buf = new StringBuffer(10);
        if (periodInSeconds < 0)
            buf.append('-');
        periodInSeconds = Math.abs(periodInSeconds);
        buf.append("PT");
        buf.append((int)periodInSeconds);
        buf.append('S');
        return buf.toString();
    }
    
    
    /**
     * Formats a period in seconds to ISO8601 period standard
     * starting with the given unit and going down as much as required
     * @param periodInSeconds
     * @param biggestUnit largest unit to use in encoded string (one of Y, M, D, H, m, S)
     * @return ISO string representing the period
     */
    public String formatIsoPeriod(double periodInSeconds, char biggestUnit)
    {
        if (NumberUtils.ulpEquals(periodInSeconds, 0.0))
            return "PT0S";
        
        StringBuffer buf = new StringBuffer(20);
        if (periodInSeconds < 0)
            buf.append('-');
        buf.append('P');
        int totalSeconds = (int)Math.abs(periodInSeconds);
        int secondsLeft = totalSeconds;
        
        if (biggestUnit == 'Y')
        {
            int months = totalSeconds / SECONDS_IN_YEAR;
            if (months > 0)
            {
                buf.append(months);
                buf.append('M');
            }
            secondsLeft = totalSeconds % SECONDS_IN_YEAR;
            biggestUnit = 'M';
        }        
        
        if (biggestUnit == 'M')
        {
            int months = totalSeconds / SECONDS_IN_MONTH;
            if (months > 0)
            {
                buf.append(months);
                buf.append('M');
            }
            secondsLeft = totalSeconds % SECONDS_IN_MONTH;
            biggestUnit = 'D';
        }
        
        if (biggestUnit == 'D')
        {
            int days = totalSeconds / SECONDS_IN_DAY;
            if (days > 0)
            {
                buf.append(days);
                buf.append('D');
            }
            secondsLeft = totalSeconds % SECONDS_IN_DAY;
            biggestUnit = 'H';
        }
        
        if (secondsLeft > 0)
            buf.append('T');
        
        if (biggestUnit == 'H')
        {
            int hours = secondsLeft / SECONDS_IN_HOUR;
            if (hours > 0)
            {
                buf.append(hours);
                buf.append('H');
            }
            secondsLeft = totalSeconds % SECONDS_IN_HOUR;
            biggestUnit = 'm';
        }
        
        if (biggestUnit == 'm')
        {
            int minutes = secondsLeft / SECONDS_IN_MINUTE;
            if (minutes > 0)
            {
                buf.append(minutes);
                buf.append('M');
            }
            secondsLeft = totalSeconds % SECONDS_IN_MINUTE;
        }
        
        if (secondsLeft > 0)
        {
            buf.append(secondsLeft);
            buf.append('S');
        }
        
        return buf.toString();
    }
    
    
    /**
     * Formats a julian time to ISO8601 standard
     * @param julianTime Julian time with 1970 base
     * @param timeZone time zone to use in the ISO string
     * @return ISO string representing the date/time
     */
    public String formatIso(double julianTime, int timeZone)
	{
		if (timeZone != LOCAL)
		{
			if (timeZone >= 0)
				setTimeZone(TimeZone.getTimeZone("GMT+" + timeZone));
			else
				setTimeZone(TimeZone.getTimeZone("GMT" + timeZone));
		}
		
		StringBuffer isoString = new StringBuffer(30);
		
		long time = (long)(julianTime*1e3);
		format(new Date(time), isoString, new FieldPosition(0));
		
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
