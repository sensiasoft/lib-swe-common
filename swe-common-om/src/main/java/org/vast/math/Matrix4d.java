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

package org.vast.math;


/**
 * <p>
 * Matrix specialization for a 4d homogeneous matrix.
 * Extends from javax.vecmath.Matrix4d and adds additional optimized
 * math routines for 3D transformations.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Jul 19, 2006
 * */
public class Matrix4d extends javax.vecmath.Matrix4d
{
    static final long serialVersionUID = 0;
    
    
    public Matrix4d()
    {
        super();
        this.setIdentity();
    }
    
    
    public Matrix4d(double[] components)
    {
        super(components);
    }
    
    
    public Matrix4d(double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20, double m21, double m22, double m23, double m30, double m31, double m32, double m33)
    {
        super(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }
    
    
    public Matrix4d(Vector3d v1, Vector3d v2, Vector3d v3)
    {
        this();
        this.setColumn(0, v1.x, v1.y, v1.z, 0.0);
        this.setColumn(1, v2.x, v2.y, v2.z, 0.0);
        this.setColumn(2, v3.x, v3.y, v3.z, 0.0);
    }
    
    
    public Matrix4d(Matrix4d mat)
    {
        super(mat);
    }
    
    
    public Matrix4d(Matrix3d mat)
    {
        this();
        this.setRotation(mat);
    }
    
    
    public Matrix4d copy()
    {
        return new Matrix4d(this);
    }
    
    
    public void setTranslation(double x, double y, double z)
    {
        this.m03 = x;
        this.m13 = y;
        this.m23 = z;
    }
    
    
    /**
     * Multiplies only the upper portion of the homogeneous matrix
     * by the given Matrix3d components
     * @param mat
     */
    public void mul(Matrix3d mat)
    {
        Matrix3d rotPortion = new Matrix3d(); 
        this.getRotationScale(rotPortion);
        rotPortion.mul(mat);
        this.setRotationScale(rotPortion);
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
        m00 = -s * m20 + c * m;
        m20 = c * m20 + s * m;
        
        m = m01;
        m01 = -s * m21 + c * m;
        m21 = c * m21 + s * m;
        
        m = m02;
        m02 = -s * m22 + c * m;
        m22 = c * m22 + s * m;
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
}
