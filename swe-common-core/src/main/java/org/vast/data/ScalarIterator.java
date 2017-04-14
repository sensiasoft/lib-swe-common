/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
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
 * @author Alex Robin
 * @since Apr 21, 2010
 * */
public class ScalarIterator extends DataIterator
{
    
    public ScalarIterator(DataComponent rootComponent)
    {
    	super(rootComponent);
    }


    @Override
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
        while (nextComponent != rootComponent);
        
        Collections.reverse(componentList);
        
        return componentList.toArray(new DataComponent[0]);
    }
}