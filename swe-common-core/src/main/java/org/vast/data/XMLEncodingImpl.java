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

import net.opengis.swe.v20.XMLEncoding;


/**
 * POJO class for XML type XMLEncodingType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class XMLEncodingImpl extends AbstractEncodingImpl implements XMLEncoding
{
    private static final long serialVersionUID = 4491450481030614280L;
    String namespace;
    String prefix;
    
    
    public XMLEncodingImpl()
    {        
    }
    
    
    public XMLEncodingImpl(String namespace, String prefix)
    {
        this.namespace = namespace;
        this.prefix = prefix;
    }
    
    
    @Override
    public XMLEncodingImpl copy()
    {
        XMLEncodingImpl newObj = new XMLEncodingImpl(namespace, prefix);
        return newObj;
    }


    public String getNamespace()
    {
        return namespace;
    }


    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }


    public String getPrefix()
    {
        return prefix;
    }


    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }
}
