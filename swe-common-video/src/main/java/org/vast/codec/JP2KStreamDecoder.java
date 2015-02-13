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
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.

 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.codec;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import jj2000.j2k.io.RandomAccessIO;
import jj2000.j2k.util.ISRandomAccessIO;
import net.opengis.swe.v20.BinaryBlock;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataType;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.CompressedStreamParser;
import org.vast.cdm.common.DataInputExt;
import org.vast.data.DataValue;


/**
 * <p>
 * Decoder used by the BinaryDataParser to decode a binary block compressed in JPEG 2000.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Oct 16, 2009
 * */
public class JP2KStreamDecoder implements CompressedStreamParser
{
    protected JP2KDecoder jp2kDecoder;
    protected long fixedSize = -1;

    
    public JP2KStreamDecoder()
    {
        this.jp2kDecoder = new JP2KDecoder();
    }
    
    
    public JP2KStreamDecoder(long fixedSize)
    {
        this();
        this.fixedSize = fixedSize;
    }


    public void init(DataComponent blockComponent, BinaryBlock binaryBlock) throws CDMException
    {
        DataComponent primitiveRecord = blockComponent.getComponent(0).getComponent(0);
        
        for (int i = 0; i < primitiveRecord.getComponentCount(); i++)
        {
            ((DataValue) primitiveRecord.getComponent(i)).setDataType(DataType.BYTE);
        }
        
        // TODO set decoder parameter??
    }

    
    public void decode(DataInputExt inputStream, DataComponent blockComponent) throws CDMException
    {
        int byteSize = -1;

        try
        {
            byteSize = (int)(fixedSize > 0 ? fixedSize : inputStream.readLong());
            if (byteSize <= 0)
                throw new CDMException("Invalid block size: " + byteSize);
            
            byte[] buffer = new byte[byteSize];
            inputStream.readFully(buffer);
            InputStream is = new ByteArrayInputStream(buffer);
            RandomAccessIO in = new ISRandomAccessIO(is, byteSize, 1<<18, 1<<24);
            byte[][] decodedImage = jp2kDecoder.decode(in);
            
            // assign value to output data block
            for (int c = 0; c < decodedImage.length; c++)
            {
                byte[] bandData = decodedImage[c];
                for (int p = 0; p < bandData.length; p++)
                {
                    int index = p*decodedImage.length + c;
                    blockComponent.getData().setByteValue(index, bandData[p]);
                }
            }
        }
        catch (IOException e)
        {
            throw new CDMException("Error when decoding " + byteSize + "B block for " + blockComponent.getName(), e);
        }
    }
}
