/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import net.opengis.gml.v32.AbstractGeometry;
import com.vividsolutions.jts.geom.Geometry;


/**
 * <p>
 * Utility methods for implementing JTS based GML geometries
 * </p>
 *
 * @author Alex Robin
 * @since Dec 23, 2014
 */
public class JTSUtils
{
    public final static String DEFAULT_CRS_URI_PREFIX = "http://www.opengis.net/def/crs/EPSG/0/";
    
    
    public static String getSrsNameFromSRID(int SRID)
    {
        return DEFAULT_CRS_URI_PREFIX + SRID;
    }
    
    
    public static int getSRIDFromSrsName(String srsName)
    {
        char c;
        int codeIndex = srsName.length()-1;
        
        while (codeIndex >= 0)
        {
            c = srsName.charAt(codeIndex);
            if (!Character.isDigit(c))
                break;
            codeIndex--;
        }
        
        codeIndex++;
        if (codeIndex < srsName.length())
            return Integer.parseInt(srsName.substring(codeIndex));
        else
            return 0;
    }
    
    
    public static Geometry getAsJTSGeometry(AbstractGeometry geom)
    {
        if (geom instanceof Geometry)
            return (Geometry)geom;
        
        throw new IllegalStateException("Conversion from pure GML implementation to JTS not implemented yet");
    }
}
