/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.om;

import java.util.ArrayList;
import java.util.List;
import org.vast.cdm.common.DataComponent;
import org.vast.ogc.def.DefinitionRef;
import org.vast.ogc.gml.FeatureRef;
import org.vast.ogc.gml.GMLFeatureReader;
import org.vast.ogc.gml.GMLTimeReader;
import org.vast.ogc.gml.IFeature;
import org.vast.ogc.xlink.XlinkUtils;
import org.vast.sweCommon.SweComponentReaderV1;
import org.vast.util.TimeExtent;
import org.vast.xml.*;
import org.w3c.dom.*;


/**
 * <p>
 * Reader for O&M observations v1.0
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @since Feb 23, 2007
 * @version 1.0
 */
public class ObservationReaderV10 implements IXMLReaderDOM<IObservation>
{
	protected GMLTimeReader timeReader;
	protected GMLFeatureReader featureReader;
    protected SweComponentReaderV1 sweReader;
    
    
    public ObservationReaderV10()
    {
        timeReader = new GMLTimeReader();
        featureReader = new GMLFeatureReader();
        sweReader = new SweComponentReaderV1();
    }
    
	
    @Override
    public IObservation read(DOMHelper dom, Element obsElt) throws XMLReaderException
    {
        IObservation obs = new ObservationImpl();
        Element timeElt;
        TimeExtent time;
        
        // name
        String name = dom.getElementValue(obsElt, "name");
        obs.setName(name);
        
        // sampling time
        timeElt = dom.getElement(obsElt, "samplingTime/*");
        time = timeReader.readTimePrimitive(dom, timeElt);
        obs.setPhenomenonTime(time);
        
        // result time
        timeElt = dom.getElement(obsElt, "resultTime/*");
        if (timeElt != null)
        {
            time = timeReader.readTimePrimitive(dom, timeElt);
            obs.setResultTime(time);
        }
        
        // procedure
        String href = dom.getAttributeValue(obsElt, "procedure/@href");
        if (href != null)
            obs.setProcedure(new ProcedureRef(href));
        else
        {
            // read procedure as raw XML
            Element procXML = dom.getElement(obsElt, "procedure/*");
            if (procXML != null)
                obs.setProcedure(new ProcedureXML((Element)procXML.cloneNode(true)));
        }  
        
        // observed property
        href = dom.getAttributeValue(obsElt, "observedProperty/@href");
        if (href != null)
            obs.setObservedProperty(new DefinitionRef(href));
            
        // foi
        Element foiElt = dom.getElement(obsElt, "featureOfInterest/*");
        if (foiElt != null)
        {
            IFeature foi = readFOI(dom, foiElt);
            obs.setFeatureOfInterest(foi);
        }
        
        // parameters
        NodeList paramElts = dom.getElements(obsElt, "parameter");
        for (int i = 0; i < paramElts.getLength(); i++)
        {
            try
            {
                Element paramElt = (Element)paramElts.item(i);
                String paramName = dom.getAttributeValue(paramElt, "name");
                Element valueElt = dom.getFirstChildElement(paramElt);
                DataComponent paramValue = sweReader.readComponent(dom, valueElt);
                obs.addParameter(paramName, paramValue);
            }
            catch (XMLReaderException e)
            {
                throw new XMLReaderException("Error while parsing parameter " + name, e);
            }
        }
        
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
    
    
    /**
     * Reads a collection and all its member recursively
     * @param dom
     * @param obsElt
     * @return
     * @throws OMException
     */
    public List<IObservation> readObsCollection(DOMHelper dom, Element obsElt) throws XMLReaderException
    {
        NodeList memberList = dom.getElements(obsElt, "member");
        List<IObservation> collection = new ArrayList<IObservation>(memberList.getLength());
        
        // read members        
        for (int i=0; i<memberList.getLength(); i++)
        {
            Element memberElt = (Element)memberList.item(i);
            Element memberObsElt = dom.getFirstChildElement(memberElt);
            IObservation obs = read(dom, memberObsElt);
            collection.add(obs);
        }
        
        return collection;
    }
    
    
    
    
    
    /**
     * Reads the feature of interest XML
     * @param dom
     * @param foiElt
     * @return
     * @throws OMException
     */
    protected IFeature readFOI(DOMHelper dom, Element foiElt) throws XMLReaderException
    {
        Element featureElt = dom.getFirstChildElement(foiElt);
        
        if (featureElt != null)
        {
            FeatureRef ref = new FeatureRef();
            XlinkUtils.readXlinkAttributes(dom, foiElt, ref);
            return ref;
        }
        else
        {
            return featureReader.read(dom, featureElt);
        }
    }    
}
