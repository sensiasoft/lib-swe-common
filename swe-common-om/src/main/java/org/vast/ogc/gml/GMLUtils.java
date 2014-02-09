/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.

 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or 
 Mike Botts <mike.botts@botts-inc.net for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.io.InputStream;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.om.OMUtils;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.vast.xml.XMLReaderException;


/**
 * <p>
 * TODO GMLUtils type description
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Feb 8, 2014
 */
public class GMLUtils
{
    public final static String GML;
    
    
    static 
    {
        GML = "GML";
        loadRegistry();
    }
    
    
    public static void loadRegistry()
    {
        String mapFileUrl = OMUtils.class.getResource("OMRegistry.xml").toString();
        OGCRegistry.loadMaps(mapFileUrl, false);
    }
    
    
    public static IFeature readFeature(InputStream inputStream) throws XMLReaderException
    {
        try
        {
            DOMHelper dom = new DOMHelper(inputStream, false);
            GMLFeatureReader reader = new GMLFeatureReader();
            return reader.read(dom, dom.getBaseElement());
        }
        catch (DOMHelperException e)
        {
            throw new XMLReaderException("Error while reading GML from input stream", e);
        }
    }
}
