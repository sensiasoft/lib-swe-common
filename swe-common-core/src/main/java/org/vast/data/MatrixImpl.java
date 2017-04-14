/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.Matrix;
import net.opengis.swe.v20.ScalarComponent;


/**
 * POJO class for XML type MatrixType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class MatrixImpl extends DataArrayImpl implements Matrix
{
    private static final long serialVersionUID = 991093969460307371L;
    protected String referenceFrame;
    protected String localFrame;
    
    
    public MatrixImpl()
    {
    }
    
    
    public MatrixImpl(int arraySize)
    {
        super(arraySize);
    }
    
    
    @Override
    public MatrixImpl copy()
    {
        MatrixImpl newObj = new MatrixImpl();
        super.copyTo(newObj);
        newObj.referenceFrame = this.referenceFrame;
        newObj.localFrame = this.localFrame;
        return newObj;
    }
    
    
    /**
     * Sets the elementType property
     */
    @Override
    public void setElementType(String name, DataComponent component)
    {
        if (!(component instanceof ScalarComponent) && !(component instanceof Matrix))
            throw new IllegalArgumentException("A matrix can only have scalar elements or a nested matrix");
        
        super.setElementType(name, component);
    }
    
    
    /**
     * Gets the referenceFrame property
     */
    @Override
    public String getReferenceFrame()
    {
        return referenceFrame;
    }
    
    
    /**
     * Checks if referenceFrame is set
     */
    @Override
    public boolean isSetReferenceFrame()
    {
        return (referenceFrame != null);
    }
    
    
    /**
     * Sets the referenceFrame property
     */
    @Override
    public void setReferenceFrame(String referenceFrame)
    {
        this.referenceFrame = referenceFrame;
    }
    
    
    /**
     * Gets the localFrame property
     */
    @Override
    public String getLocalFrame()
    {
        return localFrame;
    }
    
    
    /**
     * Checks if localFrame is set
     */
    @Override
    public boolean isSetLocalFrame()
    {
        return (localFrame != null);
    }
    
    
    /**
     * Sets the localFrame property
     */
    @Override
    public void setLocalFrame(String localFrame)
    {
        this.localFrame = localFrame;
    }
}
