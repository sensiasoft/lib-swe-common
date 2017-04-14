/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32;

import java.io.Serializable;

/**
 * POJO class for XML type TimeIntervalLengthType(@http://www.opengis.net/gml/3.2).
 *
 */
@SuppressWarnings("javadoc")
public interface TimeIntervalLength extends Serializable
{
    
    /**
     * Gets the unit property
     */
    public TimeUnit getUnit();
    
    
    /**
     * Sets the unit property
     */
    public void setUnit(TimeUnit unit);
    
    
    /**
     * Gets the radix property
     */
    public int getRadix();
    
    
    /**
     * Checks if radix is set
     */
    public boolean isSetRadix();
    
    
    /**
     * Sets the radix property
     */
    public void setRadix(int radix);
    
    
    /**
     * Unsets the radix property
     */
    public void unSetRadix();
    
    
    /**
     * Gets the factor property
     */
    public int getFactor();
    
    
    /**
     * Checks if factor is set
     */
    public boolean isSetFactor();
    
    
    /**
     * Sets the factor property
     */
    public void setFactor(int factor);
    
    
    /**
     * Unsets the factor property
     */
    public void unSetFactor();
    
    
    /**
     * Gets the inline value
     */
    public double getValue();
    
    
    /**
     * Sets the inline value
     */
    public void setValue(double value);
}
