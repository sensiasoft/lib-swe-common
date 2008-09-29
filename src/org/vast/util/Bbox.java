/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
 Tony Cook <tcook@nsstc.uah.edu>
 Alex Robin <robin@nsstc.uah.edu>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.util;


/**
 * <p>Title:
 * Bbox
 * </p>
 *
 * <p>Description:
 * Simple structure for OGC-style bbox info.
 * </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 * @author Tony Cook, Alex Robin
 * @since Aug 16, 2005
 * @version 1.0
 */
public class Bbox extends SpatialExtent
{
   
	public Bbox()
	{		
	}
	
	
	public Bbox(double minX, double minY, double maxX, double maxY)
    {
    	setMinX(minX);
        setMinY(minY);
        setMaxX(maxX);
        setMaxY(maxY);
    }
	
	
	public Bbox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
    {
    	setMinX(minX);
        setMinY(minY);
        setMinZ(minZ);
        setMaxX(maxX);
        setMaxY(maxY);
        setMaxZ(maxZ);
    }
	
	
	@Override
    public Bbox copy()
    {
        Bbox bbox = new Bbox();
        bbox.setMinX(this.minX);
        bbox.setMinY(this.minY);
        bbox.setMinZ(this.minZ);
        bbox.setMaxX(this.maxX);
        bbox.setMaxY(this.maxY);
        bbox.setMaxZ(this.maxZ);
        return bbox;
    }
}
