/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml.json;

import java.io.InputStream;
import java.util.HashSet;
import javax.xml.stream.XMLStreamException;
import org.vast.json.JsonStreamReader;


public class GMLJsonStreamReader extends JsonStreamReader
{
    // list of XML attributes
    protected HashSet<String> xmlAttNames = new HashSet<String>();
    
    // list of elements with inline values
    protected HashSet<String> inlineValueNames = new HashSet<String>();
    
    
    public GMLJsonStreamReader(InputStream is, String encoding) throws XMLStreamException
    {
        super(is, encoding);
        
        // XML attributes
        addSpecialNames(xmlAttNames,
                "href", "role", "arcrole", "code", "id", "srsName");
        
        // XML inline values
        addSpecialNames(inlineValueNames, "nilValue");
    }
    
    
    protected void addSpecialNames(HashSet<String> nameList, String... names)
    {
        for (String name: names)
            nameList.add(name);
    }
    

    @Override
    protected boolean isXmlAttribute(String name)
    {
        return ( (name.charAt(0) == ATT_PREFIX) || xmlAttNames.contains(name) );
    }
    

    @Override
    protected boolean isInlineValue(String name)
    {
        return inlineValueNames.contains(name);
    }

}
