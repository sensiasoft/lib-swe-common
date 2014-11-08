package net.opengis.swe.v20;



/**
 * POJO class for XML type MatrixType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface Matrix extends DataArray
{
    
    
    /**
     * Gets the referenceFrame property
     */
    public String getReferenceFrame();
    
    
    /**
     * Checks if referenceFrame is set
     */
    public boolean isSetReferenceFrame();
    
    
    /**
     * Sets the referenceFrame property
     */
    public void setReferenceFrame(String referenceFrame);
    
    
    /**
     * Gets the localFrame property
     */
    public String getLocalFrame();
    
    
    /**
     * Checks if localFrame is set
     */
    public boolean isSetLocalFrame();
    
    
    /**
     * Sets the localFrame property
     */
    public void setLocalFrame(String localFrame);
}
