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
 * @author Alex Robin
 * @param <B> Type of builder (must be the type derived from this class)
 * @param <T> Type of object that can be built by this builder
 * @date Oct 29, 2018
 */
public abstract class BaseBuilder<B extends BaseBuilder<B, T>, T>
{
    protected T instance;
    
    
    protected BaseBuilder(T instance)
    {
        this.instance = instance;
    }
    
    
    public T build()
    {
        T newInstance = instance;
        instance = null; // nullify instance to prevent further changes
        return newInstance;
    }
}
