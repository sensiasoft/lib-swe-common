/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2013 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe;

import java.util.Map;
import org.vast.cdm.common.CompressedStreamParser;
import org.vast.cdm.common.CompressedStreamWriter;


/**
 * <p>
 * Interface for SWE Common codec factories.
 * These factories are meant to be discovered via the java services mechanism
 * </p>
 *
 * @author Alex Robin
 * @since Sep 9, 2013
 */
public interface ICodecFactory
{
    
    public Map<String, Class<?>> getAvailableDecoders();
    
    
    public Map<String, Class<?>> getAvailableEncoders();
    
    
    public CompressedStreamParser createDecoder(String compressionType);
    
    
    public CompressedStreamWriter createEncoder(String compressionType);    
    
}
