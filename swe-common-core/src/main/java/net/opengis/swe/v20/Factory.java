package net.opengis.swe.v20;



public interface Factory
{
        
    public DataRecord newDataRecord();
    
    
    public Vector newVector();
    
    
    public DataArray newDataArray();
    
    
    public Matrix newMatrix();
    
    
    public DataStream newDataStream();
    
    
    public BinaryBlock newBinaryBlock();
    
    
    public BinaryEncoding newBinaryEncoding();
    
    
    public BinaryComponent newBinaryComponent();
    
    
    public DataChoice newDataChoice();
    
    
    public Count newCount();
    
    
    public CategoryRange newCategoryRange();
    
    
    public QuantityRange newQuantityRange();
    
    
    public Time newTime();
    
    
    public TimeRange newTimeRange();
    
    
    public Boolean newBoolean();
    
    
    public Text newText();
    
    
    public Category newCategory();
    
    
    public Quantity newQuantity();
    
    
    public CountRange newCountRange();
    
    
    public NilValues newNilValues();
    
    
    public AllowedTokens newAllowedTokens();
    
    
    public AllowedValues newAllowedValues();
    
    
    public AllowedTimes newAllowedTimes();
    
    
    public XMLEncoding newXMLEncoding();
    
    
    public TextEncoding newTextEncoding();
    
    
    public UnitReference newUnitReference();
    
    
    public NilValue newNilValue();
    
    
    public EncodedValues newEncodedValuesProperty();
    
    
    public Reference newReference();
}
