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

import java.util.ArrayList;
import java.util.List;
import net.opengis.HasCopy;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.BinaryBlock;
import net.opengis.swe.v20.BinaryMember;
import net.opengis.swe.v20.ByteEncoding;
import net.opengis.swe.v20.ByteOrder;
import net.opengis.swe.v20.BinaryComponent;


/**
 * POJO class for XML type BinaryEncodingType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class BinaryEncodingImpl extends AbstractEncodingImpl implements BinaryEncoding
{
    private static final long serialVersionUID = -4794883578805724635L;
    protected ArrayList<BinaryMember> memberList = new ArrayList<BinaryMember>();
    protected ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    protected ByteEncoding byteEncoding;
    protected Long byteLength;
    
    
    public BinaryEncodingImpl()
    {
    }
    
    
    @Override
    public BinaryEncodingImpl copy()
    {
        BinaryEncodingImpl newObj = new BinaryEncodingImpl();
        for (BinaryMember member: memberList)
            newObj.memberList.add((BinaryMember)((HasCopy)member).copy());
        newObj.byteOrder = this.byteOrder;
        newObj.byteEncoding = this.byteEncoding;
        newObj.byteLength = this.byteLength;
        return newObj;
    }
    
    
    /**
     * Gets the list of member properties
     */
    @Override
    public List<BinaryMember> getMemberList()
    {
        return memberList;
    }
    
    
    /**
     * Returns number of member properties
     */
    @Override
    public int getNumMembers()
    {
        if (memberList == null)
            return 0;
        return memberList.size();
    }
    
    
    /**
     * Adds a new memberAsComponent property
     */
    @Override
    public void addMemberAsComponent(BinaryComponent member)
    {
        this.memberList.add(member);
    }
    
    
    /**
     * Adds a new memberAsBlock property
     */
    @Override
    public void addMemberAsBlock(BinaryBlock member)
    {
        this.memberList.add(member);
    }
    
    
    /**
     * Gets the byteOrder property
     */
    @Override
    public ByteOrder getByteOrder()
    {
        return byteOrder;
    }
    
    
    /**
     * Sets the byteOrder property
     */
    @Override
    public void setByteOrder(ByteOrder byteOrder)
    {
        this.byteOrder = byteOrder;
    }
    
    
    /**
     * Gets the byteEncoding property
     */
    @Override
    public ByteEncoding getByteEncoding()
    {
        return byteEncoding;
    }
    
    
    /**
     * Sets the byteEncoding property
     */
    @Override
    public void setByteEncoding(ByteEncoding byteEncoding)
    {
        this.byteEncoding = byteEncoding;
    }
    
    
    /**
     * Gets the byteLength property
     */
    @Override
    public long getByteLength()
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
    public void setByteLength(long byteLength)
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
    
    
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("BinaryEncoding:");
        buf.append(" byteEncoding=").append(byteEncoding).append(',');
        buf.append(" byteOrder=").append(byteOrder).append(',');
        buf.append(" byteLength=").append(byteLength).append('\n');
        for (BinaryMember m: memberList)
            buf.append(m).append('\n');
        return buf.toString();
    }
}
