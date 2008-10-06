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

import java.io.IOException;
import java.io.OutputStream;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataEncoding;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.GMLException;
import org.vast.ogc.gml.GMLTimeWriter;
import org.vast.sweCommon.DataSinkDOM;
import org.vast.sweCommon.SweComponentWriterV0;
import org.vast.sweCommon.SweEncodingWriterV0;
import org.vast.xml.*;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b>
 * Observation Writer V0.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Observation Writer for version 0.0
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Feb 23, 2007
 * @version 1.0
 */
public class ObservationWriterV01 implements ObservationWriter
{
    protected GMLTimeWriter timeWriter;
    protected SweComponentWriterV0 componentWriter;
    protected SweEncodingWriterV0 encodingWriter;
    
    
    public ObservationWriterV01()
    {
        timeWriter = new GMLTimeWriter();
        componentWriter = new SweComponentWriterV0();
        encodingWriter = new SweEncodingWriterV0();
    }
    
	
    public void writeObservation(OutputStream os, AbstractObservation obs) throws OMException
    {
        try
        {
            DOMHelper dom = new DOMHelper("observation");
            Element obsElt = writeObservation(dom, obs);
            dom.serialize(obsElt, os, null);
        }
        catch (IOException e)
        {
            throw new OMException("Error while writing Observation XML in OutputStream");
        }
    }
    
    
    public Element writeObservation(DOMHelper dom, AbstractObservation obs) throws OMException
    {
        dom.addUserPrefix("swe", OGCRegistry.getNamespaceURI(OGCRegistry.SWE, "1.0"));
        dom.addUserPrefix("om", OGCRegistry.getNamespaceURI(OGCRegistry.OM, "0.0"));
        dom.addUserPrefix("gml", OGCRegistry.getNamespaceURI(OGCRegistry.GML));
        dom.addUserPrefix("xlink", OGCRegistry.getNamespaceURI(OGCRegistry.XLINK));
        
        if (obs instanceof ObservationStream)
            return writeObservationStream(dom, (ObservationStream)obs);
        else
            throw new OMException("Unsupported Observation Type: " + obs.getClass().getName());  
    }


    protected Element writeObservationStream(DOMHelper dom, ObservationStream obs) throws OMException
    {
        Element obsElt = dom.createElement("om:CommonObservation");
        
        // first write common parameters
        // (description, names, time, procedure, observables, foi)
        writeCommonHeader(dom, obsElt, obs);
        
        // write swe data definition
        try
        {
            Element dataDefElt = dom.addElement(obsElt, "om:resultDefinition/swe:DataDefinition");
            
            // write data components
            DataComponent components = obs.getResult().getDataComponents();
            Element propertyElt = dom.addElement(dataDefElt, "swe:dataComponents");
            dom.setAttributeValue(propertyElt, "@name", components.getName());
            Element componentElt = componentWriter.writeComponent(dom, components);
            propertyElt.appendChild(componentElt);
            
            // write data encoding
            DataEncoding encoding = obs.getResult().getDataEncoding();
            propertyElt = dom.addElement(dataDefElt, "swe:encoding");
            Element encodingElt = encodingWriter.writeEncoding(dom, encoding);
            propertyElt.appendChild(encodingElt);
            
            // write result
            Element resultElt = dom.addElement(obsElt, "om:result");
            DataSinkDOM dataSink = new DataSinkDOM(dom, resultElt);
            obs.getResult().writeData(dataSink);
        }
        catch (CDMException e)
        {
            throw new OMException(e);
        }
        
        return obsElt;
    }
    
    
    protected void writeCommonHeader(DOMHelper dom, Element obsElt, AbstractObservation obs) throws OMException
    {
        // write description
        String desc = obs.getDescription();
        if (desc != null)
            dom.setElementValue(obsElt, "gml:description", desc);
        
        // write names
        for (int i=0; i<obs.getNames().size(); i++)
        {
            QName name = obs.getNames().get(i);
            Element nameElt = dom.addElement(obsElt, "+gml:name");
            dom.setElementValue(nameElt, name.getLocalName());
            if (name.getNsUri() != null)
                dom.setAttributeValue(nameElt, "@codeSpace", name.getNsUri());
        }
        
        // write eventTime
        if (obs.getTime() != null)
        {
            try
            {
                Element timePropElt = dom.addElement(obsElt, "om:eventTime");
                Element timeElt = timeWriter.writeTime(dom, obs.getTime());
                timePropElt.appendChild(timeElt);
            }
            catch (GMLException e)
            {
                throw new OMException(e);
            }
        }
        
        // write procedure
        if (obs.getProcedure() != null)
            dom.setAttributeValue(obsElt, "om:procedure/xlink:href", obs.getProcedure());
        
        // write observedProperty
        dom.setAttributeValue(obsElt, "om:observedProperty/xlink:href", "xsi:nil");
        
        // write foi
        //TODO write FOI logic

    }
    
}
