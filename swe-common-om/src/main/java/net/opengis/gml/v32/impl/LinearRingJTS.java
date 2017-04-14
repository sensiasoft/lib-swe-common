/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32.impl;

import com.vividsolutions.jts.geom.GeometryFactory;
import net.opengis.gml.v32.LinearRing;


/**
 * <p>
 * Implementation of GML LinearRing derived from JTS LinearRing class.
 * </p>
 *
 * @author Alex Robin
 * @since Dec 23, 2014
 */
public class LinearRingJTS extends com.vividsolutions.jts.geom.LinearRing implements LinearRing
{
    private static final long serialVersionUID = 4092205008825991782L;
    protected double[] posList;
    
    
    public LinearRingJTS(GeometryFactory jtsFactory, int numDims)
    {
        super(new JTSCoordinatesDoubleArray(numDims), jtsFactory);
    }


    @Override
    public double[] getPosList()
    {
        return posList;
    }


    @Override
    public void setPosList(double[] posList)
    {
        this.posList = posList;
        ((JTSCoordinatesDoubleArray)getCoordinateSequence()).setPosList(posList);
        this.geometryChanged();
    }    
    
    
    @Override
    public boolean isSetPosList()
    {
        return (posList != null);
    }
    
}
