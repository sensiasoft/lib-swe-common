package net.opengis.swe.v20;



/**
 * POJO class for XML type EncodedValuesPropertyType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public interface EncodedValues extends net.opengis.OgcProperty<Object>
{
   
    public void setAsText(DataArray array, AbstractEncoding encoding, String text);
    
    
    public String getAsText(DataArray array, AbstractEncoding encoding);
    
    
    public void setAsText(DataStream dataStream, AbstractEncoding encoding, String text);
    
    
    public String getAsText(DataStream dataStream, AbstractEncoding encoding);
}
