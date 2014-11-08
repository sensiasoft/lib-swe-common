/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2014 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
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
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
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
