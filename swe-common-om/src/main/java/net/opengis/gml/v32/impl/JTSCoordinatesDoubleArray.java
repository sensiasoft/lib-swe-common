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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;


/**
 * <p>
 * Implementation of JTS coordinate sequence wrapping a double array
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Dec 23, 2014
 */
public class JTSCoordinatesDoubleArray implements CoordinateSequence
{
    double[] posList;
    int numDims = 2;
    
    
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
