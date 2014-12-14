/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2014 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.vast.cdm.common.DataStreamParser;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.swe.SWEFactory;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.BlockComponent;
import net.opengis.swe.v20.ByteEncoding;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataStream;
import net.opengis.swe.v20.EncodedValues;


/**
 * <p>
 * Full implementation of EncodedValues wrapping SWE Common data stream 
 * parsers/writers allowing decoding/encoding of any encoded array or stream
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Oct 3, 2014
 */
public class EncodedValuesImpl extends net.opengis.OgcPropertyImpl<Object> implements EncodedValues
{
        
    
    @Override
    public boolean resolveHref()
    {
        // TODO Add support for remote data
        return false;
    }
    
    
    public void decode(BlockComponent array, DataEncoding encoding, String text)
    {
        try
        {
            DataStreamParser parser = SWEFactory.createDataParser(encoding);
            parser.setParentArray(array);
            InputStream is = new ByteArrayInputStream(text.getBytes());
            parser.parse(is);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error while parsing encoded values", e);
        }        
    }


    public String encode(BlockComponent array, DataEncoding encoding)
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream(array.getData().getAtomCount()*10);
        
        // force base64 if byte encoding is raw
        if (encoding instanceof BinaryEncoding)
            ((BinaryEncoding) encoding).setByteEncoding(ByteEncoding.BASE_64);
                
        // write values with proper encoding to byte array
        try
        {
            DataStreamWriter writer = SWEFactory.createDataWriter(encoding);
            writer.setDataComponents(array.getElementType().copy());
            writer.setOutput(os);
            
            for (int i = 0; i < array.getElementCount().getValue(); i++)
            {
                DataBlock nextBlock = array.getComponent(i).getData();
                writer.write(nextBlock);
            }
            
            writer.flush();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error while writing encoded values", e);
        }
        
        // convert byte array to string
        return os.toString();     
    }


    @Override
    public void setAsText(DataArray array, DataEncoding encoding, String text)
    {
        decode(array, encoding, text);
    }


    @Override
    public String getAsText(DataArray array, DataEncoding encoding)
    {
        return encode(array, encoding);     
    }


    @Override
    public void setAsText(DataStream dataStream, DataEncoding encoding, String text)
    {
        decode(dataStream, encoding, text);        
    }


    @Override
    public String getAsText(DataStream dataStream, DataEncoding encoding)
    {
        return encode(dataStream, encoding);        
    }

}
