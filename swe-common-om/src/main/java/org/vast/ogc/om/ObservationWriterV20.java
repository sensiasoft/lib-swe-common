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

import java.text.NumberFormat;
import java.util.Map.Entry;
import net.opengis.gml.v32.AbstractFeature;
import net.opengis.gml.v32.CodeWithAuthority;
import net.opengis.swe.v20.DataComponent;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.FeatureRef;
import org.vast.ogc.gml.GMLUtils;
import org.vast.ogc.xlink.IXlinkReference;
import org.vast.ogc.xlink.XlinkUtils;
import org.vast.swe.SWEUtils;
import org.vast.xml.DOMHelper;
import org.vast.xml.IXMLWriterDOM;
import org.vast.xml.XMLWriterException;
import org.w3c.dom.Element;


/**
 * <p>
 * Observation Writer for O&M version 2.0
 * </p>
 *
 * @author Alex Robin
 * @since Sep 30, 2012
 * */
public class ObservationWriterV20 implements IXMLWriterDOM<IObservation>
{
    protected GMLUtils gmlUtils = new GMLUtils(GMLUtils.V3_2);
    protected SWEUtils sweUtils = new SWEUtils(SWEUtils.V2_0);
    protected int currentId;
    protected NumberFormat idFormatter;
    
    
    public ObservationWriterV20()
    {
        currentId = 1;
        idFormatter = NumberFormat.getNumberInstance();
        idFormatter.setMinimumIntegerDigits(3);
        idFormatter.setGroupingUsed(false);
    }
    
    
    @Override
    public Element write(DOMHelper dom, IObservation obs) throws XMLWriterException
    {
        dom.addUserPrefix("swe", OGCRegistry.getNamespaceURI(SWEUtils.SWE, SWEUtils.V2_0));
        dom.addUserPrefix("om", OGCRegistry.getNamespaceURI(OMUtils.OM, OMUtils.V2_0));
        dom.addUserPrefix("gml", OGCRegistry.getNamespaceURI(GMLUtils.GML, GMLUtils.V3_2));
        dom.addUserPrefix("xlink", OGCRegistry.getNamespaceURI(OGCRegistry.XLINK));
        
        Element obsElt = dom.createElement("om:" + obs.getQName().getLocalPart());
                
        // gml:id
        String id = obs.getId();
        if (id == null || id.length() == 0)
            id = "OBS_" + idFormatter.format(currentId++);
        dom.setAttributeValue(obsElt, "gml:id", id);
        
        // description
        String desc = obs.getDescription();
        if (desc != null)
            dom.setElementValue(obsElt, "gml:description", desc);
        
        // identifier
        if (obs.isSetIdentifier())
        {
            Element elt = dom.setElementValue(obsElt, "gml:identifier", obs.getUniqueIdentifier());
            dom.setAttributeValue(elt, "@codeSpace", "uid");
        }
        
        // names
        for (CodeWithAuthority name: obs.getNameList())
        {
            Element nameElt = dom.addElement(obsElt, "+gml:name");
            dom.setElementValue(nameElt, name.getValue());
            if (name.getCodeSpace() != null && name.getCodeSpace().length() > 0)
                dom.setAttributeValue(nameElt, "@codeSpace", name.getCodeSpace());
        }
        
        // type
        String obsType = obs.getType();
        if (obsType != null)
            dom.setAttributeValue(obsElt, "om:type/xlink:href", obsType);
        
        // TODO write ISO metadata
        
        // related observations
        if (obs.getRelatedObservations() != null)
        {
            for (IXlinkReference<IObservation> obsRef: obs.getRelatedObservations())
            {
                Element obsRefElt = dom.addElement(obsElt, "+om:relatedObservation/om:ObservationContext");
                dom.setElementValue(obsRefElt, "om:relatedObservation", obsRef.getHref());
                if (obsRef.getRole() != null)
                    dom.setElementValue(obsRefElt, "om:role", obsRef.getRole());
            }
        }
        
        Element timePropElt;
        Element timeElt;
        
        // phenomenon time (mandatory)
        timePropElt = dom.addElement(obsElt, "om:phenomenonTime");
        timeElt = gmlUtils.writeTimeExtentAsTimePrimitive(dom, obs.getPhenomenonTime());
        timePropElt.appendChild(timeElt);
        
        // result time (mandatory)
        timePropElt = dom.addElement(obsElt, "om:resultTime");
        timeElt = gmlUtils.writeTimeExtentAsTimePrimitive(dom, obs.getResultTime());
        timePropElt.appendChild(timeElt);
        
        // valid time
        if (obs.getValidTime() != null)
        {
            timePropElt = dom.addElement(obsElt, "om:validTime");
            timeElt = gmlUtils.writeTimeExtentAsTimePrimitive(dom, obs.getValidTime());
            timePropElt.appendChild(timeElt);
        }
        
        // procedure
        IProcedure proc = obs.getProcedure();
        if (proc != null)
        {
            Element propElt = dom.addElement(obsElt, "om:procedure");
            if (proc instanceof ProcedureRef)
                XlinkUtils.writeXlinkAttributes(dom, propElt, (ProcedureRef)proc);
            else
                throw new XMLWriterException("Unsupported procedure type: " + proc.getClass().getCanonicalName());
        }
        else
            dom.setXsiNil(obsElt, "om:procedure");
        
        // parameters
        // TODO add support for parameters referenced through hyperlinks?
        if (obs.getParameters() != null)
        {
            for (Entry<String, Object> param: obs.getParameters().entrySet())
            {
                Object value = param.getValue();
                Element nvElt = dom.addElement(obsElt, "+om:parameter/om:NamedValue");
                dom.setAttributeValue(nvElt, "om:name/xlink:href", param.getKey());
                Element valueElt = dom.addElement(nvElt, "om:value");
                
                if (value instanceof DataComponent)
                {
                    try
                    {
                        Element componentElt = sweUtils.writeComponent(dom, (DataComponent)value, true);
                        valueElt.appendChild(componentElt);
                    }
                    catch (XMLWriterException e)
                    {
                        throw new XMLWriterException("Error while writing observation parameter " + param.getKey(), nvElt, e);
                    }
                }
                else if (value instanceof String || value instanceof Number || value instanceof Boolean)
                {
                    dom.setElementValue(nvElt, value.toString());
                }
                else if (value instanceof Element)
                {
                    Element importedElt = (Element)nvElt.getOwnerDocument().importNode((Element)value, true);
                    valueElt.appendChild(importedElt);
                }
                else
                    throw new XMLWriterException("Unsupported parameter type: " + value.getClass());
            }
        }
        
        // observedProperty
        if (obs.getObservedProperty() != null)
            dom.setAttributeValue(obsElt, "om:observedProperty/xlink:href", obs.getObservedProperty().getHref());
        else
            dom.setXsiNil(obsElt, "om:observedProperty");
        
        // foi
        Element foiPropElt = dom.addElement(obsElt, "om:featureOfInterest");
        AbstractFeature foi = obs.getFeatureOfInterest();
        if (foi != null)
            writeFOI(dom, foiPropElt, foi);
        else
            dom.setXsiNil(obsElt, "om:featureOfInterest");
        
        // TODO write ISO quality
        
        // result
        Element resultElt = dom.addElement(obsElt, "om:result");
        if (obs.getResult() != null)
        {            
            try
            {
                Element componentElt = sweUtils.writeComponent(dom, obs.getResult(), true);
                resultElt.appendChild(componentElt);
                
                String sweQName = componentElt.getLocalName();
                dom.addUserPrefix("xsi", DOMHelper.XSI_NS_URI);
                dom.setAttributeValue(resultElt, "xsi:type", "swe:" + sweQName + "PropertyType");
            }
            catch (XMLWriterException e)
            {
                throw new XMLWriterException("Error while writing observation result", resultElt, e);
            }
        }
        
        return obsElt;
    }
    
    
    protected void writeFOI(DOMHelper dom, Element foiPropElt, AbstractFeature foi) throws XMLWriterException
    {
        if (foi instanceof FeatureRef)
        {
            XlinkUtils.writeXlinkAttributes(dom, foiPropElt, (FeatureRef)foi);
        }
        else
        {
            Element foiElt = gmlUtils.writeFeature(dom, foi);
            foiPropElt.appendChild(foiElt);
        }
    }
}
