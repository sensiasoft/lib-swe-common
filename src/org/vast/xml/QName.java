/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.xml;


/**
 * <p><b>Title:</b>
 * QName
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Utility class to store element name with namespace prefix and URI.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Dec 15, 2006
 * @version 1.0
 */
public class QName
{
    public final static String DEFAULT_PREFIX = ":NONE:";
    protected String nsUri;
    protected String prefix;
    protected String localName;
    
    
    public QName()
    {        
    }
    
    
    public QName(String qname)
    {
        setFullName(qname);
    }
    
    
    public QName(String nsUri, String localName)
    {
        setNsUri(nsUri);
        setLocalName(localName);
    }
    
    
    public void setFullName(String qname)
    {
        int sepIndex = qname.indexOf(':');
        if (sepIndex > 0)
        {
            prefix = qname.substring(0, sepIndex);
            localName = qname.substring(sepIndex + 1);
        }
        else
        {
            prefix = DEFAULT_PREFIX;
            localName = qname;
        }
        
        nsUri = null;
    }
    
    
    public String getFullName()
    {
        if (prefix.equals(DEFAULT_PREFIX))
            return localName;
        else
            return prefix + ":" + localName;
    }


    public String getLocalName()
    {
        return localName;
    }


    public void setLocalName(String localName)
    {
        this.localName = localName;
    }


    public String getNsUri()
    {
        return nsUri;
    }


    public void setNsUri(String nsUri)
    {
        this.nsUri = nsUri;
    }


    public String getPrefix()
    {
        return prefix;
    }


    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }
}
