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
import org.w3c.dom.Element;


public class XlinkUtils
{
    private final static String NS_URI = OGCRegistry.getNamespaceURI(OGCRegistry.XLINK);
        
        
    public static void readXlinkAttributes(Element propertyElt, IXlinkReference<?> refObj)
    {
        String href = propertyElt.getAttributeNS(NS_URI, "href");
        refObj.setHref(href);
        
        String role = propertyElt.getAttributeNS(NS_URI, "role");
        refObj.setRole(role);
        
        String arcRole = propertyElt.getAttributeNS(NS_URI, "arcrole");
        refObj.setArcRole(arcRole);
    }
    
    
    public static void writeXlinkAttributes(Element propertyElt, IXlinkReference<?> refObj)
    {
        if (refObj.getHref() != null)
            propertyElt.setAttributeNS(NS_URI, "href", refObj.getHref());
        
        if (refObj.getRole() != null)
            propertyElt.setAttributeNS(NS_URI, "role", refObj.getRole());
        
        if (refObj.getArcRole() != null)
            propertyElt.setAttributeNS(NS_URI, "arcrole", refObj.getArcRole());
    }
}
