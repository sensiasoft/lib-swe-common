/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are copyright (C) 2019, Sensia Software LLC
 All Rights Reserved. This software is the property of Sensia Software LLC.
 It cannot be duplicated, used, or distributed without the express written
 consent of Sensia Software LLC.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.util.Collections;
import java.util.Map;
import javax.xml.namespace.QName;

/**
 * <p>
 * Simple interface for feature objects
 * </p>
 *
 * @author Alex Robin
 * @date Sep 5, 2019
 */
public interface IFeature
{

    /**
     * @return the name
     */
    public String getName();
    
    
    /**
     * @return the globally unique identifier
     */
    public String getUniqueIdentifier();
    
    
    /**
     * @return the short description
     */
    public String getDescription();
    
    
    /**
     * @return the property list
     */
    public default Map<QName, Object> getProperties()
    {
        return Collections.emptyMap();
    }
    
}
