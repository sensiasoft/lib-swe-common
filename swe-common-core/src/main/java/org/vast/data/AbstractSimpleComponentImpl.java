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
    private static final long serialVersionUID = -2467076901109783411L;
    protected DataType dataType;
    protected OgcPropertyList<SimpleComponent> qualityList = new OgcPropertyList<>();
    protected OgcProperty<NilValues> nilValues;
    protected String referenceFrame;
    protected String axisID;

    
    public AbstractSimpleComponentImpl()
    {
        super();
    }
    
    
    protected void copyTo(AbstractSimpleComponentImpl other)
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
    
    
    @Override
    public DataType getDataType()
    {
        return dataType;
    }


    @Override
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
    public boolean isSetValue()
    {
        return dataBlock != null;
    }
    
    
    @Override
    public void unSetValue()
    {
        clearData();
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
        throw generateException();
    }
    
    @Override
    public AbstractDataComponentImpl getComponent(String name)
    {
        throw generateException();
    }
    
    @Override
    public int getComponentIndex(String name)
    {
        throw generateException();
    }
    
    @Override
    public AbstractDataComponentImpl getComponent(int index)
    {
        throw generateException();
    }
    
    @Override
    public AbstractDataComponentImpl removeComponent(int index)
    {
        throw generateException();
    }

    @Override
    public AbstractDataComponentImpl removeComponent(String name)
    {
        throw generateException();
    }
    
    protected UnsupportedOperationException generateException()
    {
        String name = getName() == null ? "This" : getName();
        return new UnsupportedOperationException(String.format("'%s' is a scalar component", name));
    }
}