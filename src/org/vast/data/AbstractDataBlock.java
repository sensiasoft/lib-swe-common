/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import org.vast.cdm.common.DataBlock;


public abstract class AbstractDataBlock implements DataBlock
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
     * @return
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
     * @return
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
