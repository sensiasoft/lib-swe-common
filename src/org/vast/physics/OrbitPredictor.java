package org.vast.physics;

import java.io.IOException;
import java.text.ParseException;
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
public class OrbitPredictor
{
	protected TLEParser tleParser;
	protected SGP4Propagator propagator;
	
	
	public OrbitPredictor(String satName)
	{
		tleParser = new TLEParser("d:\\temp\\" + satName + ".txt");
		propagator = new SGP4Propagator();
	}
	
	
	/**
	 * Gets the satellite ECI state at a given point in time
	 * @param time in seconds past 01/01/1970 (unix time)
	 * @return
	 */
	public MechanicalState getECIState(double time)
	{
		try
		{
			TLEInfo tle = tleParser.readClosestTLE(time);
			System.out.println(DateTimeFormat.formatIso(tle.tleTime, 0));
			MechanicalState state = propagator.getECIOrbitalState(time, tle);
			return state;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Gets the satellite ECF state at a given point in time
	 * @param time in seconds past 01/01/1970 (unix time)
	 * @return
	 */
	public MechanicalState getECFState(double time)
	{
		MechanicalState state = getECIState(time);
		double eciX = state.linearPosition.x;
		double eciY = state.linearPosition.y;
		double eciZ = state.linearPosition.z;
		double eciVx = state.linearVelocity.x;
		double eciVy = state.linearVelocity.y;
		double eciVz = state.linearVelocity.z;
		
		// convert position
		double[] ecf = MapProjection.ECItoECF(time, eciX, eciY, eciZ, false);
		state.linearPosition.x = ecf[0];
		state.linearPosition.y = ecf[1];
		state.linearPosition.z = ecf[2];
				
		// convert velocity
		double[] ecfVel = MapProjection.ECItoECF(time, eciVx, eciVy, eciVz, true);
		state.linearVelocity.x = ecfVel[0];
		state.linearVelocity.y = ecfVel[1];
		state.linearVelocity.z = ecfVel[2];
		
		return state;
	}
	
	
	/**
	 * Gets the ECI trajectory ad an array of state object 
	 * @param startTime in seconds past 01/01/1970 (unix time)
	 * @param stopTime in seconds past 01/01/1970 (unix time)
	 * @param step step size in seconds
	 * @return
	 */
	public MechanicalState[] getECITrajectory(double startTime, double stopTime, double step)
	{
		int numPoints = (int) ((stopTime - startTime) / step);
		MechanicalState[] trajectory = new MechanicalState[numPoints];
		
		double time = startTime;
		for (int p = 0; p < numPoints; p++)
		{
			MechanicalState state = getECIState(time);
			trajectory[p] = state;
			time += step;
		}
		
		return trajectory;
	}
	
	
	/**
	 * Gets the ECF trajectory ad an array of state object 
	 * @param startTime in seconds past 01/01/1970 (unix time)
	 * @param stopTime in seconds past 01/01/1970 (unix time)
	 * @param step step size in seconds
	 * @return
	 */
	public MechanicalState[] getECFTrajectory(double startTime, double stopTime, double step)
	{
		int numPoints = (int) ((stopTime - startTime) / step);
		MechanicalState[] trajectory = new MechanicalState[numPoints];
		
		double time = startTime;
		for (int p = 0; p < numPoints; p++)
		{
			MechanicalState state = getECFState(time);
			trajectory[p] = state;				
			time += step;
		}
		
		return trajectory;
	}
	
	
	public static void main(String[] args)
	{
		try
		{
			OrbitPredictor predictor = new OrbitPredictor("spot-4");
			
			double startTime = DateTimeFormat.parseIso("2008-01-20T13:00:00Z");
			double stopTime = DateTimeFormat.parseIso("2008-01-21T13:00:00Z");
			
			MechanicalState[] trajectory = predictor.getECFTrajectory(startTime, stopTime, 60);
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
			}
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}
