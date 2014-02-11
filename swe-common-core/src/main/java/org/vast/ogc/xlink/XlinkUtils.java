/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Initial Developer of the Original Code is SENSIA SOFTWARE LLC.
 Portions created by the Initial Developer are Copyright (C) 2012
 the Initial Developer. All Rights Reserved.

 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> for more
 information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.xlink;

import org.vast.ogc.OGCRegistry;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


public class XlinkUtils
{
    private final static String NS_URI = OGCRegistry.getNamespaceURI(OGCRegistry.XLINK);
        
        
    public static void readXlinkAttributes(DOMHelper dom, Element propertyElt, IXlinkReference<?> refObj)
    {
        String href = dom.getAttributeValue(propertyElt, "href");
        refObj.setHref(href);
        
        String role = dom.getAttributeValue(propertyElt, "role");
        refObj.setRole(role);
        
        String arcRole = dom.getAttributeValue(propertyElt, "arcrole");
        refObj.setArcRole(arcRole);
    }
    
    
    public static void writeXlinkAttributes(DOMHelper dom, Element propertyElt, IXlinkReference<?> refObj)
    {
        dom.addUserPrefix("xlink", NS_URI);
        
        if (refObj.getHref() != null && !refObj.getHref().isEmpty())
            dom.setAttributeValue(propertyElt, "xlink:href", refObj.getHref());
        
        if (refObj.getRole() != null && !refObj.getRole().isEmpty())
            dom.setAttributeValue(propertyElt, "xlink:role", refObj.getRole());
        
        if (refObj.getArcRole() != null && !refObj.getArcRole().isEmpty())
            dom.setAttributeValue(propertyElt, "xlink:arcrole", refObj.getArcRole());
    }
}
