/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/
package org.vast.swe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.vast.cdm.common.DataSink;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * <p>
 * 
 * </p>
 *
 * @author Alex Robin
 * @since 6 oct. 2008
 * */
public class DataSinkDOM implements DataSink
{
	protected ByteArrayOutputStream textData;
	protected DOMHelper dom;
	protected Element parentElt;
	
	
	public DataSinkDOM(DOMHelper dom, Element parentElt)
	{
		this.parentElt = parentElt;
		this.dom = dom;
	}


	public DOMHelper getDom()
	{
		return dom;
	}
	
	
	public Element getParentElt()
	{
		return parentElt;
	}


	@Override
    public OutputStream getDataStream() throws IOException
	{
		this.textData = new ByteArrayOutputStream(1024);
		return this.textData;
	}
	
	
	@Override
    public void flush()
	{
		String text = textData.toString();
		Text textNode = parentElt.getOwnerDocument().createTextNode(text);
		parentElt.appendChild(textNode);
	}
}
