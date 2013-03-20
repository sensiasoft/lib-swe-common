/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
 Alexandre Robin <robin@nsstc.uah.edu>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.om;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.vast.cdm.common.DataComponent;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.def.IDefinition;
import org.vast.ogc.gml.FeatureImpl;
import org.vast.ogc.gml.IFeature;
import org.vast.ogc.xlink.IXlinkReference;
import org.vast.util.TimeExtent;


/**
 * <p><b>Title:</b>
 * Single Observation
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Implementation of a single observation
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Feb 20, 2007
 * @version 1.0
 */
public class ObservationImpl extends FeatureImpl implements IObservation
{
    protected String type;
    protected List<IXlinkReference<IObservation>> relatedObservations;
    protected TimeExtent phenomenonTime;
    protected TimeExtent resultTime;
    protected TimeExtent validTime;
    protected IProcedure procedure;
    protected Map<String, Object> parameters;
    protected IXlinkReference<IDefinition> observedProperty;
    protected IFeature featureOfInterest;
    protected List<Object> resultQuality;
    protected DataComponent result;


    public ObservationImpl()
    {
        super(new QName(OGCRegistry.getNamespaceURI(OMUtils.OM, "2.0"), "Observation"));
        phenomenonTime = new TimeExtent();
    }
    
    
    @Override
    public String getType()
    {
        return type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public List<IXlinkReference<IObservation>> getRelatedObservations()
    {
        return relatedObservations;
    }


    public void addRelatedObservation(IXlinkReference<IObservation> relatedObservation)
    {
        if (this.relatedObservations == null)
            this.relatedObservations = new ArrayList<IXlinkReference<IObservation>>();
        
        this.relatedObservations.add(relatedObservation);
    }


    public TimeExtent getPhenomenonTime()
    {
        return phenomenonTime;
    }


    public void setPhenomenonTime(TimeExtent phenomenonTime)
    {
        this.phenomenonTime = phenomenonTime;
    }


    public TimeExtent getResultTime()
    {
        if (resultTime == null)
            return phenomenonTime;
        else
            return resultTime;
    }


    public void setResultTime(TimeExtent resultTime)
    {
        this.resultTime = resultTime;
    }


    public TimeExtent getValidTime()
    {
        return validTime;
    }


    public void setValidTime(TimeExtent validTime)
    {
        this.validTime = validTime;
    }


    public IProcedure getProcedure()
    {
        return procedure;
    }


    public void setProcedure(IProcedure procedure)
    {
        this.procedure = procedure;
    }


    public Map<String, Object> getParameters()
    {
        return parameters;
    }


    public void addParameter(String name, Object value)
    {
        if (this.parameters == null)
            this.parameters = new HashMap<String, Object>();
        
        this.parameters.put(name, value);
    }


    public IXlinkReference<IDefinition> getObservedProperty()
    {
        return this.observedProperty;
    }
    
    
    public void setObservedProperty(IXlinkReference<IDefinition> observedProperty)
    {
        this.observedProperty = observedProperty;
    }


    public IFeature getFeatureOfInterest()
    {
        return featureOfInterest;
    }


    public void setFeatureOfInterest(IFeature featureOfInterest)
    {
        this.featureOfInterest = featureOfInterest;
    }


    public List<Object> getResultQuality()
    {
        return resultQuality;
    }


    public void addResultQuality(Object resultQuality)
    {
        if (this.resultQuality == null)
            this.resultQuality = new ArrayList<Object>();
        
        this.resultQuality.add(resultQuality);
    }


    public DataComponent getResult()
    {
        return result;
    }


    public void setResult(DataComponent result)
    {
        this.result = result;
    }
    
    
    @Override
    public Object getProperty(String name)
    {
        if (name.equals("phenomenonTime"))
            return this.phenomenonTime;
        
        return super.getProperty(name);
    }
}
