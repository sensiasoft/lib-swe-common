/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
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
import net.opengis.gml.v32.AbstractFeature;
import net.opengis.swe.v20.DataComponent;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.def.DefinitionRef;
import org.vast.ogc.gml.GenericFeatureImpl;
import org.vast.ogc.xlink.IXlinkReference;
import org.vast.util.TimeExtent;


/**
 * <p>
 * Implementation of a single observation
 * </p>
 *
 * @author Alex Robin
 * @since Feb 20, 2007
 * */
public class ObservationImpl extends GenericFeatureImpl implements IObservation
{
    private static final long serialVersionUID = 1525908516530863277L;
    protected String type;
    protected ArrayList<IXlinkReference<IObservation>> relatedObservations;
    protected TimeExtent phenomenonTime;
    protected TimeExtent resultTime;
    protected TimeExtent validTime;
    protected IProcedure procedure;
    protected HashMap<String, Object> parameters;
    protected DefinitionRef observedProperty;
    protected AbstractFeature featureOfInterest;
    protected ArrayList<Object> resultQuality;
    protected DataComponent result;


    public ObservationImpl()
    {
        super(new QName(OGCRegistry.getNamespaceURI(OMUtils.OM, "2.0"), "OM_Observation"));
        phenomenonTime = new TimeExtent();
    }


    @Override
    public String getType()
    {
        return type;
    }


    @Override
    public void setType(String type)
    {
        this.type = type;
    }


    @Override
    public List<IXlinkReference<IObservation>> getRelatedObservations()
    {
        return relatedObservations;
    }


    @Override
    public void addRelatedObservation(IXlinkReference<IObservation> relatedObservation)
    {
        if (this.relatedObservations == null)
            this.relatedObservations = new ArrayList<IXlinkReference<IObservation>>();
        
        this.relatedObservations.add(relatedObservation);
    }


    @Override
    public TimeExtent getPhenomenonTime()
    {
        return phenomenonTime;
    }


    @Override
    public void setPhenomenonTime(TimeExtent phenomenonTime)
    {
        this.phenomenonTime = phenomenonTime;
    }


    @Override
    public TimeExtent getResultTime()
    {
        if (resultTime == null)
            return phenomenonTime;
        else
            return resultTime;
    }


    @Override
    public void setResultTime(TimeExtent resultTime)
    {
        this.resultTime = resultTime;
    }


    @Override
    public TimeExtent getValidTime()
    {
        return validTime;
    }


    @Override
    public void setValidTime(TimeExtent validTime)
    {
        this.validTime = validTime;
    }


    @Override
    public IProcedure getProcedure()
    {
        return procedure;
    }


    @Override
    public void setProcedure(IProcedure procedure)
    {
        this.procedure = procedure;
    }


    @Override
    public Map<String, Object> getParameters()
    {
        return parameters;
    }


    @Override
    public void addParameter(String name, Object value)
    {
        if (this.parameters == null)
            this.parameters = new HashMap<String, Object>();
        
        this.parameters.put(name, value);
    }


    @Override
    public DefinitionRef getObservedProperty()
    {
        return this.observedProperty;
    }
    
    
    @Override
    public void setObservedProperty(DefinitionRef observedProperty)
    {
        this.observedProperty = observedProperty;
    }


    @Override
    public AbstractFeature getFeatureOfInterest()
    {
        return featureOfInterest;
    }


    @Override
    public void setFeatureOfInterest(AbstractFeature featureOfInterest)
    {
        this.featureOfInterest = featureOfInterest;
    }


    @Override
    public List<Object> getResultQuality()
    {
        return resultQuality;
    }


    @Override
    public void addResultQuality(Object resultQuality)
    {
        if (this.resultQuality == null)
            this.resultQuality = new ArrayList<Object>();
        
        this.resultQuality.add(resultQuality);
    }


    @Override
    public DataComponent getResult()
    {
        return result;
    }


    @Override
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
