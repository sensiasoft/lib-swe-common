package net.opengis.swe.v20;



/**
 * POJO class for XML type ByteEncodingType(@http://www.opengis.net/swe/2.0).
 *
 */
@SuppressWarnings("javadoc")
public enum ByteEncoding
{
    BASE_64("base64"),
    RAW("raw");
    
    private final String text;
    
    
    
    /**
     * Private constructor for storing string representation
     */
    private ByteEncoding(String s)
    {
        this.text = s;
    }
    
    
    
    /**
     * To convert an enum constant to its String representation
     */
    public String toString()
    {
        return text;
    }
    
    
    
    /**
     * To get the enum constant corresponding to the given String representation
     */
    public static ByteEncoding fromString(String s)
    {
        if (s.equals("base64"))
            return BASE_64;
        else if (s.equals("raw"))
            return RAW;
        
        throw new IllegalArgumentException("Invalid token " + s + " for enum ByteEncoding");
    }
}
