/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import colorspace.ColorSpace;
import jj2000.j2k.codestream.HeaderInfo;
import jj2000.j2k.codestream.reader.BitstreamReaderAgent;
import jj2000.j2k.codestream.reader.HeaderDecoder;
import jj2000.j2k.decoder.Decoder;
import jj2000.j2k.decoder.DecoderSpecs;
import jj2000.j2k.entropy.decoder.EntropyDecoder;
import jj2000.j2k.fileformat.reader.FileFormatReader;
import jj2000.j2k.image.BlkImgDataSrc;
import jj2000.j2k.image.DataBlk;
import jj2000.j2k.image.DataBlkInt;
import jj2000.j2k.image.ImgDataConverter;
import jj2000.j2k.image.invcomptransf.InvCompTransf;
import jj2000.j2k.io.RandomAccessIO;
import jj2000.j2k.quantization.dequantizer.Dequantizer;
import jj2000.j2k.roi.ROIDeScaler;
import jj2000.j2k.util.ParameterList;
import jj2000.j2k.wavelet.synthesis.InvWTFull;
import jj2000.j2k.wavelet.synthesis.InverseWT;


public class JP2KDecoder
{
    protected Logger log = LoggerFactory.getLogger(JP2KDecoder.class);
    
    
    public JP2KDecoder()
    {        
    }
    
    
    public byte[][] decode(RandomAccessIO in)
    {
        try
        {
            long start = System.currentTimeMillis();
            ParameterList pl = getDefaultParameters();
                        
            FileFormatReader ff = new FileFormatReader(in);
            ff.readFileFormat();
            if (ff.JP2FFUsed)
                in.seek(ff.getFirstCodeStreamPos());
            
            HeaderInfo hi = new HeaderInfo();
            HeaderDecoder hd = new HeaderDecoder(in, pl, hi);
            DecoderSpecs decSpec = hd.getDecoderSpecs();
            
            if (log.isDebugEnabled())
                log.debug("Codestream Header Info" + hi.toStringMainHeader());
            
            // read header options
            int nComps = hd.getNumComps();
            int[] bitDepths = new int[nComps];
            for (int i = 0; i < nComps; i++)
                bitDepths[i] = hd.getOriginalBitDepth(i);

            // construct JJ2000 processing chain
            BitstreamReaderAgent bstreamReader = BitstreamReaderAgent.createInstance(in, hd, pl, decSpec, false, hi);
            EntropyDecoder entdec = hd.createEntropyDecoder(bstreamReader, pl);
            ROIDeScaler roids = hd.createROIDeScaler(entdec,pl,decSpec);
            Dequantizer deq = hd.createDequantizer(roids, bitDepths, decSpec);
            
            InverseWT inverseWT = new InvWTFull(deq, decSpec);
            int res = bstreamReader.getImgRes();
            inverseWT.setImgResLevel(res);
            
            ImgDataConverter converter = new ImgDataConverter(inverseWT, 0);
            InvCompTransf ictransf = new InvCompTransf(converter, decSpec, bitDepths, pl);
            BlkImgDataSrc decodedImg = ictransf;
            
            if (ff.JP2FFUsed)
            {
                ColorSpace csMap = new ColorSpace(in, hd, pl);
                BlkImgDataSrc channels = hd.createChannelDefinitionMapper(ictransf, csMap);
                BlkImgDataSrc resampled  = hd.createResampler(channels, csMap);
                //BlkImgDataSrc palettized = hd.createPalettizedColorSpaceMapper (resampled, csMap);
                decodedImg = hd.createColorSpaceMapper(resampled, csMap);
            
                if (log.isDebugEnabled())
                {
                    log.debug("Channel Definition Mapper: " + channels);
                    log.debug("Resampler: " + decodedImg);
                    //log.debug("Palettized Color Space Mapper: " + palettized);
                    log.debug("Enumerated Color Space Mapper: " + decodedImg);
                }
            }
        
            decodedImg.setTile(0, 0);
            int numComps = decodedImg.getNumComps();
            int width = decodedImg.getImgWidth();
            int height = decodedImg.getImgHeight();
            byte[][] imageData = new byte[numComps][];
            
            for (int c = 0; c < numComps; c++)
            {
                long startComp = System.currentTimeMillis();
                
                // decode whole frame
                DataBlkInt roi = new DataBlkInt(0, 0, width, height);
                DataBlk decodedData = decodedImg.getInternCompData(roi, c);
                int[] intData = (int[])decodedData.getData();

                if (log.isDebugEnabled())
                {
                    long dt = System.currentTimeMillis() - startComp;
                    
                    log.debug("Component Width:  " + decodedImg.getCompImgWidth(c));
                    log.debug("Component Height: " + decodedImg.getCompImgHeight(c));
                    log.debug("Component Bits: " + decodedImg.getNomRangeBits(c));
                    log.debug("Fixed Point: " + decodedImg.getFixedPoint(c));                                        
                    log.debug("Decompressed " + width + "x" + height + " component in " + dt + " ms\n");
                }
                
                // convert to unsigned byte array normalized to 0-255
                int maxVal = (1 << decodedImg.getNomRangeBits(c)) - 1;
                int shift = 1 << (decodedImg.getNomRangeBits(c) - 1);
                imageData[c] = new byte[intData.length];
                for (int p=0; p<intData.length; p++)
                {
                    int val = intData[p] + shift;
                    imageData[c][p] = (byte)((val < 0) ? 0 : ((val > maxVal) ? maxVal : val));
                }
            }
            
            if (log.isDebugEnabled())
            {
                long dt = System.currentTimeMillis() - start;
                log.debug("Decompressed " + width + "x" + height + "x" + numComps + " frame in " + dt + " ms");
            }
            
            return imageData;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public void setParameter()
    {
        
    }
    
    
    protected static ParameterList getDefaultParameters()
    {
        String[][] param = Decoder.getAllParameters();
        ParameterList defpl = new ParameterList();
        
        for (int i=param.length-1; i>=0; i--)
        {
            if (param[i][3] != null)
                defpl.put(param[i][0], param[i][3]);
        }

        // Create parameter list using defaults
        return new ParameterList(defpl);
    }
}
