/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
 Alexandre Robin <robin@nsstc.uah.edu>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.util;

/**
 * <p>
 * SensorML Contact
 * </p>
 *
 * @author Alex Robin
 * @since Feb 16, 2006
 * */
public abstract class Contact
{
    protected String role;
    protected String roleCodeList;
    protected String hrefUri;
    protected boolean hrefPresent = false;
    

    public String getRole()
    {
        return role;
    }


    public void setRole(String role)
    {
        this.role = role;
    }


    public String getRoleCodeList()
    {
        return roleCodeList;
    }


    public void setRoleCodeList(String roleCodeList)
    {
        this.roleCodeList = roleCodeList;
    }


    public boolean isHrefPresent()
    {
        return hrefPresent;
    }


    public void setHrefPresent(boolean hrefPresent)
    {
        this.hrefPresent = hrefPresent;
    }


    public String getHrefUri()
    {
        return hrefUri;
    }


    public void setHrefUri(String hrefUri)
    {
        this.hrefUri = hrefUri;
    }
}
