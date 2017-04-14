/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32;

import java.io.Serializable;

/**
 * POJO class for XML type CodeType(@http://www.opengis.net/gml/3.2).
 *
 */
@SuppressWarnings("javadoc")
public interface CodeWithAuthority extends Serializable
{
    
    /**
     * Gets the codeSpace property
     */
    public String getCodeSpace();
    
    
    /**
     * Checks if codeSpace is set
     */
    public boolean isSetCodeSpace();
    
    
    /**
     * Sets the codeSpace property
     */
    public void setCodeSpace(String codeSpace);
    
    
    /**
     * Gets the inline value
     */
    public String getValue();
    
    
    /**
     * Sets the inline value
     */
    public void setValue(String value);
}
