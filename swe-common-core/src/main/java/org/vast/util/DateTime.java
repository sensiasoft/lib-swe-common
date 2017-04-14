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

import java.text.ParseException;
import java.util.Date;


/**
 * <p>
 * Extension of java Date to provide julian time output as a double.
 * </p>
 *
 * @author Alex Robin
 * @since Nov 29, 2005
 * */
public class DateTime extends Date
{
	private static final long serialVersionUID = -237455813885095232L;


	public DateTime()
	{
		super();
	}
	
	
	public DateTime(long time)
	{
		super(time);
	}
	
	
	public DateTime(double julianTime)
	{
		super((long)(julianTime*1000));
	}
	
	
	public DateTime(String iso8601)
	{
		try
		{
			this.setTime((long)(new DateTimeFormat().parseIso(iso8601) * 1000));
		}
		catch (ParseException e)
		{
			throw new IllegalArgumentException(e);
		}
	}
	
	
	public double getJulianTime()
	{
		return (this.getTime()) / 1000.0;
	}
	
	
	public String formatIso(int timeZone)
	{
		return new DateTimeFormat().formatIso(getJulianTime(), timeZone);
	}
}
