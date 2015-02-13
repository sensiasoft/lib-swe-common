/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
 Alexandre Robin <alex.robin@sensiasoftware.com>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.vast.math.Vector3d;


/**
 * <p>
 * Generic implementation of a GML feature.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Feb 20, 2007
 * */
public class FeatureImpl implements IGeoFeature
{
    public final static QName LOCATION = new QName("location");
    protected QName qname;
    protected String id;
    protected String identifier;
    protected Object metadata;
    protected String type;
    protected String description;
    protected List<QName> names;
    protected Map<QName, Object> properties;


    public FeatureImpl(QName qname)
    {
        this.qname = qname;
        names = new ArrayList<QName>();
        properties = new LinkedHashMap<QName, Object>();
    }
    
    
    public QName getQName()
    {
        return qname;
    }
    
    
    public String getLocalId()
    {
        return this.id;
    }
    
    
    public void setLocalId(String id)
    {
        this.id = id;
    }
    
    
    public String getIdentifier()
    {
        return this.identifier;
    }
    
    
    public void setIdentifier(String uid)
    {
        this.identifier = uid;
    }
    
    
    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }
    
    
    public String getName()
    {
        QName name = names.get(0);
        if (name != null)
            return name.getLocalPart();
        return null;
    }
    
    
    public void setName(String name)
    {
        QName qname = new QName(name);
        names.add(qname);
    }
    
    
    public void addName(QName qname)
    {
        names.add(qname);
    }


    public List<QName> getNames()
    {
        return names;
    }


    public void setNames(List<QName> names)
    {
        this.names = names;
    }
    
    
    public Vector3d getLocation()
    {
        return (Vector3d)properties.get(LOCATION);
    }
    
    
    public void setLocation(Vector3d point)
    {
        properties.put(LOCATION, point);
    }


    public Map<QName, Object> getProperties()
    {
        return properties;
    }
  
    
    public void setProperty(QName qname, Object prop)
    {
        if (prop != null)
            properties.put(qname, prop);
    }
    
    
    public Object getProperty(QName qname)
    {
        return properties.get(qname);
    }
    
    
    public void setProperty(String name, Object prop)
    {
        if (prop != null)
            properties.put(new QName(name), prop);
    }
    
    
    public Object getProperty(String name)
    {
        return properties.get(new QName(name));
    }


    public String getType()
    {
        return type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public Object getMetadata()
    {
        return metadata;
    }


    public void setMetadata(Object metadata)
    {
        this.metadata = metadata;
    }
}
