/***************************************************************
 (c) Copyright 2007, University of Alabama in Huntsville (UAH)
 ALL RIGHTS RESERVED

 This software is the property of UAH.
 It cannot be duplicated, used, or distributed without the
 express written consent of UAH.

 This software developed by the Vis Analysis Systems Technology
 (VAST) within the Earth System Science Lab under the direction
 of Mike Botts (mike.botts@atmos.uah.edu)
 ***************************************************************/

package org.vast.util;

import java.io.Serializable;

/**
 * <p>
 * Class for storing the definition of a temporal domain.
 * This can include a base time, time bias (deviation from base time),
 * time step, and lead/lag time deltas.
 * </p>
 *
 * @author Tony Cook, Mike Botts, Alexandre Robin
 * @since Nov 15, 2005
 * */
public class TimeExtent implements Serializable
{
    private static final long serialVersionUID = -5475380061967935208L;
    public static final double NOW_ACCURACY = 1000.;    
    public static final double UNKNOWN = Double.MAX_VALUE;
    public static final double NOW = Double.MIN_VALUE;
    
    protected double baseTime = Double.NaN;
    protected double timeBias = 0;
    protected double timeStep = 0;
    protected double leadTimeDelta = 0;
    protected double lagTimeDelta = 0;
    protected boolean baseAtNow = false;  // if true baseTime is associated to machine clock
    protected boolean endNow = false;     // if true stopTime is associated to machine clock
    protected boolean beginNow = false;   // if true startTime is associated to machine clock
    protected int timeZone = 0;
    
    
    public static TimeExtent getNowInstant()
    {
        TimeExtent time = new TimeExtent();
        time.setBaseAtNow(true);
        return time;
    }
    
    
    public static TimeExtent getPeriodStartingNow(double stopTime)
    {
        TimeExtent time = new TimeExtent();
        time.setBeginNow(true);
        time.setStopTime(stopTime);
        return time;
    }
    
    
    public static TimeExtent getPeriodEndingNow(double startTime)
    {
        TimeExtent time = new TimeExtent();
        time.setStartTime(startTime);
        time.setEndNow(true);
        return time;
    }
    
    
    public TimeExtent()
    {        
    }    


    public TimeExtent(double baseJulianTime)
    {
        this.baseTime = baseJulianTime;
    }
    
    
    public TimeExtent(double startTime, double stopTime)
    {
        setStartTime(startTime);
        setStopTime(stopTime);
    }
    
    
    public TimeExtent copy()
    {
        TimeExtent timeExtent = new TimeExtent();
        
        timeExtent.baseTime = this.getBaseTime();
        timeExtent.timeBias = this.timeBias;
        timeExtent.timeStep = this.timeStep;
        timeExtent.leadTimeDelta = this.leadTimeDelta;
        timeExtent.lagTimeDelta = this.lagTimeDelta;
        timeExtent.baseAtNow = this.baseAtNow;
        timeExtent.endNow = this.endNow;
        timeExtent.beginNow = this.beginNow;
        
        return timeExtent;
    }


    public TimeExtent(double baseJulianTime, double timeBiasSeconds, double timeStepSeconds, double leadTimeDeltaSeconds, double lagTimeDeltaSeconds)
    {

        this.baseTime = baseJulianTime;
        this.timeBias = timeBiasSeconds;
        this.timeStep = timeStepSeconds;
        this.leadTimeDelta = Math.abs(leadTimeDeltaSeconds);
        this.lagTimeDelta = Math.abs(lagTimeDeltaSeconds);
    }


    public void setBaseTime(double baseJulianTime)
    {
        this.baseTime = baseJulianTime;
    }


    public void setTimeBias(double seconds)
    {
        this.timeBias = seconds;
    }


    public void setTimeStep(double seconds)
    {
        this.timeStep = seconds;
    }


    public void setLeadTimeDelta(double seconds)
    {
        this.leadTimeDelta = Math.abs(seconds);
    }


    public void setLagTimeDelta(double seconds)
    {
        this.lagTimeDelta = Math.abs(seconds);
    }


