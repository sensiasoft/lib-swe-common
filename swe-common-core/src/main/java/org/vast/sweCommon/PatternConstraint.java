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
 * @since 13 aout. 08
 * @version 1.0
 */
public class PatternConstraint implements DataConstraint
{
	protected String pattern;
	
	
	public PatternConstraint(String pattern)
    {
    	this.pattern = pattern;
    }
	
	
	public boolean validate(DataBlock data)
    {
    	String value = data.getStringValue();
    	return value.matches(pattern);
    }
	
	
	public String getAssertionMessage()
	{
		return "match {" + pattern + "}";
	}


	public String getPattern()
	{
		return pattern;
	}


	public void setPattern(String pattern)
	{
		this.pattern = pattern;
	}
}
