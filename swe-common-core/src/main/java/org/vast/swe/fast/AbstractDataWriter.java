/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2016 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.fast;

import java.io.IOException;
import java.io.OutputStream;
import org.vast.cdm.common.DataHandler;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.cdm.common.ErrorHandler;
import org.vast.cdm.common.RawDataHandler;
import org.vast.util.Asserts;
import net.opengis.swe.v20.BlockComponent;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataEncoding;


/**
 * <p>
 * Base class for all SWE Common data stream writers (text, binary, xml, json)
 * </p>
 *
 * @author Alex Robin
 * @since Dec 7, 2016
 */
public abstract class AbstractDataWriter extends DataBlockProcessor implements DataStreamWriter
{
    BlockComponent parentArray;
    int parentArrayIndex;    
    DataEncoding dataEncoding;
    
        
    @Override
    public void write(DataBlock data) throws IOException
    {
        Asserts.checkNotNull(data, "DataBlock");
        
        try
        {
            if (!processorTreeReady)
            {
                checkEnabled(dataComponents);
                dataComponents.accept(this);
                processorTreeReady = true;
                init();
            }
            
            // go once through the tree of parser atoms
            int index = rootProcessor.process(data, 0);
            Asserts.checkState(index == data.getAtomCount(), "Data block wasn't fully serialized");
        }
        catch (Exception e)
        {
            throw new IOException("Error while writing record", e);
        }
    }
    
    
    @Override
    public void startStream(boolean multipleRecords) throws IOException
    {
    }
    
    
    @Override
    public void endStream() throws IOException
    {
    }


    @Override
    public void reset()
    {
        processorTreeReady = false;
    }


    @Override
    public void setDataEncoding(DataEncoding encoding)
    {
        this.dataEncoding = encoding;
        this.processorTreeReady = false;
    }


    @Override
    public DataEncoding getDataEncoding()
    {
        return this.dataEncoding;
    }
    
    
    @Override
    public void setParentArray(BlockComponent parentArray)
    {
        this.parentArray = parentArray;
        parentArrayIndex = 0;
    }
    
    
    @Override
    public void write(OutputStream outputStream) throws IOException
    {
        throw new UnsupportedOperationException();
    }
    
    
    @Override
    public DataHandler getDataHandler()
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public RawDataHandler getRawDataHandler()
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public ErrorHandler getErrorHandler()
    {
        throw new UnsupportedOperationException();
    }
    

    @Override
    public void setDataHandler(DataHandler handler)
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public void setRawDataHandler(RawDataHandler handler)
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public void setErrorHandler(ErrorHandler handler)
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public void stop()
    {
        throw new UnsupportedOperationException();
    }

}
