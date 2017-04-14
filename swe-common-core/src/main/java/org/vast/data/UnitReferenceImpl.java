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

import org.vast.unit.Unit;
import org.vast.unit.UnitParserUCUM;
import org.vast.unit.UnitParserURI;
import net.opengis.swe.v20.UnitReference;


/**
 * POJO class for XML type UnitReference(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class UnitReferenceImpl extends net.opengis.OgcPropertyImpl<Unit> implements UnitReference
{
    static final long serialVersionUID = 1L;
    protected String code;
    
    
    public UnitReferenceImpl()
    {        
    }
    
    
    public UnitReferenceImpl(String codeOrUri)
    {
        if (codeOrUri.startsWith("http") || codeOrUri.startsWith("urn"))
            this.href = codeOrUri;
        else
            this.code = codeOrUri;
    }
    
    
    @Override
    public UnitReferenceImpl copy()
    {
        UnitReferenceImpl newObj = new UnitReferenceImpl();
        super.copyTo(newObj);
        newObj.code = code;
        return newObj;
    }
    
    
    /**
     * Gets the code property
     */
    @Override
    public String getCode()
    {
        return code;
    }
    
    
    /**
     * Checks if code is set
     */
    @Override
    public boolean isSetCode()
    {
        return (code != null);
    }
    
    
    /**
     * Sets the code property
     */
    @Override
    public void setCode(String code)
    {
        this.code = code;
    }


    @Override
    public Unit getValue()
    {
        if (value == null)
        {
            if (isSetCode() && !code.equalsIgnoreCase(ANY_UNIT_CODE))
                value = new UnitParserUCUM().getUnit(code);
            else if (hasHref() && !href.equalsIgnoreCase(ANY_UNIT_URI))
                value = new UnitParserURI().getUnit(href);
        }
        
        return value;
    }

}
