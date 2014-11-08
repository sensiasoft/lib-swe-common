package net.opengis.swe.v20;



/**
 * POJO class for XML type TimeIndeterminateValue(@http://www.opengis.net/swe/2.0).
 *
 */
@SuppressWarnings("javadoc")
public enum TimeIndeterminateValue
{
    NOW("now");
    
    private final String text;
    
    
    
    /**
     * Private constructor for storing string representation
     */
    private TimeIndeterminateValue(String s)
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
    public static TimeIndeterminateValue fromString(String s)
    {
        if (s.equals("now"))
            return NOW;
        
        throw new IllegalArgumentException("Invalid token " + s + " for enum TimeIndeterminateValue");
    }
}