    public void setDeltaTimes(double leadDeltaSeconds, double lagDeltaSeconds)
    {
        this.leadTimeDelta = Math.abs(leadDeltaSeconds);
        this.lagTimeDelta = Math.abs(lagDeltaSeconds);
    }


    /**
     * To get baseTime without bias applied.
     * If baseAtNow is set, this retrieves the system's current time
     * @return base time as julian time in seconds (1970 based)
     */
    public double getBaseTime()
    {
        if (baseAtNow)
            return getNow();
        else
            return baseTime;
    }


    /**
     * To get baseTime or absTime with bias applied
     * @return base time + bias as julian time in seconds (1970 based)
     */
    public double getAdjustedTime()
    {
        return (getBaseTime() + timeBias);
    }


    public double getTimeBias()
    {
        return timeBias;
    }


    public double getTimeStep()
    {
        return timeStep;
    }


    public double getLeadTimeDelta()
    {
        return leadTimeDelta;
    }


    public double getLagTimeDelta()
    {
        return lagTimeDelta;
    }


    public double getTimeRange()
    {
        return (getAdjustedLeadTime() - getAdjustedLagTime());
    }


    public double getAdjustedLeadTime()
    {
        if (endNow)
            return getNow() + timeBias;
        else
            return (getBaseTime() + timeBias + leadTimeDelta);
    }


    public double getAdjustedLagTime()
    {
        if (beginNow)
            return getNow() + timeBias;
        else
            return (getBaseTime() + timeBias - lagTimeDelta);
    }
    
    
    public boolean isBaseAtNow()
    {
        return baseAtNow;
    }


    public void setBaseAtNow(boolean baseAtNow)
    {
        this.baseAtNow = baseAtNow;
    }


    public boolean isBeginNow()
    {
        return beginNow;
    }


    public void setBeginNow(boolean beginNow)
    {
        this.beginNow = beginNow;
    }


    public boolean isEndNow()
    {
        return endNow;
    }


    public void setEndNow(boolean endNow)
    {
        this.endNow = endNow;
    }


    /**
     * @return number of full time steps
     */
    public int getNumberOfSteps()
    {
        if (NumberUtils.ulpEquals(timeStep, 0.0))
            return 1;
        else
            return (int) ((getAdjustedLeadTime() - getAdjustedLagTime()) / timeStep);
    }
    
    
    /**
     * Calculates times based on current time settings, always assuring
     * that both endpoints are included even if an uneven time step occurs
     * at the end
     * @return time grid spliting the time period evenly
     */
    public double[] getTimes()
    {
        double time = getAdjustedLeadTime();
        double lagTime = getAdjustedLagTime();

        // if step is 0 returns two extreme points
        if (NumberUtils.ulpEquals(timeStep, 0.0))
            return new double[] {time, lagTime};
            
        double timeRange = Math.abs(time - lagTime);
        double remainder = timeRange % timeStep;
        int steps = (int) (timeRange / timeStep) + 1;       

        double[] times;
        if (!NumberUtils.ulpEquals(remainder, 0.0))
        {
            times = new double[steps + 1];
            times[steps] = lagTime;
        }
        else
            times = new double[steps];

        for (int i = 0; i < steps; i++)
            times[i] = time - i * timeStep;
        
        return times;
    }


    @Override
    public String toString()
    {
        String tString = new String("TimeExtent:");
        tString += "\n  baseTime = " + (baseAtNow ? "now" : baseTime);
        tString += "\n  timeBias = " + timeBias;
        tString += "\n  timeStep = " + timeStep;
        tString += "\n  leadTimeDelta = " + leadTimeDelta;
        tString += "\n  lagTimeDelta = " + lagTimeDelta;
        return tString;
    }


    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        	return false;
        
    	if (!(obj instanceof TimeExtent))
        	return false;
    	
