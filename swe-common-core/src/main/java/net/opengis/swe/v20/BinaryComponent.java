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



/**
 * POJO class for XML type ComponentType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface BinaryComponent extends AbstractSWE, BinaryMember
{
    
    
    /**
     * Gets the encryption property
     */
    public String getEncryption();
    
    
    /**
     * Checks if encryption is set
     */
    public boolean isSetEncryption();
    
    
    /**
     * Sets the encryption property
     */
    public void setEncryption(String encryption);
    
    
    /**
     * Gets the significantBits property
     */
    public int getSignificantBits();
    
    
    /**
     * Checks if significantBits is set
     */
    public boolean isSetSignificantBits();
    
    
    /**
     * Sets the significantBits property
     */
    public void setSignificantBits(int significantBits);
    
    
    /**
     * Unsets the significantBits property
     */
    public void unSetSignificantBits();
    
    
    /**
     * Gets the bitLength property
     */
    public int getBitLength();
    
    
    /**
     * Checks if bitLength is set
     */
    public boolean isSetBitLength();
    
    
    /**
     * Sets the bitLength property
     */
    public void setBitLength(int bitLength);
    
    
    /**
     * Unsets the bitLength property
     */
    public void unSetBitLength();
    
    
    /**
     * Gets the byteLength property
     */
    public int getByteLength();
    
    
    /**
     * Checks if byteLength is set
     */
    public boolean isSetByteLength();
    
    
    /**
     * Sets the byteLength property
     */
    public void setByteLength(int byteLength);
    
    
    /**
     * Unsets the byteLength property
     */
    public void unSetByteLength();
    
    
    /**
     * Gets the dataType property
     */
    public String getDataType();
    
    
    /**
     * Sets the dataType property
     */
    public void setDataType(String dataType);
    
    
    public DataType getCdmDataType();
    
    
    public void setCdmDataType(DataType cdmDataType);
    
}
