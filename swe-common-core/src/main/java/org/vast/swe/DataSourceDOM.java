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

package org.vast.swe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.vast.cdm.common.DataSource;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p>
 * This DataSource allows to extract data from an XML DOM tree and
 * simulate an inputstream so that a low level parser can parse the
 * content using the common DataStreamParser interface.
 * </p>
 *
 * @author Alex Robin
 * @since Feb, 28 2008
 * */
public class DataSourceDOM implements DataSource
{
	protected DOMHelper dom;
	protected Element parentElt;
	    
    
    public DataSourceDOM(DOMHelper dom, Element parentElt)
    {
    	this.dom = dom;
    	this.parentElt = parentElt;
    }
    
    
    public DOMHelper getDom()
	{
		return dom;
	}
	
	
	public Element getParentElt()
	{
		return parentElt;
	}
    
    
	/**
     * Gets the right input stream from href or inline values
     * @return input stream to read data from
     * @throws IOException
     */
    @Override
    public InputStream getDataStream() throws IOException
    {
    	String values = parentElt.getTextContent();
        return(new ByteArrayInputStream(values.getBytes()));
    }
    
}
