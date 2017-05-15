/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;


/**
 * <p>
 * List implementation for holding list of OgcProperty objects.
 * As seen from the interface the elements of the list are actually the properties
 * values which allows for simple browsing when no access to the actual property is
 * needed. For more advanced usage, OgcProperty objects themselves can be retrieved
 * with getProperty() methods.
 * </p>
 *
 * @author Alex Robin
 * @param <ValueType> Type of the properties value
 * @since Nov 8, 2014
 */
public class OgcPropertyList<ValueType extends Serializable> implements List<ValueType>, Serializable
{
    private static final long serialVersionUID = -7381244771009281275L;
    protected ArrayList<OgcProperty<ValueType>> items;
    protected HashMap<String, OgcProperty<ValueType>> nameMap;
    
    
    public OgcPropertyList()
    {
        items = new ArrayList<>();
        nameMap = new HashMap<>();
    }
    
    
    public OgcPropertyList(int size)
    {
        items = new ArrayList<>(size);
        nameMap = new HashMap<>(size);
    }
    
    
    public void copyTo(OgcPropertyList<ValueType> other)
    {
        for (OgcProperty<ValueType> prop: items)
            other.add(prop.copy());
    }
    
    
    public boolean hasProperty(String name)
    {
        return nameMap.containsKey(name);
    }
    
    
    /**
     * Retrieves property value by name
     * @param name
     * @return property value object
     */
    public ValueType get(String name)
    {
        OgcProperty<ValueType> prop = getProperty(name);        
        return prop.getValue();
    }
    
    
    /**
     * Retrieves property object by name.
     * Property object contains extra info such as name, xlink, etc. about a property
     * @param name
     * @return property object or null if no property with the given name exists
     */
    public OgcProperty<ValueType> getProperty(String name)
    {
        OgcProperty<ValueType> prop = nameMap.get(name);
        if (prop == null)
            throw new IllegalArgumentException("Unknown item '" + name + "'");
        return prop;
    }
    
    
    /**
     * Retrieves property object at specified index.
     * Property object contains extra info such as name, xlink, etc. about a property
     * @param i index of property
     * @return property object or null
     */
    public OgcProperty<ValueType> getProperty(int i)
    {
        return items.get(i);
    }
    
    
    /**
     * Adds a property object to the list
     * The property carries the actual value object
     * @param prop
     */
    public void add(OgcProperty<ValueType> prop)
    {
        checkName(prop.getName());        
        items.add(prop);
        if (prop.getName() != null)
            nameMap.put(prop.getName(), prop);
    }
    
    
    /**
     * Adds a property with the specified name and value to the list
     * @param name name to use on property (= name attribute)
     * @param e the property value object
     * @return the newly created OgcPropery object 
     * @throws IllegalArgumentException if name is already in use
     */
    public OgcProperty<ValueType> add(String name, ValueType e)
    {
        checkName(name);
        OgcPropertyImpl<ValueType> prop = new OgcPropertyImpl<>(name, e);
        items.add(prop);
        nameMap.put(name, prop);
        return prop;
    }    
    
    
    /**
     * Adds a property with the specified name, href, and role to the list
     * @param name name to use on property (= name attribute)
     * @param href
     * @param role can be null
     * @return the newly created OgcPropery object
     * @throws IllegalArgumentException if name is already in use
     */
    public OgcProperty<ValueType> add(String name, String href, String role)
    {
        checkName(name);
        OgcPropertyImpl<ValueType> prop = new OgcPropertyImpl<>();
        prop.name = name;
        prop.href = href;
        prop.role = role;
        items.add(prop);
        nameMap.put(name, prop);
        return prop;
    }
    
    
    /**
     * Removes property with the given name
     * @param name
     * @return The property that was just removed
     */
    public OgcProperty<ValueType> remove(String name)
    {
        OgcProperty<ValueType> prop = nameMap.remove(name);
        if (prop == null)
            throw new IllegalArgumentException("Unknown property '" + name + "'");
        items.remove(prop);
        return prop;
    }
    
    
    /**
     * @return Read-only set of all property names
     */
    public Set<String> getPropertyNames()
    {
        return Collections.unmodifiableSet(nameMap.keySet());
    }
    
    
    /**
     * @return Read-only list of all properties
     */
    public List<OgcProperty<ValueType>> getProperties()
    {
        return Collections.unmodifiableList(items);
    }

    
    @Override
    public boolean add(ValueType e)
    {
        return items.add(new OgcPropertyImpl<ValueType>(e));
    }


