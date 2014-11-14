package net.opengis.gml.v32.impl;

import net.opengis.gml.v32.Code;


/**
 * POJO class for XML type CodeType(@http://www.opengis.net/gml/3.2).
 *
 */
public class CodeImpl implements Code
{
    static final long serialVersionUID = 1L;
    protected String codeSpace;
    protected String value;
    
    
    public CodeImpl()
    {
    }
    
    
    /**
     * Gets the codeSpace property
     */
    @Override
    public String getCodeSpace()
    {
        return codeSpace;
    }
    
    
    /**
     * Checks if codeSpace is set
     */
    @Override
    public boolean isSetCodeSpace()
    {
        return (codeSpace != null);
    }
    
    
    /**
     * Sets the codeSpace property
     */
    @Override
    public void setCodeSpace(String codeSpace)
    {
        this.codeSpace = codeSpace;
    }
    
    
    /**
     * Gets the inline value
     */
    @Override
    public String getValue()
    {
        return value;
    }
    
    
    /**
     * Sets the inline value
     */
    @Override
    public void setValue(String value)
    {
        this.value = value;
    }
}
