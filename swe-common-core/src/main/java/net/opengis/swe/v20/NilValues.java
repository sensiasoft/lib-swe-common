package net.opengis.swe.v20;

import java.util.List;


/**
 * POJO class for XML type NilValuesType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface NilValues extends AbstractSWE
{
    
    
    /**
     * Gets the list of nilValue properties
     */
    public List<NilValue> getNilValueList();
    
    
    /**
     * Returns number of nilValue properties
     */
    public int getNumNilValues();
    
    
    /**
     * Adds a new nilValue property
     */
    public void addNilValue(NilValue nilValue);
}
