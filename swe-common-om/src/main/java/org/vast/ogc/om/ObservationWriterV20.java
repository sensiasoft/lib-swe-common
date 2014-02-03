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

import java.text.NumberFormat;
import java.util.Map.Entry;
import javax.xml.namespace.QName;
import org.vast.cdm.common.DataComponent;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.FeatureRef;
import org.vast.ogc.gml.GMLFeatureWriter;
import org.vast.ogc.gml.GMLTimeWriter;
import org.vast.ogc.gml.IFeature;
import org.vast.ogc.xlink.IXlinkReference;
import org.vast.ogc.xlink.XlinkUtils;
import org.vast.sweCommon.SWECommonUtils;
import org.vast.sweCommon.SweComponentWriterV20;
import org.vast.xml.DOMHelper;
import org.vast.xml.IXMLWriterDOM;
import org.vast.xml.XMLWriterException;
import org.w3c.dom.Element;


/**
 * <p>
 * Observation Writer for O&M version 2.0
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @since Sep 30, 2012
 * @version 1.0
 */
public class ObservationWriterV20 implements IXMLWriterDOM<IObservation>
{
    private static String GML_VERSION = "3.2";
    protected GMLTimeWriter timeWriter;
    protected GMLFeatureWriter featureWriter;
    protected SweComponentWriterV20 sweWriter;
    protected int currentId;
    protected NumberFormat idFormatter;
    
    
    public ObservationWriterV20()
    {
        timeWriter = new GMLTimeWriter(GML_VERSION);
        featureWriter = new GMLFeatureWriter();
        featureWriter.setGmlVersion(GML_VERSION);
        sweWriter = new SweComponentWriterV20(true);
        
        currentId = 1;
        idFormatter = NumberFormat.getNumberInstance();
        idFormatter.setMinimumIntegerDigits(3);
        idFormatter.setGroupingUsed(false);
    }
    
    
    public Element write(DOMHelper dom, IObservation obs) throws XMLWriterException
    {
        dom.addUserPrefix("swe", OGCRegistry.getNamespaceURI(SWECommonUtils.SWE, "2.0"));
        dom.addUserPrefix("om", OGCRegistry.getNamespaceURI(OMUtils.OM, "2.0"));
        dom.addUserPrefix("gml", OGCRegistry.getNamespaceURI(OGCRegistry.GML, GML_VERSION));
        dom.addUserPrefix("xlink", OGCRegistry.getNamespaceURI(OGCRegistry.XLINK));
        
        Element obsElt = dom.createElement("om:Observation");
                
        // gml:id
        String id = obs.getLocalId();
        if (id == null)
            id = "OBS_" + idFormatter.format(currentId++);
        dom.setAttributeValue(obsElt, "gml:id", id);
        
        // description
        String desc = obs.getDescription();
        if (desc != null)
            dom.setElementValue(obsElt, "gml:description", desc);
        
        // identifier
        String identifier = obs.getIdentifier();
        if (identifier != null)
        {
            Element elt = dom.setElementValue(obsElt, "gml:identifier", identifier);
            dom.setAttributeValue(elt, "@codeSpace", "uid");
        }
        
        // names
        for (int i=0; i<obs.getNames().size(); i++)
        {
            QName name = obs.getNames().get(i);
            Element nameElt = dom.addElement(obsElt, "+gml:name");
            dom.setElementValue(nameElt, name.getLocalPart());
            if (name.getNamespaceURI() != null)
                dom.setAttributeValue(nameElt, "@codeSpace", name.getNamespaceURI());
        }
        
        // type
        String obsType = obs.getType();
        if (obsType != null)
            dom.setElementValue(obsElt, "om:type", obsType);
        
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
        timeElt = timeWriter.writeTime(dom, obs.getPhenomenonTime());
        timePropElt.appendChild(timeElt);
        
        // result time (mandatory)
        timePropElt = dom.addElement(obsElt, "om:resultTime");
        timeElt = timeWriter.writeTime(dom, obs.getResultTime());
        timePropElt.appendChild(timeElt);
        
        // valid time
        if (obs.getValidTime() != null)
        {
            timePropElt = dom.addElement(obsElt, "om:validTime");
            timeElt = timeWriter.writeTime(dom, obs.getValidTime());
            timePropElt.appendChild(timeElt);
        }
        
        // procedure
        IProcedure proc = obs.getProcedure();
        if (proc != null && proc instanceof ProcedureRef)
            dom.setAttributeValue(obsElt, "om:procedure/xlink:href", ((ProcedureRef)proc).getHref());
        else
            dom.setXsiNil(obsElt, "om:procedure");
        
        // parameters
        // TODO support for parameters referenced through hyperlinks ?
        if (obs.getParameters() != null)
        {
            for (Entry<String, Object> param: obs.getParameters().entrySet())
            {
                Object value = param.getValue();
                Element nvElt = dom.addElement(obsElt, "+om:parameter/om:NamedValue");
                dom.setElementValue(nvElt, "om:name", param.getKey());
                dom.addElement(nvElt, "om:value");
                
                if (value instanceof DataComponent)
                {
                    try
                    {
                        Element componentElt = sweWriter.writeComponent(dom, (DataComponent)value, true);
                        nvElt.appendChild(componentElt);
                    }
                    catch (XMLWriterException e)
                    {
                        throw new XMLWriterException("Error while writing observation parameter " + param.getKey(), nvElt, e);
                    }
                }
                else
                {
                    dom.setElementValue(nvElt, value.toString());
                }
            }
        }
        
        // observedProperty
        if (obs.getObservedProperty() != null)
            dom.setElementValue(obsElt, "om:observedProperty", obs.getObservedProperty().getHref());
        else
            dom.setXsiNil(obsElt, "om:observedProperty");
        
        // write foi
        IFeature foi = obs.getFeatureOfInterest();
        if (foi != null)
        {
            if (foi instanceof FeatureRef)
                dom.setAttributeValue(obsElt, "om:featureOfInterest/xlink:href", ((FeatureRef)foi).getHref());
            else
                writeFOI(dom, obsElt, foi);
        }
        else
            dom.setXsiNil(obsElt, "om:featureOfInterest");
        
        // TODO write ISO quality
        
        // result
        if (obs.getResult() != null)
        {
            Element resultElt = dom.addElement(obsElt, "swe:result");
            try
            {
                Element componentElt = sweWriter.writeComponent(dom, obs.getResult(), true);
                resultElt.appendChild(componentElt);
            }
            catch (XMLWriterException e)
            {
                throw new XMLWriterException("Error while writing observation result", resultElt, e);
            }
        }
        
        return obsElt;
    }
    
    
    protected void writeFOI(DOMHelper dom, Element obsElt, IFeature foi) throws XMLWriterException
    {
        Element foiPropElt = dom.addElement(obsElt, "+om:featureOfInterest");
        
        if (foi instanceof FeatureRef)
        {
            XlinkUtils.writeXlinkAttributes(foiPropElt, (FeatureRef)foi);
        }
        else
        {
            Element foiElt = featureWriter.write(dom, foi);
            foiPropElt.appendChild(foiElt);
        }
    }
}
