package net.opengis.swe.v20;

import net.opengis.OgcPropertyList;


/**
 * POJO class for XML type DataRecordType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface DataRecord extends DataComponent
{
    
    
    /**
     * Gets the list of field properties
     */
    public OgcPropertyList<DataComponent> getFieldList();
    
    
    /**
     * Returns number of field properties
     */
    public int getNumFields();
    
    
    /**
     * Gets the field property with the given name
     */
    public DataComponent getField(String name);
    
    
    /**
     * Adds a new field property
     */
    public void addField(String name, DataComponent field);
    
}
