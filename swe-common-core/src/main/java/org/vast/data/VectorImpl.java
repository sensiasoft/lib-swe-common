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
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataComponentVisitor;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.ScalarComponent;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.Vector;


/**
 * POJO class for XML type VectorType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class VectorImpl extends AbstractRecordImpl<ScalarComponent> implements Vector
{
    private static final long serialVersionUID = -4376114617327479016L;
    protected String referenceFrame = "";
    protected String localFrame;
    
    
    public VectorImpl()
    {
        super();
    }
    
    
    public VectorImpl(int size)
    {
        super(size);
    }
    
    
    @Override
    public VectorImpl copy()
    {
        VectorImpl newObj = new VectorImpl(fieldList.size());
        super.copyTo(newObj);
        fieldList.copyTo(newObj.fieldList);
        newObj.referenceFrame = referenceFrame;
        newObj.localFrame = localFrame;
        return newObj;
    }
    
    
    @Override
    public void addComponent(String name, DataComponent component)
    {
        if (!(component instanceof ScalarComponent))
            throw new IllegalArgumentException("A vector can only have scalar coordinates");
        
        fieldList.add(new OgcPropertyImpl<ScalarComponent>(name, (ScalarComponent)component));       
    }
    
    
    @Override
    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append(indent);
        text.append("Vector (");
        text.append(referenceFrame);
        text.append(")\n");

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
     * Gets the list of coordinate properties
     */
    @Override
    public OgcPropertyList<ScalarComponent> getCoordinateList()
    {
        return fieldList;
    }
    
    
    /**
     * Returns number of coordinate properties
     */
    @Override
    public int getNumCoordinates()
    {
        return fieldList.size();
    }
    
    
    /**
     * Gets the coordinate property with the given name
     */
    @Override
    public ScalarComponent getCoordinate(String name)
    {
        return fieldList.get(name);
    }
    
    
    /**
     * Adds a new coordinateAsCount property
     */
    @Override
    public void addCoordinateAsCount(String name, Count coordinate)
    {
        fieldList.add(new OgcPropertyImpl<ScalarComponent>(name, coordinate));
    }
    
    
    /**
     * Adds a new coordinateAsQuantity property
     */
    @Override
    public void addCoordinateAsQuantity(String name, Quantity coordinate)
    {
        fieldList.add(new OgcPropertyImpl<ScalarComponent>(name, coordinate));
    }
    
    
    /**
     * Adds a new coordinateAsTime property
     */
    @Override
    public void addCoordinateAsTime(String name, Time coordinate)
    {
        fieldList.add(new OgcPropertyImpl<ScalarComponent>(name, coordinate));
    }
    
    
    /**
     * Gets the referenceFrame property
     */
    @Override
    public String getReferenceFrame()
    {
        return referenceFrame;
    }
    
    
    /**
     * Checks if referenceFrame is set
     */
    @Override
    public boolean isSetReferenceFrame()
    {
        return (referenceFrame != null);
    }
    
    
    /**
     * Sets the referenceFrame property
     */
    @Override
    public void setReferenceFrame(String referenceFrame)
    {
        this.referenceFrame = referenceFrame;
    }
    
    
    /**
     * Gets the localFrame property
     */
    @Override
    public String getLocalFrame()
    {
        return localFrame;
    }
    
    
    /**
     * Checks if localFrame is set
     */
    @Override
    public boolean isSetLocalFrame()
    {
        return (localFrame != null);
    }
    
    
    /**
     * Sets the localFrame property
     */
    @Override
    public void setLocalFrame(String localFrame)
    {
        this.localFrame = localFrame;
    }


    @Override
    public DataType getDataType()
    {
        return fieldList.get(0).getDataType();
    }


    @Override
    public void setDataType(DataType type)
    {
        for (ScalarComponent coord: fieldList)
            coord.setDataType(type);
    }


    @Override
    public void accept(DataComponentVisitor visitor)
    {
        visitor.visit(this);
    }
}
