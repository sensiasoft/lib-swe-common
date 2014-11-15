package net.opengis.gml.v32;



/**
 * POJO class for XML type TimeIndeterminateValueType(@http://www.opengis.net/gml/3.2).
 *
 */
public enum TimeIndeterminateValue
{
    AFTER("after"),
    BEFORE("before"),
    NOW("now"),
    UNKNOWN("unknown");
    
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
        if (s.equals("after"))
            return AFTER;
        else if (s.equals("before"))
            return BEFORE;
        else if (s.equals("now"))
            return NOW;
        else if (s.equals("unknown"))
            return UNKNOWN;
        
        throw new IllegalArgumentException("Invalid token " + s + " for enum TimeIndeterminateValue");
    }
}
