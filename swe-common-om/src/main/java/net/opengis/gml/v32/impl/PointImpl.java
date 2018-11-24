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

import java.text.DecimalFormat;
import net.opengis.gml.v32.Envelope;
import net.opengis.gml.v32.Point;


/**
 * POJO class for XML type PointType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public class PointImpl extends AbstractGeometryImpl implements Point
{
    private static final long serialVersionUID = 3783995735670295600L;
    protected double[] pos;
    
    
    @SuppressWarnings("unused")
    private PointImpl()
    {
    }
    
    
    public PointImpl(int numDims)
    {
        this.srsDimension = numDims;
    }
    
    
    /**
     * Gets the pos property
     */
    @Override
    public double[] getPos()
    {
        return pos;
    }
    
    
    /**
     * Checks if pos is set
     */
    @Override
    public boolean isSetPos()
    {
        return (pos != null);
    }
    
    
    /**
     * Sets the pos property
     */
    @Override
    public void setPos(double[] pos)
    {
        this.pos = pos;
        this.envelope = null;
        this.srsDimension = pos.length;
    }
    
    
    @Override
    public Envelope getGeomEnvelope()
    {
        if (envelope == null)
            envelope = addCoordinatesToEnvelope(envelope, pos, srsDimension);
            
        return envelope;
    }
    
    
    @Override
    public String toString()
    {
        DecimalFormat formatter = new DecimalFormat(GMLFactory.COORDINATE_FORMAT);
        StringBuilder buf = new StringBuilder(32);
        
        buf.append("POINT (");
        
        if (pos != null)
        {
            int count = 0;
            for (double c: pos)
            {
                buf.append(formatter.format(c));
                count++;
                if (count < pos.length)
                    buf.append(' ');
            }
        }
        
        buf.append(')');
        
        return buf.toString();
    }
}
