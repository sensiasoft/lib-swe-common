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

import java.util.Objects;
import net.opengis.gml.v32.TimeInstant;
import net.opengis.gml.v32.TimePosition;


/**
 * POJO class for XML type TimeInstantType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public class TimeInstantImpl extends AbstractTimeGeometricPrimitiveImpl implements TimeInstant
{
    private static final long serialVersionUID = -4839211806785133760L;
    protected TimePosition timePosition;
    
    
    public TimeInstantImpl()
    {
    }
    
    
    /**
     * Gets the timePosition property
     */
    @Override
    public TimePosition getTimePosition()
    {
        return timePosition;
    }
    
    
    /**
     * Sets the timePosition property
     */
    @Override
    public void setTimePosition(TimePosition timePosition)
    {
        this.timePosition = timePosition;
    }


    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof TimeInstant &&
               Objects.equals(timePosition, ((TimeInstant)obj).getTimePosition());
    }
    
    
    @Override
    public int hashCode()
    {
        return Objects.hash(frame,
                            timePosition);
    }
}
