/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.test;

import static org.junit.Assert.*;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataRecord;
import org.junit.Test;
import org.vast.swe.SWEUtils;
import org.vast.swe.SWEHelper;


public class TestVarSizeArrays
{
    SWEHelper fac = new SWEHelper();
    SWEUtils utils = new SWEUtils(SWEUtils.V2_0);

    
    @Test
    public void testVarSizeArray1D() throws Exception
    {
        DataRecord rec = fac.newDataRecord();
        
        Count size = fac.newCount();
        size.setId("ARRAY_SIZE");
        rec.addField("num_pos", size);        
        
        DataArray array = fac.newDataArray();
        array.setElementType("pos", fac.newLocationVectorLLA(null));
        array.setElementCount(size);
        rec.addField("pos_array", array);
        
        utils.writeComponent(System.out, rec, false, true);
                
        DataBlock data = rec.createDataBlock();
        assertEquals("Wrong data block size", 4, data.getAtomCount());
        
        int arraySize = 100;
        array.updateSize(arraySize);
        data = rec.createDataBlock();
        assertEquals("Wrong data block size", arraySize*3+1, data.getAtomCount());        
    }
    
    
    @Test
    public void testVarSizeArray1Dx3() throws Exception
    {
        DataRecord rec = fac.newDataRecord();
        rec.addField("el", fac.newQuantity());
        rec.addField("az", fac.newQuantity());
        
        Count numBins = fac.newCount();
        numBins.setId("NUM_BINS");
        rec.addField("num_bins", numBins);
        
        DataRecord arrayGroup = fac.newDataRecord(3);

        DataArray array1 = fac.newDataArray();
        array1.setElementType("elt", fac.newQuantity());
        array1.setElementCount(numBins);
        arrayGroup.addComponent("array1", array1);

        DataArray array2 = fac.newDataArray();
        array2.setElementType("elt", fac.newQuantity());
        array2.setElementCount(numBins);
        arrayGroup.addComponent("array2", array2);

        DataArray array3 = fac.newDataArray();
        array3.setElementType("elt", fac.newQuantity());
        array3.setElementCount(numBins);
        arrayGroup.addComponent("array3", array3);

        rec.addComponent("data", arrayGroup);
        
        utils.writeComponent(System.out, rec, false, true);
        
        DataBlock data = rec.createDataBlock();
        assertEquals("Wrong data block size", 6, data.getAtomCount());
        
        int arraySize = 100;
        array1.updateSize(arraySize);
        array2.updateSize();
        array3.updateSize();
        data = rec.createDataBlock();
        assertEquals("Wrong data block size", arraySize*3+3, data.getAtomCount()); 
    }
    
    
    @Test
    public void testVarSizeArray2D() throws Exception
    {
        DataRecord rec = fac.newDataRecord();
        
        Count w = fac.newCount();
        w.setId("WIDTH");
        rec.addField("w", w);
        
        Count h = fac.newCount();
        h.setId("HEIGHT");
        rec.addField("h", h);  
        
        DataArray innerArray = fac.newDataArray();
        innerArray.setElementType("val", fac.newQuantity());
        innerArray.setElementCount(w);
        
        DataArray outerArray = fac.newDataArray();
        outerArray.setElementType("inner", innerArray);
        outerArray.setElementCount(h);       
        
        rec.addField("outer_array", outerArray);
        
        utils.writeComponent(System.out, rec, false, true);
        utils.writeEncoding(System.out, SWEHelper.getDefaultBinaryEncoding(rec), true);
        
        DataBlock data = rec.createDataBlock();
        assertEquals("Wrong data block size", 3, data.getAtomCount());
        
        int innerSize = 100;
        innerArray.updateSize(innerSize);
        int outerSize = 200;
        outerArray.updateSize(outerSize);
        data = rec.createDataBlock();
        assertEquals("Wrong data block size", innerSize*outerSize*1+2, data.getAtomCount());        
    }
}
