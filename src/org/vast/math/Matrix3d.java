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
 * <p><b>Title:</b>
 * Matrix3d
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Matrix specialization for a 3d rotation matrix.
 * Extends from javax.vecmath.Matrix3d and adds additional optimized
 * math routines for 3D transformations.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Jul 19, 2006
 * @version 1.0
 */
public class Matrix3d extends javax.vecmath.Matrix3d
{
    static final long serialVersionUID = 0;


    public Matrix3d()
    {
        super();
        this.setIdentity();
    }


    public Matrix3d(double[] components)
    {
        super(components);
    }


    public Matrix3d(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22)
    {
        super(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }


    public Matrix3d(Vector3d v1, Vector3d v2, Vector3d v3)
    {
        this.setColumn(0, v1.x, v1.y, v1.z);
        this.setColumn(1, v2.x, v2.y, v2.z);
        this.setColumn(2, v3.x, v3.y, v3.z);
    }


    public Matrix3d(Matrix3d mat)
    {
        super(mat);
    }


    public Matrix3d copy()
    {
        return new Matrix3d(this);
    }


    public void mulRight(Matrix3d mat)
    {
        super.mul(mat);
    }


    public void mulLeft(Matrix3d mat)
    {

    }


    /**
     * Rotate about x-axis (right-handed rotation)
     * @param angleRadians
     */
    public void rotateX(double angleRadians)
    {
        if (angleRadians == 0)
            return;
        
        double c, s, m;
        c = Math.cos(angleRadians);
        s = Math.sin(angleRadians);

        m = m10;
        m10 = s * m20 + c * m;
        m20 = c * m20 - s * m;
        
        m = m11;
        m11 = s * m21 + c * m;
        m21 = c * m21 - s * m;
        
        m = m12;
        m12 = s * m22 + c * m;
        m22 = c * m22 - s * m;
    }


    /**
     * Rotate about y-axis (right-handed rotation)
     * @param angleRadians
     */
    public void rotateY(double angleRadians)
    {
        if (angleRadians == 0)
            return;
        
        double c, s, m;
        c = Math.cos(angleRadians);
        s = Math.sin(angleRadians);

        m = m00;
        m00 = s * m20 + c * m;
        m20 = c * m20 - s * m;
        
        m = m01;
        m01 = s * m21 + c * m;
        m21 = c * m21 - s * m;
        
        m = m02;
        m02 = s * m22 + c * m;
        m22 = c * m22 - s * m;
    }


    /**
     * Rotate about z-axis (right-handed rotation)
     * @param angleRadians
     */
    public void rotateZ(double angleRadians)
    {
        if (angleRadians == 0)
            return;
        
        double c, s, m;
        c = Math.cos(angleRadians);
        s = Math.sin(angleRadians);

        m = m00;
        m00 = s * m10 + c * m;
        m10 = c * m10 - s * m;
        
        m = m01;
        m01 = s * m11 + c * m;
        m11 = c * m11 - s * m;
        
        m = m02;
        m02 = s * m12 + c * m;
        m12 = c * m12 - s * m;
    }
    
    /**
     * Inverse the matrix 
    
    public void inverse2()
    {
        
        double a11, a12, a13, a21, a22, a23, a31, a32, a33;
        
        a11 = m00;
        a12 = m01;
        a13 = m02;
        a21 = m10;
        a22 = m11;
        a23 = m12;
        a31 = m20;
        a32 = m21;
        a33 = m22;
        
        m00 = a22*a33-a32*a23;
        m01 = a13*a32-a12*a33;
        m02 = a12*a23-a22*a13;
        m10 = a23*a31-a33*a21;
        m11 = a11*a33-a31*a13;
        m12 = a13*a21-a23*a11;
        m20 = a21*a32-a31*a22;
        m21 = a12*a31-a11*a32;
        m22 = a11*a22-a21-a12;
        m = m11;
        m11 = s * m21 + c * m;
        m21 = c * m21 - s * m;
        
        m = m12;
        m12 = s * m22 + c * m;
        m22 = c * m22 - s * m;
    }
    
    **/
}
