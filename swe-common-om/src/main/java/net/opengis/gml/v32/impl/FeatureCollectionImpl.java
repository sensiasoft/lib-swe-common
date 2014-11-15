package net.opengis.gml.v32.impl;

import net.opengis.OgcPropertyList;
import net.opengis.gml.v32.AbstractFeature;
import net.opengis.gml.v32.FeatureCollection;


/**
 * POJO class for XML type AbstractFeatureCollectionType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public class FeatureCollectionImpl extends AbstractFeatureImpl implements FeatureCollection
{
    static final long serialVersionUID = 1L;
    protected OgcPropertyList<AbstractFeature> featureMemberList = new OgcPropertyList<AbstractFeature>();
    protected AbstractFeature featureMembers;
    
    
    public FeatureCollectionImpl()
    {
    }
    
    
    /**
     * Gets the list of featureMember properties
     */
    @Override
    public OgcPropertyList<AbstractFeature> getFeatureMemberList()
    {
        return featureMemberList;
    }
    
    
    /**
     * Returns number of featureMember properties
     */
    @Override
    public int getNumFeatureMembers()
    {
        if (featureMemberList == null)
            return 0;
        return featureMemberList.size();
    }
    
    
    /**
     * Adds a new featureMember property
     */
    @Override
    public void addFeatureMember(AbstractFeature featureMember)
    {
        this.featureMemberList.add(featureMember);
    }
    
    
    /**
     * Gets the featureMembers property
     */
    @Override
    public AbstractFeature getFeatureMembers()
    {
        return featureMembers;
    }
    
    
    /**
     * Checks if featureMembers is set
     */
    @Override
    public boolean isSetFeatureMembers()
    {
        return (featureMembers != null);
    }
    
    
    /**
     * Sets the featureMembers property
     */
    @Override
    public void setFeatureMembers(AbstractFeature featureMembers)
    {
        this.featureMembers = featureMembers;
    }
}
