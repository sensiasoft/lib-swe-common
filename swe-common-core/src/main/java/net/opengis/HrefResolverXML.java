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
import java.net.URL;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


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
    
    public HrefResolverXML()
    {
    }
    
    
    /* (non-Javadoc)
     * @see net.opengis.HrefResolver#resolveHref(net.opengis.OgcProperty)
     */
    @Override
    public boolean resolveHref(OgcProperty<?> hrefProp) throws IOException
    {
        if (!hrefProp.hasHref())
            return false;
        
        URL hrefUrl = new URL(hrefProp.getHref());
        InputStream is = new BufferedInputStream(hrefUrl.openStream());
                
        try
        {
            XMLInputFactory fac = XMLInputFactory.newInstance();
            XMLStreamReader reader = fac.createXMLStreamReader(is);
            reader.nextTag();
            parseContent(reader);
            reader.close();
            if (hrefProp.hasValue())
                return true;
        }
        catch (XMLStreamException e)
        {
            throw new IOException("Error while parsing remote XML content", e);
        }
        
        return false;
    }
    
    
    public abstract void parseContent(XMLStreamReader reader) throws XMLStreamException;
}
