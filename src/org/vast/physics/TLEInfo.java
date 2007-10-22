/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.physics;


/**
 * <p><b>Title:</b><br/>
 * TLEInfo
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Class for storing and retrieving two-line element data
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Mike Botts
 * @since 10/14/98
 * @version 1.0
 */
public class TLEInfo
{
    // Members
    private String satName; // satellite name
    private int satID; // satellite ID
    private double tleTime; // tle Time (julian)
    private double xmo; // Mean Anomaly
    private double xnodeo; // right ascension
    private double eo; // eccentricity
    private double omegao; // argument of the perigee
    private double xincl; // inclination
    private double xno; // revolutions per day
    private double bstar; // bstar
    private double previousTleTime; // time of previous tle (julianTime)
    private double nextTleTime; // time of next tle (julianTime)
    private int lineNumberInFile = 0; // if available, location in tle file
    private boolean lastEntry = false; // flag = true if tle is last listing in file
    private boolean firstEntry = false; // flag = true if tle is first listing in file


    public String getSatelliteName()
    {
        return satName;
    }


    public int getSatelliteID()
    {
        return satID;
    }


    public double getTleTime()
    {
        return tleTime;
    }


    public double getMeanAnomaly()
    {
        return xmo;
    }


    public double getRightAscension()
    {
        return xnodeo; // right ascension
    }


    public double getEccentricity()
    {
        return eo;
    }


    public double getArgumentOfPerigee()
    {
        return omegao; // argument of the perigee
    }


    public double getInclination()
    {
        return xincl; // inclination
    }


    public double getRevsPerDay()
    {
        return xno; // revolutions per day
    }


    public double getBStar()
    {
        return bstar; // bstar
    }


    public double getPreviousTleTime()
    {
        return previousTleTime;
    }


    public double getNextTleTime()
    {
        return nextTleTime;
    }


    public int getLineNumberInFile()
    {
        return lineNumberInFile;
    }


    public boolean isLastEntry()
    {
        return lastEntry;
    }


    public boolean isFirstEntry()
    {
        return firstEntry;
    }


    public void setSatelliteName(String satName)
    {
        this.satName = satName;
    }


    public void setSatelliteID(int satID)
    {
        this.satID = satID;
    }


    public void setTleTime(double tleTime)
    {
        this.tleTime = tleTime;
    }


    public void setMeanAnomaly(double value)
    {
        this.xmo = value;
    }


    public void setRightAscension(double value)
    {
        this.xnodeo = value; // right ascension
    }


    public void setEccentricity(double value)
    {
        this.eo = value;
    }


    public void setArgumentOfPerigee(double value)
    {
        this.omegao = value; // argument of the perigee
    }


    public void setInclination(double value)
    {
        this.xincl = value; // inclination
    }


    public void setRevsPerDay(double value)
    {
        this.xno = value; // revolutions per day
    }


    public void setBStar(double value)
    {
        this.bstar = value; // bstar
    }


    public void setPreviousTleTime(double julianTime)
    {
        this.previousTleTime = julianTime;
    }


    public void setNextTleTime(double julianTime)
    {
        this.nextTleTime = julianTime;
    }


    public void setLineNumberInFile(int line)
    {
        this.lineNumberInFile = line;
    }


    public void setLastEntryFlag(boolean flag)
    {
        lastEntry = flag;
    }


    public void setFirstEntryFlag(boolean flag)
    {
        firstEntry = flag;
    }
}
