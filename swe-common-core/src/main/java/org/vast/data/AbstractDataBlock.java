/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import java.io.Serializable;
import net.opengis.swe.v20.DataBlock;


@SuppressWarnings("serial")
public abstract class AbstractDataBlock implements DataBlock, Serializable
{
	protected int atomCount;
	protected int startIndex = 0;
	
	
    /**
     * Shallow copy of datablock structure
     * The underlying object is shared
     */
	public abstract AbstractDataBlock copy();
    
    
    /**
     * Regenerate an identical datablock of same
     * size with a new underlying object
     * @return new data block
     */
    public abstract AbstractDataBlock renew();
    
    
    /**
     * Full copy of datablock structure and values
     * A new underlying object is created with the same values
     */
    public abstract AbstractDataBlock clone();
    
    
    /**
     * Allow direct access to underlying object carrying
     * the data (usually a primitive array or composite)
     * @return underlying object
     */
    public abstract Object getUnderlyingObject();
    
    
    /**
     * Allows to set the underlying object directly
     * @param obj
     */
    public abstract void setUnderlyingObject(Object obj);
    
	
    /**
     * Returns number of scalar values in this DataBlock
     */
	public int getAtomCount()
	{
		return atomCount;
	}
	

	public String toString()
	{
		return new String(getDataType() + "[" + getAtomCount() + "]");
	}
}
