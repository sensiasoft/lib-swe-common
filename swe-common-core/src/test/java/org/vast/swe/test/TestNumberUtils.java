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
        
        assertTrue(NumberUtils.ulpEquals(val1, val2));
        assertTrue(NumberUtils.ulpEquals(val1, val1+Math.ulp(val1)));
        assertTrue(NumberUtils.ulpEquals(0.0f, Math.signum(-1f)*0.0f));
        assertTrue(NumberUtils.ulpEquals(Float.NaN, Float.NaN));
        assertTrue(NumberUtils.ulpEquals(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY));
        
        assertFalse(NumberUtils.ulpEquals(f, f + 2*Math.ulp(f)));
        assertFalse(NumberUtils.ulpEquals(f, 1.0000001*f));
        assertFalse(NumberUtils.ulpEquals(f, f+1e-6f));
        assertFalse(NumberUtils.ulpEquals(Float.NaN, Float.POSITIVE_INFINITY));
        assertFalse(NumberUtils.ulpEquals(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY));
        
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
        
        assertTrue(NumberUtils.ulpEquals(val1, val2));
        assertTrue(NumberUtils.ulpEquals(val1, val1+Math.ulp(val1)));
        assertTrue(NumberUtils.ulpEquals(0.0, Math.signum(-1.0)*0.0));
        assertTrue(NumberUtils.ulpEquals(Double.NaN, Double.NaN));
        assertTrue(NumberUtils.ulpEquals(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        
        assertFalse(NumberUtils.ulpEquals(f, f + 2*Math.ulp(f)));
        assertFalse(NumberUtils.ulpEquals(f, 1.0000001*f));
        assertFalse(NumberUtils.ulpEquals(f, f+1e-10));
        assertFalse(NumberUtils.ulpEquals(Double.NaN, Double.POSITIVE_INFINITY));
        assertFalse(NumberUtils.ulpEquals(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY));
    }
}
