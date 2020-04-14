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

 Please Contact Alexandre Robin or 
 Mike Botts <mike.botts@botts-inc.net for more information.
 
 Contributor(s): 
    Alexandre Robin
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.om;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import net.opengis.swe.v20.DataComponent;
import org.vast.ogc.def.DefinitionRef;
import org.vast.ogc.gml.FeatureRef;
import org.vast.ogc.gml.IGeoFeature;
import org.vast.ogc.xlink.IReferenceResolver;
import org.vast.ogc.xlink.IXlinkReference;
import org.vast.util.TimeExtent;
import org.vast.util.ResolveException;
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
    
    public ObservationRef()
    {
        this.resolver = new IReferenceResolver<IObservation>()
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
    public TimeExtent getResultTime()
    {
        return getTarget().getResultTime();
    }


    @Override
    public TimeExtent getValidTime()
    {
        return getTarget().getValidTime();
    }


    @Override
    public DefinitionRef getObservedProperty()
    {
        return getTarget().getObservedProperty();
    }


    @Override
    public IGeoFeature getFeatureOfInterest()
    {
        return getTarget().getFeatureOfInterest();
    }


    @Override
    public IProcedure getProcedure()
    {
        return getTarget().getProcedure();
    }
    
    
    @Override
    public Map<String, Object> getParameters()
    {
        return getTarget().getParameters();
    }


    @Override
    public List<Object> getResultQuality()
    {
        return getTarget().getResultQuality();
    }


    @Override
    public DataComponent getResult()
    {
        return getTarget().getResult();
    }
    
    
    @Override
    public IObservation getTarget()
    {
        return (IObservation)super.getTarget();
    }
}
