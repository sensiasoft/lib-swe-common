/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the
 
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
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Feb 23, 2007
 * */
public class DefaultParserHandler implements DataHandler
{
    protected SWEData sweData;
    
    
    public DefaultParserHandler(SWEData sweData)
    {
        this.sweData = sweData;
    }
    
    
    public void endData(DataComponent info, DataBlock data)
    {
        sweData.addData(data);
    }
    
    
    public void beginDataAtom(DataComponent info)
    {
    }


    public void endDataAtom(DataComponent info, DataBlock data)
    {
    }


    public void startDataBlock(DataComponent info)
    {
    }


    public void endDataBlock(DataComponent info, DataBlock data)
    {
    }


    public void startData(DataComponent info)
    {
    }

}