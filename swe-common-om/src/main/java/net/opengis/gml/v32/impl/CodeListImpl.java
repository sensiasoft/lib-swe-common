package net.opengis.gml.v32.impl;

import net.opengis.gml.v32.CodeList;


/**
 * POJO class for XML type CodeListType(@http://www.opengis.net/gml/3.2).
 *
 * This is a list type whose items are java.lang.String.
 */
public class CodeListImpl implements CodeList
{
    static final long serialVersionUID = 1L;
    protected String codeSpace;
    protected String[] value;
    
    
    public CodeListImpl()
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
    public String[] getValue()
    {
        return value;
    }
    
    
    /**
     * Sets the inline value
     */
    @Override
    public void setValue(String[] value)
    {
        this.value = value;        
    }
}
