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
 Alexandre Robin <robin@nsstc.uah.edu>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.vast.math.Vector3d;
import org.vast.xml.QName;


/**
 * <p><b>Title:</b>
 * Feature
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Generic implementation of a GML feature.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Feb 20, 2007
 * @version 1.0
 */
public class Feature
{
    public final static String LOCATION = "location";
    protected Object metadata;
    protected String type;
    protected String description;
    protected List<QName> names;
    protected Hashtable<String, Object> properties;


    public Feature()
    {
        names = new ArrayList<QName>();
        properties = new Hashtable<String, Object>();
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
            return name.getLocalName();
        return null;
    }
    
    
    public void setName(String name)
    {
        QName qname = new QName();
        qname.setLocalName(name);
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


    public Hashtable<String, Object> getProperties()
    {
        return properties;
    }


    public void setProperties(Hashtable<String, Object> properties)
    {
        this.properties = properties;
    }
    
    
    public void setProperty(String name, Object prop)
    {
        if (prop != null)
            properties.put(name, prop);
    }
    
    
    public Object getProperty(String name)
    {
        return properties.get(name);
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
