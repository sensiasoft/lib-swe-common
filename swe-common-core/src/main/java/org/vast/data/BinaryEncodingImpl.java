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

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import net.opengis.HasCopy;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.BinaryBlock;
import net.opengis.swe.v20.BinaryMember;
import net.opengis.swe.v20.ByteEncoding;
import net.opengis.swe.v20.BinaryComponent;
import net.opengis.swe.v20.DataComponent;


/**
 * POJO class for XML type BinaryEncodingType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class BinaryEncodingImpl extends AbstractEncodingImpl implements BinaryEncoding
{
    static final long serialVersionUID = 1L;
    protected List<BinaryMember> memberList = new ArrayList<BinaryMember>();
    protected ByteOrder byteOrder;
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
    
    
    public static BinaryEncodingImpl getDefaultEncoding(DataComponent dataComponents)
    {
        BinaryEncodingImpl encoding = new BinaryEncodingImpl();
        encoding.byteEncoding = ByteEncoding.RAW;
        encoding.byteOrder = ByteOrder.BIG_ENDIAN;
        
        // use default encoding info for each data value
        ScalarIterator it = new ScalarIterator(dataComponents);
        while (it.hasNext())
        {
            DataComponent[] nextPath = it.nextPath();
            DataValue nextScalar = (DataValue)nextPath[nextPath.length-1];
            
            // build path (just use / for root)
            StringBuffer pathString = new StringBuffer();
            pathString.append(DataComponentHelper.PATH_SEPARATOR);
            for (int i = 1; i < nextPath.length; i++)
            {
                pathString.append(nextPath[i].getName());
                pathString.append(DataComponentHelper.PATH_SEPARATOR);
            }
            
            BinaryComponentImpl binaryOpts = new BinaryComponentImpl();
            binaryOpts.setCdmDataType(nextScalar.getDataType());
            binaryOpts.setRef(pathString.substring(0, pathString.length()-1));
            
            encoding.addMemberAsComponent(binaryOpts);
            nextScalar.setEncodingInfo(binaryOpts);
        }
        
        return encoding;
    }
}
