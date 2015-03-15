/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

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
    
    
    /**
     * Sets the choice selection to the item with the given name
     * @param name
     */
    public void setSelectedItem(String name);
    
    
    /**
     * Sets the choice selection to the item with the given index
     * @param name
     */
    public void setSelectedItem(int index);
}
