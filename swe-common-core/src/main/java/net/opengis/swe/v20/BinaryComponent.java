package net.opengis.swe.v20;



/**
 * POJO class for XML type ComponentType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface BinaryComponent extends AbstractSWE, BinaryMember
{
    
    
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
     * Gets the significantBits property
     */
    public int getSignificantBits();
    
    
    /**
     * Checks if significantBits is set
     */
    public boolean isSetSignificantBits();
    
    
    /**
     * Sets the significantBits property
     */
    public void setSignificantBits(int significantBits);
    
    
    /**
     * Unsets the significantBits property
     */
    public void unSetSignificantBits();
    
    
    /**
     * Gets the bitLength property
     */
    public int getBitLength();
    
    
    /**
     * Checks if bitLength is set
     */
    public boolean isSetBitLength();
    
    
    /**
     * Sets the bitLength property
     */
    public void setBitLength(int bitLength);
    
    
    /**
     * Unsets the bitLength property
     */
    public void unSetBitLength();
    
    
    /**
     * Gets the byteLength property
     */
    public int getByteLength();
    
    
    /**
     * Checks if byteLength is set
     */
    public boolean isSetByteLength();
    
    
    /**
     * Sets the byteLength property
     */
    public void setByteLength(int byteLength);
    
    
    /**
     * Unsets the byteLength property
     */
    public void unSetByteLength();
    
    
    /**
     * Gets the dataType property
     */
    public String getDataType();
    
    
    /**
     * Sets the dataType property
     */
    public void setDataType(String dataType);
    
}
