package net.opengis.gml.v32.impl;

import net.opengis.gml.v32.CodeWithAuthority;


/**
 * POJO class for XML type CodeWithAuthorityType(@http://www.opengis.net/gml/3.2).
 *
 */
public class CodeWithAuthorityImpl extends CodeImpl implements CodeWithAuthority
{
    static final long serialVersionUID = 1L;
    
    
    public CodeWithAuthorityImpl()
    {
    }
    
    
    public CodeWithAuthorityImpl(String codeSpace, String value)
    {
        this.codeSpace = codeSpace;
        this.value = value;
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
}
