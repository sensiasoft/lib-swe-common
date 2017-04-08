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
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.vast.unit.Unit;
import org.w3c.dom.Element;


/**
 * <p>
 * TODO GMLUnitReader type description
 * </p>
 *
 * @author Alex Robin
 * @since Feb 9, 2007
 * */
public class GMLUnitReader
{
    protected final static String invalidISO = "Invalid Unit: ";
    
    
    public GMLUnitReader()
    {
    }
    
    
    public Unit readUnit(DOMHelper dom, Element unitElt) throws XMLReaderException
    {
        String eltName = unitElt.getLocalName();
        
        if (eltName.equals("UnitDefinition"))
            return readUnitDefinition(dom, unitElt);
        else if (eltName.equals("BaseUnit"))
            return readBaseUnit(dom, unitElt);
        else if (eltName.equals("DerivedUnit"))
            return readDerivedUnit(dom, unitElt);
        else if (eltName.equals("ConventionalUnit"))
            return readDerivedUnit(dom, unitElt);
        
        throw new XMLReaderException("Unsupported Unit Type: " + eltName, unitElt);
    }
    
    
    public Unit readUnitDefinition(DOMHelper dom, Element timeElt) throws XMLReaderException
    {
        readCommons(null, dom, timeElt);
        return null;
    }
    
    
    public Unit readBaseUnit(DOMHelper dom, Element timeElt) throws XMLReaderException
    {
        readCommons(null, dom, timeElt);
        return null;
    }
    
    
    public Unit readDerivedUnit(DOMHelper dom, Element timeElt) throws XMLReaderException
    {
        readCommons(null, dom, timeElt);
        return null;
    }
    
    
    public Unit readConventionalUnit(DOMHelper dom, Element timeElt) throws XMLReaderException
    {
        readCommons(null, dom, timeElt);
        return null;
    }
    
    
    private void readCommons(Unit unit, DOMHelper dom, Element timeElt) throws XMLReaderException
    {
        
    }
}
