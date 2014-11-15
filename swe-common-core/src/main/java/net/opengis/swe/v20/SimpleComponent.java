package net.opengis.swe.v20;

import net.opengis.OgcProperty;
import net.opengis.OgcPropertyList;


/**
 * POJO class for XML type AbstractSimpleComponentType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface SimpleComponent extends DataComponent
{
    
    
    /**
     * Gets the list of quality properties
     */
    public OgcPropertyList<SimpleComponent> getQualityList();
    
    
    /**
     * Returns number of quality properties
     */
    public int getNumQualitys();
    
    
    /**
     * Adds a new qualityAsQuantity property
     */
    public void addQuality(Quantity quality);
    
    
    /**
     * Adds a new qualityAsQuantityRange property
     */
    public void addQuality(QuantityRange quality);
    
    
    /**
     * Adds a new qualityAsCategory property
     */
    public void addQuality(Category quality);
    
    
    /**
     * Adds a new qualityAsText property
     */
    public void addQuality(Text quality);
    
    
    /**
     * Gets the nilValues property
     */
    public NilValues getNilValues();
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the nilValues property
     */
    public OgcProperty<NilValues> getNilValuesProperty();
    
    
    /**
     * Checks if nilValues is set
     */
    public boolean isSetNilValues();
    
    
    /**
     * Sets the nilValues property
     */
    public void setNilValues(NilValues nilValues);
    
    
    /**
     * Gets the referenceFrame property
     */
    public String getReferenceFrame();
    
    
    /**
     * Checks if referenceFrame is set
     */
    public boolean isSetReferenceFrame();
    
    
    /**
     * Sets the referenceFrame property
     */
    public void setReferenceFrame(String referenceFrame);
    
    
    /**
     * Gets the axisID property
     */
    public String getAxisID();
    
    
    /**
     * Checks if axisID is set
     */
    public boolean isSetAxisID();
    
    
    /**
     * Sets the axisID property
     */
    public void setAxisID(String axisID);
}
