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
 * Quaternion
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Quaternion data structure and related math routines
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 29, 2005
 * @version 1.0
 */
public class Quaternion
{
    protected double q1, q2, q3, q4;


    public Quaternion()
    {
    }


    public Quaternion(double q1, double q2, double q3, double q4)
    {
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.q4 = q4;
    }


    /**
     * Create the quaternion containing same elements as vector and q4=0
     * @param v Vector
     */
    public Quaternion(Vector3D v)
    {
        this.q1 = v.coordinates[0];
        this.q2 = v.coordinates[1];
        this.q3 = v.coordinates[2];
        this.q4 = 0.0;
    }


    /**
     * Create a quaternion with this axis and angle
     * @param v Vector Axis vector (a copy of it is normalized 1st)
     * @param angle double angle in radians
     */
    public Quaternion(Vector3D v, double angle)
    {
        Vector3D axis = (Vector3D)v.copy();
        axis.normalize();
        this.q1 = axis.coordinates[0] * Math.sin(angle/2);
        this.q2 = axis.coordinates[1] * Math.sin(angle/2);
        this.q3 = axis.coordinates[2] * Math.sin(angle/2);
        this.q4 = Math.cos(angle/2);
    }


    public void setValues(double q1, double q2, double q3, double q4)
    {
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.q4 = q4;
    }


    public Quaternion copy()
    {
        return (new Quaternion(q1, q2, q3, q4));
    }


    public double [] getArray()
    {
        return new double [] {q1, q2, q3, q4};
    }


    public double getQ1()
    {
        return q1;
    }


    public double getQ2()
    {
        return q2;
    }


    public double getQ3()
    {
        return q3;
    }


    public double getQ4()
    {
        return q4;
    }


    public Vector3D getVector()
    {
        return new Vector3D(q1, q2, q3);
    }


    public void add(Quaternion b)
    {
        this.q1 += b.q1;
        this.q2 += b.q2;
        this.q3 += b.q3;
        this.q4 += b.q4;
    }


    public void sub(Quaternion b)
    {
        this.q1 -= b.q1;
        this.q2 -= b.q2;
        this.q3 -= b.q3;
        this.q4 -= b.q4;
    }


    public void normalize()
    {

    }


    public void transpose()
    {
        this.q1 = -this.q1;
        this.q2 = -this.q2;
        this.q3 = -this.q3;
    }


    public Quaternion mul(Quaternion b)
    {
        double x =  q1 * b.q4 + q2 * b.q3 - q3 * b.q2 + q4 * b.q1;
        double y = -q1 * b.q3 + q2 * b.q4 + q3 * b.q1 + q4 * b.q2;
        double z =  q1 * b.q2 - q2 * b.q1 + q3 * b.q4 + q4 * b.q3;
        double w = -q1 * b.q1 - q2 * b.q2 - q3 * b.q3 + q4 * b.q4;

        return new Quaternion(x, y, z, w);
    }


    public String toString()
    {
        return "Quaternion: " + q1 + ", " + q2 + ", " + q3 + ", " + q4;
    }
}
