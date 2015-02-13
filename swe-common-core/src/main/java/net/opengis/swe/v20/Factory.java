/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

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
}
