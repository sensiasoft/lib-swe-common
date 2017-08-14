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
import org.vast.ogc.xlink.IXlinkReference;
import net.opengis.gml.v32.AbstractFeature;
import net.opengis.gml.v32.AbstractGML;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.AbstractTimePrimitive;
import net.opengis.gml.v32.bind.XMLStreamBindings;
import net.opengis.gml.v32.impl.GMLFactory;


/**
 * <p>
 * Helper wrapping the auto-generated GML StAX bindings
 * </p>
 *
 * @author Alex Robin
 * @since Sep 25, 2014
 */
public class GMLStaxBindings extends XMLStreamBindings
{
    public final static String NS_PREFIX_GML = "gml";
    public final static String NS_PREFIX_XLINK = "xlink";

    protected int geomIdCounter = 1;
    protected int timeIdCounter = 1;
    protected int featureIdCounter = 1;
    protected StringBuilder sb = new StringBuilder();
    protected Map<QName, IFeatureStaxBindings> featureTypesBindings;
    
    
    public GMLStaxBindings()
    {
        this(false);
    }
    
    
    public GMLStaxBindings(boolean useJTS)
    {
        this(new GMLFactory(useJTS));
    }
    
    
    public GMLStaxBindings(net.opengis.gml.v32.Factory fac)
    {
        super(fac);
        featureTypesBindings = new HashMap<QName, IFeatureStaxBindings>();        
        nsContext.registerNamespace(NS_PREFIX_GML, net.opengis.gml.v32.bind.XMLStreamBindings.NS_URI);
        nsContext.registerNamespace(NS_PREFIX_XLINK, net.opengis.swe.v20.bind.XMLStreamBindings.XLINK_NS_URI);
    }
    
    
    public GMLFactory getFactory()
    {
        return (GMLFactory)factory;
    }
    
    
    public void registerFeatureBindings(IFeatureStaxBindings... featureBindings)
    {
        for (IFeatureStaxBindings binding: featureBindings)
        for (QName fType: binding.getSupportedFeatureTypes())
            featureTypesBindings.put(fType, binding);
    }
    
    
    public GenericFeature readGenericFeature(XMLStreamReader reader) throws XMLStreamException
    {
        QName featureType = reader.getName();
        GenericFeature newFeature = new GenericFeatureImpl(featureType);
                
        Map<String, String> attrMap = collectAttributes(reader);
        this.readAbstractFeatureTypeAttributes(attrMap, newFeature);
        
        reader.nextTag();
        this.readAbstractFeatureTypeElements(reader, newFeature);
                
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
                                value = text.trim();
                            }
                        }
                    }
                    
                    newFeature.setProperty(propName, value);
                }
            }
            else
            {
                skipElementAndAllChildren(reader);
            }
        }        
        
        return newFeature;
    }
    
    
    public void writeGenericFeature(XMLStreamWriter writer, GenericFeature bean) throws XMLStreamException
    {
        QName featureType = bean.getQName();
        String newPrefix = ensurePrefix(writer, featureType);
        
        // element name
        writer.writeStartElement(featureType.getNamespaceURI(), featureType.getLocalPart());
        if (newPrefix != null)
            writer.writeNamespace(newPrefix, featureType.getNamespaceURI());
        
        // write property namespaces if needed
        for (Entry<QName, Object> prop: bean.getProperties().entrySet())
            ensureNamespaceDecl(writer, prop.getKey());
        
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
            
            if (val instanceof IXlinkReference<?>)
            {
                String href = ((IXlinkReference<?>) val).getHref();
                writer.writeAttribute(XLINK_NS_URI, "href", href);
            }
            else if (val instanceof AbstractGeometry)
            {
                writeAbstractGeometry(writer, (AbstractGeometry)val);
            }
            else
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


    @Override
    public void writeAbstractGMLTypeAttributes(XMLStreamWriter writer, AbstractGML bean) throws XMLStreamException
    {        
        // automatically generate gml:id if not set
        String gmlID = bean.getId();        
        if (gmlID == null || gmlID.length() == 0)
        {
            sb.setLength(0);
            
            if (bean instanceof AbstractGeometry)
            {
                sb.append('G');
                sb.append(geomIdCounter++);
            }
            else if (bean instanceof AbstractTimePrimitive)
            {
                sb.append('T');
                sb.append(timeIdCounter++);
            }
            else if (bean instanceof AbstractFeature)
            {
                sb.append('F');
                sb.append(featureIdCounter++);
            }
            
            gmlID = sb.toString();
        }
        
        writer.writeAttribute(NS_URI, "id", gmlID);
    }
    
    
    public final void resetGeomIdCounter(int geomIdCounter)
    {
        this.geomIdCounter = geomIdCounter;
    }


    public final void resetTimeIdCounter(int timeIdCounter)
    {
        this.timeIdCounter = timeIdCounter;
    }
}
