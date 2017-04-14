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

import net.opengis.gml.v32.LinearRing;


/**
 * POJO class for XML type LinearRingType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public class LinearRingImpl implements LinearRing
{
    private static final long serialVersionUID = 5727812095477893201L;
    protected double[] posList;
    
    
    public LinearRingImpl()
    {
    }


    @Override
    public double[] getPosList()
    {
        return posList;
    }


    @Override
    public void setPosList(double[] posList)
    {
        this.posList = posList;        
    }    
    
    
    @Override
    public boolean isSetPosList()
    {
        return (posList != null);
    }
    
}
