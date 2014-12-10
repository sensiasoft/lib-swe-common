package net.opengis.swe.v20;


/**
 * POJO class for XML type DataStreamType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface DataStream extends AbstractSWEIdentifiable, BlockComponent
{
    
    @Override
    public DataStream copy();
    
    
    /**
     * Checks if elementCount is set
     */
    public boolean isSetElementCount();
    
}
