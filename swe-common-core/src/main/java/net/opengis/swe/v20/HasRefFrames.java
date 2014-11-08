package net.opengis.swe.v20;


/**
 * <p>
 * Tagging interface for data components with local and reference frames
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Nov 8, 2014
 */
@SuppressWarnings("javadoc")
public interface HasRefFrames
{

    /**
     * Gets the referenceFrame property
     */
    public abstract String getReferenceFrame();
    
    
    /**
     * Checks if referenceFrame is set
     */
    public boolean isSetReferenceFrame();


    /**
     * Sets the referenceFrame property
     */
    public abstract void setReferenceFrame(String referenceFrame);


    /**
     * Gets the localFrame property
     */
    public abstract String getLocalFrame();


    /**
     * Checks if localFrame is set
     */
    public abstract boolean isSetLocalFrame();


    /**
     * Sets the localFrame property
     */
    public abstract void setLocalFrame(String localFrame);

}