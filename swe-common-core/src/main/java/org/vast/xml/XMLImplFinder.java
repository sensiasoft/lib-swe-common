/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.xml;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;


/**
 * <p>
 * Helper class to manage what XML implementations are used
 * </p>
 *
 * @author Alex Robin
 * @since Jan 23, 2015
 */
public class XMLImplFinder
{
    static final Logger log = LoggerFactory.getLogger(XMLImplFinder.class);
    static DOMImplementation domImplementation;
    static XMLInputFactory staxInputFactory;
    static XMLOutputFactory staxOutputFactory;
    
    
    public static DOMImplementation getDOMImplementation()
    {
        if (domImplementation != null)
            return domImplementation;
        
        try
        {
            // first try to get an LS implementation
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            return registry.getDOMImplementation("LS");
        }
        catch (Exception e)
        {
            log.warn("Cannot find DOM LS implementation", e);
            
            try
            {
                // otherwise fallback to simple implementation
                DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
                return registry.getDOMImplementation("XML 1.0");
            }
            catch (Exception e1)
            {
                throw new IllegalStateException("Cannot find DOM implementation", e1);
            }
        }
    }
    
    
    public static void setDOMImplementation(DOMImplementation domImpl)
    {
        domImplementation = domImpl;
    }
    
    
    public static XMLInputFactory getStaxInputFactory()
    {
        if (staxInputFactory != null)
            return staxInputFactory;
        
        return XMLInputFactory.newInstance();
    }
    
    
    public static void setStaxInputFactory(XMLInputFactory inputFactory)
    {
        staxInputFactory = inputFactory;
    }
    
    
    public static XMLOutputFactory getStaxOutputFactory()
    {
        if (staxOutputFactory != null)
            return staxOutputFactory;
        
        return XMLOutputFactory.newInstance();
    }
    
    
    public static void setStaxOutputFactory(XMLOutputFactory outputFactory)
    {
        staxOutputFactory = outputFactory;
    }
}
