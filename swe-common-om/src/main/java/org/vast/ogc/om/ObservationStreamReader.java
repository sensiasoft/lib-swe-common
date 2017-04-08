/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.om;

import java.io.IOException;
import java.io.InputStream;
import org.vast.cdm.common.DataHandler;
import org.vast.swe.SWEReader;


/**
 * <p>
 * Base interface for Streaming Observation Readers of all versions
 * </p>
 *
 * @author Alex Robin
 * @since May 22, 2008
 * */
public abstract class ObservationStreamReader extends SWEReader
{
    
    /**
     * Reads an Observation object from the given InputStream
     * @param inputStream
     * @param dataHandler
     * @throws IOException
     */
    public void readObservationStream(InputStream inputStream, DataHandler dataHandler) throws IOException
    {
        parse(inputStream, dataHandler);
    }

    
    public abstract double[] getFoiLocation();


    public abstract String getObservationName();


    public abstract String getProcedure();
}
