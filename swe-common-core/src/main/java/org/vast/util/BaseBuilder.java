/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.util;


/**
 * <p>
 * Base class for all builders
 * </p>
 * 
 * @param <T> Type of object that can be built by this builder
 *
 * @author Alex Robin
 * @date Oct 29, 2018
 */
public abstract class BaseBuilder<T>
{
    protected T instance;
    
    
    protected BaseBuilder()
    {
    }
    
    
    protected BaseBuilder(T instance)
    {
        this.instance = instance;
    }
    
    
    /**
     * Builds the object configured by this builder. This can only be called once.
     * @return The new object instance.
     * 
     */
    public T build()
    {
        Asserts.checkNotNull(instance, "build() can only be called once");
        T newInstance = instance;
        instance = null; // nullify instance to prevent further changes
        return newInstance;
    }
}
