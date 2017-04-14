/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import net.opengis.swe.v20.NilValue;


/**
 * POJO class for XML type NilValue(@http://www.opengis.net/swe/2.0).
 *
 */
public class NilValueImpl implements NilValue
{
    private static final long serialVersionUID = -7281813971940522932L;
    protected String reason = "";
    protected String value;
    
    
    public NilValueImpl()
    {
    }
    
    
    public NilValueImpl(String reason, String value)
    {
        this.reason = reason;
        this.value = value;
    }
    
    
    /**
     * Gets the reason property
     */
    @Override
    public String getReason()
    {
        return reason;
    }
    
    
    /**
     * Sets the reason property
     */
    @Override
    public void setReason(String reason)
    {
        this.reason = reason;
    }
    
    
    /**
     * Gets the inline value
     */
    @Override
    public String getValue()
    {
        return value;
    }
    
    
    /**
     * Sets the inline value
     */
    @Override
    public void setValue(String value)
    {
        this.value = value;
    }
}
