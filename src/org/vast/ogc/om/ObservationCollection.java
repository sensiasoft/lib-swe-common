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


/**
 * <p><b>Title:</b>
 * Observation Collection
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Implementation of the Observation Collection which uses the 
 * composite pattern to encapsulate a list of member Observations
 * while still being an Observation itself.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Feb 20, 2007
 * @version 1.0
 */
public class ObservationCollection extends AbstractObservation
{
    protected List<AbstractObservation> members;

    public ObservationCollection()
    {
        members = new ArrayList<AbstractObservation>();
    }
    
    
    public List<AbstractObservation> getMembers()
    {
        return members;
    }


    public void setMembers(List<AbstractObservation> members)
    {
        this.members = members;
    }
}
