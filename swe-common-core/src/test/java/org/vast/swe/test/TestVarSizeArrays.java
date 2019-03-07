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
import net.opengis.swe.v20.DataType;
import org.junit.Test;
import org.vast.data.DataBlockByte;
import org.vast.data.DataBlockDouble;
import org.vast.data.DataBlockFactory;
import org.vast.data.DataBlockMixed;
import org.vast.swe.SWEUtils;
import org.vast.swe.SWEHelper;
import org.vast.swe.helper.GeoPosHelper;


public class TestVarSizeArrays
{
    static final String TEST1_FAIL_MSG = "Wrong data block size before resizing";
    static final String TEST2_FAIL_MSG = "Wrong data block size after resizing";
    static final String WRONG_ARRAY_SIZE_MSG = "Wrong array size";
    
    GeoPosHelper fac = new GeoPosHelper();
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
             
        // test initial size
        DataBlock data = rec.createDataBlock();
        assertEquals(TEST1_FAIL_MSG, 4, data.getAtomCount());
        
        // test resizing
        int arraySize = 100;
        array.updateSize(arraySize);
        data = rec.createDataBlock();
        assertEquals(TEST2_FAIL_MSG, arraySize*3+1, data.getAtomCount());
        
        // test resizing to 0
        array.updateSize(0);
        data = rec.createDataBlock();
        assertEquals(TEST2_FAIL_MSG, 1, data.getAtomCount());
        
        // test set new datablock
        arraySize = 50;
        DataBlockMixed newData = new DataBlockMixed(
                DataBlockFactory.createBlock(new int[] {arraySize}),
                new DataBlockDouble(arraySize*3)
        );
        rec.setData(newData);
        assertEquals(arraySize, array.getComponentCount());
        
        // test set new datablock with empty array
        arraySize = 0;
        newData = new DataBlockMixed(
                DataBlockFactory.createBlock(new int[] {arraySize}),
                new DataBlockDouble(arraySize*3)
        );
        rec.setData(newData);
        assertEquals(arraySize, array.getComponentCount());
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

        DataArray array1 = fac.newDataArray();
        array1.setElementType("elt", fac.newQuantity());
        array1.setElementCount(numBins);
        rec.addComponent("array1", array1);

        DataArray array2 = fac.newDataArray();
        array2.setElementType("elt", fac.newQuantity());
        array2.setElementCount(numBins);
        rec.addComponent("array2", array2);

        DataArray array3 = fac.newDataArray();
        array3.setElementType("elt", fac.newQuantity());
        array3.setElementCount(numBins);
        rec.addComponent("array3", array3);
        
        utils.writeComponent(System.out, rec, false, true);
        
        // test initial size
        DataBlock data = rec.createDataBlock();
        assertEquals(TEST1_FAIL_MSG, 6, data.getAtomCount());
        
        // test resizing
        int arraySize = 100;
        array1.updateSize(arraySize);
        array2.updateSize();
        array3.updateSize();
        data = rec.createDataBlock();
        assertEquals(TEST2_FAIL_MSG, arraySize*3+3, data.getAtomCount()); 
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
        assertEquals(TEST1_FAIL_MSG, 3, data.getAtomCount());
        
        int innerSize = 100;
        innerArray.updateSize(innerSize);
        int outerSize = 200;
        outerArray.updateSize(outerSize);
        data = rec.createDataBlock();
        assertEquals(TEST2_FAIL_MSG, innerSize*outerSize*1+2, data.getAtomCount());        
    }
    
    
    @Test
    public void testVarSizeArray2DSetData() throws Exception
    {
        DataRecord rec = fac.newDataRecord();
        
        Count w = fac.newCount();
        w.setId("WIDTH");
        rec.addField("w", w);
        
        Count h = fac.newCount();
        h.setId("HEIGHT");
        rec.addField("h", h);  
                
        DataArray innerArray = fac.newDataArray();
        innerArray.setElementType("val", fac.newCount(DataType.BYTE));
        innerArray.setElementCount(w);
        
        DataArray outerArray = fac.newDataArray();
        outerArray.setElementType("inner", innerArray);
        outerArray.setElementCount(h);       
        
        rec.addField("outer_array", outerArray);
        
        // create and assign datablock
        int width = 320;
        int height = 240; 
        DataBlock dataBlk = rec.createDataBlock();
        dataBlk.setIntValue(0, width);
        dataBlk.setIntValue(1, height);
        ((DataBlock[])dataBlk.getUnderlyingObject())[2] = new DataBlockByte(width*height);
        
        rec.setData(dataBlk);
        assertEquals(WRONG_ARRAY_SIZE_MSG, height, outerArray.getComponentCount());
        assertEquals(WRONG_ARRAY_SIZE_MSG, width, innerArray.getComponentCount());    
    }
}
