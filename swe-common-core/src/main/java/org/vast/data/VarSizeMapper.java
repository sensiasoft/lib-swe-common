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

import net.opengis.swe.v20.DataBlock;


/**
 * <p>
 * This transfers uses a value from a DataBlock as an array size.
 * This is used automatically when DataArray of variable sizes are
 * referenced in an Indexer Tree.
 * </p>
 *
 * @author Alex Robin
 * @since Apr 3, 2007
 * */
public class VarSizeMapper implements DataVisitor
{
    DataArrayIndexer arrayIndexer;
    
    
    public VarSizeMapper(DataArrayIndexer arrayIndexer)
    {
        this.arrayIndexer = arrayIndexer;
    }


    @Override
    public void mapData(DataBlock data)
    {
        arrayIndexer.setArraySize(data.getIntValue());
        arrayIndexer.updateScalarCount();
    }    
}
