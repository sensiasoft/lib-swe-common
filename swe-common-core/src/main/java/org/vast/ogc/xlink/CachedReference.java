/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Initial Developer of the Original Code is SENSIA SOFTWARE LLC.
 Portions created by the Initial Developer are Copyright (C) 2012
 the Initial Developer. All Rights Reserved.

 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> for more
 information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.xlink;

import java.io.IOException;
import org.vast.util.ResolveException;


/**
 * <p>
 * Implementation of Xlink Reference that keeps a cached version of the target.
 * Object is cached on the first call to getTarget().
 * Reloading the target object can be enforced by calling refresh().
 * @param <TargetType> Type of the link target object
 * </p>
 *
 * @author Alex Robin
 * @since Sep 28, 2012
 *
 */
public class CachedReference<TargetType> implements IXlinkReference<TargetType>
{
    protected String href;
    protected String role;
    protected String arcRole;
    protected TargetType value;
    protected IReferenceResolver<TargetType> resolver;


    public CachedReference()
    {        
    }
    
    
    public CachedReference(String href)
    {
        setHref(href);
    }
    
    
    public CachedReference(IReferenceResolver<TargetType> resolver)
    {
        setResolver(resolver);
    }
    
    
    public CachedReference(String href, IReferenceResolver<TargetType> resolver)
    {
        setHref(href);
        setResolver(resolver);
    }
    
    
    @Override
    public String getHref()
    {
        return href;
    }


    @Override
    public void setHref(String href)
    {
        this.href = href;
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
    public void setArcRole(String arcRole)
    {
        this.arcRole = arcRole;
    }


    @Override
    public TargetType getTarget()
    {
        try
        {
            if (value == null)
                refresh();
        }
        catch (IOException e)
        {
            throw new ResolveException("Error while fetching linked content", e);
        }
        
        return value;
    }
    
    
    public void setResolver(IReferenceResolver<TargetType> resolver)
    {
        this.resolver = resolver;
    }
    
    
    public void refresh() throws IOException
    {
        if (resolver == null)
            throw new IllegalStateException("No resolver has been set for " + href);
        
        value = fetchTarget(href);
        
        // also try to fetch using role URI as unique ID of the object
        if (value == null && role != null)
            value = fetchTarget(role);       
    }
    
    
    protected TargetType fetchTarget(String href) throws IOException
    {
        if (href != null)
            return resolver.fetchTarget(href);
        else
            return null;
    }
}
