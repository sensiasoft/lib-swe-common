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

package org.vast.sweCommon;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataEncoding;
import org.vast.cdm.common.DataSink;
import org.vast.cdm.common.DataSource;
import org.vast.cdm.common.DataStreamParser;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.cdm.common.XmlEncoding;
import org.vast.data.DataList;


/**
 * <p><b>Title:</b>
 * SWE Data
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Implementation of SWE data including component definition,
 * data encoding, and data source as well as method to parse the 
 * corresponding incoming data.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Feb 21, 2007
 * @version 1.0
 */
public class SWEData
{
    protected DataEncoding dataEncoding;
    protected DataSource dataSource;
    protected DataList dataList;


    public SWEData()
    {
        dataList = new DataList();
    }
    
    
    public DataComponent getDataComponents()
    {
        return dataList.getListComponent();
    }


    public void setDataComponents(DataComponent dataComponents)
    {
        dataList.addComponent(dataComponents);
    }


    public DataEncoding getDataEncoding()
    {
        return dataEncoding;
    }


    public void setDataEncoding(DataEncoding dataEncoding)
    {
        this.dataEncoding = dataEncoding;
    }


    public DataSource getDataSource()
    {
        return dataSource;
    }


    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }
    
    
    public DataList getDataList()
    {
        return dataList;
    }


    public void setDataList(DataList dataList)
    {
        this.dataList = dataList;
    }
    
    
    /**
     * Retrieves parser created for this SWE structure/encoding pair
     * Allows the use of the parser on a separate input streams w/ same structure
     * @return
     */
    public DataStreamParser getDataParser()
    {
        DataStreamParser parser = SWEFactory.createDataParser(dataEncoding);
        parser.setDataComponents(getDataComponents());
        return parser;
    }
    
    
    /**
     * Retrieves writer created for this structure/encoding pair
     * Allows the use of the writer on separate output streams
     * @return
     */
    public DataStreamWriter getDataWriter()
    {
        DataStreamWriter writer = SWEFactory.createDataWriter(dataEncoding);
        writer.setDataComponents(getDataComponents());
        return writer;
    }
    
    
    /**
     * Parses data from the internally stored data source stream
     * and stores data blocks in a DataList 
     * @throws CDMException
     */
    public void parseData() throws CDMException
    {
    	assert(this.dataSource != null);
    	parseData(this.dataSource);
    }
    
    
    /**
     * Parses data from the given data source stream and stores
     * data blocks in the DataList
     * @param dataSource
     * @throws CDMException
     */
    public void parseData(DataSource dataSource) throws CDMException
    {
    	// special case for reading XML encoded stream from a DOM
        if (dataSource instanceof DataSourceDOM && dataEncoding instanceof XmlEncoding)
        {
        	DataSourceDOM domSrc = (DataSourceDOM)dataSource;
        	XmlDataParserDOM parser = new XmlDataParserDOM();
        	parser.setDataEncoding(dataEncoding);
        	parser.setDataComponents(getDataComponents());
        	parser.setDataHandler(new DefaultParserHandler(this));
        	parser.read(domSrc.getDom(), domSrc.getParentElt());
        }
        else
        {
        	DataStreamParser parser = getDataParser();
        	parser.setDataHandler(new DefaultParserHandler(this));        
        	InputStream dataStream = dataSource.getDataStream();
        	parser.parse(dataStream);
        }
    }
    
    
    /**
     * Validates all data against constraints specified in
     * the data components definition
     * @throws CDMException
     */
    public void validateData(List<CDMException> errorList)
    {
    	dataList.validateData(errorList);
    }
    
    
    /**
     * Writes data blocks to the data stream specified
     * @param buffer
     * @throws CDMException
     */
    public void writeData(DataSink dataSink) throws CDMException
    {
        // special case for writing XML encoded stream in a DOM
        if (dataSink instanceof DataSinkDOM && dataEncoding instanceof XmlEncoding)
        {
        	DataSinkDOM domSink = (DataSinkDOM)dataSink;
        	XmlDataWriterDOM writer = new XmlDataWriterDOM();
        	writer.setDataEncoding(dataEncoding);
        	writer.setDataComponents(getDataComponents());
        	writer.setDataHandler(new DefaultWriterHandler(this, writer));
        	writer.write(domSink.getDom(), domSink.getParentElt());
        }
        else
        {
        	DataStreamWriter writer = getDataWriter();
            writer.setDataHandler(new DefaultWriterHandler(this, writer));            
        	OutputStream dataStream = dataSink.getDataStream();
            writer.write(dataStream);
            writer.flush();
            dataSink.flush();
        }
    }
    
}
