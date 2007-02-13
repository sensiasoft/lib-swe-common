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

package org.vast.sweCommon;

import java.util.ArrayList;


/**
 * <p><b>Title:</b>
 * Constraint
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Abstract object for constraints of a given field in SWE Common v1.0
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Feb 7, 2007
 * @version 1.0
 */
interface Constraint {}
public class Constraints extends ArrayList<Constraint>
{
    private static final long serialVersionUID = 8873758049042174380L;

    public class IntervalConstraint implements Constraint
    {
        public double min;
        public double max;
    }
    
    public class EnumConstraint implements Constraint
    {
        
    }
}
