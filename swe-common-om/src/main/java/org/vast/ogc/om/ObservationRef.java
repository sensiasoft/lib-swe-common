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

 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or 
 Mike Botts <mike.botts@botts-inc.net for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.om;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import net.opengis.gml.v32.AbstractFeature;
import net.opengis.swe.v20.DataComponent;
import org.vast.ogc.def.DefinitionRef;
import org.vast.ogc.gml.FeatureRef;
import org.vast.ogc.gml.GenericFeature;
import org.vast.ogc.xlink.IReferenceResolver;
import org.vast.ogc.xlink.IXlinkReference;
import org.vast.util.ResolveException;
import org.vast.util.TimeExtent;
import org.vast.util.URIResolver;


/**
 * <p>
 * Wrapper class for use when an IObservation object is or can be included by reference.
 * This enables fetching and instantiating the target object lazily.
 * </p>
 *
 * @author Alex Robin
 * @since Sep 28, 2012
 * */
public class ObservationRef extends FeatureRef implements IObservation
{
    private static final long serialVersionUID = 2595161878598571315L;


    public ObservationRef()
    {
        this.resolver = new IReferenceResolver<GenericFeature>()
        {            
            @Override
            public IObservation fetchTarget(String uri) throws IOException
            {
                try
                {
                    URIResolver resolver = new URIResolver(new URI(href));
                    InputStream is = resolver.openStream();            
                    return new OMUtils(OMUtils.V2_0).readObservation(is);
                }
                catch (URISyntaxException e)
                {
                    throw new ResolveException("Bad URI", e);
                }
            }
        };
    }
    
    
    public ObservationRef(String href)
    {
        setHref(href);
    }


    @Override
    public String getType()
    {
        return getTarget().getType();
    }


    @Override
    public void setType(String type)
    {
        getTarget().setType(type);
    }    


    @Override
    public List<IXlinkReference<IObservation>> getRelatedObservations()
    {
        return getTarget().getRelatedObservations();
    }


    @Override
    public void addRelatedObservation(IXlinkReference<IObservation> obsUri)
    {
        getTarget().addRelatedObservation(obsUri);
    }


    @Override
    public TimeExtent getPhenomenonTime()
    {
        return getTarget().getPhenomenonTime();
    }


    @Override
    public void setPhenomenonTime(TimeExtent time)
    {
        getTarget().setPhenomenonTime(time);
    }


    @Override
    public TimeExtent getResultTime()
    {
        return getTarget().getResultTime();
    }


    @Override
    public void setResultTime(TimeExtent time)
    {
        getTarget().setResultTime(time);
    }


    @Override
    public TimeExtent getValidTime()
    {
        return getTarget().getValidTime();
    }


    @Override
    public void setValidTime(TimeExtent time)
    {
        getTarget().setValidTime(time);
    }


    @Override
    public DefinitionRef getObservedProperty()
    {
        return getTarget().getObservedProperty();
    }


    @Override
    public void setObservedProperty(DefinitionRef propRef)
    {
        getTarget().setObservedProperty(propRef);
    }


    @Override
    public AbstractFeature getFeatureOfInterest()
    {
        return getTarget().getFeatureOfInterest();
    }


    @Override
    public void setFeatureOfInterest(AbstractFeature foi)
    {
        getTarget().setFeatureOfInterest(foi);
    }


    @Override
    public IProcedure getProcedure()
    {
        return getTarget().getProcedure();
    }


    @Override
    public void setProcedure(IProcedure procedure)
    {
        getTarget().setProcedure(procedure);
    }
    
    
    @Override
    public Map<String, Object> getParameters()
    {
        return getTarget().getParameters();
    }
    
    
    @Override
    public void addParameter(String name, Object value)
    {
        getTarget().addParameter(name, value);
    }


    @Override
    public List<Object> getResultQuality()
    {
        return getTarget().getResultQuality();
    }


    @Override
    public void addResultQuality(Object qualityInfo)
    {
        getTarget().addResultQuality(qualityInfo);
    }


    @Override
    public DataComponent getResult()
    {
        return getTarget().getResult();
    }


    @Override
    public void setResult(DataComponent result)
    {
        getTarget().setResult(result);
    }
    
    
    @Override
    public IObservation getTarget()
    {
        return (IObservation)super.getTarget();
    }
}
