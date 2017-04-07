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

package org.vast.swe;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import org.vast.util.URIResolver;


public class URIStreamHandler
{
    static final String ERR_MSG = "Cannot connect to data stream ";
    
    
    public static synchronized InputStream openStream(String uri) throws IOException
	{
		try
		{
			URI uriObject = new URI(uri);
			return URIStreamHandler.openStream(uriObject);
		}
		catch (URISyntaxException e)
		{
			throw new IOException(ERR_MSG, e);
		}	
	}

	
	public static synchronized InputStream openStream(URI uri) throws IOException
	{
		try
		{
			URIResolver resolver = new URIResolver(uri);
			return resolver.openStream();
		}
		catch (IOException e)
		{
			throw new IOException(ERR_MSG + uri, e);
		}
	}
}
