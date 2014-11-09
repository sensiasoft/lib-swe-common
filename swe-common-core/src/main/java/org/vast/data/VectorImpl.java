package org.vast.data;

import org.vast.cdm.common.DataComponent;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.OgcPropertyList;
import net.opengis.swe.v20.AbstractSimpleComponent;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.Vector;


/**
 * POJO class for XML type VectorType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class VectorImpl extends AbstractRecordImpl<AbstractSimpleComponent> implements Vector
{
    static final long serialVersionUID = 1L;
    protected String referenceFrame = "";
    protected String localFrame;
    
    
    public VectorImpl()
    {
        super();
    }
    
    
    public VectorImpl(int size)
    {
        super(size);
    }
    
    
    @Override
    public VectorImpl copy()
    {
        VectorImpl newObj = new VectorImpl(fieldList.size());
        super.copyTo(newObj);
        fieldList.copyTo(newObj.fieldList);
        newObj.referenceFrame = referenceFrame;
        newObj.localFrame = localFrame;
        return newObj;
    }
    
    
    @Override
    public void addComponent(String name, DataComponent component)
    {
        addCoordinate(new OgcPropertyImpl<AbstractSimpleComponent>(name, (AbstractSimpleComponent)component));       
    }
    
    
    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append(indent);
        text.append("Vector (");
        text.append(referenceFrame);
        text.append(")\n");

        for (int i=0; i<fieldList.size(); i++)
        {
            text.append(indent + INDENT);
            text.append(fieldList.getProperty(i).getName());
            text.append(":\n");
            text.append(getComponent(i).toString(indent + INDENT + INDENT));
            text.append('\n');
        }

        return text.toString();
    }
    
    
    /* ************************************ */
    /*  Auto-generated Getters and Setters  */    
    /* ************************************ */  
    
    /**
     * Gets the list of coordinate properties
     */
    @Override
    public OgcPropertyList<AbstractSimpleComponent> getCoordinateList()
    {
        return fieldList;
    }
    
    
    /**
     * Returns number of coordinate properties
     */
    @Override
    public int getNumCoordinates()
    {
        return fieldList.size();
    }
    
    
    /**
     * Gets the coordinate property with the given name
     */
    @Override
    public AbstractSimpleComponent getCoordinate(String name)
    {
        return (AbstractSimpleComponent)fieldList.get(name);
    }
    
    
    /**
     * Adds a new coordinateAsCount property
     */
    @Override
    public void addCoordinateAsCount(String name, Count coordinate)
    {
        addCoordinate(new OgcPropertyImpl<AbstractSimpleComponent>(name, coordinate));
    }
    
    
    /**
     * Adds a new coordinateAsQuantity property
     */
    @Override
    public void addCoordinateAsQuantity(String name, Quantity coordinate)
    {
        addCoordinate(new OgcPropertyImpl<AbstractSimpleComponent>(name, coordinate));
    }
    
    
    /**
     * Adds a new coordinateAsTime property
     */
    @Override
    public void addCoordinateAsTime(String name, Time coordinate)
    {
        addCoordinate(new OgcPropertyImpl<AbstractSimpleComponent>(name, coordinate));
    }
    
    
    /**
     * Adds a new field property
     */
    private void addCoordinate(OgcProperty<AbstractSimpleComponent> prop)
    {
        fieldList.add(prop);
        
        if (prop.hasValue())
        {
            ((AbstractDataComponentImpl)prop.getValue()).parent = this;
            ((AbstractDataComponentImpl)prop.getValue()).name = name;            
        }
    }
    
    
    /**
     * Gets the referenceFrame property
     */
    @Override
    public String getReferenceFrame()
    {
        return referenceFrame;
    }
    
    
    /**
     * Checks if referenceFrame is set
     */
    @Override
    public boolean isSetReferenceFrame()
    {
        return (referenceFrame != null);
    }
    
    
    /**
     * Sets the referenceFrame property
     */
    @Override
    public void setReferenceFrame(String referenceFrame)
    {
        this.referenceFrame = referenceFrame;
    }
    
    
    /**
     * Gets the localFrame property
     */
    @Override
    public String getLocalFrame()
    {
        return localFrame;
    }
    
    
    /**
     * Checks if localFrame is set
     */
    @Override
    public boolean isSetLocalFrame()
    {
        return (localFrame != null);
    }
    
    
    /**
     * Sets the localFrame property
     */
    @Override
    public void setLocalFrame(String localFrame)
    {
        this.localFrame = localFrame;
    }
}
