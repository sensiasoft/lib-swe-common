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
 * Matrix specialization for a 3d rotation matrix.
 * Extends from javax.vecmath.Matrix3d and adds additional optimized
 * math routines for 3D transformations.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Jul 19, 2006
 * */
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
     */ 
    public void inverse() {
      //  int n = a.length;
        int n = 3;
        double a[][] = new double[n][n];
        a[0][0] = m00;
        a[0][1] = m01;
        a[0][2] = m02;
        a[1][0] = m10;
        a[1][1] = m11;
        a[1][2] = m12;
        a[2][0] = m20;
        a[2][1] = m21;
        a[2][2] = m22;
        
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i=0; i<n; ++i) b[i][i] = 1;

     // Transform the matrix into an upper triangle
        gaussian(a, index);

     // Update the matrix b[i][j] with the ratios stored
        for (int i=0; i<n-1; ++i)
          for (int j=i+1; j<n; ++j)
            for (int k=0; k<n; ++k)
              b[index[j]][k]
                -= a[index[j]][i]*b[index[i]][k];

     // Perform backward substitutions
        for (int i=0; i<n; ++i) {
          x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
          for (int j=n-2; j>=0; --j) {
            x[j][i] = b[index[j]][i];
            for (int k=j+1; k<n; ++k) {
              x[j][i] -= a[index[j]][k]*x[k][i];
            }
            x[j][i] /= a[index[j]][j];
          }
        }
      
      m00 = x[0][0];
      m01 = x[0][1];
      m02 = x[0][2];
      m10 = x[1][0];
      m11 = x[1][1];
      m12 = x[1][2];
      m20 = x[2][0];
      m21 = x[2][1];
      m22 = x[2][2];
      
      }

//     Method to carry out the partial-pivoting Gaussian
//     elimination.  Here index[] stores pivoting order.

      public static void gaussian(double a[][],
        int index[]) {
        int n = index.length;
        double c[] = new double[n];

     // Initialize the index
        for (int i=0; i<n; ++i) index[i] = i;

     // Find the rescaling factors, one from each row
        for (int i=0; i<n; ++i) {
          double c1 = 0;
          for (int j=0; j<n; ++j) {
            double c0 = Math.abs(a[i][j]);
            if (c0 > c1) c1 = c0;
          }
          c[i] = c1;
        }

     // Search the pivoting element from each column
        int k = 0;
        for (int j=0; j<n-1; ++j) {
          double pi1 = 0;
          for (int i=j; i<n; ++i) {
            double pi0 = Math.abs(a[index[i]][j]);
            pi0 /= c[index[i]];
            if (pi0 > pi1) {
              pi1 = pi0;
              k = i;
            }
          }

       // Interchange rows according to the pivoting order
          int itmp = index[j];
          index[j] = index[k];
          index[k] = itmp;
          for (int i=j+1; i<n; ++i) {
            double pj = a[index[i]][j]/a[index[j]][j];

         // Record pivoting ratios below the diagonal
            a[index[i]][j] = pj;

         // Modify other elements accordingly
            for (int l=j+1; l<n; ++l)
              a[index[i]][l] -= pj*a[index[j]][l];
          }
        }
      }
      
}
