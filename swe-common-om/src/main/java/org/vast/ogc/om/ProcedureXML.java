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

package org.vast.ogc.om;

import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;


/**
 * <p>
 * Wrapper class to store procedure info as XML DOM. It can temporary hold a DOM
 * Node until it is parsed by an adequate reader into a fully functional Procedure
 * object.
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @since Sep 30, 2012
 * @version 1.0
 */
public class ProcedureXML implements IProcedure
{
    protected Element domElt;
    protected IProcedure procedure;
    
    
    public ProcedureXML(Element domElt)
    {
        this.domElt = domElt;
    }
    
    
    public Element getDomElement()
    {
        return domElt;
    }
       
    
    @Override
    public QName getQName()
    {
        return new QName(domElt.getNamespaceURI(), domElt.getLocalName());
    }


    public String getLocalId()
    {
        if (procedure == null)
            throw getException();
        else
            return procedure.getLocalId();
    }


    public void setLocalId(String id)
    {
        if (procedure == null)
            throw getException();
        else
            procedure.setLocalId(id);
    }


    public String getIdentifier()
    {
        if (procedure == null)
            throw getException();
        else
            return procedure.getIdentifier();
    }


    public void setIdentifier(String uid)
    {
        if (procedure == null)
            throw getException();
        else
            procedure.setIdentifier(uid);
    }


    public String getDescription()
    {
        if (procedure == null)
            throw getException();
        else
            return procedure.getDescription();
    }


    public void setDescription(String desc)
    {
        if (procedure == null)
            throw getException();
        else
            procedure.setDescription(desc);
    }


    public String getName()
    {
        if (procedure == null)
            throw getException();
        else
            return procedure.getName();
    }


    public void setName(String name)
    {
        if (procedure == null)
            throw getException();
        else
            procedure.setName(name);
    }


    public List<QName> getNames()
    {
        if (procedure == null)
            throw getException();
        else
            return procedure.getNames();
    }


    public void addName(QName name)
    {
        if (procedure == null)
            throw getException();
        else
            procedure.addName(name);
    }


    public Map<QName, Object> getProperties()
    {
        if (procedure == null)
            throw getException();
        else
            return procedure.getProperties();
    }


    public Object getProperty(String name)
    {
        if (procedure == null)
            throw getException();
        else
            return procedure.getProperty(name);
    }


    public void setProperty(String name, Object value)
    {
        if (procedure == null)
            throw getException();
        else
            procedure.setProperty(name, value);
    }


    public Object getProperty(QName qname)
    {
        if (procedure == null)
            throw getException();
        else
            return procedure.getProperty(qname);
    }


    public void setProperty(QName qname, Object value)
    {
        if (procedure == null)
            throw getException();
        else
            procedure.setProperty(qname, value);
    }
    
    
    protected RuntimeException getException() 
    {
        return new UnsupportedOperationException("Method not supported because XML content hasn't been parsed yet.");
    }
}
