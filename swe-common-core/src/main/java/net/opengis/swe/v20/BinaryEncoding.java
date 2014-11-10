package net.opengis.swe.v20;

import java.nio.ByteOrder;
import java.util.List;


/**
 * POJO class for XML type BinaryEncodingType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface BinaryEncoding extends DataEncoding
{
    
    
    /**
     * Gets the list of member properties
     */
    public List<BinaryMember> getMemberList();
    
    
    /**
     * Returns number of member properties
     */
    public int getNumMembers();
    
    
    /**
     * Adds a new memberAsComponent property
     */
    public void addMemberAsComponent(BinaryComponent member);
    
    
    /**
     * Adds a new memberAsBlock property
     */
    public void addMemberAsBlock(BinaryBlock member);
    
    
    /**
     * Gets the byteOrder property
     */
    public ByteOrder getByteOrder();
    
    
    /**
     * Sets the byteOrder property
     */
    public void setByteOrder(ByteOrder byteOrder);
    
    
    /**
     * Gets the byteEncoding property
     */
    public ByteEncoding getByteEncoding();
    
    
    /**
     * Sets the byteEncoding property
     */
    public void setByteEncoding(ByteEncoding byteEncoding);
    
    
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
