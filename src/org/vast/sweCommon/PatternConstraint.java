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
 * PatternConstraint
 * </p>
 *
 * <p><b>Description:</b><br/>
 * 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 13 aout. 08
 * @version 1.0
 */
public class PatternConstraint implements DataConstraint
{
	protected String[] patternList;
	
	
	public PatternConstraint(String[] patternList)
    {
    	this.patternList = patternList;
    }
	
	
	public void validate(DataBlock data) throws CDMException
    {
    	//String value = data.getStringValue();
    	

    	
    	//throw new CDMException("Value must be match " + patternList[0]);
    }


	public String[] getPatternList()
	{
		return patternList;
	}


	public void setPatternList(String[] patternList)
	{
		this.patternList = patternList;
	}
}
