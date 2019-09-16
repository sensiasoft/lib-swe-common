/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are copyright (C) 2019, Sensia Software LLC
 All Rights Reserved. This software is the property of Sensia Software LLC.
 It cannot be duplicated, used, or distributed without the express written
 consent of Sensia Software LLC.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import net.opengis.gml.v32.AbstractGeometry;


/**
 * <p>
 * Simple interface for geographic feature objects (i.e. with a geometry)
 * </p>
 *
 * @author Alex Robin
 * @date Sep 5, 2019
 */
public interface IGeoFeature extends IFeature
{
    
    /**
     * @return the geometry/location
     */
    public AbstractGeometry getGeometry();
}
