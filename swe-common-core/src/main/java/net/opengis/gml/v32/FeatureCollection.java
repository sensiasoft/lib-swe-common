package net.opengis.gml.v32;

import net.opengis.OgcPropertyList;


/**
 * POJO class for XML type AbstractFeatureCollectionType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface FeatureCollection extends AbstractFeature
{
    
    
    /**
     * Gets the list of featureMember properties
     */
    public OgcPropertyList<AbstractFeature> getFeatureMemberList();
    
    
    /**
     * Returns number of featureMember properties
     */
    public int getNumFeatureMembers();
    
    
    /**
     * Adds a new featureMember property
     */
    public void addFeatureMember(AbstractFeature featureMember);
    
    
    /**
     * Gets the featureMembers property
     */
    public AbstractFeature getFeatureMembers();
    
    
    /**
     * Checks if featureMembers is set
     */
    public boolean isSetFeatureMembers();
    
    
    /**
     * Sets the featureMembers property
     */
    public void setFeatureMembers(AbstractFeature featureMembers);
}
