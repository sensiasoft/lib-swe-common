/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.OgcPropertyList;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.SimpleComponent;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.NilValues;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.QuantityRange;
import net.opengis.swe.v20.Text;


public abstract class AbstractSimpleComponentImpl extends AbstractDataComponentImpl implements SimpleComponent
{
    private static final long serialVersionUID = 8170666086821263672L;
   
    protected DataType dataType;
    protected OgcPropertyList<SimpleComponent> qualityList = new OgcPropertyList<SimpleComponent>();
    protected OgcProperty<NilValues> nilValues;
    protected String referenceFrame;
    protected String axisID;

    
    public AbstractSimpleComponentImpl()
    {
        super();
    }
    
    
    protected void copyTo(AbstractRangeComponentImpl other)
    {
        super.copyTo(other);
        other.dataType = dataType;
        
        if (nilValues != null)
            other.nilValues = nilValues.copy();
        else
            other.nilValues = null;
        
        qualityList.copyTo(other.qualityList);
        other.referenceFrame = referenceFrame;
        other.axisID = axisID;
    }
    
    
    public DataType getDataType()
    {
        return dataType;
    }


    public void setDataType(DataType type)
    {
        this.dataType = type;
    }
    
    
    /**
     * Gets the list of quality properties
     */
    @Override
    public OgcPropertyList<SimpleComponent> getQualityList()
    {
        return qualityList;
    }
    
    
    /**
     * Returns number of quality properties
     */
    @Override
    public int getNumQualitys()
    {
        if (qualityList == null)
            return 0;
        return qualityList.size();
    }
    
    
    /**
     * Adds a new qualityAsQuantity property
     */
    @Override
    public void addQuality(Quantity quality)
    {
        this.qualityList.add(quality);
    }
    
    
    /**
     * Adds a new qualityAsQuantityRange property
     */
    @Override
    public void addQuality(QuantityRange quality)
    {
        this.qualityList.add(quality);
    }
    
    
    /**
     * Adds a new qualityAsCategory property
     */
    @Override
    public void addQuality(Category quality)
    {
        this.qualityList.add(quality);
    }
    
    
    /**
     * Adds a new qualityAsText property
     */
    @Override
    public void addQuality(Text quality)
    {
        this.qualityList.add(quality);
    }
    
    
    /**
     * Gets the nilValues property
     */
    @Override
    public NilValues getNilValues()
    {
        return nilValues.getValue();
    }
    
    
    /**
     * Gets extra info (name, xlink, etc.) carried by the nilValues property
     */
    @Override
    public OgcProperty<NilValues> getNilValuesProperty()
    {
        if (nilValues == null)
            nilValues = new OgcPropertyImpl<NilValues>();
        return nilValues;
    }
    
    
    /**
     * Checks if nilValues is set
     */
    @Override
    public boolean isSetNilValues()
    {
        return (nilValues != null && (nilValues.hasValue() || nilValues.hasHref()));
    }
    
    
    /**
     * Sets the nilValues property
     */
    @Override
    public void setNilValues(NilValues nilValues)
    {
        if (this.nilValues == null)
            this.nilValues = new OgcPropertyImpl<NilValues>();
        this.nilValues.setValue(nilValues);
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
     * Gets the axisID property
     */
    @Override
    public String getAxisID()
    {
        return axisID;
    }
    
    
    /**
     * Checks if axisID is set
     */
    @Override
    public boolean isSetAxisID()
    {
        return (axisID != null);
    }
    
    
    /**
     * Sets the axisID property
     */
    @Override
    public void setAxisID(String axisID)
    {
        this.axisID = axisID;
    }
    
    
    @Override
    public void clearData()
    {
        this.dataBlock = null;
    }
    
    
    @Override
    protected void updateAtomCount(int childOffsetCount)
    {
        // DO NOTHING
    }
    
    
    /////////////////////////////////////////
    // Methods invalid in simple component //
    /////////////////////////////////////////
    @Override
    public void addComponent(String name, DataComponent component)
    {
        throw new UnsupportedOperationException();        
    }
    
    @Override
    public AbstractDataComponentImpl getComponent(String name)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int getComponentIndex(String name)
    {
        throw new UnsupportedOperationException();
    }
    
    public AbstractDataComponentImpl getComponent(int index)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public AbstractDataComponentImpl removeComponent(int index)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public AbstractDataComponentImpl removeComponent(String name)
    {
        throw new UnsupportedOperationException();
    }
}