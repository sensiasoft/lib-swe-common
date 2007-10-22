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

package org.vast.math;

public class actan
{
    public actan()
    {    	
    }        
    

    public static double getactan(double sinx, double cosx)
    {

    double pi = 3.141592653589793238;
    //double dtr = pi / 180.;
    double pi2 = pi / 2;
    double tpi = pi * 2;
    double thpi = pi * 1.5;
    //double ret_val = 0.;
    //double temp=0.;

    if (cosx == 0.){
        if (sinx == 0.) return 0;
        if (sinx < 0.) return thpi;
        return pi2;  //  cosx==0 && sinx>0
    }

    if (cosx > 0.){
        if (sinx == 0.) return 0;
        if (sinx > 0.)  return(Math.atan(sinx/cosx));
        return(tpi+Math.atan(sinx/cosx));  //  cosx>0 && sinx <0
    }

    if(cosx < 0.0)
        return(pi+Math.atan(sinx/cosx));

//    System.out.println("Error:  actan reached impossible case");
//    System.out.println("sinx, cosx ==> " + sinx + ", " + cosx);
    return(-9999.0);
    }
}

