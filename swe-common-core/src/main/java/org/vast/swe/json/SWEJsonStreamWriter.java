/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.json;

import java.io.OutputStream;
import java.util.HashMap;
import javax.xml.stream.XMLStreamException;
import org.vast.json.JsonStreamWriter;


public class SWEJsonStreamWriter extends JsonStreamWriter
{
    protected final static String NO_NS = "";
    
    // array name mappings (key is localName, value is NS uri)
    protected HashMap<String, String> arrays = new HashMap<String, String>();
    protected HashMap<String, String> numerics = new HashMap<String, String>();
    
    public SWEJsonStreamWriter(OutputStream os, String encoding) throws XMLStreamException
    {
        super(os, encoding);
        this.markAttributes = false;
        
        // elements to encode as JSON arrays
        addSpecialNames(arrays, NO_NS, "field", "coordinate", "item", "quality", "member");
        
        // elements with numerical values
        addSpecialNames(numerics, NO_NS, "value", "nilValue", "paddingBytes-after", "paddingBytes-before", "byteLength", "significantBits", "bitLength");
    }
    
    
    protected void addSpecialNames(HashMap<String, String> nameList, String namespaceURI, String... names)
    {
        for (String name: names)
            nameList.put(name, namespaceURI);
    }
    
    
    @Override
    protected boolean isArray(String namespaceURI, String localName)
    {
        String assocNsUri = arrays.get(localName);
        
        if (assocNsUri == null)
            return false;
        
        else if (assocNsUri.isEmpty() || assocNsUri.equals(namespaceURI))
            return true;
        
        return false;
    }
    
    
    @Override
    protected boolean isNumericValue(String value)
    {
        if (!super.isNumericValue(value))
            return false;
        
        if (numerics.containsKey(currentContext.eltName))
        {
            if (currentContext.eltName.equals("value"))
            {
                String parentName = currentContext.parent.eltName;
                if (parentName.startsWith("Quantity") || parentName.startsWith("Count") || parentName.startsWith("Time"))
                    return true;
                else
                    return false;
            }
            
            return true; 
        }
        
        return false;
    }


    @Override
    protected boolean isObjectElement(String namespaceURI, String localName)
    {
        // for now, all elements starting with an upper case letter are considered objects
        // we could also lookup in a map containing the list of all object elements
        return Character.isUpperCase(localName.charAt(0));
    }
}
