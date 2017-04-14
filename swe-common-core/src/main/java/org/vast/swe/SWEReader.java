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
import java.io.InputStream;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import org.vast.cdm.common.*;


/**
 * <p>
 * Abstract class for all CDM readers.
 * This class provides methods to parse any XML document
 * containing CDM data structure, encoding and stream sections.
 * The class has getters to get this info after it has been
 * parsed. Concrete derived classes are actually responsible
 * for finding the XML content for each of these sections and
 * using the corresponding parsers to parse it out. This class
 * also has a helper method that constructs the DataParser
 * suited for a given encoding.
 * </p>
 *
 * @author Alex Robin
 * @since Aug 16, 2005
 * */
public abstract class SWEReader implements InputStreamProvider
{
	protected DataEncoding dataEncoding;
	protected DataComponent dataComponents;
	protected DataStreamParser dataParser;
	protected DataHandler dataHandler;
	protected String valuesUri;
	

	public abstract void parse(InputStream inputStream, DataHandler handler) throws IOException;
	
	
	@Override
    public abstract InputStream getDataStream() throws IOException;

	
	public void setDataComponents(DataComponent dataComponents)
	{
		this.dataComponents = dataComponents;
	}
	
	
	public void setDataEncoding(DataEncoding dataEncoding)
	{
		this.dataEncoding = dataEncoding;
	}
	
	
	public void parse(InputStream inputStream) throws IOException
    {
	    parse(inputStream, null);
    }
	
	
	public DataComponent getDataComponents()
	{
		return this.dataComponents;
	}
	
	
	public DataEncoding getDataEncoding()
	{
		return this.dataEncoding;
	}
	
	
	public DataStreamParser getDataParser()
    {
        return this.dataParser;
    }
	
	
	protected DataStreamParser createDataParser() throws IOException
	{
	    DataStreamParser parser = SWEHelper.createDataParser(dataEncoding);
	    parser.setDataComponents(this.dataComponents);
	    parser.reset();
        return parser;
	}
}
