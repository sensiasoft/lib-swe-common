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

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * Interface for basic term definitions
 * </p>
 *
 * @author Alex Robin
 * @since Sep 28, 2012
 * */
public interface IDefinition extends Serializable
{
    public String getIdentifier();
    
    public void setIdentifier(String uid);
    
    public List<String> getNames();
    
    public String addName(String name);
    
    public String getDescription();
    
    public void setDescription();
}
