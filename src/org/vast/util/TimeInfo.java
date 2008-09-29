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
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.util;



/**
 * <p><b>Title:</b><br/>
 * TimeInfo
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Simple structure containing OWS style request time
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @since Aug 9, 2005
 * @version 1.0
 */
public class TimeInfo extends TimeExtent
{
    
	@Override
    public TimeInfo copy()
    {
        TimeInfo timeInfo = new TimeInfo();
        
        timeInfo.baseTime = this.getBaseTime();
        timeInfo.timeBias = this.timeBias;
        timeInfo.timeStep = this.timeStep;
        timeInfo.leadTimeDelta = this.leadTimeDelta;
        timeInfo.lagTimeDelta = this.lagTimeDelta;
        timeInfo.baseAtNow = this.baseAtNow;
        timeInfo.endNow = this.endNow;
        timeInfo.beginNow = this.beginNow;
        timeInfo.timeZone = this.timeZone;
        
        return timeInfo;
    }
}