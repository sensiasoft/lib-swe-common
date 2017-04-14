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

import java.io.*;
import java.net.URI;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;
import org.vast.cdm.common.*;
import org.vast.util.ReaderException;


/**
 * <p>
 * Abstract class for parsing a CDM data stream
 * </p>
 *
 * @author Alex Robin
 * @since Aug 16, 2005
 * */
public abstract class AbstractDataParser extends DataTreeVisitor implements DataStreamParser
{
	protected final static String STREAM_ERROR = "Error while parsing data stream";
	protected final static String CHOICE_ERROR = "Invalid choice selection: ";
	
	protected SWEDataTypeUtils dataTypeUtils = new SWEDataTypeUtils();
	protected boolean stopParsing = false;
	protected boolean renewDataBlock = true;
	
	
	public AbstractDataParser()
	{
		super(true);
	}
	
	
	protected abstract boolean moreData() throws IOException;
	
	
	/**
	 * Stop the parsing from another thread
	 */
	@Override
    public synchronized void stop()
	{
		stopParsing = true;
	}
	
	
	@Override
	public void reset()
    {
	    super.reset();
	    
	    // generate new data block if not parsing to an array
	    if (parentArray == null && renewDataBlock)
	        dataComponents.renewDataBlock();
    }
	
	
	/**
	 * Default parse method from a URI string
	 */
	@Override
    public void parse(String uri) throws IOException
	{
		InputStream in = URIStreamHandler.openStream(uri);
		this.parse(new BufferedInputStream(in));
	}

	
	/**
	 * Default parse method from a URI object
	 */
	@Override
    public void parse(URI uri) throws IOException
	{
		InputStream in = URIStreamHandler.openStream(uri);
		this.parse(new BufferedInputStream(in));
	}
	
	
	/**
	 * Parse next atom from stream
	 */
	@Override
    public DataBlock parseNextBlock() throws IOException
	{
		try
        {
            do
            {
                if (!moreData())
                    return null;
                
                this.processNextElement();
            }
            while (!componentStack.isEmpty());
            
            return dataComponents.getData();
        }
        catch (Exception e)
        {
            throw new ReaderException(e);
        }
	}
	
	
	@Override
    public void setDataComponents(DataComponent dataInfo)
    {
        dataComponents = dataInfo.copy();
    }


	@Override
    public void setRenewDataBlock(boolean renewDataBlock)
    {
	    // call reset once so that datablock is properly initialized
	    if (renewDataBlock == false)
	        reset();
	    
	    this.renewDataBlock = renewDataBlock;
    }
}