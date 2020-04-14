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

 Please Contact Alexandre Robin or 
 Mike Botts <mike.botts@botts-inc.net for more information.
 
 Contributor(s): 
    Alexandre Robin
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import javax.xml.namespace.QName;
import net.opengis.gml.v32.AbstractGeometry;
import org.vast.ogc.xlink.CachedReference;
import org.vast.ogc.xlink.IReferenceResolver;
import org.vast.util.TimeExtent;
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
public class FeatureRef extends CachedReference<IFeature> implements IGeoFeature, ITemporalFeature
{
    
    public FeatureRef()
    {
        this.resolver = new IReferenceResolver<IFeature>()
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
    public String getUniqueIdentifier()
    {
        return getTarget().getUniqueIdentifier();
    }


    @Override
    public String getName()
    {
        return getTarget().getName();
    }


    @Override
    public String getDescription()
    {
        return getTarget().getDescription();
    }


    @Override
    public AbstractGeometry getGeometry()
    {
        IFeature f = getTarget();
        if (f instanceof IGeoFeature)
            return ((IGeoFeature)f).getGeometry();
        else
            return null;
    }


    @Override
    public TimeExtent getValidTime()
    {
        IFeature f = getTarget();
        if (f instanceof ITemporalFeature)
            return ((ITemporalFeature)f).getValidTime();
        else
            return null;
    }


    @Override
    public Map<QName, Object> getProperties()
    {
        return getTarget().getProperties();
    }
}
