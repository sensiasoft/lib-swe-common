package net.opengis.gml.v32;



/**
 * POJO class for XML type CodeListType(@http://www.opengis.net/gml/3.2).
 *
 * This is a list type whose items are java.lang.String.
 */
public interface CodeList
{
    
    
    /**
     * Gets the codeSpace property
     */
    public String getCodeSpace();
    
    
    /**
     * Checks if codeSpace is set
     */
    public boolean isSetCodeSpace();
    
    
    /**
     * Sets the codeSpace property
     */
    public void setCodeSpace(String codeSpace);


    /**
     * Gets the inline value
     */
    public String[] getValue();
    
    
    /**
     * Sets the inline value
     */
    public void setValue(String[] value);    
}
