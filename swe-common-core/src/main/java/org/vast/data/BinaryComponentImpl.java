package org.vast.data;

import org.vast.cdm.common.DataType;
import net.opengis.HasCopy;
import net.opengis.swe.v20.BinaryComponent;


/**
 * POJO class for XML type ComponentType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class BinaryComponentImpl extends AbstractSWEImpl implements BinaryComponent, HasCopy
{
    public static String DATATYPE_URI_PREFIX = "http://www.opengis.net/def/dataType/OGC/0/";
    static final long serialVersionUID = 1L;
    protected String encryption;
    protected Integer significantBits;
    protected Integer bitLength;
    protected Integer byteLength;
    protected String dataType = "";
    protected String ref = "";
    protected DataType cdmDataType;
    
    
    public BinaryComponentImpl()
    {
    }
    
    
    @Override
    public BinaryComponentImpl copy()
    {
        BinaryComponentImpl newObj = new BinaryComponentImpl();
        newObj.encryption = this.encryption;
        newObj.significantBits = this.significantBits;
        newObj.bitLength = this.bitLength;
        newObj.byteLength = this.byteLength;
        newObj.dataType = this.dataType;
        newObj.ref = this.ref;
        return newObj;
    }
    
    
    /**
     * Gets the encryption property
     */
    @Override
    public String getEncryption()
    {
        return encryption;
    }
    
    
    /**
     * Checks if encryption is set
     */
    @Override
    public boolean isSetEncryption()
    {
        return (encryption != null);
    }
    
    
    /**
     * Sets the encryption property
     */
    @Override
    public void setEncryption(String encryption)
    {
        this.encryption = encryption;
    }
    
    
    /**
     * Gets the significantBits property
     */
    @Override
    public int getSignificantBits()
    {
        return significantBits;
    }
    
    
    /**
     * Checks if significantBits is set
     */
    @Override
    public boolean isSetSignificantBits()
    {
        return (significantBits != null);
    }
    
    
    /**
     * Sets the significantBits property
     */
    @Override
    public void setSignificantBits(int significantBits)
    {
        this.significantBits = significantBits;
    }
    
    
    /**
     * Unsets the significantBits property
     */
    @Override
    public void unSetSignificantBits()
    {
        this.significantBits = null;
    }
    
    
    /**
     * Gets the bitLength property
     */
    @Override
    public int getBitLength()
    {
        return bitLength;
    }
    
    
    /**
     * Checks if bitLength is set
     */
    @Override
    public boolean isSetBitLength()
    {
        return (bitLength != null);
    }
    
    
    /**
     * Sets the bitLength property
     */
    @Override
    public void setBitLength(int bitLength)
    {
        this.bitLength = bitLength;
    }
    
    
    /**
     * Unsets the bitLength property
     */
    @Override
    public void unSetBitLength()
    {
        this.bitLength = null;
    }
    
    
    /**
     * Gets the byteLength property
     */
    @Override
    public int getByteLength()
    {
        return byteLength;
    }
    
    
    /**
     * Checks if byteLength is set
     */
    @Override
    public boolean isSetByteLength()
    {
        return (byteLength != null);
    }
    
    
    /**
     * Sets the byteLength property
     */
    @Override
    public void setByteLength(int byteLength)
    {
        this.byteLength = byteLength;
    }
    
    
    /**
     * Unsets the byteLength property
     */
    @Override
    public void unSetByteLength()
    {
        this.byteLength = null;
    }
    
    
    /**
     * Gets the dataType property
     */
    @Override
    public String getDataType()
    {
        return dataType;
    }
    
    
    /**
     * Sets the dataType property
     */
    @Override
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }
    
    
    /**
     * Gets the ref property
     */
    @Override
    public String getRef()
    {
        return ref;
    }
    
    
    /**
     * Sets the ref property
     */
    @Override
    public void setRef(String ref)
    {
        this.ref = ref;
    }


    public DataType getCdmDataType()
    {
        if (cdmDataType == null)
        {
            if (dataType.endsWith("/boolean"))
            {
                cdmDataType = DataType.BOOLEAN;
            }
            else if (dataType.endsWith("/signedByte"))
            {
                cdmDataType = DataType.BYTE;
            }
            else if (dataType.endsWith("/unsignedByte"))
            {
                cdmDataType = DataType.UBYTE;
            }
            else if (dataType.endsWith("/signedShort"))
            {
                cdmDataType = DataType.SHORT;
            }
            else if (dataType.endsWith("/unsignedShort"))
            {
                cdmDataType = DataType.USHORT;
            }
            else if (dataType.endsWith("/signedInt"))
            {
                cdmDataType = DataType.INT;
            }
            else if (dataType.endsWith("/unsignedInt"))
            {
                cdmDataType = DataType.UINT;
            }
            else if (dataType.endsWith("/signedLong"))
            {
                cdmDataType = DataType.LONG;
            }
            else if (dataType.endsWith("/unsignedLong"))
            {
                cdmDataType = DataType.ULONG;
            }
            else if (dataType.endsWith("/float32"))
            {
                cdmDataType = DataType.FLOAT;
            }
            else if (dataType.endsWith("/double") || dataType.endsWith("/float64"))
            {
                cdmDataType = DataType.DOUBLE;
            }
            else if (dataType.endsWith("/string-utf-8"))
            {
                cdmDataType = DataType.UTF_STRING;
            }
            else if (dataType.endsWith("/string-ascii"))
            {
                cdmDataType = DataType.ASCII_STRING;
            }
            else
                cdmDataType = DataType.DISCARD;
        }
        
        return cdmDataType;
    }
    
    
    public void setCdmDataType(DataType cdmDataType)
    {
        switch (cdmDataType)
        {
            case BOOLEAN:
                this.dataType = DATATYPE_URI_PREFIX + "boolean";
                break;
            
            case BYTE:
                this.dataType = DATATYPE_URI_PREFIX + "signedByte";
                break;
                
            case UBYTE:
                this.dataType = DATATYPE_URI_PREFIX + "unsignedByte";
                break;
                
            case SHORT:
                this.dataType = DATATYPE_URI_PREFIX + "signedShort";
                break;
                
            case USHORT:
                this.dataType = DATATYPE_URI_PREFIX + "unsignedShort";
                break;
                
            case INT:
                this.dataType = DATATYPE_URI_PREFIX + "signedInt";
                break;
                
            case UINT:
                this.dataType = DATATYPE_URI_PREFIX + "unsignedInt";
                break;
                
            case LONG:
                this.dataType = DATATYPE_URI_PREFIX + "signedLong";
                break;
                
            case ULONG:
                this.dataType = DATATYPE_URI_PREFIX + "unsignedLong";
                break;
                
            case FLOAT:
                this.dataType = DATATYPE_URI_PREFIX + "float32";
                break;
                
            case DOUBLE:
                this.dataType = DATATYPE_URI_PREFIX + "double";
                break;
                
            case UTF_STRING:
            case ASCII_STRING:
                this.dataType = DATATYPE_URI_PREFIX + "string-utf-8";
                break;
                
            default:
                throw new RuntimeException("Unsupported datatype " + dataType);
        }
    }
}
