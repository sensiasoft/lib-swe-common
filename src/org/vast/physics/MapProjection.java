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
 * MapProjection
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO MapProjection type description
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Oct 18, 2005
 * @version 1.0
 */
public class MapProjection
{

    public final static double [] LLAtoMerc(double lat, double lon, double alt)
    {
        double sinLat = Math.sin(lat);
        double y = 0.5 * Math.log((1 + sinLat) / (1 - sinLat));
        return new double [] {lon, y, alt};
    }
    
    
    public final static double [] LLAtoECF(double latitude, double longitude, double altitude, Datum datum)
    {
        if (datum == null)
            datum = new Datum();
        
        double a = datum.equatorRadius;
        double e2 = datum.e2;

        double sinLat = Math.sin(latitude);
        double cosLat = Math.cos(latitude);
        double N = a / Math.sqrt(1.0 - e2 * sinLat * sinLat);

        double x = (N + altitude) * cosLat * Math.cos(longitude);
        double y = (N + altitude) * cosLat * Math.sin(longitude);
        double z = (N * (1.0 - e2) + altitude) * sinLat;

        return new double [] {x, y, z};
    }


    public final static double [] ECFtoLLA(double x, double y, double z, Datum datum)
    {
        if (datum == null)
            datum = new Datum();
        
        // Method from Peter Dana
        double a = datum.equatorRadius;
        double b = datum.polarRadius;
        double longitude = actan.getactan(y,x);
        double ePrimeSquared = (a*a - b*b)/(b*b);
        double p = Math.sqrt(x*x + y*y);
        double theta = Math.atan((z*a)/(p*b));
        double sineTheta = Math.sin(theta);
        double cosTheta = Math.cos(theta);
        double e2 = datum.e2;
        double top = z + ePrimeSquared * b * sineTheta * sineTheta * sineTheta;
        double bottom = p - e2 * a * cosTheta * cosTheta * cosTheta;
        double geodeticLat = Math.atan(top/bottom);
        double sineLat = Math.sin(geodeticLat);
        double N = a / Math.sqrt( 1 - e2 * sineLat * sineLat);
        double altitude = (p / Math.cos(geodeticLat)) -  N;

        return new double [] {geodeticLat, longitude, altitude};
    }
}
