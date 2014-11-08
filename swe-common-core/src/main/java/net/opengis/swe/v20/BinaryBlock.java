package net.opengis.swe.v20;



/**
 * POJO class for XML type BlockType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface BinaryBlock extends AbstractSWE, BinaryMember
{
    
    
    /**
     * Gets the compression property
     */
    public String getCompression();
    
    
    /**
     * Checks if compression is set
     */
    public boolean isSetCompression();
    
    
    /**
     * Sets the compression property
     */
    public void setCompression(String compression);
    
    
    /**
     * Gets the encryption property
     */
    public String getEncryption();
    
    
    /**
     * Checks if encryption is set
     */
    public boolean isSetEncryption();
    
    
    /**
     * Sets the encryption property
     */
    public void setEncryption(String encryption);
    
    
    /**
     * Gets the paddingBytesAfter property
     */
    public int getPaddingBytesAfter();
    
    
    /**
     * Checks if paddingBytesAfter is set
     */
    public boolean isSetPaddingBytesAfter();
    
    
    /**
     * Sets the paddingBytesAfter property
     */
    public void setPaddingBytesAfter(int paddingBytesAfter);
    
    
    /**
     * Unsets the paddingBytesAfter property
     */
    public void unSetPaddingBytesAfter();
    
    
    /**
     * Gets the paddingBytesBefore property
     */
    public int getPaddingBytesBefore();
    
    
    /**
     * Checks if paddingBytesBefore is set
     */
    public boolean isSetPaddingBytesBefore();
    
    
    /**
     * Sets the paddingBytesBefore property
     */
    public void setPaddingBytesBefore(int paddingBytesBefore);
    
    
    /**
     * Unsets the paddingBytesBefore property
     */
    public void unSetPaddingBytesBefore();
    
    
    /**
     * Gets the byteLength property
     */
    public long getByteLength();
    
    
    /**
     * Checks if byteLength is set
     */
    public boolean isSetByteLength();
    
    
    /**
     * Sets the byteLength property
     */
    public void setByteLength(long byteLength);
    
    
    /**
     * Unsets the byteLength property
     */
    public void unSetByteLength();
}
