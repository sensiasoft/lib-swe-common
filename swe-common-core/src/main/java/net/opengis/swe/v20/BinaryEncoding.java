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

import java.util.List;


/**
 * POJO class for XML type BinaryEncodingType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface BinaryEncoding extends DataEncoding
{
    
    
    /**
     * Gets the list of member properties
     */
    public List<BinaryMember> getMemberList();
    
    
    /**
     * Returns number of member properties
     */
    public int getNumMembers();
    
    
    /**
     * Adds a new memberAsComponent property
     */
    public void addMemberAsComponent(BinaryComponent member);
    
    
    /**
     * Adds a new memberAsBlock property
     */
    public void addMemberAsBlock(BinaryBlock member);
    
    
    /**
     * Gets the byteOrder property
     */
    public ByteOrder getByteOrder();
    
    
    /**
     * Sets the byteOrder property
     */
    public void setByteOrder(ByteOrder byteOrder);
    
    
    /**
     * Gets the byteEncoding property
     */
    public ByteEncoding getByteEncoding();
    
    
    /**
     * Sets the byteEncoding property
     */
    public void setByteEncoding(ByteEncoding byteEncoding);
    
    
    /**
     * Gets the byteLength property
     */
    public long getByteLength();
    
    
    /**
     * Checks if byteLength is set
     */
    public boolean isSetByteLength();
    
    
    /**
     * Sets the byteLength property
     */
    public void setByteLength(long byteLength);
    
    
    /**
     * Unsets the byteLength property
     */
    public void unSetByteLength();
}
