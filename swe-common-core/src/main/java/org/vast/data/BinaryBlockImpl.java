package org.vast.data;

import org.vast.cdm.common.CompressedStreamParser;
import net.opengis.HasCopy;
import net.opengis.swe.v20.BinaryBlock;


/**
 * POJO class for XML type BlockType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class BinaryBlockImpl extends AbstractSWEImpl implements BinaryBlock, HasCopy
{
    static final long serialVersionUID = 1L;
    protected String compression;
    protected String encryption;
    protected Integer paddingBytesAfter;
    protected Integer paddingBytesBefore;
    protected Integer byteLength;
    protected String ref = "";
    protected CompressedStreamParser reader;
    
    
    public BinaryBlockImpl()
    {
    }
    
    
    @Override
    public BinaryBlockImpl copy()
    {
        BinaryBlockImpl newObj = new BinaryBlockImpl();
        newObj.compression = this.compression;
        newObj.encryption = this.encryption;
        newObj.paddingBytesAfter = this.paddingBytesAfter;
        newObj.paddingBytesBefore = this.paddingBytesBefore;
        newObj.byteLength = this.byteLength;
        newObj.ref = this.ref;
        return newObj;
    }
    
    
    /**
     * Gets the compression property
     */
    @Override
    public String getCompression()
    {
        return compression;
    }
    
    
    /**
     * Checks if compression is set
     */
    @Override
    public boolean isSetCompression()
    {
        return (compression != null);
    }
    
    
    /**
     * Sets the compression property
     */
    @Override
    public void setCompression(String compression)
    {
        this.compression = compression;
    }
    
    
    /**
     * Gets the encryption property
     */
    @Override
    public String getEncryption()
    {
        return encryption;
    }
    
    
    /**
     * Checks if encryption is set
     */
    @Override
    public boolean isSetEncryption()
    {
        return (encryption != null);
    }
    
    
    /**
     * Sets the encryption property
     */
    @Override
    public void setEncryption(String encryption)
    {
        this.encryption = encryption;
    }
    
    
    /**
     * Gets the paddingBytesAfter property
     */
    @Override
    public int getPaddingBytesAfter()
    {
        return paddingBytesAfter;
    }
    
    
    /**
     * Checks if paddingBytesAfter is set
     */
    @Override
    public boolean isSetPaddingBytesAfter()
    {
        return (paddingBytesAfter != null);
    }
    
    
    /**
     * Sets the paddingBytesAfter property
     */
    @Override
    public void setPaddingBytesAfter(int paddingBytesAfter)
    {
        this.paddingBytesAfter = paddingBytesAfter;
    }
    
    
    /**
     * Unsets the paddingBytesAfter property
     */
    @Override
    public void unSetPaddingBytesAfter()
    {
        this.paddingBytesAfter = null;
    }
    
    
    /**
     * Gets the paddingBytesBefore property
     */
    @Override
    public int getPaddingBytesBefore()
    {
        return paddingBytesBefore;
    }
    
    
    /**
     * Checks if paddingBytesBefore is set
     */
    @Override
    public boolean isSetPaddingBytesBefore()
    {
        return (paddingBytesBefore != null);
    }
    
    
    /**
     * Sets the paddingBytesBefore property
     */
    @Override
    public void setPaddingBytesBefore(int paddingBytesBefore)
    {
        this.paddingBytesBefore = paddingBytesBefore;
    }
    
    
    /**
     * Unsets the paddingBytesBefore property
     */
    @Override
    public void unSetPaddingBytesBefore()
    {
        this.paddingBytesBefore = null;
    }
    
    
    /**
     * Gets the byteLength property
     */
    @Override
    public int getByteLength()
    {
        return byteLength;
    }
    
    
    /**
     * Checks if byteLength is set
     */
    @Override
    public boolean isSetByteLength()
    {
        return (byteLength != null);
    }
    
    
    /**
     * Sets the byteLength property
     */
    @Override
    public void setByteLength(int byteLength)
    {
        this.byteLength = byteLength;
    }
    
    
    /**
     * Unsets the byteLength property
     */
    @Override
    public void unSetByteLength()
    {
        this.byteLength = null;
    }
    
    
    /**
     * Gets the ref property
     */
    @Override
    public String getRef()
    {
        return ref;
    }
    
    
    /**
     * Sets the ref property
     */
    @Override
    public void setRef(String ref)
    {
        this.ref = ref;
    }
    
    
    /**
     * Gets the reader to use to decode the data for this block
     * @return reader instance
     */
    public CompressedStreamParser getBlockReader()
    {
        return this.reader;
    }
    
    
    /**
     * Sets the reader to use for decoding this block
     * @param reader
     */
    public void setBlockReader(CompressedStreamParser reader)
    {
        this.reader = reader;
    }
}
