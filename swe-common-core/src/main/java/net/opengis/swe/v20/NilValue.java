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
 * POJO class for XML type NilValue(@http://www.opengis.net/swe/2.0).
 *
 */
@SuppressWarnings("javadoc")
public interface NilValue extends Serializable
{
    
    
    /**
     * Gets the reason property
     */
    public String getReason();
    
    
    /**
     * Sets the reason property
     */
    public void setReason(String reason);
    
    
    /**
     * Gets the inline value
     */
    public String getValue();
    
    
    /**
     * Sets the inline value
     */
    public void setValue(String value);
}
