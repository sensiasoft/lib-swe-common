/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32.impl;


/**
 * <p>
 * Utility methods for implementing JTS based GML geometries
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
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
}
