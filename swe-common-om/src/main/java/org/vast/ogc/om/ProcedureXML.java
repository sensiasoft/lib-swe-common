/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.

 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or 
 Mike Botts <mike.botts@botts-inc.net for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.om;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyList;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.CodeWithAuthority;
import net.opengis.gml.v32.Envelope;
import net.opengis.gml.v32.Reference;
import org.w3c.dom.Element;


/**
 * <p>
 * Wrapper class to store procedure info as XML DOM. It can temporary hold a DOM
 * Node until it is parsed by an adequate reader into a fully functional Procedure
 * object.
 * </p>
 *
 * @author Alex Robin
 * @since Sep 30, 2012
 * */
public class ProcedureXML implements IProcedure
{
    private static final long serialVersionUID = -1978010581789300273L;
    protected transient Element domElt;
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
            return procedure.getId();
    }


    public void setLocalId(String id)
    {
        if (procedure == null)
            throw getException();
        else
            procedure.setId(id);
    }


    @Override
    public String getUniqueIdentifier()
    {
        if (procedure == null)
            throw getException();
        else
            return procedure.getUniqueIdentifier();
    }


    public void setIdentifier(String uid)
    {
        if (procedure == null)
            throw getException();
        else
            procedure.setUniqueIdentifier(uid);
    }


    @Override
    public String getDescription()
    {
        if (procedure == null)
            throw getException();
        else
            return procedure.getDescription();
    }


    @Override
    public void setDescription(String desc)
    {
        if (procedure == null)
            throw getException();
        else
            procedure.setDescription(desc);
    }


    @Override
    public String getName()
    {
        if (procedure == null)
            throw getException();
        else
            return procedure.getName();
    }


    @Override
    public void setName(String name)
    {
        if (procedure == null)
            throw getException();
        else
            procedure.setName(name);
    }



    @Override
    public Map<QName, Object> getProperties()
    {
        if (procedure == null)
            throw getException();
        else
            return procedure.getProperties();
    }


    @Override
    public Object getProperty(String name)
    {
        if (procedure == null)
            throw getException();
        else
            return procedure.getProperty(name);
    }


    @Override
    public void setProperty(String name, Object value)
    {
        if (procedure == null)
            throw getException();
        else
            procedure.setProperty(name, value);
    }


    @Override
    public Object getProperty(QName qname)
    {
        if (procedure == null)
            throw getException();
        else
            return procedure.getProperty(qname);
    }


    @Override
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


    @Override
    public Envelope getBoundedBy()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public boolean isSetBoundedBy()
    {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public void setBoundedByAsEnvelope(Envelope boundedBy)
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public AbstractGeometry getLocation()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public OgcProperty<AbstractGeometry> getLocationProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public boolean isSetLocation()
    {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public void setLocation(AbstractGeometry location)
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public OgcPropertyList<Serializable> getMetaDataPropertyList()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public boolean isSetDescription()
    {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public Reference getDescriptionReference()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public boolean isSetDescriptionReference()
    {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public void setDescriptionReference(Reference descriptionReference)
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public CodeWithAuthority getIdentifier()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public boolean isSetIdentifier()
    {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public void setIdentifier(CodeWithAuthority identifier)
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void setUniqueIdentifier(String identifier)
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public List<CodeWithAuthority> getNameList()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public int getNumNames()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public void addName(CodeWithAuthority name)
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public String getId()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void setId(String id)
    {
        // TODO Auto-generated method stub
        
    }
}
