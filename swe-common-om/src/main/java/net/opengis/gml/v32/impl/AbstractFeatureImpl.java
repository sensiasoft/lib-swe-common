/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32.impl;

import javax.xml.namespace.QName;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.gml.v32.AbstractFeature;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.Envelope;


/**
 * POJO class for XML type AbstractFeatureType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public abstract class AbstractFeatureImpl extends AbstractGMLImpl implements AbstractFeature
{
    private static final long serialVersionUID = -7915849537365579906L;
    protected QName qName;
    protected Envelope boundedBy;
    protected OgcProperty<AbstractGeometry> location;
    
    
    public AbstractFeatureImpl()
    {
    }
    
    
    @Override
    public QName getQName()
    {
        return qName;
    }
    

    /**
     * Gets the boundedBy property
     */
    @Override
    public Envelope getBoundedBy()
    {
        return boundedBy;
    }
    
    
    /**
     * Checks if boundedBy is set
     */
    @Override
    public boolean isSetBoundedBy()
    {
        return (boundedBy != null);
    }
    
    
    /**
     * Sets the boundedByAsEnvelope property
     */
    @Override
    public void setBoundedByAsEnvelope(Envelope boundedBy)
    {
        this.boundedBy = boundedBy;
    }
    
    
    /**
     * Gets the location property
     */
    @Override
    public AbstractGeometry getLocation()
    {
        if (location == null)
            return null;
        
        return location.getValue();
    }
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the location property
     */
    @Override
    public OgcProperty<AbstractGeometry> getLocationProperty()
    {
        if (location == null)
            location = new OgcPropertyImpl<AbstractGeometry>();
        return location;
    }
    
    
    /**
     * Checks if location is set
     */
    @Override
    public boolean isSetLocation()
    {
        return (location != null && location.getValue() != null);
    }
    
    
    /**
     * Sets the locationAsAbstractGeometry property
     */
    @Override
    public void setLocation(AbstractGeometry location)
    {
        if (this.location == null)
            this.location = new OgcPropertyImpl<>();
        this.location.setValue(location);
    }


    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof AbstractFeature))
           return false;
        
        // features are equal if their UIDs are equal
        String thisUid = getUniqueIdentifier();
        String otherUid = ((AbstractFeature)obj).getUniqueIdentifier();
        if (thisUid == null || otherUid == null)
            return false;
        
        return thisUid.equals(otherUid);
    }


    @Override
    public int hashCode()
    {
        String uid = getUniqueIdentifier();
        if (uid == null)
            return 0;
        
        return uid.hashCode();
    }
}
