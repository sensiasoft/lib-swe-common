/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis;

import java.io.IOException;


public class OgcPropertyImpl<ValueType> implements OgcProperty<ValueType>
{
    protected ValueType value;
    protected String name;
    protected String title;
    protected String href;
    protected String role;
    protected String arcRole;
    protected String nilReason;
    protected HrefResolver hrefResolver;
    
    
    public OgcPropertyImpl()
    {
    }
    
    
    public OgcPropertyImpl(ValueType value)
    {
        this.value = value;
    }
    
    
    public OgcPropertyImpl(String name, ValueType value)
    {
        this(value);
        this.name = name;
    }
    
    
    public OgcPropertyImpl<ValueType> copy()
    {
        OgcPropertyImpl<ValueType> newProp = new OgcPropertyImpl<ValueType>();
        copyTo(newProp);
        return newProp;
    }
    
    
    protected void copyTo(OgcPropertyImpl<ValueType> other)
    {        
        other.name = this.name;
        other.href = this.href;
        other.role = this.role;
        other.arcRole = this.arcRole;
        other.nilReason = this.nilReason;
        other.hrefResolver = this.hrefResolver;
        
        if (this.value != null && this.value instanceof HasCopy)
            other.value = (ValueType)((HasCopy)this.value).copy();
        else
            other.value = this.value;
    }
    
    
    @Override
    public String getName()
    {
        return name;
    }


    @Override
    public void setName(String name)
    {
        this.name = name;
    }


    @Override
    public String getTitle()
    {
        return title;
    }


    @Override
    public void setTitle(String title)
    {
        this.title = title;        
    }


    @Override
    public String getHref()
    {
        return href;
    }


    @Override
    public void setHref(String href)
    {
        //if (value != null)
        //    throw new IllegalStateException("Attempting to set xlink:href on property that already has a value");
        
        this.href = href;
    }
    
    
    @Override
    public boolean hasHref()
    {
        return href != null;
    }


    @Override
    public String getRole()
    {
        return role;
    }


    @Override
    public void setRole(String role)
    {
        this.role = role;
    }
    
    
    @Override
    public String getArcRole()
    {
        return arcRole;
    }
    
    
    @Override
    public void setArcRole(String role)
    {
        this.arcRole = role;
    }
    
    
    @Override
    public String getNilReason()
    {
        return nilReason;
    }


    @Override
    public void setNilReason(String nilReason)
    {
        this.nilReason = nilReason;
    }


    @Override
    public ValueType getValue()
    {
        try
        {
            if (!hasValue() && hasHref())
                resolveHref();
            return value;
        }
        catch (IOException e)
        {
            // output message?
            return null;
        }
    }
    
    
    @Override
    public boolean hasValue()
    {
        return value != null;
    }
    
    
    @Override
    public void setValue(ValueType value)
    {
        //if (href != null)
        //    throw new IllegalStateException("Attempting to set value of property that already has an xlink:href");
        
        this.value = value;
    }
    
    
    @Override
    public void setHrefResolver(HrefResolver hrefResolver)
    {
        this.hrefResolver = hrefResolver;
    }


    @Override
    public boolean resolveHref() throws IOException
    {
        if (hrefResolver == null || !hasHref() || hasValue())
            return false;
        
        // call resolver and discard it so it can be GC
        boolean ret = hrefResolver.resolveHref(this);
        hrefResolver = null;
        
        return ret;
    }
    
    
    @Override
    public ValueType getTarget()
    {
        return getValue();
    }
}
