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
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.QuantityRange;
import net.opengis.swe.v20.RangeComponent;
import net.opengis.swe.v20.SimpleComponent;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.TimeRange;


/**
 * <p>
 * Abstract scalar visitor allowing to recisovely visit all scalars components
 * in the data component tree.<br/>
 * By default, the visit methods for scalar and range components
 * call the {@link #visitSimple(SimpleComponent)} method which does nothing.
 * </p>
 *
 * @author Alex Robin
 * @since Oct 9, 2015
 */
public abstract class ScalarVisitor extends BaseTreeVisitor
{

    public void visitSimple(SimpleComponent component)
    {
        // do nothing, to be overriden by derived classes
    }
    
    
    public void visitScalar(SimpleComponent component)
    {
        visitSimple(component);
    }
    
    
    public void visitRange(RangeComponent component)
    {
        visitSimple(component);
    }
    
    
    @Override
    public void visit(Boolean component)
    {
        visitScalar(component);
    }


    @Override
    public void visit(Count component)
    {
        visitScalar(component);
    }


    @Override
    public void visit(Quantity component)
    {
        visitScalar(component);
    }


    @Override
    public void visit(Time component)
    {
        visitScalar(component);
    }


    @Override
    public void visit(Category component)
    {
        visitScalar(component);
    }


    @Override
    public void visit(Text component)
    {
        visitScalar(component);
    }


    @Override
    public void visit(CountRange component)
    {
        visitRange(component);
    }


    @Override
    public void visit(QuantityRange component)
    {
        visitRange(component);
    }


    @Override
    public void visit(TimeRange component)
    {
        visitRange(component);
    }


    @Override
    public void visit(CategoryRange component)
    {
        visitRange(component);
    }
}
