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

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.vast.cdm.common.DataComponent;
import org.vast.ogc.def.IDefinition;
import org.vast.ogc.gml.FeatureRef;
import org.vast.ogc.gml.IFeature;
import org.vast.ogc.xlink.IXlinkReference;
import org.vast.util.TimeExtent;
import org.vast.util.URIResolver;


/**
 * <p>
 * Wrapper class for use when an IObservation object is or can be included by reference.
 * This enables fetching and instantiating the target object lazily.
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @since Sep 28, 2012
 * @version 1.0
 */
public class ObservationRef extends FeatureRef implements IObservation
{

    public ObservationRef()
    {
    }
    
    
    public ObservationRef(String href)
    {
        setHref(href);
    }
        
    
    public String getType()
    {
        return getTarget().getType();
    }


    public Object getProperty(String name)
    {
        return getTarget().getProperty(name);
    }


    public void setType(String type)
    {
        getTarget().setType(type);
    }


    public void setProperty(String name, Object val)
    {
        getTarget().setProperty(name, val);
    }


    public Object getMetadata()
    {
        return getTarget().getMetadata();
    }


    public void setMetadata(Object metadata)
    {
        getTarget().setMetadata(metadata);
    }


    public Object getProperty(QName qname)
    {
        return getTarget().getProperty(qname);
    }


    public List<IXlinkReference<IObservation>> getRelatedObservations()
    {
        return getTarget().getRelatedObservations();
    }


    public void setProperty(QName qname, Object val)
    {
        getTarget().setProperty(qname, val);
    }


    public void addRelatedObservation(IXlinkReference<IObservation> obsUri)
    {
        getTarget().addRelatedObservation(obsUri);
    }


    public TimeExtent getPhenomenonTime()
    {
        return getTarget().getPhenomenonTime();
    }


    public void setPhenomenonTime(TimeExtent time)
    {
        getTarget().setPhenomenonTime(time);
    }


    public TimeExtent getResultTime()
    {
        return getTarget().getResultTime();
    }


    public void setResultTime(TimeExtent time)
    {
        getTarget().setResultTime(time);
    }


    public TimeExtent getValidTime()
    {
        return getTarget().getValidTime();
    }


    public void setValidTime(TimeExtent time)
    {
        getTarget().setValidTime(time);
    }


    public IXlinkReference<IDefinition> getObservedProperty()
    {
        return getTarget().getObservedProperty();
    }


    public void setObservedProperty(IXlinkReference<IDefinition> propRef)
    {
        getTarget().setObservedProperty(propRef);
    }


    public IFeature getFeatureOfInterest()
    {
        return getTarget().getFeatureOfInterest();
    }


    public void setFeatureOfInterest(IFeature foi)
    {
        getTarget().setFeatureOfInterest(foi);
    }


    public IProcedure getProcedure()
    {
        return getTarget().getProcedure();
    }


    public void setProcedure(IProcedure procedure)
    {
        getTarget().setProcedure(procedure);
    }
    
    
    public Map<String, Object> getParameters()
    {
        return getTarget().getParameters();
    }
    
    
    public void addParameter(String name, Object value)
    {
        getTarget().addParameter(name, value);
    }


    public List<Object> getResultQuality()
    {
        return getTarget().getResultQuality();
    }


    public void addResultQuality(Object qualityInfo)
    {
        getTarget().addResultQuality(qualityInfo);
    }


    public DataComponent getResult()
    {
        return getTarget().getResult();
    }


    public void setResult(DataComponent result)
    {
        getTarget().setResult(result);
    }
    
    
    @Override
    public IObservation getTarget()
    {
        return (IObservation)super.getTarget();
    }
    
    
    @Override
    protected IObservation fetchTarget(String href)
    {
        try
        {
            URIResolver resolver = new URIResolver(new URI(href));
            InputStream is = resolver.openStream();            
            return OMUtils.readObservation(is);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
