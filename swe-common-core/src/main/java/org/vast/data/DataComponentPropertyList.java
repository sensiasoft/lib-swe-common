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

import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.OgcPropertyList;
import net.opengis.swe.v20.DataComponent;


/**
 * <p>
 * Specialized list for holding data component properties.
 * Overriden methods ensure that parent and name attributes are properly set 
 * for each data component added to the list.
 * </p>
 *
 * @author Alex Robin
 * @param <ComponentType> Type of data component this list can hold
 * @since Nov 9, 2014
 */
public class DataComponentPropertyList<ComponentType extends DataComponent> extends OgcPropertyList<ComponentType>
{
    private static final long serialVersionUID = -7810621773444025503L;
    AbstractDataComponentImpl parent;
    
    
    @SuppressWarnings("unused")
    private DataComponentPropertyList()
    {
        super();
    }
    
    
    public DataComponentPropertyList(AbstractDataComponentImpl parent)
    {
        this.parent = parent;
    }
    
    
    public DataComponentPropertyList(AbstractDataComponentImpl parent, int size)
    {
        super(size);
        this.parent = parent;
    }
    
    
    @Override
    public void add(OgcProperty<ComponentType> prop)
    {
        if (prop.hasValue())
        {
            prop.getValue().setName(prop.getName());
            ((AbstractDataComponentImpl)prop.getValue()).setParent(parent);
        }
        
        super.add(prop);
    }
    

    @Override
    public OgcProperty<ComponentType> add(String name, ComponentType component)
    {
        component.setName(name);
        ((AbstractDataComponentImpl)component).setParent(parent);
        return super.add(name, component);
    }
    

    @Override
    public boolean add(ComponentType component)
    {
        ((AbstractDataComponentImpl)component).setParent(parent);
        super.add(component.getName(), component);
        return true;
    }
    

    @Override
    public void add(int index, ComponentType component)
    {
        ((AbstractDataComponentImpl)component).setParent(parent);
        String name = component.getName();
        checkName(name);
        OgcPropertyImpl<ComponentType> prop = new OgcPropertyImpl<>(name, component);
        items.add(index, prop);
        nameMap.put(name, prop);
    }

}
