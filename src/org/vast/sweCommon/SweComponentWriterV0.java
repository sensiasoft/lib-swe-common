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

import org.w3c.dom.*;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataBlock;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataComponentWriter;
import org.vast.cdm.common.DataType;
import org.vast.data.*;
import org.vast.xml.DOMHelper;


/**
 * <p><b>Title:</b><br/>
 * Data Component Writer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Writes CDM Components structures including DataValue,
 * DataGroup, DataArray to an XML DOM tree. 
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Feb 10, 2006
 * @version 1.0
 */
public class SweComponentWriterV0 implements DataComponentWriter
{
    public static String tokenSeparator = " ";
    protected boolean writeInlineData = false;
    protected DOMHelper dom;
        
    
    public SweComponentWriterV0(DOMHelper parentWriter)
    {
        dom = parentWriter;
        dom.addUserPrefix("swe", "http://www.opengis.net/swe");
    }
    
    
    public Element writeComponent(DOMHelper dom, DataComponent dataComponents) throws CDMException
    {
        Element newElt = null;
        
        if (dataComponents instanceof DataGroup)
        {
            newElt = writeDataGroup((DataGroup)dataComponents);
        }
        else if (dataComponents instanceof DataArray)
        {
            newElt = writeDataArray(dataComponents);
        }
        else if (dataComponents instanceof DataList)
        {
            newElt = writeDataArray(dataComponents);
        }
        else if (dataComponents instanceof DataValue)
        {
            newElt = writeDataValue((DataValue)dataComponents);
        }

        return newElt;
    }
    
    
    private Element writeComponentProperty(DataComponent dataComponents) throws CDMException
    {
        Element propElt = dom.createElement("swe:component");
        writeName(propElt, dataComponents.getName());        
        Element componentElt = writeComponent(null, dataComponents);
        propElt.appendChild(componentElt);        
        return propElt;
    }


    private Element writeDataGroup(DataGroup dataGroup) throws CDMException
    {
        Element dataGroupElt = dom.createElement("swe:DataGroup");
                
        // write each sub-component
        int groupSize = dataGroup.getComponentCount();        
        for (int i=0; i<groupSize; i++)
        {
            Element propElt = writeComponentProperty(dataGroup.getComponent(i));
            dataGroupElt.appendChild(propElt);
        }
        
        // write attributes
        writeAttributes(dataGroup, dataGroupElt);

        return dataGroupElt;
    }


    private Element writeDataArray(DataComponent dataArray) throws CDMException
    {
        Element dataArrayElt = dom.createElement("swe:DataArray");
        
        // write array size
        int arraySize = dataArray.getComponentCount();
        dataArrayElt.setAttribute("arraySize", String.valueOf(arraySize));
        
        // make sure we disable writing data inline
        boolean saveWriteInlineDataState = writeInlineData;
        writeInlineData = false;
        
        // write array component
        Element propElt = writeComponentProperty(dataArray.getComponent(0));
        dataArrayElt.appendChild(propElt);
        writeAttributes(dataArray, dataArrayElt);
        
        // restore state of writeInlineData
        writeInlineData = saveWriteInlineDataState;
                
        // write tuple values if present
        if (dataArray.getData() != null && writeInlineData)
        {
            Element tupleValuesElt = writeArrayValues(dataArray);
            dataArrayElt.appendChild(tupleValuesElt);
        }
        
        return dataArrayElt;        
    }


    private Element writeDataValue(DataValue dataValue) throws CDMException
    {
        Object def = dataValue.getProperty("definition");
        String eltName = "swe:Parameter";
        DataBlock data = dataValue.getData();
        
        // create right element and write value
        if (def != null && ((String)def).contains("time"))
        {
            eltName = "swe:Time";               
        }
        else if (dataValue.getDataType() == DataType.BOOLEAN)
        {
            eltName = "swe:Boolean";
        }
        else if (dataValue.getDataType() == DataType.DOUBLE || dataValue.getDataType() == DataType.FLOAT)
        {
            eltName = "swe:Quantity";
        }
        else if (dataValue.getDataType() == DataType.ASCII_STRING || dataValue.getDataType() == DataType.UTF_STRING)
        {
            eltName = "swe:Category";
        }
        else
        {
            eltName = "swe:Count";
        }
        
        // create element
        Element dataValueElt = dom.createElement(eltName);
        
    	// write attributes
    	writeAttributes(dataValue, dataValueElt);
        
        // write value
        if (writeInlineData)
            dataValueElt.setTextContent(data.getStringValue());
        
        return dataValueElt;
    }
    
    
    public void writeName(Element propertyElement, String name)
    {
        if (name != null)
            propertyElement.setAttribute("name", name);
    }
    
    
    private void writeAttributes(DataComponent dataComponent, Element dataValueElt) throws CDMException
    {
        // definition URI
        Object defUri = dataComponent.getProperty(DataComponent.DEF_URI);
        if (defUri != null)
            dataValueElt.setAttribute(DataComponent.DEF_URI, (String)defUri);
        
        // reference frame
        Object refFrame = dataComponent.getProperty(DataComponent.REF);
        if (refFrame != null)
            dataValueElt.setAttribute(DataComponent.REF, (String)refFrame);
        
        // local frame
        Object locFrame = dataComponent.getProperty(DataComponent.LOC);
        if (locFrame != null)
            dataValueElt.setAttribute(DataComponent.LOC, (String)locFrame);
                
        // scale factor
        Object scale = dataComponent.getProperty(DataComponent.SCALE);
        if (scale != null)
            dataValueElt.setAttribute(DataComponent.SCALE, (String)scale);
        
        // uom attribute
        Object unit = dataComponent.getProperty(DataComponent.UOM_CODE);
        if (unit != null)
            dataValueElt.setAttribute(DataComponent.UOM_CODE, (String)unit);
        
        // axis code attribute
        Object axisCode = dataComponent.getProperty(DataComponent.AXIS);
        if (axisCode != null)
            dataValueElt.setAttribute(DataComponent.AXIS, (String)axisCode);
    }
    
    
    private Element writeArrayValues(DataComponent arrayStructure) throws CDMException
    {
        Element tupleValuesElement = dom.createElement("swe:tupleValues");       
        DataBlock data = arrayStructure.getData();
        StringBuffer buffer = new StringBuffer();
       
        for (int i=0; i<data.getAtomCount(); i++)
        {
            if (i != 0)
                buffer.append(tokenSeparator);
            
            String text = data.getStringValue(i);
            buffer.append(text);
        }
        
        tupleValuesElement.setTextContent(buffer.toString());
        
        return tupleValuesElement;
    }


    public boolean getWriteInlineData()
    {
        return writeInlineData;
    }


    public void setWriteInlineData(boolean writeInlineData)
    {
        this.writeInlineData = writeInlineData;
    }

}
