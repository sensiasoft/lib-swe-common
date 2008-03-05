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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataSink;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * DataSinkXML
 * </p>
 *
 * <p><b>Description:</b><br/>
 * This DataSink allows to serialize data into an XML DOM tree
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date Feb, 29 2008
 * @version 1.0
 */
public class DataSinkXML implements DataSink
{
	protected ByteArrayOutputStream textData;
    
    
    public DataSinkXML(Element elt)
    {
        
    }


	public OutputStream getDataStream() throws CDMException
	{
		return this.textData;
	}
    
}
