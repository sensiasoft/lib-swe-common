package net.opengis.swe.v20;

import net.opengis.OgcPropertyList;


/**
 * POJO class for XML type DataChoiceType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface DataChoice extends DataComponent
{
    
    @Override
    public DataChoice copy();
    
    
    /**
     * Gets the choiceValue property
     */
    public Category getChoiceValue();
    
    
    /**
     * Checks if choiceValue is set
     */
    public boolean isSetChoiceValue();
    
    
    /**
     * Sets the choiceValue property
     */
    public void setChoiceValue(Category choiceValue);
    
    
    /**
     * Gets the list of item properties
     */
    public OgcPropertyList<DataComponent> getItemList();
    
    
    /**
     * Returns number of item properties
     */
    public int getNumItems();
    
    
    /**
     * Gets the item property with the given name
     */
    public DataComponent getItem(String name);
    
    
    /**
     * Adds a new item property
     */
    public void addItem(String name, DataComponent item);
    
    
    /**
     * Gets the selected item in this choice
     * @return the selected component or null if non is selected
     */
    public DataComponent getSelectedItem();
}
