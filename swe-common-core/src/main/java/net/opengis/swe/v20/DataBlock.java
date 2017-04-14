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

import java.io.Serializable;

/**
 * <p>
 * Implementations of this class should be able to carry data
 * for any part of the data cluster defined in the DataDefinition
 * part of the SWE Common Data Model. It should provide access to any
 * data atom through primitive specific methods. Data should be
 * casted from one primitive to another when needed and possible.
 * In order to improve performance, no check needs to be enforced on
 * the index argument (i.e 0 < index < blockSize - 1), thus all
 * getXXXValue(int index) and setXXXValue(int index, ...) do not
 * need to guarantee their bahavior when an invalid index is used.
 * IndexOutOfBoundException can be thrown in some cases.  
 * </p>
 *
 * @author Alex Robin
 * @since Aug 14, 2005
 * */
public interface DataBlock extends Serializable
{
	public DataType getDataType();
	
	
	public DataType getDataType(int index);
	
	
	public int getAtomCount();
	
	
	public boolean getBooleanValue(int index);
	
	
	public byte getByteValue(int index);


	public short getShortValue(int index);


	public int getIntValue(int index);


	public long getLongValue(int index);


	public float getFloatValue(int index);


	public double getDoubleValue(int index);
	
	
	public String getStringValue(int index);


	public boolean getBooleanValue();
	
	
	public byte getByteValue();


	public short getShortValue();


	public int getIntValue();


	public long getLongValue();


	public float getFloatValue();


	public double getDoubleValue();
	
	
	public String getStringValue();


	public void setBooleanValue(int index, boolean value);
			
			
	public void setByteValue(int index, byte value);
	
	
	public void setShortValue(int index, short value);


	public void setIntValue(int index, int value);


	public void setLongValue(int index, long value);


	public void setFloatValue(int index, float value);


	public void setDoubleValue(int index, double value);
	
	
	public void setStringValue(int index, String value);


	public void setBooleanValue(boolean value);
	
	
	public void setByteValue(byte value);


	public void setShortValue(short value);


	public void setIntValue(int value);


	public void setLongValue(long value);


	public void setFloatValue(float value);


	public void setDoubleValue(double value);
	
	
	public void setStringValue(String value);
	
	
	public void resize(int size);
    
    
    public DataBlock copy();
    
    
    public DataBlock clone();
    
    
    public DataBlock renew();
    
    
    public Object getUnderlyingObject();
    
    
    public void setUnderlyingObject(Object obj);
}
