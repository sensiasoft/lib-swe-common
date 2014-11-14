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
}