    	return equals((TimeExtent)obj);
    }
    
    
    /**
     * Checks if time extents are equal (no null check)
     * (i.e. stop=stop AND start=start)
     * @param timeExtent
     * @return true if time extents are equal
     */
    public boolean equals(TimeExtent timeExtent)
    {
    	if (!baseAtNow)
    	{
    	   	if (!NumberUtils.ulpEquals(this.getAdjustedLagTime(), timeExtent.getAdjustedLagTime()) &&
	    	    !(this.isBeginNow() && timeExtent.isBeginNow()))
	            return false;
	        
	        if (!NumberUtils.ulpEquals(this.getAdjustedLeadTime(), timeExtent.getAdjustedLeadTime()) &&
	    	    !(this.isEndNow() && timeExtent.isEndNow()))
	            return false;
    	}
    	else
    	{
    		if (!timeExtent.isBaseAtNow())
    			return false;
    		
    		if (!NumberUtils.ulpEquals(this.getLagTimeDelta(), timeExtent.getLagTimeDelta()))
    			return false;
    		
    		if (!NumberUtils.ulpEquals(this.getLeadTimeDelta(), timeExtent.getLeadTimeDelta()))
    			return false;
    	}
    	
    	if (!NumberUtils.ulpEquals(this.getTimeBias(), timeExtent.getTimeBias()))
            return false;
    	
    	if (!NumberUtils.ulpEquals(this.getTimeZone(), timeExtent.getTimeZone()))
            return false;
        
        return true;
    }
    
    
    @Override
    public int hashCode()
    {
        int prime = 31;
        int hash = 1;
        
        hash = prime*hash + (baseAtNow ? 0 : 1);
        hash = prime*hash + Double.hashCode(getTimeBias());
        hash = prime*hash + getTimeZone();
        
        if (!baseAtNow)
        {
            hash = isBeginNow() ? hash : prime*hash + Double.hashCode(getAdjustedLagTime());
            hash = isEndNow() ? hash : prime*hash + Double.hashCode(getAdjustedLeadTime());
        }
        else
        {
            hash = prime*hash + Double.hashCode(getLagTimeDelta());
            hash = prime*hash + Double.hashCode(getLeadTimeDelta());
        }
        
        return hash;
    }
    
    
    /**
     * Checks if this TimeExtent contains the given time
     * @param time
     * @return true if it contains the given time point
     */
    public boolean contains(double time)
    {
    	double thisLag = this.getAdjustedLagTime();
        double thisLead = this.getAdjustedLeadTime();
        
        if (time < thisLag)
        	return false;
        
        if (time > thisLead)
        	return false;
        
        return true;
    }
    
    
    /**
     * Checks if this TimeExtent contains the given TimeExtent
     * @param timeExtent
     * @return true if it contains the given TimeExtent
     */
    public boolean contains(TimeExtent timeExtent)
    {
        double thisLag = this.getAdjustedLagTime();
        double thisLead = this.getAdjustedLeadTime();
        double otherLag = timeExtent.getAdjustedLagTime();
        double otherLead = timeExtent.getAdjustedLeadTime();
        
        if (otherLag < thisLag)
            return false;
        
        if  (otherLag > thisLead)
            return false;
        
        if (otherLead < thisLag)
            return false;        
        
        if (otherLead > thisLead)
            return false;
        
        return true;
    }
    
    
    /**
     * Checks if this timeExtent intersects the given timeExtent
     * @param timeExtent
     * @return true if both TimeExtents intersects
     */
    public boolean intersects(TimeExtent timeExtent)
    {
        double thisLag = this.getAdjustedLagTime();
        double thisLead = this.getAdjustedLeadTime();
        double otherLag = timeExtent.getAdjustedLagTime();
        double otherLead = timeExtent.getAdjustedLeadTime();
        
        if (otherLag > thisLag && otherLag < thisLead)
            return true;
        
        if (otherLead > thisLag && otherLead < thisLead)
            return true;
        
        if (otherLag <= thisLag && otherLead >= thisLead)
            return true;
        
        return false;
    }
    
    
    /**
     * Check if time is null (i.e. baseTime is not set)
     * @return true if no base time has been set
     */
    public boolean isNull()
    {
        return (Double.isNaN(baseTime) && !baseAtNow && !beginNow && !endNow);
    }
    
    
    /**
     * Check if this is a single point in time
     * @return true if this is a time instant
     */
	public boolean isTimeInstant()
	{
        if (!NumberUtils.ulpEquals(leadTimeDelta, 0.0))
            return false;
        
        if (!NumberUtils.ulpEquals(lagTimeDelta, 0.0))
            return false;
        
        if (!baseAtNow && (beginNow || endNow))
            return false;
        
        return true;
	}
    
    
    /**
     * Resets all variables so that extent is null (i.e. unset)
     */
    public void nullify()
    {
        baseTime = Double.NaN;
        timeBias = 0;
        timeStep = 0;
        leadTimeDelta = 0;
        lagTimeDelta = 0;
        baseAtNow = false;
        endNow = false;
        beginNow = false;
    }
    
    
    /**
     * Resizes this extent so that it contains the given time value
     * @param t time value (MUST be in same reference frame as the extent)
     */
    public void resizeToContain(double t)
    {
        if (isNull())
        {
            baseTime = t;
            timeBias = 0;
            return;
        }    
        
        double adjBaseTime = getAdjustedTime();
        if (t > getAdjustedLeadTime())
            leadTimeDelta = t - adjBaseTime;
        else if (t < getAdjustedLagTime())
            lagTimeDelta = adjBaseTime - t; 
    }
    
    
    /**
     * Return latest value for now. This would return a new 'now' value
     * only if previous call was made more than NOW_ACCURACY seconds ago.
     */
    private double now = 0;
    private double getNow()
    {
        double exactNow = System.currentTimeMillis()/1000.;
        if (exactNow - now > NOW_ACCURACY)
            now = exactNow;        
        return now;
    }
    
    
    /**
     * Helper method to get start time
     * @return start time as julian time in seconds (1970 based)
     */
    public double getStartTime()
    {
        return getAdjustedLagTime();
    }
    
    
    /**
     * Helper method to set start time
     * @param startTime start time as julian time in seconds (1970 based)
     */
    public void setStartTime(double startTime)
    {
        beginNow = false;
        
        if (Double.isNaN(baseTime) || baseAtNow)
        {
            baseTime = startTime;
            lagTimeDelta = 0.0;
            baseAtNow = false;
        }
        
        else if (startTime > baseTime)
        {
            double stopTime = baseTime + leadTimeDelta;
            baseTime = startTime;
            leadTimeDelta = Math.max(0.0, stopTime - baseTime);
            lagTimeDelta = 0.0;
        }
        
        else
        {
            lagTimeDelta = baseTime - startTime;
        }
    }


    /**
     * Helper method to get stop time
     * @return stop time as julian time in seconds (1970 based)
     */
    public double getStopTime()
    {
        return getAdjustedLeadTime();
    }
    
    
    /**
     * Helper method to set stop time
     * @param stopTime stop time as julian time in seconds (1970 based)
     */
    public void setStopTime(double stopTime)
    {
        endNow = false;
        
        if (Double.isNaN(baseTime) || baseAtNow)
        {
            baseTime = stopTime;
            leadTimeDelta = 0.0;
            baseAtNow = false;
        }
        
        else if (stopTime < baseTime)
        {
            double startTime = baseTime - lagTimeDelta;
            baseTime = stopTime;
            lagTimeDelta = Math.max(0.0, baseTime - startTime);
            leadTimeDelta = 0.0;
        }
        
        else
        {
            leadTimeDelta = stopTime - baseTime;
        }
    }

    
    public String getIsoString(int zone)
    {
        DateTimeFormat timeFormat = new DateTimeFormat();
        
        if (isTimeInstant())
        {
            String time = baseAtNow ? "now" : timeFormat.formatIso(getBaseTime(), zone);
            return time;
        }
        else
        {
            String start = beginNow ? "now" : timeFormat.formatIso(getStartTime(), zone);
            String stop = endNow ? "now" : timeFormat.formatIso(getStopTime(), zone);
            return start + "/" + stop;
        }
    }


    public int getTimeZone()
    {
        return timeZone;
    }


    public void setTimeZone(int timeZone)
    {
        this.timeZone = timeZone;
    }
}
