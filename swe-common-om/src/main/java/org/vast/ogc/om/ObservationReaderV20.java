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

import net.opengis.gml.v32.impl.CodeWithAuthorityImpl;
import net.opengis.swe.v20.DataComponent;
import org.vast.ogc.def.DefinitionRef;
import org.vast.ogc.gml.FeatureRef;
import org.vast.ogc.gml.GMLUtils;
import org.vast.ogc.gml.GenericFeature;
import org.vast.ogc.xlink.CachedReference;
import org.vast.ogc.xlink.IXlinkReference;
import org.vast.ogc.xlink.XlinkUtils;
import org.vast.swe.SWEUtils;
import org.vast.swe.SWEFilter;
import org.vast.swe.SWEStaxBindings;
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
 * @author Alex Robin
 * @since Sep 30, 2012
 * */
public class ObservationReaderV20 implements IXMLReaderDOM<IObservation>
{
	protected SWEFilter streamFilter;
    protected GMLUtils gmlUtils = new GMLUtils(GMLUtils.V3_2, new SamplingFeatureReader());
    protected SWEUtils sweUtils = new SWEUtils(SWEUtils.V2_0);
    
    
    @Override
    public IObservation read(DOMHelper dom, Element obsElt) throws XMLReaderException
    {        
        IObservation obs = new ObservationImpl();
        
        // local ID
        String id = dom.getAttributeValue(obsElt, "id");
        obs.setId(id);
            
        // description
        obs.setDescription(dom.getElementValue(obsElt, "description"));
        
        // identifier
        String uid = dom.getElementValue(obsElt, "identifier");
        if (uid != null)
            obs.setUniqueIdentifier(uid);
        
        // observation names
        NodeList nameElts = dom.getElements("name");
        for (int i = 0; i < nameElts.getLength(); i++)
        {
            Element nameElt = (Element)nameElts.item(i);
            String name = dom.getElementValue(nameElt);
            String codeSpace = dom.getAttributeValue(nameElt, "@codeSpace");
            obs.getNameList().add(new CodeWithAuthorityImpl(codeSpace, name));
        }
        
        // type
        obs.setType(dom.getAttributeValue(obsElt, "type/href"));
        
        // metadata as raw XML
        /*Element metadata = dom.getElement(obsElt, "metadata/*");
        if (metadata != null)
            obs.getMetaDataPropertyList().add(metadata.cloneNode(true));*/
        
        // related observations
        NodeList relObsElts = dom.getElements(obsElt, "relatedObservation/ObservationContext");
        for (int i = 0; i < relObsElts.getLength(); i++)
        {
            Element refElt = (Element)relObsElts.item(i);
            String href = dom.getElementValue(refElt, "relatedObservation");
            IXlinkReference<IObservation> obsRef = new CachedReference<>(href);
            obsRef.setRole(dom.getElementValue(refElt, "role"));
            obs.addRelatedObservation(obsRef);
        }
        
        Element timeElt;
        TimeExtent time;
        
        // phenomenon time
        timeElt = dom.getElement(obsElt, "phenomenonTime/*");
        time = gmlUtils.readTimePrimitiveAsTimeExtent(dom, timeElt);
        obs.setPhenomenonTime(time);
        
        // result time
        timeElt = dom.getElement(obsElt, "resultTime/*");
        time = gmlUtils.readTimePrimitiveAsTimeExtent(dom, timeElt);
        obs.setResultTime(time);
        
        // optional valid time
        timeElt = dom.getElement(obsElt, "validTime/*");
        if (timeElt != null)
        {
            time = gmlUtils.readTimePrimitiveAsTimeExtent(dom, timeElt);
            obs.setValidTime(time);
        }
        
        // procedure
        Element procPropElt = dom.getElement(obsElt, "procedure");
        if (procPropElt != null && !dom.existAttribute(procPropElt, "nil"))
        {
            if (dom.existAttribute(procPropElt, "href"))
            {
                ProcedureRef ref = new ProcedureRef();
                XlinkUtils.readXlinkAttributes(dom, procPropElt, ref);
                obs.setProcedure(ref);
            }
            else
            {
                // read procedure as raw XML
                Element procXML = dom.getFirstChildElement(procPropElt);
                if (procXML != null)
                    obs.setProcedure(new ProcedureXML((Element)procXML.cloneNode(true)));
            }
        }
        
        // parameters
        NodeList paramElts = dom.getElements(obsElt, "parameter/*");
        for (int i = 0; i < paramElts.getLength(); i++)
        {
            Element paramElt = (Element)paramElts.item(i);
            String paramName = dom.getAttributeValue(paramElt, "name/href");
            Element valueElt = dom.getElement(paramElt, "value/*");
            Object paramValue;
            
            try
            {
                if (valueElt.getNamespaceURI().equals(SWEStaxBindings.NS_URI))
                    paramValue = sweUtils.readComponent(dom, valueElt);
                else
                    paramValue = valueElt.cloneNode(true);
            }
            catch (XMLReaderException e)
            {
                throw new XMLReaderException("Error while parsing observation parameter", paramElt, e);
            }
            
            obs.addParameter(paramName, paramValue);
        }
        
        // observed property
        Element obsPropElt = dom.getElement(obsElt, "observedProperty");
        if (obsPropElt != null && !obsPropElt.hasAttribute("nil"))
        {
            DefinitionRef ref = new DefinitionRef();
            XlinkUtils.readXlinkAttributes(dom, obsPropElt, ref);
            obs.setObservedProperty(ref);
        }
        
        // read foi
        Element foiPropElt = dom.getElement(obsElt, "featureOfInterest");
        if (foiPropElt != null && !dom.existAttribute(foiPropElt, "nil"))
            obs.setFeatureOfInterest(readFOI(dom, foiPropElt));
        
        // result quality as raw XML
        NodeList qualityElts = dom.getElements(obsElt, "resultQuality/*");
        for (int i = 0; i < qualityElts.getLength(); i++)
            obs.addResultQuality(qualityElts.item(i).cloneNode(true));
             
        // result
        Element resultElt = dom.getElement(obsElt, "result/*");
        if (resultElt != null)
        {
            try
            {
                DataComponent result = sweUtils.readComponent(dom, resultElt);
                obs.setResult(result);
            }
            catch (XMLReaderException e)
            {
                throw new XMLReaderException("Error while parsing observation result", resultElt, e);
            }
        }
        
        return obs;
    }
    
    
    protected GenericFeature readFOI(DOMHelper dom, Element foiPropElt) throws XMLReaderException
    {
        Element featureElt = dom.getFirstChildElement(foiPropElt);
        
        if (featureElt != null)
        {
            return gmlUtils.readFeature(dom, featureElt);
        }
        
        else if (dom.existAttribute(foiPropElt, "href"))
        {
            FeatureRef ref = new FeatureRef();
            XlinkUtils.readXlinkAttributes(dom, foiPropElt, ref);
            return ref;
        }
                
        return null;
    }    
}
