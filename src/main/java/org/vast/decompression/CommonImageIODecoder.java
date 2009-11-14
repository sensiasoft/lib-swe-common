package org.vast.decompression;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.imageio.stream.MemoryCacheImageInputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.vast.cdm.common.BinaryBlock;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataBlock;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataType;
import org.vast.data.DataValue;
import org.vast.sweCommon.DataInputExt;


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
	protected boolean checkedForSize;
	protected byte [] block;
	protected ByteArrayInputStream byteArrayStream;
	protected InputStream stream;
	protected MemoryCacheImageInputStream imageStream;
	protected Color color;
	protected ImageReader imageReader;
	
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
		String compression = binaryBlock.compression;
		
		String mimeType = "";
		Iterator<ImageReader> readersList = null;
		
		if(compression.equals("jpeg") || compression.equals("jpg"))
		{
			mimeType = "image/jpeg";
		}
		else if(compression.equals("gif"))
		{
			mimeType = "image/gif";
		}
		else if(compression.equals("png"))
		{
			mimeType = "image/png";
		}
		readersList = ImageIO.getImageReadersByMIMEType(mimeType);
		
		if(readersList.hasNext())
		{
			imageReader = readersList.next();
		}
		else throw new CDMException("ImageIO cannot decode this image compression: " + compression);
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
    	
		
    	block = new byte[byteSize];
		try {
			inputStream.readFully(block);
		} catch (IOException e) {
			throw new CDMException("error when reading binary block of DataBlock " + blockInfo.getName(), e);
		}
		
		byteArrayStream = new ByteArrayInputStream(block);
		stream = (InputStream)byteArrayStream;
		imageStream = new MemoryCacheImageInputStream(stream);
		
		try {
			imageReader.setInput(imageStream);
			image = imageReader.read(0);			
		} catch (IOException e) {
			throw new CDMException("error when generating the compressed from the binary block of DataBlock " + blockInfo.getName(), e);
		}
		
		if(checkedForSize)
		if(blockInfo.getData().getAtomCount() != image.getWidth()*image.getHeight()*3)
		{
			throw new CDMException("the size of the decoded image is not that " +
								   "described in the Swe Common description");
		}
		checkedForSize = true;
		
		color = null;
		
		int m=0;
		for (int j = 0; j<image.getHeight(); j++){
			for (int i = 0; i<image.getWidth(); i++){
				color = new Color(image.getRGB(i, j));
				short red = (short)color.getRed();
				short green = (short)color.getGreen();
				short blue = (short)color.getBlue();

				blockInfo.getData().setShortValue(m, red);
				blockInfo.getData().setShortValue(m+1, green);
				blockInfo.getData().setShortValue(m+2, blue);
				m+=3;
			}
		}
		image = null;
		block = null;
		byteArrayStream = null;
		stream = null;
	}
	
}
