/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;


/**
 * <p><b>Title:</b><br/>
 * Data Value Indexer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO DataValueIndexer type description
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Aug 15, 2005
 * @version 1.0
 */
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
    public void getData(int i, int j, int k)
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