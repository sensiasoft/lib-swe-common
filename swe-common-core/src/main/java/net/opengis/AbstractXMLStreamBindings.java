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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;


/**
 * <p>
 * Base abstract class for all XML bindings using XML Stream API
 * </p>
 *
 * @author Alex Robin
 * @since Jul 28, 2014
 */
public abstract class AbstractXMLStreamBindings extends AbstractBindings
{
    public static final String ERROR_INVALID_ELT = "Invalid Element: ";
    public static final String ERROR_UNSUPPORTED_TYPE = "Unsupported Type: ";
    public static final String XLINK_NS_URI = "http://www.w3.org/1999/xlink";
    public static final String XSI_NS_URI = "http://www.w3.org/2001/XMLSchema-instance";
        
    boolean needNamespaceDecl = false;
    boolean resolveAllXlinks = false;
    protected NamespaceRegister nsContext = new NamespaceRegister();
    protected Map<String, Object> idrefMap = new HashMap<>();
    
    
    public void declareNamespacesOnRootElement()
    {
        needNamespaceDecl = true;
    }
    
    
    public QName getQNameFromString(String qname)
    {
        String[] parts = qname.split(":");
        String prefix = parts[0];
        String uri = nsContext.getNamespaceURI(prefix);
        String local = parts[1];
        return new QName(uri, local, prefix);
    }
    
    
    protected String getStringValue(QName qname)
    {
        return qname.getPrefix() + ":" + qname.getLocalPart();
    }
    
    
    public void ensureNamespaceDecl(XMLStreamWriter writer, QName qname) throws XMLStreamException
    {
        String newPrefix = ensurePrefix(writer, qname);
        if (newPrefix != null)
            writer.writeNamespace(newPrefix, qname.getNamespaceURI());
    }
    
    
    public String ensurePrefix(XMLStreamWriter writer, QName qname) throws XMLStreamException
    {
        String nsUri = qname.getNamespaceURI();
        String prefix = qname.getPrefix();
                
        if (writer.getPrefix(nsUri) == null)
        {
            // try to get a prefix from this binding context
            String contextPrefix = nsContext.getPrefix(nsUri);
            if (contextPrefix != null)
                prefix = contextPrefix;
            
            int i = 1;
            while (writer.getNamespaceContext().getNamespaceURI(prefix) != null)
            {
                prefix = "ns" + i;
                i++;
            }
            
            writer.setPrefix(prefix, nsUri);
            return prefix;
        }
        
        return null;
    }
    
    
    public void setNamespacePrefixes(XMLStreamWriter writer) throws XMLStreamException
    {
        writer.setNamespaceContext(nsContext);
        
        for (Entry<String, String> pair: nsContext.getPrefixMap().entrySet())
        {
            if (pair.getValue() != null)
                writer.setPrefix(pair.getKey(), pair.getValue());
        }
    }
    
    
    public void writeNamespaces(XMLStreamWriter writer) throws XMLStreamException
    {
        if (needNamespaceDecl)
        {
            for (Entry<String, String> pair: nsContext.getPrefixMap().entrySet())
            {
                String prefix = pair.getKey();
                String uri = pair.getValue();
                if (uri != null)
                    writer.writeNamespace(prefix, uri);
            }
            
            needNamespaceDecl = false;
        }
    }
    
    
    protected final Map<String, String> collectAttributes(XMLStreamReader reader) throws XMLStreamException
    {
        Map<String, String> attrMap = new HashMap<String, String>();
        for (int i=0; i<reader.getAttributeCount(); i++)
            attrMap.put(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
        return attrMap;
    }
    
    
    protected final boolean checkElementName(XMLStreamReader reader, String localName) throws XMLStreamException
    {
        // check if tag has right local name
        if (reader.getEventType() == XMLStreamConstants.START_ELEMENT && reader.getLocalName().equals(localName))
        {
            // skip tag if xsi:nil is true
            String nil = reader.getAttributeValue(XSI_NS_URI, "nil");
            if ("true".equalsIgnoreCase(nil))
            {
                reader.nextTag();
                return false;
            }
            
            return true;
        }
        
        return false;
    }
    
    
    protected final boolean checkElementQName(XMLStreamReader reader, String nsUri, String localName) throws XMLStreamException
    {
        // check if tag has right local name and namespace URI
        return checkElementName(reader, localName) && reader.getNamespaceURI().equals(nsUri);
    }
    
    
    protected final String errorLocationString(XMLStreamReader reader) throws XMLStreamException
    {
        Location loc = reader.getLocation();
        return " at line " + loc.getLineNumber() + ", col " + loc.getColumnNumber();
    }
    
    
    protected final void readPropertyAttributes(XMLStreamReader reader, OgcProperty<?> prop) throws XMLStreamException
    {
        Map<String, String> attrMap = collectAttributes(reader);
        readPropertyAttributes(attrMap, prop);
    }
    
    
    protected void readPropertyAttributes(Map<String, String> attrMap, OgcProperty<?> prop) throws XMLStreamException
    {
        prop.setName(attrMap.get("name"));
        prop.setTitle(attrMap.get("title"));
        prop.setHref(attrMap.get("href"));
        prop.setRole(attrMap.get("role"));
        prop.setArcRole(attrMap.get("arcrole"));
    }
    
    
    protected void writePropertyAttributes(XMLStreamWriter writer, OgcProperty<?> prop) throws XMLStreamException
    {
        String val;
        
        val = prop.getName();
        if (val != null)
            writer.writeAttribute("name", val);
        
        val = prop.getTitle();
        if (val != null)
            writer.writeAttribute(XLINK_NS_URI, "title", val);
        
        val = prop.getRole();
        if (val != null)
            writer.writeAttribute(XLINK_NS_URI, "role", val);
        
        val = prop.getArcRole();
        if (val != null)
            writer.writeAttribute(XLINK_NS_URI, "arcrole", val);
        
        val = prop.getHref();
        if (val != null)
            writer.writeAttribute(XLINK_NS_URI, "href", val);
    }
    
    
    /**
     * Skip an element and all its children and keep parsing
     * @param reader
     * @throws XMLStreamException
     */
    protected final void skipElementAndAllChildren(XMLStreamReader reader) throws XMLStreamException
    {
        skipElementAndAllChildren(reader, true);
    }
    
    
    protected final void skipElementAndAllChildren(XMLStreamReader reader, boolean skipEndTag) throws XMLStreamException
    {
        int eventCode;
        int levelCount = 1;
        while (levelCount > 0)
        {
            eventCode = reader.next();            
            if (eventCode == XMLStreamConstants.START_ELEMENT)
                levelCount++;
            else if (eventCode == XMLStreamConstants.END_ELEMENT)
                levelCount--;
        }
        
        if (skipEndTag)
            reader.nextTag();
    }
    
    
    public Serializable readExtension(XMLStreamReader reader) throws XMLStreamException
    {
        // by default we skip the extension element and all its children
        // sub-classes can override to implement some extensions
        skipElementAndAllChildren(reader);        
        return null;
    }
    
    
    public void writeExtension(XMLStreamWriter writer, Object obj) throws XMLStreamException
    {
        // we do nothing by default
        // sub-classes can override to implement some extensions
    }
    
    
    public boolean canWriteExtension(Object obj)
    {
        return false;
    }


    public NamespaceRegister getNamespaceContext()
    {
        return nsContext;
    }


    public void setNamespaceContext(NamespaceRegister nsContext)
    {
        this.nsContext = nsContext;
    }
}
