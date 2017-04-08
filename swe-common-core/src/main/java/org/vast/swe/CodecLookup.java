/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2013 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe;

import java.util.Map;
import java.util.ServiceLoader;
import org.vast.cdm.common.CompressedStreamParser;
import org.vast.cdm.common.CompressedStreamWriter;


/**
 * <p>
 * Class for looking up codecs in the classpath
 * </p>
 *
 * @author Alex Robin
 * @since Sep 9, 2013
 */
public class CodecLookup
{
    private static CodecLookup singleton;
    ICodecFactory factory;
    
    
    public static CodecLookup getInstance()
    {
        if (singleton == null)
            singleton = new CodecLookup();
        return singleton;
    }
    
    
    private CodecLookup()
    {
        // for now we get the first factory we find
        for (ICodecFactory factory: ServiceLoader.load(ICodecFactory.class))
        {
            this.factory = factory;
            break;
        }
    }
    
    
    public Map<String, Class<?>> getAvailableDecoders()
    {
        return factory.getAvailableDecoders();
    }

    
    public Map<String, Class<?>> getAvailableEncoders()
    {
        return factory.getAvailableEncoders();
    }

    
    public CompressedStreamParser createDecoder(String compressionType)
    {
        if (factory == null)
            return null;
        return factory.createDecoder(compressionType);
    }

    
    public CompressedStreamWriter createEncoder(String compressionType)
    {
        if (factory == null)
            return null;
        return factory.createEncoder(compressionType);
    }    
}
