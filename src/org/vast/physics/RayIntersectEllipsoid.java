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
 * Ray Intersect Ellipsoid
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Intersect the surface of an ellipsoid with a ray.
 *          Ellipsoid is our 'External Data'.  The ellipsoid
 *          is assumed to have its center at the origin.
 *          Three doubles are used to define the ellipsoid's
 *          intersections with the x,y, & z axes respectively.
 *          The intersecting ray should consist of a three-tuple
 *          defining the ray vertex, and another three-tuple
 *          defining the ray direction.
 *          NOTE:   When initializing InputKeys vector, the user
 *                  must declare them in the following order:
 *                  vertexX, vertexY, vertexZ, directionX, dirY, dirZ
 *          This is necessary because you can call this transform
 *          using any number of different Cartesian coordinate frames.
 *          The transform won't be able to figure out how to index
 *          the columns of the source node otherwise.
 *          The same holds true for output, except we're only worried
 *          about position, so the keys must be declared in this order:
 *                  vertexX, vertexY, vertexZ
 *          This illustrates an 'issue' with using the DataKey
 *          scheme in the first place.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Pete Conway, Tony Cook, Alexandre Robin
 * @dateMar 3, 1998
 * @version 1.0
 */
public class RayIntersectEllipsoid
{
	boolean foundFlag = false;
	double xRadius, yRadius, zRadius;


	//constructor looking for ellipsoid dimensions
	public RayIntersectEllipsoid(double xI, double yI, double zI)
	{
		xRadius = xI;
		yRadius = yI;
		zRadius = zI;
	}


	public RayIntersectEllipsoid(double[] intercept)
	{
		xRadius = intercept[0];
		yRadius = intercept[1];
		zRadius = intercept[2];
	}


	public RayIntersectEllipsoid(Datum datum)
	{
		xRadius = datum.equatorRadius;
		yRadius = datum.equatorRadius;
		zRadius = datum.polarRadius;
	}


	public double[] getIntersection(double[] vertex, double[] direction)
	{
		double alpha, beta, gamma, dscrm, scalar;//, den;
		foundFlag = false;

		double[] result = new double[3];

		double[] U = new double[3];
		double[] x = new double[3];
		double[] y = new double[3];

		//den = Math.sqrt(vertex[0] * vertex[0] + vertex[1] * vertex[1] + vertex[2] * vertex[2]);
		
		try
		{
			U[0] = direction[0];
			U[1] = direction[1];
			U[2] = direction[2];

			x[0] = U[0] / xRadius;
			x[1] = U[1] / yRadius;
			x[2] = U[2] / zRadius;

			y[0] = vertex[0] / xRadius;
			y[1] = vertex[1] / yRadius;
			y[2] = vertex[2] / zRadius;

			alpha = Vector.dot(x, x);
			beta = Vector.dot(x, y);
			gamma = Vector.dot(y, y) - 1.0;

			dscrm = beta * beta - alpha * gamma;

			if (dscrm < 0.0)
			{
				//            result[0] = result[1] = result[2] = 0.0;
				foundFlag = false;
				//            dscrm= -dscrm;
			}
			else if (gamma < 0.0)
			{
				scalar = (-beta + Math.sqrt(dscrm)) / alpha;
				result[0] = vertex[0] + (U[0] * scalar);
				result[1] = vertex[1] + (U[1] * scalar);
				result[2] = vertex[2] + (U[2] * scalar);
				foundFlag = true;
			}
			else if (gamma == 0.0)
			{
				result[0] = vertex[0];
				result[1] = vertex[1];
				result[2] = vertex[2];
				foundFlag = true;
			}
			else if (beta < 0.0)
			{
				scalar = (-beta - Math.sqrt(dscrm)) / alpha;
				result[0] = vertex[0] + (U[0] * scalar);
				result[1] = vertex[1] + (U[1] * scalar);
				result[2] = vertex[2] + (U[2] * scalar);
				foundFlag = true;
			}
			else
			{
				result[0] = result[1] = result[2] = 0.0;
				foundFlag = false;
			}

		}
		catch (ArithmeticException ex)
		{
			foundFlag = false;
		}

		if (!foundFlag)
			throw new IllegalStateException("No Intersection Found");
		
		return result;
	}
}
