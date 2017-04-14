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
    private static final long serialVersionUID = 205617168330167795L;
    protected OgcPropertyList<AbstractFeature> featureMemberList = new OgcPropertyList<AbstractFeature>();
        
    
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
}
