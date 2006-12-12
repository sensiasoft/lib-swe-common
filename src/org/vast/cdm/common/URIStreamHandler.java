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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import org.vast.util.URIResolver;


public class URIStreamHandler
{
	
	public static synchronized InputStream openStream(String uri) throws CDMException
	{
		try
		{
			URI uriObject = new URI(uri);
			return URIStreamHandler.openStream(uriObject);
		}
		catch (URISyntaxException e)
		{
			throw new CDMException("Invalid URI syntax");
		}	
	}

	
	public static synchronized InputStream openStream(URI uri) throws CDMException
	{
		try
		{
			URIResolver resolver = new URIResolver(uri);
			return resolver.openStream();
		}
		catch (IOException e)
		{
			throw new CDMException("Error while connecting to the data stream");
		}
	}
}
