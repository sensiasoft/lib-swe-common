package org.vast.decompression;

import java.awt.Color;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JLabel;

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
 * CommonImageDecoder
 * </p>
 *
 * <p><b>Description:</b><br/>
 * decoder to be used by the BinaryDataParser to decode a binary block 
 * that is compressed in JPEG and TIFF, PNG, ... 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Gregoire Berthiau
 * @date Jul 16, 2008
 * @version 1.0
 */
public class CommonImageIODecoder extends CompressedStreamReader
{
	protected String imageUrl;
	protected BufferedImage image;  
	protected DataBlock imageBlock;
	protected boolean fileBased;
	protected ByteArrayInputStream byteArrayInputStream;
	protected byte[] byteArray;
	
	
	public CommonImageIODecoder() 
	{
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
	protected void parse(DataInputExt inputStream, DataComponent blockInfo)
			throws CDMException {

		int byteSize = 0;
    	
    	try
    	{
    		byteSize = (int)inputStream.readLong();
    	}
    	catch (IOException e)
		{
			throw new CDMException("Error while reading binary stream", e);
		}
    	
		
		byte [] block = new byte[byteSize];
		try {
			inputStream.readFully(block);
		} catch (IOException e) {
			throw new CDMException("error when reading binary block of DataBlock " + blockInfo.getName(), e);
		}
		
		ByteArrayInputStream byteArrayStream = new ByteArrayInputStream(block);
		InputStream stream = (InputStream)byteArrayStream;
		//ImageInputStream imageInputStream = (ImageInputStream) byteArrayStream;
		BufferedImage image;
		try {
			image = ImageIO.read(stream);
		} catch (IOException e) {
			throw new CDMException("error when generating the compressed from the binary block of DataBlock " + blockInfo.getName(), e);
		}
		if(blockInfo.getData().getAtomCount() != image.getWidth()*image.getHeight()*3)
		{
			throw new CDMException("the size of the decoded image is not that " +
								   "described in the Swe Common description");
		}
		int [] rgbArray = new int[image.getWidth()*image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), rgbArray, 0, 0);
		Color color = null;
			
		for (int i = 0; i<rgbArray.length; i++){
			color = new Color(rgbArray[i]);
		    short red = (short)color.getRed();
		    short green = (short)color.getGreen();
		    short blue = (short)color.getBlue();
		    blockInfo.getData().setShortValue(i, red);
			blockInfo.getData().setShortValue(i+1, green);
			blockInfo.getData().setShortValue(i+2, blue);
		}
	}
	
}
