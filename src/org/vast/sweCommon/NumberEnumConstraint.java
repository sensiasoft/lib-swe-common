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
 * NumberEnumConstraint
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
public class NumberEnumConstraint implements DataConstraint
{
	protected double[] valueList;
	
	
	public NumberEnumConstraint(double[] valueList)
    {
    	this.valueList = valueList;
    }
	
	
	public void validate(DataBlock data) throws CDMException
    {
    	double value = data.getDoubleValue();
    	
    	for (int i=0; i<valueList.length; i++)
    		if (valueList[i] == value)
    			return;
    	
    	StringBuffer numberList = new StringBuffer();
    	numberList.append('{');
    	for (int i=0; i<valueList.length; i++)
    	{
    		numberList.append(Double.toString(valueList[i]));
    		if (i < valueList.length-1)
    			numberList.append(", ");
    	}
    	numberList.append('}');
    	
    	throw new CDMException("Value must be one of " + numberList);
    }


	public double[] getValueList()
	{
		return valueList;
	}


	public void setValueList(double[] valueList)
	{
		this.valueList = valueList;
	}
}
