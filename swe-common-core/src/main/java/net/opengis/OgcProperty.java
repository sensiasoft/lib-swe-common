/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis;

import java.io.IOException;
import java.io.Serializable;
import org.vast.ogc.xlink.IXlinkReference;


public interface OgcProperty<ValueType> extends IXlinkReference<ValueType>, Serializable
{

    public OgcProperty<ValueType> copy();
    
    
    public String getName();


    public void setName(String name);
    
    
    public String getTitle();


    public void setTitle(String title);
    
    
    public String getNilReason();


    public void setNilReason(String nilReason);


    public ValueType getValue();
    
    
    public boolean hasValue();


    public void setValue(ValueType value);
    
    
    public boolean hasHref();
    
    
    /**
     * Fetches remote value from xlink href
     * This method does nothing if property value is not null
     * @return true if content was successfully fetched
     * @throws IOException if href URL cannot be reached
     */
    public boolean resolveHref() throws IOException;
    
    
    /**
     * Assigns an instance of HrefResolver that is responsible for connecting
     * to the href URL, fetch the data and parse it with the appropriate parser
     * @param hrefResolver
     */
    public void setHrefResolver(HrefResolver hrefResolver);

}