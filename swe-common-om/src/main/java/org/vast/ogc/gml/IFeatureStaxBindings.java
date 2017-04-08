/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.util.Collection;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import net.opengis.gml.v32.AbstractFeature;


/**
 * <p>
 * Interface for StAX bindings of specific feature types 
 * </p>
 *
 * @author Alex Robin
 * @since May 30, 2015
 */
public interface IFeatureStaxBindings
{
    public Collection<QName> getSupportedFeatureTypes();
    
    public AbstractFeature readFeature(XMLStreamReader reader, QName qName) throws XMLStreamException;
    
    public void writeFeature(XMLStreamWriter writer, AbstractFeature bean) throws XMLStreamException;
}
