/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2014 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import net.opengis.gml.v32.AbstractFeature;
import net.opengis.gml.v32.bind.XMLStreamBindings;
import net.opengis.gml.v32.impl.GMLFactory;


/**
 * <p>
 * Helper wrapping the auto-generated GML StAX bindings
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Sep 25, 2014
 */
public class GMLStaxBindings extends XMLStreamBindings
{
    public final static String NS_PREFIX_GML = "gml";
    public final static String NS_PREFIX_XLINK = "xlink";
    
    protected Map<QName, IFeatureStaxBindings> featureTypesBindings;
    
    
    public GMLStaxBindings()
    {
        this(false);
    }
    
    
    public GMLStaxBindings(boolean useJTS)
    {
        super(new GMLFactory(useJTS));
        
        featureTypesBindings = new HashMap<QName, IFeatureStaxBindings>();
        
        nsContext.registerNamespace(NS_PREFIX_GML, net.opengis.gml.v32.bind.XMLStreamBindings.NS_URI);
        nsContext.registerNamespace(NS_PREFIX_XLINK, net.opengis.swe.v20.bind.XMLStreamBindings.XLINK_NS_URI);
    }
    
    
    public GMLFactory getFactory()
    {
        return (GMLFactory)factory;
    }
    
    
    public void registerFeatureBindings(IFeatureStaxBindings featureBindings)
    {
        for (QName fType: featureBindings.getSupportedFeatureTypes())
            featureTypesBindings.put(fType, featureBindings);
    }
    
    
    public GenericFeature readGenericFeature(XMLStreamReader reader) throws XMLStreamException
    {
        QName featureType = reader.getName();
        GenericFeature newFeature = new GenericFeatureImpl(featureType);
        this.readAbstractFeatureType(reader, newFeature);
                
        // also read all other properties in a generic manner
        while (reader.getEventType() != XMLStreamConstants.END_ELEMENT)
        {
            reader.nextTag();
            QName propName = reader.getName();
            
            if (reader.hasText())
            {
                String text = reader.getElementText();
                Object value = null;
                
                if (text != null)
                {
                    try
                    {
                        value = Integer.parseInt(text);
                    }
                    catch (NumberFormatException e)
                    {
                        try
                        {
                            value = Double.parseDouble(text);
                        }
                        catch (NumberFormatException e1)
                        {
                            try
                            {
                                if (text.equalsIgnoreCase("true") || text.equalsIgnoreCase("false"))
                                    value = Boolean.parseBoolean(text);
                                else
                                    this.getDateTimeFromString(text);
                            }
                            catch (Exception e2)
                            {
                                value = text;
                            }
                        }
                    }
                    
                    newFeature.setProperty(propName, value);
                }
            }
            else
                this.skipElementAndAllChildren(reader);
            
            reader.nextTag();
        }
        
        return newFeature;
    }
    
    
    public void writeGenericFeature(XMLStreamWriter writer, GenericFeature bean) throws XMLStreamException
    {
        QName featureType = bean.getQName();
        String nsUri = featureType.getNamespaceURI();
        
        // namespace        
        if (writer.getPrefix(nsUri) == null)
            writer.writeNamespace("ns1", nsUri);
        
        // element name
        writer.writeStartElement(nsUri, featureType.getLocalPart());
        
        // common attributes and elements from AbstractFeature
        this.writeAbstractFeatureTypeAttributes(writer, bean);
        this.writeAbstractFeatureTypeElements(writer, bean);
        
        // write all other properties
        for (Entry<QName, Object> prop: bean.getProperties().entrySet())
        {
            // prop name
            QName propName = prop.getKey();
            writer.writeStartElement(propName.getNamespaceURI(), propName.getLocalPart());
            
            // prop value
            Object val = prop.getValue();            
            writer.writeCharacters(val.toString());                
                
            writer.writeEndElement();
        }
        
        writer.writeEndElement();
    }
    
    
    @Override
    public AbstractFeature readAbstractFeature(XMLStreamReader reader) throws XMLStreamException
    {
        QName featureType = reader.getName();
        IFeatureStaxBindings customBindings = featureTypesBindings.get(featureType);
        
        if (customBindings != null)
            return customBindings.readFeature(reader, featureType);
        else if (featureType.getNamespaceURI().equals(NS_URI))
            return super.readAbstractFeature(reader);
        else
            return readGenericFeature(reader);
    }


    @Override
    public void writeAbstractFeature(XMLStreamWriter writer, AbstractFeature bean) throws XMLStreamException
    {
        QName featureType = bean.getQName();
        IFeatureStaxBindings customBindings = featureTypesBindings.get(featureType);
        
        if (customBindings != null)
            customBindings.writeFeature(writer, bean);
        else if (bean instanceof GenericFeature)
            this.writeGenericFeature(writer, (GenericFeature)bean);
        else
            super.writeAbstractFeature(writer, bean);
    }
    
}
