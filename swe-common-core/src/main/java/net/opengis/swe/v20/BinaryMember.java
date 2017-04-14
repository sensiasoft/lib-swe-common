/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.swe.v20;

import java.io.Serializable;

/**
 * <p>
 * Base interface for binary encoding members: BinaryComponent and BinaryBlock
 * </p>
 *
 * @author Alex Robin
 * @since Oct 26, 2014
 */
@SuppressWarnings("javadoc")
public interface BinaryMember extends Serializable
{
    /**
     * Gets the ref property
     */
    public String getRef();
    
    
    /**
     * Sets the ref property
     */
    public void setRef(String ref);
}
