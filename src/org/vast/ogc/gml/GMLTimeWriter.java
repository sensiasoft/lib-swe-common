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

package org.vast.ogc.gml;

import java.util.UUID;
import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.util.DateTimeFormat;
import org.vast.util.TimeInfo;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b>
 * GML Time Writer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Used to serialize TimeInfo object to GML. 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 25, 2006
 * @version 1.0
 */
public class GMLTimeWriter
{
    protected double now;
    private String version = null;
    
    
    public GMLTimeWriter()
    {
        now = System.currentTimeMillis() / 1000;
    }
    
    
    public GMLTimeWriter(String version)
    {    	
    	this.version = version;
    }
    
        
    public Element writeTime(DOMHelper dom, TimeInfo timeInfo) throws GMLException
    {
    	dom.addUserPrefix("gml", OGCRegistry.getNamespaceURI(OGCRegistry.GML, version));
    	
    	Element timeElt;
        int zone = timeInfo.getTimeZone();
        
        if (timeInfo.isTimeInstant())
        {
            timeElt = dom.createElement("gml:TimeInstant");
            
            if (timeInfo.isBeginNow() || timeInfo.isEndNow() || timeInfo.isBaseAtNow())
                dom.setAttributeValue(timeElt, "gml:timePosition/@indeterminatePosition", "now");
            else
                dom.setElementValue(timeElt, "gml:timePosition", DateTimeFormat.formatIso(timeInfo.getStartTime(), zone));
        }
        else
        {
            timeElt = dom.createElement("gml:TimePeriod");
            
            // case of relative start or stop (now +/- period)
            if (timeInfo.isBaseAtNow())
            {
                if (timeInfo.getLeadTimeDelta() > 0.0 && timeInfo.getLagTimeDelta() > 0.0)
                {
                    dom.setElementValue(timeElt, "gml:beginPosition", DateTimeFormat.formatIso(now - timeInfo.getLagTimeDelta(), zone));
                    dom.setElementValue(timeElt, "gml:endPosition", DateTimeFormat.formatIso(now + timeInfo.getLeadTimeDelta(), zone));
                }
                else if (timeInfo.getLagTimeDelta() == 0.0)
                {
                    dom.setAttributeValue(timeElt, "gml:beginPosition/@indeterminatePosition", "now");
                    dom.setAttributeValue(timeElt, "gml:endPosition/@indeterminatePosition", "unknown");
                    dom.setElementValue(timeElt, "gml:timeInterval", DateTimeFormat.formatIsoPeriod(timeInfo.getLeadTimeDelta()));
                }
                else if (timeInfo.getLeadTimeDelta() == 0.0)
                {
                    dom.setAttributeValue(timeElt, "gml:beginPosition/@indeterminatePosition", "unknown");
                    dom.setAttributeValue(timeElt, "gml:endPosition/@indeterminatePosition", "now");
                    dom.setElementValue(timeElt, "gml:timeInterval", DateTimeFormat.formatIsoPeriod(timeInfo.getLagTimeDelta()));
                }             
            }
            
            // case of absolute start and stop
            else
            {
                if (timeInfo.isBeginNow())
                    dom.setAttributeValue(timeElt, "gml:beginPosition/@indeterminatePosition", "now");
                else
                    dom.setElementValue(timeElt, "gml:beginPosition", DateTimeFormat.formatIso(timeInfo.getStartTime(), zone));
                
                if (timeInfo.isEndNow())
                    dom.setAttributeValue(timeElt, "gml:endPosition/@indeterminatePosition", "now");
                else
                    dom.setElementValue(timeElt, "gml:endPosition", DateTimeFormat.formatIso(timeInfo.getStopTime(), zone));
            }            
            
            // handle time step if specified (i.e. not 0)
            if (timeInfo.getTimeStep() != 0)
            {
                dom.setElementValue(timeElt, "gml:timeStep", DateTimeFormat.formatIsoPeriod(timeInfo.getTimeStep()));
            }
        }
        
        // assign random ID
        String randomId = "T" + Integer.toString(UUID.randomUUID().hashCode());
        timeElt.setAttribute("gml:id", randomId);
        
        return timeElt;
    }
}
