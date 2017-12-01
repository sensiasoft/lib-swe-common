/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.DataComponent;


/**
 * <p>
 * Specialized property for holding data components.
 * Overriden methods ensure that parent and name attributes are properly set 
 * for the child data component.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @param <ComponentType> Type of data component this property can hold
 * @since Dec 1, 2017
 */
public class DataComponentProperty<ComponentType extends DataComponent> extends OgcPropertyImpl<ComponentType>
{
    private static final long serialVersionUID = 4972898784235641842L;
    AbstractDataComponentImpl parent;
    
    
    private DataComponentProperty()
    {
        super();
    }
    
    
    public DataComponentProperty(AbstractDataComponentImpl parent)
    {
        this.parent = parent;
    }
    
    
    @Override
    public DataComponentProperty<ComponentType> copy()
    {
        DataComponentProperty<ComponentType> newProp = new DataComponentProperty<>();
        copyTo(newProp);
        return newProp;
    }
    

    @Override
    public void setValue(ComponentType value)
    {
        ((AbstractDataComponentImpl)value).setName(this.name);
        ((AbstractDataComponentImpl)value).setParent(parent);
        super.setValue(value);
    }

}
