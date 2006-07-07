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

package org.vast.util;


/**
 * <p><b>Title:</b><br/>
 * Message System
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO MessageHandler type description
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Apr 28, 2006
 * @version 1.0
 */
public class ExceptionSystem
{
    private static ExceptionHandler displayHandler;
    private static ExceptionHandler logHandler;
    public static boolean debug = true;
    
    
    static
    {
        displayHandler = new DefaultExceptionHandler();
        logHandler = new DefaultExceptionHandler();
    }
    

    public static void display(Throwable e)
    {
        if (displayHandler != null)
            displayHandler.handleException(e, debug);
    }
    
    
    public static void log(Throwable e)
    {
        if (logHandler != null)
            logHandler.handleException(e, debug);
    }


    public static void setDisplayHandler(ExceptionHandler displayHandler)
    {
        ExceptionSystem.displayHandler = displayHandler;
    }


    public static void setLogHandler(ExceptionHandler logHandler)
    {
        ExceptionSystem.logHandler = logHandler;
    }
}
