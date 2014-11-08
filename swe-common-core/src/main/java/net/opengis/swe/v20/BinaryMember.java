/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2014 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.swe.v20;


/**
 * <p>
 * Base interface for binary encoding members: BinaryComponent and BinaryBlock
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Oct 26, 2014
 */
@SuppressWarnings("javadoc")
public interface BinaryMember
{
    /**
     * Gets the ref property
     */
    public String getRef();
    
    
    /**
     * Sets the ref property
     */
    public void setRef(String ref);
}
