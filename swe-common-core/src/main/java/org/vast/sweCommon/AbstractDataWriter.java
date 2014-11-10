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

package org.vast.sweCommon;

import java.io.IOException;
import java.io.OutputStream;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.ScalarComponent;
import net.opengis.swe.v20.Time;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.util.DateTimeFormat;
import org.vast.util.WriterException;


/**
 * <p>
 * Abstract class for writing a CDM data stream.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @since Feb 10, 2006
 * @version 1.0
 */
public abstract class AbstractDataWriter extends DataTreeVisitor implements DataStreamWriter
{
	protected final static String STREAM_ERROR = "Error while writing data stream";
	protected final static String CHOICE_ERROR = "Invalid choice selection: ";
	protected final static String NO_HANDLER_ERROR = "A DataHandler must be registered";
	
	protected boolean stopWriting = false;
	
	
	public AbstractDataWriter()
	{
		super(false);
	}


	public abstract void setOutput(OutputStream os) throws IOException;
	
	public abstract void close() throws IOException;
	
    public abstract void flush() throws IOException;
                
    protected abstract void processAtom(ScalarComponent component) throws IOException;

    protected abstract boolean processBlock(DataComponent component) throws IOException;
    
    
    public void write(OutputStream outputStream) throws IOException
    {
        // error if no dataHandler is registered
        if (dataHandler == null)
            throw new IllegalStateException(NO_HANDLER_ERROR);
        
        stopWriting = false;
        
        try
        {
            setOutput(outputStream);
            
            // keep writing until told to stop
            do processNextElement();
            while(!stopWriting);
        }
        catch (Exception e)
        {
            throw new WriterException(STREAM_ERROR, e);
        }
        finally
        {
            dataComponents.clearData();
        }
    }
    
    
    public void write(DataBlock dataBlock) throws IOException
    {
        dataComponents.setData(dataBlock);
        
        try
        {
            do processNextElement();
            while(!isEndOfDataBlock());
        }
        catch (Exception e)
        {
            throw new WriterException(STREAM_ERROR, e);
        }
        finally
        {
            dataComponents.clearData();
        }
    }
    
    
	public synchronized void stop()
	{
        stopWriting = true;
	}
	
	
	/**
	 * Retrieve string representation of value of component
	 * This will convert to an ISO string for appropriate time components
	 * @param component
	 * @return
	 */
	protected String getStringValue(ScalarComponent component)
	{	
	    DataBlock data = component.getData();
	    DataType dataType = data.getDataType();
        String val;
	    
        // case of time component
	    String uom = null;
		if (component instanceof Time)
		    uom = ((Time)component).getUom().getHref();
				
		if (uom != null && uom.equals(Time.ISO_TIME_UNIT))
			val = getDoubleAsString(data.getDoubleValue(), true);
		else if (dataType == DataType.DOUBLE || dataType == DataType.FLOAT)
		    val = getDoubleAsString(data.getDoubleValue(), false);
		else
			val = data.getStringValue();
		
		return val;
	}
	
	
	@Override
	public void setDataComponents(DataComponent dataInfo)
    {
        this.dataComponents = dataInfo.copy();
    }
	
	
	public static String getDoubleAsString(double doubleVal, boolean useIso)
	{
	    if (Double.isInfinite(doubleVal))
            return (doubleVal == Double.POSITIVE_INFINITY) ? "+INF" : "-INF";
	    else if (useIso)
            return DateTimeFormat.formatIso(doubleVal, 0);
        else
            return Double.toString(doubleVal);
	}
}
