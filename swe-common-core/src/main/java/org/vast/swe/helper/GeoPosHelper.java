/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
The Initial Developer is Botts Innovative Research Inc. Portions created by the Initial
Developer are Copyright (C) 2016 the Initial Developer. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.helper;

import java.util.Arrays;
import java.util.List;
import org.vast.swe.SWEConstants;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.Vector;


/**
 * <p>
 * Helper class to create SWE structures used for geo-positioning<br/>
 * This includes location and attitude but also linear velocity, angular rate,
 * and linear acceleration
 * </p>
 *
 * @author Alex Robin
 * @since March 2016
 */
public class GeoPosHelper extends VectorHelper
{
    public static final String DEF_VELOCITY = getPropertyUri("Velocity");
    public static final String DEF_ACCELERATION = getPropertyUri("Acceleration");
    public static final String DEF_ACCELERATION_MAG = getPropertyUri("AccelerationMagnitude");
    public static final String DEF_ANGULAR_RATE = getPropertyUri("AngularRate");
    
    
    /**
     * Creates a 3D location vector with latitude/longitude/altitude axes (EPSG 4979) 
     * @param def semantic definition of location vector (if null, {@link #DEF_LOCATION} is used)
     * @return the new Vector component object
     */
    public Vector newLocationVectorLLA(String def)
    {
        if (def == null)
            def = DEF_LOCATION;
        
        return newVector(
                def,
                SWEConstants.REF_FRAME_4979,
                new String[] {"lat", "lon", "alt"},
                new String[] {"Geodetic Latitude", "Longitude", "Altitude"},
                new String[] {"deg", "deg", "m"},
                new String[] {"Lat", "Long", "h"});
    }
    
    
    /**
     * Creates a 2D location vector with latitude/longitude axes (EPSG 4326) 
     * @param def semantic definition of location vector (if null, {@link #DEF_LOCATION} is used)
     * @return the new Vector component object
     */
    public Vector newLocationVectorLatLon(String def)
    {
        if (def == null)
            def = DEF_LOCATION;
        
        return newVector(
                def,
                SWEConstants.REF_FRAME_4326,
                new String[] {"lat", "lon"},
                new String[] {"Geodetic Latitude", "Longitude"},
                new String[] {"deg", "deg"},
                new String[] {"Lat", "Long"});
    }
    
    
    /**
     * Creates a 3D location vector with ECEF X/Y/Z axes (EPSG 4978) 
     * @param def semantic definition of location vector (if null, {@link #DEF_LOCATION} is used)
     * @return the new Vector component object
     */
    public Vector newLocationVectorECEF(String def)
    {
        if (def == null)
            def = DEF_LOCATION;
        
        return newLocationVectorXYZ(def, SWEConstants.REF_FRAME_ECEF, "m");
    }
    
    
    /**
     * Creates a 3D velocity vector in an ortho-normal frame with X/Y/Z axes
     * @param def semantic definition of velocity vector (if null, {@link #DEF_VELOCITY} is used)
     * @param refFrame reference frame within which the vector is expressed
     * @param uomCode unit of velocity to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newVelocityVector(String def, String refFrame, String uomCode)
    {
        if (def == null)
            def = DEF_VELOCITY;
        
        return newVector(
                def,
                refFrame,
                new String[] {"vx", "vy", "vz"},
                new String[] {"X Vel", "Y Vel", "Z Vel"},
                new String[] {uomCode, uomCode, uomCode},
                new String[] {"X", "Y", "Z"});
    }
    
    
    /**
     * Creates a 3D velocity with ECEF X/Y/Z axes (EPSG 4978) 
     * @param def semantic definition of velocity vector (if null, {@link #DEF_VELOCITY} is used)
     * @param uomCode unit of velocity to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newVelocityVectorECEF(String def, String uomCode)
    {
        return newVelocityVector(def, SWEConstants.REF_FRAME_ECEF, uomCode);
    }
    
    
    /**
     * Creates a 3D velocity with ENU X/Y/Z axes
     * @param def semantic definition of velocity vector (if null, {@link #DEF_VELOCITY} is used)
     * @param uomCode unit of velocity to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newVelocityVectorENU(String def, String uomCode)
    {
        return newVelocityVector(def, SWEConstants.REF_FRAME_ENU, uomCode);
    }
    
    
    /**
     * Creates a 3D velocity with NED X/Y/Z axes
     * @param def semantic definition of velocity vector (if null, {@link #DEF_VELOCITY} is used)
     * @param uomCode unit of velocity to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newVelocityVectorNED(String def, String uomCode)
    {
        return newVelocityVector(def, SWEConstants.REF_FRAME_NED, uomCode);
    }
    
    
    /**
     * Creates a 3D acceleration vector in an ortho-normal frame with X/Y/Z axes
     * @param def semantic definition of acceleration vector (if null, {@link #DEF_ACCELERATION} is used)
     * @param refFrame reference frame within which the vector is expressed
     * @param uomCode unit of acceleration to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newAccelerationVector(String def, String refFrame, String uomCode)
    {
        if (def == null)
            def = DEF_ACCELERATION;
        
        return newVector(
                def,
                refFrame,
                new String[] {"ax", "ay", "az"},
                new String[] {"X Accel", "Y Accel", "Z Accel"},
                new String[] {uomCode, uomCode, uomCode},
                new String[] {"X", "Y", "Z"});
    }
    
    
    /**
     * Creates a 3D orientation vector composed of 3 Euler angles expressed in local
     * East-North-Up (ENU) frame (order of rotations is Z, X, Y in rotating frame)
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_EULER} is used)
     * @return the new Vector component object
     */
    public Vector newEulerOrientationENU(String def)
    {
        if (def == null)
            def = DEF_ORIENTATION_EULER;
        
        return newVector(
                def,
                SWEConstants.REF_FRAME_ENU,
                new String[] {"yaw", "pitch", "roll"},
                new String[] {"Yaw Angle", "Pitch Angle", "Roll Angle"},
                new String[] {"deg", "deg", "deg"},
                new String[] {"Z", "X", "Y"});
    }
    
    
    /**
     * Creates a 3D orientation vector composed of 3 Euler angles expressed in local
     * North-East-Down (NED) frame (order of rotations is Z, Y, X)
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_EULER} is used)
     * @return the new Vector component object
     */
    public Vector newEulerOrientationNED(String def)
    {
        if (def == null)
            def = DEF_ORIENTATION_EULER;
        
        return newVector(
                def,
                SWEConstants.REF_FRAME_NED,
                new String[] {"yaw", "pitch", "roll"},
                new String[] {"Yaw Angle", "Pitch Angle", "Roll Angle"},
                new String[] {"deg", "deg", "deg"},
                new String[] {"Z", "Y", "X"});
    }
    
    
    /**
     * Creates an orientation vector component composed of 3 Euler angles expressed in
     * Earth-Centered-Earth-Fixed (ECEF) frame (order of rotations is X, Y, Z)
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_EULER} is used)
     * @return the new Vector component object
     */
    public Vector newEulerOrientationECEF(String def)
    {
        if (def == null)
            def = DEF_ORIENTATION_EULER;
        
        return newVector(
                def,
                SWEConstants.REF_FRAME_ECEF,
                new String[] {"x", "y", "z"},
                new String[] {"X Angle", "Y Angle", "Z Angle"},
                new String[] {"deg", "deg", "deg"},
                new String[] {"X", "Y", "Z"});
    }
    
    
    /**
     * Creates a 4d vector representing an orientation quaternion expressed in the given frame (scalar comes first).
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_QUAT} is used)
     * @param crs reference frame with respect to which the coordinates of this quaternion are expressed
     * @return the new Vector component object
     */
    public Vector newQuatOrientation(String def, String crs)
    {
        if (def == null)
            def = DEF_ORIENTATION_QUAT;
        
        return newVector(
                def,
                crs,
                new String[] {"qx", "qy", "qz", "q0"},
                new String[] {"X Component", "Y Component", "Z Component", "Scalar"},
                new String[] {"1", "1", "1", "1"},
                new String[] {"X", "Y", "Z", null});
    }
    
    
    /**
     * Creates a 4d vector representing an orientation quaternion expressed in ENU frame.
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_QUAT} is used)
     * @return the new Vector component object
     */
    public Vector newQuatOrientationENU(String def)
    {
        return newQuatOrientation(def, SWEConstants.REF_FRAME_ENU);
    }
    
    
    /**
     * Creates a 4d vector representing an orientation quaternion expressed in NED frame.
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_QUAT} is used)
     * @return the new Vector component object
     */
    public Vector newQuatOrientationNED(String def)
    {
        return newQuatOrientation(def, SWEConstants.REF_FRAME_NED);
    }
    
    
    /**
     * Creates a 4d vector representing an orientation quaternion expressed in ECEF frame.
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_QUAT} is used)
     * @return the new Vector component object
     */
    public Vector newQuatOrientationECEF(String def)
    {
        return newQuatOrientation(def, SWEConstants.REF_FRAME_ECEF);
    }
    
    
    /**
     * Creates a 3D angular velocity vector in an ortho-normal frame with X/Y/Z axes
     * @param def semantic definition of angular velocity vector (if null, {@link #DEF_ANGULAR_RATE} is used)
     * @param refFrame reference frame within which the vector is expressed
     * @param uomCode unit of acceleration to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newAngularVelocityVector(String def, String refFrame, String uomCode)
    {
        if (def == null)
            def = DEF_ANGULAR_RATE;
        
        return newVector(
                def,
                refFrame,
                new String[] {"rx", "ry", "rz"},
                new String[] {"X Angular Rate", "Y Angular Rate", "Z Angular Rate"},
                new String[] {uomCode, uomCode, uomCode},
                new String[] {"X", "Y", "Z"});
    }
    
    
    ///
    // Methods providing complete output structure including the time tag
    ///
    
    public enum ImuFields
    {
        GYRO,
        ACCEL,
        MAG
    }
    
    public DataRecord newImuOutput(String name, String localFrame, ImuFields... imuFields)
    {
        List<ImuFields> fields = Arrays.asList(imuFields);
        DataRecord imuData = newDataRecord(3);
        imuData.setName(name);
        imuData.setDefinition(getPropertyUri("ImuData"));
        
        // time stamp
        imuData.addComponent("time", newTimeStampIsoUTC());
        
        // angular rate vector
        if (fields.contains(ImuFields.GYRO))
        {
            Vector angRate = newAngularVelocityVector(null, localFrame, "deg/s");
            angRate.setDataType(DataType.FLOAT);
            imuData.addComponent("angRate", angRate);
        }
        
        // acceleration vector        
        if (fields.contains(ImuFields.ACCEL))
        {
            Vector accel = newAccelerationVector(null, localFrame, "m/s2");
            accel.setDataType(DataType.FLOAT);
            imuData.addComponent("accel", accel);
        }
        
        // magnetic field vector
        if (fields.contains(ImuFields.MAG))
        {
            Vector mag = newAngularVelocityVector(null, localFrame, "deg/s");
            mag.setDataType(DataType.FLOAT);
            imuData.addComponent("magField", mag);
        }
        
        return imuData;
    }
}
