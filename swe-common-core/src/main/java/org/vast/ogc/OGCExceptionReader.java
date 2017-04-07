/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc;

import java.io.InputStream;
import org.vast.ogc.OGCException;
import org.vast.xml.*;
import org.w3c.dom.Element;


public class OGCExceptionReader
{
	
	public static void checkException(DOMHelper dom) throws OGCException
	{
		Element rootElt = dom.getRootElement();
		
		// SOAP envelope and fault cases
		if (rootElt.getLocalName().equals("Envelope"))
		{
			rootElt = dom.getElement(rootElt, "Body/*");
		
			if (rootElt.getLocalName().equals("Fault"))
				rootElt = dom.getElement(rootElt, "faultstring");
		}
		
        String exceptionText = dom.getElementValue(rootElt, "");
        
        if (exceptionText == null)
            exceptionText = dom.getElementValue(rootElt, "Exception");
        
        if (exceptionText == null || exceptionText.equals(""))
            exceptionText = dom.getElementValue(rootElt, "Exception/ExceptionText");
		
        if (exceptionText == null)
            exceptionText = dom.getElementValue(rootElt, "ServiceException");
		
		if (exceptionText == null)
            exceptionText = dom.getElementValue(rootElt, "ServiceException/Locator");

        if (exceptionText == null || exceptionText.equals(""))
            exceptionText = dom.getAttributeValue(rootElt, "ServiceException/code");
		
        if (exceptionText != null)
        	throw new OGCException("ServiceException: " + exceptionText);
	}
	
	
	public static void parseException(InputStream in) throws OGCException
	{
		try
        {
            DOMHelper dom = new DOMHelper(in, false);
            checkException(dom);
        }
        catch (DOMHelperException e)
        {
            throw new OGCException("Invalid Exception", e);
        }
        
		return;
	}
}
