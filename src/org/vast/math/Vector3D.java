/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.math;


/**
 * <p><b>Title:</b><br/>
 * Vector3D
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Vector specialization for a 3 dimensions vector [x,y,z].
 * Contains additional optimized math routines for 3D processing.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 29, 2005
 * @version 1.0
 */
public class Vector3D extends Vector
{
    public Vector3D()
    {
        super(3);
    }


    public Vector3D(double x, double y, double z)
    {
        super(3);
        coordinates[0] = x;
        coordinates[1] = y;
        coordinates[2] = z;
    }


    public Vector3D(double[] coordinates)
    {
        super(3);
        this.coordinates = coordinates;
    }


    public void setCoordinates(double x, double y, double z)
    {
        coordinates[0] = x;
        coordinates[1] = y;
        coordinates[2] = z;
    }


    public double getX()
    {
        return coordinates[0];
    }


    public double getY()
    {
        return coordinates[1];
    }


    public double getZ()
    {
        return coordinates[2];
    }


    public Vector3D copy()
    {
        return new Vector3D(coordinates[0], coordinates[1], coordinates[2]);
    }


    /**
     * Rotates vector using given quaternion as axis and angle
     * @param q Quaternion
     */
    public void rotate(Quaternion q)
    {
        Quaternion v = new Quaternion(this);
        Quaternion qT = q.copy();
        qT.transpose();
        Quaternion qv = q.mul(v.mul(qT));
        coordinates[0] = qv.q1;
        coordinates[1] = qv.q2;
        coordinates[2] = qv.q3;
    }


    /**
     * Rotates vector around X axis by given angle
     * @param angleRadians double
     */
    public void rotateX(double angleRadians)
    {
        double c, s;
        c = Math.cos( -angleRadians);
        s = Math.sin( -angleRadians);
        Vector3D v = (Vector3D)this.copy();
        coordinates[1] = c * v.getY() + s * v.getZ();
        coordinates[2] = -s * v.getY() + c * v.getZ();
    }


    /**
     * Rotates vector around Y axis by given angle
     * @param angleRadians double
     */
    public void rotateY(double angleRadians)
    {
        double c, s;
        c = Math.cos( -angleRadians);
        s = Math.sin( -angleRadians);
        Vector3D v = (Vector3D)this.copy();
        coordinates[0] = c * v.getX() - s * v.getZ();
        coordinates[2] = s * v.getX() + c * v.getZ();
    }


    /**
     * Rotates vector around Z axis by given angle
     * @param angleRadians double
     */
    public void rotateZ(double angleRadians)
    {
        double c, s;
        c = Math.cos( -angleRadians);
        s = Math.sin( -angleRadians);
        Vector3D v = (Vector3D)this.copy();
        coordinates[0] = c * v.getX() + s * v.getY();
        coordinates[1] = -s * v.getX() + c * v.getY();
    }


    /**
     * Transforms vector using homogeneous 4x4 matrix
     * @param m Matrix4x4
     */
    public void transform(Matrix4x4 m)
    {
        double x = coordinates[0];
        double y = coordinates[1];
        double z = coordinates[2];

        coordinates[0] = m.getElement(0, 0) * x + m.getElement(1, 0) * y
            + m.getElement(2, 0) * z + m.getElement(3, 0);

        coordinates[1] = m.getElement(0, 1) * x + m.getElement(1, 1) * y
            + m.getElement(2, 1) * z + m.getElement(3, 1);

        coordinates[2] = m.getElement(0, 2) * x + m.getElement(1, 2) * y
            + m.getElement(2, 2) * z + m.getElement(3, 2);
    }



    /**
     * Rotates vector using 3x3 rotation matrix
     * @param m Matrix4x4
     */
    public void rotate(Matrix3x3 m)
    {
        double x = coordinates[0];
        double y = coordinates[1];
        double z = coordinates[2];

        coordinates[0] = m.getElement(0, 0) * x + m.getElement(1, 0) * y
            + m.getElement(2, 0) * z;
        coordinates[1] = m.getElement(0, 1) * x + m.getElement(1, 1) * y
            + m.getElement(2, 1) * z;
        coordinates[2] = m.getElement(0, 2) * x + m.getElement(1, 2) * y
            + m.getElement(2, 2) * z;
    }


    /**
     * Rotates vector utilizing the 3x3 portion of a 4x4 matrix
     * @param m Matrix4x4
     */
    public void rotate(Matrix4x4 m)
    {
        double x = coordinates[0];
        double y = coordinates[1];
        double z = coordinates[2];

        coordinates[0] = m.getElement(0, 0) * x + m.getElement(1, 0) * y
            + m.getElement(2, 0) * z;
        coordinates[1] = m.getElement(0, 1) * x + m.getElement(1, 1) * y
            + m.getElement(2, 1) * z;
        coordinates[2] = m.getElement(0, 2) * x + m.getElement(1, 2) * y
            + m.getElement(2, 2) * z;
    }


    /**
     * Computes cross product of this vector with b
     * @param b Vector
     * @return Vector3D
     */
    public Vector3D cross(Vector b)
    {
        double x = coordinates[0];
        double y = coordinates[1];
        double z = coordinates[2];

        double newX = y * b.coordinates[2] - z * b.coordinates[1];
        double newY = z * b.coordinates[0] - x * b.coordinates[2];
        double newZ = x * b.coordinates[1] - y * b.coordinates[0];

        return new Vector3D(newX, newY, newZ);
    }


    /**
     * Helper method to add 2 3D vectors
     * @param a Vector3D
     * @param b Vector3D
     * @return Vector3D
     */
    public static Vector3D addVectors(Vector3D a, Vector3D b)
    {
        return (Vector3D)Vector.addVectors(a,b);
    }


    /**
     * Helper method to subtract 2 3D vectors
     * @param a Vector3D
     * @param b Vector3D
     * @return Vector3D
     */
    public static Vector3D subtractVectors(Vector3D a, Vector3D b)
    {
        return (Vector3D)Vector.subtractVectors(a,b);
    }
}
