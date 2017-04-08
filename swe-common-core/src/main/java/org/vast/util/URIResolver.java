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

package org.vast.util;

import java.io.*;
import java.net.*;


/**
 * <p>
 * URI Resolver uses local mapping file or DNS servers
 * to resolve a URI to a URL, or simply uses the URL
 * class if the URI is already a URL.
 * </p>
 *
 * @author Alex Robin
 * @since Aug 19, 2005
 * */
public class URIResolver
{
    URI uri, baseUri, resolvedUri;
    URL url;
    
    
    public URIResolver(URI uri)
    {
        this.resolvedUri = uri;
    }
    
    
    public URIResolver(URI uri, URI baseUri)
    {
        this.uri = uri;
        this.baseUri = baseUri;
        this.resolvedUri = baseUri.resolve(uri);
    }
    
    
    
    /**
     * Open the stream pointed by this uri
     * @return InputStream
     * @throws IOException
     */
    public InputStream openStream() throws IOException
    {
        resolveToUrl();
        return url.openStream();
    }
    
    
    /**
     * Resolves the URI to a URL using the appropriate resolution method (depending on URI scheme)
     * @return the resolved URL
     * @throws IOException
     */
    public URL resolveToUrl() throws IOException
    {
        try
        {
            url = resolvedUri.toURL();
        }
        catch (MalformedURLException e)
        {
            if (uri.getScheme().equals("urn"))
            {
                String path = uri.getSchemeSpecificPart();
                //String fragment = uri.getFragment();
                
                // TODO use a resource file for local and dns for delegated
                // logic to get url from uri...
                try
                {
                    if (path.equals("ogc:process:lookUpTable"))
                    {
                        url = new URL("file:///d:/Projects/NSSTC/SensorML/instances/Processes.xml#LOOK_UP_TABLE");
                    }
                    else if (path.equals("ogc:process:transform"))
                    {
                        url = new URL("file:///d:/Projects/NSSTC/SensorML/instances/Processes.xml#TRANSFORM");
                    }
                    else if (path.equals("ogc:process:llaToEcef"))
                    {
                        url = new URL("file:///d:/Projects/NSSTC/SensorML/instances/Processes.xml#LLA_TO_ECEF");
                    }
                    else if (path.equals("ogc:process:genericResponse"))
                    {
                        url = new URL("file:///d:/Projects/NSSTC/SensorML/instances/Processes.xml#GENERIC_RESPONSE");
                    }
                    else if (path.equals("ogc:process:genericGeometry"))
                    {
                        url = new URL("file:///d:/Projects/NSSTC/SensorML/instances/Processes.xml#GENERIC_GEOMETRY");
                    }
                    else
                    {
                        throw new ResolveException("Cannot resolve URI " + uri);
                    }
                }
                catch (MalformedURLException e1)
                {
                    throw new IllegalStateException(e1);
                }
            }
            else
            {
                throw new IOException("Unsupported URI scheme: " + uri.getScheme());
            }
        }
        
        return url;
    }


    public URI getResolvedUri()
    {
        return resolvedUri;
    }
}
