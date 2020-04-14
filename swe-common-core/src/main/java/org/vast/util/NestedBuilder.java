/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2020 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.util;


/**
 * <p>
 * Base interface for all nested builders, that is builders that are returned
 * by a parent builder, and that allow control flow to go back to the parent
 * builder when calling {@link #done()}
 * </p>
 * 
 * @param <B> Parent builder type
 *
 * @author Alex Robin
 * @date Apr 13, 2020
 */
public interface NestedBuilder<B>
{

    /**
     * Builds the object created by this builder and returns control flow
     * to the parent builder
     * @return The parent builder for chaining
     */
    public B done();
}
