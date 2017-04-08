/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
 Alex Robin <robin@nsstc.uah.edu>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.util;


/**
 * <p>
 * Title: Interval
 * </p>
 *
 * <p>Description:
 * Simple structure for OGC-style interval info.
 * </p>
 *
 * @author Alex Robin
 * @since Sep 21, 2007
 * */
public class Interval
{
	protected double min;
	protected double max;
	protected double resolution;


	public Interval()
	{		
	}
	
	
	public Interval(double min, double max)
	{
		this.min = min;
		this.max = max;
	}
	
	
	public Interval(double min, double max, double res)
	{
		this.min = min;
		this.max = max;
		this.resolution = res;
	}
	
	
	public double getMin()
	{
		return min;
	}


	public void setMin(double min)
	{
		this.min = min;
	}


	public double getMax()
	{
		return max;
	}


	public void setMax(double max)
	{
		this.max = max;
	}


	public double getResolution()
	{
		return resolution;
	}


	public void setResolution(double res)
	{
		this.resolution = res;
	}
	
	
	public Interval copy()
	{
		Interval interval = new Interval();
		interval.min = this.min;
		interval.max = this.max;
		interval.resolution = this.resolution;
		return interval;
	}
}
