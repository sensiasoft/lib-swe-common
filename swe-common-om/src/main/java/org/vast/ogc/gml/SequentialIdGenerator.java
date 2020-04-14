/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2020 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import org.vast.util.Asserts;
import net.opengis.gml.v32.AbstractGML;


/**
 * <p>
 * Implementation of GML ID generator that just creates numerical IDs in 
 * sequence, prefixed by a configurable string. 
 * </p>
 * 
 * @param <T> Type of identified object
 *
 * @author Alex Robin
 * @date Apr 12, 2020
 */
public class SequentialIdGenerator<T> implements GmlIdGenerator<T>
{
    protected String prefix;
    protected boolean keepOriginal = true;
    protected int counter;
    
    
    /**
     * @param prefix
     * @param keepOriginal Keep the ID contained in the object if set
     */
    public SequentialIdGenerator(String prefix, boolean keepOriginal)
    {
        this.prefix = Asserts.checkNotNull(prefix, "prefix");
        this.keepOriginal = keepOriginal;
    }
    
    
    
    @Override
    public String nextId(T obj)
    {
        if (keepOriginal && obj instanceof AbstractGML)
        {
            String id = ((AbstractGML) obj).getId();
            if (id != null && !id.isBlank())
                return ((AbstractGML) obj).getId();
        }
        
        return prefix + (++counter);
    }

}
