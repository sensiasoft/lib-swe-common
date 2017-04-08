/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.xml;


/**
 * <p>
 * Utility class to store element name with namespace prefix and URI.
 * </p>
 *
 * @author Alex Robin
 * @since Dec 15, 2006
 * */
class QName
{
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
    
    
    public QName(String nsUri, String qname)
    {
        setNsUri(nsUri);
        setFullName(qname);
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
            prefix = DOMHelper.DEFAULT_PREFIX;
            localName = qname;
        }
    }
    
    
    public String getFullName()
    {
        if (prefix.equals(DOMHelper.DEFAULT_PREFIX))
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
        if (prefix == null)
            this.prefix = DOMHelper.DEFAULT_PREFIX;
        else
            this.prefix = prefix;
    }
    
    
    @Override
    public int hashCode()
    {
    	return (nsUri + '#' + localName).hashCode();
    }
    
    
    @Override
    public boolean equals(Object other)
    {
    	if (other instanceof QName)
    	{
	    	QName qname = (QName)other;
	    	if (nsUri == null)
	    		return qname.nsUri == null && localName.equals(qname.localName);
	    	else
	    		return nsUri.equals(qname.nsUri) && localName.equals(qname.localName);
    	}
    	else
    		return false;
    }
    
    
    @Override
    public String toString()
    {
    	return getFullName();
    }
}
