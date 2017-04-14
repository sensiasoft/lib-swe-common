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

import java.io.IOException;
import java.io.OutputStream;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.cdm.common.OutputStreamProvider;


/**
 * <p>
 * Abstract class for all CDM writers.
 * This class provides methods to write an XML document
 * containing CDM data structure, encoding and stream sections.
 * The class has setters allowing one to specify a data component
 * structure and an encoding. Concrete derived classes are
 * actually responsible for writing the XML content for each of
 * these sections using the corresponding writers. This class
 * also has a helper method that constructs the DataWriter
 * suited for the given encoding.
 * </p>
 *
 * @author Alex Robin
 * @since Feb 10, 2006
 * */
public abstract class SWEWriter implements OutputStreamProvider
{
	protected DataEncoding dataEncoding;
	protected DataComponent dataComponents;
		

	public abstract void write(OutputStream inputStream) throws IOException;
	
	@Override
    public abstract OutputStream getDataStream() throws IOException;
	
	
	public DataStreamWriter getDataWriter() throws CDMException
	{
        DataStreamWriter writer = SWEHelper.createDataWriter(dataEncoding);
        writer.setDataComponents(this.dataComponents);
        writer.reset();
        return writer;
	}
	
	
	public DataEncoding getDataEncoding()
	{
		return this.dataEncoding;
	}


	public DataComponent getDataComponents()
	{
		return this.dataComponents;
	}
    
    
    public void setDataComponents(DataComponent dataComponents)
    {
        this.dataComponents = dataComponents;        
    }
    
    
    public void setDataEncoding(DataEncoding dataEncoding)
    {
        this.dataEncoding = dataEncoding;        
    }
}
