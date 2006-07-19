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
}
