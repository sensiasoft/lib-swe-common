/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.

 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or 
 Mike Botts <mike.botts@botts-inc.net for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.om;

import java.util.Map;
import org.vast.ogc.gml.GenericFeature;
import org.vast.swe.ISweInputDataStream;
import org.vast.swe.ISweOutputDataStream;
import org.vast.util.TimeExtent;


/**
 * <p>
 * Interface for a time series of observations.
 * Such a time series contain observations made by a single procedure
 * but can target several features of interest.
 * The procedure can be a homogeneous sensor array.
 * </p>
 *
 * @author Alex Robin
 * @since Sep 28, 2012
 * */
public interface IObservationSeries extends GenericFeature, ISweInputDataStream, ISweOutputDataStream
{
    /**
     * Get time extent including phenomenon times of all observation in the series
     * @return the phenomenon time extent of the series
     */
    public TimeExtent getPhenomenonTimeExtent();
    
    
    /**
     * Set the phenomenon time extent of the whole series
     * @param time
     */
    public void setPhenomenonTimeExtent(TimeExtent time);
    
    
    /**
     * Get time extent including result times of all observation in the series
     * @return the result time extent of the series
     */
    public TimeExtent getResultTimeExtent();
    
    
    /**
     * Set the result time extent of the whole series
     * @param time
     */
    public void setResultTimeExtent(TimeExtent time);
    
    
    /**
     * Get the list of features of interest of which this
     * time series contain observations
     * @return The map of features of interest (for read and write) 
     *         mapping feature IDs as they appear in the stream to 
     *         the actual feature instances.
     */
    public Map<String, GenericFeature> getFeaturesOfInterest();
    
    
    /**
     * Get the procedure that generated observations in this series
     * @return a reference to the list of procedures (for read and write)
     */
    public IProcedure getProcedure();
    
    
    /**
     * Set the procedure that generated observations in this series
     * @param procedure
     */
    public void setProcedure(IProcedure procedure);
    
}
