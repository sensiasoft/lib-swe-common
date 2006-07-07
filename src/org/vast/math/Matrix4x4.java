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


public class Matrix4x4 extends Matrix
{
    /** default constructor builds identity matrix */
    public Matrix4x4()
    {
        a = new double[4][4];
        rows = 4;
        columns = 4;
        for (int i = 0; i < 4; i++)a[i][i] = 1.0;
    }


    // constructor to make all elements a constant value
    public Matrix4x4(double value)
    {
        a = new double[4][4];
        rows = 4;
        columns = 4;
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                a[i][j] = value;
            }
        }
    }


    public Matrix4x4(Matrix3x3 m)
    {
        this(m.getVector1(), m.getVector2(), m.getVector3());
    }


    /** this constructor only takes nine elements for the upper left-hand
        3x3 corner of the matrix, and fills in the 4th column 4th row with
        values typical of a homogeneous matrix
     */
    public Matrix4x4(double a0, double a1, double a2, double a3, double a4,
                     double a5, double a6, double a7, double a8)
    {

        this();
        a[0][0] = a0;
        a[0][1] = a1;
        a[0][2] = a2;
        a[0][3] = 0;
        a[1][0] = a3;
        a[1][1] = a4;
        a[1][2] = a5;
        a[1][3] = 0;
        a[2][0] = a6;
        a[2][1] = a7;
        a[2][2] = a8;
        a[2][3] = 0;
        a[3][0] = 0;
        a[3][1] = 0;
        a[3][2] = 0;
        a[3][3] = 1;
    }


    // expects a double[16] array, with a[0][1] = b[1]
    public Matrix4x4(double[] b)
    {
        this();
        a[0][0] = b[0];
        a[0][1] = b[1];
        a[0][2] = b[2];
        a[0][3] = b[3];
        a[1][0] = b[4];
        a[1][1] = b[5];
        a[1][2] = b[6];
        a[1][3] = b[7];
        a[2][0] = b[8];
        a[2][1] = b[9];
        a[2][2] = b[10];
        a[2][3] = b[11];
        a[3][0] = b[12];
        a[3][1] = b[13];
        a[3][2] = b[14];
        a[3][3] = b[15];
    }


    // expects a double[4][4] array, with double[row][column] ordering
    public Matrix4x4(double[][] array)
    {
        this();
        a = array; // NOTE: do I need a copy instead of assignment ??
    }


    /** this constructor takes three vectors to fill in the nine elements
        of the upper left-hand 3x3 corner of the matrix, and fills in the
        4th column 4th row with values typical of a homogeneous matrix;
        The vector are such that xAxis fills in a[0][0-2], yAxis a[1][0-2],
        and zAxis a[2][0-2]
     */
    public Matrix4x4(Vector3D xAxis, Vector3D yAxis, Vector3D zAxis)
    {
        this();
        a[0][0] = xAxis.getX();
        a[0][1] = yAxis.getX();
        a[0][2] = zAxis.getX();
        a[0][3] = 0;
        a[1][0] = xAxis.getY();
        a[1][1] = yAxis.getY();
        a[1][2] = zAxis.getY();
        a[1][3] = 0;
        a[2][0] = xAxis.getZ();
        a[2][1] = yAxis.getZ();
        a[2][2] = zAxis.getZ();
        a[2][3] = 0;
        a[3][0] = 0;
        a[3][1] = 0;
        a[3][2] = 0;
        a[3][3] = 1;

    }


    public Matrix4x4(double a0, double a1, double a2, double a3, double a4,
                     double a5, double a6, double a7, double a8, double a9, double a10, double a11,
                     double a12, double a13, double a14, double a15)
    {

        this();
        a[0][0] = a0;
        a[0][1] = a1;
        a[0][2] = a2;
        a[0][3] = a3;
        a[1][0] = a4;
        a[1][1] = a5;
        a[1][2] = a6;
        a[1][3] = a7;
        a[2][0] = a8;
        a[2][1] = a9;
        a[2][2] = a10;
        a[2][3] = a11;
        a[3][0] = a12;
        a[3][1] = a13;
        a[3][2] = a14;
        a[3][3] = a15;
    }


    /** turn matrix into identity matrix */
    public void identity()
    {
        a[0][0] = 1;
        a[0][1] = 0;
        a[0][2] = 0;
        a[0][3] = 0;
        a[1][0] = 0;
        a[1][1] = 1;
        a[1][2] = 0;
        a[1][3] = 0;
        a[2][0] = 0;
        a[2][1] = 0;
        a[2][2] = 1;
        a[2][3] = 0;
        a[3][0] = 0;
        a[3][1] = 0;
        a[3][2] = 0;
        a[3][3] = 1;
    }


    /* the following concatenation routine utilize "Efficient Post-Concatenation
        of Transformation Matrices" by Joseph Cychosz in Graphics Gems I
     */
    public void translate(Vector3D translation)
    {
        translate(translation.getX(), translation.getY(), translation.getZ());
    }


    public void translate(double tx, double ty, double tz)
    {
        a[0][3] += tx;
        a[1][3] += ty;
        a[2][3] += tz;
    }


    public void scale(Vector3D scaling)
    {
        scale(scaling.getX(), scaling.getY(), scaling.getZ());
    }


    public void scale(double sx, double sy, double sz)
    {
        int i;
        for (i = 0; i < 4; i++)
        {
            a[i][0] *= sx;
            a[i][1] *= sy;
            a[i][2] *= sz;
        }
    }


    // rotate about x-axis (right-handed rotation)
    public void rotateX(double angleRadians)
    {
        double c, s, t;
        int i;
        c = Math.cos( -angleRadians);
        s = Math.sin( -angleRadians);
        // meb-2/27/2001: NOTE: these equations are for rotating the coordinate
        // frame by angleRadians, and getting new z and y for vector in that
        // frame ... this is the reason for the negation of the angle above
        for (i = 0; i < 4; i++)
        {
            t = a[i][1];
            a[i][1] = t * c - a[i][2] * s;
            a[i][2] = t * s + a[i][2] * c;
        }
    }


    // rotate about y-axis (right-handed rotation)
    public void rotateY(double angleRadians)
    {
        double c, s, t;
        int i;
        c = Math.cos( -angleRadians);
        s = Math.sin( -angleRadians);
        for (i = 0; i < 4; i++)
        {
            t = a[i][0];
            a[i][0] = t * c + a[i][2] * s;
            a[i][2] = a[i][2] * c - t * s;
        }
    }


    // rotate about z-axis (right-handed rotation)
    public void rotateZ(double angleRadians)
    {
        double c, s, t;
        int i;
        c = Math.cos( -angleRadians);
        s = Math.sin( -angleRadians);
        for (i = 0; i < 4; i++)
        {
            t = a[i][0];
            a[i][0] = t * c - a[i][1] * s;
            a[i][1] = t * s + a[i][1] * c;
        }
    }


    // perspective is along z-axis with the eye at +z
    public void perspective(double distance)
    {
        int i;
        double f = 1.0 / distance;
        for (i = 0; i < 4; i++)
        {
            a[i][3] += a[i][2] * f;
            a[i][2] = 0.0;
        }
    }


    public void preMultiply(Matrix4x4 b)
    {
        int i, j, k;
        Matrix4x4 c = this.copy();
        for (i = 0; i < 4; i++)
        {
            for (j = 0; j < 4; j++)
            {
                a[i][j] = 0.0;
                for (k = 0; k < 4; k++)
                {
                    a[i][j] += b.getElement(i, k) * c.getElement(k, j);
                }
            }
        }
    }


    public void multiply(Matrix4x4 b)
    {
        int i, j, k;
        Matrix4x4 c = this.copy();
        for (i = 0; i < 4; i++)
        {
            for (j = 0; j < 4; j++)
            {
                a[i][j] = 0.0;
                for (k = 0; k < 4; k++)
                {
                    a[i][j] += c.getElement(i, k) * b.getElement(k, j);
                }
            }
        }
    }


    // Be careful when using this function: it multiplies only the rotation part !!!
    public void preMultiply(Matrix3x3 b)
    {
        int i, j, k;
        Matrix4x4 c = this.copy();
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
    
    
    public void multiply(Matrix3x3 b)
    {
        int i, j, k;
        Matrix4x4 c = this.copy();
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


    public void multiply(double scalar)
    {
        int i, j;
        for (i = 0; i < 4; i++)
        {
            for (j = 0; j < 4; j++)
            {
                a[i][j] *= scalar;
            }
        }
    }


    public void transpose()
    {
        Matrix4x4 b = this.copy();
        int i, j;
        for (i = 0; i < 4; i++)
        {
            for (j = 0; j < 4; j++)
            {
                a[i][j] = b.getElement(j, i);
            }
        }
    }


    // copies a matrix
    public Matrix4x4 copy()
    {
        Matrix4x4 c = new Matrix4x4();
        int i, j;
        for (i = 0; i < 4; i++)
        {
            for (j = 0; j < 4; j++)
            {
                c.setElement(i, j, a[i][j]);
            }
        }
        return c;
    }
}
