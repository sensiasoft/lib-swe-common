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

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyList;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.Code;
import net.opengis.gml.v32.CodeWithAuthority;
import net.opengis.gml.v32.Envelope;
import net.opengis.gml.v32.Reference;
import org.vast.ogc.xlink.CachedReference;
import org.vast.util.URIResolver;


/**
 * <p>
 * Wrapper class for use when an IFeature object is or can be included by reference.
 * This enables fetching and instantiating the target object lazily.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Sep 28, 2012
 * */
public class FeatureRef extends CachedReference<GenericFeature> implements GenericFeature
{
        
    
    public FeatureRef()
    { 
    }
    
    
    public FeatureRef(String href)
    { 
        this.href = href;
    }



    @Override
    protected GenericFeature fetchTarget(String href)
    {
        try
        {
            URIResolver resolver = new URIResolver(new URI(href));
            InputStream is = resolver.openStream();            
            return new GMLUtils(GMLUtils.V3_2).readFeature(is);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }



    public OgcPropertyList<Object> getMetaDataPropertyList()
    {
        return getTarget().getMetaDataPropertyList();
    }



    public Envelope getBoundedBy()
    {
        return getTarget().getBoundedBy();
    }



    public boolean isSetBoundedBy()
    {
        return getTarget().isSetBoundedBy();
    }



    public String getDescription()
    {
        return getTarget().getDescription();
    }



    public void setBoundedByAsEnvelope(Envelope boundedBy)
    {
        getTarget().setBoundedByAsEnvelope(boundedBy);
    }



    public boolean isSetDescription()
    {
        return getTarget().isSetDescription();
    }



    public AbstractGeometry getLocation()
    {
        return getTarget().getLocation();
    }



    public void setDescription(String description)
    {
        getTarget().setDescription(description);
    }



    public QName getQName()
    {
        return getTarget().getQName();
    }



    public OgcProperty<AbstractGeometry> getLocationProperty()
    {
        return getTarget().getLocationProperty();
    }



    public Reference getDescriptionReference()
    {
        return getTarget().getDescriptionReference();
    }



    public String getType()
    {
        return getTarget().getType();
    }



    public void setType(String type)
    {
        getTarget().setType(type);
    }



    public Map<QName, Object> getProperties()
    {
        return getTarget().getProperties();
    }



    public boolean isSetDescriptionReference()
    {
        return getTarget().isSetDescriptionReference();
    }



    public Object getProperty(String name)
    {
        return getTarget().getProperty(name);
    }



    public boolean isSetLocation()
    {
        return getTarget().isSetLocation();
    }



    public void setProperty(String name, Object value)
    {
        getTarget().setProperty(name, value);
    }



    public Object getProperty(QName qname)
    {
        return getTarget().getProperty(qname);
    }



    public void setLocation(AbstractGeometry location)
    {
        getTarget().setLocation(location);
    }



    public void setDescriptionReference(Reference descriptionReference)
    {
        getTarget().setDescriptionReference(descriptionReference);
    }



    public void setProperty(QName qname, Object value)
    {
        getTarget().setProperty(qname, value);
    }



    public CodeWithAuthority getIdentifier()
    {
        return getTarget().getIdentifier();
    }



    public String getUniqueIdentifier()
    {
        return getTarget().getUniqueIdentifier();
    }



    public boolean isSetIdentifier()
    {
        return getTarget().isSetIdentifier();
    }



    public void setIdentifier(CodeWithAuthority identifier)
    {
        getTarget().setIdentifier(identifier);
    }



    public void setUniqueIdentifier(String identifier)
    {
        getTarget().setUniqueIdentifier(identifier);
    }



    public List<Code> getNameList()
    {
        return getTarget().getNameList();
    }



    public int getNumNames()
    {
        return getTarget().getNumNames();
    }



    public void addName(Code name)
    {
        getTarget().addName(name);
    }



    public void setName(String name)
    {
        getTarget().setName(name);
    }



    public String getName()
    {
        return getTarget().getName();
    }



    public String getId()
    {
        return getTarget().getId();
    }



    public void setId(String id)
    {
        getTarget().setId(id);
    }
}
