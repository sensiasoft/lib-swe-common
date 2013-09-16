/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Initial Developer of the Original Code is SENSIA SOFTWARE LLC.
 Portions created by the Initial Developer are Copyright (C) 2012
 the Initial Developer. All Rights Reserved.

 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> for more
 information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.def;

import java.util.List;
import org.vast.ogc.xlink.CachedReference;


/**
 * <p>
 * TODO DefinitionRef type description
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @since Sep 29, 2012
 * @version 1.0
 */
public class DefinitionRef extends CachedReference<IDefinition> implements IDefinition
{

    public DefinitionRef()
    {
    }
    
    
    public DefinitionRef(String href)
    {
        setHref(href);
    }
    
    
    @Override
    public String getIdentifier()
    {
        return getTarget().getIdentifier();
    }


    @Override
    public void setIdentifier(String uid)
    {
        getTarget().setIdentifier(uid);
    }


    @Override
    public List<String> getNames()
    {
        return getTarget().getNames();
    }


    @Override
    public String addName(String name)
    {
        return getTarget().addName(name);
    }


    @Override
    public String getDescription()
    {
        return getTarget().getDescription();
    }


    @Override
    public void setDescription()
    {
        getTarget().setDescription();
    }
}
