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

package org.vast.sweCommon;

import org.vast.cdm.common.DataStreamWriter;
import org.vast.data.DataIterator;
import org.vast.data.DataValue;
import org.vast.util.DateTimeFormat;


/**
 * <p><b>Title:</b><br/>
 * Data Writer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Abstract class for writing a CDM data stream.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Feb 10, 2006
 * @version 1.0
 */
public abstract class AbstractDataWriter extends DataIterator implements DataStreamWriter
{
	protected boolean stopWriting = false;
		
	
	public AbstractDataWriter()
	{
		super(false);
	}
	
	
	/**
	 * Stop the parsing from another thread
	 */
	public synchronized void stop()
	{
        stopWriting = true;
	}
	
	
	/**
	 * Retrieve string value of component
	 * This will convert to an ISO string for appropriate time components
	 * @param scalarInfo
	 * @return
	 */
	protected String getStringValue(DataValue scalarInfo)
	{
		String def = (String)scalarInfo.getProperty(SweConstants.UOM_URI);
		String val;
		
		if (def != null && def.contains("8601"))
			val = DateTimeFormat.formatIso(scalarInfo.getData().getDoubleValue(), 0);
		else
			val = scalarInfo.getData().getStringValue();
		
		return val;
	}
}
