package net.opengis.gml.v32.impl;

import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.gml.v32.AbstractFeature;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.Code;
import net.opengis.gml.v32.Envelope;
import net.opengis.gml.v32.StringOrRef;


/**
 * POJO class for XML type AbstractFeatureType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public abstract class AbstractFeatureImpl extends AbstractGMLImpl implements AbstractFeature
{
    static final long serialVersionUID = 1L;
    protected Envelope boundedBy;
    protected OgcProperty<Object> location;
    
    
    public AbstractFeatureImpl()
    {
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
    public Object getLocation()
    {
        return location.getValue();
    }
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the location property
     */
    @Override
    public OgcProperty<Object> getLocationProperty()
    {
        if (location == null)
            location = new OgcPropertyImpl<Object>();
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
    public void setLocationAsAbstractGeometry(AbstractGeometry location)
    {
        if (this.location == null)
            this.location = new OgcPropertyImpl<Object>();
        this.location.setValue(location);
    }
    
    
    /**
     * Sets the locationAsLocationKeyWord property
     */
    @Override
    public void setLocationAsLocationKeyWord(Code location)
    {
        if (this.location == null)
            this.location = new OgcPropertyImpl<Object>();
        this.location.setValue(location);
    }
    
    
    /**
     * Sets the locationAsLocationString property
     */
    @Override
    public void setLocationAsLocationString(StringOrRef location)
    {
        if (this.location == null)
            this.location = new OgcPropertyImpl<Object>();
        this.location.setValue(location);
    }
}
