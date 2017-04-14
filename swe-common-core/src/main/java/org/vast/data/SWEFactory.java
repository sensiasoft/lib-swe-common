/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import net.opengis.swe.v20.*;


public class SWEFactory implements Factory
{       
    
    public SWEFactory()
    {
    }
    
    
    @Override
    public DataRecord newDataRecord()
    {
        return new DataRecordImpl();
    }
    
    
    public DataRecord newDataRecord(int recordSize)
    {
        return new DataRecordImpl(recordSize);
    }
    
    
    @Override
    public Vector newVector()
    {
        return new VectorImpl();
    }
    
    
    @Override
    public DataArray newDataArray()
    {
        return new DataArrayImpl();
    }
    
    
    public DataArray newDataArray(int arraySize)
    {
        return new DataArrayImpl(arraySize);
    }
    
    
    @Override
    public Matrix newMatrix()
    {
        return new MatrixImpl();
    }
    
    
    public Matrix newMatrix(int arraySize)
    {
        return new MatrixImpl(arraySize);
    }
    
    
    @Override
    public DataStream newDataStream()
    {
        return new DataList();
    }
    
    
    @Override
    public BinaryBlock newBinaryBlock()
    {
        return new BinaryBlockImpl();
    }
    
    
    @Override
    public BinaryEncoding newBinaryEncoding()
    {
        return new BinaryEncodingImpl();
    }
    
    
    @Override
    public BinaryComponent newBinaryComponent()
    {
        return new BinaryComponentImpl();
    }
    
    
    @Override
    public DataChoice newDataChoice()
    {
        return new DataChoiceImpl();
    }
    
    
    @Override
    public Count newCount()
    {
        return new CountImpl();
    }
    
    
    public Count newCount(DataType dataType)
    {
        return new CountImpl(dataType);
    }
    
    
    @Override
    public CategoryRange newCategoryRange()
    {
        return new CategoryRangeImpl();
    }
    
    
    @Override
    public QuantityRange newQuantityRange()
    {
        return new QuantityRangeImpl();
    }
    
    
    public QuantityRange newQuantityRange(DataType dataType)
    {
        return new QuantityRangeImpl(dataType);
    }
    
    
    @Override
    public Time newTime()
    {
        return new TimeImpl();
    }
    
    
    public Time newTime(DataType dataType)
    {
        return new TimeImpl(dataType);
    }
    
    
    @Override
    public TimeRange newTimeRange()
    {
        return new TimeRangeImpl();
    }
    
    
    public TimeRange newTimeRange(DataType dataType)
    {
        return new TimeRangeImpl(dataType);
    }
    
    
    @Override
    public net.opengis.swe.v20.Boolean newBoolean()
    {
        return new BooleanImpl();
    }
    
    
    @Override
    public Text newText()
    {
        return new TextImpl();
    }
    
    
    @Override
    public Category newCategory()
    {
        return new CategoryImpl();
    }
    
    
    @Override
    public Quantity newQuantity()
    {
        return new QuantityImpl();
    }
    
    
    public Quantity newQuantity(DataType dataType)
    {
        return new QuantityImpl(dataType);
    }
    
    
    @Override
    public CountRange newCountRange()
    {
        return new CountRangeImpl();
    }
    
    
    public CountRange newCountRange(DataType dataType)
    {
        return new CountRangeImpl(dataType);
    }
    
    
    @Override
    public NilValues newNilValues()
    {
        return new NilValuesImpl();
    }
    
    
    @Override
    public AllowedTokens newAllowedTokens()
    {
        return new AllowedTokensImpl();
    }
    
    
    @Override
    public AllowedValues newAllowedValues()
    {
        return new AllowedValuesImpl();
    }
    
    
    @Override
    public AllowedTimes newAllowedTimes()
    {
        return new AllowedTimesImpl();
    }
    
    
    @Override
    public XMLEncoding newXMLEncoding()
    {
        return new XMLEncodingImpl();
    }
    
    
    @Override
    public TextEncoding newTextEncoding()
    {
        return new TextEncodingImpl();
    }
    
    
    @Override
    public UnitReference newUnitReference()
    {
        return new UnitReferenceImpl();
    }
    
    
    @Override
    public NilValue newNilValue()
    {
        return new NilValueImpl();
    }
    
    
    @Override
    public EncodedValues newEncodedValuesProperty()
    {
        return new EncodedValuesImpl();
    }
}
