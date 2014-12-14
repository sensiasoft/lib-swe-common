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

import java.text.ParseException;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.vast.util.DateTimeFormat;
import org.vast.util.TimeExtent;
import org.w3c.dom.Element;


/**
 * <p>
 * Reads GML Time Primitive structures.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @since Sep 01, 2006
 * @version 1.0
 */
public class GMLTimeReader
{
    final static String invalidISO = "Invalid ISO time: ";
    DateTimeFormat timeFormat = new DateTimeFormat();
    double now;
    
    
    public GMLTimeReader()
    {
        now = System.currentTimeMillis() / 1000;
    }
    
    
    /**
     * Reads any supported GML time primitive
     * @param dom
     * @param timeElt
     * @return
     * @throws XMLReaderException 
     */
    public TimeExtent readTimePrimitive(DOMHelper dom, Element timeElt) throws XMLReaderException
    {
        String eltName = timeElt.getLocalName();
        
        if (eltName.equals("TimeInstant"))
            return readTimeInstant(dom, timeElt);
        else if (eltName.equals("TimePeriod"))
            return readTimePeriod(dom, timeElt);
        
        throw new XMLReaderException("Unsupported Time Primitive: " + eltName, timeElt);
    }
    
    
    /**
     * Reads a gml:TimeInstant
     * @param dom
     * @param timeElt
     * @return
     * @throws XMLReaderException
     */
    public TimeExtent readTimeInstant(DOMHelper dom, Element timeElt) throws XMLReaderException
    {
        TimeExtent time = new TimeExtent();
        
        String att = dom.getAttributeValue(timeElt, "timePosition/indeterminatePosition");
        String isoTime = dom.getElementValue(timeElt, "timePosition");
        
        if (att != null && att.equals("now"))
        {
            time.setBaseAtNow(true);
        }
        else if (att != null && att.equals("unknown"))
        {
            time.nullify();
        }
        else
        {
            try
            {
                time.setBaseTime(timeFormat.parseIso(isoTime));
            }
            catch (ParseException e)
            {
                throw new XMLReaderException(invalidISO + isoTime, timeElt);
            }
        }
        
        return time;
    }
    
    
    /**
     * Reads a gml:TimePeriod
     * @param dom
     * @param timePeriodElt
     * @return TimeExtent containing period
     * @throws XMLReaderException
     */
    public TimeExtent readTimePeriod(DOMHelper dom, Element timePeriodElt) throws XMLReaderException
    {
        TimeExtent timeInfo = new TimeExtent();
        
        String startAtt = dom.getAttributeValue(timePeriodElt, "beginPosition/@indeterminatePosition");
        String isoStartTime = dom.getElementValue(timePeriodElt, "beginPosition");
        String stopAtt = dom.getAttributeValue(timePeriodElt, "endPosition/@indeterminatePosition");
        String isoStopTime = dom.getElementValue(timePeriodElt, "endPosition");
        String duration = dom.getElementValue(timePeriodElt, "timeInterval");
        String timeStep = dom.getElementValue(timePeriodElt, "timeStep");
        
        boolean startUnknown = false;
        boolean stopUnknown = false;
        
        try
        {
            // read beginPosition intederminatePosition attribute
            if (startAtt != null)
            {
                if (startAtt.equals("now"))
                    timeInfo.setBeginNow(true);
                else if (startAtt.equals("unknown"))
                    startUnknown = true;
            }
            else
                timeInfo.setStartTime(timeFormat.parseIso(isoStartTime));
            
            // read endPosition intederminatePosition attribute
            if (stopAtt != null)
            {
                if (stopAtt.equals("now"))
                    timeInfo.setEndNow(true);
                else if (startAtt.equals("unknown"))
                    stopUnknown = true;
            }
            else
                timeInfo.setStopTime(timeFormat.parseIso(isoStopTime));
            
            // handle case of period specified with unknown start or stop time
            if (startUnknown || stopUnknown)
            {
                double dT = timeFormat.parseIsoPeriod(duration);
                
                if (startUnknown)
                    timeInfo.setLagTimeDelta(dT);
                else
                    timeInfo.setLeadTimeDelta(dT);
            }
            
            // also parse step time
            if (timeStep != null)
                timeInfo.setTimeStep(timeFormat.parseIsoPeriod(timeStep));
        }
        catch (ParseException e)
        {
            String start = (startAtt != null) ? startAtt : isoStartTime;
            String stop = (stopAtt != null) ? stopAtt : isoStopTime;
            String message = "Invalid Time Period: " + start + "/" + stop;
            if (duration != null)
                message = message + "/" + duration;
            throw new XMLReaderException(message, timePeriodElt);
        }
        
        return timeInfo;
    }


    public double getNow()
    {
        return now;
    }


    public void setNow(double now)
    {
        this.now = now;
    }
}
