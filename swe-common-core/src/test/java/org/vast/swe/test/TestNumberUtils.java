/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.vast.util.NumberUtils;


public class TestNumberUtils
{

    @Test
    public void testUlpEqualFloat()
    {
        float f = 0.1f;
        float val1 = 0.0f;
        float val2 = 0.0f;
        
        val1 = f * 10;
        for (int i = 0; i < 10; ++i)
            val2 += f;
        
        assertTrue(NumberUtils.ulpEqual(val1, val2));
        assertTrue(NumberUtils.ulpEqual(val1, val1+Math.ulp(val1)));
        
        assertFalse(NumberUtils.ulpEqual(f, f + 2*Math.ulp(f)));
        assertFalse(NumberUtils.ulpEqual(f, 1.0000001*f));
        assertFalse(NumberUtils.ulpEqual(f, f+1e-6f));
    }
    
    
    @Test
    public void testUlpEqualDouble()
    {
        double f = 0.1;
        double val1 = 0.0;
        double val2 = 0.0;
        
        val1 = f * 10.;
        for (int i = 0; i < 10; ++i)
            val2 += f;
        
        assertTrue(NumberUtils.ulpEqual(val1, val2));
        assertTrue(NumberUtils.ulpEqual(val1, val1+Math.ulp(val1)));
        
        assertFalse(NumberUtils.ulpEqual(f, f + 2*Math.ulp(f)));
        assertFalse(NumberUtils.ulpEqual(f, 1.0000001*f));
        assertFalse(NumberUtils.ulpEqual(f, f+1e-10));
    }
}
