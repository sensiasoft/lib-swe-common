/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.vast.util.ResolveException;


/**
 * <p>
 * Implementation of xlink resolution and parsing for remote XML content
 * </p>
 *
 * @author Alex Robin
 * @since Sep 29, 2014
 */
public abstract class HrefResolverXML implements HrefResolver
{    
    URI baseURI;
    
    
    public HrefResolverXML()
    {
    }
    
    
    public HrefResolverXML(String baseURI)
    {
        try
        {
            this.baseURI = (baseURI == null) ? null : new URI(baseURI);
        }
        catch (URISyntaxException e)
        {
            throw new IllegalArgumentException("Invalid base URI", e);
        }
    }
    
    
    @Override
    public boolean resolveHref(OgcProperty<?> hrefProp) throws ResolveException
    {
        if (!hrefProp.hasHref())
            return false;
        
        URL hrefURL;
        try
        {
            URI hrefURI = new URI(hrefProp.getHref());
            if (baseURI == null)
                hrefURL = hrefURI.toURL();
            else
                hrefURL = baseURI.resolve(hrefURI).toURL();
        }
        catch (Exception e)
        {
            throw new ResolveException(String.format("Cannot resolve URI '%s'", hrefProp.getHref()), e);
        }
                
        try
        {
            InputStream is = new BufferedInputStream(hrefURL.openStream());
            XMLInputFactory fac = XMLInputFactory.newInstance();
            XMLStreamReader reader = fac.createXMLStreamReader(is);
            reader.nextTag();
            parseContent(reader);
            reader.close();
            if (hrefProp.hasValue())
                return true;
        }
        catch (IOException e)
        {
            throw new ResolveException("Cannot access XML content at " + hrefProp.getHref(), e);
        }
        catch (XMLStreamException e)
        {
            throw new ResolveException("Cannot parse XML content at " + hrefProp.getHref(), e);
        }
        
        return false;
    }
    
    
    public abstract void parseContent(XMLStreamReader reader) throws XMLStreamException;
}
