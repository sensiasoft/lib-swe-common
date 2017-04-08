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

package org.vast.cdm.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.BlockComponent;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;


/**
 * <p>
 * Concrete implementations of this interface are responsible for
 * parsing data with the given encoding format, decoding this data
 * into DataInfo and DecodedData objects if a DataHandler is 
 * registered and sending events to registered handlers. 
 * 
 * TODO add parseNextBlock() method taking DataBlock as argument so
 * the same datablock can be efficiently reused + do impl
 * </p>
 *
 * @author Alex Robin
 * @since Aug 12, 2005
 * */
public interface DataStreamParser
{
	public DataHandler getDataHandler();
	
	
	public RawDataHandler getRawDataHandler();


	public ErrorHandler getErrorHandler();
	
	
	public DataComponent getDataComponents();


	public DataEncoding getDataEncoding();


	public void setDataHandler(DataHandler handler);
	
	
	public void setRawDataHandler(RawDataHandler handler);


	public void setErrorHandler(ErrorHandler handler);


	public void setDataComponents(DataComponent components);


	public void setDataEncoding(DataEncoding encoding);
    
    
    public void setParentArray(BlockComponent parentArray);
    
    
    public void setInput(InputStream inputStream) throws IOException; 
	
	
	public void parse(String uri) throws IOException;


	public void parse(URI uri) throws IOException;


	public void parse(InputStream inputStream) throws IOException;
	
	
	public DataBlock parseNextBlock() throws IOException;
	
	
	public void close() throws IOException;
	
	
	public void reset();
	
	
	public void stop();


    void setRenewDataBlock(boolean renewDataBlock);
}
