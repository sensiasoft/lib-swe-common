/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are copyright (C) 2018, Sensia Software LLC
 All Rights Reserved. This software is the property of Sensia Software LLC.
 It cannot be duplicated, used, or distributed without the express written
 consent of Sensia Software LLC.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import org.vast.util.TimeExtent;


/**
 * <p>
 * Base interface for feature that only exist or whose representation is only
 * valid during a certain time period. 
 * </p>
 *
 * @author Alex Robin
 * @date Sep 6, 2019
 */
public interface ITemporalFeature extends IFeature
{

    /**
     * @return feature validity period (or null if always valid)
     */
    public TimeExtent getValidTime();
}
