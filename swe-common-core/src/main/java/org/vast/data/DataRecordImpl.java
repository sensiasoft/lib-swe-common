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

package org.vast.data;

import net.opengis.OgcPropertyList;
import net.opengis.swe.v20.AbstractDataComponent;
import net.opengis.swe.v20.DataRecord;
import org.vast.cdm.common.DataComponent;


/**
 * <p>
 * Implementation of SWE Common DataRecord
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @version 1.0
 */
public class DataRecordImpl extends AbstractRecordImpl<AbstractDataComponent> implements DataRecord
{
    private static final long serialVersionUID = 5402778409089789225L;

    
    public DataRecordImpl()
    {
        super();
    }
    
    
    public DataRecordImpl(int size)
    {
        super(size);
    }
    
    
    @Override
    public DataRecordImpl copy()
    {
        DataRecordImpl newObj = new DataRecordImpl(fieldList.size());
        super.copyTo(newObj);
        fieldList.copyTo(newObj.fieldList);
        return newObj;
    }
    
    
    @Override
    public void addComponent(String name, DataComponent component)
    {
        ((AbstractDataComponentImpl)component).parent = this;
        ((AbstractDataComponentImpl)component).name = name;
        fieldList.add(name, (AbstractDataComponentImpl)component);        
    }
    
    
    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append(indent);
        text.append("DataRecord\n");

        for (int i=0; i<fieldList.size(); i++)
        {
            text.append(indent + INDENT);
            text.append(fieldList.getProperty(i).getName());
            text.append(":\n");
            text.append(getComponent(i).toString(indent + INDENT + INDENT));
            text.append('\n');
        }

        return text.toString();
    }
    
	
	/* ************************************ */
    /*  Auto-generated Getters and Setters  */    
    /* ************************************ */	
	
    /**
     * Gets the list of field properties
     */
    @Override
    public OgcPropertyList<AbstractDataComponent> getFieldList()
    {
        return fieldList;
    }
    
    
    /**
     * Returns number of field properties
     */
    @Override
    public int getNumFields()
    {
        return fieldList.size();
    }
    
    
    /**
     * Gets the field property with the given name
     */
    @Override
    public AbstractDataComponent getField(String name)
    {
        return fieldList.get(name);
    }
    
    
    /**
     * Adds a new field property
     */
    @Override
    public void addField(String name, AbstractDataComponent field)
    {
        addComponent(name, (AbstractDataComponentImpl)field);
    }
}
