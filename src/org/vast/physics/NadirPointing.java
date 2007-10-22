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

package org.vast.physics;

import org.vast.math.*;


/**
 * <p><b>Title:</b><br/>
 * Nadir Pointing
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Static routines to compute ECEF nadir orientation
 * at a given ECEF position 
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 29, 2005
 * @version 1.0
 */
public class NadirPointing
{
    /**
     * Compute the rotation matrix to obtain nadir/north orientation
     * @param position position Vector in ECF
     * @param toEcfNorth vector pointing to north in ECF
     * @param forwardAxis number of the axis (1:x, 2:y, 3:z) to put in the forward direction
     * @param upAxis number of the axis (1:x, 2:y, 3:z)  to put in the up direction
     * @return the 3x3 rotation matrix
     */
    public static Matrix3d getRotationMatrix(Vector3d position, Vector3d toEcfNorth, int forwardAxis, int upAxis)
    {
        Vector3d up = new Vector3d();
        Vector3d heading = new Vector3d();
        Vector3d other = new Vector3d();

        double[] lla = MapProjection.ECFtoLLA(position.x, position.y, position.z, new Datum());
        double[] ecf = MapProjection.LLAtoECF(lla[0], lla[1], -3e3, new Datum());        
        Vector3d nearPoint = new Vector3d(ecf[0], ecf[1], ecf[2]);
        
        up.sub(position, nearPoint);
        up.normalize();

        if ((forwardAxis == 1) && (upAxis == 3))
        {
            other.cross(up, toEcfNorth);
            other.normalize();
            heading.cross(other, up);
            return new Matrix3d(heading, other, up);
        }

        if ((forwardAxis == 1) && (upAxis == 2))
        {
            other.cross(toEcfNorth, up);
            other.normalize();
            heading.cross(up, other);
            return new Matrix3d(heading, up, other);
        }

        if ((forwardAxis == 2) && (upAxis == 1))
        {
            other.cross(up, toEcfNorth);
            other.normalize();
            heading.cross(other, up);
            return new Matrix3d(up, heading, other);
        }

        if ((forwardAxis == 2) && (upAxis == 3))
        {
            other.cross(toEcfNorth, up);
            other.normalize();
            heading.cross(up, other);
            return new Matrix3d(other, heading, up);
        }

        if ((forwardAxis == 3) && (upAxis == 1))
        {
            other.cross(toEcfNorth, up);
            other.normalize();
            heading.cross(up, other);
            return new Matrix3d(up, other, heading);
        }

        if ((forwardAxis == 3) && (upAxis == 2))
        {
            other.cross(up, toEcfNorth);
            other.normalize();
            heading.cross(other, up);
            return new Matrix3d(other, up, heading);
        }

        return new Matrix3d();
    }


    /**
     * Computes a vector pointing to north from ecf position
     * @param ecfPosition ECF
     * @return ECFVelocity with new velocity vector
     */
    public static Vector3d getEcfVectorToNorth(Vector3d ecfPosition)
    {
        double polarRadius = (new Datum()).polarRadius;
        Vector3d northPole = new Vector3d(0.0, 0.0, polarRadius);
        Vector3d res = new Vector3d();
        res.sub(northPole, ecfPosition);
        return res;
    }
}
