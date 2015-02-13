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
 * Vector specialization for a 3 dimensions vector [x,y,z].
 * Extends from javax.vecmath.Vector3d and adds additional optimized
 * math routines for 3D transformations.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Nov 29, 2005
 * */
public class Vector3d extends javax.vecmath.Vector3d
{
	private static final long serialVersionUID = -2905506681710125806L;
	protected String crs;


	public Vector3d()
	{
		super();
	}


	public Vector3d(double[] coordinates)
	{
		super(coordinates);
	}


	public Vector3d(double x, double y, double z)
	{
		super(x, y, z);
	}


	public Vector3d(Vector3d v)
	{
		super(v);
		this.crs = v.crs;
	}


	public Vector3d copy()
	{
		return new Vector3d(this);
	}


	/**
	 * Rotates vector around X axis by given angle
	 * @param angleRadians double
	 */
	public void rotateX(double angleRadians)
	{
		double c, s;
		c = Math.cos(angleRadians);
		s = Math.sin(angleRadians);
		Vector3d v = this.copy();
		y = c * v.y + -s * v.z;
		z = s * v.y + c * v.z;
	}


	/**
	 * Rotates vector around Y axis by given angle
	 * @param angleRadians double
	 */
	public void rotateY(double angleRadians)
	{
		double c, s;
		c = Math.cos(angleRadians);
		s = Math.sin(angleRadians);
		Vector3d v = this.copy();
		x = c * v.x + s * v.z;
		z = -s * v.x + c * v.z;
	}


	/**
	 * Rotates vector around Z axis by given angle
	 * @param angleRadians double
	 */
	public void rotateZ(double angleRadians)
	{
		double c, s;
		c = Math.cos(angleRadians);
		s = Math.sin(angleRadians);
		Vector3d v = this.copy();
		x = c * v.x - s * v.y;
		y = s * v.x + c * v.y;
	}


	/**
	 * Transforms vector using homogeneous 4x4 matrix
	 * @param m Matrix4x4
	 */
	public void transform(Matrix4d m)
	{
		double x = this.x;
		double y = this.y;
		double z = this.z;

		this.x = m.getElement(0, 0) * x +
		         m.getElement(0, 1) * y +
		         m.getElement(0, 2) * z +
		         m.getElement(0, 3);

		this.y = m.getElement(1, 0) * x +
		         m.getElement(1, 1) * y +
		         m.getElement(1, 2) * z +
		         m.getElement(1, 3);

		this.z = m.getElement(2, 0) * x +
		         m.getElement(2, 1) * y +
		         m.getElement(2, 2) * z +
		         m.getElement(2, 3);
	}


	/**
	 * Rotates vector using 3x3 rotation matrix
	 * @param m Matrix4x4
	 */
	public void rotate(Matrix3d m)
	{
		double x = this.x;
		double y = this.y;
		double z = this.z;

		this.x = m.getElement(0, 0) * x +
		         m.getElement(0, 1) * y +
		         m.getElement(0, 2) * z;

		this.y = m.getElement(1, 0) * x +
		         m.getElement(1, 1) * y +
		         m.getElement(1, 2) * z;

		this.z = m.getElement(2, 0) * x +
		         m.getElement(2, 1) * y +
		         m.getElement(2, 2) * z;
	}


	/**
	 * Rotates vector utilizing the 3x3 portion of a 4x4 matrix
	 * @param m Matrix4x4
	 */
	public void rotate(Matrix4d m)
	{
		double x = this.x;
		double y = this.y;
		double z = this.z;

		this.x = m.getElement(0, 0) * x +
				 m.getElement(0, 1) * y +
				 m.getElement(0, 2) * z;

		this.y = m.getElement(1, 0) * x +
		         m.getElement(1, 1) * y +
		         m.getElement(1, 2) * z;

		this.z = m.getElement(2, 0) * x +
		         m.getElement(2, 1) * y +
		         m.getElement(2, 2) * z;
	}


	/**
	 * Rotates vector using given quaternion as axis and angle
	 * @param q Quaternion
	 */
	public void rotate(Quat4d q)
	{
		if (q.isNull())
			return;

		q = q.copy();
		Quat4d v = new Quat4d(this);
		Quat4d qT = q.copy();
		qT.conjugate();

		v.mul(qT);
		q.mul(v);

		x = q.x;
		y = q.y;
		z = q.z;
	}


	// subtract vector 2 from vector1 and return resulting vector
	public final static Vector3d subtract(Vector3d v1, Vector3d v2)
	{
		Vector3d v3 = new Vector3d(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
		return v3;
	}


	// subtract vector 2 from this vector1
	public void subtract(Vector3d v2)
	{
		this.x = this.x - v2.x;
		this.y = this.y - v2.y;
		this.z = this.z - v2.z;
	}


	// add vector 2 to vector1 and return resulting vector
	public final static Vector3d add(Vector3d v1, Vector3d v2)
	{
		Vector3d v3 = new Vector3d(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
		return v3;
	}


	// add vector 2 to this vector
	public void add(Vector3d v2)
	{
		this.x = this.x + v2.x;
		this.y = this.y + v2.y;
		this.z = this.z + v2.z;
	}


	public String getCrs()
	{
		return crs;
	}


	public void setCrs(String crs)
	{
		this.crs = crs;
	}
}
