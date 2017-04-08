/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2013 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.cdm.common;

import net.opengis.swe.v20.BinaryBlock;
import net.opengis.swe.v20.DataComponent;


/**
 * <p>
 * Interface for all SWE compressed block encoders
 * </p>
 *
 * @author Alex Robin
 * @since Sep 9, 2013
 */
public interface CompressedStreamWriter
{

    /**
     * Initializes encoder with block data component and its binary encoding info
     * @param blockComponent
     * @param binaryBlock
     * @throws CDMException
     */
    public abstract void init(DataComponent blockComponent, BinaryBlock binaryBlock) throws CDMException;
    
    
    /**
     * Writes compressed data obtained from block component to output stream
     * @param outputStream
     * @param blockComponent
     * @throws CDMException
     */
    public abstract void encode(DataOutputExt outputStream, DataComponent blockComponent) throws CDMException;
    
}
