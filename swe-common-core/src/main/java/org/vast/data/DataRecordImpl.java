/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import net.opengis.OgcPropertyImpl;
import net.opengis.OgcPropertyList;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataComponentVisitor;
import net.opengis.swe.v20.DataRecord;


/**
 * <p>
 * Implementation of SWE Common DataRecord
 * </p>
 *
 * @author Alex Robin
 * */
public class DataRecordImpl extends AbstractRecordImpl<DataComponent> implements DataRecord
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
        addField(name, component);
    }
    
    
    @Override
    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append("DataRecord\n");
        
        for (int i=0; i<fieldList.size(); i++)
        {
            text.append(indent);
            text.append(fieldList.getProperty(i).getName());
            text.append(": ");
            text.append(getComponent(i).toString(indent + INDENT));
            if (i < fieldList.size()-1)
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
    public OgcPropertyList<DataComponent> getFieldList()
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
    public DataComponent getField(String name)
    {
        return fieldList.get(name);
    }
    
    
    /**
     * Adds a new field property
     */
    @Override
    public void addField(String name, DataComponent field)
    {
        fieldList.add(new OgcPropertyImpl<DataComponent>(name, field));
    }


    @Override
    public void accept(DataComponentVisitor visitor)
    {
        visitor.visit(this);
    }
}
