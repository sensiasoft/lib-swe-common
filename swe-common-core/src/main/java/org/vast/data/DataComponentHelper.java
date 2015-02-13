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

import org.vast.cdm.common.CDMException;
import net.opengis.swe.v20.BinaryComponent;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.BinaryMember;
import net.opengis.swe.v20.DataComponent;


/**
 * <p>
 * Provides helper methods to access components by name,
 * definition, full path, etc...
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Mar, 11 2008
 * */
public class DataComponentHelper
{
    public final static String PATH_SEPARATOR = "/";
    

	public static DataComponent findParameterByName(DataComponent parent, String name)
	{
		if (parent instanceof DataArrayImpl)
			parent = ((DataArrayImpl)parent).getArrayComponent();
		
		int childCount = parent.getComponentCount();
		for (int i=0; i<childCount; i++)
		{
			DataComponent child = parent.getComponent(i);
			String childName = child.getName();
			
			if (childName.equals(name))
				return child;
			
			// try to find it recursively!
			DataComponent desiredParam = findParameterByName(child, name);
			if (desiredParam != null)
				return desiredParam;
		}
		
		return null;
	}


	public static DataComponent findParameterByDefinition(DataComponent parent, String defUri)
	{
		if (parent instanceof DataArrayImpl)
			parent = ((DataArrayImpl)parent).getArrayComponent();
		
		int childCount = parent.getComponentCount();
		for (int i=0; i<childCount; i++)
		{
		    DataComponent child = parent.getComponent(i);
			String childDef = ((DataComponent)child).getDefinition();
			
			if (childDef != null && childDef.equals(defUri))
				return child;
			
			// try to find it recursively!
			DataComponent desiredParam = findParameterByDefinition(child, defUri);
			if (desiredParam != null)
				return desiredParam;
		}
		
		return null;
	}
	
	
	public static DataComponent findComponentByPath(String path, DataComponent parent) throws CDMException
	{
	    try
        {
            return findComponentByPath(path.split(PATH_SEPARATOR), parent);
        }
        catch (CDMException e)
        {
            throw new CDMException("Unknown component " + path);
        }
	}
	
	
	public static DataComponent findComponentByPath(String [] dataPath, DataComponent parent) throws CDMException
    {
        DataComponent data = parent;
        
        for (int i=0; i<dataPath.length; i++)
        {
            String pathElt = dataPath[i];
            if (pathElt.length() == 0) // a leading '/' create an empty array element
                continue;
            
            data = data.getComponent(pathElt);
            if (data == null)
                throw new CDMException("Unknown component " + pathElt);
        }
        
        return data;
    }
	
	
	/**
     * Maps binary components and blocks definitions to the actual data component.
     * This sets the encodingInfo attribute of the component so it can be used.
     * For scalars, it also sets the default data type so it is the same as in the encoded stream.
	 * @param dataComponents 
	 * @param dataEncoding 
	 * @throws CDMException 
     */
    public static void resolveComponentEncodings(DataComponent dataComponents, BinaryEncoding dataEncoding) throws CDMException
    {
        for (BinaryMember binaryOpts: dataEncoding.getMemberList())
        {
            DataComponent comp = DataComponentHelper.findComponentByPath(binaryOpts.getRef(), dataComponents);
            ((AbstractDataComponentImpl)comp).setEncodingInfo(binaryOpts);
            
            // for scalars, also set default data type
            if (binaryOpts instanceof BinaryComponent)
                ((AbstractSimpleComponentImpl)comp).setDataType(((BinaryComponentImpl)binaryOpts).getCdmDataType());
        }
    }
}