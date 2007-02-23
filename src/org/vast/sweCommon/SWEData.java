/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
 Alexandre Robin <robin@nsstc.uah.edu>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.sweCommon;

import java.io.IOException;
import java.io.InputStream;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataEncoding;
import org.vast.cdm.common.DataSource;
import org.vast.cdm.common.DataStreamParser;
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
    protected DataComponent dataComponents;
    protected DataEncoding dataEncoding;
    protected DataSource dataSource;
    protected DataList dataBlocks;


    public DataComponent getDataComponents()
    {
        return dataComponents;
    }


    public void setDataComponents(DataComponent dataComponents)
    {
        this.dataComponents = dataComponents;
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
    
    
    public DataList getDataBlocks()
    {
        return dataBlocks;
    }


    public void setDataBlocks(DataList dataList)
    {
        this.dataBlocks = dataList;
    }
    
    
    /**
     * Retrieves parser created for this SWE structure/encoding pair
     * Allows the use of the parser on a different stream w/ same structure
     * @return
     */
    public DataStreamParser getDataParser()
    {
        DataStreamParser parser = SWEFactory.createDataParser(dataEncoding);
        parser.setDataComponents(dataComponents);
        return parser;
    }
    
    
    /**
     * Parses the data source stream and stores data blocks in a DataList 
     * @throws CDMException
     */
    public void parseResult() throws CDMException
    {
        DataStreamParser parser = getDataParser();
        dataBlocks = new DataList();
        dataBlocks.addComponent(dataComponents);
        parser.setDataHandler(new DefaultHandler(this));
        
        InputStream resultStream = dataSource.getDataStream();
        parser.parse(resultStream);
        
        try { resultStream.close(); }
        catch (IOException e) { e.printStackTrace(); }
    }
    
}
