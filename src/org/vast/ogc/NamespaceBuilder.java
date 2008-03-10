/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
 Alexandre Robin <alexandre.robin@spotimage.fr>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc;


/**
 * <p><b>Title:</b>
 * Namespace Builder
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Default implementation of a namespace builder that
 * provides namespace URI given an OGC Spec name and the version
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date 18 oct. 07
 * @version 1.0
 */
public class NamespaceBuilder
{
    protected String baseUri;
    protected boolean appendVersion;
    protected String appendVersionFrom; // TODO take that into account


    public NamespaceBuilder(String baseUri, String appendVersionFrom)
    {
        this.baseUri = baseUri;
        this.appendVersionFrom = appendVersionFrom;
        if (appendVersionFrom != null)
            appendVersion = true;
    }


    public String getNsUri(String version)
    {
        if (appendVersion && version != null)
        {
        	// keep only two first digits of version number!
        	int endIndex = version.indexOf('.');
        	endIndex = version.indexOf('.', endIndex+1);
        	if (endIndex > 0)
        		version = version.substring(0, endIndex);
        	return baseUri + "/" + version;
        }
        else
            return baseUri;
    }
}
