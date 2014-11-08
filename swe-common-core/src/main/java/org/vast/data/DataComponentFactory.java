package org.vast.data;

import org.vast.data.AllowedTimesImpl;
import org.vast.data.AllowedTokensImpl;
import org.vast.data.AllowedValuesImpl;
import org.vast.data.CategoryRangeImpl;
import org.vast.data.CountRangeImpl;
import org.vast.data.DataStreamImpl;
import org.vast.data.NilValueImpl;
import org.vast.data.QuantityRangeImpl;
import org.vast.data.ReferenceImpl;
import org.vast.data.TextEncodingImpl;
import org.vast.data.TimeRangeImpl;
import org.vast.data.XMLEncodingImpl;
import net.opengis.swe.v20.AllowedTimes;
import net.opengis.swe.v20.AllowedTokens;
import net.opengis.swe.v20.AllowedValues;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.BinaryBlock;
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.CategoryRange;
import net.opengis.swe.v20.BinaryComponent;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.CountRange;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.DataStream;
import net.opengis.swe.v20.EncodedValues;
import net.opengis.swe.v20.Matrix;
import net.opengis.swe.v20.NilValue;
import net.opengis.swe.v20.NilValues;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.QuantityRange;
import net.opengis.swe.v20.Reference;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.TextEncoding;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.TimeRange;
import net.opengis.swe.v20.UnitReference;
import net.opengis.swe.v20.Vector;
import net.opengis.swe.v20.XMLEncoding;
import net.opengis.swe.v20.Factory;


public class DataComponentFactory implements Factory
{
    
    
    
    public DataComponentFactory()
    {
    }
    
    
    public DataRecord newDataRecord()
    {
        return new DataRecordImpl();
    }
    
    
    public Vector newVector()
    {
        return new VectorImpl();
    }
    
    
    public DataArray newDataArray()
    {
        return new DataArrayImpl();
    }
    
    
    public Matrix newMatrix()
    {
        return new MatrixImpl();
    }
    
    
    public DataStream newDataStream()
    {
        return new DataStreamImpl();
    }
    
    
    public BinaryBlock newBinaryBlock()
    {
        return new BinaryBlockImpl();
    }
    
    
    public BinaryEncoding newBinaryEncoding()
    {
        return new BinaryEncodingImpl();
    }
    
    
    public BinaryComponent newBinaryComponent()
    {
        return new BinaryComponentImpl();
    }
    
    
    public DataChoice newDataChoice()
    {
        return new DataChoiceImpl();
    }
    
    
    public Count newCount()
    {
        return new CountImpl();
    }
    
    
    public CategoryRange newCategoryRange()
    {
        return new CategoryRangeImpl();
    }
    
    
    public QuantityRange newQuantityRange()
    {
        return new QuantityRangeImpl();
    }
    
    
    public Time newTime()
    {
        return new TimeImpl();
    }
    
    
    public TimeRange newTimeRange()
    {
        return new TimeRangeImpl();
    }
    
    
    public Boolean newBoolean()
    {
        return new BooleanImpl();
    }
    
    
    public Text newText()
    {
        return new TextImpl();
    }
    
    
    public Category newCategory()
    {
        return new CategoryImpl();
    }
    
    
    public Quantity newQuantity()
    {
        return new QuantityImpl();
    }
    
    
    public CountRange newCountRange()
    {
        return new CountRangeImpl();
    }
    
    
    public NilValues newNilValues()
    {
        return new NilValuesImpl();
    }
    
    
    public AllowedTokens newAllowedTokens()
    {
        return new AllowedTokensImpl();
    }
    
    
    public AllowedValues newAllowedValues()
    {
        return new AllowedValuesImpl();
    }
    
    
    public AllowedTimes newAllowedTimes()
    {
        return new AllowedTimesImpl();
    }
    
    
    public XMLEncoding newXMLEncoding()
    {
        return new XMLEncodingImpl();
    }
    
    
    public TextEncoding newTextEncoding()
    {
        return new TextEncodingImpl();
    }
    
    
    public UnitReference newUnitReference()
    {
        return new UnitReferenceImpl();
    }
    
    
    public NilValue newNilValue()
    {
        return new NilValueImpl();
    }
    
    
    public EncodedValues newEncodedValuesProperty()
    {
        return new EncodedValuesImpl();
    }
    
    
    public Reference newReference()
    {
        return new ReferenceImpl();
    }
}
