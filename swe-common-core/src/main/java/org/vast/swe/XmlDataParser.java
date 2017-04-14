/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SWE Common Data Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe;

import java.io.IOException;
import java.io.InputStream;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.ScalarComponent;


/**
 * <p>
 * Parser for data streams written using SWE Common XML encoding
 * </p>
 *
 * @author Alex Robin
 * @since Feb, 28 2008
 * */
public class XmlDataParser extends AbstractDataParser
{

	@Override
    public void setInput(InputStream inputStream) throws IOException
	{
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
    public void parse(InputStream inputStream) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	
	@Override
	protected void processAtom(ScalarComponent component) throws IOException
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	protected boolean processBlock(DataComponent component) throws IOException
	{
	    // TODO Auto-generated method stub
	    return true;
	}


    @Override
    protected boolean moreData() throws IOException
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public void close() throws IOException
    {
        // TODO Auto-generated method stub
        
    }
}
