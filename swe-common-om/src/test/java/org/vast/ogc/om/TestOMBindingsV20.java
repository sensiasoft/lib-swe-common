/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SWE Common Data Framework".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.om;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.custommonkey.xmlunit.Validator;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.vast.xml.DOMHelper;
import org.xml.sax.InputSource;


public class TestOMBindingsV20 extends XMLTestCase
{
    
    @Override
    public void setUp() throws Exception
    {
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setNormalizeWhitespace(true);
    }
    
    
    protected void validate(InputStream is, String schemaUrl) throws Exception
    {
        InputSource saxIs = new InputSource(is);
        Validator v = new Validator(saxIs);
        v.useXMLSchema(true);
        v.setJAXP12SchemaSource(schemaUrl);
        assertTrue(v.isValid());
    }
    
    
    protected void readWriteCompareOmXml(String path) throws Exception
    {
        OMUtils omUtils = new OMUtils(OMUtils.V2_0);
        
        InputStream is = getClass().getResourceAsStream(path);
        DOMHelper dom1 = new DOMHelper(is, false);
        IObservation obs1 = omUtils.readObservation(dom1, dom1.getBaseElement());
        is.close();
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.out.println();
        omUtils.writeObservation(System.out, obs1, "2.0");
        omUtils.writeObservation(os, obs1, "2.0");
        os.close();
        
        DOMHelper dom2 = new DOMHelper(new ByteArrayInputStream(os.toByteArray()), false);
        assertXMLEqual(dom1.getDocument(), dom2.getDocument());
    }
    
    
    public void testReadWriteObsScalar() throws Exception
    {
        readWriteCompareOmXml("examples_v20/sweScalarObservation4.xml");
    }
    
    
    public void testReadWriteObsRecord() throws Exception
    {
        readWriteCompareOmXml("examples_v20/sweRecordObservation3.xml");
        readWriteCompareOmXml("examples_v20/weatherObservation.xml");
    }
    
    
    public void testReadWriteObsArray() throws Exception
    {
        readWriteCompareOmXml("examples_v20/sweArrayObservation1.xml");
        readWriteCompareOmXml("examples_v20/sweArrayObservation2.xml");
    }
    
}
