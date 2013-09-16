/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SWE Common Data Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sweCommon;

import org.vast.cdm.common.DataBlock;
import org.vast.cdm.common.DataConstraint;

/**
 * <p>
 * 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @since 3 janv. 08
 * @version 1.0
 */
public class EnumTokenConstraint implements DataConstraint
{
	protected String[] valueList;
	
	
	public EnumTokenConstraint(String[] valueList)
    {
    	this.valueList = valueList;
    }
	
	
	public boolean validate(DataBlock data)
    {
    	String value = data.getStringValue();
    	
    	for (int i=0; i<valueList.length; i++)
    		if (valueList[i].equals(value))
    			return true;
    	
    	return false;
    }
	
	
	public String getAssertionMessage()
	{
		StringBuffer msg = new StringBuffer();
		msg.append("be one of {");
    	
    	for (int i=0; i<valueList.length; i++)
    	{
    		msg.append(valueList[i]);
    		if (i < valueList.length-1)
    			msg.append(", ");
    	}
    	
    	msg.append('}');
    	return msg.toString();
	}


	public String[] getValueList()
	{
		return valueList;
	}


	public void setValueList(String[] valueList)
	{
		this.valueList = valueList;
	}
}
