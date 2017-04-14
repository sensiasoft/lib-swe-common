/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.vast.cdm.common.DataStreamParser;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.swe.SWEHelper;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.BlockComponent;
import net.opengis.swe.v20.ByteEncoding;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataStream;
import net.opengis.swe.v20.EncodedValues;


/**
 * <p>
 * Full implementation of EncodedValues wrapping SWE Common data stream 
 * parsers/writers allowing decoding/encoding of any encoded array or stream
 * </p>
 *
 * @author Alex Robin
 * @since Oct 3, 2014
 */
public class EncodedValuesImpl extends OgcPropertyImpl<byte[]> implements EncodedValues
{
    private static final long serialVersionUID = 5065676107640449321L;


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
            DataStreamParser parser = SWEHelper.createDataParser(encoding);
            parser.setParentArray(array);
            InputStream is = new ByteArrayInputStream(text.getBytes());
            parser.parse(is);
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Cannot parse encoded values", e);
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
            DataStreamWriter writer = SWEHelper.createDataWriter(encoding);
            writer.setParentArray(array);
            writer.write(os);            
            writer.flush();
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Cannot write encoded values", e);
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
