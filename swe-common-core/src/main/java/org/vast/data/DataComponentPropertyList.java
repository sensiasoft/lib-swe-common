/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import net.opengis.OgcProperty;
import net.opengis.OgcPropertyList;
import net.opengis.swe.v20.AbstractDataComponent;


/**
 * <p>
 * Specialized list for holding data component properties.
 * Overriden methods ensure that parent and name attributes are properly set 
 * for each data component added to the list.
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @param <ComponentType> Type of data component this list can hold
 * @since Nov 9, 2014
 */
public class DataComponentPropertyList<ComponentType extends AbstractDataComponent> extends OgcPropertyList<ComponentType>
{
    AbstractDataComponentImpl parent;
    
    
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
            ((AbstractDataComponentImpl)prop.getValue()).setName(prop.getName());
            ((AbstractDataComponentImpl)prop.getValue()).setParent(parent);
        }
        
        super.add(prop);
    }
    

    @Override
    public OgcProperty<ComponentType> add(String name, ComponentType component)
    {
        ((AbstractDataComponentImpl)component).setName(name);
        ((AbstractDataComponentImpl)component).setParent(parent);
        return super.add(name, component);
    }
    

    @Override
    public boolean add(ComponentType component)
    {
        ((AbstractDataComponentImpl)component).setParent(parent);
        return super.add(component);
    }
    

    @Override
    public void add(int index, ComponentType component)
    {
        ((AbstractDataComponentImpl)component).setParent(parent);
        super.add(index, component);
    }

}
