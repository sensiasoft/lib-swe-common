/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32.impl;

import net.opengis.gml.v32.CodeWithAuthority;


/**
 * POJO class for XML type CodeType(@http://www.opengis.net/gml/3.2).
 *
 */
public class CodeWithAuthorityImpl implements CodeWithAuthority
{
    private static final long serialVersionUID = -758584730988314036L;
    protected String codeSpace;
    protected String value;
    
    
    public CodeWithAuthorityImpl()
    {
    }
    
    
    public CodeWithAuthorityImpl(String value)
    {
        this.value = value;
    }
    
    
    public CodeWithAuthorityImpl(String codeSpace, String value)
    {
        this.codeSpace = codeSpace;
        this.value = value;
    }
    
    
    /**
     * Gets the codeSpace property
     */
    @Override
    public String getCodeSpace()
    {
        return codeSpace;
    }
    
    
    /**
     * Checks if codeSpace is set
     */
    @Override
    public boolean isSetCodeSpace()
    {
        return (codeSpace != null);
    }
    
    
    /**
     * Sets the codeSpace property
     */
    @Override
    public void setCodeSpace(String codeSpace)
    {
        this.codeSpace = codeSpace;
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
