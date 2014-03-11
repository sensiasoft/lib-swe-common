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

package org.vast.cdm.common;


public enum DataType
{
	BOOLEAN,
	BYTE,
	SHORT,
	INT,
	LONG,
	UBYTE,
	USHORT,
	UINT,
	ULONG,
	FLOAT,
	DOUBLE,
	ASCII_STRING,
	UTF_STRING,

	OTHER,
	DISCARD,
	MIXED;
	
	
	public boolean isIntegralType()
	{
	    if (this == BYTE)
	        return true;
	    if (this == UBYTE)
            return true;
	    if (this == SHORT)
            return true;
	    if (this == USHORT)
            return true;
	    if (this == INT)
            return true;
	    if (this == UINT)
            return true;
	    if (this == LONG)
            return true;
	    if (this == ULONG)
            return true;
	    return false;
	}
	
	
	public boolean isNumberType()
	{
	    if (isIntegralType())
	        return true;
	    if (this == FLOAT)
	        return true;
	    if (this == DOUBLE)
            return true;
	    return false;
	}
	
	
	public boolean isTextType()
    {
        if (this == ASCII_STRING)
            return true;
        if (this == UTF_STRING)
            return true;
        return false;
    }
}
