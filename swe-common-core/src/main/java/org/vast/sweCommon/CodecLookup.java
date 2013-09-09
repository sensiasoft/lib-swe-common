/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2013 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sweCommon;

import java.util.Map;
import java.util.ServiceLoader;
import org.vast.cdm.common.CompressedStreamParser;
import org.vast.cdm.common.CompressedStreamWriter;


/**
 * <p><b>Title:</b>
 * CodecLookup
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Class for looking up codecs in the classpath
 * </p>
 *
 * <p>Copyright (c) 2013</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @date Sep 9, 2013
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
        return factory.createDecoder(compressionType);
    }

    
    public CompressedStreamWriter createEncoder(String compressionType)
    {
        return factory.createEncoder(compressionType);
    }    
}
