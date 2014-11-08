package net.opengis.swe.v20;


/**
 * <p>
 * Tagging interface for data components that have a code space
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Nov 7, 2014
 */
@SuppressWarnings("javadoc")
public interface HasCodeSpace
{   
    /**
     * Gets the codeSpace property
     */
    public String getCodeSpace();
    
    
    /**
     * Checks if codeSpace is set
     */
    public boolean isSetCodeSpace();
    
    
    /**
     * Sets the codeSpace property
     */
    public void setCodeSpace(String codeSpaceUri);
}
