
package org.vast.physics;


/**
 * <p><b>Title:</b><br/>
 * Orbit Predictor
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Interface to predict satellite trajectory based on time
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 21 avr. 08
 * @version 1.0
 */
public interface OrbitPredictor
{

	/**
	 * Gets the satellite ECI state at a given point in time
	 * @param time in seconds past 01/01/1970 (unix time)
	 * @return
	 */
	public abstract MechanicalState getECIState(double time);


	/**
	 * Gets the satellite ECF state at a given point in time
	 * @param time in seconds past 01/01/1970 (unix time)
	 * @return
	 */
	public abstract MechanicalState getECFState(double time);


	/**
	 * Gets the ECI trajectory ad an array of state object 
	 * @param startTime in seconds past 01/01/1970 (unix time)
	 * @param stopTime in seconds past 01/01/1970 (unix time)
	 * @param step step size in seconds
	 * @return
	 */
	public abstract MechanicalState[] getECITrajectory(double startTime, double stopTime, double step);


	/**
	 * Gets the ECF trajectory ad an array of state object 
	 * @param startTime in seconds past 01/01/1970 (unix time)
	 * @param stopTime in seconds past 01/01/1970 (unix time)
	 * @param step step size in seconds
	 * @return
	 */
	public abstract MechanicalState[] getECFTrajectory(double startTime, double stopTime, double step);

}