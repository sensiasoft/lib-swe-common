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
    public static Matrix3x3 getRotationMatrix(Vector3D position, Vector3D toEcfNorth, int forwardAxis, int upAxis)
    {
        Vector3D up, heading, other;

        double[] lla = MapProjection.ECFtoLLA(position.getX(), position.getY(), position.getZ(), new Datum());
        double[] ecf = MapProjection.LLAtoECF(lla[0], lla[1], -3e3, new Datum());        
        Vector3D nearPoint = new Vector3D(ecf[0], ecf[1], ecf[2]);
        
        up = Vector3D.subtractVectors(position, nearPoint);
        up.normalize();

        if ((forwardAxis == 1) && (upAxis == 3))
        {
            other = up.cross(toEcfNorth);
            other.normalize();
            heading = other.cross(up);
            return new Matrix3x3(heading, other, up);
        }

        if ((forwardAxis == 1) && (upAxis == 2))
        {
            other = toEcfNorth.cross(up);
            other.normalize();
            heading = up.cross(other);
            return new Matrix3x3(heading, up, other);
        }

        if ((forwardAxis == 2) && (upAxis == 1))
        {
            other = up.cross(toEcfNorth);
            other.normalize();
            heading = other.cross(up);
            return new Matrix3x3(up, heading, other);
        }

        if ((forwardAxis == 2) && (upAxis == 3))
        {
            other = toEcfNorth.cross(up);
            other.normalize();
            heading = up.cross(other);
            return new Matrix3x3(other, heading, up);
        }

        if ((forwardAxis == 3) && (upAxis == 1))
        {
            other = toEcfNorth.cross(up);
            other.normalize();
            heading = up.cross(other);
            return new Matrix3x3(up, other, heading);
        }

        if ((forwardAxis == 3) && (upAxis == 2))
        {
            other = up.cross(toEcfNorth);
            other.normalize();
            heading = other.cross(up);
            return new Matrix3x3(other, up, heading);
        }

        return new Matrix3x3();
    }


    /**
     * Computes a vector pointing to north from ecf position
     * @param ecfPosition ECF
     * @return ECFVelocity with new velocity vector
     */
    public static Vector3D getEcfVectorToNorth(Vector3D ecfPosition)
    {
        double polarRadius = (new Datum()).polarRadius;
        Vector3D northPole = new Vector3D(0.0, 0.0, polarRadius);
        return Vector3D.subtractVectors(northPole, ecfPosition);
    }
}
