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

package org.vast.math;


/**
 * <p><b>Title:</b><br/>
 * Vector
 * </p>
 *
 * <p><b>Description:</b><br/>
 * General N dimensions vector [x1,x2 ... ,xN].
 * Contians a set of generic linear algebra routines.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 29, 2005
 * @version 1.0
 */
public class Vector
{
    protected double [] coordinates;


    public Vector(int dimensions)
    {
        coordinates = new double[dimensions];
    }


    public Vector(double[] coordinates)
    {
        this.coordinates = coordinates;
    }


    public int getSize()
    {
        return coordinates.length;
    }


    public void setCoordinates(double [] coordinates)
    {
        this.coordinates = coordinates;
    }


    public void setCoordinate(int index, double value)
    {
        checkIndex(index);
        coordinates[index] = value;
    }


    public double getCoordinate(int index)
    {
        checkIndex(index);
        return coordinates[index];
    }


    public double getElement(int index)
    {
        return getCoordinate(index);
    }


    public double [] getCoordinates()
    {
        return coordinates;
    }


    public Vector copy()
    {
        Vector newVector = new Vector(coordinates.length);

        for (int i=0; i<coordinates.length; i++)
        {
            newVector.coordinates[i] = this.coordinates[i];
        }

        return newVector;
    }


    public void add(Vector b)
    {
        checkVector(b);
        for (int i=0; i<coordinates.length; i++)
        {
            this.coordinates[i] += b.coordinates[i];
        }
    }


    public void sub(Vector b)
    {
        checkVector(b);
        for (int i=0; i<coordinates.length; i++)
        {
            this.coordinates[i] -= b.coordinates[i];
        }
    }


    public double length()
    {
        double length2 = 0.0;

        for (int i=0; i<coordinates.length; i++)
        {
            length2 += coordinates[i] * coordinates[i];
        }

        return Math.sqrt(length2);
    }


    public void normalize()
    {
        double length = this.length();

        if (length == 0)
        {
            return;
        }

        for (int i=0; i<coordinates.length; i++)
        {
            coordinates[i] = coordinates[i] / length;
        }
    }


    public void normalize(double newLength)
    {
        normalize();
        scale(newLength);
    }


    public double distanceTo(Vector b)
    {
        Vector delta = this.copy();
        delta.sub(b);
        return delta.length();
    }


    public void negative()
    {
        for (int i=0; i<coordinates.length; i++)
        {
            coordinates[i] = -coordinates[i];
        }
    }


    public void scale(double s)
    {
        for (int i=0; i<coordinates.length; i++)
        {
            coordinates[i] *= s;
        }
    }


    public void scale(double [] scaleFactors)
    {
        checkIndex(scaleFactors.length-1);

        for (int i=0; i<coordinates.length; i++)
        {
            coordinates[i] *= scaleFactors[i];
        }
    }


    public void translate(Vector vector)
    {
        add(vector);
    }


    public double dot(Vector b)
    {
        checkVector(b);
        double dotProduct = 0.0;

        for (int i=0; i<coordinates.length; i++)
        {
            dotProduct += coordinates[i] * b.coordinates[i];
        }

        return dotProduct;
    }
    
    
    public static double dot(Vector a, Vector b)
    {
    	return Vector.dot(a.coordinates, b.coordinates);
    }
    
    
    public static double dot(double[] a, double[] b)
    {
    	if(a.length != b.length)
    		throw new IllegalArgumentException("Vectors must be the same length");
    	
    	double dotProduct = 0.0;

        for (int i=0; i<a.length; i++)
        {
            dotProduct += a[i] * b[i];
        }

        return dotProduct;
    }


    public String toString()
    {
        StringBuffer text = new StringBuffer("Vector: ");

        for (int i=0; i<coordinates.length; i++)
        {
            text.append(coordinates[i]);

            if (i < coordinates.length - 1)
                text.append(", ");
        }

        return text.toString();
    }


    private void checkIndex(int index)
    {
        if ((index < 0) || (index >= coordinates.length))
            throw new IndexOutOfBoundsException();
    }


    private void checkVector(Vector v)
    {
        if (coordinates.length != v.coordinates.length)
            throw new IllegalArgumentException("Vectors must have the same size");
    }


    /**
     * Helper method to add 2 vectors
     * @param a Vector
     * @param b Vector
     * @return Vector
     */
    public static Vector addVectors(Vector a, Vector b)
    {
        Vector c = a.copy();
        c.add(b);
        return c;
    }


    /**
     * Helper method to subtract 2 vectors
     * @param a Vector
     * @param b Vector
     * @return Vector
     */
    public static Vector subtractVectors(Vector a, Vector b)
    {
        Vector c = a.copy();
        c.sub(b);
        return c;
    }
}
