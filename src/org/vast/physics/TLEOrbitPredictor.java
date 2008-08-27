/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.

 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.physics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.vast.util.DateTimeFormat;


/**
 * <p><b>Title:</b><br/>
 * OrbitPredictor
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Helper class to compute satellite orbit position/velocity
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @since Feb 25, 2003
 * @version 1.0
 */
public class TLEOrbitPredictor extends AbstractOrbitPredictor
{
	protected TLEParser tleParser;
	protected SGP4Propagator propagator;
	
	
	public TLEOrbitPredictor(String tleFile)
	{
		tleParser = new TLEParser(tleFile);
		propagator = new SGP4Propagator();
	}
	
	
	/* (non-Javadoc)
	 * @see org.vast.physics.OrbitPredictor#getECIState(double)
	 */
	public MechanicalState getECIState(double time)
	{
		try
		{
			TLEInfo tle = tleParser.readClosestTLE(time);			
			MechanicalState state = propagator.getECIOrbitalState(time, tle);			
			return state;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static void main(String[] args)
	{
		try
		{
			OrbitPredictor predictor = new TLEOrbitPredictor("spot-4");
			
			double startTime = DateTimeFormat.parseIso("2008-01-17T13:11:13Z");
			double stopTime = DateTimeFormat.parseIso("2008-01-17T15:11:00Z");
			MechanicalState[] trajectory = predictor.getECFTrajectory(startTime, stopTime, 60);
			
			BufferedWriter writer = new BufferedWriter(new FileWriter("d:\\temp\\spot4.swe"));
			writer.write(Integer.toString(trajectory.length) + ",");
			
			for (int p = 0; p < trajectory.length; p++)
			{
				System.out.println("Time: " + DateTimeFormat.formatIso(trajectory[p].julianTime, 0));
				
				System.out.println("ECF Position (m): " + 
						           trajectory[p].linearPosition.x + "," +
						           trajectory[p].linearPosition.y + "," +
						           trajectory[p].linearPosition.z);
				
				System.out.println("ECF Velocity (m/s): " + 
				           trajectory[p].linearVelocity.x + "," +
				           trajectory[p].linearVelocity.y + "," +
				           trajectory[p].linearVelocity.z);
				
				System.out.println();
				
				writer.write(Double.toString(trajectory[p].linearPosition.x) + ",");
				writer.write(Double.toString(trajectory[p].linearPosition.y) + ",");
				writer.write(Double.toString(trajectory[p].linearPosition.z) + ",\n");
				
				// We could also compute nadir pointing attitude
				// see org.vast.physics.NadirPointing
			}
			
			writer.flush();
			writer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}
