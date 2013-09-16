/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Initial Developer of the Original Code is SENSIA SOFTWARE LLC.
 Portions created by the Initial Developer are Copyright (C) 2012
 the Initial Developer. All Rights Reserved.

 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> for more
 information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.om;

import javax.xml.namespace.QName;
import org.vast.cdm.common.DataComponent;
import org.vast.ogc.def.IDefinition;
import org.vast.ogc.gml.FeatureRef;
import org.vast.ogc.gml.GMLFeatureReader;
import org.vast.ogc.gml.GMLTimeReader;
import org.vast.ogc.gml.IFeature;
import org.vast.ogc.xlink.CachedReference;
import org.vast.ogc.xlink.IXlinkReference;
import org.vast.ogc.xlink.XlinkUtils;
import org.vast.sweCommon.SWEFilter;
import org.vast.sweCommon.SweComponentReaderV20;
import org.vast.util.TimeExtent;
import org.vast.xml.DOMHelper;
import org.vast.xml.IXMLReaderDOM;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.*;


/**
 * <p>
 * Reader for O&M observations v2.0
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @since Sep 30, 2012
 * @version 1.0
 */
public class ObservationReaderV20 implements IXMLReaderDOM<IObservation>
{
	protected SWEFilter streamFilter;
    protected GMLTimeReader timeReader;
    protected GMLFeatureReader featureReader;
    protected SweComponentReaderV20 sweReader;
    
    
    public ObservationReaderV20()
    {
        timeReader = new GMLTimeReader();
        featureReader = new GMLFeatureReader();
        sweReader = new SweComponentReaderV20();
    }
    
    
    public IObservation read(DOMHelper dom, Element obsElt) throws XMLReaderException
    {
        
        IObservation obs = new ObservationImpl();
        
        // local ID
        String id = dom.getAttributeValue(obsElt, "id");
        obs.setLocalId(id);
            
        // description
        obs.setDescription(dom.getElementValue(obsElt, "description"));
        
        // identifier
        obs.setIdentifier(dom.getElementValue(obsElt, "identifier"));
        
        // observation names
        NodeList nameElts = dom.getElements("name");
        for (int i = 0; i < nameElts.getLength(); i++)
        {
            Element nameElt = (Element)nameElts.item(i);
            String name = dom.getElementValue(nameElt);
            String codeSpace = dom.getAttributeValue(nameElt, "@codeSpace");
            obs.addName(new QName(codeSpace, name));
        }
        
        // type
        obs.setType(dom.getElementValue(obsElt, "type"));
        
        // metadata as raw XML
        Element metadata = dom.getElement(obsElt, "metadata/*");
        if (metadata != null)
            obs.setMetadata(metadata.cloneNode(true));
        
        // related observations
        NodeList relObsElts = dom.getElements(obsElt, "relatedObservation/ObservationContext");
        for (int i = 0; i < relObsElts.getLength(); i++)
        {
            Element refElt = (Element)relObsElts.item(i);
            String href = dom.getElementValue(refElt, "relatedObservation");
            IXlinkReference<IObservation> obsRef = new CachedReference<IObservation>(href);
            obsRef.setRole(dom.getElementValue(refElt, "role"));
            obs.addRelatedObservation(obsRef);
        }
        
        Element timeElt;
        TimeExtent time;
        
        // phenomenon time
        timeElt = dom.getElement(obsElt, "phenomenonTime/*");
        time = timeReader.readTimePrimitive(dom, timeElt);
        obs.setPhenomenonTime(time);
        
        // result time
        timeElt = dom.getElement(obsElt, "resultTime/*");
        time = timeReader.readTimePrimitive(dom, timeElt);
        obs.setPhenomenonTime(time);
        
        // optional valid time
        timeElt = dom.getElement(obsElt, "validTime/*");
        if (timeElt != null)
        {
            time = timeReader.readTimePrimitive(dom, timeElt);
            obs.setPhenomenonTime(time);
        }
        
        // procedure
        String href = dom.getAttributeValue(obsElt, "procedure/@href");
        if (href != null)
        {
            obs.setProcedure(new ProcedureRef(href));
        }
        else
        {
            // read procedure as raw XML
            Element procXML = dom.getElement(obsElt, "procedure/*");
            if (procXML != null)
                obs.setProcedure(new ProcedureXML((Element)procXML.cloneNode(true)));
        }
        
        // parameters
        NodeList paramElts = dom.getElements(obsElt, "parameter");
        for (int i = 0; i < paramElts.getLength(); i++)
        {
            Element paramElt = (Element)paramElts.item(i);
            String paramName = dom.getElementValue(paramElt, "name");
            Element valueElt = dom.getElement(paramElt, "value/*");
            Object paramValue;
            
            try
            {
                if (valueElt.getNamespaceURI().equals(SweComponentReaderV20.SWE_NS))
                    paramValue = sweReader.readComponent(dom, valueElt);
                else
                    paramValue = valueElt.cloneNode(true);//valueElt.getParentNode().removeChild(valueElt);
            }
            catch (XMLReaderException e)
            {
                throw new XMLReaderException("Error while parsing observation parameter", paramElt, e);
            }
            
            obs.addParameter(paramName, paramValue);
        }
        
        // observed property
        String propRef = dom.getAttributeValue(obsElt, "observedProperty");
        obs.setObservedProperty(new CachedReference<IDefinition>(propRef));
        
        // read foi
        href = dom.getAttributeValue(obsElt, "featureOfInterest/@href");
        if (href != null)
            obs.setFeatureOfInterest(new FeatureRef(href));
        else
        {
            Element foiElt = dom.getElement(obsElt, "featureOfInterest/*");
            if (foiElt != null)
            {
                IFeature foi = readFOI(dom, foiElt);
                obs.setFeatureOfInterest(foi);
            }
        }
        
        // result quality as raw XML
        NodeList qualityElts = dom.getElements(obsElt, "resultQuality/*");
        for (int i = 0; i < qualityElts.getLength(); i++)
            obs.addResultQuality(qualityElts.item(i).cloneNode(true));
             
        // result
        Element resultElt = dom.getElement(obsElt, "result");
        try
        {
            DataComponent result = sweReader.readComponentProperty(dom, resultElt);
            obs.setResult(result);
        }
        catch (XMLReaderException e)
        {
            throw new XMLReaderException("Error while parsing observation result", resultElt, e);
        }
        
        return obs;
    }
    
    
    protected IFeature readFOI(DOMHelper dom, Element foiElt) throws XMLReaderException
    {
        Element featureElt = dom.getFirstChildElement(foiElt);
        
        if (featureElt != null)
        {
            FeatureRef ref = new FeatureRef();
            XlinkUtils.readXlinkAttributes(foiElt, ref);
            return ref;
        }
        else
        {
            return featureReader.read(dom, featureElt);
        }
    }    
}
