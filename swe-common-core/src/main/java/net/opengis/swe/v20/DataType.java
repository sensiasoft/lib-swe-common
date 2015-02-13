/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.swe.v20;


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
