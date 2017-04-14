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
import net.opengis.gml.v32.AbstractTimePrimitive;


/**
 * POJO class for XML type AbstractTimePrimitiveType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public abstract class AbstractTimePrimitiveImpl extends AbstractGMLImpl implements AbstractTimePrimitive
{
    private static final long serialVersionUID = -2509273429785476571L;
    protected OgcPropertyList<AbstractTimePrimitive> relatedTimeList = new OgcPropertyList<AbstractTimePrimitive>();
    
    
    public AbstractTimePrimitiveImpl()
    {
    }
    
    
    /**
     * Gets the list of relatedTime properties
     */
    @Override
    public OgcPropertyList<AbstractTimePrimitive> getRelatedTimeList()
    {
        return relatedTimeList;
    }
    
    
    /**
     * Returns number of relatedTime properties
     */
    @Override
    public int getNumRelatedTimes()
    {
        if (relatedTimeList == null)
            return 0;
        return relatedTimeList.size();
    }
    
    
    /**
     * Adds a new relatedTime property
     */
    @Override
    public void addRelatedTime(AbstractTimePrimitive relatedTime)
    {
        this.relatedTimeList.add(relatedTime);
    }
}
