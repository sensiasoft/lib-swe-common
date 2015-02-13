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

import java.awt.Dimension;
import java.io.IOException;
import javax.media.Buffer;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import net.opengis.swe.v20.BinaryBlock;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataType;
import net.sourceforge.jffmpeg.VideoDecoder;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.CompressedStreamParser;
import org.vast.cdm.common.DataInputExt;
import org.vast.data.DataValue;


/**
 * <p>
 * Decoder used by the BinaryDataParser to decode a binary block compressed in MPEG2.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Oct 16, 2009
 * */
public class MPEG2StreamDecoder implements CompressedStreamParser
{
    protected VideoDecoder mpegDecoder;
    protected byte[] frameData;
    
    
    public MPEG2StreamDecoder()
    {
        // create video format and decoder
        VideoFormat inputFormat = new VideoFormat("MPEG", new Dimension(720, 480), -1, byte[].class, -1);
        mpegDecoder = new VideoDecoder();
        mpegDecoder.setInputFormat(inputFormat);
        mpegDecoder.setOutputFormat(new RGBFormat());        
    }


    public void init(DataComponent blockComponent, BinaryBlock binaryBlock) throws CDMException
    {
        DataComponent primitiveRecord = blockComponent.getComponent(0).getComponent(0);
        
        for (int i = 0; i < primitiveRecord.getComponentCount(); i++)
        {
            ((DataValue) primitiveRecord.getComponent(i)).setDataType(DataType.BYTE);
        }
        
        // TODO set decoder parameter??
        mpegDecoder.reset();
    }

    
    public void decode(DataInputExt inputStream, DataComponent blockComponent) throws CDMException
    {
        int frameSize = -1;

        try
        {
            // read frame size
            frameSize = inputStream.readInt();
            //System.out.println("Frame size = " + frameSize);
            if (frameSize <= 0)
                throw new CDMException("Invalid block size: " + frameSize);
            
            // read frame to memory buffer
            if (frameData == null || frameData.length < frameSize)
                frameData = new byte[frameSize];
            inputStream.readFully(frameData, 0, frameSize);
            Buffer buffer = new Buffer();
            buffer.setData(frameData);
            buffer.setLength(frameSize);
            
            // decode mpeg into RGB
            Buffer rgbFrame = new Buffer();
            mpegDecoder.process(buffer, rgbFrame);
            
            // write RGB values in datablock
            if (rgbFrame.getData() != null)
            {
                int[] rgbData = (int[])rgbFrame.getData();
                for (int p=0; p<rgbData.length; p++)
                {
                    blockComponent.getData().setByteValue(p*3, (byte)rgbData[p]);
                    blockComponent.getData().setByteValue(p*3 + 1, (byte)(rgbData[p] >> 8));
                    blockComponent.getData().setByteValue(p*3 + 2, (byte)(rgbData[p] >> 16));
                }
            }
        }
        catch (IOException e)
        {
            throw new CDMException("Error when decoding " + frameSize + "B block for " + blockComponent.getName(), e);
        }
    }
}
