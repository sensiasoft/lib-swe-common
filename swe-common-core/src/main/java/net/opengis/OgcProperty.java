/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2014 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis;

import java.io.IOException;


public interface OgcProperty<ValueType>
{

    public OgcProperty<ValueType> copy();
    
    
    public String getName();


    public void setName(String name);


    public String getHref();


    public void setHref(String href);
    
    
    public boolean hasHref();


    public String getRole();


    public void setRole(String role);
    
    
    public String getArcRole();


    public void setArcRole(String role);
    
    
    public String getNilReason();


    public void setNilReason(String nilReason);


    public ValueType getValue();
    
    
    public boolean hasValue();


    public void setValue(ValueType value);
    
    
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