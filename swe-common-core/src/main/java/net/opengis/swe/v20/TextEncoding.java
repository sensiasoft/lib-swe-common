package net.opengis.swe.v20;



/**
 * POJO class for XML type TextEncodingType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface TextEncoding extends AbstractEncoding
{
    
    
    /**
     * Gets the collapseWhiteSpaces property
     */
    public boolean getCollapseWhiteSpaces();
    
    
    /**
     * Checks if collapseWhiteSpaces is set
     */
    public boolean isSetCollapseWhiteSpaces();
    
    
    /**
     * Sets the collapseWhiteSpaces property
     */
    public void setCollapseWhiteSpaces(boolean collapseWhiteSpaces);
    
    
    /**
     * Unsets the collapseWhiteSpaces property
     */
    public void unSetCollapseWhiteSpaces();
    
    
    /**
     * Gets the decimalSeparator property
     */
    public String getDecimalSeparator();
    
    
    /**
     * Checks if decimalSeparator is set
     */
    public boolean isSetDecimalSeparator();
    
    
    /**
     * Sets the decimalSeparator property
     */
    public void setDecimalSeparator(String decimalSeparator);
    
    
    /**
     * Gets the tokenSeparator property
     */
    public String getTokenSeparator();
    
    
    /**
     * Sets the tokenSeparator property
     */
    public void setTokenSeparator(String tokenSeparator);
    
    
    /**
     * Gets the blockSeparator property
     */
    public String getBlockSeparator();
    
    
    /**
     * Sets the blockSeparator property
     */
    public void setBlockSeparator(String blockSeparator);
}
