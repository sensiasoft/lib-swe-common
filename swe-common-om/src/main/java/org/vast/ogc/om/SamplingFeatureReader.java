/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.om;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.vast.ogc.gml.GMLStaxBindings;
import org.vast.ogc.gml.GenericFeature;
import org.vast.ogc.gml.IFeatureStaxBindings;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.gml.v32.AbstractFeature;
import net.opengis.gml.v32.AbstractGeometry;


/**
 * <p>
 * Custom reader for O&M sampling features
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Apr 21, 2017
 */
public class SamplingFeatureReader extends GMLStaxBindings implements IFeatureStaxBindings
{
    
    @Override
    public Collection<QName> getSupportedFeatureTypes()
    {
        return Arrays.asList(SamplingFeature.SF_SAMPLING_FEATURE);
    }


    @Override
    public AbstractFeature readFeature(XMLStreamReader reader, QName qName) throws XMLStreamException
    {
        SamplingFeature<AbstractGeometry> newFeature = new SamplingFeature<>();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readAbstractFeatureTypeAttributes(attrMap, newFeature);
        
        reader.nextTag();
        this.readAbstractFeatureTypeElements(reader, newFeature);
        
        // type
        if (checkElementName(reader, "type"))
        {
            OgcProperty<Serializable> prop = new OgcPropertyImpl<>();
            readPropertyAttributes(reader, prop);
            newFeature.setType(prop.getHref());
            reader.nextTag();
            reader.nextTag();
        }
        
        // sampledFeature
        if (checkElementName(reader, "sampledFeature"))
        {
            OgcProperty<Serializable> prop = new OgcPropertyImpl<>();
            readPropertyAttributes(reader, prop);
            newFeature.setSampledFeatureUID(prop.getHref());
            reader.nextTag();
            reader.nextTag();
        }
        
        // lineage (skip for now)
        while (checkElementName(reader, "lineage"))
            skipElementAndAllChildren(reader);
                    
        // 0..* related observation (skip for now)
        while (checkElementName(reader, "relatedObservation"))
            skipElementAndAllChildren(reader);
                    
        // 0..* relatedSamplingFeature (skip for now)
        while (checkElementName(reader, "relatedSamplingFeature"))
            skipElementAndAllChildren(reader);
        
        // 0..* parameter (skip for now)
        while (checkElementName(reader, "parameter"))
            skipElementAndAllChildren(reader);
        
        // hostedProcedure
        if (checkElementName(reader, "hostedProcedure"))
        {
            OgcProperty<Serializable> prop = new OgcPropertyImpl<>();
            readPropertyAttributes(reader, prop);
            newFeature.setHostedProcedureUID(prop.getHref());
            reader.nextTag();
            reader.nextTag();
        }
        
        // shape        
        if (!checkElementName(reader, "shape"))
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        reader.nextTag();
        AbstractGeometry geom = readAbstractGeometry(reader);
        newFeature.setShape(geom);
        
        return newFeature.getAsSpecializedType();
    }


    @Override
    public void writeFeature(XMLStreamWriter writer, AbstractFeature bean) throws XMLStreamException
    {
        writeGenericFeature(writer, (GenericFeature)bean);
    }

}
