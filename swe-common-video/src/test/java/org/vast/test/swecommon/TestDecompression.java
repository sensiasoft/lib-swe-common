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
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.test.swecommon;

import static org.junit.Assert.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import jj2000.j2k.decoder.Decoder;
import jj2000.j2k.io.RandomAccessIO;
import jj2000.j2k.util.ISRandomAccessIO;
import jj2000.j2k.util.ParameterList;
import net.opengis.swe.v20.DataBlock;
import org.vast.codec.JP2KDecoder;
import org.vast.codec.JP2KStreamDecoder;
import org.vast.data.CountImpl;
import org.vast.data.DataArrayImpl;
import org.vast.data.DataRecordImpl;
import org.vast.swe.DataInputStreamBI;


public class TestDecompression
{
    
    protected static ParameterList getDefaultParameters()
    {
        String[][] param = Decoder.getAllParameters();
        ParameterList defpl = new ParameterList();
        
        for (int i=param.length-1; i>=0; i--)
        {
            if (param[i][3] != null)
                defpl.put(param[i][0], param[i][3]);
        }

        // Create parameter list using defaults
        return new ParameterList(defpl);
    }
    
    
    //@Test
    public void testJP2KDecompression() throws Exception {
    	InputStream is = getClass().getResourceAsStream("/file1.jp2");
    	RandomAccessIO raio = new ISRandomAccessIO(is);
    	JP2KDecoder decoder = new JP2KDecoder();
    	byte [][] decodedImage = decoder.decode(raio);
    	int bandSize = 768 * 512;
    	assertEquals("Wrong number of bands returned.", 3, decodedImage.length);
    	assertNotNull("Band 0 is null.", decodedImage[0]);
    	assertEquals("Wrong size of decoded image.", bandSize, decodedImage[0].length);
    	assertNotNull("Band 1 is null.", decodedImage[1]);
    	assertEquals("Wrong size of decoded image.", bandSize, decodedImage[1].length);
    	assertNotNull("Band 2 is null.", decodedImage[2]);
    	assertEquals("Wrong size of decoded image.", bandSize, decodedImage[2].length);
    }
    
    // The following method was disabled by Chris Dillard on 2009-12-09 for these reasons:
    //   1. Hard coded file path is bad.  (Maven runs JUnit tests by default.)
    //   2. Test seems invalid any way... throws NullPointerExceptions.
    // Put in a simple replacement above.
    public void DISABLED_testJP2KDecompression() throws Exception
    {
        String filePath = "F:\\Data\\ErdasTigerShark\\2008-07-21\\images\\A121665541042.jp2";//.small.jpc";
        File file = new File(filePath);
        FileInputStream is = new FileInputStream(file);

        DataArrayImpl imgData = new DataArrayImpl();
        DataRecordImpl pixelData = new DataRecordImpl(3);
        pixelData.addComponent("red", new CountImpl());
        pixelData.addComponent("green", new CountImpl());
        pixelData.addComponent("blue", new CountImpl());
        imgData.addComponent("pixel", pixelData);
        
        JP2KStreamDecoder decoder = new JP2KStreamDecoder(file.length());
        decoder.decode(new DataInputStreamBI(is), imgData);
        System.out.println(imgData.getData());
        
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream("D:\\subset.raw"));
        for (int p=0; p<imgData.getComponentCount(); p++)
        {
            DataBlock dataBlk = imgData.getComponent(p).getData();
            
            /*int tmp1 = dataBlk.getIntValue(0)+128;
            int tmp2 = dataBlk.getIntValue(1)+128;
            int tmp3 = dataBlk.getIntValue(2)+128;
            
            os.write(tmp1 > 255 ? 255 : (tmp1 < 0 ? 0 : tmp1));
            os.write(tmp2 > 255 ? 255 : (tmp2 < 0 ? 0 : tmp2));
            os.write(tmp3 > 255 ? 255 : (tmp3 < 0 ? 0 : tmp3));*/
            os.write(dataBlk.getByteValue(0));
            os.write(dataBlk.getByteValue(1));
            os.write(dataBlk.getByteValue(2));
        }        
        os.close();
    }
    
    
}
