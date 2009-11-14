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

import org.vast.ogc.gml.Feature;
import org.vast.util.Contact;
import org.vast.util.TimeInfo;


/**
 * <p><b>Title:</b>
 * Abstract Observation
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Implementation of the AbstractObservation object
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Feb 20, 2007
 * @version 1.0
 */
public abstract class AbstractObservation extends Feature
{
    protected final static String TIME = "time";
    protected final static String RESPONSIBLE = "responsible";
    protected final static String PROCEDURE = "procedure";
    protected final static String FOI = "featureOfInterest";


    public TimeInfo getTime()
    {
        return (TimeInfo)properties.get(TIME);
    }


    public void setTime(TimeInfo time)
    {
        setProperty(TIME, time);
    }
    
    
    public Contact getResponsible()
    {
        return (Contact)properties.get(RESPONSIBLE);
    }


    public void setResponsible(Contact contact)
    {
        setProperty(RESPONSIBLE, contact);
    }
    
    
    public String getProcedure()
    {
        return (String)properties.get(PROCEDURE);
    }


    public void setProcedure(String procedure)
    {
        setProperty(PROCEDURE, procedure);
    }


    public Feature getFeatureOfInterest()
    {
        return (Feature)properties.get(FOI);
    }


    public void setFeatureOfInterest(Feature featureOfInterest)
    {
        setProperty(FOI, featureOfInterest);
    }
}
