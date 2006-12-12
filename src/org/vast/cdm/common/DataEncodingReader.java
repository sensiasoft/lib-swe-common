/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.cdm.common;

import org.w3c.dom.*;


/**
 * <p><b>Title:</b><br/>
 * Data Encoding XML Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Concrete implementations of this interface are responsible for
 * parsing an XML element containing data encoding parameters
 * as specified in the schema and create the appropriate DataEncoding
 * object.  
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @since Aug 12, 2005
 * @version 1.0
 */
public interface DataEncodingReader
{
	public DataEncoding readDataEncoding(Element encodingElement) throws CDMException;
}
