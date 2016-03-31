/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.helper;

import org.vast.swe.SWEConstants;
import org.vast.swe.SWEHelper;
import net.opengis.swe.v20.Matrix;
import net.opengis.swe.v20.Vector;


public class VectorHelper extends SWEHelper
{
    public static String DEF_UNIT_VECTOR = SWEHelper.getPropertyUri("UnitVector");
    public static String DEF_ROW = SWEHelper.getPropertyUri("Row");
    public static String DEF_COEF = SWEHelper.getPropertyUri("Coordinate");
    public static String DEF_ROT_MATRIX = SWEHelper.getPropertyUri("RotationMatrix");
    
    
    public Vector newVector3()
    {
        return newVector3(null, null);
    }
    
    
    public Vector newVector3(String def, String crs)
    {
        return newVector(
                def,
                crs,
                new String[] {"u1", "u2", "u3"},
                null,
                new String[] {"1", "1", "1"},
                null);
    }
    
    
    public Vector newUnitVectorXYZ(String def, String crs)
    {
        if (def == null)
            def = DEF_UNIT_VECTOR;
        
        return newVector(
                def,
                crs,
                new String[] {"ux", "uy", "uz"},
                null,
                new String[] {"1", "1", "1"},
                new String[] {"X", "Y", "Z"});
    }
    
    
    /**
     * Creates a 3D location vector in an ortho-normal frame with X/Y/Z axes
     * @param def semantic definition of velocity vector (if null, {@link SWEConstants#DEF_LOCATION} is used)
     * @param refFrame reference frame within which the vector is expressed
     * @param uomCode unit of distance to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newLocationVectorXYZ(String def, String refFrame, String uomCode)
    {
        if (def == null)
            def = SWEConstants.DEF_LOCATION;
        
        return newVector(
                def,
                refFrame,
                new String[] {"x", "y", "z"},
                new String[] {"X Pos", "Y Pos", "Z Pos"},
                new String[] {uomCode, uomCode, uomCode},
                new String[] {"X", "Y", "Z"});
    }
    
    
    public Vector newEulerAngles()
    {
        return newVector(
                SWEConstants.DEF_ORIENTATION,
                null,
                new String[] {"r1", "r2", "r3"},
                null,
                new String[] {"rad", "rad", "rad"},
                new String[] {null, null, null});
    }
    
    
    public Matrix newMatrix(int nRows, int nCols)
    {
        return newMatrix(null, null, nRows, nCols);
    }
    
    
    public Matrix newMatrix(String def, String crs, int nRows, int nCols)
    {
        Matrix row = newMatrix(nCols);
        row.setDefinition(DEF_ROW);
        row.setElementType("coef", newQuantity(DEF_COEF, null, null, "1"));
        
        Matrix mat = newMatrix(nRows);
        mat.setDefinition(def);
        mat.setElementType("row", row);
        
        return mat;
    }
}
