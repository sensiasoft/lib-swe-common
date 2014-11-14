/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2014 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Jul 28, 2014
 */
public abstract class AbstractXMLStreamBindings extends AbstractBindings
{
    public final static String ERROR_INVALID_ELT = "Invalid Element: ";
    public final static String XLINK_NS_URI = "http://www.w3.org/1999/xlink";
        
    boolean needNamespaceDecl = false;
    boolean resolveAllXlinks = false;
    protected NamespaceRegister nsContext = new NamespaceRegister();    
    protected Map<String, Object> idrefMap = new HashMap<String, Object>();
    
    
    public void declareNamespacesOnRootElement()
    {
        needNamespaceDecl = true;
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
    
    
    protected void writeNamespaces(XMLStreamWriter writer) throws XMLStreamException
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
    
    
    protected Map<String, String> collectAttributes(XMLStreamReader reader) throws XMLStreamException
    {
        Map<String, String> attrMap = new HashMap<String, String>();
        for (int i=0; i<reader.getAttributeCount(); i++)
            attrMap.put(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
        return attrMap;
    }
    
    
    protected boolean checkElementName(XMLStreamReader reader, String localName) throws XMLStreamException
    {
        // check if tag has right local name
        if (reader.getEventType() == XMLStreamConstants.START_ELEMENT && reader.getLocalName().equals(localName))
            return true;

        return false;
    }
    
    
    protected boolean checkElementQName(XMLStreamReader reader, String nsUri, String localName) throws XMLStreamException
    {
        // check if tag has right local name and namespace URI
        if (reader.getEventType() == XMLStreamConstants.START_ELEMENT 
                && reader.getNamespaceURI().equals(nsUri) && reader.getLocalName().equals(localName) )
            return true;

        return false;
    }
    
    
    protected String errorLocationString(XMLStreamReader reader) throws XMLStreamException
    {
        Location loc = reader.getLocation();
        return " at line " + loc.getLineNumber() + ", col " + loc.getColumnNumber();
    }
    
    
    public void readPropertyAttributes(XMLStreamReader reader, OgcProperty<?> prop) throws XMLStreamException
    {
        Map<String, String> attrMap = collectAttributes(reader);
        readPropertyAttributes(attrMap, prop);
    }
    
    
    public void readPropertyAttributes(Map<String, String> attrMap, OgcProperty<?> prop) throws XMLStreamException
    {
        prop.setName(attrMap.get("name"));
        prop.setHref(attrMap.get("href"));
        prop.setRole(attrMap.get("role"));
        prop.setArcRole(attrMap.get("arcrole"));
    }
    
    
    public void writePropertyAttributes(XMLStreamWriter writer, OgcProperty<?> prop) throws XMLStreamException
    {
        String val;
        
        val = prop.getName();
        if (val != null)
            writer.writeAttribute("name", val);
        
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
    
    
    public Object readExtension(XMLStreamReader reader) throws XMLStreamException
    {
        // default = skip next element and all its children
        int eventCode;
        int levelCount = 1;
        while (levelCount > 0)
        {
            eventCode = reader.nextTag();
            if (eventCode == XMLStreamConstants.START_ELEMENT)
                levelCount++;
            if (eventCode == XMLStreamConstants.END_ELEMENT)
                levelCount--;
        }
        
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
}
