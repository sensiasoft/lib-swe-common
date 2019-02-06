/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.fast;

import java.util.Collection;
import java.util.HashSet;
import org.vast.swe.IComponentFilter;
import net.opengis.swe.v20.DataComponent;


public class FilterByDefinition implements IComponentFilter
{
    HashSet<String> selectedDefUris = new HashSet<>();
    
    
    public FilterByDefinition(Collection<String> defUris)
    {
        selectedDefUris.addAll(defUris);
    }
    
    
    @Override
    public boolean accept(DataComponent comp)
    {
        String def = comp.getDefinition();
        if (def == null || !selectedDefUris.contains(def))
            return false;
        else
            return true;
    }

}
