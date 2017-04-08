/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe;

import net.opengis.swe.v20.DataComponent;


/**
 * <p>
 * A filter for data component.<br/>
 * An instance of this class can be passed to several methods in the
 * DataComponent APIs in order to filter a component tree.
 * </p>
 *
 * @author Alex Robin
 * @since Apr 9, 2015
 */
public interface IComponentFilter
{

    /**
     * Tells wether the given component should be selected
     * @param comp component to test
     * @return tru if selected, false otherwise
     */
    public boolean accept(DataComponent comp);
}
