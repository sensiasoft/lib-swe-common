/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2014 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;


/**
 * <p>
 * Generic namespace register used by all XML writers.
 * User can register namespace prefix:URI mappings to be used when
 * serializing the XML
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Aug 21, 2014
 */
public class NamespaceRegister implements NamespaceContext
{
    Map<String, String> prefixToUri = new LinkedHashMap<String, String>();
    Map<String, String> uriToPrefix = new LinkedHashMap<String, String>();
    

    @Override
    public String getNamespaceURI(String prefix)
    {
        return prefixToUri.get(prefix);
    }


    @Override
    public String getPrefix(String namespaceURI)
    {
        return uriToPrefix.get(namespaceURI);
    }


    @Override
    public Iterator<String> getPrefixes(String namespaceURI)
    {
        if (namespaceURI == null)
            return prefixToUri.keySet().iterator();
        
        return null;
    }
    
    
    public Map<String, String> getPrefixMap()
    {
        return prefixToUri;
    }
    
    
    public void registerNamespace(String prefix, String namespaceURI)
    {
        prefixToUri.put(prefix, namespaceURI);
        uriToPrefix.put(namespaceURI, prefix);
    }

}
