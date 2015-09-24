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
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.BlockComponent;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.EncodedValues;


/**
 * <p>
 * Provides common methods for all implementations of block components
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Nov 10, 2014
 */
public abstract class AbstractArrayImpl extends AbstractDataComponentImpl implements DataArray, BlockComponent
{
    private static final long serialVersionUID = -2536261971844652828L;
    public final static String ELT_COUNT_NAME = "elementCount";
    
    protected OgcPropertyImpl<Count> elementCount = new OgcPropertyImpl<Count>();
    protected OgcPropertyImpl<DataComponent> elementType;
    protected DataEncoding encoding;
    protected EncodedValues values;


    public AbstractArrayImpl()
    {
        // special property object to correctly set parent and name
        elementType = new OgcPropertyImpl<DataComponent>() 
        {
            @Override
            public void setValue(DataComponent value)
            {
                ((AbstractDataComponentImpl)value).setName(this.name);
                ((AbstractDataComponentImpl)value).setParent(AbstractArrayImpl.this);
                super.setValue(value);
            }
        };
        
        // special property object to correctly set element count name
        elementCount = new OgcPropertyImpl<Count>() 
        {
            @Override
            public void setValue(Count value)
            {
                ((CountImpl)value).setName(AbstractArrayImpl.ELT_COUNT_NAME);
                super.setValue(value);
            }
        };        
    }
    
    
    @Override
    public abstract AbstractArrayImpl copy();
    
    
    protected void copyTo(AbstractArrayImpl other)
    {
        super.copyTo(other);
        
        other.elementCount = this.elementCount.copy();
        other.elementType = this.elementType.copy();
        
        if (this.encoding != null)
            other.encoding = this.encoding.copy();
        else
            other.encoding = null;
    }
    
    
    @Override
    public void addComponent(String name, DataComponent component)
    {
        if (elementType.hasValue())
            throw new IllegalStateException("The array element type is already set. Use setElementType() to replace it");
            
        setElementType(name, (DataComponent)component);
    }
    
    
    @Override
    public DataComponent removeComponent(int index)
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public DataComponent removeComponent(String name)
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean hasConstraints()
    {
        return ((DataComponent)elementType.getValue()).hasConstraints();
    }
    
    
    public final boolean isVariableSize()
    {
        return elementCount.hasHref() || isImplicitSize();
    }
    
    
    public final boolean isImplicitSize()
    {
        return !elementCount.hasHref() && elementCount.hasValue() && !elementCount.getValue().isSetValue();
    }


    /**
     * Gets the elementCount property
     */
    @Override
    public Count getElementCount()
    {
        return elementCount.getValue();
    }


    /**
     * Gets extra info (name, xlink, etc.) carried by the elementCount property
     */
    @Override
    public OgcProperty<Count> getElementCountProperty()
    {
        return elementCount;
    }


    /**
     * Sets the elementCount property<br/>
     * If the Count object has a parent, it will used as an external variable size component
     */
    @Override
    public void setElementCount(Count elementCount)
    {
        // case of variable size component
        if (elementCount.getParent() != null)
            setVariableSizeComponent(elementCount);
        
        // case of fixed size
        else
            this.elementCount.setValue(elementCount);
    }
    
    
    /**
     * Sets the size component to use (for variable size array).
     * The component must have an id and exist up the data component tree
     * @param sizeComponent Count component to obtain array size from
     */
    protected void setVariableSizeComponent(Count sizeComponent)
    {
        assert(sizeComponent.isSetId());
        this.elementCount.setHref("#" + sizeComponent.getId());
    }


    /**
     * Gets the elementType property
     */
    @Override
    public DataComponent getElementType()
    {
        return (DataComponent)elementType.getValue();
    }


    @Override
    public OgcProperty<DataComponent> getElementTypeProperty()
    {
        return elementType;
    }


    /**
     * Sets the elementType property
     */
    @Override
    public void setElementType(String name, DataComponent component)
    {
        elementType.setName(name);
        elementType.setValue(component);
    }


    /**
     * Gets the encoding property
     */
    @Override
    public DataEncoding getEncoding()
    {
        return encoding;
    }


    /**
     * Checks if encoding is set
     */
    @Override
    public boolean isSetEncoding()
    {
        return (encoding != null);
    }


    /**
     * Sets the encoding property
     */
    @Override
    public void setEncoding(DataEncoding encoding)
    {
        this.encoding = (AbstractEncodingImpl) encoding;
    }


    /**
     * Gets the values property
     */
    @Override
    public EncodedValues getValues()
    {
        return values;
    }


    /**
     * Checks if values is set
     */
    @Override
    public boolean isSetValues()
    {
        return (values != null);
    }


    /**
     * Sets the values property
     */
    @Override
    public void setValues(EncodedValues values)
    {
        this.values = values;
    }

}