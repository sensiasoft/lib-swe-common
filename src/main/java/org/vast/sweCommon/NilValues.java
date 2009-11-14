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

import java.util.Hashtable;


/**
 * <p><b>Title:</b><br/>
 * NilValues
 * </p>
 *
 * <p><b>Description:</b><br/>
 * 
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 12 Dec. 08
 * @version 1.0
 */
public class NilValues
{
	protected Hashtable<String, Object> reasonsToValues;
	protected Hashtable<Object, String> valuesToReasons;
	
	
	public NilValues()
	{
		reasonsToValues = new Hashtable<String, Object>();
		valuesToReasons = new Hashtable<Object, String>();
	}
	
	
	public void addNilValue(String reason, Object value)
	{
		reasonsToValues.put(reason, value);
		valuesToReasons.put(value, reason);
	}
	
	
	public Object getReservedValue(String reason)
	{
		return reasonsToValues.get(reason);
	}
	
	
	public String getReason(Object value)
	{
		return valuesToReasons.get(value);
	}
}
