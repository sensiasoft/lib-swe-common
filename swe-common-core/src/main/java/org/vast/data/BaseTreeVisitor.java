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

import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.CategoryRange;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.CountRange;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataComponentVisitor;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.QuantityRange;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.TimeRange;
import net.opengis.swe.v20.Vector;


/**
 * <p>
 * Abstract tree visitor providing logic to visit nested data components
 * recursively. By default, the visit methods for scalar and range components
 * do nothing.
 * </p>
 *
 * @author Alex Robin
 * @since Oct 9, 2015
 */
public abstract class BaseTreeVisitor implements DataComponentVisitor
{

    @Override
    public void visit(Boolean component)
    {
    }


    @Override
    public void visit(Count component)
    {
    }


    @Override
    public void visit(Quantity component)
    {
    }


    @Override
    public void visit(Time component)
    {
    }


    @Override
    public void visit(Category component)
    {
    }


    @Override
    public void visit(Text component)
    {
    }


    @Override
    public void visit(CountRange component)
    {
    }


    @Override
    public void visit(QuantityRange component)
    {
    }


    @Override
    public void visit(TimeRange component)
    {
    }


    @Override
    public void visit(CategoryRange component)
    {
    }


    @Override
    public void visit(DataRecord record)
    {
        for (DataComponent field: record.getFieldList())
            field.accept(this);
    }


    @Override
    public void visit(Vector vect)
    {
        for (DataComponent coord: vect.getCoordinateList())
            coord.accept(this);
    }


    @Override
    public void visit(DataChoice choice)
    {
        for (DataComponent item: choice.getItemList())
            item.accept(this);
    }


    @Override
    public void visit(DataArray array)
    {
        array.getElementType().accept(this);
    }

}
