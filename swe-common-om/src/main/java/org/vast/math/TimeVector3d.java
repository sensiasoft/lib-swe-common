/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.

 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.math;

/**
 * <p>
 * Extension of Vector3d to add a time coordinate
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since 28 ao�t 08
 * */
public class TimeVector3d extends Vector3d
{
	private static final long serialVersionUID = 1538029865321858257L;
	public double t;
	
	
	public TimeVector3d()
	{
		super();
	}


	public TimeVector3d(double[] xyztCoords)
	{
		this.x = xyztCoords[0];
		this.y = xyztCoords[1];
		this.z = xyztCoords[2];
		this.t = xyztCoords[3];
	}


	public TimeVector3d(double x, double y, double z, double t)
	{
		super(x, y, z);
		this.t = t;
	}


	public TimeVector3d(Vector3d v)
	{
		super(v);
	}
	
	
	public TimeVector3d(TimeVector3d v)
	{
		super(v);
		this.t = v.t;
	}
	
	
	public TimeVector3d copy()
	{
		return new TimeVector3d(this);
	}
}
