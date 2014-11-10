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
    static final long serialVersionUID = 1L;
    protected String referenceFrame;
    protected String localFrame;
    
    
    public MatrixImpl()
    {
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
        if (!(component instanceof ScalarComponent))
            throw new IllegalArgumentException("A matrix can only have scalar elements");
        
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
