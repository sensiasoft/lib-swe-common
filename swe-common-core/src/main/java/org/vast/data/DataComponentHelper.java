/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.

 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.com>
 
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
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @since Mar, 11 2008
 * @version 1.0
 */
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