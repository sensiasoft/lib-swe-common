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

import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;
import org.vast.cdm.common.DataHandler;
import org.vast.cdm.common.DataStreamWriter;


/**
 * <p>
 * Default handler used by SWE Data to write all values
 * from the data list within the SWE Data.
 * </p>
 *
 * @author Alex Robin
 * @since Feb 23, 2007
 * */
public class DefaultWriterHandler implements DataHandler
{
    protected SWEData sweData;
    protected DataStreamWriter writer;
    protected int blockCount = 0;
    protected int blockNum = 0;
    
    
    public DefaultWriterHandler(SWEData sweData, DataStreamWriter writer)
    {
        this.sweData = sweData;
        this.writer = writer;
        this.blockCount = sweData.getComponentCount();
    }
    
    
    @Override
    public void startData(DataComponent info)
    {
        //System.out.println("start data: " + info.getName());
        
    	if (blockNum < blockCount)
        {
            info.setData(sweData.getComponent(blockNum).getData());
            blockNum++;
        }            
    }
    
    
    @Override
    public void endData(DataComponent info, DataBlock data)
    {
    	//System.out.println("end data: " + info.getName());
        
    	if (blockNum >= blockCount)
    		writer.stop();
    }
    
    
    @Override
    public void beginDataAtom(DataComponent info)
    {
    	//System.out.println("begin atom: " + info.getName());        
    }


    @Override
    public void endDataAtom(DataComponent info, DataBlock data)
    {
    	//System.out.println("end atom: " + info.getName());
    }


    @Override
    public void startDataBlock(DataComponent info)
    {
    	//System.out.println("begin block: " + info.getName());
    }


    @Override
    public void endDataBlock(DataComponent info, DataBlock data)
    {
    	//System.out.println("end block: " + info.getName());
    }

}
