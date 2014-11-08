/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2014 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class OgcPropertyList<ValueType> implements List<ValueType>
{
    ArrayList<OgcProperty<ValueType>> items;
    HashMap<String, OgcProperty<ValueType>> nameMap;
    
    
    public OgcPropertyList()
    {
        items = new ArrayList<OgcProperty<ValueType>>();
        nameMap = new HashMap<String, OgcProperty<ValueType>>();
    }
    
    
    public OgcPropertyList(int size)
    {
        items = new ArrayList<OgcProperty<ValueType>>(size);
        nameMap = new HashMap<String, OgcProperty<ValueType>>(size);
    }
    
    
    public void copyTo(OgcPropertyList<ValueType> other)
    {
        for (OgcProperty<ValueType> prop: items)
            other.add(prop.copy());
    }
    
    
    /**
     * Retrieves property value by name
     * @param name
     * @return property value object
     */
    public ValueType get(String name)
    {
        OgcProperty<ValueType> prop = nameMap.get(name);
        if (prop != null)
            return prop.getValue();
        
        return null;
    }
    
    
    /**
     * Retrieves property object by name.
     * Property object contains extra info such as name, xlink, etc. about a property
     * @param name
     * @return property object or null if no property with the given name exists
     */
    public OgcProperty<ValueType> getProperty(String name)
    {
        return nameMap.get(name);
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
        OgcPropertyImpl<ValueType> prop = new OgcPropertyImpl<ValueType>(name, e);
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
        OgcPropertyImpl<ValueType> prop = new OgcPropertyImpl<ValueType>();
        prop.name = name;
        prop.href = href;
        prop.role = role;
        items.add(prop);
        return prop;
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
    public boolean addAll(Collection<? extends ValueType> c)
    {
        for (ValueType e: c)
            add(e);
        return true;
    }


    @Override
    public boolean addAll(int index, Collection<? extends ValueType> c)
    {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public void clear()
    {
        items.clear();
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
        // TODO Auto-generated method stub
        return false;
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
        return new Iterator<ValueType>() {

            int index = 0;
            
            @Override
            public boolean hasNext()
            {
                if (index == items.size())
                    return false;
                else
                    return true;
            }

            @Override
            public ValueType next()
            {
                OgcProperty<ValueType> prop = items.get(index);
                index++;
                if (prop == null)
                    return null;
                else
                    return prop.getValue();
            }

            @Override
            public void remove()
            {
                index--;
                items.remove(index);
            }            
        };
    }


    @Override
    public int lastIndexOf(Object o)
    {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public ListIterator<ValueType> listIterator()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public ListIterator<ValueType> listIterator(int index)
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public boolean remove(Object o)
    {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public ValueType remove(int index)
    {
        return items.remove(index).getValue();
    }


    @Override
    public boolean removeAll(Collection<?> c)
    {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean retainAll(Collection<?> c)
    {
        // TODO Auto-generated method stub
        return false;
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
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Object[] toArray()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public <T> T[] toArray(T[] a)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    protected void checkName(String name)
    {
        if(name == null)
            return;
        
        if (nameMap.containsKey(name))
            throw new IllegalArgumentException("Name " + name + " is already in use");
    }
}
