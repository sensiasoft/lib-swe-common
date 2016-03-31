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
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.CountRange;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.Vector;
import org.junit.Test;
import org.vast.swe.SWEUtils;
import org.vast.swe.ScalarIndexer;
import org.vast.swe.helper.GeoPosHelper;


public class TestScalarIndexer
{
    GeoPosHelper fac = new GeoPosHelper();
    
    
    @Test
    public void testFixedRecord()
    {
        DataRecord rec = fac.newDataRecord();
        Time t = fac.newTimeStampIsoUTC();
        rec.addField("time", t);
        Quantity q = fac.newQuantity();
        rec.addField("q", q);
        Count c = fac.newCount();
        rec.addField("c", c);
        
        ScalarIndexer indexer;
        
        indexer = new ScalarIndexer(rec, t);
        assertEquals(0, indexer.getDataIndex(rec.createDataBlock()));
        
        indexer = new ScalarIndexer(rec, q);
        assertEquals(1, indexer.getDataIndex(rec.createDataBlock()));
        
        indexer = new ScalarIndexer(rec, c);
        assertEquals(2, indexer.getDataIndex(rec.createDataBlock()));
    }
    
    
    @Test
    public void testChoiceOfScalarsInRecord() throws Exception
    {
        DataRecord rec = fac.newDataRecord();
        Time t = fac.newTime();
        rec.addField("time", t);
        
        DataChoice choice = fac.newDataChoice();
        Quantity q = fac.newQuantity();
        choice.addItem("q", q);
        Count c = fac.newCount();
        choice.addItem("c", c);
        Text tx = fac.newText();
        choice.addItem("t", tx);
        rec.addComponent("choice", choice);
        
        Boolean b = fac.newBoolean();
        rec.addComponent("b", b);
        
        new SWEUtils(SWEUtils.V2_0).writeComponent(System.out, rec, true, true);
        
        
        ScalarIndexer indexer;
        DataBlock dataBlk;
        
        // choice 0
        choice.setSelectedItem(0);
        dataBlk = rec.createDataBlock();
        indexer = new ScalarIndexer(rec, t);
        assertEquals(0, indexer.getDataIndex(dataBlk));
        indexer = new ScalarIndexer(rec, q);
        assertEquals(2, indexer.getDataIndex(dataBlk));
        indexer = new ScalarIndexer(rec, c);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(rec, tx);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(rec, b);
        assertEquals(3, indexer.getDataIndex(dataBlk));
        
        // choice 1
        choice.setSelectedItem(1);
        dataBlk = rec.createDataBlock();
        indexer = new ScalarIndexer(rec, t);
        assertEquals(0, indexer.getDataIndex(dataBlk));
        indexer = new ScalarIndexer(rec, c);
        assertEquals(2, indexer.getDataIndex(dataBlk));
        indexer = new ScalarIndexer(rec, q);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(rec, tx);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(rec, b);
        assertEquals(3, indexer.getDataIndex(dataBlk));
        
        // choice 2
        choice.setSelectedItem(2);
        dataBlk = rec.createDataBlock();
        indexer = new ScalarIndexer(rec, t);        
        assertEquals(0, indexer.getDataIndex(dataBlk));
        indexer = new ScalarIndexer(rec, tx);
        assertEquals(2, indexer.getDataIndex(dataBlk));
        indexer = new ScalarIndexer(rec, q);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(rec, c);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(rec, b);
        assertEquals(3, indexer.getDataIndex(dataBlk));
    }
    
    
    @Test
    public void testChoiceWithRecords() throws Exception
    {
        DataChoice choice = fac.newDataChoice();
        
        Quantity q = fac.newQuantity();
        choice.addItem("q", q);        
        
        DataRecord rec = fac.newDataRecord();
        Time t = fac.newTime();
        rec.addField("time", t);
        CountRange c = fac.newCountRange();
        rec.addField("c", c);
        Text tx = fac.newText();
        rec.addField("t", tx);
        choice.addItem("rec", rec);
        
        Boolean b = fac.newBoolean();
        choice.addItem("b", b);
        
        Vector vec = fac.newLocationVectorECEF(null);
        choice.addItem("vec", vec);
        
        new SWEUtils(SWEUtils.V2_0).writeComponent(System.out, choice, true, true);
        
        
        ScalarIndexer indexer;
        DataBlock dataBlk;
        
        // choice 0
        choice.setSelectedItem(0);
        dataBlk = choice.createDataBlock();
        indexer = new ScalarIndexer(choice, q);
        assertEquals(1, indexer.getDataIndex(dataBlk));
        indexer = new ScalarIndexer(choice, t);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(choice, tx);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(choice, b);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(choice, vec.getCoordinate("x"));
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        
        // choice 1
        choice.setSelectedItem(1);
        dataBlk = choice.createDataBlock();
        indexer = new ScalarIndexer(choice, q);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(choice, t);
        assertEquals(1, indexer.getDataIndex(dataBlk));
        indexer = new ScalarIndexer(choice, tx);
        assertEquals(4, indexer.getDataIndex(dataBlk));
        indexer = new ScalarIndexer(choice, b);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(choice, vec.getCoordinate("x"));
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        
        // choice 2
        choice.setSelectedItem(2);
        dataBlk = choice.createDataBlock();
        indexer = new ScalarIndexer(choice, q);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(choice, t);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(choice, tx);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(choice, b);
        assertEquals(1, indexer.getDataIndex(dataBlk));
        indexer = new ScalarIndexer(choice, vec.getCoordinate("x"));
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        
        // choice 3
        choice.setSelectedItem(3);
        dataBlk = choice.createDataBlock();
        indexer = new ScalarIndexer(choice, q);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(choice, t);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(choice, tx);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(choice, b);
        assertTrue("Index should be negative", indexer.getDataIndex(dataBlk) < -1000);
        indexer = new ScalarIndexer(choice, vec.getCoordinate("x"));
        assertEquals(1, indexer.getDataIndex(dataBlk));
        indexer = new ScalarIndexer(choice, vec.getCoordinate("y"));
        assertEquals(2, indexer.getDataIndex(dataBlk));
        indexer = new ScalarIndexer(choice, vec.getCoordinate("z"));
        assertEquals(3, indexer.getDataIndex(dataBlk));        
    }

}
