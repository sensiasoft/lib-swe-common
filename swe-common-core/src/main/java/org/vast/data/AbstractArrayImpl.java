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
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.BlockComponent;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.EncodedValues;


/**
 * <p>
 * Provides common methods for all implementations of block components
 * </p>
 *
 * @author Alex Robin
 * @since Nov 10, 2014
 */
public abstract class AbstractArrayImpl extends AbstractDataComponentImpl implements DataArray, BlockComponent
{
    private static final long serialVersionUID = -6508481468764422077L;
    public static final String ELT_COUNT_NAME = "elementCount";
    
    protected OgcPropertyImpl<Count> elementCount;
    protected OgcPropertyImpl<DataComponent> elementType;
    protected DataEncoding encoding;
    protected EncodedValues values;
    protected CountImpl implicitElementCount;
    

    public AbstractArrayImpl()
    {
        // special property object to correctly set parent and name
        elementType = new OgcPropertyImpl<DataComponent>() 
        {
            private static final long serialVersionUID = 3604877033013079886L;

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
            private static final long serialVersionUID = -5136432942948581877L;

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
        
        this.elementCount.copyTo(other.elementCount);
        this.elementType.copyTo(other.elementType);
        
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
            
        setElementType(name, component);
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
        return elementType.getValue().hasConstraints();
    }
    
    
    @Override
    public final boolean isVariableSize()
    {
        return elementCount.hasHref() || isImplicitSize();
    }
    
    
    @Override
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
    
    
    @Override
    public final Count getArraySizeComponent()
    {
        // case of implicit size
        if (isImplicitSize())
        {
            if (implicitElementCount == null)
                implicitElementCount = new CountImpl();
            return implicitElementCount;
        }
        
        // if variable size, try to find the size component up the component tree
        else if (isVariableSize())
        {
            String sizeIdRef = elementCount.getHref().substring(1);
            DataComponent parentComponent = this.parent;
            DataComponent sizeComponent = this;
            
            while (parentComponent != null)
            {
                if (parentComponent instanceof DataRecord)
                {
                    boolean found = false;
                    
                    for (int i=0; i<parentComponent.getComponentCount(); i++)
                    {
                        sizeComponent = parentComponent.getComponent(i);
                        if (sizeComponent instanceof Count && sizeComponent.isSetId() && sizeComponent.getId().equals(sizeIdRef))
                        {
                            found = true;
                            break;
                        }
                    }
                    
                    if (found)
                        break;
                }
                
                parentComponent = parentComponent.getParent();
            }
            
            if (parentComponent == null)
                throw new IllegalStateException("Could not find array size component with ID " + sizeIdRef);
            
            return (CountImpl)sizeComponent;
        }
        
        if (elementCount.hasValue())
            return elementCount.getValue();
        else
            throw new IllegalStateException("The array element count hasn't been set. Please use one of setElementCount() or setFixedSize() methods");
    }


    /**
     * Gets the elementType property
     */
    @Override
    public DataComponent getElementType()
    {
        return elementType.getValue();
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
        ((AbstractDataComponentImpl)component).setParent(this);
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
        this.encoding = encoding;
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