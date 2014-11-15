package org.vast.data;

import net.opengis.swe.v20.*;


public class SWEFactory implements Factory
{       
    
    public SWEFactory()
    {
    }
    
    
    public DataRecord newDataRecord()
    {
        return new DataRecordImpl();
    }
    
    
    public DataRecord newDataRecord(int recordSize)
    {
        return new DataRecordImpl(recordSize);
    }
    
    
    public Vector newVector()
    {
        return new VectorImpl();
    }
    
    
    public DataArray newDataArray()
    {
        return new DataArrayImpl();
    }
    
    
    public DataArray newDataArray(int arraySize)
    {
        return new DataArrayImpl(arraySize);
    }
    
    
    public Matrix newMatrix()
    {
        return new MatrixImpl();
    }
    
    
    public Matrix newMatrix(int arraySize)
    {
        return new MatrixImpl(arraySize);
    }
    
    
    public DataStream newDataStream()
    {
        return new DataList();
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
    
    
    public Count newCount(DataType dataType)
    {
        return new CountImpl(dataType);
    }
    
    
    public CategoryRange newCategoryRange()
    {
        return new CategoryRangeImpl();
    }
    
    
    public QuantityRange newQuantityRange()
    {
        return new QuantityRangeImpl();
    }
    
    
    public QuantityRange newQuantityRange(DataType dataType)
    {
        return new QuantityRangeImpl(dataType);
    }
    
    
    public Time newTime()
    {
        return new TimeImpl();
    }
    
    
    public Time newTime(DataType dataType)
    {
        return new TimeImpl(dataType);
    }
    
    
    public TimeRange newTimeRange()
    {
        return new TimeRangeImpl();
    }
    
    
    public TimeRange newTimeRange(DataType dataType)
    {
        return new TimeRangeImpl(dataType);
    }
    
    
    public net.opengis.swe.v20.Boolean newBoolean()
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
    
    
    public Quantity newQuantity(DataType dataType)
    {
        return new QuantityImpl(dataType);
    }
    
    
    public CountRange newCountRange()
    {
        return new CountRangeImpl();
    }
    
    
    public CountRange newCountRange(DataType dataType)
    {
        return new CountRangeImpl(dataType);
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
}
