/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.xml;

import java.net.URI;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamReader;


/**
 * <p>
 * This wrapper allows specifying the URI of the document that the stream
 * is read from.<br/>
 * This can be used for error localization as well as for relative link
 * resolution.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since May 18, 2017
 */
public class XMLStreamReaderWithLocation extends DelegatingXMLStreamReader
{
    protected final URI baseURI;
    
    
    protected XMLStreamReaderWithLocation(XMLStreamReader reader, URI baseURI)
    {
        super(reader);
        this.baseURI = baseURI;
    }
    
    
    @Override
    public Location getLocation()
    {
        final Location loc = super.getLocation();
        
        return new Location() {
            @Override
            public int getCharacterOffset()
            {
                return loc.getCharacterOffset();
            }

            @Override
            public int getColumnNumber()
            {
                return loc.getColumnNumber();
            }

            @Override
            public int getLineNumber()
            {
                return loc.getLineNumber();
            }

            @Override
            public String getPublicId()
            {
                if (baseURI != null)
                    return baseURI.toString();
                else
                    return loc.getPublicId();
            }

            @Override
            public String getSystemId()
            {
                return loc.getSystemId();
            }
            
            @Override
            public String toString()
            {
                return loc.toString();
            }
        };
    }

}
