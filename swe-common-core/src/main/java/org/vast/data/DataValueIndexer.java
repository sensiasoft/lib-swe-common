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


/**
 * <p>
 * TODO DataValueIndexer type description
 * </p>
 *
 * @author Alex Robin
 * @since Aug 15, 2005
 * */
public class DataValueIndexer extends DataIndexer
{
    
    public DataValueIndexer()
    {
        this.scalarCount = 1;
    }
    
    
    public DataValueIndexer(int componentIndex)
    {
        this.componentIndex = componentIndex;
    }
    
    
    @Override
    public DataValueIndexer copy()
    {
        DataValueIndexer newIndexer = new DataValueIndexer();
        newIndexer.componentIndex = this.componentIndex;
        newIndexer.visitorList = this.visitorList;        
        return newIndexer;
    }
    
    
    @Override
    public void updateStartIndex(int startIndex)
    {
        this.data.startIndex = startIndex;
    }


    @Override
    public void setData(AbstractDataBlock data)
    {
        this.data = data;
    }
    
    
    @Override
    public void getData(int[] indexList)
    {
        this.doVisitors = true;
        this.applyVisitors();
    }
    
    
    @Override
    public void clearData()
    {
        this.data = null;
    }
    

    @Override
    public void next()
    {
        this.applyVisitors();        
        this.hasNext = false;
    }
    
    
    @Override
    public int skip(int num)
    {
        return num;
    }
    
    
    @Override
    public void reset()
    {
        this.hasNext = true;
        this.doVisitors = true;
    }
    
    
    @Override
    public void updateScalarCount()
    {
    }
}