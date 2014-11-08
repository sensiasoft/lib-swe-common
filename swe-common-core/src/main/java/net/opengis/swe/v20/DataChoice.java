package net.opengis.swe.v20;

import net.opengis.OgcPropertyList;


/**
 * POJO class for XML type DataChoiceType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface DataChoice extends AbstractDataComponent
{
    
    
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
    public OgcPropertyList<AbstractDataComponent> getItemList();
    
    
    /**
     * Returns number of item properties
     */
    public int getNumItems();
    
    
    /**
     * Gets the item property with the given name
     */
    public AbstractDataComponent getItem(String name);
    
    
    /**
     * Adds a new item property
     */
    public void addItem(String name, AbstractDataComponent item);
}
