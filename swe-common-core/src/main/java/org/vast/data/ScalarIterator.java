/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import java.util.*;
import net.opengis.swe.v20.DataComponent;


/**
 * <p>
 * Iterates through scalar component definitions only.
 * This does not iterate through each array value.
 * </p>
 *
 * <p>Copyright (c) 2010</p>
 * @author Alexandre Robin
 * @since Apr 21, 2010
 * @version 1.0
 */
public class ScalarIterator extends DataIterator
{
    
    public ScalarIterator(DataComponent rootComponent)
    {
    	super(rootComponent);
    }


    public DataValue next()
    {
        DataComponent nextComponent = null;
        
        do { nextComponent = super.next(); }
        while (!(nextComponent instanceof DataValue));
            
        return (DataValue)nextComponent;
    }
    
    
    public DataComponent[] nextPath()
    {
        DataComponent nextComponent = next();        
        List<DataComponent> componentList = new ArrayList<DataComponent>();
        
        do
        {
            componentList.add(nextComponent);
            nextComponent = nextComponent.getParent();
        }
        while (nextComponent != null);
        
        Collections.reverse(componentList);
        
        return (DataComponent[])componentList.toArray(new DataComponent[0]);
    }
}