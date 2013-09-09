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

package org.vast.ogc.gml;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.vast.ogc.xlink.CachedReference;
import org.vast.util.URIResolver;


/**
 * <p><b>Title:</b>
 * FeatureRef
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Wrapper class for use when an IFeature object is or can be included by reference.
 * This enables fetching and instantiating the target object lazily.
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @date Sep 28, 2012
 * @version 1.0
 */
public class FeatureRef extends CachedReference<IFeature> implements IFeature
{
    
    
    public FeatureRef()
    { 
    }
    
    
    public FeatureRef(String href)
    {
        setHref(href);
    }
    
    
    @Override
    public QName getQName()
    {
        return getTarget().getQName();
    }
    
    
    @Override
    public String getLocalId()
    {
        return getTarget().getLocalId();
    }


    @Override
    public void setLocalId(String id)
    {
        getTarget().setLocalId(id);        
    }


    @Override
    public String getIdentifier()
    {
        return getTarget().getIdentifier();
    }


    @Override
    public void setIdentifier(String uid)
    {
        getTarget().setIdentifier(uid);        
    }
    
    
    @Override
    public String getDescription()
    {
        return getTarget().getDescription();
    }


    @Override
    public void setDescription(String desc)
    {
        getTarget().setDescription(desc);
    }


    @Override
    public String getName()
    {
        return getTarget().getName();
    }


    @Override
    public void setName(String name)
    {
        getTarget().setName(name);
    }


    @Override
    public List<QName> getNames()
    {
        return getTarget().getNames();
    }


    @Override
    public void addName(QName name)
    {
        getTarget().addName(name);
    }
    
    
    public Map<QName, Object> getProperties()
    {
        return getTarget().getProperties();
    }


    @Override
    public Object getProperty(String name)
    {
        return getTarget().getProperty(name);
    }


    @Override
    public void setProperty(String name, Object value)
    {
        getTarget().setProperty(name, value);
    }


    @Override
    public Object getProperty(QName qname)
    {
        return getTarget().getProperty(qname);
    }
    

    @Override
    public void setProperty(QName qname, Object value)
    {
        getTarget().setProperty(qname, value);
    }


    @Override
    protected IFeature fetchTarget(String href)
    {
        try
        {
            URIResolver resolver = new URIResolver(new URI(href));
            InputStream is = resolver.openStream();            
            return GMLUtils.readFeature(is);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
