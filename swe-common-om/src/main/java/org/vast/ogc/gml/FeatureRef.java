/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.

 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or 
 Mike Botts <mike.botts@botts-inc.net for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyList;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.CodeWithAuthority;
import net.opengis.gml.v32.Envelope;
import net.opengis.gml.v32.Reference;
import org.vast.ogc.xlink.CachedReference;
import org.vast.ogc.xlink.IReferenceResolver;
import org.vast.util.ResolveException;
import org.vast.util.URIResolver;


/**
 * <p>
 * Wrapper class for use when an IFeature object is or can be included by reference.
 * This enables fetching and instantiating the target object lazily.
 * </p>
 *
 * @author Alex Robin
 * @since Sep 28, 2012
 * */
public class FeatureRef extends CachedReference<GenericFeature> implements GenericFeature
{
    private static final long serialVersionUID = 8523158284557229913L;


    public FeatureRef()
    {
        this.resolver = new IReferenceResolver<GenericFeature>()
        {            
            @Override
            public GenericFeature fetchTarget(String uri) throws IOException
            {
                try
                {
                    URIResolver resolver = new URIResolver(new URI(href));
                    InputStream is = resolver.openStream();            
                    return new GMLUtils(GMLUtils.V3_2).readFeature(is);
                }
                catch (URISyntaxException e)
                {
                    throw new ResolveException("Bad URI", e);
                }
            }
        };
    }
    
    
    public FeatureRef(String href)
    { 
        this.href = href;
    }


    @Override
    public OgcPropertyList<Serializable> getMetaDataPropertyList()
    {
        return getTarget().getMetaDataPropertyList();
    }


    @Override
    public Envelope getBoundedBy()
    {
        return getTarget().getBoundedBy();
    }


    @Override
    public boolean isSetBoundedBy()
    {
        return getTarget().isSetBoundedBy();
    }


    @Override
    public String getDescription()
    {
        return getTarget().getDescription();
    }


    @Override
    public void setBoundedByAsEnvelope(Envelope boundedBy)
    {
        getTarget().setBoundedByAsEnvelope(boundedBy);
    }


    @Override
    public boolean isSetDescription()
    {
        return getTarget().isSetDescription();
    }


    @Override
    public AbstractGeometry getLocation()
    {
        return getTarget().getLocation();
    }


    @Override
    public void setDescription(String description)
    {
        getTarget().setDescription(description);
    }


    @Override
    public QName getQName()
    {
        return getTarget().getQName();
    }


    @Override
    public OgcProperty<AbstractGeometry> getLocationProperty()
    {
        return getTarget().getLocationProperty();
    }


    @Override
    public Reference getDescriptionReference()
    {
        return getTarget().getDescriptionReference();
    }


    @Override
    public Map<QName, Object> getProperties()
    {
        return getTarget().getProperties();
    }


    @Override
    public boolean isSetDescriptionReference()
    {
        return getTarget().isSetDescriptionReference();
    }


    @Override
    public Object getProperty(String name)
    {
        return getTarget().getProperty(name);
    }


    @Override
    public boolean isSetLocation()
    {
        return getTarget().isSetLocation();
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
    public void setLocation(AbstractGeometry location)
    {
        getTarget().setLocation(location);
    }


    @Override
    public void setDescriptionReference(Reference descriptionReference)
    {
        getTarget().setDescriptionReference(descriptionReference);
    }


    @Override
    public void setProperty(QName qname, Object value)
    {
        getTarget().setProperty(qname, value);
    }


    @Override
    public CodeWithAuthority getIdentifier()
    {
        return getTarget().getIdentifier();
    }


    @Override
    public String getUniqueIdentifier()
    {
        return getTarget().getUniqueIdentifier();
    }


    @Override
    public boolean isSetIdentifier()
    {
        return getTarget().isSetIdentifier();
    }


    @Override
    public void setIdentifier(CodeWithAuthority identifier)
    {
        getTarget().setIdentifier(identifier);
    }


    @Override
    public void setUniqueIdentifier(String identifier)
    {
        getTarget().setUniqueIdentifier(identifier);
    }


    @Override
    public List<CodeWithAuthority> getNameList()
    {
        return getTarget().getNameList();
    }


    @Override
    public int getNumNames()
    {
        return getTarget().getNumNames();
    }


    @Override
    public void addName(CodeWithAuthority name)
    {
        getTarget().addName(name);
    }


    @Override
    public void setName(String name)
    {
        getTarget().setName(name);
    }


    @Override
    public String getName()
    {
        return getTarget().getName();
    }


    @Override
    public String getId()
    {
        return getTarget().getId();
    }


    @Override
    public void setId(String id)
    {
        getTarget().setId(id);
    }
}
