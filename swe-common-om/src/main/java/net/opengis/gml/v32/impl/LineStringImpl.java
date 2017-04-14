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

import net.opengis.gml.v32.Envelope;
import net.opengis.gml.v32.LineString;


/**
 * POJO class for XML type LineStringType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public class LineStringImpl extends AbstractCurveImpl implements LineString
{
    private static final long serialVersionUID = -7975794768843147648L;
    protected double[] posList;
    
    
    @SuppressWarnings("unused")
    private LineStringImpl()
    {
    }
    
    
    public LineStringImpl(int numDims)
    {
        this.srsDimension = numDims;
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
        this.envelope = null;     
    }    
    
    
    @Override
    public boolean isSetPosList()
    {
        return (posList != null);
    }


    @Override
    public Envelope getGeomEnvelope()
    {
        if (envelope == null)
            envelope = addCoordinatesToEnvelope(envelope, posList, srsDimension);
            
        return envelope;
    }
}
