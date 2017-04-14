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

import java.util.ArrayList;
import java.util.List;
import net.opengis.HasCopy;
import net.opengis.swe.v20.NilValue;
import net.opengis.swe.v20.NilValues;


/**
 * POJO class for XML type NilValuesType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class NilValuesImpl extends AbstractSWEImpl implements NilValues, HasCopy
{
    private static final long serialVersionUID = -5131700003111279239L;
    protected ArrayList<NilValue> nilValueList = new ArrayList<NilValue>();
    
    
    public NilValuesImpl()
    {
    }
    
    
    @Override
    public NilValuesImpl copy()
    {
        NilValuesImpl newObj = new NilValuesImpl();
        for (NilValue nil: nilValueList)
            newObj.nilValueList.add(new NilValueImpl(nil.getReason(), nil.getValue()));
        return newObj;
    }
    
    
    /**
     * Gets the list of nilValue properties
     */
    @Override
    public List<NilValue> getNilValueList()
    {
        return nilValueList;
    }
    
    
    /**
     * Returns number of nilValue properties
     */
    @Override
    public int getNumNilValues()
    {
        if (nilValueList == null)
            return 0;
        return nilValueList.size();
    }
    
    
    /**
     * Adds a new nilValue property
     */
    @Override
    public void addNilValue(NilValue nilValue)
    {
        this.nilValueList.add(nilValue);
    }
}
