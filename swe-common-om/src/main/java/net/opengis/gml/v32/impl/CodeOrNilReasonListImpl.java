package net.opengis.gml.v32.impl;

import net.opengis.gml.v32.CodeOrNilReasonList;


/**
 * POJO class for XML type CodeOrNilReasonListType(@http://www.opengis.net/gml/3.2).
 *
 * This is a list type whose items are net.opengis.gml.v32.NameOrNilReason.
 */
public class CodeOrNilReasonListImpl implements CodeOrNilReasonList
{
    static final long serialVersionUID = 1L;
    protected String codeSpace;
    
    
    public CodeOrNilReasonListImpl()
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
}
