
package org.vast.codec;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import jj2000.j2k.decoder.Decoder;
import jj2000.j2k.util.ParameterList;
import net.opengis.swe.v20.BinaryBlock;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataType;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.CompressedStreamParser;
import org.vast.cdm.common.DataInputExt;
import org.vast.data.DataValue;


/**
 * <p>
 * decoder to be used by the BinaryDataParser to decode a binary block 
 * that is compressed in JPEG and TIFF, PNG, ... 
 * </p>
 *
 * @author Gregoire Berthiau
 * @since Jul 16, 2008
 * */
public class CommonImageIODecoder implements CompressedStreamParser
{
    protected String imageUrl;
    protected BufferedImage image;
    protected DataBlock imageBlock;
    protected ParameterList list;
    protected Decoder dec;
    protected boolean fileBased;
    protected ByteArrayInputStream byteArrayInputStream;
    protected byte[] byteArray;
    protected boolean checkedForSize;
    protected byte[] block;
    protected ByteArrayInputStream byteArrayStream;
    protected InputStream stream;
    protected MemoryCacheImageInputStream imageStream;
    protected Color color;
    protected ImageReader imageReader;


    public CommonImageIODecoder()
    {
    }


    public void init(DataComponent blockComponent, BinaryBlock binaryBlock) throws CDMException
    {

        DataComponent primitiveRecord = blockComponent.getComponent(0).getComponent(0);
        for (int i = 0; i < primitiveRecord.getComponentCount(); i++)
        {
            ((DataValue) primitiveRecord.getComponent(i)).setDataType(DataType.BYTE);
        }
        String compression = binaryBlock.getCompression();

        String mimeType = "";
        Iterator<ImageReader> readersList = null;

        if (compression.equals("jpeg") || compression.equals("jpg"))
        {
            mimeType = "image/jpeg";
        }
        else if (compression.equals("gif"))
        {
            mimeType = "image/gif";
        }
        else if (compression.equals("png"))
        {
            mimeType = "image/png";
        }
        readersList = ImageIO.getImageReadersByMIMEType(mimeType);

        if (readersList.hasNext())
        {
            imageReader = readersList.next();
        }
        else
            throw new CDMException("ImageIO cannot decode this image compression: " + compression);
    }


    public void decode(DataInputExt inputStream, DataComponent blockComponent) throws CDMException
    {

        int byteSize = 0;

        try
        {
            byteSize = (int) inputStream.readLong();
        }
        catch (IOException e)
        {
            throw new CDMException("Error while reading binary stream", e);
        }

        block = new byte[byteSize];
        try
        {
            inputStream.readFully(block);
        }
        catch (IOException e)
        {
            throw new CDMException("error when reading binary block of DataBlock " + blockComponent.getName(), e);
        }

        byteArrayStream = new ByteArrayInputStream(block);
        stream = (InputStream) byteArrayStream;
        imageStream = new MemoryCacheImageInputStream(stream);

        try
        {
            imageReader.setInput(imageStream);
            image = imageReader.read(0);
        }
        catch (IOException e)
        {
            throw new CDMException("error when generating the compressed from the binary block of DataBlock " + blockComponent.getName(), e);
        }

        if (checkedForSize)
            if (blockComponent.getData().getAtomCount() != image.getWidth() * image.getHeight() * 3)
            {
                throw new CDMException("the size of the decoded image is not that " + "described in the Swe Common description");
            }
        checkedForSize = true;

        color = null;

        int m = 0;
        for (int j = 0; j < image.getHeight(); j++)
        {
            for (int i = 0; i < image.getWidth(); i++)
            {
                color = new Color(image.getRGB(i, j));
                short red = (short) color.getRed();
                short green = (short) color.getGreen();
                short blue = (short) color.getBlue();

                blockComponent.getData().setShortValue(m, red);
                blockComponent.getData().setShortValue(m + 1, green);
                blockComponent.getData().setShortValue(m + 2, blue);
                m += 3;
            }
        }
        image = null;
        block = null;
        byteArrayStream = null;
        stream = null;
    }

}