    @Override
    public void add(int index, ValueType e)
    {
        items.add(index, new OgcPropertyImpl<ValueType>(e));
    }
    
    
    @Override
    @SuppressWarnings("rawtypes")
    public boolean addAll(Collection<? extends ValueType> c)
    {
        if (c instanceof OgcPropertyList)
        {
            items.addAll(((OgcPropertyList) c).items);
            nameMap.putAll(((OgcPropertyList) c).nameMap);
        }
        else
        {
            for (ValueType e: c)
                add(e);
        }
        
        return true;
    }


    @Override
    public boolean addAll(int index, Collection<? extends ValueType> c)
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public void clear()
    {
        items.clear();
        nameMap.clear();
    }


    @Override
    public boolean contains(Object o)
    {
        for (OgcProperty<ValueType> prop: items)
        {
            if (o == null && prop.getValue() == null)
                return true;
            
            else if (o != null && o.equals(prop.getValue()))
                return true;
        }
        
        return false;
    }


    @Override
    public boolean containsAll(Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public ValueType get(int i)
    {
        OgcProperty<ValueType> prop = items.get(i);
        if (prop == null)
            return null;
        else
            return prop.getValue();
    }


    @Override
    public int indexOf(Object o)
    {
        for (int i=0; i<items.size(); i++)
        {
            OgcProperty<ValueType> prop = items.get(i);
            if (prop == null)
                continue; 
            
            if (prop.getValue() == o)
                return i;
        }
        
        return -1;
    }


    @Override
    public boolean isEmpty()
    {
        return items.isEmpty();
    }


    @Override
    public Iterator<ValueType> iterator()
    {
        final Iterator<OgcProperty<ValueType>> it = items.iterator();
        
        // wrap iterator to change type
        return new Iterator<ValueType>() {
            
            @Override
            public boolean hasNext()
            {
                return it.hasNext();
            }

            @Override
            public ValueType next()
            {
                OgcProperty<ValueType> prop = it.next();
                if (prop == null)
                    return null;
                else
                    return prop.getValue();
            }

            @Override
            public void remove()
            {
                it.remove();
            }            
        };
    }


    @Override
    public int lastIndexOf(Object o)
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public ListIterator<ValueType> listIterator()
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public ListIterator<ValueType> listIterator(int index)
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean remove(Object o)
    {
        boolean removed = items.remove(o);
        if (removed)
        {
            OgcProperty<ValueType> prop = (OgcProperty<ValueType>)o;
            nameMap.remove(prop.getName());
        }
        
        return removed;
    }


    @Override
    public ValueType remove(int index)
    {
        OgcProperty<ValueType> prop = items.remove(index);
        nameMap.remove(prop.getName());
        return prop.getValue();
    }


    @Override
    public boolean removeAll(Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean retainAll(Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public ValueType set(int i, ValueType e)
    {
        return items.set(i, new OgcPropertyImpl<ValueType>(e)).getValue();
    }


    @Override
    public int size()
    {
        return items.size();
    }


    @Override
    public List<ValueType> subList(int fromIndex, int toIndex)
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public Object[] toArray()
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public <T> T[] toArray(T[] a)
    {
        throw new UnsupportedOperationException();
    }
    
    
    protected void checkName(String name)
    {
        if (name == null)
            return;
        
        if (nameMap.containsKey(name))
            throw new IllegalArgumentException("Item '" + name + "' already exists");
    }
}
