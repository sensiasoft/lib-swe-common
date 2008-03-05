/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.

 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/
package org.vast.sweCommon;

import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataBlock;
import org.vast.cdm.common.DataConstraint;

/**
 * <p><b>Title:</b><br/>
 * IntervalConstraint
 * </p>
 *
 * <p><b>Description:</b><br/>
 * 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 3 janv. 08
 * @version 1.0
 */
public class IntervalConstraint implements DataConstraint
{
    protected double min;
    protected double max;
    
    
    public IntervalConstraint(double min, double max)
    {
    	this.min = min;
    	this.max = max;
    }
    
    
    public void validate(DataBlock data) throws CDMException
    {
    	double value = data.getDoubleValue();
    	
    	if (value < min)
    		throw new CDMException("Value must be higher or equal to " + min);
    	
    	if (value > max)
    		throw new CDMException("Value must be lower or equal to " + max);
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
}
