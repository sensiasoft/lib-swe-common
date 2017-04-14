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


/**
 * <p>
 * Default handler used by SWE Data to read all values
 * from the data stream to the list within the SWE Data.
 * </p>
 *
 * @author Alex Robin
 * @since Feb 23, 2007
 * */
public class DefaultParserHandler implements DataHandler
{
    protected SWEData sweData;
    
    
    public DefaultParserHandler(SWEData sweData)
    {
        this.sweData = sweData;
    }
    
    
    @Override
    public void endData(DataComponent info, DataBlock data)
    {
        sweData.addData(data);
    }
    
    
    @Override
    public void beginDataAtom(DataComponent info)
    {
    }


    @Override
    public void endDataAtom(DataComponent info, DataBlock data)
    {
    }


    @Override
    public void startDataBlock(DataComponent info)
    {
    }


    @Override
    public void endDataBlock(DataComponent info, DataBlock data)
    {
    }


    @Override
    public void startData(DataComponent info)
    {
    }

}
