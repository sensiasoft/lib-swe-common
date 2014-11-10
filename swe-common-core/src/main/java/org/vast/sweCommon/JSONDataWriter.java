/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SWE Common Data Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sweCommon;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import net.opengis.swe.v20.BlockComponent;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.RangeComponent;
import net.opengis.swe.v20.ScalarComponent;
import net.opengis.swe.v20.Vector;
import org.vast.data.AbstractArrayImpl;
import org.vast.util.WriterException;


/**
 * <p>
 * Writes SWE data stream in JSON format
 * </p>
 *
 * <p>Copyright (c) 2014</p>
 * @author Alexandre Robin
 * @since Nov 2, 2014
 * @version 1.0
 */
public class JSONDataWriter extends AbstractDataWriter
{
	protected Writer jsonWriter;
	protected String namespace;
	protected String prefix;
	protected int prevStackSize = 0;
	protected boolean pretty;
	protected StringBuffer indent = new StringBuffer();
	

	public JSONDataWriter()
	{	    
	}
	
	
	@Override
    public void setOutput(OutputStream outputStream) throws IOException
    {
	    jsonWriter = new OutputStreamWriter(outputStream);
    }
	
	
	@Override
    public void close() throws IOException
    {
        try
        {
            jsonWriter.flush();
            jsonWriter.close();
        }
        catch (IOException e)
        {
            throw new WriterException(STREAM_ERROR, e);
        }
    }
    
    
    @Override
    public void flush() throws IOException
    {
        try
        {
            jsonWriter.flush();
        }
        catch (IOException e)
        {
            throw new WriterException(STREAM_ERROR, e);
        }
    }
	
	
    @Override
	public void write(OutputStream outputStream) throws IOException
	{
		try
        {
		    super.write(outputStream);
		    closeElements();
        }
        catch (Exception e)
        {
            throw new WriterException(STREAM_ERROR, e);
        }
	}
	
	
	protected void closeElements() throws IOException
	{
		if (prevStackSize > componentStack.size())
		{
		    DataComponent parent = componentStack.peek().component;
		    
		    while (prevStackSize > componentStack.size())
			{
			    if (parent instanceof BlockComponent)
	                jsonWriter.write(']');
			    else if (parent instanceof RangeComponent)
                    jsonWriter.write(']');
	            else if (parent instanceof DataRecord || parent instanceof Vector)
	                jsonWriter.write('}');
	            else if (parent instanceof DataChoice)
	                jsonWriter.write('}');
			    
			    parent = parent.getParent();
			    prevStackSize--;
			}
		}
		else
			prevStackSize = componentStack.size();
		
		if (componentStack.size() == 0)
		    jsonWriter.write('\n');
	}
	
	
	@Override
	protected boolean processBlock(DataComponent component) throws IOException
	{
		try
		{
			closeElements();
			
			if (component instanceof BlockComponent)
			    jsonWriter.write('[');
			else if (component instanceof RangeComponent)
                jsonWriter.write('[');
			else if (component instanceof DataRecord || component instanceof Vector)
			    jsonWriter.write('{');
			else if (component instanceof DataChoice)
			    jsonWriter.write('{');
			
			return true;
		}
		catch (IOException e)
		{
			throw new WriterException("Error writing data for block component " + component.getName(), e);
		}	
	}
	
	
	@Override
	protected void processAtom(ScalarComponent component) throws IOException
	{
		try
        {
			closeElements();
			String localName = component.getName();
			
			if (localName.equals(AbstractArrayImpl.ELT_COUNT_NAME))
			{
				// don't write array size
			}
			else
			{	            
			    DataComponent parent = component.getParent();
			    if (parent instanceof DataRecord || parent instanceof Vector || parent instanceof DataChoice)
			    {
			        jsonWriter.write(component.getName());
			        jsonWriter.write(':');
			    }
			    
			    jsonWriter.write(getStringValue(component));
			    
	            Record parentRecord = componentStack.get(componentStack.size()-2);
	            if (parentRecord.index < parentRecord.count-1)
	            {
	                jsonWriter.write(',');
	                if (pretty)
	                    jsonWriter.write('\n');
	            }
	        }
        }
        catch (IOException e)
        {
            throw new WriterException("Error writing data for scalar component " + component.getName(), e);
        }
	}

}