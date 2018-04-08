/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are copyright (C) 2018, Sensia Software LLC
 All Rights Reserved. This software is the property of Sensia Software LLC.
 It cannot be duplicated, used, or distributed without the express written
 consent of Sensia Software LLC.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.time.Instant;
import com.google.common.collect.Range;
import net.opengis.gml.v32.AbstractFeature;


public interface TemporalFeature extends AbstractFeature
{

    public Range<Instant> getValidTime();
}
