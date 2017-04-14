/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32.impl;

import net.opengis.gml.v32.TimeIntervalLength;
import net.opengis.gml.v32.TimeUnit;


/**
 * POJO class for XML type TimeIntervalLengthType(@http://www.opengis.net/gml/3.2).
 *
 */
public class TimeIntervalLengthImpl implements TimeIntervalLength
{
    private static final long serialVersionUID = -2034897023968381686L;
    protected TimeUnit unit = TimeUnit.SECOND;
    protected Integer radix;
    protected Integer factor;
    protected double value;
    
    
    public TimeIntervalLengthImpl()
    {
    }
    
    
    /**
     * Gets the unit property
     */
    @Override
    public TimeUnit getUnit()
    {
        return unit;
    }
    
    
    /**
     * Sets the unit property
     */
    @Override
    public void setUnit(TimeUnit unit)
    {
        this.unit = unit;
    }
    
    
    /**
     * Gets the radix property
     */
    @Override
    public int getRadix()
    {
        return radix;
    }
    
    
    /**
     * Checks if radix is set
     */
    @Override
    public boolean isSetRadix()
    {
        return (radix != null);
    }
    
    
    /**
     * Sets the radix property
     */
    @Override
    public void setRadix(int radix)
    {
        this.radix = radix;
    }
    
    
    /**
     * Unsets the radix property
     */
    @Override
    public void unSetRadix()
    {
        this.radix = null;
    }
    
    
    /**
     * Gets the factor property
     */
    @Override
    public int getFactor()
    {
        return factor;
    }
    
    
    /**
     * Checks if factor is set
     */
    @Override
    public boolean isSetFactor()
    {
        return (factor != null);
    }
    
    
    /**
     * Sets the factor property
     */
    @Override
    public void setFactor(int factor)
    {
        this.factor = factor;
    }
    
    
    /**
     * Unsets the factor property
     */
    @Override
    public void unSetFactor()
    {
        this.factor = null;
    }
    
    
    /**
     * Gets the inline value
     */
    @Override
    public double getValue()
    {
        return value;
    }
    
    
    /**
     * Sets the inline value
     */
    @Override
    public void setValue(double value)
    {
        this.value = value;
    }
}
