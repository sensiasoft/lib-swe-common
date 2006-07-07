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
 * Matrix3x3
 * </p>
 *
 * <p><b>Description:</b><br/>
 * 3x3 Matrix specialization
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Mike Botts, Alexandre Robin
 * @date Nov 29, 2005
 * @version 1.0
 */
public class Matrix3x3 extends Matrix
{
    // default matrix is identity
    public Matrix3x3()
    {
        a = new double[3][3];
        rows = 3;
        columns = 3;
        for (int i = 0; i < 3; i++)a[i][i] = 1.0;
    }


    // constructor to make all elements a constant value
    public Matrix3x3(double value)
    {
        a = new double[3][3];
        rows = 3;
        columns = 3;
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                a[i][j] = value;
            }
        }
    }


    public String toString()
    {
        String str = new String();

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
                str += ( (new Double(a[i][j])).toString()) + "  ";
            str += "\n";
        }

        return str;
    }


    public Matrix3x3(double a0, double a1, double a2, double a3, double a4,
                     double a5, double a6, double a7, double a8)
    {

        this();
        a[0][0] = a0;
        a[0][1] = a1;
        a[0][2] = a2;
        a[1][0] = a3;
        a[1][1] = a4;
        a[1][2] = a5;
        a[2][0] = a6;
        a[2][1] = a7;
        a[2][2] = a8;
    }


    // expects a double[9] array, with a[0][1] = array[1]
    public Matrix3x3(double[] array)
    {

        this();
        a[0][0] = array[0];
        a[0][1] = array[1];
        a[0][2] = array[2];
        a[1][0] = array[3];
        a[1][1] = array[4];
        a[1][2] = array[5];
        a[2][0] = array[6];
        a[2][1] = array[7];
        a[2][2] = array[8];
    }


    // expects a double[3][3] array, with double[row][column] ordering
    public Matrix3x3(double[][] array)
    {
        this();
        a = array; // NOTE: do I need a copy instead of assignment ??
    }


    public Matrix3x3(Vector3D xAxis, Vector3D yAxis, Vector3D zAxis)
    {

        this();
        a[0][0] = xAxis.getX();
        a[0][1] = yAxis.getX();
        a[0][2] = zAxis.getX();
        a[1][0] = xAxis.getY();
        a[1][1] = yAxis.getY();
        a[1][2] = zAxis.getY();
        a[2][0] = xAxis.getZ();
        a[2][1] = yAxis.getZ();
        a[2][2] = zAxis.getZ();
    }


    // turn matrix into identity matrix
    public void identity()
    {
        a[0][0] = 1;
        a[0][1] = 0;
        a[0][2] = 0;
        a[1][0] = 0;
        a[1][1] = 1;
        a[1][2] = 0;
        a[2][0] = 0;
        a[2][1] = 0;
        a[2][2] = 1;
    }


    /* the following concatenation routines utilize "Efficient Post-Concatenation
        of Transformation Matrices" by Joseph Cychosz in Graphics Gems I
     */

    // rotate about x-axis (right-handed rotation)
    public void rotateX(double angleRadians)
    {
        if (angleRadians == 0)return;
        double c, s, t;
        int i;
        c = Math.cos( -angleRadians);
        s = Math.sin( -angleRadians);
        for (i = 0; i < 3; i++)
        {
            t = a[i][1];
            a[i][1] = t * c - a[i][2] * s;
            a[i][2] = t * s + a[i][2] * c;
        }
    }


    // rotate about y-axis (right-handed rotation)
    public void rotateY(double angleRadians)
    {
        if (angleRadians == 0)return;
        double c, s, t;
        int i;
        c = Math.cos( -angleRadians);
        s = Math.sin( -angleRadians);
        for (i = 0; i < 3; i++)
        {
            t = a[i][0];
            a[i][0] = t * c + a[i][2] * s;
            a[i][2] = a[i][2] * c - t * s;
        }
    }


    // rotate about z-axis (right-handed rotation)
    public void rotateZ(double angleRadians)
    {
        if (angleRadians == 0)return;
        double c, s, t;
        int i;
        c = Math.cos( -angleRadians);
        s = Math.sin( -angleRadians);
        for (i = 0; i < 3; i++)
        {
            t = a[i][0];
            a[i][0] = t * c - a[i][1] * s;
            a[i][1] = t * s + a[i][1] * c;
        }
    }


    public void multiply(Matrix3x3 b)
    {
        int i, j, k;
        Matrix3x3 c = this.copy();
        for (i = 0; i < 3; i++)
        {
            for (j = 0; j < 3; j++)
            {
                a[i][j] = 0.0;
                for (k = 0; k < 3; k++)
                {
                    a[i][j] += c.getElement(i, k) * b.getElement(k, j);
                }
            }
        }
    }


    public void preMultiply(Matrix3x3 b)
    {
        int i, j, k;
        Matrix3x3 c = this.copy();
        for (i = 0; i < 3; i++)
        {
            for (j = 0; j < 3; j++)
            {
                a[i][j] = 0.0;
                for (k = 0; k < 3; k++)
                {
                    a[i][j] += b.getElement(i, k) * c.getElement(k, j);
                }
            }
        }
    }


    public void postMultiply(Matrix3x3 b)
    {
        multiply(b);
    }


    public final static Matrix3x3 matrixMultiply(Matrix3x3 b, Matrix3x3 c)
    {
        Matrix3x3 m = b.copy();
        m.multiply(c);
        return m;
    }


    public void transpose()
    {
        Matrix3x3 b = this.copy();
        int i, j;
        for (i = 0; i < 3; i++)
        {
            for (j = 0; j < 3; j++)
            {
                a[i][j] = b.getElement(j, i);
            }
        }
    }


    // copies a matrix
    public Matrix3x3 copy()
    {
        Matrix3x3 c = new Matrix3x3();
        int i, j;
        for (i = 0; i < 3; i++)
        {
            for (j = 0; j < 3; j++)
            {
                c.setElement(i, j, a[i][j]);
            }
        }
        return c;
    }


    public Vector3D getVector1()
    {
        return (new Vector3D(a[0][0], a[1][0], a[2][0]));
    }


    public Vector3D getVector2()
    {
        return (new Vector3D(a[0][1], a[1][1], a[2][1]));
    }


    public Vector3D getVector3()
    {
        return (new Vector3D(a[0][2], a[1][2], a[2][2]));
    }


    public void normalize()
    {
        Vector[] v = new Vector[3];
        v[0] = getVector1();
        v[0].normalize();
        v[1] = getVector2();
        v[1].normalize();
        v[2] = getVector3();
        v[2].normalize();
        a[0][0] = v[0].getElement(0);
        a[1][0] = v[0].getElement(1);
        a[2][0] = v[0].getElement(2);
        a[0][1] = v[1].getElement(0);
        a[1][1] = v[1].getElement(1);
        a[2][1] = v[1].getElement(2);
        a[0][2] = v[2].getElement(0);
        a[1][2] = v[2].getElement(1);
        a[2][2] = v[2].getElement(2);
    }


    /** generates transform matrix given three Euler angles (Type II 3-2-3 assumed);
        the Euler angles are assumed to represent transforms in the following order:
        Angle1 about the zAxis, followed by Angle2 about the xAxis, followed by Angle3
        about the zAxis (i.e. 3-2-3 rotation)
        Added by Mike Botts 8/14/98
     */
    public void euler(double Angle1Radians, double Angle2Radians, double Angle3Radians)
    {
        rotateZ(Angle1Radians);
        rotateX(Angle2Radians);
        rotateZ(Angle3Radians);
    }


    public boolean isOrthogonal(double threshHold)
    {
        Vector[] v = new Vector[3];
        v[0] = getVector1();
        v[1] = getVector2();
        v[2] = getVector3();

        double dot = v[0].dot(v[1]);
        if (Math.abs(dot) > threshHold)
            return false;
        dot = v[1].dot(v[2]);
        if (Math.abs(dot) > threshHold)
            return false;
        dot = v[2].dot(v[0]);
        if (Math.abs(dot) > threshHold)
            return false;

        return (true);
    }


    public boolean isOrthogonal()
    {
        return (isOrthogonal(.0001));
    }

}
