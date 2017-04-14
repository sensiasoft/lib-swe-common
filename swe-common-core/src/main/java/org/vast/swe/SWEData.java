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
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.XMLEncoding;
import org.vast.cdm.common.DataSink;
import org.vast.cdm.common.DataSource;
import org.vast.cdm.common.DataStreamParser;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.data.DataList;


/**
 * <p>
 * Implementation of SWE input/output data stream storing data in memory,
 * This class also contains methods for parsing/writing the stored data.
 * </p>
 *
 * @author Alex Robin
 * @since Feb 21, 2007
 * */
public class SWEData extends DataList implements ISweInputDataStream, ISweOutputDataStream
{
    private static final long serialVersionUID = 1745973314151779348L;
    protected transient DataSource dataSource;
    

    public SWEData()
    {
    }
    
    
    @Override
    public SWEData copy()
    {
        SWEData newObj = new SWEData();
        copyTo(newObj);
        newObj.dataSource = this.dataSource;
        return newObj;
    }
    
    
    @Override
    public DataComponent getNextElement()
    {
        return nextComponent();
    }
    
    
    @Override
    public DataBlock getNextDataBlock()
    {
        return nextDataBlock();
    }
    
    
    @Override
    public void pushNextDataBlock(DataBlock dataBlock)
    {
        addData(dataBlock);
    }


    public DataSource getDataSource()
    {
        return dataSource;
    }


    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }
    
    
    /**
     * Retrieves parser created for this SWE structure/encoding pair
     * Allows the use of the parser on a separate input streams w/ same structure
     * @return parser instance
     */
    public DataStreamParser getDataParser()
    {
        DataStreamParser parser = SWEHelper.createDataParser(getEncoding());
        parser.setDataComponents(getElementType());
        return parser;
    }
    
    
    /**
     * Retrieves writer created for this structure/encoding pair
     * Allows the use of the writer on separate output streams
     * @return writer instance
     */
    public DataStreamWriter getDataWriter()
    {
        DataStreamWriter writer = SWEHelper.createDataWriter(getEncoding());
        writer.setDataComponents(getElementType());
        return writer;
    }
    
    
    /**
     * Parses data from the internally stored data source stream
     * and stores data blocks in a DataList 
     * @throws IOException
     */
    public void parseData() throws IOException
    {
    	assert(this.dataSource != null);
    	parseData(this.dataSource);
    }
    
    
    /**
     * Parses data from the given data source stream and stores
     * data blocks in the DataList
     * @param dataSource
     * @throws IOException
     */
    public void parseData(DataSource dataSource) throws IOException
    {
        DataEncoding encoding = getEncoding();
    	    	
        // special case for reading XML encoded stream from a DOM
        if (dataSource instanceof DataSourceDOM && encoding instanceof XMLEncoding)
        {
        	DataSourceDOM domSrc = (DataSourceDOM)dataSource;
        	XmlDataParserDOM parser = new XmlDataParserDOM();
        	parser.setDataEncoding(encoding);
        	parser.setDataComponents(getElementType());
        	parser.setDataHandler(new DefaultParserHandler(this));
        	parser.read(domSrc.getDom(), domSrc.getParentElt());
        }
        else
        {
            if (dataSource instanceof DataSourceDOM)
                encoding = SWEHelper.ensureXmlCompatible(encoding);
       
            DataStreamParser parser = SWEHelper.createDataParser(encoding);
            parser.setDataComponents(getElementType());
        	parser.setDataHandler(new DefaultParserHandler(this));        	
        	parser.parse(dataSource.getDataStream());
        }
    }
    
    
    /**
     * Writes data blocks to the data stream specified
     * @param dataSink
     * @throws IOException
     */
    public void writeData(DataSink dataSink) throws IOException
    {
        DataEncoding encoding = getEncoding();
        
        // special case for writing XML encoded stream in a DOM
        if (dataSink instanceof DataSinkDOM && encoding instanceof XMLEncoding)
        {
        	DataSinkDOM domSink = (DataSinkDOM)dataSink;
        	XmlDataWriterDOM writer = new XmlDataWriterDOM();
        	writer.setDataEncoding(encoding);
        	writer.setDataComponents(getElementType());
        	writer.setDataHandler(new DefaultWriterHandler(this, writer));
        	writer.write(domSink.getDom(), domSink.getParentElt());
        }
        else
        {
            if (dataSink instanceof DataSinkDOM)
                encoding = SWEHelper.ensureXmlCompatible(encoding);
            
            DataStreamWriter writer = SWEHelper.createDataWriter(encoding);
            writer.setParentArray(this);
        	writer.write(dataSink.getDataStream());
            writer.flush();
            dataSink.flush();
        }
    }


    @Override
    public SWEData clone()
    {
        return (SWEData)super.clone();
    }


    @Override
    public void setElementType(DataComponent elementType)
    {
        this.setElementType(elementType.getName(), elementType);        
    }


    @Override
    public int getNumElements()
    {
        return getComponentCount();
    } 
}
