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

import net.opengis.gml.v32.AbstractTimeGeometricPrimitive;


/**
 * POJO class for XML type AbstractTimeGeometricPrimitiveType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public abstract class AbstractTimeGeometricPrimitiveImpl extends AbstractTimePrimitiveImpl implements AbstractTimeGeometricPrimitive
{
    private static final long serialVersionUID = 1243705128803944313L;
    protected String frame;
    
    
    public AbstractTimeGeometricPrimitiveImpl()
    {
    }
    
    
    /**
     * Gets the frame property
     */
    @Override
    public String getFrame()
    {
        return frame;
    }
    
    
    /**
     * Checks if frame is set
     */
    @Override
    public boolean isSetFrame()
    {
        return (frame != null);
    }
    
    
    /**
     * Sets the frame property
     */
    @Override
    public void setFrame(String frame)
    {
        this.frame = frame;
    }
}
