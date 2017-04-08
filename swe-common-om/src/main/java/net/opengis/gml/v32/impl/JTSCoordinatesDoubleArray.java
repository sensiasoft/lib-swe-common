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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;


/**
 * <p>
 * Implementation of JTS coordinate sequence wrapping a double array
 * </p>
 *
 * @author Alex Robin
 * @since Dec 23, 2014
 */
public class JTSCoordinatesDoubleArray implements CoordinateSequence
{
    double[] posList;
    int numDims = 2;
    
    
    private JTSCoordinatesDoubleArray()
    {        
    }
    
    
    public JTSCoordinatesDoubleArray(int numDims)
    {
        this.numDims = numDims;
    }
    
    
    public void setNumDimensions(int numDims)
    {
        this.numDims = numDims;
    }
    
    
    public void setPosList(double[] posList)
    {
        this.posList = posList;
    }
    
    
    @Override
    public int getDimension()
    {
        return numDims;
    }


    @Override
    public final Coordinate getCoordinate(int i)
    {
        Coordinate coord = new Coordinate();
        getCoordinate(i, coord);
        return coord;
    }


    @Override
    public final Coordinate getCoordinateCopy(int i)
    {
        return getCoordinate(i);
    }


    @Override
    public final void getCoordinate(int i, Coordinate coord)
    {
        int index = i*numDims;
        coord.x = posList[index++];
        coord.y = posList[index++];
        coord.z = (numDims > 2) ? posList[index++] : Coordinate.NULL_ORDINATE;
    }


    @Override
    public final double getX(int i)
    {
        int index = i*numDims;
        return posList[index];
    }


    @Override
    public final double getY(int i)
    {
        int index = i*numDims + 1;
        return posList[index];
    }


    @Override
    public final double getOrdinate(int i, int ordinateIndex)
    {
        int index = i*numDims + ordinateIndex;
        return posList[index];
    }


    @Override
    public final int size()
    {
        if (posList == null)
            return 0;
        
        return posList.length / numDims;
    }


    @Override
    public final void setOrdinate(int i, int ordinateIndex, double value)
    {
        int index = i*numDims + ordinateIndex;
        posList[index] = value;
    }


    @Override
    public final Coordinate[] toCoordinateArray()
    {
        int numCoords = size();
        Coordinate[] coordArray = new Coordinate[numCoords];
        for (int i=0; i<numCoords; i++)
            coordArray[i] = getCoordinate(i);
        return coordArray;
    }


    @Override
    public final Envelope expandEnvelope(Envelope env)
    {
        int index = 0;
        for (int i=0; i<size(); i++)
        {
            env.expandToInclude(posList[index], posList[index+1]);
            index += numDims;
        }
        return env;
    }
    
    
    @Override
    public final JTSCoordinatesDoubleArray clone()
    {
        JTSCoordinatesDoubleArray newSeq = new JTSCoordinatesDoubleArray();
        newSeq.numDims = this.numDims;
        newSeq.posList = this.posList.clone();
        return newSeq;
    }

}
