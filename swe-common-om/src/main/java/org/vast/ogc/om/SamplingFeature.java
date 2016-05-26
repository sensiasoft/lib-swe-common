/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.om;

import javax.xml.namespace.QName;
import net.opengis.gml.v32.AbstractGeometry;
import org.vast.ogc.gml.FeatureRef;
import org.vast.ogc.gml.GenericFeatureImpl;
import org.vast.ogc.xlink.CachedReference;


public abstract class SamplingFeature<GeomType extends AbstractGeometry> extends GenericFeatureImpl
{
    public static final String SAMS_NS_PREFIX = "sams";
    public static final String SAMS_NS_URI = "http://www.opengis.net/samplingSpatial/2.0";
    public static final String SF_NS_PREFIX = "sf";
    public static final String SF_NS_URI = "http://www.opengis.net/sampling/2.0";
    
    public static final QName PROP_SAMPLED_FEATURE = new QName(SF_NS_URI, "sampledFeature", SF_NS_PREFIX);
    public static final QName PROP_HOSTED_PROCEDURE = new QName(SAMS_NS_URI, "hostedProcedure", SAMS_NS_PREFIX);
    public static final QName PROP_SHAPE = new QName(SAMS_NS_URI, "shape", SAMS_NS_PREFIX);
    
    
    public SamplingFeature(String type)
    {
        super(new QName(SAMS_NS_URI, "SF_SpatialSamplingFeature", SAMS_NS_PREFIX));
        
        // set sampling feature type
        QName qname = new QName(SF_NS_URI, "type", SF_NS_PREFIX);
        setProperty(qname, new CachedReference<Object>(type));
    }
    
    
    public void setSampledFeatureUID(String featureUID)
    {
        setProperty(PROP_SAMPLED_FEATURE, new FeatureRef(featureUID));
    }
    
    
    public String getSampledFeatureUID()
    {
        FeatureRef ref = (FeatureRef)getProperty(PROP_SAMPLED_FEATURE);
        if (ref == null)
            return null;
        return ref.getHref();
    }
    
    
    public void setHostedProcedureUID(String processUID)
    {
        setProperty(PROP_HOSTED_PROCEDURE, new FeatureRef(processUID));
    }
    
    
    public String getHostedProcedureUID()
    {
        FeatureRef ref = (FeatureRef)getProperty(PROP_HOSTED_PROCEDURE);
        if (ref == null)
            return null;
        return ref.getHref();
    }
    
    
    public void setShape(GeomType geom)
    {
        setProperty(PROP_SHAPE, geom);
    }
    
    
    public GeomType getShape()
    {
        return (GeomType)getProperty(PROP_SHAPE);
    }
    

    @Override
    public AbstractGeometry getLocation()
    {
        AbstractGeometry location = super.getLocation();
        if (location != null)
            return location;
        else
            return getShape();
    }

}