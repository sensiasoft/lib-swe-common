package org.vast.physics;

import org.vast.math.Vector3d;
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
public class HelioSyncOrbitPredictor extends AbstractOrbitPredictor
{
	protected final static double TWO_PI = 2 * Math.PI;
	protected final static double omegaSun = TWO_PI / (365.24219 * 86400);
	
	protected double ascNodeTime;
	protected double ascNodeLong;
	protected int orbitCycle;
	protected int numOrbits;
	protected double nodalPeriod;
	protected double keplerPeriod;
	protected double orbitRadius;
	protected double orbitInclination;
	
	
	public HelioSyncOrbitPredictor(double ascNodeTime, double ascNodeLong, int orbitCycle, int numOrbits)
	{
		this.ascNodeTime = ascNodeTime;
		this.ascNodeLong = ascNodeLong;
		this.orbitCycle = orbitCycle;
		this.numOrbits = numOrbits;
		
		this.nodalPeriod = ((double)orbitCycle * 86400) / (double)numOrbits;
		this.orbitRadius = 7200000;
		this.orbitInclination = 98.7 * Math.PI/180;
	}
	
	
	/* (non-Javadoc)
	 * @see org.vast.physics.OrbitPredictor#getECIState(double)
	 */
	public MechanicalState getECIState(double time)
	{
		Vector3d ecfPos = new Vector3d(orbitRadius, 0.0, 0.0);
		double dT = time - ascNodeTime;
		
		// pos on orbit plane
		ecfPos.rotateZ(TWO_PI * dT / nodalPeriod);
		
		// inclination
		ecfPos.rotateX(orbitInclination);
		
		// heliosynchronous rotation
		ecfPos.rotateZ(ascNodeLong + dT * omegaSun);
		
		MechanicalState state = new MechanicalState();
		state.julianTime = time;
		state.linearPosition = ecfPos;
		state.linearVelocity = new Vector3d();
		
		return state;
	}
	
	
	public static void main(String[] args)
	{
		try
		{
			// SPOT
			double ascNodeDate = 00; // s
			double ascNodeTime = ascNodeDate;// + 44967.551; // s
			double ascNodeLong = 0;//330.24 * Math.PI/180; // rad
			int orbitCycle = 26; // d
			int numOrbits = 369;
			
			OrbitPredictor predictor = new HelioSyncOrbitPredictor(ascNodeTime, ascNodeLong, orbitCycle, numOrbits);
			
			double startTime = DateTimeFormat.parseIso("1970-01-01T00:00:00Z");
			double stopTime = DateTimeFormat.parseIso("1971-01-01T04:00:00Z");
			MechanicalState[] trajectory = predictor.getECITrajectory(startTime, stopTime, 6087.804878);
						
			for (int p = 0; p < trajectory.length; p++)
			{
				System.out.println("Time: " + DateTimeFormat.formatIso(trajectory[p].julianTime, 0));
				
				System.out.println("ECF Position (m): " + 
						           trajectory[p].linearPosition.x + "," +
						           trajectory[p].linearPosition.y + "," +
						           trajectory[p].linearPosition.z);
				
//				System.out.println("ECF Velocity (m/s): " + 
//				           trajectory[p].linearVelocity.x + "," +
//				           trajectory[p].linearVelocity.y + "," +
//				           trajectory[p].linearVelocity.z);
				
				System.out.println();
				
				// We could also compute nadir pointing attitude
				// see org.vast.physics.NadirPointing
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}


	public double getCycleInDays()
	{
		return this.orbitCycle;
	}
}
