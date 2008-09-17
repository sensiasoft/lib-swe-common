package org.vast.decompression;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.JLabel;
import jj2000.j2k.decoder.Decoder;
import jj2000.j2k.util.ParameterList;

import org.vast.cdm.common.BinaryBlock;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataBlock;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataType;
import org.vast.data.DataBlockFactory;
import org.vast.data.DataValue;
import org.vast.sweCommon.DataInputExt;

import sun.awt.image.ToolkitImage;


/**
 * <p><b>Title:</b>
 * Jpeg2000Decoder
 * </p>
 *
 * <p><b>Description:</b><br/>
 * decoder to be used by the BinaryDataParser to decode a binary block 
 * that is compressed in jpeg2000. Handle single-tiled image only.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Gregoire Berthiau
 * @date Jul 16, 2008
 * @version 1.0
 */
public class Jpeg2000Decoder extends CompressedStreamReader
{
	protected String imageUrl;
	protected BufferedImage image;  
	protected DataBlock imageBlock;
	protected ParameterList list;
	protected Decoder dec;
	protected boolean fileBased;
	protected ByteArrayInputStream byteArrayInputStream;
	protected byte[] byteArray;
	
	public Jpeg2000Decoder(String filepath) 
	{
		this.imageUrl = "file:///" + filepath;
		this.fileBased = true;
	}

	
	public Jpeg2000Decoder(ByteArrayInputStream byteArrayInputStream) 
	{
		this.byteArrayInputStream = byteArrayInputStream;
		this.fileBased = false; 
	}
	
	public Jpeg2000Decoder() 
	{
	}


	 public byte[] getDecodedImageByteArray(){
			// Create parameter list using defaults
		 	if(fileBased){
		 		ParameterList list = new ParameterList(getDefaultParams());
		 		list.put("i", imageUrl);
		 		list.put("debug", "on");
		 		list.put("res", "5");
		 		dec = new Decoder(list);
		 	}

			if(!fileBased){
				ParameterList list = new ParameterList(getDefaultParams());
				list.put("res", "5");
				dec = new Decoder(list);
		 		dec.setByteArrayInputStream(byteArrayInputStream);
			}
			
			// Run the decoder
			try {
				dec.run();
			} catch (Throwable e) {
				e.printStackTrace();
			} 
			finally {
				if (dec.getExitCode() != 0) {
					System.exit(dec.getExitCode());
				}
			}

			Image imTmp = dec.getImage();
			waitForImage(imTmp);
			image = ((ToolkitImage)imTmp).getBufferedImage();
			setImageInByteArray();
			return byteArray;
		}
	 

	 private void waitForImage(Image img){
			MediaTracker tracker = new MediaTracker(new JLabel());
			tracker.addImage(img, 1);
			try {
				System.err.print("waiting...");
				tracker.waitForID(1);
				System.out.println(" image decoding done!");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	 
	 
	 private void setImageInByteArray(){
    	 // if DataBufferByte, just wrap image data with a DataBlock
        DataBuffer buf = image.getData().getDataBuffer();
        if (buf instanceof DataBufferByte)
        {
            byte[] data = ((DataBufferByte)buf).getData();
            byteArray = data;
        }
        else if (buf instanceof DataBufferInt)
        {
            int[] data = ((DataBufferInt)buf).getData();
            byte[] byteData = new byte[data.length*3];
            
            for (int i=0; i<data.length; i++)
            {
                int b = i*3;
                byteData[b+2] = (byte)(data[i] & 0xFF);
                byteData[b+1] = (byte)((data[i] >> 8) & 0xFF);
                byteData[b] = (byte)((data[i] >> 16) & 0xFF);
            }
            byteArray = byteData;
        }
    }
	
	 private DataBlock getDecodedImageDataBlock(){
    	
        byte [] bytes = getDecodedImageByteArray();
        imageBlock = DataBlockFactory.createBlock(bytes);
        return imageBlock;
    }
	 
	protected ParameterList getDefaultParams(){
		ParameterList def  = new ParameterList();
		String[][] param = Decoder.getAllParameters();

		for (int i = param.length - 1; i >= 0; i--) {
			if (param[i][3] != null)
				def.put(param[i][0], param[i][3]);
		
		}
		return def;
	}


	public static void main(String [] args){
		try
        {
            // Create parameter list using defaults
           // ProcessLoader.processMap = new Hashtable<String,String>();
			long t0 = System.currentTimeMillis();
			
			String imagepath = "C:/Data/20080717/jp2/1216308900.953000.jp2";
			
		//	Jpeg2000Decoder prov = new Jpeg2000Decoder(imagepath);
			
			RandomAccessFile inputData = new RandomAccessFile(new File(imagepath), "r");
			long sizeInByte = inputData.length();
			byte[] b = new byte[(int)sizeInByte];
			inputData.readFully(b);

			ByteArrayInputStream is = new ByteArrayInputStream(b);
			
			Jpeg2000Decoder prov = new Jpeg2000Decoder(is);
			
			int min=200, max =-200;
			byte[] bb = prov.getDecodedImageByteArray();
			for(int i=0; i<bb.length; i++){
				if(bb[i]<min)
					min=bb[i];
				if(bb[i]>max)
					max=bb[i];
				
			}
			System.out.println(min + "   " +  max);
			long t1 = System.currentTimeMillis();
            long a = t1-t0;
            System.out.println(a);
            //prov.dispImg();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}


	@Override
	public void init(BinaryBlock binaryBlock, DataComponent blockComponent) throws CDMException {
		
		DataComponent primitiveRecord = blockComponent.getComponent(0).getComponent(0);
		for(int i = 0; i<primitiveRecord.getComponentCount(); i++)
		{
			((DataValue)primitiveRecord.getComponent(i)).setDataType(DataType.BYTE);
		}
	}


	@Override
	protected void parse(DataInputExt inputStream, int byteSize, DataComponent blockInfo)
			throws CDMException {

		byte [] block = new byte[byteSize];
		try {
			inputStream.readFully(block);
		} catch (IOException e) {
			throw new CDMException("error when reading binary block of DataBlock " + blockInfo.getName(), e);
		}
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(block);		
		Jpeg2000Decoder jp2kDecoder = new Jpeg2000Decoder(byteArrayInputStream);
		byte [] byteArray = jp2kDecoder.getDecodedImageByteArray();
				
		for (int i = 0; i<byteArray.length; i++){
			int  Byte = (0x000000FF & ((int)byteArray[i]));
	        byte unsignedByte = (byte)Byte;
			blockInfo.getData().setByteValue(i, unsignedByte);
		}
	}


}
