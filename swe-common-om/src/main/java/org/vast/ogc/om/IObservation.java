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

import java.util.List;
import java.util.Map;
import net.opengis.gml.v32.AbstractFeature;
import net.opengis.swe.v20.DataComponent;
import org.vast.ogc.def.DefinitionRef;
import org.vast.ogc.gml.GenericFeature;
import org.vast.ogc.xlink.IXlinkReference;
import org.vast.util.TimeExtent;


/**
 * <p>
 * Interface for an Observation as defined by the O&M standard
 * </p>
 *
 * @author Alex Robin
 * @since Sep 28, 2012
 * */
public interface IObservation extends GenericFeature
{
    public final static String OBS_TYPE_GENERIC = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Observation";
    public final static String OBS_TYPE_MEAS = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement";
    public final static String OBS_TYPE_COUNT = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CountObservation"; 
    public final static String OBS_TYPE_CATEGORY = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CategoryObservation"; 
    public final static String OBS_TYPE_SCALAR = "http://www.opengis.net/def/observationType/OGC-OM/2.0/SWEScalarObservation";
    public final static String OBS_TYPE_RECORD = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_ComplexObservation";
    public final static String OBS_TYPE_ARRAY = "http://www.opengis.net/def/observationType/OGC-OM/2.0/SWEArrayObservation";
    
    public String getType();
    
    public void setType(String type);
    
    public List<IXlinkReference<IObservation>> getRelatedObservations();
    
    public void addRelatedObservation(IXlinkReference<IObservation> obsRef);
    
    public TimeExtent getPhenomenonTime();
    
    public void setPhenomenonTime(TimeExtent time);
    
    public TimeExtent getResultTime();
    
    public void setResultTime(TimeExtent time);
    
    public TimeExtent getValidTime();
    
    public void setValidTime(TimeExtent time);
    
    public DefinitionRef getObservedProperty();
    
    public void setObservedProperty(DefinitionRef propRef);
    
    public AbstractFeature getFeatureOfInterest();
    
    public void setFeatureOfInterest(AbstractFeature foi);
    
    public IProcedure getProcedure();
    
    public void setProcedure(IProcedure procedure);
    
    public Map<String, Object> getParameters();
    
    public void addParameter(String name, Object value);
    
    public List<Object> getResultQuality();
    
    public void addResultQuality(Object qualityInfo);
    
    public DataComponent getResult();
    
    public void setResult(DataComponent result);
}
