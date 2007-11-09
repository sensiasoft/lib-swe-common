/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.

 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.cdm.common;


public class DataTypeObjects
{
	
	public static DataTypeObject getFromType(DataType dataType)
	{
		switch(dataType)
		{
			case BYTE:
				return new Byte();
				
			case UBYTE:
				
			case SHORT:
			case USHORT:
			
			case INT:
			case UINT:
				
			case FLOAT:
				
			case DOUBLE:
				
			default:
				throw new IllegalArgumentException();
		}
	}
	
	
	public abstract static class DataTypeObject
	{
		public abstract DataType getDataType();
		public abstract byte getAsByte(Object array, int index);
		public abstract short getAsShort(Object array, int index);
		public abstract int getAsInt(Object array, int index);
		public abstract long getAsLong(Object array, int index);
		public abstract float getAsFloat(Object array, int index);
		public abstract double getAsDouble(Object array, int index);
		public abstract void setAsByte(Object array, int index, byte val);
		public abstract void setAsShort(Object array, int index, short val);
		public abstract void setAsInt(Object array, int index, int val);
		public abstract void setAsLong(Object array, int index, long val);
		public abstract void setAsFloat(Object array, int index, float val);
		public abstract void setAsDouble(Object array, int index, double val);
	}
	
	
	/*
	 * Data Type Byte
	 */
	public static class Byte extends DataTypeObject
	{
		public DataType getDataType()
		{
			return DataType.BYTE;
		}

		@Override
		public byte getAsByte(Object array, int index)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double getAsDouble(Object array, int index)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float getAsFloat(Object array, int index)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getAsInt(Object array, int index)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getAsLong(Object array, int index)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public short getAsShort(Object array, int index)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setAsByte(Object array, int index, byte val)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setAsDouble(Object array, int index, double val)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setAsFloat(Object array, int index, float val)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setAsInt(Object array, int index, int val)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setAsLong(Object array, int index, long val)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setAsShort(Object array, int index, short val)
		{
			// TODO Auto-generated method stub
			
		}
	}
	
	
	/*
	 * Data Type UnsignedByte
	 */
	public static class UnsignedByte extends DataTypeObject
	{
		public DataType getDataType()
		{
			return DataType.UBYTE;
		}

		@Override
		public byte getAsByte(Object array, int index)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double getAsDouble(Object array, int index)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float getAsFloat(Object array, int index)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getAsInt(Object array, int index)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getAsLong(Object array, int index)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public short getAsShort(Object array, int index)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setAsByte(Object array, int index, byte val)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setAsDouble(Object array, int index, double val)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setAsFloat(Object array, int index, float val)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setAsInt(Object array, int index, int val)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setAsLong(Object array, int index, long val)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setAsShort(Object array, int index, short val)
		{
			// TODO Auto-generated method stub
			
		}		
	}
}
