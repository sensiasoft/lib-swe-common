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


/**
 * POJO class for XML type DataArrayType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public interface DataArray extends BlockComponent
{
      
    @Override
    public DataArray copy();
    
    
    /**
     * @return true if array has variable size
     */
    public boolean isVariableSize();
    
    
    /**
     * @return true if array has implicit variable size (i.e. the size is not
     * specified by any component but will be included implicitely in a stream
     * right before the array data).
     */
    public boolean isImplicitSize();
    
    
    /**
     * Gets the array size component whether the array has fixed, implicit
     * or variable size.
     * @return the Count component that contains the array size value
     */
    public Count getArraySizeComponent();
    
    
    /**
     * Updates the size of the array (and corresponding data block if set)
     * using the size value set in the size component
     */
    public void updateSize();
    
    
    /**
     * Updates the size of the array (and corresponding data block if set) to
     * the given size. If a size component is set, its value is also updated.
     * @param arraySize new array size
     */
    public void updateSize(int arraySize);
    
}
