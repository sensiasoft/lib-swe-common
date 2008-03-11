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
    Alexandre Robin <alexandre.robin@spotimage.com>
 
******************************* END LICENSE BLOCK ***************************/
package org.vast.data;

import org.vast.cdm.common.DataComponent;
import org.vast.sweCommon.SweConstants;


/**
 * <p><b>Title:</b><br/>
 * Param Helper
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides helper methods to access components by name,
 * definition, full path, etc...
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date Mar, 11 2008
 * @version 1.0
 */
public class ParamHelper
{

	public ParamHelper()
	{
		super();
	}


	public DataComponent findParameterByName(String name, DataComponent parent)
	{
		int childCount = parent.getComponentCount();
		for (int i=0; i<childCount; i++)
		{
			DataComponent child = parent.getComponent(i);
			String childName = child.getName();
			
			if (childName.equals(name))
				return child;
			
			// try to find it recursively!
			DataComponent desiredParam = findParameterByName(name, child);
			if (desiredParam != null)
				return desiredParam;
		}
		
		return null;
	}


	public DataComponent findParameterByDefinition(String defUri, DataComponent parent)
	{
		int childCount = parent.getComponentCount();
		for (int i=0; i<childCount; i++)
		{
			DataComponent child = parent.getComponent(i);
			String childDef = (String)child.getProperty(SweConstants.DEF_URI);
			
			if (childDef != null && childDef.equals(defUri))
				return child;
			
			// try to find it recursively!
			DataComponent desiredParam = findParameterByDefinition(defUri, child);
			if (desiredParam != null)
				return desiredParam;
		}
		
		return null;
	}

}