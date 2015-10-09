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


public interface DataComponentVisitor
{
    public void visit(Boolean component);
    
    public void visit(Count component);
    
    public void visit(Quantity component);
    
    public void visit(Time component);
    
    public void visit(Category component);
    
    public void visit(Text component);
    
    public void visit(CountRange component);
    
    public void visit(QuantityRange component);
    
    public void visit(TimeRange component);
    
    public void visit(CategoryRange component);
    
    public void visit(DataRecord record);
    
    public void visit(Vector vect);
    
    public void visit(DataChoice choice);
    
    public void visit(DataArray array);
}
