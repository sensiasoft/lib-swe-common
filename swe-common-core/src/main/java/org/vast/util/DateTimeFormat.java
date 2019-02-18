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
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
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
public class DateTimeFormat
{
    public static final DateTimeFormatter ISO_DATE_OR_TIME_FORMAT = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .optionalStart()
            .appendLiteral('T')
            .append(DateTimeFormatter.ISO_LOCAL_TIME)
            .optionalEnd()
            .appendOffsetId()
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter(Locale.US);
    
    
	public DateTimeFormat()
	{
	}
	
	
    /**
     * Parses an ISO8601 period string and return the length of the period.
     * @param iso8601
     * @return length of period in seconds 
     * @throws ParseException
     */
    public double parseIsoPeriod(String iso8601) throws ParseException
    {
        try
        {
            Duration duration = Duration.parse(iso8601);
            return duration.getSeconds() + duration.getNano()/1e9;
        }
        catch (DateTimeParseException e)
        {
            ParseException pe = new ParseException("Invalid ISO 8601 period string", 0);
            pe.initCause(e);
            throw pe;
        }
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
			OffsetDateTime dateTime = OffsetDateTime.parse(iso8601, ISO_DATE_OR_TIME_FORMAT);
			return dateTime.toEpochSecond() + dateTime.getNano()/1e9;
		}
		catch (Exception e)
		{
		    ParseException pe = new ParseException("Invalid ISO 8601 time string: " + iso8601, 0);
		    pe.initCause(e);
		    throw pe;
		}
	}
	
	
    /**
     * Formats a period expressed in seconds to ISO8601 time period standard
     * @param periodInSeconds
     * @return ISO string representing the period
     */
    public String formatIsoPeriod(double periodInSeconds)
    {
        long seconds = (long)periodInSeconds;
        int nanos = (int)((periodInSeconds - seconds)*1E9);
        return Duration.ofSeconds(seconds, nanos).toString();
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
        return formatIsoPeriod(periodInSeconds);
    }
    
    
    /**
     * Formats a julian time to ISO8601 standard
     * @param julianTime Julian time with 1970 base
     * @param timeZone time zone to use in the ISO string
     * @return ISO string representing the date/time
     */
    public String formatIso(double julianTime, int timeZone)
	{
        long epochSeconds = (long)julianTime;
        // improve rounding error by pre-multiplying by 1000
        int nanos = (int)((julianTime*1000. - epochSeconds*1000L)*1e6);
        Instant instant = Instant.ofEpochSecond(epochSeconds, nanos);
        OffsetDateTime dateTime = OffsetDateTime.ofInstant(instant, ZoneOffset.ofHours(timeZone));
		return dateTime.format(DateTimeFormat.ISO_DATE_OR_TIME_FORMAT);
	}
}
