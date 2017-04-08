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
import java.io.InputStream;
import java.net.URI;
import org.vast.cdm.common.DataHandler;
import org.vast.cdm.common.DataStreamParser;
import org.vast.cdm.common.ErrorHandler;
import org.vast.cdm.common.RawDataHandler;
import org.vast.util.Asserts;
import net.opengis.swe.v20.BlockComponent;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;


/**
 * <p>
 * Base class for all SWE Common data stream parsers (text, binary, xml, json)
 * </p>
 *
 * @author Alex Robin
 * @since Dec 7, 2016
 */
public abstract class AbstractDataParser extends DataBlockProcessor implements DataStreamParser
{
    static final String INVALID_BOOLEAN_MSG = "Invalid boolean value: ";
    static final String INVALID_INTEGER_MSG = "Invalid integer value: ";
    static final String INVALID_DECIMAL_MSG = "Invalid decimal value: ";    
    static final String INVALID_CHOICE_MSG = "Invalid choice selector value: ";
    
    BlockComponent parentArray;
    int parentArrayIndex;
    
    DataEncoding dataEncoding;
    DataBlock dataBlk;    
    boolean renewDataBlock;    
    
    
    protected abstract boolean moreData() throws IOException;
    
    
    @Override
    public DataBlock parseNextBlock() throws IOException
    {
        try
        {
            if (!processorTreeReady)
            {
                dataComponents.accept(this);
                processorTreeReady = true;
                init();
            }
            
            if (!moreData())
                return null;
            
            // get datablock object
            if (dataBlk == null || renewDataBlock)
                getNextDataBlock();
            
            // go once through the tree of parser atoms
            int index = rootProcessor.process(dataBlk, 0);
            Asserts.checkState(index == dataBlk.getAtomCount(), "Data block wasn't fully deserialized");
            
            return dataBlk;
        }
        catch (Exception e)
        {
            throw new IOException("Error while parsing text record", e);
        }
    }
    
    
    @Override
    public void reset()
    {
        processorTreeReady = false;
    }
    

    protected DataBlock getNextDataBlock()
    {
        // depends if parsing to array block
        if (dataBlk != null)
            dataBlk = dataBlk.clone();
        else
            dataBlk = dataComponents.createDataBlock();
        
        return dataBlk;
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
        this.dataComponents = parentArray.getElementType();
        ((DataComponent)parentArray).renewDataBlock();
        parentArrayIndex = 0;
    }
    
    
    @Override
    public void setRenewDataBlock(boolean renewDataBlock)
    {
        // call reset once so that datablock is properly initialized
        if (renewDataBlock == false)
            reset();
        
        this.renewDataBlock = renewDataBlock;
    }
    
    
    @Override
    public void parse(String uri) throws IOException
    {
        throw new UnsupportedOperationException();
    }

    
    @Override
    public void parse(URI uri) throws IOException
    {
        throw new UnsupportedOperationException();
    }
    
    
    @Override
    public void parse(InputStream inputStream) throws IOException
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
